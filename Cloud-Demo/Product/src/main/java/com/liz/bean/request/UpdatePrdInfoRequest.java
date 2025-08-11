package com.liz.bean.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePrdInfoRequest extends BaseRequest{

    @NotNull(message = "商品id不能为空")
    public int id;

    @NotBlank(message = "更新后的商品名称不能为空")
    public String newPrdName;

}
