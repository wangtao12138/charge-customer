package cn.com.cdboost.charge.customer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 系统自定义的配置信息
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "server")
public class SystemConfig {
    /**
     * 服务器域名
     */
    private String domain;
}
