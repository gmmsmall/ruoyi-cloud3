package com.ruoyi.javamail.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.EmptyException;
import com.ruoyi.javamail.bo.SendMailGroupBo;
import com.ruoyi.javamail.bo.SendMailGroupDeleteBo;
import com.ruoyi.javamail.bo.SendMailGroupEditBo;
import com.ruoyi.javamail.dao.SendMailGroupMapper;
import com.ruoyi.javamail.entity.SendMailGroup;
import com.ruoyi.javamail.entity.SendMailGroupItems;
import com.ruoyi.javamail.service.ISendMailGroupItemsService;
import com.ruoyi.javamail.service.ISendMailGroupService;
import com.ruoyi.javamail.util.FebsUtil;
import com.ruoyi.javamail.vo.SendMailGroupInfoVo;
import com.ruoyi.javamail.vo.SendMailGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 分组管理service
 * @author gmm
 */
@Service
@Slf4j
public class SendMailGroupServiceImpl extends ServiceImpl<SendMailGroupMapper, SendMailGroup> implements ISendMailGroupService {

    @Autowired
    private ISendMailGroupItemsService itemsService;

    @Autowired
    private SendMailGroupMapper groupMapper;

    @Override
    @Transactional
    public void saveGroup(SendMailGroupBo sendMailGroupBo) {
        log.info("新增一个分组");
        try{
            //判断当前的分组名称是否已存在
            SendMailGroup sendMailGroup = this.groupMapper.selectOne(new QueryWrapper<SendMailGroup>()
                    .eq("deleteflag","1")
                    .eq("addpersonid",sendMailGroupBo.getAddpersonid())
                    .eq("name",sendMailGroupBo.getName()));
            if(sendMailGroup != null){//分组名称已存在，不允许新增重复的
                throw new DuplicateKeyException("数据已存在，操作失败");
            }else{
                LocalDateTime now = LocalDateTime.now();
                SendMailGroup group = new SendMailGroup();
                group.setName(sendMailGroupBo.getName());
                group.setName(sendMailGroupBo.getName());//分组名称
                group.setAddperson(sendMailGroupBo.getAddperson());//新增人
                group.setAddpersonid(sendMailGroupBo.getAddpersonid());//新增人id
                group.setAddtime(now);
                group.setEditperson(sendMailGroupBo.getAddperson());//修改人
                group.setEditpersonid(sendMailGroupBo.getAddpersonid());//修改人id
                group.setEdittime(now);
                group.setDeleteflag("1");//未删除
                this.baseMapper.insert(group);
            }
        }catch (Exception e){
            log.error("新增分组失败");
            throw e;
        }
    }

    /**
     * 修改分组的具体操作
     * @param sendMailGroup
     * @param itemsList
     */
    @Override
    @Transactional
    public void updateGroupById(SendMailGroup sendMailGroup,List<SendMailGroupItems> itemsList) {
        try{
            if(sendMailGroup != null){
                if(itemsList != null && itemsList.size() > 0){
                    //分组子表进行先删后增操作
                    LambdaQueryWrapper<SendMailGroupItems> queryWrapper = new LambdaQueryWrapper<SendMailGroupItems>();
                    queryWrapper.eq(SendMailGroupItems::getFid,sendMailGroup.getId());
                    this.itemsService.remove(queryWrapper);//先删除
                    this.itemsService.saveBatch(itemsList);//后增加
                }
                this.baseMapper.updateById(sendMailGroup);
            }
        }catch (Exception e){
            log.error("修改分组失败");
            throw e;
        }

    }

