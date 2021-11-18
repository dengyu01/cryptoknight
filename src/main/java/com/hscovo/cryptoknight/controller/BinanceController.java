package com.hscovo.cryptoknight.controller;

import com.hscovo.cryptoknight.model.param.BoxParam;
import com.hscovo.cryptoknight.model.dto.BinanceBoxDTO;
import com.hscovo.cryptoknight.service.BinanceService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/binance/")
public class BinanceController {
    BinanceService binanceService;

    public BinanceController(BinanceService binanceService) {
        this.binanceService = binanceService;
    }

    @GetMapping("status")
    public boolean test() {
        return binanceService.checkStatus();
    }

    @GetMapping(value = "nft-box/list")
    public List<BinanceBoxDTO> getBoxList() {
        return binanceService.getNftBoxList();
    }

    @GetMapping(value = "nft-box/detail")
    public BinanceBoxDTO getBoxDetail(@RequestBody @Valid BoxParam boxParam) {
        return binanceService.getBoxDetail(boxParam);
    }

    @PostMapping(value = "nft-box/purchase")
    public boolean buyBox(@RequestBody @Valid BoxParam boxParam) {
        return binanceService.buyBox(boxParam);
    }

    @PostMapping("nft-box/purchase_jobs")
    public boolean startBuyBoxJobs(@RequestBody @Valid BoxParam boxParam) throws InterruptedException {
        return binanceService.multiThreadsBuyBox(boxParam);
    }
}
