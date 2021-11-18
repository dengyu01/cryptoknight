package com.hscovo.cryptoknight.config;

import com.hscovo.cryptoknight.util.Request;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

@Configuration
@ConditionalOnClass(ProxyConfig.class)
public class RestTemplateConfig {
    private final ProxyConfig proxyConfig;

    public RestTemplateConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory  factory  = new SimpleClientHttpRequestFactory();
        // 连接超时时间/毫秒（连接上服务器(握手成功)的时间，超时抛出connect timeout）
        factory.setConnectTimeout(5 * 1000);
        // 数据读取超时时间(socketTimeout)/毫秒（服务器返回数据(response)的时间，超时抛出read timeout）
        factory.setReadTimeout(10 * 1000);

        if(proxyConfig.getEnabled()){
            SocketAddress address = new InetSocketAddress(proxyConfig.getHost(), proxyConfig.getPort());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            factory.setProxy(proxy);
        }
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        Request.setRestTemplate(restTemplate);
        return restTemplate;
    }
}
