package com.ruoyi.system.util;

import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.system.domain.SysUser;

public class PasswordUtil
{
    public static boolean matches(SysUser user, String newPassword)
    {
        return user.getPassword().equals(encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
    }

    public static String encryptPassword(String username, String password, String salt)
    {
        return Md5Utils.hash(username + password + salt);
    }

    public static void main (String[] args){

        String password = Md5Utils.hash("admin" + "admin" + "111111");
        System.out.println(password);
    }
}