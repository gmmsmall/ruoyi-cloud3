package com.ruoyi.acad.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.client.ClientSearchCriteria;
import com.ruoyi.acad.dao.BaseInfoMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.BaseInfo;
import com.ruoyi.acad.domain.BaseInfoEs;
import com.ruoyi.acad.domain.QueryRequest;
import com.ruoyi.acad.form.BaseInfoForm;
import com.ruoyi.acad.service.IBaseInfoService;
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

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        LocalDateTime now = LocalDateTime.now();
        baseInfo.setCreateTime(now);//新增时间
        baseInfo.setUpdateTime(now);//创建时间
        this.save(baseInfo);

        ClientAcad acad = new ClientAcad();
        acad.setAcadId(String.valueOf(acadId));
        BaseInfoEs baseInfoEs = new BaseInfoEs();
        BeanUtil.copyProperties(baseInfo,baseInfoEs);
        /*DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        baseInfoEs.setUpdateTime(dateTimeFormatter.format(now));
        baseInfoEs.setCreateTime(dateTimeFormatter.format(now));*/
        acad.setBaseInfo(baseInfoEs);
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

        LocalDateTime now = LocalDateTime.now();
        baseInfo.setUpdateTime(now);
        //mysql修改
        this.updateById(baseInfo);

        baseInfo = this.getOne(new QueryWrapper<BaseInfo>().eq("acad_id",baseInfo.getAcadId()));
        ClientAcad acad = new ClientAcad();
        BaseInfoEs baseInfoEs = new BaseInfoEs();
        BeanUtil.copyProperties(baseInfo,baseInfoEs);
        /*DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        baseInfoEs.setUpdateTime(dateTimeFormatter.format(now));*/
        acad.setAcadId(String.valueOf(baseInfo.getAcadId()));
        acad.setBaseInfo(baseInfoEs);
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
