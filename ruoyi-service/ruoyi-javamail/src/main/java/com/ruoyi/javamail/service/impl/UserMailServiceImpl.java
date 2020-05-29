package com.ruoyi.javamail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.javamail.dao.UserMailMapper;
import com.ruoyi.javamail.domain.UserMail;
import com.ruoyi.javamail.service.IUserMailService;
import com.ruoyi.javamail.util.MailEncryptUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangwei
 */
@Service
public class UserMailServiceImpl extends ServiceImpl<UserMailMapper, UserMail> implements IUserMailService {

    @Override
    public void saveUserMail(String[] mailArr, String[] passwordArr, String[] smtpArr, int arrLen) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        List<UserMail> userMailList = new ArrayList<>();
        for (int i = 0; i < arrLen; ++i) {
            UserMail userMail = new UserMail();
            userMail.setCreateTime(now);
            userMail.setUpdateTime(now);
            userMail.setMailAddress(mailArr[i]);
            userMail.setPassword(MailEncryptUtil.getInstance().encrypt(passwordArr[i]));
            userMailList.add(userMail);
        }
        saveBatch(userMailList);
    }

    @Override
    public void createUserMail(UserMail userMail) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        userMail.setCreateTime(now);
        userMail.setUpdateTime(now);
        userMail.setPassword(MailEncryptUtil.getInstance().encrypt(userMail.getPassword()));
        save(userMail);
    }

    @Override
    public List<UserMail> findUserMailList(Long userId) throws Exception {
        List<UserMail> userMailList = list(new LambdaQueryWrapper<UserMail>().eq(UserMail::getUserId, userId));
        for (UserMail userMail : userMailList) {
            userMail.setPassword(MailEncryptUtil.getInstance().decrypt(userMail.getPassword()));
        }
        return userMailList;
    }

    @Override
    public UserMail findUserMail(Long id) throws Exception {
        UserMail userMail = getById(id);
        if(userMail != null) {
            userMail.setPassword(MailEncryptUtil.getInstance().decrypt(userMail.getPassword()));
        }
        return userMail;
    }

    @Override
    public UserMail findUserMail(String mailAddress) throws Exception {
        UserMail userMail = getOne(new LambdaQueryWrapper<UserMail>().eq(UserMail::getMailAddress, mailAddress));
        if(userMail != null) {
            userMail.setPassword(MailEncryptUtil.getInstance().decrypt(userMail.getPassword()));
        }
        return userMail;
    }

    @Override
    public void deleteUserMail(String[] userMailIds) throws Exception {
        List<String> list = Arrays.asList(userMailIds);
        removeByIds(list);
    }

    @Override
    public void updateUserMail(UserMail userMail) throws Exception {
        userMail.setPassword(MailEncryptUtil.getInstance().encrypt(userMail.getPassword()));
        userMail.setUpdateTime(LocalDateTime.now());
        updateById(userMail);
    }
}
