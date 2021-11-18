package com.hscovo.cryptoknight.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hscovo.cryptoknight.config.properties.BinanceNftProperties;
import com.hscovo.cryptoknight.config.properties.BinanceProperties;
import com.hscovo.cryptoknight.model.param.BoxParam;
import com.hscovo.cryptoknight.model.dto.BinanceBoxDTO;
import com.hscovo.cryptoknight.util.Request;
import com.hscovo.cryptoknight.util.TimeUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BinanceService {
    private final BinanceProperties binanceProperties;
    private final Map<String, String> headersCache = new HashMap<>();
    private final Map<String, BinanceBoxDTO> boxCache = new HashMap<>();

    private final TwoCaptchaService twoCaptchaService;

    public BinanceService(BinanceProperties binanceConfig, TwoCaptchaService twoCaptchaService) {
        this.binanceProperties = binanceConfig;
        this.twoCaptchaService = twoCaptchaService;
    }

    private synchronized void setHeaderMap() {
        BinanceNftProperties nft = binanceProperties.getNft();
        headersCache.put("cookie", nft.getCookie());
        headersCache.put("csrftoken", nft.getCsrfToken());
        headersCache.put("bnc-uuid", nft.getDeviceInfo());
        headersCache.put("device-info", nft.getDeviceInfo());
    }

    public Boolean checkStatus() {
        if (headersCache.isEmpty()) {
            setHeaderMap();
        }
        JSONObject jsonObject =
                Request.postJson(binanceProperties.getUserInfoUrl(), headersCache, null, JSONObject.class);
        //TODO: 异常处理和日志
        return jsonObject.getBoolean("success");
    }

    private void checkStatusHandler() {
        if (!checkStatus()) {
            //TODO
        }
    }

    public List<BinanceBoxDTO> getNftBoxList() {
        checkStatusHandler();
        JSONObject jsonObject =
                Request.get(binanceProperties.getNft().getNftBoxListUrl(), headersCache, JSONObject.class);
        JSONArray boxArray = jsonObject.getJSONArray("data");
        List<BinanceBoxDTO> boxes = JSONArray.parseArray(boxArray.toJSONString(), BinanceBoxDTO.class);
        boxes.forEach(box -> boxCache.put(box.getProductId(), box));
        return boxes;
    }

    public BinanceBoxDTO getBoxDetail(BoxParam boxParam) {
        checkStatusHandler();
        Map<String, String> params = new HashMap<>();
        params.put("productId", boxParam.getProductId());
        JSONObject response
                = Request.get(binanceProperties.getNft().getNftBoxInfoUrl(), headersCache, JSONObject.class, params);
        BinanceBoxDTO box = response.getObject("data", BinanceBoxDTO.class);
        if(box != null) {
            boxCache.put(box.getProductId(), box);
        }
        return box;
    }

    public boolean buyBox(BoxParam boxParam) {
        String url = binanceProperties.getNft().getNftBoxForCaptchaUrl() + boxParam.getProductId();
        String code = twoCaptchaService.solveBinanceCaptcha(url, binanceProperties.getSiteKey());
        return buyBox(boxParam, code);
    }

    public boolean buyBox(BoxParam boxParam, String code) {
        if (boxCache.isEmpty()) {
            getNftBoxList();
        }
        BinanceBoxDTO box = boxCache.get(boxParam.getProductId());
        JSONObject body = new JSONObject();
        body.put("productId", box.getProductId());
        body.put("number", boxParam.getAmount());

        HashMap<String, String> headers = new HashMap<>(headersCache);
        headers.put("x-nft-checkbot-token", code);
        JSONObject response =
                Request.postJson(binanceProperties.getNft().getNftBoxBuyUrl(), headers, body, JSONObject.class);
        System.out.println(response);
        return response.getBoolean("success");
    }

    private synchronized CopyOnWriteArrayList<String> startCaptchaJobs(Integer tNums, String url, String siteKey)
            throws InterruptedException {
        CopyOnWriteArrayList<String> codeList = new CopyOnWriteArrayList<>();
        ArrayList<Thread> captchaJobs = new ArrayList<>();
        for (int i = 0; i < tNums; i++) {
            Thread t = new Thread(() -> {
                String code = twoCaptchaService.solveBinanceCaptcha(url, siteKey);
                codeList.add(code);
            });
            t.start();
            captchaJobs.add(t);
            Thread.sleep(100);
        }
        for (Thread t: captchaJobs) {
            t.join();
        }
        return codeList;
    }

    private synchronized boolean startBuyBoxJob(BoxParam boxParam, CopyOnWriteArrayList<String> codeList)
            throws InterruptedException {
        AtomicInteger response = new AtomicInteger();
        ArrayList<Thread> buyBoxJobs = new ArrayList<>();
        for (int i = 0; i < codeList.size(); i++) {
            String code = codeList.get(i);
            Thread t = new Thread(() -> {
                boolean success = buyBox(boxParam, code);
                if (success) {
                    response.incrementAndGet();
                    System.out.println("buy box success.");
                }
            });
            t.start();
            buyBoxJobs.add(t);
            Thread.sleep(100);
            System.out.println("create a buyBox thread success.");
        }
        for (Thread t: buyBoxJobs) {
            t.join();
        }
        if (response.get() > 0) {
            return true;
        }
        return false;
    }

    public boolean multiThreadsBuyBox(BoxParam boxParam) throws InterruptedException {
        String url = binanceProperties.getNft().getNftBoxForCaptchaUrl() + boxParam.getProductId();
        String siteKey = binanceProperties.getSiteKey();

        BinanceBoxDTO boxDetail = getBoxDetail(boxParam);
        Long startTime = boxDetail.getStartTime();

        TimeUtil.wait(startTime, "waiting box sale...", 105, 1000);

        System.out.println("start to get captcha code.");
        CopyOnWriteArrayList<String> codeList = startCaptchaJobs(boxParam.getThreadNum(), url, siteKey);
        if (codeList.isEmpty()) {
            System.out.println("failed to get captcha!");
            return false;
        }
        System.out.println("get all captcha code success. wait to run buyBox threads.");
        TimeUtil.wait(startTime, "waiting box sale...", 1, 100);

        System.out.println("start to run buyBox threads.");
        boolean response = startBuyBoxJob(boxParam, codeList);
        if (!response) {
            System.out.println("buy box:" + boxDetail.getName() + " --> final result: failed.");
            return false;
        }
        System.out.println("buy box:" + boxDetail.getName() + " --> final result: success.");
        return true;
    }

}
