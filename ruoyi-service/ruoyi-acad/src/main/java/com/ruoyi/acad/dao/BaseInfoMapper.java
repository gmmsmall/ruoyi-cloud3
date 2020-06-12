package com.ruoyi.acad.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.acad.domain.BaseInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
	@Select("SELECT acad_id FROM acad_base_info WHERE cn_name LIKE '%${name}%' or en_name LIKE '%${name}%' or real_name LIKE '%${name}%' and del_flag = 1;")
	List<Integer> getAcadListByName(@Param("name") String name);

	/**
	 * 删除院士信息~删除标记0-已删除，1-未删除
	 * @param acadId
	 * @param userId
	 * @param deleteTime
	 * @return
	 */
	@Update("UPDATE acad_base_info SET del_flag = 0,del_user_id=#{userId},del_datetime=#{deleteTime} WHERE acad_id = #{acadId};")
	int deleteBaseInfo(@Param("acadId") Integer acadId,@Param("userId") Long userId,@Param("deleteTime") Date deleteTime);
}
