package com.ruoyi.javamail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.javamail.dao.SendMailGroupMapper;
import com.ruoyi.javamail.entity.SendMailGroup;
import com.ruoyi.javamail.entity.SendMailGroupItems;
import com.ruoyi.javamail.service.ISendMailGroupItemsService;
import com.ruoyi.javamail.service.ISendMailGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author gmm
 */
@Service
public class SendMailGroupServiceImpl extends ServiceImpl<SendMailGroupMapper, SendMailGroup> implements ISendMailGroupService {

    @Autowired
    private ISendMailGroupItemsService itemsService;

    @Override
    @Transactional
    public void saveGroup(SendMailGroup sendMailGroup) {
        this.baseMapper.insert(sendMailGroup);
    }

    @Override
    @Transactional
    public void updateGroupById(SendMailGroup sendMailGroup,List<SendMailGroupItems> itemsList) {
        if(sendMailGroup != null){
            if(itemsList != null && itemsList.size() > 0){
                //分组子表进行先删后增操作
                LambdaQueryWrapper<SendMailGroupItems> queryWrapper = new LambdaQueryWrapper<SendMailGroupItems>();
                queryWrapper.eq(SendMailGroupItems::getFid,sendMailGroup.getId());
                itemsService.remove(queryWrapper);//先删除
                itemsService.saveBatch(itemsList);//后增加
            }
            this.baseMapper.updateById(sendMailGroup);
        }

    }

    @Override
    @Transactional
    public void deleteGroups(String[] ids) {
        List<String> list = Arrays.asList(ids);
        LambdaQueryWrapper<SendMailGroup> queryWrapper = new LambdaQueryWrapper<>();
        if(list != null && list.size() > 0){
            queryWrapper.in(SendMailGroup::getId,list);
        }
        SendMailGroup sendMailGroup = new SendMailGroup();
        sendMailGroup.setDeleteflag("2");
        /*sendMailGroup.setDeleteperson(FebsUtil.getCurrentUser().getUsername());
        sendMailGroup.setDeletepersonid(FebsUtil.getCurrentUser().getUserId());*/
        sendMailGroup.setDeleteperson("小笨蛋");
        sendMailGroup.setDeletepersonid(20200528L);
        sendMailGroup.setDeletetime(LocalDateTime.now());
        //先删除主表
        this.baseMapper.update(sendMailGroup,queryWrapper);
        //后删除子表
        LambdaQueryWrapper<SendMailGroupItems> queryWrapperItems = new LambdaQueryWrapper<SendMailGroupItems>();
        queryWrapperItems.in(SendMailGroupItems::getFid,list);
        SendMailGroupItems groupItems = new SendMailGroupItems();
        groupItems.setDeleteflag("2");
        itemsService.update(groupItems,queryWrapperItems);

    }
}
