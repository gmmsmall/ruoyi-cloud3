package com.ruoyi.javamail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.javamail.dao.SendMailItemsMapper;
import com.ruoyi.javamail.domain.FebsConstant;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.entity.SendMailItems;
import com.ruoyi.javamail.service.ISendMailItemsService;
import com.ruoyi.javamail.util.SortUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jxd
 */
@Service
@Slf4j
public class SendMailItemsServiceImpl extends ServiceImpl<SendMailItemsMapper, SendMailItems> implements ISendMailItemsService {

    @Autowired
    private SendMailItemsMapper itemsMapper;

    @Override
    public IPage<SendMailItems> findSendMailItems(QueryRequest request, SendMailItems sendMailItems) {
        try{
            LambdaQueryWrapper<SendMailItems> queryWrapper = new LambdaQueryWrapper<>();

            if (sendMailItems.getFid() != null && !sendMailItems.getFid().equals("")) {
                queryWrapper.eq(SendMailItems::getFid, sendMailItems.getFid());
            }
            /*if (sendMailItems.getAddpersonid() != null && !sendMailItems.getAddpersonid().equals("")) {
                queryWrapper.eq(SendMailItems::getAddpersonid, sendMailItems.getAddpersonid());
            }*/
            if (StringUtils.isNotBlank(sendMailItems.getName())) {
                queryWrapper.like(SendMailItems::getName, sendMailItems.getName());
            }
            if (StringUtils.isNotBlank(sendMailItems.getNationality())) {
                queryWrapper.like(SendMailItems::getNationality, sendMailItems.getNationality());
            }
            if (StringUtils.isNotBlank(sendMailItems.getShowemail())) {
                queryWrapper.like(SendMailItems::getShowemail, sendMailItems.getShowemail());
            }
            if (StringUtils.isNotBlank(sendMailItems.getSendflag())) {
                queryWrapper.eq(SendMailItems::getSendflag, sendMailItems.getSendflag());
            }
            //需要加数据过滤
            //1、调用用户中心接口，查询当前用户可以查看的科学院
            //2、调用院士信息接口，根据科学院查询出该院下所有院士的邮箱（主、副邮箱都有），最终是一个List<String>列表
            List<String> emailList = new ArrayList<>();//先假设这是最终查询出的院士邮箱列表
            emailList.add("1");
            emailList.add("1@qq.com");
            if(emailList != null && emailList.size() > 0) {
                String str = convertListToString(emailList);
                queryWrapper.inSql(SendMailItems::getShowemail,str);
                Page<SendMailItems> page = new Page<>(request.getPageNum(), request.getPageSize());
                SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, true);
                return this.page(page, queryWrapper);
            }else{
                return null;
            }
        } catch (Exception e) {
            log.error("根据fid获取邮件详情列表失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public List<SendMailItems> getListByFid(Long fid,String sendflag) {
        LambdaQueryWrapper<SendMailItems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SendMailItems::getFid,fid);
        if(sendflag != null && !sendflag.equals("")){
            queryWrapper.eq(SendMailItems::getSendflag,sendflag);
        }
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<SendMailItems> getSendMailItemsPage(QueryRequest request, SendMailItems sendMailItems) {
        try{
            Map<String,Object> map = new HashMap<>();
            map.put("fid",sendMailItems.getFid());
            map.put("name",sendMailItems.getName());
            map.put("nationality",sendMailItems.getNationality());
            map.put("showemail",sendMailItems.getShowemail());
            map.put("sendflag",sendMailItems.getSendflag());
            //需要加数据过滤
            //1、调用用户中心接口，查询当前用户可以查看的科学院
            //2、调用院士信息接口，根据科学院查询出该院下所有院士编码       不是查邮箱邮箱（主、副邮箱都有），最终是一个List<String>列表
            List<String> emailList = new ArrayList<>();//先假设这是最终查询出的院士邮箱列表
            emailList.add("1");
            emailList.add("1@qq.com");
            if(emailList != null && emailList.size() > 0) {
                String emailstr = convertListToString(emailList);
                //获取所有可用的院士id的字符串拼接
                /*List<Integer> acadeidlist = itemsMapper.getUseableAcadIdOfSendMailItems(emailstr);
                String acadeidStr = convertIntegerListToString(acadeidlist);*/
                String acadeidStr = "'1','2','16','19','26','33'";
                IPage<SendMailItems> page = new Page<>();
                if(acadeidStr != null && !acadeidStr.equals("")){//当前用户可以查看的院士权限
                    map.put("str",acadeidStr);
                    List<SendMailItems> listtemp = itemsMapper.getSendMailItemsList(map);
                    if(listtemp != null && listtemp.size() > 0){
                        page.setTotal(listtemp.size());
                    }else{
                        page.setTotal(0);
                    }
                    map.put("limit"," "+(request.getPageNum()-1)*request.getPageSize()+","+request.getPageSize());
                    map.put("order"," i.addtime desc ");
                    page.setRecords(itemsMapper.getSendMailItemsList(map));
                }else{
                    page.setTotal(0);
                    page.setRecords(null);
                }
                return page;
            }else{
                return null;
            }
        }catch (Exception e){
            log.error("根据fid获取邮件详情列表失败", e);
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

    /**
     * 将List<Integer>集合 转化为String
     * 如{"aaa","bbb"} To 'aaa','bbb'
     */
    public static String convertIntegerListToString(List<Integer> strlist){
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
