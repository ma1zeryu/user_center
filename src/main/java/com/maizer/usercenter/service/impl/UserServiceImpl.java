package com.maizer.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maizer.usercenter.common.ErrorCode;
import com.maizer.usercenter.contant.UserConstant;
import com.maizer.usercenter.exception.BusinessException;
import com.maizer.usercenter.model.User;
import com.maizer.usercenter.service.UserService;
import com.maizer.usercenter.mapper.UserMapper;
import com.maizer.usercenter.utils.PasswordUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)){
            //todo: 修改为自定义异常处理类
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        if(userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号太短");
        }

        if(userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }

        // 账户不能包含特殊字符（只允许字母、数字、下划线）
        String validPattern = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$";
        if(!userAccount.matches(validPattern)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if(!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //把校验的逻辑提到前面去，这样可以节省查数据库的性能
        //账户不能重复，这里是调用的mybatis-plus的库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能重复");
        }

        //星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);
        if(count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号不能重复");
        }

        //2.加密
        String s = PasswordUtil.encryptPassword(userPassword);

        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(s);
        user.setPlanetCode(planetCode);

        int insert = userMapper.insert(user);
        if(insert != 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户插入数据库失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能为空");
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
        User safetyUser = getSafetyUser(user);

        //4. 记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    @Override
    public User getSafetyUser(User originUser) {
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setRole(originUser.getRole());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




