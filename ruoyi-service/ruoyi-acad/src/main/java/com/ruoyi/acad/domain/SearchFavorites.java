package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.acad.client.ClientSearchCriteria;
import com.ruoyi.acad.utils.JsonUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description：创建搜索收藏夹<br/>
 * CreateTime ：2020年3月26日下午4:12:15<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_search_favorites")
public class SearchFavorites implements Serializable {

	@ApiModelProperty("用户id")
	private Integer userId;

	@ApiModelProperty("收藏夹名称")
	private String name;//收藏夹名称

	@ApiModelProperty("收藏内容条件")
    private String content;//收藏内容条件

	@ApiModelProperty("创建时间")
    private LocalDateTime crrateTime;

	@ApiModelProperty("parentId")
    private Long parentId;

	@ApiModelProperty("收藏夹具体内容")
    private transient ClientSearchCriteria clientSearchCriteria;
    
	public void setContent(ClientSearchCriteria clientSearchCriteria) {
		this.clientSearchCriteria = clientSearchCriteria;
		if (null != clientSearchCriteria) {
			this.content = JsonUtils.toJson(clientSearchCriteria);
		}
	}

	public void setClientSearchCriteria(String content) {
		this.content = content;
		if (StringUtils.isNotBlank(content)) {
			this.clientSearchCriteria = JsonUtils.fromJson(content, new TypeReference<ClientSearchCriteria>(){});
		} else {
			this.clientSearchCriteria = new ClientSearchCriteria();
		}
	}
}
