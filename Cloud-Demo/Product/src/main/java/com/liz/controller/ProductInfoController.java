package com.liz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liz.anno.AuthVerifyAnnotation;
import com.liz.anno.TokenVerifyAnnotation;
import com.liz.bean.request.*;
import com.liz.bean.response.BaseResponse;
import com.liz.bean.vo.ProductInfoVO;
import com.liz.constant.Constant;
import com.liz.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bus")
public class ProductInfoController {

    @Autowired
    ProductInfoService productInfoService;

    @PostMapping("/query/infolist")
    @TokenVerifyAnnotation
    @AuthVerifyAnnotation(authList = {Constant.USER})
    public BaseResponse<Page<ProductInfoVO>> queryProInfoList(@Validated @RequestBody QueryPrdInfoListRequest request) {

        return productInfoService.queryProInfoList(request);
    }

    @PostMapping("/del/info")
    @TokenVerifyAnnotation
    @AuthVerifyAnnotation(authList = {Constant.EDITOR})
    public BaseResponse<Object> delInfo(@Validated @RequestBody DelPrdInfoRequest request) {

        return productInfoService.delInfo(request);
    }

    @PostMapping("/update/info")
    @TokenVerifyAnnotation
    @AuthVerifyAnnotation(authList = {Constant.EDITOR})
    public BaseResponse<Object> updateInfo(@Validated @RequestBody UpdatePrdInfoRequest request) {

        return productInfoService.updateInfo(request);
    }

    @PostMapping("/add/info")
    @TokenVerifyAnnotation
    @AuthVerifyAnnotation(authList = {Constant.EDITOR})
    public BaseResponse<Object> addInfo(@Validated @RequestBody SavePrdInfoRequest request) {

        return productInfoService.addInfo(request);
    }
}
