package com.hscovo.cryptoknight.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BoxDTO {
    @NotBlank(message = "productId不能为空")
    private String productId;
    private Integer amount;
    private Integer threadNum;
}
