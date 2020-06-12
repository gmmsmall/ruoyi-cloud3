package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.dao.OnlineResumeMapper;
import com.ruoyi.acad.domain.OnlineResume;
import com.ruoyi.acad.service.IOnlineResumeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gmm
 */
@Service
public class OnlineResumeServiceImpl extends ServiceImpl<OnlineResumeMapper, OnlineResume> implements IOnlineResumeService {

    /**
     * 根据院士编码列表查询简历列表
     * @param acadIdList
     * @return
     */
    @Override
    public List<OnlineResume> getAllByAcadIdList(List<String> acadIdList) {
        List<OnlineResume> list = this.baseMapper.selectList(new QueryWrapper<OnlineResume>()
                .eq("deleteflag",1)
                .in("acadecode",acadIdList));
        return list;
    }
}
