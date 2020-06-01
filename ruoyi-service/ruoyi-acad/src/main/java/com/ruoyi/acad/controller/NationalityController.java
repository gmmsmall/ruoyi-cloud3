package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Nationality;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.INationalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author jxd
 */
@RestController
@RequestMapping("/nationality")
public class NationalityController {


    @Autowired
    private INationalityService nationalityService;

    /**
     * Description:保存国籍信息
     * CreateTime:2020年3月31日上午11:08:02
     * @param nationalityList
     * @return
     * @throws Exception
     */
    @PostMapping("/saveModel")
    public ResponseResult saveModel(@RequestBody List<Nationality> nationalityList, @NotBlank Integer acadId) throws Exception {

        nationalityService.saveModel(nationalityList, acadId);
        return new ResponseResult(true, 200, "保存成功");
    }

    /**
     * Description:修改国籍信息
     * CreateTime:2020年3月31日上午11:08:02
     * @return
     * @throws Exception
     */
    @PostMapping("/updateModel")
    public ResponseResult updateModel(@RequestBody List<Nationality> nationalityList, @NotBlank Integer acadId) throws Exception {

        nationalityService.updateModel(nationalityList, acadId);
        return new ResponseResult(true, 200, "修改成功");
    }

    /**
     * Description:根据院士ID获取国籍信息
     * CreateTime:2020年4月10日下午3:34:38
     * @param acadId
     * @return
     * @throws Exception
     */
    @GetMapping("/getModelById")
    public List<Nationality> getModelById(Integer acadId) throws Exception {

        List<Nationality> nationalityList = nationalityService.getModelById(acadId);
        return nationalityList;
    }
}
