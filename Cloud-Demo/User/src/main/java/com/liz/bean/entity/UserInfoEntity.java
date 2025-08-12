package com.liz.bean.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserInfoEntity {

    @TableId("id")
    private int id;

    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "user_name")
    private String userName;

    @TableField("pwd")
    private String pwd;

    @TableField("user_type")
    private String userType;
}
