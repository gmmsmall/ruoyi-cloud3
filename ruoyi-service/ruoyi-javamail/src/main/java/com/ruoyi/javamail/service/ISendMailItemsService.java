package com.ruoyi.javamail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.SendMailItems;

import java.util.List;

/**
 * @author jxd
 */
public interface ISendMailItemsService extends IService<SendMailItems> {

    IPage<SendMailItems> findSendMailItems(QueryRequest request, SendMailItems sendMailItems);

    List<SendMailItems> getListByFid(Long fid, String sendflag);

    public IPage<SendMailItems> getSendMailItemsPage(QueryRequest request, SendMailItems sendMailItems);

}
