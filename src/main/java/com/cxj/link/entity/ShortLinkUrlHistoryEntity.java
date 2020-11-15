package com.cxj.link.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 短链接生成记录
 *
 * @author cxj
 * @emall 735374036@qq.com
 **/
@TableName("shortlink_url_history")
public class ShortLinkUrlHistoryEntity implements Serializable {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * hash码
     */
    @TableField(value = "hash_value")
    private String hashValue;

    /**
     * 短链接
     */
    @TableField(value = "hash_url")
    private String hashUrl;

    /**
     * 原url
     */
    @TableField(value = "really_url")
    private String reallyUrl;

    /**
     * 是否有效 1 有效 0 无效
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 失效时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 失效时间 单位：分钟
     */
    @TableField(value = "end_time_number")
    private Integer endTimeNumber;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public String getHashUrl() {
        return hashUrl;
    }

    public void setHashUrl(String hashUrl) {
        this.hashUrl = hashUrl;
    }

    public String getReallyUrl() {
        return reallyUrl;
    }

    public void setReallyUrl(String reallyUrl) {
        this.reallyUrl = reallyUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getEndTimeNumber() {
        return endTimeNumber;
    }

    public void setEndTimeNumber(Integer endTimeNumber) {
        this.endTimeNumber = endTimeNumber;
    }
}