    /**
     * 删除分组(单删或批量删除)
     * @param sendMailGroupDeleteBo
     */
    @Override
    @Transactional
    public void deleteGroups(SendMailGroupDeleteBo sendMailGroupDeleteBo) {
        log.info("删除分组");
        try{
            if(sendMailGroupDeleteBo != null && CollUtil.isNotEmpty(sendMailGroupDeleteBo.getIdList())){
                //删除主表
                LambdaQueryWrapper<SendMailGroup> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(SendMailGroup::getId,sendMailGroupDeleteBo.getIdList());
                SendMailGroup sendMailGroup = new SendMailGroup();
                sendMailGroup.setDeleteflag("2");
                sendMailGroup.setDeleteperson(sendMailGroupDeleteBo.getEditperson());
                sendMailGroup.setDeletepersonid(sendMailGroupDeleteBo.getEditpersonid());
                sendMailGroup.setDeletetime(LocalDateTime.now());
                //先删除主表
                this.baseMapper.update(sendMailGroup,queryWrapper);
                //后删除子表
                LambdaQueryWrapper<SendMailGroupItems> queryWrapperItems = new LambdaQueryWrapper<SendMailGroupItems>();
                queryWrapperItems.in(SendMailGroupItems::getFid,sendMailGroupDeleteBo.getIdList());
                SendMailGroupItems groupItems = new SendMailGroupItems();
                groupItems.setDeleteflag("2");
                itemsService.update(groupItems,queryWrapperItems);

            }else{
                throw new EmptyException("分组主键id列表不能为空");
            }
        }catch (Exception e){
            log.error("删除分组失败");
            throw e;
        }

    }

    /**
     * 获取当前用户下的分组列表
     * @param userId
     * @return
     */
    @Override
    public List<SendMailGroupVo> groupList(Long userId) {
        return this.groupMapper.groupList(userId);
    }

    /**
     * 修改分组（主子表同时修改）
     * @param sendMailGroupEditBo
     */
    @Override
    @Transactional
    public void updateGroupByEntity(SendMailGroupEditBo sendMailGroupEditBo) {
        log.info("修改分组");
        try{
            //判断当前的分组名称是否已存在
            SendMailGroup sendGroup = this.groupMapper.selectOne(new QueryWrapper<SendMailGroup>()
                    .eq("deleteflag","1")
                    .eq("addpersonid",sendMailGroupEditBo.getEditpersonid())
                    .eq("name",sendMailGroupEditBo.getName())
                    .notIn("id",sendMailGroupEditBo.getId()));
            if(sendGroup != null){//分组名称已存在，不允许重复的
                throw new DuplicateKeyException("数据已存在，操作失败");
            }else{
                SendMailGroup sendMailGroup = new SendMailGroup();
                //更新的分组主表中的内容
                sendMailGroup.setId(sendMailGroupEditBo.getId());
                sendMailGroup.setName(sendMailGroupEditBo.getName());
                sendMailGroup.setEditpersonid(sendMailGroupEditBo.getEditpersonid());
                sendMailGroup.setEditperson(sendMailGroupEditBo.getEditperson());
                LocalDateTime now = LocalDateTime.now();
                sendMailGroup.setEdittime(now);
                this.updateGroupById(sendMailGroup,sendMailGroupEditBo.getItemsList());
            }
        }catch (Exception e){
            log.error("修改分组失败");
            throw e;
        }
    }

    /**
     * 根据分组id查看详情
     * @param id
     * @return
     */
    @Override
    public SendMailGroupInfoVo getInfoById(Long id) {
        log.info("查询分组详情[{}]",id);
        SendMailGroupInfoVo vo = new SendMailGroupInfoVo();
        try{
            SendMailGroup group = this.groupMapper.selectById(id);
            if(group != null){
                vo.setId(id);
                vo.setName(group.getName());//分组名称
                LambdaQueryWrapper<SendMailGroupItems> itemsLambdaQueryWrapper = new LambdaQueryWrapper<>();
                itemsLambdaQueryWrapper.eq(SendMailGroupItems::getFid,id).eq(SendMailGroupItems::getDeleteflag,"1");
                vo.setItemsList(this.itemsService.list(itemsLambdaQueryWrapper));
            }else{
                throw new EmptyException("数据不能为空");
            }
        }catch (Exception e){
            log.error("查询分组详情失败");
            throw e;
        }
        return vo;
    }
}
