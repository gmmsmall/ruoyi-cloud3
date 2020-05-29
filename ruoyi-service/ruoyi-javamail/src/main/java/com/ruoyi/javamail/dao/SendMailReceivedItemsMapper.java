package com.ruoyi.javamail.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.javamail.entity.SendMailReceivedItems;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author gmm
 */
@Repository
public interface SendMailReceivedItemsMapper extends BaseMapper<SendMailReceivedItems> {

    public List<SendMailReceivedItems> getReceivedItemsList(Map<String, Object> map);

    public List<Map<String,Object>> getMailExchangebyAcademial(@Param("email") String email);

}
