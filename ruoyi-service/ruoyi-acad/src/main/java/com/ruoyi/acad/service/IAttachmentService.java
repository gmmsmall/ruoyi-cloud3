package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Attachment;

import java.util.List;

/**
 * @author jxd
 */
public interface IAttachmentService extends IService<Attachment> {

	/**
	 * Description:根据ID获取对应获奖信息列表
	 * CreateTime:2020年3月20日下午2:54:24
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	List<Attachment> getModelById(Integer acadId) throws Exception;

	/**
	 * Description:附件上传
	 * CreateTime:2020年3月23日下午12:00:51
	 * @param attachment
	 * @return
	 * @throws Exception
	 */
	void saveModel(Attachment attachment, Integer acadId) throws Exception;

	/**
	 * Description:附件删除
	 * CreateTime:2020年3月23日下午12:01:25
	 * @param attachment
	 * @return
	 * @throws Exception
	 */
	void deleteModel(Attachment attachment) throws Exception;

}
