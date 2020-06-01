package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Education;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IEducationService;
import com.ruoyi.common.core.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * Description：教育信息控制层<br/>
 * CreateTime ：2020年3月18日下午2:04:58<br/>
 * CreateUser：ys<br/>
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/education")
public class EducationController extends BaseController {

    @Autowired
    private IEducationService educationService;


    /**
     * Description:根据院士ID获取对应信息
     * CreateTime:2020年3月18日下午2:06:15
     *
     * @param acadId
     * @return
     * @throws Exception
     */
    @GetMapping("/getModelById")
    public List<Education> getModelById(Integer acadId) throws Exception {

        List<Education> list = educationService.getModelById(acadId);

        return list;
    }

    /**
     * Description:修改院士教育信息
     * CreateTime:2020年3月19日下午5:32:45
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/updateModel")
    public ResponseResult updateModel(@RequestBody List<Education> list,@NotBlank Integer acadId) throws Exception {

        educationService.updateModel(list,acadId);

        return new ResponseResult(true, 200, "成功");
    }

    /**
     * Description:保存院士教育信息
     * CreateTime:2020年3月19日下午5:32:45
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/saveModel")
    public ResponseResult saveModel(@RequestBody List<Education> list,@NotBlank Integer acadId) throws Exception {

        educationService.saveModel(list,acadId);

        return new ResponseResult(true, 200, "成功");
    }
}