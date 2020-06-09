package com.ruoyi.acad.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.acad.domain.MstAos;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 科学院公共数据类
 * @Author guomiaomiao
 * @Date 2020/6/9 10:01
 * @Version 1.0
 */
@Repository
public interface MstAosMapper extends BaseMapper<MstAos> {

    /**
     * 根据大洲编码查询科学院列表
     * @param code
     * @return
     */
    @Select("select * from acad_mst_aos where continent_short_name=#{code};")
    List<MstAos> getAosByType(@Param("code") String code);

}
