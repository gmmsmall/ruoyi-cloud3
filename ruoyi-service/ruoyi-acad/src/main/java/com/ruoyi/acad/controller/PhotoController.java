package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Photo;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IPhotoService;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author jxd
 */
@RestController
@RequestMapping("/photo")
@Api(tags = "院士照片信息表")
public class PhotoController {

    @Autowired
    private IPhotoService photoService;

    /**
     * Description:查询操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getModel")
    @ApiOperation(value = "根据院士编码查询院士照片信息")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
    public List<Photo> getModelById(@ApiParam(value = "院士id",required = true)@RequestParam Integer id) throws Exception {
        return this.photoService.getModelById(id);
    }


    /**
     * Description:保存操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/saveModel")
    @ApiOperation(value = "保存院士照片信息")
    @ApiResponses({@ApiResponse(code = 200,message = "保存成功")})
    @OperLog(title = "保存院士照片信息", businessType = BusinessType.INSERT)
    public RE saveModel(@Valid @RequestBody@ApiParam(value = "院士照片列表") List<Photo> list,
                        @ApiParam(value = "院士编码",required = true)@RequestParam Integer acadId) throws Exception {
        this.photoService.saveModelList(list,acadId);
        return new RE().ok("保存成功");
    }

    /**
     * Description:删除操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    /*@PostMapping("/deleteModel")
    @ApiOperation(value = "删除院士照片信息")
    @ApiResponses({@ApiResponse(code = 200,message = "删除成功")})
    @OperLog(title = "删除院士照片信息", businessType = BusinessType.DELETE)
    public RE deleteModel(@ApiParam(value = "照片id",required = true)@RequestParam Long photoId,
                          @ApiParam(value = "院士编码",required = true)@RequestParam Integer acadId) throws Exception {
        this.photoService.deleteModel(photoId);
        return new RE().ok("删除成功");
    }*/

    /**
     * Description:查询操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/initGender")
    @ApiOperation(value = "初始化照片性别")
    @ApiResponses({@ApiResponse(code = 200,message = "初始化成功")})
    public RE initGender() {
        this.photoService.initGender();
        return new RE().ok("初始化成功");
    }

}
