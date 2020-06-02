package com.ruoyi.acad.controller;

import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.client.ClientSearchCriteria;
import com.ruoyi.acad.domain.QueryRequest;
import com.ruoyi.acad.service.IClientAcadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/clientAcad")
@Api(value = "/clientAcad", description = "院士信息查询")
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
    @ApiOperation(value = "根据对应条件查询列表")
    public Page<ClientAcad> getAcadList(QueryRequest queryRequest, @RequestBody ClientSearchCriteria clientSearchCriteria) throws Exception {

        Page<ClientAcad> clientAcadIterable = clientAcadService.getBaseInfoList(queryRequest, clientSearchCriteria);

        return clientAcadIterable;
    }

}