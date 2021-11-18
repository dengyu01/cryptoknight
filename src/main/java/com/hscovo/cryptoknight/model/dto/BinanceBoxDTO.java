package com.hscovo.cryptoknight.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString()
@EqualsAndHashCode()
public class BinanceBoxDTO {
    //TODO
    private String name;
    private String description;
    private String productId;
    //private String image;
    private Long startTime;
    private Long endTime;
    private Double price;
    private String currency;
    private Integer status;
    //private String artist;
    private Integer currentStore;
    //private Integer totalStore;
    //private String duration;
    //private String subTitle;
    //private Integer mappingStatus;
    //private String store;
    //private Boolean isGray;
    //private String serialsNo;
    private Long secondMarketSellingDelay;
    //private String network;
    private Integer limitPerTime;
}

