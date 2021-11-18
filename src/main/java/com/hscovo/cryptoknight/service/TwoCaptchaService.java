package com.hscovo.cryptoknight.service;

import com.hscovo.cryptoknight.config.ProxyConfig;
import com.twocaptcha.TwoCaptcha;
import com.twocaptcha.captcha.ReCaptcha;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwoCaptchaService {

    @Value("${two-captcha.api-key}")
    private String apiKey;

    private TwoCaptcha binanceSolver;

    private final ProxyConfig proxyConfig;

    public TwoCaptchaService(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    public String solveBinanceCaptcha(String url, String siteKey) {
        TwoCaptcha solver = getSolver();
        ReCaptcha captcha = getBinanceCaptcha(url, siteKey);

        try {
            solver.solve(captcha);
            System.out.println("Captcha solved: " + captcha.getCode());
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        return captcha.getCode();
    }

    public synchronized TwoCaptcha getSolver() {
        if (binanceSolver == null) {
            binanceSolver = new TwoCaptcha(apiKey);
        }
        return binanceSolver;
    }

    private ReCaptcha getBinanceCaptcha(String url, String siteKey) {
        ReCaptcha reCaptcha = new ReCaptcha();
        reCaptcha.setSiteKey(siteKey);
        reCaptcha.setUrl(url);
        reCaptcha.setVersion("v3");
        reCaptcha.setAction("verify");
        reCaptcha.setScore(0.3);
        reCaptcha.setProxy("HTTPS", proxyConfig.getHost() + ":" + proxyConfig.getPort());
        return reCaptcha;
    }
}
