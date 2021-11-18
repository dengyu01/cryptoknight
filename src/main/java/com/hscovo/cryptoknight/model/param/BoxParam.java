package com.hscovo.cryptoknight.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BoxParam {
    @NotBlank(message = "productId不能为空")
    private String productId;
    private Integer amount = 1;
    private Integer threadNum = 15;
}
