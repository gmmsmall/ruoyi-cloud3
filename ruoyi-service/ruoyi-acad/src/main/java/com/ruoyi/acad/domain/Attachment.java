package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description：院士附件表<br/>
 * CreateTime ：2020年3月11日上午10:01:14<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_attachment")
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer acadId;

    private Integer attachmentId;//附件ID

    private String attachmentName;//附件名称

    private String attachmentUrl;//附件地址

    private String uploadUserId;//上传用户


}
