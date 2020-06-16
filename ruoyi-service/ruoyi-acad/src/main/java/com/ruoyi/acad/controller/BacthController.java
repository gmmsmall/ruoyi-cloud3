package com.ruoyi.acad.controller;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.base.Joiner;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.*;
import com.ruoyi.acad.form.BaseInfoForm;
import com.ruoyi.acad.form.PhotoForm;
import com.ruoyi.acad.service.*;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：创建原始基本信息控制层<br/>
 * CreateTime ：2020年3月11日上午10:09:59<br/>
 * CreateUser：ys<br/>
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/bacth")
@Api(tags = "ES索引库批量插入")
public class BacthController {

    @Autowired
    private IBaseInfoService baseInfoService;

    @Autowired
    private IAosService aosService;

    @Autowired
    private IAwardService awardService;

    @Autowired
    private IEducationService educationService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private INationalityService nationalityService;

    @Autowired
    private IPaperService paperService;

    @Autowired
    private IPatentService patentService;

    @Autowired
    private IPhoneService phoneService;

    @Autowired
    private IPhotoService photoService;

    @Autowired
    private ISnsService snsService;

    @Autowired
    private IWorkService workService;

    @Autowired
    private ElasticClientAcadRepository elasticClientAcadRepository;


    /**
     * Description:保存操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/saveModel")
    @ApiOperation(value = "ES索引库批量插入", notes = "ES索引库批量插入")
    public RE saveModel() throws Exception {

        List<BaseInfo> baseInfoList = baseInfoService.list();

        for (BaseInfo item:baseInfoList) {

            ClientAcad  clientAcad = new ClientAcad();
            Integer acadId = item.getAcadId();
            clientAcad.setAcadId(String.valueOf(acadId));
            clientAcad.setBaseInfo(item);
            //授衔履历
            List<Aos> aosList =  aosService.getModelById(acadId);
            clientAcad.setAosList(aosList);
            //荣誉
            List<Award> awardList = awardService.getModelById(acadId);
            clientAcad.setAwardList(awardList);
            //教育经历
            List<Education> educationList = educationService.getModelById(acadId);
            clientAcad.setEducationList(educationList);
            //邮箱
            List<Email> emailList = emailService.getModelById(acadId);
            clientAcad.setEmailList(emailList);
            //国籍
            List<Nationality> nationalityList = nationalityService.getModelById(acadId);
            clientAcad.setNationalityList(nationalityList);
            //论文
            List<Paper> paperList = paperService.getModelByAcadId(acadId);
            clientAcad.setPaperList(paperList);
            //专利
            List<Patent> patentList = patentService.getModelById(acadId);
            clientAcad.setPatentList(patentList);
            //电话
            List<Phone> phoneList = phoneService.getModelById(acadId);
            clientAcad.setPhoneList(phoneList);
            //照片
            List<Photo> photoList = photoService.getModelById(acadId);
            List<PhotoForm> photoFormList = new ArrayList<>();
            for (Photo photo:photoList) {
                PhotoForm photoForm = new PhotoForm();
                BeanUtil.copyProperties(photo,photoForm);
                photoFormList.add(photoForm);
            }
            clientAcad.setPhotoList(photoFormList);
            //社交联系方式
            List<Sns> snsList = snsService.getModelById(acadId);
            clientAcad.setSnsList(snsList);
            //工作履历
            List<Work> workList = workService.getModelById(acadId);
            clientAcad.setWorkList(workList);

            elasticClientAcadRepository.save(clientAcad);
        }
        RE re = new RE();
        re.setErrorCode(200);
        re.setStatus(true);
        re.setErrorDesc("批量插入成功");
        return re;
    }


}