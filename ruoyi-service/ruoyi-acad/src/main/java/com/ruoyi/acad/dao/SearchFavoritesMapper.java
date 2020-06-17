package com.ruoyi.acad.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.acad.domain.SearchFavorites;
import com.ruoyi.acad.form.SearchFavoritesForm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gmm
 */
@Repository
public interface SearchFavoritesMapper extends BaseMapper<SearchFavorites> {

    @Select("select * from acad_search_favorites where user_id=#{userId}; ")
    public List<SearchFavoritesForm> getFormList(@Param("userId") Integer userId);

}
