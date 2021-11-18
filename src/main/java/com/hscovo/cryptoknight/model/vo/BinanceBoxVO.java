package com.hscovo.cryptoknight.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString()
@EqualsAndHashCode()
public class BinanceBoxVO {
    private String name;
    private String description;
    private String productId;
    private Long startTime;
    private Long endTime;
    private Double price;
    private String currency;
    private Integer currentStore;
    private Integer limitPerTime;
}
