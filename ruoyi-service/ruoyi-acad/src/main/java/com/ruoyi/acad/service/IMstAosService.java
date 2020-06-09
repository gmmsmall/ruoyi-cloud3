package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.MstAos;

import java.util.List;

/**
 * @Author guomiaomiao
 * @Date 2020/6/9 10:03
 * @Version 1.0
 */

public interface IMstAosService extends IService<MstAos> {

    /**
     * 根据大洲编码查询科学院列表
     * @param code
     * @return
     */
    List<MstAos> getAosByType(String code);
}
