package com.ruoyi.acad.client;

import com.ruoyi.acad.domain.Paper;
import lombok.Data;

import java.util.List;

/**
 * Description：院士论文辅助类<br/>
 * CreateTime ：2020年3月25日下午5:00:21<br/>
 * CreateUser：ys<br/>
 */
@Data
public class ClientPaper {

	private Integer indexAll;//引用数量
	
	private Integer IEEECount;
	
	private Integer EICount;
	
	private Integer sciCount;
	
	private List<Paper> paperList;
}
