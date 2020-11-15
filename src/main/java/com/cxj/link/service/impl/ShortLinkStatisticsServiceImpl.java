package com.cxj.link.service.impl;

import com.cxj.link.constant.RedisKeyConstant;
import com.cxj.link.service.RedisService;
import com.cxj.link.service.ShortLinkStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 调用统计
 *
 * @author cxj
 * @emall 735374036@qq.com
 */
@Component("shortLinkStatisticsService")
public class ShortLinkStatisticsServiceImpl implements ShortLinkStatisticsService {

    @Autowired
    private RedisService redisService;


    @Override
    public void incr(Long id) {
        String hashKey = String.format(RedisKeyConstant.CXJ_SHORT_LINK_URL_HASH_STATISTICS, id);
        redisService.incr(hashKey);
    }

}
