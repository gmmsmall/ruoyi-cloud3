package com.ruoyi.acad.controller;

import cn.hutool.core.collection.CollUtil;
import com.ruoyi.acad.domain.Attachment;
import com.ruoyi.acad.domain.Award;
import com.ruoyi.acad.service.IAttachmentService;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.redis.util.JWTUtil;
import com.ruoyi.fdfs.feign.RemoteFdfsService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.core.MediaType;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Description：附件控制层<br/>
 * CreateTime ：2020年6月15日下午14:33:16<br/>
 * CreateUser：gmm<br/>
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/acad/attachment")
@Api(tags = "院士附件管理")
public class AttachmentController{
    
    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private RemoteFdfsService fdfsService;
	
    /**
     * Description:上传附件
     * @param file
     * @param acadId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "上传文件", notes = "选择文件上传")
    @ApiResponses({@ApiResponse(code = 200,message = "上传成功")})
    @RequestMapping(value = "/upload",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON,headers = "content-type=multipart/form-data")
    @OperLog(title = "上传院士附件", businessType = BusinessType.INSERT)
    public RE upload(@RequestPart("file") MultipartFile file,
                     @RequestParam("acadId") @ApiParam(value = "院士编码id",required = true) Integer acadId,
                     @RequestParam("name") @ApiParam(value = "附件名称",required = true) String name) throws Exception {
    	Map<String,Object> map = this.fdfsService.upload(file);
        String url = "";
        if(map != null){
            List<Map<String,Object>> results = (List<Map<String,Object>>)map.get("object");
            if(CollUtil.isNotEmpty(results)){
                url = String.valueOf(results.get(0).get("url"));
            }
        }
        Attachment attachment = new Attachment();
        attachment.setAttachmentUrl(url);
        attachment.setAcadId(acadId);
        attachment.setAttachmentName(name);
        attachment.setUploadUserId(JWTUtil.getUser().getUserId());
        this.attachmentService.save(attachment);
        return new RE().ok("上传成功");
    }
    
    /**
     * Description:读出文件    
     * TODO 暂时无写入操作，代码来源与 fastDFSController，后期若有写入操作
     * 		需要重新修改方法
     * @param fileUrl
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "下载文件", notes = "根据url下载文件")
    @ApiResponses({@ApiResponse(code = 200,message = "下载成功")})
    @RequestMapping(value = "/download",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON)
    public void downLoad(@RequestParam("fileUrl") @ApiParam(value = "下载路径",required = true)String fileUrl, HttpServletRequest request, HttpServletResponse response) throws Exception {

        this.fdfsService.download(fileUrl);

    }
    
    /**
     * Description:根据院士编码获取对应的上传记录
     * @param acadId
     * @return
     * @throws Exception
     */

    @GetMapping("/getAllList")
    @ApiOperation(value = "查询院士的附件列表", notes = "根据院士编码查询附件列表")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
    public List<Attachment> getAllList(@RequestParam("acadId") @ApiParam(value = "院士编码id",required = true) Integer acadId) throws Exception {
    	return this.attachmentService.getModelById(acadId);
    }

    /**
     * 根据附件id进行删除
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping
    @ApiOperation(value = "删除院士附件")
    @ApiResponses({@ApiResponse(code = 200,message = "删除成功")})
    public RE getModelById(@RequestParam("id") @ApiParam(value = "附件id",required = true) Long id) throws Exception {
        this.attachmentService.deleteModelById(id);
        return new RE().ok("删除成功");
    }
}