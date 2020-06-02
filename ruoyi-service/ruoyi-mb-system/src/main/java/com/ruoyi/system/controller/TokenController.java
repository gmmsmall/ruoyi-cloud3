package com.ruoyi.system.controller;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.exception.RuoyiException;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.system.domain.Token;
import com.ruoyi.system.domain.TokenForQuery;
import com.ruoyi.system.domain.TokenTree;
import com.ruoyi.system.result.DeleteTokensParams;
import com.ruoyi.system.service.ITokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
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
@RequestMapping("/token")
@Api(value = "/token", description = "令牌管理")
public class TokenController {

    @Autowired
    private ITokenService tokenService;

//    @ApiOperation(value = "信息权限列表", notes = "信息权限列表")
//    @GetMapping("getList")
////    @RequiresPermissions("token:view")
//    public List<TokenResult> getList() {
//        return tokenService.getList();
//    }

    @ApiOperation(value = "信息权限列表", notes = "信息权限列表")
    @GetMapping("list")
//    @RequiresPermissions("token:view")
    public TokenTree<T> tokenList(TokenForQuery tokenForQuery) {
        return tokenService.findTokens(tokenForQuery);
    }

    @PostMapping("save")
    @OperLog(title = "新增令牌", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增令牌", notes = "新增令牌")
//    @RequiresPermissions("token:add")
    public RE addToken(@RequestBody @Valid Token token) {
        try {
            return tokenService.createToken(token) > 0 ? RE.ok() : RE.error();
        } catch (RuoyiException e) {
            return RE.error(e.getCode(), e.getMsg());
        }
    }

    @PostMapping("update")
    @OperLog(title = "修改令牌", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改令牌", notes = "修改令牌")
//    @RequiresPermissions("token:update")
    public RE updateToken(@RequestBody @Valid Token token) {
        try {
            return tokenService.updateToken(token) > 0 ? RE.ok() : RE.error();
        } catch (RuoyiException e) {
            return RE.error(e.getCode(), e.getMsg());
        }
    }

    @PostMapping("delete")
    @OperLog(title = "删除令牌", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除令牌", notes = "删除令牌")
//    @RequiresPermissions("token:delete")
    public RE deleteTokens(@RequestBody @Valid DeleteTokensParams params) {
        try {
            String[] tokenNoArray = params.getTokenNos().split(Constants.COMMA);
            return tokenService.deleteTokens(tokenNoArray) > 0 ? RE.ok() : RE.error();
        } catch (RuoyiException e) {
            return RE.error(e.getCode(), e.getMsg());
        }
    }

    @GetMapping(value = "/init")
    @ApiOperation(value = "初始化区块链令牌信息", notes = "初始化区块链令牌信息")
    public RE initTokenList() {
        return tokenService.initTokenList() > 0 ? RE.ok() : RE.error();
    }
}
