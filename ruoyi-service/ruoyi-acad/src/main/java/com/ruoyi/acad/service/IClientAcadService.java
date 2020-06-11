package com.ruoyi.acad.service;

import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.client.ClientSearchCriteria;
import com.ruoyi.acad.domain.QueryRequest;
import com.ruoyi.acad.form.BaseInfoPage;
import com.ruoyi.acad.form.BaseInfoShowForm;
import org.springframework.data.domain.Page;

import java.util.Map;

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
	BaseInfoPage getBaseInfoList(QueryRequest queryRequest, ClientSearchCriteria clientSearchCriteria) throws Exception;

	BaseInfoPage wholeWordSearch(QueryRequest queryRequest, String wholeWord) throws Exception;

	/**
	 * 根据院士编码，获取该院士的所有信息
	 * @param acadId
	 * @return
	 */
	public ClientAcad getClientAcadByacadId(Integer acadId);

}
