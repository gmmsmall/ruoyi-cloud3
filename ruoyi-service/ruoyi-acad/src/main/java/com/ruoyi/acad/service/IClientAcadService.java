package com.ruoyi.acad.service;

import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.client.ClientSearchCriteria;
import com.ruoyi.acad.domain.QueryRequest;
import org.springframework.data.domain.Page;

/**
 * Description：查询院士信息service接口层<br/>
 * CreateTime ：2020年3月11日上午10:11:31<br/>
 * CreateUser：ys<br/>
 */
public interface IClientAcadService {

	/**
	 * Description:根据条件获取对应对象集合
	 * CreateTime:2020年3月11日上午10:13:52
	 * @return
	 * @throws Exception
	 */
	Page<ClientAcad> getBaseInfoList(QueryRequest queryRequest, ClientSearchCriteria clientSearchCriteria) throws Exception;

}
