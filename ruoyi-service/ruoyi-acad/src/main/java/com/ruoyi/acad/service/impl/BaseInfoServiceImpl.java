package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.BaseInfoMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.BaseInfo;
import com.ruoyi.acad.form.BaseInfoForm;
import com.ruoyi.acad.service.IBaseInfoService;
import com.ruoyi.common.redis.util.JWTUtil;
import com.ruoyi.system.domain.AcadOperLog;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.feign.RemoteAcadLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.Date;
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
     * 院士信息修改日志
     */
    @Autowired
    private RemoteAcadLogService acadLogService;


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

        //增加院士修改日志
        AcadOperLog acadOperLog = new AcadOperLog();
        acadOperLog.setAcadId(Long.valueOf(baseInfo.getAcadId()));
        acadOperLog.setTitle("新增院士信息");
        acadOperLog.setBusinessType(1);
        acadOperLog.setOpUserId(JWTUtil.getUser().getUserId());
        this.acadLogService.insertOperlog(acadOperLog);


        ClientAcad acad = new ClientAcad();
        acad.setAcadId(String.valueOf(acadId));
        /*BaseInfoEs baseInfoEs = new BaseInfoEs();
        BeanUtil.copyProperties(baseInfo,baseInfoEs);*/
        /*DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        baseInfoEs.setUpdateTime(dateTimeFormatter.format(now));
        baseInfoEs.setCreateTime(dateTimeFormatter.format(now));*/
        acad.setBaseInfo(baseInfo);
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
        ClientAcad acad = new ClientAcad();
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
        ClientAcad acad = new ClientAcad();
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
    public void updateBaseInfo(BaseInfo baseInfo) throws Exception {

        Date now = new Date();
        baseInfo.setDelFlag(true);//未删除
        baseInfo.setUpdateTime(now);
        //mysql修改
        this.updateById(baseInfo);

        baseInfo = this.getOne(new QueryWrapper<BaseInfo>().eq("acad_id",baseInfo.getAcadId()));
        ClientAcad acad = new ClientAcad();
        /*BaseInfoEs baseInfoEs = new BaseInfoEs();
        BeanUtil.copyProperties(baseInfo,baseInfoEs);*/
        /*DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        baseInfoEs.setUpdateTime(dateTimeFormatter.format(now));*/
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

        return baseInfo;
    }

}
