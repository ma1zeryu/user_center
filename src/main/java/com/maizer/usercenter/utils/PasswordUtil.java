package com.maizer.usercenter.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    // 创建一个全局的编码器实例，可以指定强度（比如 12），默认是 10[citation:7]
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(12);

    /**
     * 加密密码（每次结果都不同，因为会自动生成随机盐值）
     */
    public static String encryptPassword(String password) {
        return ENCODER.encode(password);
    }

    /**
     * 验证密码
     * @param rawPassword 用户输入的明文密码
     * @param encodedPassword 数据库里存的那个哈希字符串
     * @return 是否匹配
     */
    public static boolean verifyPassword(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }
}
