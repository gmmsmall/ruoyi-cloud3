package com.ruoyi.acad.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.acad.domain.Photo;
import com.ruoyi.acad.form.PhotoForm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jxd
 */
@Repository
public interface PhotoMapper extends BaseMapper<Photo> {

    @Select("select photo_id,acad_id,photo_url,is_hall,ai_gender from acad_photo where del_flag = 1 and acad_id = #{acadId};")
    List<PhotoForm> getPhotoFormList(@Param("acadId") Long acadId);

}
