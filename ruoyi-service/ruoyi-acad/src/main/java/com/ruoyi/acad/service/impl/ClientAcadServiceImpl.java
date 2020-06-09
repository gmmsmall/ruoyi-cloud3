package com.ruoyi.acad.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.client.ClientSearchCriteria;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.*;
import com.ruoyi.acad.form.BaseInfoShowForm;
import com.ruoyi.acad.form.PhotoForm;
import com.ruoyi.acad.service.IClientAcadService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

/**
 * Description：查询院士信息逻辑层实现<br/>
 * CreateTime ：2020年4月1日上午9:26:48<br/>
 * CreateUser：ys<br/>
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ClientAcadServiceImpl implements IClientAcadService {

    @Autowired
    private ElasticClientAcadRepository elasticClientAcadRepository;

    /**
     * 根据条件查询列表数据
     */
    @Override
    public Map<String,Object> getBaseInfoList(QueryRequest queryRequest, ClientSearchCriteria clientSearchCriteria) throws Exception {

        Map<String,Object> map = new HashMap<String,Object>();

        //查询条件拼接
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (clientSearchCriteria != null) {

            //如果院士名称不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getAcadName())) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(
                        clientSearchCriteria.getAcadName(), "baseInfo.cnName", "baseInfo.enName", "baseInfo.realName"));
            }

            //出生日期如果查询开始时间不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getStartBirthday())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("baseInfo.birthday").from(clientSearchCriteria.getStartBirthday()));

            }

          //如果出生日期结束时间不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEndBrithday())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("baseInfo.birthday").to(clientSearchCriteria.getEndBrithday()));
            }

            //是否展示
            if (clientSearchCriteria.getIsShow() != null) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("baseInfo.isShow", clientSearchCriteria.getIsShow()));
            }

            //如果国籍不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getNationPlace())) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(clientSearchCriteria.getNationPlace(), "baseInfo.nationPlace"));
            }
            //如果授衔机构不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getAosName())) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(clientSearchCriteria.getAosName(), "baseInfo.aosName"));
            }

            //如果生活习惯不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getLivingHabit())) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(clientSearchCriteria.getLivingHabit(), "baseInfo.livingHabit"));
            }

            //如果工作单位不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getWorkName())) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(clientSearchCriteria.getWorkName(), "workList.workUnit"));
            }


            /*//如果宗教信仰不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getReligion())) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(clientSearchCriteria.getReligion(), "baseInfo.religion"));
            }
*/
            //专业领域类别
            if (clientSearchCriteria.getRsfCategory() != null) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery
                        ("baseInfo.rsfCategory", clientSearchCriteria.getRsfCategory())).minimumShouldMatch("70%");
            }

            //签约情况
            if (clientSearchCriteria.getContactStatus() != null) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery
                        ("baseInfo.contactStatus", clientSearchCriteria.getContactStatus())).minimumShouldMatch("70%");
            }

            //联系方式类别
            if (clientSearchCriteria.getContactMethon() != null) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery
                        ("baseInfo.contactMethon", clientSearchCriteria.getContactMethon())).minimumShouldMatch("70%");
            }

            //如果邮箱不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEmail())) {
                boolQueryBuilder.must(QueryBuilders.wildcardQuery
                        ("emailList.email", "*" + clientSearchCriteria.getEmail() + "*")).minimumShouldMatch("70%");
            }

            //如果电话不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getPhone())) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery
                        (clientSearchCriteria.getPhone(), "phoneList.phoneNumber")).minimumShouldMatch("90%");
            }
            //如果院士当选年  查询开始年不为空
           /* if (StringUtils.isNotBlank(clientSearchCriteria.getStartElected())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("startElected").gte(clientSearchCriteria.getStartElected()));
            }

            //如果院士当选年  查询结束年不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEndElected())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("startElected").lte(clientSearchCriteria.getEndElected()));
            }
            //如果院士工作年  查询开始年不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getStartWorkTime())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("workList.jobStartYear").gte(clientSearchCriteria.getStartWorkTime()));
            }
            //如果院士工作年  查询结束年不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEndWorkTime())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("workList.jobStartYear").lte(clientSearchCriteria.getEndWorkTime()));
            }

            //如果院士毕业年-学士  查询开始年不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getStartUndergraduate())) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("educationList.education", 1));
                boolQueryBuilder.must(QueryBuilders.rangeQuery("educationList.graduationYear").gte(clientSearchCriteria.getStartUndergraduate()));
            }
            //如果院士毕业年-学士  查询结束年不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEndUndergraduate())) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("educationList.education", 1));
                boolQueryBuilder.must(QueryBuilders.rangeQuery("educationList.graduationYear").lte(clientSearchCriteria.getEndUndergraduate()));
            }

            //如果院士毕业年-硕士  查询开始年不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getStartGraduate())) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("educationList.education", 2));
                boolQueryBuilder.must(QueryBuilders.rangeQuery("educationList.graduationYear").gte(clientSearchCriteria.getStartGraduate()));
            }
            //如果院士毕业年-硕士  查询结束年不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEndGraduate())) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("educationList.education", 2));
                boolQueryBuilder.must(QueryBuilders.rangeQuery("educationList.graduationYear").lte(clientSearchCriteria.getEndGraduate()));
            }

            //如果院士毕业年-博士  查询开始年不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getStartPHDGraduation())) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("educationList.education", 3));
                boolQueryBuilder.must(QueryBuilders.rangeQuery("educationList.graduationYear").gte(clientSearchCriteria.getStartPHDGraduation()));
            }
            //如果院士毕业年-博士  查询结束年不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEndPHDGraduation())) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("educationList.education", 3));
                boolQueryBuilder.must(QueryBuilders.rangeQuery("educationList.graduationYear").lte(clientSearchCriteria.getEndPHDGraduation()));
            }*/

        }

        /*Iterable<ClientAcad> clientAcadIterable = elasticClientAcadRepository.search(boolQueryBuilder);*/

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()))
                .build();

        Page<ClientAcad> page = elasticClientAcadRepository.search(searchQuery);
        //在列表中展示的实体类
        List<BaseInfoShowForm> formList = new ArrayList<BaseInfoShowForm>();
        if(page != null && page.getTotalPages() > 0){
            //有数据
            List<ClientAcad> list = page.getContent();
            if(CollUtil.isNotEmpty(list)){
                for(ClientAcad acad : list){
                    BaseInfoShowForm form = new BaseInfoShowForm();
                    form.setAcadId(Integer.valueOf(acad.getAcadId()));//院士编码
                    //院士姓名显示顺序：真实姓名、中文名字、英文名字
                    if(StringUtils.isNotBlank(acad.getBaseInfo().getRealName())){
                        form.setAcadName(acad.getBaseInfo().getRealName());
                    }else if(StringUtils.isNotBlank(acad.getBaseInfo().getCnName())){
                        form.setAcadName(acad.getBaseInfo().getCnName());
                    }else if(StringUtils.isNotBlank(acad.getBaseInfo().getEnName())){
                        form.setAcadName(acad.getBaseInfo().getEnName());
                    }
                    //头像显示展厅的头像
                    List<PhotoForm> photoList = acad.getPhotoList();
                    if(CollUtil.isNotEmpty(photoList)){
                        for(PhotoForm p : photoList){
                            if(p.getIsHall()){//展厅图片
                                form.setPhoto(p.getPhotoUrl());
                                break;
                            }
                        }
                    }
                    //出生日期
                    if(acad.getBaseInfo().getBirthday() != null && !String.valueOf(acad.getBaseInfo().getBirthday()).equals("")){
                        form.setBirthday(String.valueOf(acad.getBaseInfo().getBirthday()));
                    }else{
                        form.setBirthday(acad.getBaseInfo().getBirthdayRemark());
                    }
                    //国籍
                    form.setNationPlace(acad.getBaseInfo().getNationPlace());
                    //籍贯
                    form.setNativePlace(acad.getBaseInfo().getNativePlace());
                    //授衔机构，显示正籍科学院
                    List<Aos> aosList = acad.getAosList();
                    if(CollUtil.isNotEmpty(aosList)){
                        for(Aos aos : aosList){
                            if(aos.getAosMemberType() == 1){//正籍
                                form.setAosName(aos.getAosName());
                                break;
                            }
                        }
                    }
                    //邮箱：显示有效的主邮箱
                    List<Email> emailList = acad.getEmailList();
                    if(CollUtil.isNotEmpty(emailList)){
                        for(Email email : emailList){
                            if(email.getIsEffectiveEmail() && email.getIsMainEmail()){//有效的主邮箱
                                form.setEmail(email.getEmail());
                                break;
                            }
                        }
                    }
                    //专业领域
                    form.setRsfCategory(acad.getBaseInfo().getRsfCategory());
                    //联络情况
                    form.setContactMethon(acad.getBaseInfo().getContactMethon());
                    //签约情况
                    form.setSignType(acad.getBaseInfo().getSignType());
                    //是否展厅展示
                    form.setIsShow(acad.getBaseInfo().getIsShow());
                    //是否拉黑
                    form.setIsBlack(acad.getBaseInfo().getIsBlack());
                    formList.add(form);
                }
            }
        }

        map.put("content",formList);
        map.put("size",page.getSize());
        map.put("totalElements",page.getTotalElements());//总条数
        map.put("totalPages",page.getTotalPages());//一共多少页
        map.put("number",page.getNumber());
        return map;
    }

}
