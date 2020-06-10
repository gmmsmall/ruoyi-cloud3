package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.MstCountry;

import java.util.List;

/**
 * @Author guomiaomiao
 * @Date 2020/6/9 10:03
 * @Version 1.0
 */

public interface IMstCountryService extends IService<MstCountry> {

    /**
     * 根据大洲编码查询国家列表
     * @param code
     * @return
     */
    List<MstCountry> getCountryByType(String code);

    /**
     * 获取所有国家列表
     * @return
     */
    List<MstCountry> getAllCountry();
}
