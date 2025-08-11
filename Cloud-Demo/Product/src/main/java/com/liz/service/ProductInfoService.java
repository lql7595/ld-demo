package com.liz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liz.bean.request.*;
import com.liz.bean.response.BaseResponse;
import com.liz.bean.vo.ProductInfoVO;

import java.util.List;

public interface ProductInfoService {
    BaseResponse<Page<ProductInfoVO>> queryProInfoList(QueryPrdInfoListRequest request);

    BaseResponse<Object> delInfo(DelPrdInfoRequest request);

    BaseResponse<Object> updateInfo(UpdatePrdInfoRequest request);

    BaseResponse<Object> addInfo(SavePrdInfoRequest request);
}
