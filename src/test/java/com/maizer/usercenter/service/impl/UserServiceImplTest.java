package com.maizer.usercenter.service.impl;

import com.maizer.usercenter.model.User;
import com.maizer.usercenter.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("dogYupi");
        user.setUserAccount("123");
        user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.io");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

//    @Test
//    void userRegister() {
//        String userAccount = "玉米排骨汤";
//        String userPassword = "12345678";
//
//        String checkPassword = "12345678";
//        long userId = userService.userRegister(userAccount, userPassword, checkPassword);
//        // 如果注册成功，返回的是用户ID（应该 > 0）
//        Assertions.assertTrue(userId > 0);
//
//        // 去数据库查一下，看看是不是真的插入了
//        User user = userService.getById(userId);
//        Assertions.assertNotNull(user);
//        Assertions.assertEquals(userAccount, user.getUserAccount());
//    }
}