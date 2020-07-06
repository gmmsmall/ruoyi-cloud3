package com.ruoyi.acad.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.config.RabbitConfig;
import com.ruoyi.acad.dao.AosMapper;
import com.ruoyi.acad.dao.BaseInfoMapper;
import com.ruoyi.acad.dao.NationalityMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Aos;
import com.ruoyi.acad.domain.BaseInfo;
import com.ruoyi.acad.domain.MstCountry;
import com.ruoyi.acad.domain.Nationality;
import com.ruoyi.acad.form.BaseInfoAcadIdForm;
import com.ruoyi.acad.form.BaseInfoAcadIdIntegerForm;
import com.ruoyi.acad.form.BaseInfoBatch;
import com.ruoyi.acad.form.BaseInfoForm;
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
        aos.setAosNo("1592550962574268722");
        aos.setAosName("暂无");
        aosMapper.insert(aos);

        //增加院士修改日志
        AcadOperLog acadOperLog = new AcadOperLog();
        acadOperLog.setAcadId(Long.valueOf(baseInfo.getAcadId()));
        acadOperLog.setTitle("新增院士信息");
        acadOperLog.setBusinessType(1);
        acadOperLog.setOpUserId(JWTUtil.getUser().getUserId());
        this.acadLogService.insertOperlog(acadOperLog);

        ClientAcad acad = new ClientAcad();
        acad.setAcadId(String.valueOf(acadId));
        baseInfo.setAosName("暂无");
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
            baseInfo.setContactMethon(baseInfo.getContactMethon());
            baseInfo.setSignType(baseInfo.getSignType());
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
                acadOperLog.setBusinessType(2);
                acadOperLog.setOpUserId(JWTUtil.getUser().getUserId());
                this.acadLogService.insertOperlog(acadOperLog);
                baseInfo = this.getOne(new QueryWrapper<BaseInfo>().eq("acad_id",acadIdForm.getAcadId()));

                String nationStr = "";//多个国籍拼接成的字符串
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
                }
                //同步刷新es中的院士信息和国籍信息
                Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadIdForm.getAcadId()));
                ClientAcad acad = optionalClientAcad.get();
                acad.setAcadId(String.valueOf(acadIdForm.getAcadId()));
                baseInfo.setNationPlace(nationStr);
                acad.setBaseInfo(baseInfo);
                acad.setNationalityList(baseInfoBatch.getNationalityList());
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
        acadOperLog.setBusinessType(2);
        acadOperLog.setOpUserId(JWTUtil.getUser().getUserId());
        this.acadLogService.insertOperlog(acadOperLog);
        //同步ES
        Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
        ClientAcad acad = optionalClientAcad.get();
        //同步更新es中的字段
        acad.setAcadId(String.valueOf(baseInfo.getAcadId()));
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
        }

        return baseInfo;
    }

}
