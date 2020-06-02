package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.RuoyiException;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.AcadMstAos;
import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.feign.RemoteIBlockAosService;
import com.ruoyi.system.feign.RemoteIBlockUserService;
import com.ruoyi.system.mapper.AcadMstAosMapper;
import com.ruoyi.system.params.AosParams;
import com.ruoyi.system.result.AosResult;
import com.ruoyi.system.result.FabricResult;
import com.ruoyi.system.service.IAcadMstAosService;
import com.ruoyi.system.service.ISysUserService;
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
    @Autowired
    private RemoteIBlockUserService remoteIBlockUserService;
    @Autowired
    private ISysUserService sysUserService;

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
        } else {
            throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
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
                String aosResult = remoteIBlockUserService.queryUserAos(String.valueOf(sysUserService.getUser().getUserId()));
                if (null != aosResult) {
                    FabricResult aosFabricResult = JSON.parseObject(aosResult, FabricResult.class);
                    if (aosFabricResult.getCode() == FabricResult.RESULT_SUCC) {
                        List<String> aosNos = new ArrayList<>();
                        for (Aos a : aosFabricResult.getAosList()) {
                            aosNos.add(a.getAosNo());
                        }
                        if (aosNos.size() > 0) {
                            for (AosResult a : aosResultList) {
                                if (aosNos.contains(a.getAosNo())) {
                                    a.setIsCheck(AosResult.IS_CHECK);
                                }
                            }
                        }
                    }
                } else {
                    throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
                }
                return aosResultList;
            }
        } else {
            throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
        }
        return null;
    }
}