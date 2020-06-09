package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.dao.MstCountryMapper;
import com.ruoyi.acad.domain.MstCountry;
import com.ruoyi.acad.service.IMstCountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @Author guomiaomiao
 * @Date 2020/6/9 10:05
 * @Version 1.0
 */
@Service
public class MstCountryServiceImpl extends ServiceImpl<MstCountryMapper, MstCountry> implements IMstCountryService {

    @Autowired
    private MstCountryMapper countryMapper;

    /**
     * 根据大洲编码查询国家列表
     * @param code
     * @return
     */
    @Override
    public List<MstCountry> getCountryByType(String code) {
        return this.countryMapper.getCountryByType(code);
    }

}
