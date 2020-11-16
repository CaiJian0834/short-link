package com.cxj.link.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxj.link.annotation.ShardingDataSource;
import com.cxj.link.constant.NumberConstant;
import com.cxj.link.constant.RedisKeyConstant;
import com.cxj.link.entity.ShortLinkUrlHistoryEntity;
import com.cxj.link.mapper.ShortLinkUrlHistoryMapper;
import com.cxj.link.model.ApiResultModel;
import com.cxj.link.model.ShortLinkInfoVO;
import com.cxj.link.model.ShortLinkRequestDTO;
import com.cxj.link.model.ShortLinkResultVO;
import com.cxj.link.service.RedisService;
import com.cxj.link.service.ShortUrlService;
import com.cxj.link.util.DateUtil;
import com.cxj.link.util.LoadingCacheUtil;
import com.cxj.link.util.LogExceptionStackTrace;
import com.cxj.link.util.MathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.MurmurHash3;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ShortUrlServiceImpl implements ShortUrlService {

    /**
     * 后缀
     */
    private static final String URL_SUFFIX = "[SUFFIX]";


    @Autowired
    private ShortLinkUrlHistoryMapper shortlinkUrlHistoryMapper;
    @Autowired
    private RedisService redisService;


    /**
     * 短链接域名
     */
    @Value("${cxj.short.link.host}")
    private String CXJ_SHORT_LINK_HOST;
    /**
     * 短链接 protocol
     */
    @Value("${cxj.short.link.protocol:https}")
    private String CXJ_SHORT_LINK_PROTOCOL;
    /**
     * hash冲突时的重试次数
     */
    @Value("${cxj.short.link.retry.number:5}")
    private Integer CXJ_SHORT_LINK_RETRY_NUMBER;
    /**
     * 是否使用redis布隆过滤器
     */
    @Value("${cxj.short.link.redis.bloom:false}")
    private Boolean CXJ_SHORT_LINK_REDIS_BLOOM;

    /**
     * 生成短链接
     *
     * @param url
     * @return
     */
    @Override
    public String urlToHash(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("请输入正确url");
        }
        long hash = MurmurHash3.hash32x86(url.getBytes());
        return MathUtils._10_to_62(hash);
    }

    /**
     * 保存
     *
     * @param entity
     */
    private void save(ShortLinkUrlHistoryEntity entity) {
        //入库
        entity.setHashUrl(CXJ_SHORT_LINK_HOST + entity.getHashValue());
        entity.setStatus(NumberConstant.STATUS_YES);
        entity.setCreateTime(new Date());
        shortlinkUrlHistoryMapper.insert(entity);
    }


    @Override
    public ApiResultModel<ShortLinkResultVO> create(ShortLinkRequestDTO dto) {

        // 数据校验
        ApiResultModel verify = verify(dto);
        if (!verify.isSuccess()) {
            return verify;
        }

        // 去除前后空格
        String url = StringUtils.trim(dto.getUrl()).toLowerCase();

        //判断 url 是否是Http https 开头，否则去拼接默认的protocol
        if (!isStartWithHttpOrHttps(url)) {
            url = appendHttp2Head(url, CXJ_SHORT_LINK_PROTOCOL);
        }

        //计算多差次拼接才能生成不重复的 hash value
        // hash码
        String hash = "";
        // 重试次数
        int count = 0;

        while (true) {
            if (count > CXJ_SHORT_LINK_RETRY_NUMBER) {
                return ApiResultModel.error("超过最大重试次数，请稍后重新请求");
            }

            hash = urlToHash(url);

            // 是否存在
            ShortLinkUrlHistoryEntity dbShortUrl = getEntByHash(hash);

            if (dbShortUrl == null) {
                break;
            }

            //hash 相同且长链接相同
            if (dbShortUrl.getReallyUrl().equals(url)) {
                ShortLinkResultVO resultApiDTO = new ShortLinkResultVO();
                resultApiDTO.setUrl(dbShortUrl.getHashUrl());
                resultApiDTO.setHash(dbShortUrl.getHashValue());
                return ApiResultModel.success(resultApiDTO);
            } else {
                // 添加后缀 进行重试
                url += URL_SUFFIX;
            }
            count++;
            log.warn("重试，次数:[{}]", count);
        }
        String lockKey = "";
        RLock lock = redisService.getLock(lockKey);

        try {
            // 加分布式锁进行 写库
            lock.lockInterruptibly(NumberConstant.REDIS_EXPIRE_SECOND_3, TimeUnit.SECONDS);

            // 校验并发下hash存在重复的情况
            ShortLinkUrlHistoryEntity dbShortUrl = getEntByHash(hash);
            if (dbShortUrl != null) {
                if (dbShortUrl.getReallyUrl().equals(url)) {
                    ShortLinkResultVO resultApiDTO = new ShortLinkResultVO();
                    resultApiDTO.setUrl(dbShortUrl.getHashUrl());
                    resultApiDTO.setHash(dbShortUrl.getHashValue());
                    return ApiResultModel.success(resultApiDTO);
                } else {
                    log.error("短链接生成失败");
                    return ApiResultModel.error("短链接生成失败，请重试!");
                }
            }

            ShortLinkUrlHistoryEntity entity = new ShortLinkUrlHistoryEntity();
            entity.setHashValue(hash);
            entity.setReallyUrl(dto.getUrl());
            if (dto.getEndTime() == null) {
                dto.setEndTime(NumberConstant.INT_REDIS_EXPIRE_DAY_30);
            }
            Date endTime = DateUtil.moveMinit(new Date(), dto.getEndTime(), false);
            entity.setEndTime(endTime);
            entity.setEndTimeNumber(dto.getEndTime());
            // save db
            save(entity);

            // set cache
            setCache(entity);

            // 返回
            ShortLinkResultVO resultApiDTO = new ShortLinkResultVO();
            resultApiDTO.setUrl(entity.getHashUrl());
            resultApiDTO.setHash(entity.getHashValue());
            return ApiResultModel.success(resultApiDTO);
        } catch (Exception e) {
            log.error("短链接生成失败，e={}", LogExceptionStackTrace.erroStackTrace(e));
            return ApiResultModel.error("短链接生成失败，请重试!");
        } finally {
            lock.unlock();
        }
    }

    @ShardingDataSource(db = ShardingDataSource.READ)
    @Override
    public ApiResultModel<ShortLinkInfoVO> getByHash(String hash) {

        if (StringUtils.isEmpty(hash)) {
            log.warn("参数错误，hashValue不能为空");
            return ApiResultModel.error("参数错误");
        }

        ShortLinkInfoVO vo = getReallyByHash(hash);

        return ApiResultModel.success(vo);
    }

    @ShardingDataSource(db = ShardingDataSource.READ)
    @Override
    public ShortLinkInfoVO getReallyByHash(String hash) {

        ShortLinkUrlHistoryEntity entity = getEntByHash(hash);

        if (entity == null) {
            return null;
        }

        ShortLinkInfoVO vo = new ShortLinkInfoVO();
        vo.setId(entity.getId());
        vo.setUrl(entity.getReallyUrl());
        vo.setEndTime(entity.getEndTime());

        return vo;
    }

    /**
     * 数据校验
     *
     * @param dto
     * @return
     */
    private ApiResultModel verify(ShortLinkRequestDTO dto) {

        // 数据校验
        if (StringUtils.isEmpty(dto.getUrl())) {
            log.warn("参数错误，Url 为空,dto={}", JSON.toJSONString(dto));
            return ApiResultModel.error("参数错误，Url 为空");
        }

        // 有效期
        if (dto.getEndTime() != null && dto.getEndTime() <= 0) {
            log.warn("参数错误，有效期不能小于等于0,dto={}", JSON.toJSONString(dto));
            return ApiResultModel.error("参数错误，有效期不能小于等于0");
        }

        // 验证url地址是否合法
        if (!isValidUrl(dto.getUrl())) {
            log.warn("无效的url:[{}]", dto.getUrl());
            return ApiResultModel.error("无效的url");
        }

        return ApiResultModel.success();
    }


    /**
     * 获取表对象
     * 走缓存
     * @param hash
     * @return
     */
    private ShortLinkUrlHistoryEntity getEntByHash(String hash) {

        if (StringUtils.isEmpty(hash)) {
            return null;
        }
        String value = "";
        String hashKey = String.format(RedisKeyConstant.CXJ_SHORT_LINK_URL_HASH, hash);

        // 获取本地缓存
        value = LoadingCacheUtil.get(hashKey);

        if (StringUtils.isNotEmpty(value)) {
            return JSON.parseObject(value, ShortLinkUrlHistoryEntity.class);
        }

        // 获取redis缓存
        value = redisService.get(hashKey);

        if (StringUtils.isNotEmpty(value)) {
            LoadingCacheUtil.set(hashKey, value);
            return JSON.parseObject(value, ShortLinkUrlHistoryEntity.class);
        }

        // 布隆过滤器判断key是否存在
        if (CXJ_SHORT_LINK_REDIS_BLOOM) {
            boolean bloomFlag = redisService.bfExists(RedisKeyConstant.CXJ_SHORT_LINK_URL_BLOOM_FILTER, hash);
            if (!bloomFlag) {
                return null;
            }
        }

        // 获取db信息
        ShortLinkUrlHistoryEntity entity = get(hash);

        if (entity == null) {
            return null;
        }

        value = JSON.toJSONString(entity);

        LoadingCacheUtil.set(hashKey, value);
        redisService.set(hashKey, value);

        return entity;
    }

    /**
     * 获取表对象
     * 走db
     * @param hash
     * @return
     */
    private ShortLinkUrlHistoryEntity get(String hash){

        QueryWrapper<ShortLinkUrlHistoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ShortLinkUrlHistoryEntity::getHashValue,hash);
        queryWrapper.lambda().eq(ShortLinkUrlHistoryEntity::getStatus,NumberConstant.STATUS_YES);
        queryWrapper.lambda().ge(ShortLinkUrlHistoryEntity::getEndTime,NumberConstant.STATUS_YES);

        List<ShortLinkUrlHistoryEntity> list = shortlinkUrlHistoryMapper.selectList(queryWrapper);

        if (list == null || NumberConstant.INT_0.equals(list.size())) {
            return null;
        }

        return list.get(NumberConstant.INT_0);
    }



    /**
     * 设置缓存
     *
     * @param entity
     */
    private void setCache(ShortLinkUrlHistoryEntity entity) {

        // 根据配置是否使用 布隆过滤器
        if (CXJ_SHORT_LINK_REDIS_BLOOM) {
            // 设置布隆过滤器
            redisService.bfAdd(RedisKeyConstant.CXJ_SHORT_LINK_URL_BLOOM_FILTER, entity.getHashValue());
        }

        // 设置redis缓存
        String hashKey = String.format(RedisKeyConstant.CXJ_SHORT_LINK_URL_HASH, entity.getHashValue());
        String value = JSON.toJSONString(entity);
        redisService.setex(hashKey, value, Long.valueOf(entity.getEndTimeNumber()), TimeUnit.MINUTES);

        // 设置本地缓存
        LoadingCacheUtil.set(hashKey, value);
    }

    /**
     * 是否是http 或 http开头
     *
     * @param url
     * @return
     */
    private boolean isStartWithHttpOrHttps(String url) {
        String regex = "^((https|http)?://)";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(url);
        return matcher.find();
    }

    /**
     * url 开头拼接 http
     *
     * @param url
     * @return
     */
    private String appendHttp2Head(String url, String prefix) {
        StringBuilder stringBuilder = new StringBuilder(prefix).append("://");
        stringBuilder.append(url);
        return stringBuilder.toString();
    }


    /**
     * 是否是有效的 url
     *
     * @param urls
     * @return
     */
    public boolean isValidUrl(String urls) {
        boolean isurl = false;
        String regex = "^(http(s)?:\\/\\/)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:[0-9]{1,5})?[-a-zA-Z0-9()@:%_\\\\\\+\\.~#?&//=]*$";
        //对比
        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(urls.trim());
        //判断是否匹配
        isurl = mat.matches();
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }
}

