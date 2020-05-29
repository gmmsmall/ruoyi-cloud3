package com.ruoyi.javamail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.javamail.entity.SendMailGroup;
import com.ruoyi.javamail.entity.SendMailGroupItems;

import java.util.List;

/**
 * @author gmm
 */
public interface ISendMailGroupService extends IService<SendMailGroup> {

    void saveGroup(SendMailGroup sendMailGroup);

    void updateGroupById(SendMailGroup sendMailGroup, List<SendMailGroupItems> itemsList);

    void deleteGroups(String[] ids);

}
