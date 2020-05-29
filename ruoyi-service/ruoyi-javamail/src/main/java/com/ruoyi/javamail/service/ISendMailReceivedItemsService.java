package com.ruoyi.javamail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.SendMailReceivedItems;

import java.util.List;
import java.util.Map;

/**
 * @author gmm
 */
public interface ISendMailReceivedItemsService extends IService<SendMailReceivedItems> {

    IPage<SendMailReceivedItems> findSendMailReceivedItems(QueryRequest request, SendMailReceivedItems sendMailReceivedItems);

    IPage<SendMailReceivedItems> getReceivedItemsList(QueryRequest request, SendMailReceivedItems sendMailReceivedItems);

    public List<Map<String,Object>> getMailExchangebyAcademial(String email);

}
