package com.ruoyi.acad.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.acad.domain.BaseInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description：创建基本信息dao<br/>
 * CreateTime ：2020年3月11日上午10:16:07<br/>
 * CreateUser：ys<br/>
 */
@Repository
public interface BaseInfoMapper extends BaseMapper<BaseInfo> {

	/**
	 * Description:获取最后一位ID
	 * CreateTime:2020年3月23日下午3:42:36
	 * @return
	 * @throws Exception
	 */
	Integer selectMaxId() throws Exception;

	/**
	 * 根据院士姓名模糊查询院士编码列表
	 * @param name
	 * @return
	 */
	@Select("SELECT acad_id FROM acad_base_info WHERE cn_name LIKE '%${name}%' or en_name LIKE '%${name}%' or real_name LIKE '%${name}%' ;")
	List<Integer> getAcadListByName(@Param("name") String name);
}
