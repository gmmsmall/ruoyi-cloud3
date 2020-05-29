package com.ruoyi.javamail.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.javamail.bo.TemplateManagerBo;
import com.ruoyi.javamail.dao.TemplateManagerMapper;
import com.ruoyi.javamail.domain.FebsConstant;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.TemplateManager;
import com.ruoyi.javamail.service.ITemplateManagerService;
import com.ruoyi.javamail.util.FebsUtil;
import com.ruoyi.javamail.util.SortUtil;
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
 * @author jxd
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
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteTemplates(String[] ids) {
        List<String> list = Arrays.asList(ids);
        LambdaQueryWrapper<TemplateManager> queryWrapper = new LambdaQueryWrapper<>();
        if(list != null && list.size() > 0){
            queryWrapper.in(TemplateManager::getId,list);
        }
        TemplateManager templateManager = new TemplateManager();
        templateManager.setDeleteflag("2");
        /*templateManager.setDeleteperson(FebsUtil.getCurrentUser().getUsername());
        templateManager.setDeletepersonid(FebsUtil.getCurrentUser().getUserId());*/
        templateManager.setDeleteperson("小笨蛋");
        templateManager.setDeletepersonid(20200528L);
        templateManager.setDeletetime(LocalDateTime.now());
        this.baseMapper.update(templateManager,queryWrapper);
        //this.baseMapper.deleteBatchIds(list);
    }

    /**
     * 新增一个模板
     * @param templateManagerBo
     */
    @Transactional
    public void saveT(TemplateManagerBo templateManagerBo){
        LocalDateTime now = LocalDateTime.now();
        TemplateManager templateManager = new TemplateManager();
        BeanUtil.copyProperties(templateManagerBo,templateManager);
        templateManager.setAddtime(now);
        templateManager.setEdittime(now);//因为排序是按照修改时间来的，所以第一次添加时，新增时间即修改时间
        templateManager.setDeleteflag("1");//未删除
        templateManager.setUsenumber(0);//新增时使用次数为0
        this.baseMapper.insert(templateManager);
    }

    @Override
    @Transactional
    public void updateTById(TemplateManager templateManager) {
        this.baseMapper.updateById(templateManager);
    }

    /**
     * 根据主键id获取模板详情
     * @param id
     * @return
     */
    @Override
    public TemplateManagerVo getVoById(Long id) {
        TemplateManagerVo vo = new TemplateManagerVo();
        if(id != null && !id.equals("")){
            LambdaQueryWrapper<TemplateManager> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(TemplateManager::getId, id).eq(TemplateManager::getDeleteflag,"1");
            TemplateManager templateManager = this.templateManagerMapper.selectById(id);
            BeanUtil.copyProperties(templateManager,vo);
        }
        return vo;
    }

}
