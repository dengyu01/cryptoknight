package com.hscovo.cryptoknight.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("binance")
@Data
public class BinanceProperties {
    private String userInfoUrl;
    private String siteKey;
    private BinanceNftProperties nft;
}
