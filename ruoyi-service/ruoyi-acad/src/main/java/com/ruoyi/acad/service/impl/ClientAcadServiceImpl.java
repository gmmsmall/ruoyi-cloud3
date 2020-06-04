package com.ruoyi.acad.service.impl;

import com.google.common.collect.Lists;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.client.ClientSearchCriteria;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.QueryRequest;
import com.ruoyi.acad.service.IClientAcadService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Page<ClientAcad> getBaseInfoList(QueryRequest queryRequest, ClientSearchCriteria clientSearchCriteria) throws Exception {

        //返参集合
        List<String> result = Lists.newArrayList();

        //查询条件拼接
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (clientSearchCriteria != null) {

            //如果院士名称不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getAcadName())) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(
                        clientSearchCriteria.getAcadName(), "baseInfo.cnName", "baseInfo.enName", "baseInfo.realName"));
            }

            //如果查询开始时间不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getStartBirthday())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("baseInfo.birthday").from(clientSearchCriteria.getStartBirthday()));

            }

          //如果出生日期结束时间不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEndBrithday())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("baseInfo.birthday").to(clientSearchCriteria.getEndBrithday()));
            }

            if (clientSearchCriteria.getIsShow() != null) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("baseInfo.isShow", clientSearchCriteria.getIsShow()));
            }

            //如果生活习惯不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getLivingHabit())) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(clientSearchCriteria.getLivingHabit(), "baseInfo.livingHabit"));
            }

            //如果宗教信仰不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getReligion())) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(clientSearchCriteria.getReligion(), "baseInfo.religion"));
            }

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
            if (StringUtils.isNotBlank(clientSearchCriteria.getStartElected())) {
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
            }

        }

        /*Iterable<ClientAcad> clientAcadIterable = elasticClientAcadRepository.search(boolQueryBuilder);*/

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()))
                .build();

        Page<ClientAcad> page = elasticClientAcadRepository.search(searchQuery);
        return page;
    }

}
