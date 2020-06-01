package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Photo;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IPhotoService;
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
@RequestMapping("/photo")
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
    @PostMapping("/getModel")
    public List<Photo> getModelById(@NotBlank(message = "{required}") Integer id) throws Exception {

        List<Photo> photoList = photoService.getModelById(id);
        return photoList;
    }


    /**
     * Description:保存操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/saveModel")
    public ResponseResult saveModel(@RequestBody Photo photo) throws Exception {

        photoService.saveModel(photo);
        return new ResponseResult(true, 200, "保存成功");
    }

    /**
     * Description:删除操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteModel")
    public ResponseResult deleteModel(@RequestBody long photoId) throws Exception {

        photoService.deleteModel(photoId);
        return new ResponseResult(true, 200, "删除成功");
    }

}
