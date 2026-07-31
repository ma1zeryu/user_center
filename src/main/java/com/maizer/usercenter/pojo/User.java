package com.maizer.usercenter.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("`user`")
public class User {
    private Long id;
    private String username;
    private String userAccount;
    private String avatarUrl;
    private Integer gender;
    private String userPassword;
    private String phone;
    private String email;
    private Integer userStatus;
    private Date createTime;
    private Date updateTime;
    private Integer isDelete;
}
