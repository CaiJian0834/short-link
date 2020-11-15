package com.cxj.link.service;

import com.cxj.link.model.ApiResultModel;
import com.cxj.link.model.ShortLinkInfoVO;
import com.cxj.link.model.ShortLinkRequestDTO;
import com.cxj.link.model.ShortLinkResultVO;


public interface ShortUrlService {
    /**
     * 原链接生成hash码
     *
     * @param url
     * @return
     */
    String urlToHash(String url);

    /**
     * 创建短链接
     *
     * @param dto
     * @return
     */
    ApiResultModel<ShortLinkResultVO> create(ShortLinkRequestDTO dto);

    /**
     * 根据hashValue获取真实链接
     *
     * @param hash
     * @return
     */
    ApiResultModel<ShortLinkInfoVO> getByHash(String hash);

    /**
     * 根据hashValue获取真实链接
     *
     * @param hash
     * @return
     */
    ShortLinkInfoVO getReallyByHash(String hash);


}

