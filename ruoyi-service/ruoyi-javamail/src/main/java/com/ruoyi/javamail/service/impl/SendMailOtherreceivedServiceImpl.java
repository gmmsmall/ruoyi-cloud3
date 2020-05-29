package com.ruoyi.javamail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.javamail.dao.SendMailOtherreceivedMapper;
import com.ruoyi.javamail.domain.FebsConstant;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.SendMailOtherreceived;
import com.ruoyi.javamail.service.ISendMailOtherreceivedService;
import com.ruoyi.javamail.util.SortUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author gmm
 */
@Service
@Slf4j
public class SendMailOtherreceivedServiceImpl extends ServiceImpl<SendMailOtherreceivedMapper, SendMailOtherreceived> implements ISendMailOtherreceivedService {

    @Override
    public IPage<SendMailOtherreceived> findSendMailOtherReceived(QueryRequest request, SendMailOtherreceived sendMailOtherreceived) {
        try {
            LambdaQueryWrapper<SendMailOtherreceived> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.isNotBlank(sendMailOtherreceived.getMailtopic())) {
                queryWrapper.like(SendMailOtherreceived::getMailtopic, sendMailOtherreceived.getMailtopic());
            }
            if (sendMailOtherreceived.getAddpersonid() != null && !sendMailOtherreceived.getAddpersonid().equals("")) {
                queryWrapper.eq(SendMailOtherreceived::getAddpersonid, sendMailOtherreceived.getAddpersonid());
            }
            if (StringUtils.isNotBlank(sendMailOtherreceived.getSendemail())) {
                queryWrapper.like(SendMailOtherreceived::getSendemail, sendMailOtherreceived.getSendemail());
            }
            if (StringUtils.isNotBlank(sendMailOtherreceived.getIsnote())) {
                queryWrapper.eq(SendMailOtherreceived::getIsnote, sendMailOtherreceived.getIsnote());
            }
            if (StringUtils.isNotBlank(sendMailOtherreceived.getCreateTimeFrom())) {
                queryWrapper.ge(SendMailOtherreceived::getReceivetime, sendMailOtherreceived.getCreateTimeFrom());
            }
            if (StringUtils.isNotBlank(sendMailOtherreceived.getCreateTimeTo())) {
                queryWrapper.le(SendMailOtherreceived::getReceivetime, sendMailOtherreceived.getCreateTimeTo());
            }

            Page<SendMailOtherreceived> page = new Page<>(request.getPageNum(), request.getPageSize());
            SortUtil.handlePageSort(request, page, "receivetime", FebsConstant.ORDER_DESC, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取模板列表失败", e);
            return null;
        }
    }
}
