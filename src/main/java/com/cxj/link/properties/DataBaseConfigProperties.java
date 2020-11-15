package com.cxj.link.properties;

/**
 * 数据库
 * 连接配置
 *
 * @author cxj
 * @date 2020/8/17
 */
public class DataBaseConfigProperties {

    /**
     * 是否默认数据库
     */
    private boolean defaultTarget = false;

    /**
     * 数据库连接地址
     */
    private String url;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 驱动
     */
    private String driverClassName;


    public boolean isDefaultTarget() {
        return defaultTarget;
    }

    public void setDefaultTarget(boolean defaultTarget) {
        this.defaultTarget = defaultTarget;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

}
