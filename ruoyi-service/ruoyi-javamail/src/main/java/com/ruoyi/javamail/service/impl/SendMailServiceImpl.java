package com.ruoyi.javamail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.javamail.dao.SendMailMapper;
import com.ruoyi.javamail.domain.FebsConstant;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.SendMail;
import com.ruoyi.javamail.service.ISendMailService;
import com.ruoyi.javamail.util.SortUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jxd
 */
@Service
@Slf4j
public class SendMailServiceImpl extends ServiceImpl<SendMailMapper, SendMail> implements ISendMailService {

    @Autowired
    public SendMailMapper sendMailMapper;

    @Override
    @Transactional
    public void saveSendMail(SendMail sendMail) {
        this.baseMapper.insert(sendMail);
    }

    @Override
    @Transactional
    public IPage<SendMail> findSendMail(QueryRequest request, SendMail sendMail) {
        try {
            LambdaQueryWrapper<SendMail> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.isNotBlank(sendMail.getMailtopic())) {
                queryWrapper.like(SendMail::getMailtopic, sendMail.getMailtopic());
            }
            if (StringUtils.isNotBlank(sendMail.getMailname())) {
                queryWrapper.like(SendMail::getMailname, sendMail.getMailname());
            }
           /* if (sendMail.getAddpersonid() != null && !sendMail.getAddpersonid().equals("")) {
                queryWrapper.eq(SendMail::getAddpersonid, sendMail.getAddpersonid());
            }*/
            if (sendMail.getSendnumber() != null && !sendMail.getSendnumber().equals("")) {
                queryWrapper.eq(SendMail::getSendnumber, sendMail.getSendnumber());
            }
            queryWrapper.eq(SendMail::getDeleteflag,"1");
            if (StringUtils.isNotBlank(sendMail.getCreateTimeFrom())) {
                queryWrapper.ge(SendMail::getAddtime, sendMail.getCreateTimeFrom());
            }
            if (StringUtils.isNotBlank(sendMail.getCreateTimeTo())) {
                queryWrapper.le(SendMail::getAddtime, sendMail.getCreateTimeTo());
            }
            //需要加数据过滤
            //1、调用用户中心接口，查询当前用户可以查看的科学院
            //2、调用院士信息接口，根据科学院查询出该院下所有院士的邮箱（主、副邮箱都有），最终是一个List<String>列表
            //3、在邮件发送子表中根据这些院士邮箱，查询出fid列表

            //可以不根据邮箱，而是根据院士编码来查询




            List<String> emailList = new ArrayList<>();//先假设这是最终查询出的院士邮箱列表
            emailList.add("1");
            emailList.add("1@qq.com");
            if(emailList != null && emailList.size() > 0){
                String str = convertListToString(emailList);
                queryWrapper.inSql(SendMail::getId,"select i.fid from send_mail_items as i where i.showemail in("+str+") and i.fid is not null and i.fid != ''");
                Page<SendMail> page = new Page<>(request.getPageNum(), request.getPageSize());
                SortUtil.handlePageSort(request, page, "addtime", FebsConstant.ORDER_DESC, true);
                return this.page(page, queryWrapper);
            }else{
                return null;
            }

        } catch (Exception e) {
            log.error("获取邮件列表失败", e);
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
