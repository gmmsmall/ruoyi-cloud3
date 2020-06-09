package com.ruoyi.acad.client;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description：基本搜索条件<br/>
 * CreateTime ：2020年3月26日下午1:59:59<br/>
 * CreateUser：ys<br/>
 */
@Data
@ApiModel(value = "搜索条件集合")
public class ClientSearchCriteria {

	/* baseinfo */
	@ApiModelProperty(value = "院士姓名", dataType = "String")
	private String acadName;// 院士姓名

	@ApiModelProperty(value = "生日开始日期", dataType = "String")
	private String startBirthday;// 查询生日开始日期

	@ApiModelProperty(value = "生日结束日期", dataType = "String")
	private String endBrithday;// 查询生日结束日期

	@ApiModelProperty(value = "是否展示0-否，1-是", dataType = "Boolean")
	private Boolean isShow;// 是否展示

	@ApiModelProperty(value = "生活习惯", dataType = "String")
	private String livingHabit;// 生活习惯

	/*@ApiModelProperty(value = "宗教信仰", dataType = "String")
	private String religion;// 宗教信仰*/

	/*@ApiModelProperty(value = "籍贯", dataType = "String")
	private String nativePlace;// 籍贯*/

	@ApiModelProperty(value = "授衔机构", dataType = "String")
	private String aosName;// 授衔机构

	@ApiModelProperty(value = "国籍", dataType = "String")
	private String nationPlace;// 国籍

	@ApiModelProperty(value = "专业领域1-高端装备制造，2-生物医药，3-新能源新材料，4-网络信息，5-设计研发，6-海洋经济，7-军民融合，8-其他", dataType = "Integer")
	private Integer rsfCategory;// 专业领域

	@ApiModelProperty(value = "签约情况1-全职，2-刚性，3-柔性，4-注册，5-其他", dataType = "Integer")
	private Integer contactStatus;// 签约情况

	@ApiModelProperty(value = "联络情况1-邮箱，2-电话，3-邮箱/电话，4-未联络", dataType = "Integer")
	private Integer contactMethon;// 联络情况
	
	/* baseInfo end */
	

	/* phone/email  */
	@ApiModelProperty(value = "邮箱", dataType = "String")
	private String email;// 邮箱

	@ApiModelProperty(value = "电话", dataType = "String")
	private String phone;// 电话
	
	/* phone/email end */

	/*@ApiModelProperty(value = "本科毕业开始年", dataType = "String")
	private String startUndergraduate;// 查询本科毕业开始年

	@ApiModelProperty(value = "本科毕业结束年", dataType = "String")
	private String endUndergraduate;// 查询本科毕业结束年

	@ApiModelProperty(value = "研究生毕业开始年", dataType = "String")
	private String startGraduate;// 查询研究生毕业开始年

	@ApiModelProperty(value = "研究生毕业结束年", dataType = "String")
	private String endGraduate;// 查询研究生毕业结束年*/

	@ApiModelProperty(value = "工作单位名称", dataType = "String")
	private String workName;// 工作单位名称

	/*@ApiModelProperty(value = "参加工作开始年", dataType = "String")
	private String startWorkTime;// 查询开始工作开始年

	@ApiModelProperty(value = "参加工作开结束年", dataType = "String")
	private String endWorkTime;// 查询开始工作结束年

	@ApiModelProperty(value = "博士毕业开始年", dataType = "String")
	private String startPHDGraduation;// 查询 博士毕业开始年

	@ApiModelProperty(value = "博士毕业结束年", dataType = "String")
	private String endPHDGraduation;// 查询 博士毕业结束年

	@ApiModelProperty(value = "院士当选年开始年", dataType = "String")
	private String startElected;// 查询 院士当选年开始年

	@ApiModelProperty(value = "院士当选年结束年", dataType = "String")
	private String endElected;// 查询 院士当选年结束年*/
}