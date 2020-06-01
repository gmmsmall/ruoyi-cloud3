package com.ruoyi.javamail.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.javamail.entity.TemplateManager;
import com.ruoyi.javamail.vo.TemplateManagerPublishVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gmm
 */
@Repository
public interface TemplateManagerMapper extends BaseMapper<TemplateManager> {

    @Select("select id,NAME,topic,worktype from template_manager where publish=1 and deleteflag=1 and effective=1 and type=1 order by edittime desc ;")
    public List<TemplateManagerPublishVo> publishlist();

}
