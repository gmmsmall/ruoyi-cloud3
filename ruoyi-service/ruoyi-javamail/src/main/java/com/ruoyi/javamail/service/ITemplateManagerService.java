package com.ruoyi.javamail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.javamail.bo.TemplateManagerBo;
import com.ruoyi.javamail.bo.TemplateManagerDeleteBo;
import com.ruoyi.javamail.bo.TemplateManagerEditBo;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.TemplateManager;
import com.ruoyi.javamail.vo.TemplateManagerPublishVo;
import com.ruoyi.javamail.vo.TemplateManagerVo;

import java.util.List;

/**
 * @author gmm
 */
public interface ITemplateManagerService extends IService<TemplateManager> {

    IPage<TemplateManager> findTemplate(QueryRequest request, TemplateManagerBo templateManagerBo);

    void deleteTemplates(TemplateManagerDeleteBo bo);

    void saveT(TemplateManagerBo templateManagerBo);

    void updateTById(TemplateManagerEditBo templateManagerEditBo);

    /**
     * 根据主键id获取模板详情
     * @param id
     * @return
     */
    TemplateManagerVo getVoById(Long id);

    /**
     * 获取已发布的模板列表
     * @return
     */
    List<TemplateManagerPublishVo> publisht();

}
