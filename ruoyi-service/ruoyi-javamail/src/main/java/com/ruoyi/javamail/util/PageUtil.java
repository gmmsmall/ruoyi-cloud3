package com.ruoyi.javamail.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.javamail.domain.ListResponse;
import com.ruoyi.javamail.domain.User;
import com.ruoyi.javamail.entity.TemplateManager;
import com.ruoyi.javamail.entity.UserListResponse;

public class PageUtil {

    public static UserListResponse getUserListResponse(IPage<User> userIPage) {
        UserListResponse response = new UserListResponse();
        for(User user : userIPage.getRecords()) {
            user.setPassword(PwdEncryptUtil.getInstance().decrypt(user.getPassword()));
        }
        response.setUserList(userIPage.getRecords());
        response.setTotal(userIPage.getTotal());
        response.setSize(userIPage.getRecords().size());
        return response;
    }

    /*public static ListResponse<Role> getListResponse(IPage<Role> iPage) {
        ListResponse<Role> response = new ListResponse<>();
        response.setList(iPage.getRecords());
        response.setTotal(iPage.getTotal());
        response.setSize(iPage.getRecords().size());
        return response;
    }*/

    public static ListResponse<TemplateManager> getTemplateManagerListResponse(IPage<TemplateManager> iPage) {
        ListResponse<TemplateManager> response = new ListResponse<>();
        response.setList(iPage.getRecords());
        response.setTotal(iPage.getTotal());
        response.setSize(iPage.getSize());
        return response;
    }


}
