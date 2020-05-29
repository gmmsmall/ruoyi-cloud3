package com.ruoyi.javamail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.javamail.bo.TemplateManagerBo;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.TemplateManager;
import com.ruoyi.javamail.vo.TemplateManagerVo;

/**
 * @author jxd
 */
public interface ITemplateManagerService extends IService<TemplateManager> {

    IPage<TemplateManager> findTemplate(QueryRequest request, TemplateManagerBo templateManagerBo);

    void deleteTemplates(String[] jobIds);

    void saveT(TemplateManagerBo templateManagerBo);

    void updateTById(TemplateManager templateManager);

    /**
     * 根据主键id获取模板详情
     * @param id
     * @return
     */
    TemplateManagerVo getVoById(Long id);

}
