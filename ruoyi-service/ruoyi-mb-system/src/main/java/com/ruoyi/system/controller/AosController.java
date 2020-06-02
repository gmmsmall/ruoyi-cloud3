package com.ruoyi.system.controller;

import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.params.AosParams;
import com.ruoyi.system.service.IAcadMstAosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author jxd
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/aos")
@Api(value = "/aos", description = "科学院管理")
public class AosController {

    @Autowired
    private IAcadMstAosService iAcadMstAosService;

    @GetMapping("getList")
    @OperLog(title = "数据权限列表")
    @ApiOperation(value = "数据权限列表", notes = "数据权限列表")
//    @RequiresPermissions("aos:add")
    public RE getList() {
        try {
            return RE.ok(iAcadMstAosService.getList());
        } catch (Exception e) {
            log.error("查询数据权限列表失败", e);
            return RE.error();
        }
    }

    @PostMapping("save")
    @OperLog(title = "新增科学院", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增科学院", notes = "新增科学院")
//    @RequiresPermissions("aos:add")
    public RE addAos(@RequestBody @Valid AosParams aosParams) {
        try {
            return iAcadMstAosService.addAos(aosParams) > 0 ? RE.ok() : RE.error();
        } catch (Exception e) {
            log.error("新增科学院失败", e);
            return RE.error();
        }
    }

    @PostMapping("update")
    @OperLog(title = "更新科学院", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "更新科学院", notes = "更新科学院")
//    @RequiresPermissions("aos:add")
    public RE updateAos(@RequestBody @Valid Aos aos) {
        try {
            return iAcadMstAosService.updateAos(aos) > 0 ? RE.ok() : RE.error();
        } catch (Exception e) {
            log.error("更新科学院失败", e);
            return RE.error();
        }
    }

    @GetMapping("list")
    @OperLog(title = "查询科学院列表")
    @ApiOperation(value = "查询科学院列表", notes = "查询科学院列表")
//    @RequiresPermissions("aos:add")
    public RE list() {
        try {
            return RE.ok(iAcadMstAosService.listAos());
        } catch (Exception e) {
            log.error("查询科学院列表失败", e);
            return RE.error();
        }
    }
}
