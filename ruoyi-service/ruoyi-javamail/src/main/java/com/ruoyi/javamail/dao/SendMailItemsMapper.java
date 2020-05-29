package com.ruoyi.javamail.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.javamail.entity.SendMailItems;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author gmm
 */
@Repository
public interface SendMailItemsMapper extends BaseMapper<SendMailItems> {

    public List<SendMailItems> getSendMailItemsList(Map<String, Object> map);

    public List<Integer> getUseableAcadIdOfSendMailItems(@Param("str") String str);

}
