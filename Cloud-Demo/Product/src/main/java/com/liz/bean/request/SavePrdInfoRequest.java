package com.liz.bean.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SavePrdInfoRequest extends BaseRequest{

    @NotBlank(message = "商品名称不能为空")
    public String productName;

}
