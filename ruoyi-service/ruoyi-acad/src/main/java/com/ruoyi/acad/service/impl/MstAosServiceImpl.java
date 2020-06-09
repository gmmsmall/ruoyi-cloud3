package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.dao.MstAosMapper;
import com.ruoyi.acad.domain.MstAos;
import com.ruoyi.acad.service.IMstAosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author guomiaomiao
 * @Date 2020/6/9 10:05
 * @Version 1.0
 */
@Service
public class MstAosServiceImpl extends ServiceImpl<MstAosMapper, MstAos> implements IMstAosService {

    @Autowired
    private MstAosMapper mstAosMapper;

    /**
     * 根据大洲编码查询科学院列表
     * @param code
     * @return
     */
    @Override
    public List<MstAos> getAosByType(String code) {
        return this.mstAosMapper.getAosByType(code);
    }
}
