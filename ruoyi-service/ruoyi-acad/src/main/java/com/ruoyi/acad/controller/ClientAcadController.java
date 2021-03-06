package com.ruoyi.acad.controller;

import cn.gjing.tools.excel.ExcelFactory;
import cn.gjing.tools.excel.driven.ExcelWrite;
import cn.gjing.tools.excel.driven.ExcelWriteWrapper;
import cn.gjing.tools.excel.write.BigTitle;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.acad.client.ClientSearchCriteria;
import com.ruoyi.acad.domain.QueryRequest;
import com.ruoyi.acad.form.BaseInfoExcelForm;
import com.ruoyi.acad.form.BaseInfoPage;
import com.ruoyi.acad.service.IClientAcadService;
import com.ruoyi.acad.utils.MyStyleListener;
import com.ruoyi.common.json.JSON;
import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
    @ApiOperation(value = "全文检索院士列表")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功",response = BaseInfoPage.class)})
    public BaseInfoPage wholeWordSearch(QueryRequest queryRequest, @ApiParam(value = "查询参数") @RequestBody(required = false) String wholeWord) throws Exception {
        //如果没有查询条件，默认显示全部的数据
        if(wholeWord != null && !wholeWord.trim().equals("") && !wholeWord.equals("null") ){
            com.alibaba.fastjson.JSONObject json = JSONObject.parseObject(wholeWord);
            if(json.get("wholeWord") != null && !json.get("wholeWord").equals("")){
                log.info("进行全文检索");
                return this.clientAcadService.wholeWordSearch(queryRequest, wholeWord);
            }else{
                log.info("进行普通列表查询");
                ClientSearchCriteria clientSearchCriteria = new ClientSearchCriteria();
                clientSearchCriteria.setAcadName("");
                return this.clientAcadService.getBaseInfoList(queryRequest, clientSearchCriteria);
            }
        }else{
            log.info("进行普通列表查询");
            ClientSearchCriteria clientSearchCriteria = new ClientSearchCriteria();
            clientSearchCriteria.setAcadName("");
            return this.clientAcadService.getBaseInfoList(queryRequest, clientSearchCriteria);
        }
    }

    @PostMapping("/excel")
    @ApiOperation(value="导出院士信息列表", notes="导出院士信息列表")
    @ApiResponses({@ApiResponse(code = 200, message = "导出成功")})
    @ExcelWrite(mapping = BaseInfoExcelForm.class)
    public void export(QueryRequest request, @ApiParam(value = "查询参数") @RequestBody ClientSearchCriteria clientSearchCriteria, HttpServletResponse response) {
        log.info("导出院士信息列表");
        try {
            request.setPageNum(0);
            request.setPageSize(9999);
            List<BaseInfoExcelForm> list = this.clientAcadService.getBaseInfoExcelList(request,clientSearchCriteria);
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode("院士信息表.xls", "UTF-8"));
            response.setHeader("Content-Type", "multipart/form-data");
            ExcelFactory.createWriter(BaseInfoExcelForm.class, response)
                    //加入自己定义的样式
                    .addListener(new MyStyleListener())
                    .write(list)
                    .flush();

        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出院士信息列表失败");
        }
    }

}