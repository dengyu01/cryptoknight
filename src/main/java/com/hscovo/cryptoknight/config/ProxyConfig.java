package com.hscovo.cryptoknight.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="proxy")
@Data
public class ProxyConfig {
    private Boolean enabled;
    private String host;
    private Integer port;
}
