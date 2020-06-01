package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Name;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.domain.Sns;
import com.ruoyi.acad.service.INameService;
import com.ruoyi.acad.service.ISnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author jxd
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @Autowired
    private INameService nameService;

    /**
     * Description:查询操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/getModel")
    public Name getModelById(@NotBlank(message = "{required}") Integer id) throws Exception {

        Name name = nameService.getModelById(id);
        return name;
    }

    /**
     * Description:更新操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/updateModel")
    public ResponseResult updateModel(@RequestBody Name name) throws Exception {

        nameService.updateModel(name);
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
    public ResponseResult saveModel(@RequestBody Name name) throws Exception {

        nameService.saveModel(name);
        return new ResponseResult(true, 200, "保存成功");
    }

}
