package com.ruoyi.javamail.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author gmm
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SendMailRedis implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 邮件发送主表id
     */
    private Long fid;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 邮箱密码
     */
    private String password;

    /**
     * stmp服务器类型
     */
        @TableField("sendtype")
    private String sendtype;

    /**
     * 有效标记，1和2有效，其他无效，1表示第一次发，2表示二次授权后发的
     */
    private String flag;

    /**
     * 删除标记，1未删  2已删
     */
    private String deleteflag;

    /**
     * 新增时间
     */
    private LocalDateTime addtime;

    /**
     * 新增操作人
     */
    private String addperson;

    private Long addpersonid;

    /**
     * 删除时间
     */
    private LocalDateTime deletetime;


}
