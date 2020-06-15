package com.ruoyi.acad.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.client.ClientSearchCriteria;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.*;
import com.ruoyi.acad.form.AosForm;
import com.ruoyi.acad.form.BaseInfoPage;
import com.ruoyi.acad.form.BaseInfoShowForm;
import com.ruoyi.acad.form.PhotoForm;
import com.ruoyi.acad.service.IClientAcadService;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.redis.util.JWTUtil;
import com.ruoyi.system.feign.RemoteMBUserService;
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
import java.util.stream.Collectors;

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

    //获取当前用户的科学院信息权限
    @Autowired
    private RemoteMBUserService remoteMBUserService;

    /**
     * 根据条件查询列表数据
     */
    @Override
    public BaseInfoPage getBaseInfoList(QueryRequest queryRequest, ClientSearchCriteria clientSearchCriteria) throws Exception {

        //获取当前用户具有哪些科学院权限
        RE re = remoteMBUserService.getAosPerms(JWTUtil.getToken());
        List<Map<String,Object>> remoteAosList = (List<Map<String,Object>>)re.getObject();
        List<String> aosNoList = new ArrayList<String>();
        if(CollUtil.isNotEmpty(remoteAosList)){
            JSONArray jsonArray = JSONUtil.parseArray(remoteAosList);
            List<AosForm> jsonDtosList = JSONUtil.toList(jsonArray,AosForm.class);
            if(CollUtil.isNotEmpty(jsonDtosList)){
                aosNoList = jsonDtosList.stream().map(AosForm::getAosNo).collect(Collectors.toList());
            }
        }

        //查询条件拼接
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (clientSearchCriteria != null && CollUtil.isNotEmpty(aosNoList)) {

            //科学院集合
            boolQueryBuilder.must(QueryBuilders.termsQuery("aosList.aosId",aosNoList));

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
        }

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(this.getPageable(queryRequest))
                .build();

        Page<ClientAcad> page = elasticClientAcadRepository.search(searchQuery);
        //封装返回的数据
        BaseInfoPage baseInfoPage = this.getBaseInfoPage(page);

        return baseInfoPage;
    }

    @Override
    public BaseInfoPage wholeWordSearch(QueryRequest queryRequest, String wholeWord) throws Exception {

        //查询条件拼接
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.multiMatchQuery(
                wholeWord, "baseInfo.cnName", "baseInfo.enName", "baseInfo.realName"
                ,"aosList.aosName","snsList.snsValue","snsList.snsValue","educationList.school","baseInfo.nationPlace"
                ,"workList.workUnit","awardList.awardName","awardList.awardCategory","paperList.paperTitle"
                ,"paperList.paperTitle","paperList.paperAbstract","paperList.paper_publication"
                ,"paperList.patentName"));
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(this.getPageable(queryRequest))
                .build();
        Page<ClientAcad> page = elasticClientAcadRepository.search(searchQuery);

        //封装返回的数据
        BaseInfoPage baseInfoPage = this.getBaseInfoPage(page);

        return baseInfoPage;
    }

    private Pageable getPageable(QueryRequest queryRequest) {

        // 构建查询、排序条件
        Sort sort = null;
        Sort.Direction direction = null;

        switch (queryRequest.getSortOrder()) {
            case "DESC":
                direction = Sort.Direction.DESC;
                break;
            case "ASC":
                direction = Sort.Direction.ASC;
                break;
            default:
                direction = Sort.Direction.DESC;
                break;
        }

        switch (queryRequest.getSortField()){
            case "acadName":
                sort = Sort.by(direction,"baseInfo.realName.keyword");
                break;
            case "birthday":
                sort = Sort.by(direction,"baseInfo.birthday.keyword");
                break;
            case "nationPlace":
                sort = Sort.by(direction,"baseInfo.nationPlace.keyword");
                break;
            case "email":
                sort = Sort.by(direction,"emailList.email.keyword");
                break;
            case "rsfCategory":
                sort = Sort.by(direction,"baseInfo.rsfCategory.keyword");
                break;
            case "contactMethon":
                sort = Sort.by(direction,"baseInfo.contactMethon.keyword");
                break;
            case "signType":
                sort = Sort.by(direction,"baseInfo.signType.keyword");
                break;
            case "isBlack":
                sort = Sort.by(direction,"baseInfo.isBlack.keyword");
                break;
            case "isShow":
                sort = Sort.by(direction,"baseInfo.isShow.keyword");
                break;
            default:
                sort = Sort.by(direction,"baseInfo.acadId.keyword");
                break;
        }


        Pageable pageable = PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()
                ,sort);
        return pageable;
    }

    private BaseInfoPage getBaseInfoPage(Page<ClientAcad> page) {

        //在列表中展示的实体类
        List<BaseInfoShowForm> formList = new ArrayList<BaseInfoShowForm>();
        if(page != null && page.getTotalPages() > 0){
            //有数据
            List<ClientAcad> list = page.getContent();
            if(CollUtil.isNotEmpty(list)){
                for(ClientAcad acad : list){
                    if (acad.getAcadId() != null && !acad.getAcadId().equals("") && !acad.getAcadId().equals("null")) {
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
                                if(aos.getAosMemberType() != null){
                                    if(aos.getAosMemberType() == 1){//正籍
                                        form.setAosName(aos.getAosName());
                                        break;
                                    }
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
        }

        BaseInfoPage baseInfoPage = new BaseInfoPage();
        baseInfoPage.setContent(formList);
        baseInfoPage.setSize(page.getSize());
        baseInfoPage.setNumber(page.getNumber());
        baseInfoPage.setTotalElements(page.getTotalElements());
        baseInfoPage.setTotalPages(page.getTotalPages());
        return baseInfoPage;
    }


    //根据院士编码获取院士的所有信息
    @Override
    public ClientAcad getClientAcadByacadId(Integer acadId) {
        Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
        return optionalClientAcad.get();
    }


}
