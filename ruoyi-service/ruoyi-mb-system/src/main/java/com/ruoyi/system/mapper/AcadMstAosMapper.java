package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.AcadMstAos;

import java.util.List;

/**
 * @author jxd
 */
public interface AcadMstAosMapper {

    public int insertAos(AcadMstAos acadMstAos);

    public int updateMSTAos(AcadMstAos acadMstAos);

    public int updateACADAos(AcadMstAos acadMstAos);

    List<AcadMstAos> selectList();
}
