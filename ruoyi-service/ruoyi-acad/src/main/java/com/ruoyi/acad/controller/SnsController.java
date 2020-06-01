package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.domain.Sns;
import com.ruoyi.acad.service.ISnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author jxd
 */
@RestController
@RequestMapping("/sns")
public class SnsController {

    @Autowired
    private ISnsService snsService;

    /**
     * Description:查询操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/getModel")
    public Sns getModelById(@NotBlank(message = "{required}") Integer id) throws Exception {

        Sns sns = snsService.getModelById(id);
        return sns;
    }

    /**
     * Description:更新操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/updateModel")
    public ResponseResult updateModel(@RequestBody List<Sns> snsList,Integer acadId) throws Exception {

        snsService.updateModel(snsList,acadId);
        return new ResponseResult(true, 200, "修改成功");
    }

    /**
     * Description:保存操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/saveModel")
    public ResponseResult saveModel(@RequestBody List<Sns> snsList,Integer acadId) throws Exception {

        snsService.saveModel(snsList,acadId);
        return new ResponseResult(true, 200, "保存成功");
    }

}
