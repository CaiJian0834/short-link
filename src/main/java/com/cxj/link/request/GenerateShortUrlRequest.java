package com.cxj.link.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 入参model
 *
 * @author caijian
 */
@Data
public class GenerateShortUrlRequest {
    /**
     * 要生成的短链接地址
     */
    @NotNull
    private String url;

}
