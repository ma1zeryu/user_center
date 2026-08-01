package com.maizer.usercenter.service;

import com.maizer.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 17702
* @description 针对表【user】的数据库操作Service
* @createDate 2026-08-01 13:30:39
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);
}
