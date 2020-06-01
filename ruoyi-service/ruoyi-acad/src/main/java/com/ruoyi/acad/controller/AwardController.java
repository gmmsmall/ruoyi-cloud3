package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Award;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IAwardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Description：院士获奖表控制层<br/>
 * CreateTime ：2020年3月20日下午2:45:15<br/>
 * CreateUser：ys<br/>
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/award")
public class AwardController{

    @Autowired
    private IAwardService awardService;

    /**
     * Description:根据ID获取院士获奖信息列表
     * CreateTime:2020年3月20日下午2:49:47
     *
     * @param acadId
     * @return
     * @throws Exception
     */
    @GetMapping("/getModelById")
    public List<Award> getModelById(@NotBlank Integer acadId) throws Exception {

        List<Award> awardList = awardService.getModelById(acadId);
        return awardList;
    }

    /**
     * Description:修改荣誉信息列表
     * CreateTime:2020年3月20日下午3:57:16
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/updateModel")
    public ResponseResult updateModel(@RequestBody List<Award> list,@NotBlank Integer acadId) throws Exception {

        awardService.updateModel(list,acadId);
        return new ResponseResult(true, 200, "修改成功");
    }

    /**
     * Description:新增荣誉信息列表
     * CreateTime:2020年3月20日下午3:57:16
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/saveModel")
    public ResponseResult saveModel(@RequestBody List<Award> list,@NotBlank Integer acadId) throws Exception {

        awardService.saveModel(list,acadId);
        return new ResponseResult(true, 200, "新增成功");
    }
}
