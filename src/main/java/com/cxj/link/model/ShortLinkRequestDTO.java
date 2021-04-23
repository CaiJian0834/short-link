package com.cxj.link.model;

/**
 * 短链接生成入参
 *
 * @author cxj
 * @emall 735374036@qq.com
 */
public class ShortLinkRequestDTO {

    /**
     * 原链接
     */
    private String url;

    /**
     * 有效期时间，单位分钟，默认30天
     */
    private Integer endTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }
}
