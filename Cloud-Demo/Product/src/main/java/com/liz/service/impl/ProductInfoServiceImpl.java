package com.liz.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liz.bean.entity.ProductInfoEntity;
import com.liz.bean.request.*;
import com.liz.bean.response.BaseResponse;
import com.liz.bean.vo.ProductInfoVO;
import com.liz.constant.ErrorCode;
import com.liz.mapper.ProductInfoMapper;
import com.liz.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductInfoServiceImpl implements ProductInfoService {


    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public BaseResponse<Page<ProductInfoVO>> queryProInfoList(QueryPrdInfoListRequest request) {
        Page<ProductInfoVO> result = productInfoMapper.queryProInfoList(request.getPage(), request);
        return BaseResponse.success(ErrorCode.SUCCESS, result);
    }

    @Override
    public BaseResponse<Object> delInfo(DelPrdInfoRequest request) {
        int i = productInfoMapper.deleteById(request.getId());
        if (i > 0) {
            return BaseResponse.success(ErrorCode.SUCCESS);
        }
        return BaseResponse.failure(ErrorCode.PRODUCT_DEL_ERROR);
    }

    @Override
    public BaseResponse<Object> updateInfo(UpdatePrdInfoRequest request) {
        UpdateWrapper<ProductInfoEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", request.getId());
        wrapper.set("product_name", request.getNewPrdName());
        int update = productInfoMapper.update(wrapper);
        if (update > 0) {
            return BaseResponse.success(ErrorCode.SUCCESS);
        }
        return BaseResponse.failure(ErrorCode.PRODUCT_UPD_ERROR);
    }

    @Override
    public BaseResponse<Object> addInfo(SavePrdInfoRequest request) {
        ProductInfoEntity productInfoEntity = new ProductInfoEntity();
        productInfoEntity.setProductName(request.getProductName());
        int insert = productInfoMapper.insert(productInfoEntity);
        if (insert > 0) {
            return BaseResponse.success(ErrorCode.SUCCESS);
        }
        return BaseResponse.failure(ErrorCode.PRODUCT_SAVE_ERROR);
    }
}
