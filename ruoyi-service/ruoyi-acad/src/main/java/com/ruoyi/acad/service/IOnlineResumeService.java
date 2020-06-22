package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.OnlineResume;
import com.ruoyi.common.core.domain.RE;

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

    /**
     * 根据院士编码查看简历
     * @param acadecode
     * @return
     */
    public OnlineResume getModelByAcadecode(String acadecode);

    /**
     * 根据院士编码在线生产简历
     * @param acadId
     */
    public RE generateResume(Integer acadId);

    /**
     * 初始化所有院士简历
     */
    public void initResume();

}
