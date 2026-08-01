package com.maizer.usercenter.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maizer.usercenter.model.User;
import com.maizer.usercenter.service.UserService;
import com.maizer.usercenter.mapper.UserMapper;
import com.maizer.usercenter.utils.PasswordUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.sql.Wrapper;

/**
* @author jyp
* @description 针对表【user】的数据库操作Service实现
* @createDate 2026-08-01 13:30:39
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
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
}




