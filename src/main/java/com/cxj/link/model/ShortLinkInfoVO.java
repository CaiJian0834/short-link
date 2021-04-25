package com.cxj.link.model;

import java.util.Date;

/**
 * @author cxj
 * @emall 735374036@qq.com
 */
public class ShortLinkInfoVO {

    private Long id;
    /**
     * 真实链接
     */
    private String url;

    /**
     * 失效时间
     */
    private Date endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
