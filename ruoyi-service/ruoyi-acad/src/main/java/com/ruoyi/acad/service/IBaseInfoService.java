package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.client.ClientSearchCriteria;
import com.ruoyi.acad.domain.BaseInfo;
import com.ruoyi.acad.domain.QueryRequest;
import com.ruoyi.acad.form.BaseInfoForm;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Description：创建基本信息service接口层<br/>
 * CreateTime ：2020年3月11日上午10:11:31<br/>
 * CreateUser：ys<br/>
 */
public interface IBaseInfoService extends IService<BaseInfo> {

	/**
	 * Description:拉黑院士操作
	 * CreateTime:2020年3月12日下午1:16:34
	 * @param baseInfoForm
	 * @return
	 * @throws Exception
	 */
	String pullBlack(BaseInfoForm baseInfoForm) throws Exception;

	/**
	 * Description:
	 * CreateTime:2020年3月12日下午1:29:16
	 * @param acadId
	 * @param isShow
	 * @return
	 * @throws Exception
	 */
	String showBaseInfo(@NotBlank(message = "{required}") Integer acadId, Boolean isShow) throws Exception;

	/**
	 * Description:根据ID获取对应数据
	 * CreateTime:2020年3月13日上午11:19:32
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	public BaseInfo getModelById(Integer acadId) throws Exception;

	/**
	 * Description:修改基本信息
	 * CreateTime:2020年3月19日下午1:59:10
	 * @return
	 * @throws Exception
	 */
	public void updateBaseInfo(BaseInfo baseInfo) throws Exception;

	/**
	 * Description:新增基本信息
	 * CreateTime:2020年3月19日下午1:59:10
	 * @return
	 * @throws Exception
	 */
	public Integer saveModel(BaseInfo baseInfo) throws Exception;

	/**
	 * Description:根据院士姓名模糊查询院士编码列表
	 * CreateTime:2020年6月5日上午09:50:01
	 * @return
	 * @throws Exception
	 */
	public List<Integer> getAcadListByName(String name) throws Exception;

}
