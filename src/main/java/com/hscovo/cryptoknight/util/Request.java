package com.hscovo.cryptoknight.util;

import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


public class Request {
    private static RestTemplate restTemplate;

    public static void setRestTemplate(RestTemplate client) {
        restTemplate = client;
    }

    public static <T> T get(String url, Class<T> clazz) {
        return restTemplate.getForObject(url, clazz);
    }

    public static <T> T get(String url, Map<String, String> headMap, Class<T> clazz) {
        return get(url, headMap, clazz, new HashMap<>());
    }

    public static <T> T get(String url, Map<String, String> headMap, Class<T> clazz, Map<String, String> params) {
        HttpHeaders headers = getHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        if (null != headMap) {
            headMap.forEach(headers::add);
        }
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, request, clazz, params).getBody();
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept-language", "en-US,en;q=0.9,ru;q=0.8");
        headers.add("user-agent", "Mozilla/5.0 (X11; Linux x86_64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36");
        headers.add("lang", "en");
        headers.add("clienttype", "web");
        return headers;
    }

    public static <T> T postJson(String url, Map<String, String> headMap, Object bodyObj, Class<T> clazz) {
        HttpHeaders headers = getHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        if (null != headMap) {
            headMap.forEach(headers::add);
        }
        String body = bodyObj == null? "{}": JSON.toJSONString(bodyObj);
        HttpEntity<String> formEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, formEntity, clazz);
    }

    public static <T> T postForm(String url, Map<String, String> attrMap, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        attrMap.forEach(params::add);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, clazz).getBody();
    }
}
