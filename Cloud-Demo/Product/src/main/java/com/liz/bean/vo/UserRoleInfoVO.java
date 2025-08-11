package com.liz.bean.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class UserRoleInfoVO {

    private String userId;

    private String userRoleTp;

    /**
     * PRODUCT_ADMIN
     * EDITOR
     * USER
     */
    private List<String> roleTpList;
}
