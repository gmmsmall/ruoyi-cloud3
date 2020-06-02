package com.ruoyi.javamail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.javamail.bo.SendMailGroupBo;
import com.ruoyi.javamail.bo.SendMailGroupDeleteBo;
import com.ruoyi.javamail.bo.SendMailGroupEditBo;
import com.ruoyi.javamail.entity.SendMailGroup;
import com.ruoyi.javamail.entity.SendMailGroupItems;
import com.ruoyi.javamail.vo.SendMailGroupInfoVo;
import com.ruoyi.javamail.vo.SendMailGroupVo;

import java.util.List;

/**
 * @author gmm
 */
public interface ISendMailGroupService extends IService<SendMailGroup> {

    void saveGroup(SendMailGroupBo sendMailGroupBo);

    void updateGroupById(SendMailGroup sendMailGroup, List<SendMailGroupItems> itemsList);

    void deleteGroups(SendMailGroupDeleteBo sendMailGroupDeleteBo);

    /**
     * 获取当前用户下的分组列表
     * @param userId
     * @return
     */
    public List<SendMailGroupVo> groupList(Long userId);

    /**
     * 修改分组(主子表同时进行修改)
     * @param sendMailGroupEditBo
     */
    void updateGroupByEntity(SendMailGroupEditBo sendMailGroupEditBo);

    /**
     * 根据分组主键id，查看分组详情
     * @param id
     * @return
     */
    SendMailGroupInfoVo getInfoById(Long id);

}
