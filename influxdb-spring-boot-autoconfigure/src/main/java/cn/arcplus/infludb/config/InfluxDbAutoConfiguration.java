package cn.arcplus.infludb.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "spring.influxdb.enable", havingValue = "true")
@ConditionalOnClass(InfluxDB.class)
@EnableConfigurationProperties(InfluxdbProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class InfluxDbAutoConfiguration {

    @Autowired
    private InfluxdbProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public InfluxDB influxDB() throws Exception {
        return influxDbBuild();
    }

    /**
     * 连接数据库，若不存在则创建
     *
     * @return influxDb实例
     */
    private InfluxDB influxDbBuild() throws Exception {
        InfluxDB influxDB = InfluxDBFactory.connect(properties.getUrl(), properties.getUserName(), properties.getPassword());
        try {
            createDB(properties.getDatabase(), influxDB);
            influxDB.setDatabase(properties.getDatabase());
//            BiConsumer<Iterable<Point>, Throwable> exceptionHandler = (batch, exception) -> {
//                // 批量插入失败
//                log.error("influxDB批量插入失败，请查看: {}", exception.getMessage());
//            };
//            // 设置批量插入，满足条件1000条开始插入，每一秒插入一次
//            BatchOptions options = BatchOptions.DEFAULTS.bufferLimit(5000).actions(100).flushDuration(1000).jitterDuration(20).exceptionHandler(exceptionHandler);
//            // 开启批量插入
//            influxDB.enableBatch(options);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            influxDB.setRetentionPolicy(properties.getRetentionPolicy());
        }
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
        return influxDB;
    }

    /**
     * 创建数据库
     *
     * @param database - 数据库名称
     */
    private void createDB(String database, InfluxDB influxDB) {
        influxDB.query(new Query("CREATE DATABASE " + database));
    }
}
