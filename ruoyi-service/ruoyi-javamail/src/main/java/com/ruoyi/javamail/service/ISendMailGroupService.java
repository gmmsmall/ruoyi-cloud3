package com.ruoyi.javamail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.javamail.bo.SendMailGroupBo;
import com.ruoyi.javamail.entity.SendMailGroup;
import com.ruoyi.javamail.entity.SendMailGroupItems;
import com.ruoyi.javamail.vo.SendMailGroupVo;

import java.util.List;

/**
 * @author gmm
 */
public interface ISendMailGroupService extends IService<SendMailGroup> {

    void saveGroup(SendMailGroupBo sendMailGroupBo);

    void updateGroupById(SendMailGroup sendMailGroup, List<SendMailGroupItems> itemsList);

    void deleteGroups(String[] ids);

    /**
     * 获取当前用户下的分组列表
     * @param userId
     * @return
     */
    public List<SendMailGroupVo> groupList(Long userId);

}
