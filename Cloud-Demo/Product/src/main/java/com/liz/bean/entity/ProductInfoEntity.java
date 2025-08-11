package com.liz.bean.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("pd_product_info")
public class ProductInfoEntity {

    @TableId("id")
    private int id;

    @TableField("product_name")
    private String productName;
}
