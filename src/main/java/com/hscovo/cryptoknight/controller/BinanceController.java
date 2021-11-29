package com.hscovo.cryptoknight.controller;

import com.hscovo.cryptoknight.model.dto.BoxDTO;
import com.hscovo.cryptoknight.model.dto.BinanceBoxDTO;
import com.hscovo.cryptoknight.service.BinanceService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "检查用户token信息是够正确")
    @GetMapping("status")
    public boolean test() {
        return binanceService.checkStatus();
    }

    @Operation(summary = "获取所有在售盲盒列表")
    @GetMapping(value = "nft-box/list")
    public List<BinanceBoxDTO> getBoxList() {
        return binanceService.getNftBoxList();
    }

    @Operation(summary = "获取盲盒详细信息")
    @GetMapping(value = "nft-box/detail")
    public BinanceBoxDTO getBoxDetail(@RequestParam String productId) {
        return binanceService.getBoxDetail(productId);
    }

    @Operation(summary = "购买盲盒")
    @PostMapping(value = "nft-box/purchase")
    public boolean buyBox(@RequestBody @Valid BoxDTO boxDTO) {
        return binanceService.buyBox(boxDTO);
    }

    @Operation(summary = "创建多线程抢购盲盒的任务")
    @PostMapping("nft-box/purchase_jobs")
    public boolean startBuyBoxJobs(@RequestBody @Valid BoxDTO boxDTO) throws InterruptedException {
        return binanceService.multiThreadsBuyBox(boxDTO);
    }
}
