package com.ruoyi.acad.client;

import lombok.Data;

/**
 * Description：基本搜索条件<br/>
 * CreateTime ：2020年3月26日下午1:59:59<br/>
 * CreateUser：ys<br/>
 */
@Data
public class ClientSearchCriteria {

	/* baseinfo */
	private String acadName;// 院士姓名

	private String startBirthday;// 查询生日开始日期

	private String endBrithday;// 查询生日结束日期

	private Boolean isShow;// 是否展示

	private String livingHabit;// 生活习惯

	private String religion;// 宗教信仰

	private String nativePlace;// 籍贯

	private Integer rsfCategory;// 专业领域

	private Integer contactStatus;// 签约情况
	
	private Integer contactMethon;// 联络情况
	
	/* baseInfo end */
	

	/* phone/email  */

	private String email;// 邮箱

	private String phone;// 电话
	
	/* phone/email end */
	

	private String startUndergraduate;// 查询本科毕业开始年

	private String endUndergraduate;// 查询本科毕业结束年

	private String startGraduate;// 查询研究生毕业开始年

	private String endGraduate;// 查询研究生毕业结束年

	private String workName;// 工作单位名称

	private String startWorkTime;// 查询开始工作 开始年

	private String endWorkTime;// 查询开始工作 结束年

	private String startPHDGraduation;// 查询 博士毕业 开始年

	private String endPHDGraduation;// 查询 博士毕业 结束年

	private String startElected;// 查询 院士当选年开始年

	private String endElected;// 查询 院士当选年结束年
}