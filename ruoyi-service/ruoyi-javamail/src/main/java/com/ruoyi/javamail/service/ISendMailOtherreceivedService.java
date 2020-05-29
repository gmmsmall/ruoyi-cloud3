package com.ruoyi.javamail.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.SendMailOtherreceived;

/**
 * @author gmm
 */
public interface ISendMailOtherreceivedService extends IService<SendMailOtherreceived> {

    IPage<SendMailOtherreceived> findSendMailOtherReceived(QueryRequest request, SendMailOtherreceived sendMailOtherreceived);

}
