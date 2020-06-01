package com.ruoyi.acad.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.acad.domain.BaseInfo;

/**
 * Description：创建基本信息dao<br/>
 * CreateTime ：2020年3月11日上午10:16:07<br/>
 * CreateUser：ys<br/>
 */
public interface BaseInfoMapper extends BaseMapper<BaseInfo> {

	/**
	 * Description:获取最后一位ID
	 * CreateTime:2020年3月23日下午3:42:36
	 * @return
	 * @throws Exception
	 */
	Integer selectMaxId() throws Exception;
}
