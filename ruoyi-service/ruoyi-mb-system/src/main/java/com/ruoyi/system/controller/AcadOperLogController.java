package com.ruoyi.system.controller;

import com.ruoyi.common.auth.annotation.HasPermissions;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.AcadOperLog;
import com.ruoyi.system.params.AcadOpLogExportParams;
import com.ruoyi.system.params.AcadOpLogParams;
import com.ruoyi.system.result.AcadOpLogResult;
import com.ruoyi.system.result.ListResult;
import com.ruoyi.system.service.IAcadOperLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 操作日志记录 提供者
 *
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("acadLog")
@Api(value = "acadLog", description = "操作日志--院士信息日志")
public class AcadOperLogController extends BaseController {

    @Autowired
    private IAcadOperLogService sysOperLogService;

    /**
     * 查询院士信息日志记录列表
     */
//    @HasPermissions("monitor:operlog:list")
    @GetMapping("acadOpList")
    @ApiOperation(value = "查询院士信息日志记录列表", notes = "查询院士信息日志记录列表")
    public ListResult<AcadOpLogResult> acadOpList(AcadOpLogParams acadOpLogParams) {
        return sysOperLogService.selectAcadOperLogList(acadOpLogParams);
    }

    //    @HasPermissions("monitor:operlog:export")
    @GetMapping("/export")
    @ApiOperation(value = "院士信息日志导出", notes = "院士信息日志导出")
    public RE export(AcadOpLogExportParams acadOpLogExportParams, HttpServletResponse response) {
        AcadOpLogParams acadOpLogParams = new AcadOpLogParams();
        BeanUtils.copyProperties(acadOpLogExportParams, acadOpLogParams);
        acadOpLogParams.setPageSize(999999999);
        acadOpLogParams.setPageNum(1);
        List<AcadOpLogResult> list = sysOperLogService.selectAcadOperLogList(acadOpLogParams).getRows();
        ExcelUtil<AcadOpLogResult> util = new ExcelUtil<>(AcadOpLogResult.class);
        return util.exportExcel(list, "院士信息日志", response);
    }


    /**
     * 查询操作日志记录
     */
    @GetMapping("get/{operId}")
    public AcadOperLog get(@PathVariable("operId") Long operId) {
        return sysOperLogService.selectOperLogById(operId);
    }

    /**
     * 查询操作日志记录列表
     */
    @HasPermissions("monitor:operlog:list")
    @GetMapping("list")
    public R list(AcadOperLog sysOperLog) {
        startPage();
        return result(sysOperLogService.selectOperLogList(sysOperLog));
    }


    /**
     * 新增保存操作日志记录
     */
    @PostMapping("save")
    public void addSave(@RequestBody AcadOperLog sysOperLog) {
        sysOperLogService.insertOperlog(sysOperLog);
    }

    /**
     * 删除操作日志记录
     */
    @HasPermissions("monitor:operlog:remove")
    @PostMapping("remove")
    public R remove(String ids) {
        return toAjax(sysOperLogService.deleteOperLogByIds(ids));
    }

    @OperLog(title = "操作日志", businessType = BusinessType.CLEAN)
    @HasPermissions("monitor:operlog:remove")
    @PostMapping("/clean")
    public R clean() {
        sysOperLogService.cleanOperLog();
        return R.ok();
    }
}
