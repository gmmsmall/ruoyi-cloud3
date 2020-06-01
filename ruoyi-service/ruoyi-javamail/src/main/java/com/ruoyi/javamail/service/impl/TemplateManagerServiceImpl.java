package com.ruoyi.javamail.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.javamail.bo.TemplateManagerBo;
import com.ruoyi.javamail.bo.TemplateManagerDeleteBo;
import com.ruoyi.javamail.bo.TemplateManagerEditBo;
import com.ruoyi.javamail.dao.TemplateManagerMapper;
import com.ruoyi.javamail.domain.FebsConstant;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.TemplateManager;
import com.ruoyi.javamail.service.ITemplateManagerService;
import com.ruoyi.javamail.util.FebsUtil;
import com.ruoyi.javamail.util.SortUtil;
import com.ruoyi.javamail.vo.TemplateManagerPublishVo;
import com.ruoyi.javamail.vo.TemplateManagerVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author gmm
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TemplateManagerServiceImpl extends ServiceImpl<TemplateManagerMapper, TemplateManager> implements ITemplateManagerService {

    @Autowired
    private TemplateManagerMapper templateManagerMapper;

    @Override
    public IPage<TemplateManager> findTemplate(QueryRequest request, TemplateManagerBo templateManagerBo) {
        try {
            LambdaQueryWrapper<TemplateManager> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.isNotBlank(templateManagerBo.getName())) {
                queryWrapper.like(TemplateManager::getName, templateManagerBo.getName());
            }
            if (StringUtils.isNotBlank(templateManagerBo.getTopic())) {
                queryWrapper.like(TemplateManager::getTopic, templateManagerBo.getTopic());
            }
            if (StringUtils.isNotBlank(templateManagerBo.getType())) {
                queryWrapper.eq(TemplateManager::getType, templateManagerBo.getType());
            }
            if (StringUtils.isNotBlank(templateManagerBo.getWorktype())) {
                queryWrapper.eq(TemplateManager::getWorktype, templateManagerBo.getWorktype());
            }
            if (StringUtils.isNotBlank(templateManagerBo.getPublish())) {
                queryWrapper.eq(TemplateManager::getPublish, templateManagerBo.getPublish());
            }
            if (StringUtils.isNotBlank(templateManagerBo.getEffective())) {
                queryWrapper.eq(TemplateManager::getEffective, templateManagerBo.getEffective());
            }
            queryWrapper.eq(TemplateManager::getDeleteflag,"1");

            Page<TemplateManager> page = new Page<>(request.getPageNum(), request.getPageSize());
            SortUtil.handlePageSort(request, page, "edittime", FebsConstant.ORDER_DESC, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取模板列表失败", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteTemplates(TemplateManagerDeleteBo bo) {
        log.error("删除模板");
        try{
            LambdaQueryWrapper<TemplateManager> queryWrapper = new LambdaQueryWrapper<>();
            if(bo != null && CollUtil.isNotEmpty(bo.getIdList())){
                queryWrapper.in(TemplateManager::getId,bo.getIdList());
            }
            TemplateManager templateManager = new TemplateManager();
            templateManager.setDeleteflag("2");
            LocalDateTime now = LocalDateTime.now();
            templateManager.setDeleteperson(bo.getAddperson());
            templateManager.setDeletepersonid(bo.getAddpersonid());
            templateManager.setDeletetime(now);
            templateManager.setEdittime(now);
            this.baseMapper.update(templateManager,queryWrapper);
            //this.baseMapper.deleteBatchIds(list);
        }catch (Exception e){
            log.error("删除模板失败");
            throw e;
        }
    }

    /**
     * 新增一个模板
     * @param templateManagerBo
     */
    @Transactional
    public void saveT(TemplateManagerBo templateManagerBo){
        log.info("新增一个模板");
        try{
            LocalDateTime now = LocalDateTime.now();
            TemplateManager templateManager = new TemplateManager();
            BeanUtil.copyProperties(templateManagerBo,templateManager);
            templateManager.setAddtime(now);
            templateManager.setEdittime(now);//因为排序是按照修改时间来的，所以第一次添加时，新增时间即修改时间
            templateManager.setDeleteflag("1");//未删除
            templateManager.setUsenumber(0);//新增时使用次数为0
            this.baseMapper.insert(templateManager);
        }catch (Exception e){
            log.info("新增一个模板失败");
            throw e;
        }
    }

    /**
     * 修改一个模板
     * @param templateManagerEditBo
     */
    @Override
    @Transactional
    public void updateTById(TemplateManagerEditBo templateManagerEditBo) {
        log.info("修改模板[{}]"+templateManagerEditBo.getId());
        try{
            templateManagerEditBo.setEdittime(LocalDateTime.now());
            TemplateManager templateManager = new TemplateManager();
            BeanUtil.copyProperties(templateManagerEditBo,templateManager);
            this.baseMapper.updateById(templateManager);
        }catch (Exception e){
            log.error("修改模板失败");
            throw e;
        }
    }

    /**
     * 根据主键id获取模板详情
     * @param id
     * @return
     */
    @Override
    public TemplateManagerVo getVoById(Long id) {
        log.info("根据主键id获取模板详情[{}]"+id);
        TemplateManagerVo vo = new TemplateManagerVo();
        try{
            if(id != null && !id.equals("")){
                LambdaQueryWrapper<TemplateManager> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(TemplateManager::getId, id).eq(TemplateManager::getDeleteflag,"1");
                TemplateManager templateManager = this.templateManagerMapper.selectById(id);
                BeanUtil.copyProperties(templateManager,vo);
            }
        }catch (Exception e){
            log.info("查询模板详情异常[{}]"+id);
            throw e;
        }
        return vo;
    }

    /**
     * 获取已发布的模板列表
     * @return
     */
    @Override
    public List<TemplateManagerPublishVo> publisht() {
        log.info("获取已发布的模板列表");
        try{
            return this.templateManagerMapper.publishlist();
        }catch (Exception e){
            log.error("获取已发布的模板列表失败");
            throw e;
        }
    }

}
