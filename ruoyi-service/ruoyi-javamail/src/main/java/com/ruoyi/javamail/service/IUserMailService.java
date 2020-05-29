package com.ruoyi.javamail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.javamail.domain.UserMail;

import java.util.List;

/**
 * @author zhangwei
 */
public interface IUserMailService extends IService<UserMail> {

    void saveUserMail(String[] mailArr, String[] passwordArr, String[] smtpArr, int arrLen) throws Exception;

    void createUserMail(UserMail userMail) throws Exception;

    List<UserMail> findUserMailList(Long userId) throws Exception;

    UserMail findUserMail(Long id) throws Exception;

    UserMail findUserMail(String mailAddress) throws Exception;

    void deleteUserMail(String[] userMailIds) throws Exception;

    void updateUserMail(UserMail userMail) throws Exception;

}
