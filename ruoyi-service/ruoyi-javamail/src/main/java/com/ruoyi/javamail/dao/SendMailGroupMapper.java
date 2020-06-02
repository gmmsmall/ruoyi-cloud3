package com.ruoyi.javamail.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.javamail.entity.SendMailGroup;
import com.ruoyi.javamail.vo.SendMailGroupVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gmm
 */
@Repository
public interface SendMailGroupMapper extends BaseMapper<SendMailGroup> {

    /**
     * 查询当前用户下的分组列表
     * @param userId
     * @return
     */
    @Select("SELECT id,name from send_mail_group where deleteflag=1 and addpersonid=#{userId} order by edittime desc;")
    public List<SendMailGroupVo> groupList(@Param("userId") Long userId);

}
