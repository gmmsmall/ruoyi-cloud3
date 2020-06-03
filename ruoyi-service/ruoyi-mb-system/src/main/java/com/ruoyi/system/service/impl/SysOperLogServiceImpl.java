package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.mapper.SysOperLogMapper;
import com.ruoyi.system.params.AcadOpLogParams;
import com.ruoyi.system.result.AcadOpLogResult;
import com.ruoyi.system.service.ISysOperLogService;
import com.ruoyi.system.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService {
    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLog operLog) {
        operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog) {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     *
     * @param ids 需要删除的数据
     * @return
     */
    @Override
    public int deleteOperLogByIds(String ids) {
        return operLogMapper.deleteOperLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId) {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        operLogMapper.cleanOperLog();
    }

    @Override
    public List<AcadOpLogResult> selectAcadOperLogList(AcadOpLogParams acadOpLogParams) {
        String limit = "limit " + (acadOpLogParams.getPageNum() - 1) * acadOpLogParams.getPageSize() + "," + acadOpLogParams.getPageSize();
        SysOperLog sysOperLog = new SysOperLog();
        sysOperLog.setLimit(limit);
        sysOperLog.setBusinessType(acadOpLogParams.getOperType());
        sysOperLog.setOperName(acadOpLogParams.getOperName());
        sysOperLog.setBeginTime(acadOpLogParams.getBeginTime());
        sysOperLog.setEndTime(acadOpLogParams.getEndTime());
        List<SysOperLog> sysOperLogList = operLogMapper.selectOperLogList(sysOperLog);

        List<AcadOpLogResult> acadOpLogResults = new ArrayList<>();
        for (SysOperLog s : sysOperLogList) {
            AcadOpLogResult acadOpLogResult = new AcadOpLogResult();
            acadOpLogResult.setOperName(s.getOperName());
            acadOpLogResult.setOperTime(DateUtil.getDateFormat(s.getOperTime(), DateUtil.FULL_TIME_SPLIT_PATTERN));
            acadOpLogResult.setTitle(s.getTitle());
            switch (s.getBusinessType()) {
                case 0:
                    acadOpLogResult.setOperType("其他");
                    break;
                case 1:
                    acadOpLogResult.setOperType("新增");
                    break;
                case 2:
                    acadOpLogResult.setOperType("修改");
                    break;
                case 3:
                    acadOpLogResult.setOperType("删除");
                    break;
                case 4:
                    acadOpLogResult.setOperType("授权");
                    break;
                case 5:
                    acadOpLogResult.setOperType("导出");
                    break;
                case 6:
                    acadOpLogResult.setOperType("导入");
                    break;
            }
            //// TODO: 2020/6/3 操作日志院士姓名
            acadOpLogResult.setAcadName("院士姓名");
            acadOpLogResults.add(acadOpLogResult);
        }
        return acadOpLogResults;
    }
}
