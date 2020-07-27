package com.ruoyi.acad.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.config.RabbitConfig;
import com.ruoyi.acad.dao.*;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.*;
import com.ruoyi.acad.enums.RsfCategoryType;
import com.ruoyi.acad.form.*;
import com.ruoyi.acad.service.IBaseInfoService;
import com.ruoyi.acad.service.IMstCountryService;
import com.ruoyi.acad.utils.BaiduTranslateUtil;
import com.ruoyi.acad.utils.UpdateLogUtil;
import com.ruoyi.common.redis.util.JWTUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.AcadOperLog;
import com.ruoyi.system.feign.RemoteAcadLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description：创建院士基本信息逻辑层实现<br/>
 * CreateTime ：2020年4月1日上午9:26:48<br/>
 * CreateUser：ys<br/>
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class BaseInfoServiceImpl extends ServiceImpl<BaseInfoMapper, BaseInfo> implements IBaseInfoService {

    @Autowired
    private BaseInfoMapper baseInfoMapper;

    @Autowired
    private AosMapper aosMapper;

    /**
     * mq,批量生成简历时使用
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ElasticClientAcadRepository elasticClientAcadRepository;

    /**
     * 院士信息修改日志
     */
    @Autowired
    private RemoteAcadLogService acadLogService;

    /**
     * 院士国籍mapper
     */
    @Autowired
    private NationalityMapper nationalityMapper;

    /**
     * 百度翻译
      */
    @Autowired
    private BaiduTranslateUtil baiduTranslateUtil;

    @Autowired
    private IMstCountryService mstCountryService;

    @Autowired
    private EducationMapper educationMapper;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private AwardMapper awardMapper;

    /**
     * Description:保存基本信息
     * CreateTime:2020年3月23日下午3:52:39
     *
     * @param baseInfo
     * @throws Exception
     */
    @Override
    public Integer saveModel(BaseInfo baseInfo) throws Exception {

        Integer acadId = baseInfoMapper.selectMaxId();
        if (acadId == null) {
            acadId = 10000001;
        } else {
            acadId++;
        }
        baseInfo.setAcadId(acadId);
        Date now = new Date();
        baseInfo.setCreateTime(now);//新增时间
        baseInfo.setUpdateTime(now);//创建时间
        baseInfo.setDelFlag(true);//未删除
        this.save(baseInfo);

        Aos aos = new Aos();
        aos.setAcadId(acadId);
        aos.setAosNo("77");
        aos.setAosName("暂无");
        aosMapper.insert(aos);

        //增加院士修改日志
        AcadOperLog acadOperLog = new AcadOperLog();
        acadOperLog.setAcadId(Long.valueOf(baseInfo.getAcadId()));
        acadOperLog.setTitle("新增院士信息");
        acadOperLog.setBusinessType(1);
        acadOperLog.setOperTime(LocalDateTime.now());
        acadOperLog.setOpUserId(JWTUtil.getUser().getUserId());
        this.acadLogService.insertOperlog(acadOperLog);

        ClientAcad acad = new ClientAcad();
        acad.setAcadId(String.valueOf(acadId));
        baseInfo.setAosName("暂无");

        //修改研究领域 文字
        baseInfo.setRsfCategory(RsfCategoryType.of(baseInfo.getRsfCategory()).getDesc());

        acad.setBaseInfo(baseInfo);
        List<Aos> aosList = new ArrayList<>();
        aosList.add(aos);
        acad.setAosList(aosList);
        try {
            elasticClientAcadRepository.save(acad);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acadId;
    }

    /**
     * 根据院士姓名模糊查询院士编码列表
     * @param name
     * @return
     * @throws Exception
     */
    @Override
    public List<Integer> getAcadListByName(String name) throws Exception {
        return this.baseInfoMapper.getAcadListByName(name);
    }

    /**
     * 批量修改院士基础信息
     * @param baseInfoBatch
     */
    @Override
    public void updateBatchBaseInfo(BaseInfoBatch baseInfoBatch) throws Exception{
        //批量修改院士基础信息
        if(baseInfoBatch != null && CollUtil.isNotEmpty(baseInfoBatch.getList())){
            List<Integer> acadidList = baseInfoBatch.getList().stream().map(BaseInfoAcadIdIntegerForm::getAcadId).collect(Collectors.toList());
            BaseInfo baseInfo = new BaseInfo();
            baseInfo.setContactStatus(baseInfoBatch.getContactStatus());
            baseInfo.setRsfCategory(baseInfoBatch.getRsfCategory());
            baseInfo.setNativePlace(baseInfoBatch.getNativePlace());
            baseInfo.setContactMethon(baseInfoBatch.getContactMethon());
            baseInfo.setSignType(baseInfoBatch.getSignType());
            baseInfo.setIsChinese(baseInfoBatch.getIsChinese());
            baseInfo.setIsTop(baseInfoBatch.getIsTop());
            baseInfo.setIsYoung(baseInfoBatch.getIsYoung());
            baseInfo.setIsBlack(baseInfoBatch.getIsBlack());
            baseInfo.setIsShow(baseInfoBatch.getIsShow());
            baseInfo.setShowValue(baseInfoBatch.getShowValue());
            baseInfo.setRemark(baseInfoBatch.getRemark());
            baseInfo.setAcadLabel(baseInfoBatch.getAcadLabel());
            Date now = new Date();
            baseInfo.setDelFlag(true);//未删除
            baseInfo.setUpdateTime(now);
            this.baseInfoMapper.update(baseInfo,new QueryWrapper<BaseInfo>()
                    .eq("del_flag","1")
                    .in("acad_id",acadidList));
            String str = UpdateLogUtil.UpdateBatchLog(baseInfoBatch);
            log.info("院士信息修改操作日志："+str);
            if(StringUtils.isNotEmpty(str)){
                str = str.substring(0,str.length()-1);
            }else{
                str = "批量修改院士信息";
            }
            //同步刷新es中的院士信息
            for(BaseInfoAcadIdIntegerForm acadIdForm : baseInfoBatch.getList()){

                //增加院士修改日志
                AcadOperLog acadOperLog = new AcadOperLog();
                acadOperLog.setAcadId(Long.valueOf(acadIdForm.getAcadId()));
                acadOperLog.setTitle(str);
                acadOperLog.setOperTime(LocalDateTime.now());
                acadOperLog.setBusinessType(2);
                acadOperLog.setOpUserId(JWTUtil.getUser().getUserId());
                this.acadLogService.insertOperlog(acadOperLog);
                baseInfo = this.getOne(new QueryWrapper<BaseInfo>().eq("acad_id",acadIdForm.getAcadId()));

                String nationStr = "";//多个国籍拼接成的字符串
                List<Nationality> nationalityList = null;//es中国籍列表
                //批量修改院士国籍，以及同步更新es中的国籍信息
                if(CollUtil.isNotEmpty(baseInfoBatch.getNationalityList())){
                    //国籍只能单个更新，因为每个实体类中的院士编码不一样，不能批量操作（咱们国籍采用的是先删后增操作）
                    LambdaQueryWrapper<Nationality> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Nationality::getAcadId,acadIdForm.getAcadId());
                    this.nationalityMapper.delete(queryWrapper);
                    baseInfoBatch.getNationalityList().stream().forEach(x ->{
                        x.setAcadId(acadIdForm.getAcadId());
                        this.nationalityMapper.insert(x);
                    });

                    for(int i = 0 ; i < baseInfoBatch.getNationalityList().size();i++){
                        baseInfoBatch.getNationalityList().get(i).setAcadId(acadIdForm.getAcadId());
                        if(i == 0){
                            nationStr = baseInfoBatch.getNationalityList().get(i).getCountryName();
                        }else{
                            nationStr = nationStr + "," + baseInfoBatch.getNationalityList().get(i).getCountryName();
                        }
                    }
                    nationalityList = baseInfoBatch.getNationalityList();
                }else{//默认原来的国籍信息
                    nationalityList = this.nationalityMapper.selectList(new LambdaQueryWrapper<Nationality>()
                            .eq(Nationality::getAcadId,acadIdForm.getAcadId()));
                    for (Nationality nation: nationalityList) {
                        MstCountry mstCountry = mstCountryService.getOne(
                                new LambdaQueryWrapper<MstCountry>().eq(MstCountry::getCountryId,nation.getCountryId()));
                        if (mstCountry != null) {
                            nationStr += mstCountry.getCountryCnname() + ",";
                        }
                    }
                    if (nationStr.length() > 0) {
                        nationStr = nationStr.substring(0,nationStr.lastIndexOf(","));
                    }

                }
                //同步刷新es中的院士信息和国籍信息
                Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadIdForm.getAcadId()));
                ClientAcad acad = optionalClientAcad.get();
                acad.setAcadId(String.valueOf(acadIdForm.getAcadId()));
                baseInfo.setNationPlace(nationStr);

                //修改研究领域 文字
                baseInfo.setRsfCategory(RsfCategoryType.of(baseInfo.getRsfCategory()).getDesc());

                acad.setBaseInfo(baseInfo);
                acad.setNationalityList(nationalityList);
                elasticClientAcadRepository.save(acad);
                String msgId = UUID.randomUUID().toString();
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("acadId",acadIdForm.getAcadId());
                CorrelationData correlationData = new CorrelationData(msgId);
                rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME, JSON.toJSONString(map), correlationData);
            }
        }
    }

    @Override
    public Integer initProfile(Integer acadId) {
        Integer acadIdTemp = acadId;
        try{
            List<BaseInfo> list = this.baseInfoMapper.selectList(new QueryWrapper<BaseInfo>().gt("acad_id",acadId).orderByAsc("acad_id"));
            if(CollUtil.isNotEmpty(list)){
                for(BaseInfo info : list){
                    if(StringUtils.isNotEmpty(info.getPersonalProfileOrig())){//简介原文不为空
                        String str = this.baiduTranslateUtil.getTranslateString(info.getPersonalProfileOrig());
                        if(StringUtils.isNotEmpty(str)){
                            info.setPersonalProfileMechine(str);//机器翻译
                            this.baseInfoMapper.update(info,new QueryWrapper<BaseInfo>().eq("acad_id",info.getAcadId()));
                            if(info.getAcadId() == 10040199){
                                return null;
                            }else{
                                acadIdTemp = info.getAcadId();
                            }
                            //es需要同步更新
                           /* info = this.getOne(new QueryWrapper<BaseInfo>().eq("acad_id",info.getAcadId()));
                            ClientAcad acad = new ClientAcad();
                            acad.setAcadId(String.valueOf(info.getAcadId()));
                            acad.setBaseInfo(info);
                            elasticClientAcadRepository.save(acad);*/
                            Thread.sleep(3000); //6000 毫秒，也就是6秒.
                        }
                    }
                }
            }
        }catch (Exception e){

        }
        return acadIdTemp;
    }

    @Override
    public AcadAgeInfo getAcadAgeInfo(Integer acadId) throws Exception {
        AcadAgeInfo acadAgeInfo = new AcadAgeInfo();
        List<AcadMileage> education = new ArrayList<>();//院士教育信息
        List<AcadMileage> aos = new ArrayList<>();//院士授衔信息
        List<AcadMileage> award = new ArrayList<>();//院士荣誉信息
        BaseInfo baseInfo = this.baseInfoMapper.selectOne(new LambdaQueryWrapper<BaseInfo>().in(BaseInfo::getAcadId, acadId));
        if(baseInfo != null){
            //---------------------------------------院士教育信息 start-----------------------------------
            AcadMileage mileageBirthDay = new AcadMileage();
            mileageBirthDay.setContent("0");//出生日期
            if(baseInfo.getBirthday() != null && StringUtils.isNotEmpty(baseInfo.getBirthday())){
                mileageBirthDay.setYear(baseInfo.getBirthday());
            }else{
                mileageBirthDay.setYear(baseInfo.getBirthdayRemark());
            }
            education.add(mileageBirthDay);
            //院士教育信息
            List<Education> educationList =  this.educationMapper.selectList(new LambdaQueryWrapper<Education>()
                    .in(Education::getAcadId, acadId) );
            if(CollUtil.isNotEmpty(educationList)){
                for(Education e : educationList){
                    if(e.getEducation() != null && e.getEducation() < 4 && e.getGraduationYear() != null && !e.getGraduationYear().equals("")){
                        AcadMileage mileageEdu = new AcadMileage();
                        mileageEdu.setContent(this.getValueByName(e.getEducation()));
                        mileageEdu.setYear(this.getValueByName(e.getGraduationYear()));
                        education.add(mileageEdu);
                    }
                }
            }
            List<Work> workList = this.workMapper.selectList(new LambdaQueryWrapper<Work>()
                    .eq(Work::getAcadId, acadId)
                    .orderByAsc(Work::getJobStartYear));
            if(CollUtil.isNotEmpty(workList)){
                if(workList.get(0).getJobStartYear() != null && !workList.get(0).getJobStartYear().equals("")){
                    AcadMileage mileageWork = new AcadMileage();
                    mileageWork.setContent("4");//参加工作年
                    mileageWork.setYear(this.getValueByName(workList.get(0).getJobStartYear()));
                    education.add(mileageWork);
                }
            }
            acadAgeInfo.setEducation(education);
            //---------------------------------------院士教育信息 end-----------------------------------
            //---------------------------------------院士授衔年 start-----------------------------------
            List<Aos> aosList = this.aosMapper.selectList(new LambdaQueryWrapper<Aos>().eq(Aos::getAcadId, acadId)
                    .notIn(Aos::getAosNo,"77")
                    .orderByAsc(Aos::getElectedYear));//暂无科学院不显示
            if(CollUtil.isNotEmpty(aosList)){
                for(Aos a : aosList){
                    AcadMileage mileageAos = new AcadMileage();
                    mileageAos.setContent(this.getValueByName(a.getAosName()));
                    mileageAos.setYear(this.getValueByName(a.getElectedYear()));
                    aos.add(mileageAos);
                }
                acadAgeInfo.setAos(aos);
            }
            //---------------------------------------院士授衔年 end-----------------------------------
            //---------------------------------------重大荣誉获得年 start-----------------------------------
            List<Award> awardList = this.awardMapper.selectList(new LambdaQueryWrapper<Award>()
                    .eq(Award::getAcadId, acadId)
                    .orderByAsc(Award::getAwardYear));
            if(CollUtil.isNotEmpty(awardList)){
                for(Award a : awardList){
                    AcadMileage mileageAward = new AcadMileage();
                    mileageAward.setYear(this.getValueByName(a.getAwardYear()));
                    mileageAward.setContent(this.getValueByName(a.getAwardName()));
                    award.add(mileageAward);
                }
                acadAgeInfo.setAward(award);
            }
            //---------------------------------------重大荣誉获得年 end-----------------------------------

        }
        return acadAgeInfo;
    }

    /**
     * 根据院士编码列表查询院士姓名
     * @param list
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> getAcadNameByAcadList(List<String> list) throws Exception {
        List<Map<String, Object>> list1 = new ArrayList<>();
        if(CollUtil.isNotEmpty(list)){
           String acadId = String.join(",",list);
            List<Map<String, Object>> mapList = this.baseInfoMapper.getAcadNameByAcadList(acadId);
           if(CollUtil.isNotEmpty(mapList)){
               for(Map m : mapList){
                   Map<String,Object> mm = new HashMap<>();
                   if(m.get("acadId") != null){
                       mm.put(String.valueOf(m.get("acadId")),m.get("acadName"));
                       list1.add(mm);
                   }
               }
           }
        }
        return list1;
    }

    public String getValueByName(Object object){
        String str = "";
        if(object != null){
            str = String.valueOf(object);
        }
        return str;
    }

    /**
     * 拉黑操作
     */
    @Override
    public String pullBlack(BaseInfoForm baseInfoForm) throws Exception {

        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setIsBlack(baseInfoForm.getIsBlack());
        baseInfo.setAcadId(baseInfoForm.getAcadId());
        baseInfoMapper.updateById(baseInfo);
        //同步更新es中的字段
        baseInfo = this.getOne(new QueryWrapper<BaseInfo>().eq("acad_id",baseInfo.getAcadId()));
        //同步ES
        Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(baseInfo.getAcadId()));
        ClientAcad acad = optionalClientAcad.get();
        acad.setAcadId(String.valueOf(baseInfo.getAcadId()));
        acad.setBaseInfo(baseInfo);
        elasticClientAcadRepository.save(acad);
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
        //同步更新es中的字段
        baseInfo = this.getOne(new QueryWrapper<BaseInfo>().eq("acad_id",baseInfo.getAcadId()));
        Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
        ClientAcad acad = optionalClientAcad.get();
        acad.setAcadId(String.valueOf(baseInfo.getAcadId()));
        acad.setBaseInfo(baseInfo);
        elasticClientAcadRepository.save(acad);
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
    public void updateBaseInfo(BaseInfo baseInfo,Integer acadId) throws Exception {

        Date now = new Date();
        baseInfo.setDelFlag(true);//未删除
        baseInfo.setUpdateUserId(String.valueOf(JWTUtil.getUser().getUserId()));
        baseInfo.setUpdateTime(now);
        //mysql修改
        BaseInfo baseInfoOld = this.getOne(new QueryWrapper<BaseInfo>().eq("acad_id",acadId));
        baseInfo.setAcadId(acadId);
        this.updateById(baseInfo);

        baseInfo = this.getOne(new QueryWrapper<BaseInfo>().eq("acad_id",acadId));

        String str = UpdateLogUtil.UpdateLog(baseInfoOld,baseInfo);
        log.info("院士信息修改操作日志："+str);
        if(StringUtils.isNotEmpty(str)){
            str = str.substring(0,str.length()-1);
        }
        //增加院士修改日志
        AcadOperLog acadOperLog = new AcadOperLog();
        acadOperLog.setAcadId(Long.valueOf(baseInfo.getAcadId()));
        acadOperLog.setTitle(str);
        acadOperLog.setOperTime(LocalDateTime.now());
        acadOperLog.setBusinessType(2);
        acadOperLog.setOpUserId(JWTUtil.getUser().getUserId());
        this.acadLogService.insertOperlog(acadOperLog);
        //同步ES
        Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
        ClientAcad acad = optionalClientAcad.get();
        //同步更新es中的字段
        acad.setAcadId(String.valueOf(baseInfo.getAcadId()));

        //修改研究领域 文字
        baseInfo.setRsfCategory(RsfCategoryType.of(baseInfo.getRsfCategory()).getDesc());

        acad.setBaseInfo(baseInfo);
        elasticClientAcadRepository.save(acad);
    }

    /**
     * 根据院士编码删除院士信息（假删）
     * @param acadId
     * @throws Exception
     */
    @Override
    public void deleteBaseInfo(Integer acadId) throws Exception {
        this.baseInfoMapper.deleteBaseInfo(acadId,JWTUtil.getUser().getUserId(),new Date());
        this.elasticClientAcadRepository.deleteById(String.valueOf(acadId));
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
        Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
        ClientAcad acad = optionalClientAcad.get();
        if(acad != null){
            String nationPlace = "";
            if(CollUtil.isNotEmpty(acad.getNationalityList())){
                for (Nationality nation: acad.getNationalityList()) {
                    MstCountry mstCountry = mstCountryService.getOne(
                            new LambdaQueryWrapper<MstCountry>().eq(MstCountry::getCountryId,nation.getCountryId()));
                    if (mstCountry != null) {
                        nationPlace += mstCountry.getCountryCnname() + ",";
                    }
                }
                if (nationPlace.length() > 0) {
                    baseInfo.setNationPlace(nationPlace.substring(0,nationPlace.lastIndexOf(",")));
                }
            }
            //授衔机构
            String aosName = "";
            List<Aos> aosList = acad.getAosList();
            if(CollUtil.isNotEmpty(aosList)){
                for(Aos aos : aosList){
                    if(org.apache.commons.lang.StringUtils.isNotEmpty(aos.getAosName())){
                        aosName += aos.getAosName() + ",";
                    }
                }
            }
            if(aosName.length() > 0){
                baseInfo.setAosName(aosName.substring(0,aosName.lastIndexOf(",")));
            }
            //邮箱：显示有效的主邮箱
            List<Email> emailList = acad.getEmailList();
            if(CollUtil.isNotEmpty(emailList)){
                for(Email email : emailList){
                    if(email.getIsEffectiveEmail()!= null && email.getIsMainEmail()!= null && email.getIsEffectiveEmail() && email.getIsMainEmail()){//有效的主邮箱
                        baseInfo.setEmail(email.getEmail());
                        break;
                    }
                }
            }
            //头像显示展厅的头像
            List<PhotoForm> photoList = acad.getPhotoList();
            if(CollUtil.isNotEmpty(photoList)){
                for(PhotoForm p : photoList){
                    if(p.getIsHall()){//展厅图片
                        baseInfo.setPhoto(p.getPhotoUrl());
                        break;
                    }
                }
            }
        }

        return baseInfo;
    }

}
