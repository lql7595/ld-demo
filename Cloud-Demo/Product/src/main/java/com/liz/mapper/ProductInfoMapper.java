package com.liz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liz.bean.entity.ProductInfoEntity;
import com.liz.bean.request.QueryPrdInfoListRequest;
import com.liz.bean.vo.ProductInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface ProductInfoMapper extends BaseMapper<ProductInfoEntity> {


    Page<ProductInfoVO> queryProInfoList(Page page, @Param("req") QueryPrdInfoListRequest request);
}

