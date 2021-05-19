package com.cxj.link.constant;

/**
 * @author cxj
 * @emall 735374036@qq.com
 */
public class RedisKeyConstant {

    private RedisKeyConstant() {
    }

    /**
     * 短链接
     * 已生成缓存
     * hashcode
     */
    public static final String CXJ_SHORT_LINK_URL_HASH = "cxj:short:link:url:ent:%s";

    /**
     * 短链接
     * blom filter
     */
    public static final String CXJ_SHORT_LINK_URL_BLOOM_FILTER = "cxj:short:link:url:bloom:filter";

    /**
     * 短链接
     * 访问统计
     * id
     */
    public static final String CXJ_SHORT_LINK_URL_HASH_STATISTICS = "cxj:short:link:url:statistics:%s";

}
