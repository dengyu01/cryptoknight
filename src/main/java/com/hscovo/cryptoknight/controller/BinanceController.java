package com.hscovo.cryptoknight.controller;

import com.hscovo.cryptoknight.model.dto.BoxDTO;
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
    public BinanceBoxDTO getBoxDetail(@RequestParam String productId) {
        return binanceService.getBoxDetail(productId);
    }

    @PostMapping(value = "nft-box/purchase")
    public boolean buyBox(@RequestBody @Valid BoxDTO boxDTO) {
        return binanceService.buyBox(boxDTO);
    }

    @PostMapping("nft-box/purchase_jobs")
    public boolean startBuyBoxJobs(@RequestBody @Valid BoxDTO boxDTO) throws InterruptedException {
        return binanceService.multiThreadsBuyBox(boxDTO);
    }
}
