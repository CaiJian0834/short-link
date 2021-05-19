package com.cxj.link.database.ds;

/**
 * 动态数据源  切换用
 *
 * @author cxj
 */
public class DataSourceContext {

    private DataSourceContext() {
    }

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setCustomerType(String customerType) {
        contextHolder.set(customerType);
    }

    public static String getCustomerType() {
        return contextHolder.get();
    }

    public static void clearCustomerType() {
        contextHolder.remove();
    }
}
