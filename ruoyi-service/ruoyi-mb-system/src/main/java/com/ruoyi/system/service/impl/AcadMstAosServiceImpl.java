package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.AcadMstAos;
import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.feign.RemoteIBlockAosService;
import com.ruoyi.system.mapper.AcadMstAosMapper;
import com.ruoyi.system.params.AosParams;
import com.ruoyi.system.result.AosResult;
import com.ruoyi.system.result.FabricResult;
import com.ruoyi.system.service.IAcadMstAosService;
import com.ruoyi.system.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jxd
 */
@Service
public class AcadMstAosServiceImpl implements IAcadMstAosService {

    @Autowired
    private RemoteIBlockAosService remoteIBlockAosService;
    @Autowired
    private AcadMstAosMapper acadMstAosMapper;

    @Override
    public int addAos(AosParams aosParams) {
        AcadMstAos acadMstAos = new AcadMstAos();
        BeanUtils.copyProperties(aosParams, acadMstAos);
        aosParams.setAosNo(IdGenerator.getId());
        acadMstAosMapper.insertAos(acadMstAos);
        Aos aos = new Aos();
        BeanUtils.copyProperties(aosParams, aos);
        String result = remoteIBlockAosService.addAos(aos);
        return result != null && JSON.parseObject(result, FabricResult.class).getCode() == FabricResult.RESULT_SUCC ? 1 : 0;
    }

    @Override
    public int updateAos(Aos aos) {
        AcadMstAos acadMstAos = new AcadMstAos();
        BeanUtils.copyProperties(aos, acadMstAos);
        acadMstAosMapper.updateAos(acadMstAos);
        String result = remoteIBlockAosService.updateAos(aos);
        return result != null && JSON.parseObject(result, FabricResult.class).getCode() == FabricResult.RESULT_SUCC ? 1 : 0;
    }

    @Override
    public List<Aos> listAos() {
        String result = remoteIBlockAosService.queryAos();
        if (null != result) {
            FabricResult fabricResult = JSON.parseObject(result, FabricResult.class);
            if (fabricResult.getCode() == FabricResult.RESULT_SUCC) {
                return fabricResult.getAosList();
            }
        }
        return null;
    }

    @Override
    public List<AosResult> getList() {
        String result = remoteIBlockAosService.queryAos();
        if (null != result) {
            FabricResult fabricResult = JSON.parseObject(result, FabricResult.class);
            if (fabricResult.getCode() == FabricResult.RESULT_SUCC) {
                List<AosResult> aosResultList = new ArrayList<>();
                for (Aos aos : fabricResult.getAosList()) {
                    AosResult aosResult = new AosResult();
                    BeanUtils.copyProperties(aos, aosResult);
                    aosResultList.add(aosResult);
                }
                return aosResultList;
            }
        }
        return null;
    }
}
