package com.ruoyi.acad.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.acad.domain.Attachment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author jxd
 */
@Repository
public interface AttachmentMapper extends BaseMapper<Attachment> {

    @Delete("delete from acad_attachment where attachment_id=#{id};")
    void deleteModelById(@Param("id") Long id);

}
