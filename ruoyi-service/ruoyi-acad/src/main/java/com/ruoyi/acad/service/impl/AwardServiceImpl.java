package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.AwardMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Award;
import com.ruoyi.acad.service.IAwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Description：获奖信息逻辑实现层<br/>
 * CreateTime ：2020年3月20日下午2:54:50<br/>
 * CreateUser：ys<br/>
 */
@Service
public class AwardServiceImpl extends ServiceImpl<AwardMapper, Award> implements IAwardService {

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private ElasticClientAcadRepository elasticClientAcadRepository;


    /**
     * 根据ID获取对应获奖信息列表
     */
    @Override
    public List<Award> getModelById(Integer acadId) throws Exception {

        List<Award> awardList = awardMapper.selectList(new LambdaQueryWrapper<Award>().eq(Award::getAcadId, acadId).orderByDesc(Award::getAwardYear));
        return awardList;
    }

    /**
     * 保存获奖信息
     */
    @Override
    public List<Award> saveModel(List<Award> awardList, Integer acadId) throws Exception {

        if (awardList != null && awardList.size() > 0) {
            awardList.stream().forEach(x -> {
                x.setAcadId(acadId);
                this.save(x);
            });
            Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
            ClientAcad clientAcad = optionalClientAcad.get();
            clientAcad.setAwardList(awardList);
            elasticClientAcadRepository.save(clientAcad);
        }

        return awardList;
    }

    /**
     * 修改获奖信息
     */
    @Override
    public List<Award> updateModel(List<Award> awardList, Integer acadId) throws Exception {

        LambdaQueryWrapper<Award> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Award::getAcadId,acadId);
        awardMapper.delete(queryWrapper);
        awardList.stream().forEach(x -> {
            x.setAcadId(acadId);
            this.awardMapper.insert(x);
        });
        Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
        ClientAcad clientAcad = optionalClientAcad.get();
        clientAcad.setAwardList(awardList);
        elasticClientAcadRepository.save(clientAcad);

        return awardList;
    }
}
