package com.maizer.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maizer.usercenter.contant.UserConstant;
import com.maizer.usercenter.model.User;
import com.maizer.usercenter.service.UserService;
import com.maizer.usercenter.mapper.UserMapper;
import com.maizer.usercenter.utils.PasswordUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.maizer.usercenter.utils.PasswordUtil.verifyPassword;

/**
* @author jyp
* @description 针对表【user】的数据库操作Service实现
* @createDate 2026-08-01 13:30:39
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            //todo: 修改为自定义异常处理类
            return -1;
        }

        if(userAccount.length() < 4) {
            return -1;
        }

        if(userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        // 账户不能包含特殊字符（只允许字母、数字、下划线）
        String validPattern = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$";
        if(!userAccount.matches(validPattern)) {
            return -1; // 包含特殊字符，返回错误
        }

        if(!userPassword.equals(checkPassword)) {
            return -1;
        }

        //把校验的逻辑提到前面去，这样可以节省查数据库的性能
        //账户不能重复，这里是调用的mybatisplus的库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0) {
            return -1;
        }

        //2.加密
        String s = PasswordUtil.encryptPassword(userPassword);

        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(s);

        int insert = userMapper.insert(user);
        if(insert != 1) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }

        if(userAccount.length() < 4) {
            return null;
        }

        if(userPassword.length() < 8) {
            return null;
        }

        // 账户不能包含特殊字符（只允许字母、数字、下划线）
        String validPattern = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$";
        if(!userAccount.matches(validPattern)) {
            return null; // 包含特殊字符，返回错误
        }

        //查询条件构建器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);

        User user = userMapper.selectOne(queryWrapper);

        if(user != null) {
            String password = user.getUserPassword();
            if(!verifyPassword(userPassword, password)) {
                return null;
            }
        } else { //用户不存在
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }

        //3. 用户脱敏，把敏感信息屏蔽掉
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setRole(user.getRole());

        //4. 记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }
}




