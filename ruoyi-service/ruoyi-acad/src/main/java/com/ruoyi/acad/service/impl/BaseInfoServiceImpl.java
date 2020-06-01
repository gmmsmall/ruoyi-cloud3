package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.client.ClientSearchCriteria;
import com.ruoyi.acad.dao.BaseInfoMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.BaseInfo;
import com.ruoyi.acad.domain.QueryRequest;
import com.ruoyi.acad.form.BaseInfoForm;
import com.ruoyi.acad.service.IBaseInfoService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Description：创建院士基本信息逻辑层实现<br/>
 * CreateTime ：2020年4月1日上午9:26:48<br/>
 * CreateUser：ys<br/>
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BaseInfoServiceImpl extends ServiceImpl<BaseInfoMapper, BaseInfo> implements IBaseInfoService {

    @Autowired
    private BaseInfoMapper baseInfoMapper;

    @Autowired
    private ElasticClientAcadRepository elasticClientAcadRepository;

    /**
     * 根据条件查询列表数据
     * TODO  调用es搜索，查询数据
     * 拼接条件现为手动拼接，应写为映射类进行拼接，现满足一期需求不做反射拼接
     */
    @Override
    public Iterable<ClientAcad> getBaseInfoList(QueryRequest queryRequest, ClientSearchCriteria clientSearchCriteria) throws Exception {

        //返参集合
        List<String> result = Lists.newArrayList();

        //查询条件拼接
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (clientSearchCriteria != null) {

            //如果院士名称不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getAcadName())) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(clientSearchCriteria.getAcadName(), "name.cnName", "enName", "realName"));
            }

            //如果查询开始时间不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getStartBirthday())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("brithday").gt(clientSearchCriteria.getStartBirthday()));

            }

            //如果出生日期结束时间不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEndBrithday())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("brithday").lte(clientSearchCriteria.getEndBrithday()));
            }

            if (clientSearchCriteria.getIsShow() != null) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("isShow", clientSearchCriteria.getIsShow()));
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
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("rsfCategory", clientSearchCriteria.getRsfCategory()));
            }

            //签约情况
            if (clientSearchCriteria.getContactStatus() != null) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("contactStatus", clientSearchCriteria.getContactStatus()));
            }

            //联系方式类别
            if (clientSearchCriteria.getContactMethon() != null) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("contactMethon", clientSearchCriteria.getContactMethon()));
            }

            //如果邮箱不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEmail())) {
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("clientEmail.emailList.email", "*" + clientSearchCriteria.getEmail() + "*"));
            }

            //如果电话不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getPhone())) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(clientSearchCriteria.getPhone(), "clientEmail.phoneList.phone"));
            }

            //如果查询本科毕业开始 时间不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getStartUndergraduate())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("undergraduate").gte(clientSearchCriteria.getStartUndergraduate()));
            }

            //如果查询本科毕业结束 时间不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEndUndergraduate())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("undergraduate").gte(clientSearchCriteria.getEndUndergraduate()));
            }

            //如果查询研究生毕业 开始时间不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getStartGraduate())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("graduateTime").gte(clientSearchCriteria.getStartGraduate()));
            }

            //如果查询研究生毕业时间 结束时间不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEndGraduate())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("graduateTime").gte(clientSearchCriteria.getEndGraduate()));
            }

            //如果查询博士生毕业 开始时间不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getStartPHDGraduation())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("PHDGraduation").gte(clientSearchCriteria.getStartPHDGraduation()));
            }

            //如果查询博士生毕业时间 结束时间不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEndPHDGraduation())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("PHDGraduation").gte(clientSearchCriteria.getEndPHDGraduation()));
            }

            //如果院士当选年  查询开始年不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getStartElected())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("startElected").gte(clientSearchCriteria.getStartElected()));
            }

            //如果院士当选年  查询结束年不为空
            if (StringUtils.isNotBlank(clientSearchCriteria.getEndElected())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("startElected").gte(clientSearchCriteria.getEndElected()));
            }
        }

        Iterable<ClientAcad> clientAcadIterable = elasticClientAcadRepository.search(boolQueryBuilder);
        return clientAcadIterable;
    }


    /**
     * Description:保存基本信息
     * CreateTime:2020年3月23日下午3:52:39
     *
     * @param baseInfo
     * @throws Exception
     */
    @Override
    public void saveModel(BaseInfo baseInfo) throws Exception {

        Integer acadId = baseInfoMapper.selectMaxId();
        if (acadId == null) {
            acadId = 10000001;
        } else {
            acadId++;
        }
        baseInfo.setAcadId(acadId);
        this.save(baseInfo);

        ClientAcad acad = new ClientAcad();
        acad.setBaseInfo(baseInfo);
        try {
            elasticClientAcadRepository.save(acad);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 拉黑操作
     */
    @Override
    public String pullBlack(BaseInfoForm baseInfoForm) throws Exception {

        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setIsBlack(baseInfoForm.getIsShow());
        baseInfo.setAcadId(baseInfoForm.getAcadId());
        baseInfoMapper.updateById(baseInfo);
        return "SUCCESS";
    }

    /**
     * 是否展厅展示
     */
    @Override
    public String showBaseInfo(@NotBlank(message = "{required}") Integer acadId, Boolean isShow) throws Exception {

        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setIsShow(isShow);
        baseInfo.setAcadId(acadId);
        baseInfoMapper.updateById(baseInfo);
        return "SUCCESS";
    }

    /**
     * Description:修改基本信息
     * CreateTime:2020年3月25日下午1:38:43
     *
     * @param baseInfo
     * @throws Exception
     */
    @Override
    public void updateBaseInfo(BaseInfo baseInfo) throws Exception {

        //mysql修改
        this.updateById(baseInfo);

        baseInfo = this.getModelById(baseInfo.getAcadId());
        ClientAcad acad = new ClientAcad();
        acad.setBaseInfo(baseInfo);
        elasticClientAcadRepository.save(acad);
    }

    /**
     * Description:查询基本信息
     * CreateTime:2020年3月25日下午1:38:43
     *
     * @throws Exception
     */
    @Override
    public BaseInfo getModelById(Integer acadId) throws Exception {

        BaseInfo baseInfo = baseInfoMapper.selectOne(new LambdaQueryWrapper<BaseInfo>().in(BaseInfo::getAcadId, acadId));

        return baseInfo;
    }

}
