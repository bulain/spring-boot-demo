package com.bulain.mybatis.sys.service.impl;

import com.bulain.mybatis.sys.common.LoginSecurityConstants;
import com.bulain.mybatis.sys.service.PasswordPolicyService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 密码策略服务实现
 */
@Service
public class PasswordPolicyServiceImpl implements PasswordPolicyService {

    /**
     * 常见弱密码列表
     */
    private static final List<String> WEAK_PASSWORDS = Arrays.asList(
            "123456", "12345678", "123456789", "password", "qwerty",
            "1234567", "111111", "123123", "abc123", "password1",
            "password123", "admin", "admin123", "root", "root123",
            "000000", "123321", "123456a", "a123456", "88888888",
            "666666", "1234567890", "12345", "1234", "123",
            "qwe123", "qazwsx", "asd123", "zxcvbn", "asdfgh"
    );

    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~`]");

    @Override
    public String validatePassword(String password, String username) {
        // 检查密码长度
        if (password == null || password.length() < LoginSecurityConstants.PASSWORD_MIN_LENGTH) {
            return "密码长度不能小于 " + LoginSecurityConstants.PASSWORD_MIN_LENGTH + " 位";
        }

        // 检查密码不能包含用户名
        if (username != null && !username.isEmpty() &&
                password.toLowerCase().contains(username.toLowerCase())) {
            return "密码不能包含用户名";
        }

        // 统计字符类型数量
        int charTypes = 0;
        if (UPPERCASE_PATTERN.matcher(password).find()) charTypes++;
        if (LOWERCASE_PATTERN.matcher(password).find()) charTypes++;
        if (DIGIT_PATTERN.matcher(password).find()) charTypes++;
        if (SPECIAL_CHAR_PATTERN.matcher(password).find()) charTypes++;

        // 检查字符类型是否足够
        if (charTypes < LoginSecurityConstants.PASSWORD_REQUIRED_CHAR_TYPES) {
            return "密码需要包含大小写字母、数字、特殊字符中的至少 " +
                    LoginSecurityConstants.PASSWORD_REQUIRED_CHAR_TYPES + " 种";
        }

        // 检查是否为常见弱密码
        if (WEAK_PASSWORDS.contains(password.toLowerCase())) {
            return "密码过于简单，请设置更复杂的密码";
        }

        return null;
    }

}
