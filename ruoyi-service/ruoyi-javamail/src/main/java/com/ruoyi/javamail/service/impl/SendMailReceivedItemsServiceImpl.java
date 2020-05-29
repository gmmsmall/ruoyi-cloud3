package com.ruoyi.javamail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.javamail.dao.SendMailReceivedItemsMapper;
import com.ruoyi.javamail.domain.FebsConstant;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.SendMailReceivedItems;
import com.ruoyi.javamail.service.ISendMailReceivedItemsService;
import com.ruoyi.javamail.util.SortUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gmm
 */
@Service
@Slf4j
public class SendMailReceivedItemsServiceImpl extends ServiceImpl<SendMailReceivedItemsMapper, SendMailReceivedItems> implements ISendMailReceivedItemsService {

    @Autowired
    private SendMailReceivedItemsMapper receivedItemsMapper;

    @Override
    public IPage<SendMailReceivedItems> findSendMailReceivedItems(QueryRequest request, SendMailReceivedItems sendMailReceivedItems) {
        try {
            LambdaQueryWrapper<SendMailReceivedItems> queryWrapper = new LambdaQueryWrapper<>();

            if (sendMailReceivedItems.getFid() != null && !sendMailReceivedItems.getFid().equals("")) {
                queryWrapper.eq(SendMailReceivedItems::getFid, sendMailReceivedItems.getFid());
            }
            if (StringUtils.isNotBlank(sendMailReceivedItems.getName())) {
                queryWrapper.like(SendMailReceivedItems::getName, sendMailReceivedItems.getName());
            }
            if (StringUtils.isNotBlank(sendMailReceivedItems.getNationality())) {
                queryWrapper.like(SendMailReceivedItems::getNationality, sendMailReceivedItems.getNationality());
            }
            if (StringUtils.isNotBlank(sendMailReceivedItems.getEmail())) {
                queryWrapper.like(SendMailReceivedItems::getEmail, sendMailReceivedItems.getEmail());
            }
            if (StringUtils.isNotBlank(sendMailReceivedItems.getVisit())) {
                queryWrapper.eq(SendMailReceivedItems::getVisit, sendMailReceivedItems.getVisit());
            }
            if (StringUtils.isNotBlank(sendMailReceivedItems.getSign())) {
                queryWrapper.eq(SendMailReceivedItems::getSign, sendMailReceivedItems.getSign());
            }

            Page<SendMailReceivedItems> page = new Page<>(request.getPageNum(), request.getPageSize());
            SortUtil.handlePageSort(request, page, "addtime", FebsConstant.ORDER_DESC, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取主题邮件接收子表列表失败", e);
            return null;
        }
    }

    @Override
    public IPage<SendMailReceivedItems> getReceivedItemsList(QueryRequest request, SendMailReceivedItems sendMailReceivedItems) {
        Map<String,Object> map = new HashMap<>();
        map.put("fid",sendMailReceivedItems.getFid());
        map.put("name",sendMailReceivedItems.getName());
        map.put("nationality",sendMailReceivedItems.getNationality());
        map.put("email",sendMailReceivedItems.getEmail());
        map.put("sign",sendMailReceivedItems.getSign());
        map.put("visit",sendMailReceivedItems.getVisit());
        //需要加数据过滤
        //1、调用用户中心接口，查询当前用户可以查看的科学院
        //2、调用院士信息接口，根据科学院查询出该院下所有该院下所有院士编码       不是查邮箱邮箱（主、副邮箱都有），最终是一个List<String>列表
        List<String> emailList = new ArrayList<>();//先假设这是最终查询出的院士邮箱列表
        emailList.add("1");
        emailList.add("1@qq.com");
        emailList.add("dfdjfdj@qq.com");
        if(emailList != null && emailList.size() > 0) {
            String str = convertListToString(emailList);
            /*map.put("addpersonid",sendMailReceivedItems.getAddpersonid());*/
            String acadeidStr = "'1','2','16','19','26','33'";//院士编码以逗号分隔组成的字符串
            IPage<SendMailReceivedItems> page = new Page<>();
            if(acadeidStr != null && !acadeidStr.equals("")) {//当前用户可以查看的院士权限
                map.put("str",acadeidStr);
                List<SendMailReceivedItems> listtemp = receivedItemsMapper.getReceivedItemsList(map);
                if(listtemp != null && listtemp.size() > 0){
                    page.setTotal(listtemp.size());
                }else{
                    page.setTotal(0);
                }
                map.put("limit"," "+(request.getPageNum()-1)*request.getPageSize()+","+request.getPageSize());
                map.put("order"," i.addtime desc ");
                page.setRecords(receivedItemsMapper.getReceivedItemsList(map));
            }else{
                page.setTotal(0);
                page.setRecords(null);
            }
            return page;
        }else{
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getMailExchangebyAcademial(String email) {
        return receivedItemsMapper.getMailExchangebyAcademial(email);
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
