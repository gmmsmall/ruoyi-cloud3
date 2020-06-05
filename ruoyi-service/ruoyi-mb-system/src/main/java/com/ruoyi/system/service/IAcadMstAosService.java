package com.ruoyi.system.service;

import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.params.AosParams;
import com.ruoyi.system.result.AosResult;

import java.util.List;

/**
 * @author jxd
 */
public interface IAcadMstAosService {

    /**
     * @return int
     * @Author jxd
     * @Description 新增科学院
     * @Date 17:45 2020/5/28
     * @Param [aosParams]
     **/
    public int addAos(AosParams aosParams);

    /**
     * @return int
     * @Author jxd
     * @Description 更新科学院
     * @Date 17:45 2020/5/28
     * @Param [aos]
     **/
    public int updateAos(Aos aos);

    /**
     * @return java.util.List<com.ruoyi.system.domain.Aos>
     * @Author jxd
     * @Description 查询科学院列表
     * @Date 17:51 2020/5/28
     * @Param []
     **/
    public List<Aos> listAos(String aosContinent);

    public List<AosResult> getList();

    public int initAosList();
}
