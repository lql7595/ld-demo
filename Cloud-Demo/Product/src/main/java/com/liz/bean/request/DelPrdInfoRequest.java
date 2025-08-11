package com.liz.bean.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DelPrdInfoRequest extends BaseRequest{

    @NotNull(message = "商品id不能为空")
    public int id;

}
