package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Phone;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IPhoneService;
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
@RequestMapping("/phone")
public class PhoneController {

    @Autowired
    private IPhoneService phoneService;

    /**
     * Description:查询操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/getModel")
    public List<Phone> getModelById(@NotBlank(message = "{required}") Integer id) throws Exception {

        List<Phone> phoneList = phoneService.getModelById(id);
        return phoneList;
    }

    /**
     * Description:更新操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/updateModel")
    public ResponseResult updateModel(@RequestBody List<Phone> phoneList,@NotBlank Integer acadId) throws Exception {

        phoneService.updateModel(phoneList,acadId);
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
    public ResponseResult saveModel(@RequestBody List<Phone> phoneList,@NotBlank Integer acadId) throws Exception {

        phoneService.saveModel(phoneList,acadId);
        return new ResponseResult(true, 200, "保存成功");
    }

}
