package com.cxj.link.database.exchange;


/**
 * 自定义分库接口
 *
 * @author cxj
 */
public interface DataBaseShardingService {

    /**
     * 路由方式
     * 子类实现
     *
     * @param params 参数列表
     * @return
     */
    String exchange(Object[] params);

}