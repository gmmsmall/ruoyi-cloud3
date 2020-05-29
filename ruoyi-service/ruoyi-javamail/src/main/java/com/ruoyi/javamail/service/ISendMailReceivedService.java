package com.ruoyi.javamail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.SendMailReceived;

/**
 * @author gmm
 */
public interface ISendMailReceivedService extends IService<SendMailReceived> {

    public void saveMailReceived(SendMailReceived sendMailReceived);

    IPage<SendMailReceived> findSendMailReceived(QueryRequest request, SendMailReceived sendMailReceived);

}
