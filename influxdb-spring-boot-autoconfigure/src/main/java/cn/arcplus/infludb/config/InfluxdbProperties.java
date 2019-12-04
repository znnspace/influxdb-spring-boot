package cn.arcplus.infludb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = InfluxdbProperties.INFLUXDB_PREFIX)
public class InfluxdbProperties {

    public static final String INFLUXDB_PREFIX = "spring.influxdb";

    /**
     * InfluxdbProperties 开关
     */
    boolean enable = false;

    /**
     * Influxdb的url
     * eg:127.0.0.1:8086
     */
    private String url;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 库名
     */
    private String database;

    /**
     * 数据保存策略
     */
    private String retentionPolicy;
}
