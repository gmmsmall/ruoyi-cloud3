package com.ruoyi.system.service;

import com.ruoyi.system.domain.AcadOperLog;
import com.ruoyi.system.params.AcadOpLogParams;
import com.ruoyi.system.result.AcadOpLogResult;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author ruoyi
 */
public interface IAcadOperLogService {
    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    public void insertOperlog(AcadOperLog operLog);

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    public List<AcadOperLog> selectOperLogList(AcadOperLog operLog);

    /**
     * 批量删除系统操作日志
     *
     * @param ids 需要删除的数据
     * @return 结果
     */
    public int deleteOperLogByIds(String ids);

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    public AcadOperLog selectOperLogById(Long operId);

    /**
     * 清空操作日志
     */
    public void cleanOperLog();

    /**
     * 查询院士信息日志记录列表
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    public List<AcadOpLogResult> selectAcadOperLogList(AcadOpLogParams acadOpLogParams);
}
