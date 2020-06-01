package com.ruoyi.acad.client;

import com.ruoyi.acad.domain.Award;
import com.ruoyi.acad.domain.Education;
import lombok.Data;

import java.util.List;

/**
 * Description：列表返回信息<br/>
 * CreateTime ：2020年3月25日下午5:37:09<br/>
 * CreateUser：ys<br/>
 */
@Data
public class ClientResponse {

	private ClientBaseInfo clientBaseInfo;
	
	private ClientEmail clientEmail;
	
	private ClientPaper clientPaper;
	
	private List<Education> educationList;
	
	private List<Award> awardList;
	
	private List<ClientAos> clientAosList;
}
