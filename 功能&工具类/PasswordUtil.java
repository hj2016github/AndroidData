package com.sxygsj.dppfsr.utils;

import android.os.Build;

import org.apache.commons.codec.binary.Base64;


/*base64进行加密*/
public class PasswordUtil {
    //TODO 密码强度;
    public  static String  encodeBase64(String message) {//加密
        byte[] bytes = Base64.encodeBase64(message.getBytes());
        return  new String(bytes);
    }

    public  static String  decodeBase64(String message) {
        byte[] bytes = Base64.decodeBase64(message.getBytes());
        return  new String(bytes);
    }

    //(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{6,16}
    /*| .  *  [] \  是特殊字符，在使用时要进行转义,否则报错*/
    public static boolean isPasswordStrong(String pwd) {
       // String pwdRegex = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\[^\\]{6,20}";
        String pwdRegex_ = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_!@#$%^&*`~()-+=]+$)(?![a-z0-9]+$)(?![a-z\\W_!@#$%^&*`~()-+=]+$)(?![0-9\\W_!@#$%^&*`~()-+=]+$)[a-zA-Z0-9\\W_!@#$%^&*`~()-+=]{8,30}$";

        boolean matches = pwd.matches(pwdRegex_);
        return matches;
    }
}
