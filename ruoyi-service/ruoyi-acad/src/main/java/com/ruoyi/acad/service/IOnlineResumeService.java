package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.OnlineResume;

import java.util.List;

/**
 * @author gmm
 */
public interface IOnlineResumeService extends IService<OnlineResume> {

    /**
     * 根据院士编码列表查询院士简历列表
     * @param acadIdList
     * @return
     */
    public List<OnlineResume> getAllByAcadIdList(List<String> acadIdList);

}
