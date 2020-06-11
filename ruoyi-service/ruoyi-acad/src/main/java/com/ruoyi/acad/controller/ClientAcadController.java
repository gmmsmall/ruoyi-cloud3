package com.ruoyi.acad.controller;

import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.client.ClientSearchCriteria;
import com.ruoyi.acad.domain.QueryRequest;
import com.ruoyi.acad.form.BaseInfoPage;
import com.ruoyi.acad.form.BaseInfoShowForm;
import com.ruoyi.acad.service.IClientAcadService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/clientAcad")
@Api(tags = "院士信息查询")
public class ClientAcadController {

    @Autowired
    private IClientAcadService clientAcadService;

    /**
     * 根据对应条件查询列表
     *
     * @param queryRequest
     * @param clientSearchCriteria 查询条件集合
     * @return
     * @throws Exception
     */
    @PostMapping("/getAcadList")
    @ApiOperation(value = "根据对应条件查询院士列表")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功",response = BaseInfoPage.class)})
    public BaseInfoPage getAcadList(QueryRequest queryRequest, @ApiParam(value = "查询参数") @RequestBody ClientSearchCriteria clientSearchCriteria) throws Exception {
        return this.clientAcadService.getBaseInfoList(queryRequest, clientSearchCriteria);
    }

    /**
     * 全文检索查询列表
     *
     * @param queryRequest
     * @param wholeWord 全文检索条件
     * @return
     * @throws Exception
     */
    @PostMapping("/wholeWordSearch")
    @ApiOperation(value = "根据对应条件查询院士列表")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功",response = BaseInfoPage.class)})
    public BaseInfoPage wholeWordSearch(QueryRequest queryRequest, @ApiParam(value = "查询参数") @RequestBody String wholeWord) throws Exception {
        return this.clientAcadService.wholeWordSearch(queryRequest, wholeWord);
    }

}