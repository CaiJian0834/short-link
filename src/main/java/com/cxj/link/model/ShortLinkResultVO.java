package com.cxj.link.model;

import lombok.Data;

/**
 * 短链接生成结果
 *
 * @author cxj
 * @emall 735374036@qq.com
 */
@Data
public class ShortLinkResultVO {

    /**
     * 网址链接
     */
    private String url;

    /**
     * hash码
     */
    private String hash;

    public ShortLinkResultVO() {
    }

    public ShortLinkResultVO(String url) {
        this.url = url;
    }

    public ShortLinkResultVO(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }
}
