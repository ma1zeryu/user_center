package com.maizer.usercenter.controller;

import ch.qos.logback.core.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maizer.usercenter.contant.UserConstant;
import com.maizer.usercenter.model.User;
import com.maizer.usercenter.model.request.UserLoginRequest;
import com.maizer.usercenter.model.request.UserRegisterRequest;
import com.maizer.usercenter.service.UserService;
import com.maizer.usercenter.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return null;
        };
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return id;
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if(userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        };
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        if(!isAdmin(request)) return new ArrayList<>();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        return userService.list(queryWrapper);
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        if(!isAdmin(request)) return false;

        if(id <= 0) return false;
        return userService.removeById(id);
    }

    /**
     * 是否为管理员
     * @param request
     * @return true：是管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        //鉴权，仅管理员可查询
        Object attribute = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) attribute;

        if(user == null || user.getRole() != UserConstant.ADMIN_ROLE) {
            return false;
        }

        return true;
    }
}
