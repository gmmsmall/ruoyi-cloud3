package com.ruoyi.acad.dao;

import com.ruoyi.acad.domain.MstCountry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 国籍公共数据类
 * @Author guomiaomiao
 * @Date 2020/6/9 10:01
 * @Version 1.0
 */
@Repository
public interface MstCountryMapper extends BaseMapper<MstCountry> {

    /**
     * 根据大洲编码查询国家列表
     * @param code
     * @return
     */
    @Select("select * from acad_mst_country where continent_short_name=#{code};")
    List<MstCountry> getCountryByType(@Param("code") String code);

}
