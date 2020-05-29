package com.ruoyi.javamail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.SendMail;

/**
 * @author jxd
 */
public interface ISendMailService extends IService<SendMail> {

    void saveSendMail(SendMail sendMail);

    IPage<SendMail> findSendMail(QueryRequest request, SendMail sendMail);

}
