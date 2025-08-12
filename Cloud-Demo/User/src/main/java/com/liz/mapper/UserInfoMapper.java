package com.liz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liz.bean.entity.RoleRelationTEntity;
import com.liz.bean.entity.UserInfoEntity;
import com.liz.bean.vo.UserRoleInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoEntity> {

    UserInfoEntity queryUserInfo(@Param("userLogin") String userLogin);

    UserRoleInfoVO queryUserRoleInfo(@Param("userLogin") String userLogin);

    List<RoleRelationTEntity> queryAllRoleRela();

}
