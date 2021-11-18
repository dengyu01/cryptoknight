package com.hscovo.cryptoknight.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class BinanceNftProperties {
    private String cookie;
    private String csrfToken;
    private String bncUuid;
    private String deviceInfo;

    private String nftBoxListUrl;
    private String nftBoxInfoUrl;
    private String nftBoxInfoUrl2;
    private String nftBoxBuyUrl;
    private String nftBoxForCaptchaUrl;

    private Integer startTime;
}
