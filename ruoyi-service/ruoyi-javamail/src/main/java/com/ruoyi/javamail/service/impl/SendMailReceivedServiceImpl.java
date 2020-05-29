package com.ruoyi.javamail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.javamail.dao.SendMailReceivedMapper;
import com.ruoyi.javamail.domain.FebsConstant;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.SendMailReceived;
import com.ruoyi.javamail.service.ISendMailReceivedService;
import com.ruoyi.javamail.util.SortUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gmm
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SendMailReceivedServiceImpl extends ServiceImpl<SendMailReceivedMapper, SendMailReceived> implements ISendMailReceivedService {

    @Override
    @Transactional
    public void saveMailReceived(SendMailReceived sendMailReceived) {
        this.baseMapper.insert(sendMailReceived);

    }

    @Override
    public IPage<SendMailReceived> findSendMailReceived(QueryRequest request, SendMailReceived sendMailReceived) {
        try {
            LambdaQueryWrapper<SendMailReceived> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.isNotBlank(sendMailReceived.getMailtopic())) {
                queryWrapper.like(SendMailReceived::getMailtopic, sendMailReceived.getMailtopic());
            }
            if (StringUtils.isNotBlank(sendMailReceived.getMailname())) {
                queryWrapper.like(SendMailReceived::getMailname, sendMailReceived.getMailname());
            }
            if (sendMailReceived.getSendnumber() != null) {
                queryWrapper.eq(SendMailReceived::getSendnumber, sendMailReceived.getSendnumber());
            }
            /*if (sendMailReceived.getAddpersonid() != null && !sendMailReceived.getAddpersonid().equals("")) {
                queryWrapper.eq(SendMailReceived::getAddpersonid, sendMailReceived.getAddpersonid());
            }*/
            if (StringUtils.isNotBlank(sendMailReceived.getCreateTimeFrom())) {
                queryWrapper.ge(SendMailReceived::getAddtime, sendMailReceived.getCreateTimeFrom());
            }
            if (StringUtils.isNotBlank(sendMailReceived.getCreateTimeTo())) {
                queryWrapper.le(SendMailReceived::getAddtime, sendMailReceived.getCreateTimeTo());
            }
            //需要加数据过滤
            //1、调用用户中心接口，查询当前用户可以查看的科学院
            //2、调用院士信息接口，根据科学院查询出该院下所有院士的邮箱（主、副邮箱都有），最终是一个List<String>列表
            //3、在邮件接收子表中根据这些院士邮箱，查询出fid列表
            List<String> emailList = new ArrayList<>();//先假设这是最终查询出的院士邮箱列表
            emailList.add("1");
            emailList.add("1@qq.com");
            emailList.add("dfdjfdj@qq.com");
            if(emailList != null && emailList.size() > 0) {
                String str = convertListToString(emailList);
                queryWrapper.inSql(SendMailReceived::getId,"select i.fid from send_mail_received_items as i where i.email in("+str+") and i.fid is not null and i.fid != ''");
                Page<SendMailReceived> page = new Page<>(request.getPageNum(), request.getPageSize());
                SortUtil.handlePageSort(request, page, "addtime", FebsConstant.ORDER_DESC, true);
                return this.page(page, queryWrapper);
            }else{
                return null;
            }
        } catch (Exception e) {
            log.error("获取主题邮件接收列表失败", e);
            return null;
        }
    }

    /**
     * 将List<String>集合 转化为String
     * 如{"aaa","bbb"} To 'aaa','bbb'
     */
    public static String convertListToString(List<String> strlist){
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<strlist.size();i++) {
            if(i==0){
                sb.append("'").append(strlist.get(i)).append("'");
            }else{
                sb.append(",").append("'").append(strlist.get(i)).append("'");
            }
        }
        return sb.toString();
    }

}
