package com.ruoyi.acad.client;

import com.ruoyi.acad.domain.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

/**
 * Description：基本信息入参类<br/>
 * CreateTime ：2020年3月23日上午11:19:51<br/>
 * CreateUser：ys<br/>
 */
@Data
@Document(indexName = "academician",type ="baseInfo")
public class ClientAcad implements Serializable {

	@Id
	private String acadId;

	private BaseInfo baseInfo;//基本信息
	
	/*private Name name;*/
	
	private List<Aos> aosList;//科学院对应
	
	private List<Email> emailList;//邮箱

	private List<Phone> phoneList;//电话

	private List<Sns> snsList;//社交联系方式
	
	private List<Education> educationList;//教育信息
	
	private List<Work> workList;//工作列表
	
	private List<Award> awardList;//荣誉信息集合
	
	private List<Paper> paperList;//论文集合
	
	private List<Patent> patentList;//专利和发明表

	private List<Nationality> nationalityList;
}
