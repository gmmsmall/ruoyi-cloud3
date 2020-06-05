package com.ruoyi.system.service.impl;

import com.google.common.base.Joiner;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.AcadOperLog;
import com.ruoyi.system.mapper.AcadOperLogMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.params.AcadOpLogParams;
import com.ruoyi.system.result.AcadOpLogResult;
import com.ruoyi.system.result.ListResult;
import com.ruoyi.system.result.QueryRequest;
import com.ruoyi.system.service.IAcadOperLogService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.util.DateUtil;
import io.netty.util.internal.StringUtil;
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
public class AcadOperLogServiceImpl implements IAcadOperLogService {

    @Autowired
    private AcadOperLogMapper operLogMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(AcadOperLog operLog) {
        operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<AcadOperLog> selectOperLogList(AcadOperLog operLog) {
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
    public AcadOperLog selectOperLogById(Long operId) {
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
    public ListResult<AcadOpLogResult> selectAcadOperLogList(AcadOpLogParams acadOpLogParams) {
        String limit = "limit " + (acadOpLogParams.getPageNum() - 1) * acadOpLogParams.getPageSize() + "," + acadOpLogParams.getPageSize();
        AcadOperLog sysOperLog = new AcadOperLog();
        sysOperLog.setLimit(limit);
        sysOperLog.setBusinessType(acadOpLogParams.getOperType());
        if (!StringUtil.isNullOrEmpty(acadOpLogParams.getOperName())) {
            List<Long> userIds = sysUserMapper.selectUserIdsByUserName(acadOpLogParams.getOperName());
            sysOperLog.setOpUserIds(Joiner.on(",").join(userIds));
        }
        if (!StringUtil.isNullOrEmpty(acadOpLogParams.getAcadName())) {
            //todo 根据院士姓名查看院士ids
//            List<Long> userIds =sysUserMapper.selectUserIdsByUserName(acadOpLogParams.getOperName());
//            sysOperLog.setAcadIds(Joiner.on(",").join(userIds));
        }
        sysOperLog.setBeginTime(acadOpLogParams.getBeginTime());
        sysOperLog.setEndTime(acadOpLogParams.getEndTime());
        List<AcadOperLog> sysOperLogList = operLogMapper.selectOperLogList(sysOperLog);

        List<AcadOpLogResult> acadOpLogResults = new ArrayList<>();
        for (AcadOperLog s : sysOperLogList) {
            AcadOpLogResult acadOpLogResult = new AcadOpLogResult();
            acadOpLogResult.setOperName(iSysUserService.getUserById(s.getOpUserId()).getUserName());
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
                    acadOpLogResult.setOperType("拉黑");
                    break;
                case 5:
                    acadOpLogResult.setOperType("展示");
                    break;
            }
            //// TODO: 2020/6/3 根据院士id查看院士姓名
            acadOpLogResult.setAcadName("等待接口，下次发版加上");
            acadOpLogResults.add(acadOpLogResult);
        }
        return ListResult.list(acadOpLogResults, operLogMapper.selectCount(), new QueryRequest(acadOpLogParams.getPageSize(), acadOpLogParams.getPageNum()));
    }
}
