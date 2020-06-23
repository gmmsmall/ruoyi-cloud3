package com.ruoyi.acad.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.BaseInfoMapper;
import com.ruoyi.acad.dao.OnlineResumeMapper;
import com.ruoyi.acad.domain.*;
import com.ruoyi.acad.form.PhotoForm;
import com.ruoyi.acad.service.IClientAcadService;
import com.ruoyi.acad.service.IOnlineResumeService;
import com.ruoyi.acad.utils.OnlinePdfUtils;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.enums.EducationType;
import com.ruoyi.common.enums.RsfCategoryType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fdfs.feign.RemoteFdfsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gmm
 */
@Service
@Slf4j
public class OnlineResumeServiceImpl extends ServiceImpl<OnlineResumeMapper, OnlineResume> implements IOnlineResumeService {

    //获取院士简历基础信息
    @Autowired
    private IClientAcadService clientAcadService;

    //文件上传下载
    @Autowired
    private RemoteFdfsService remoteFdfsService;

    @Autowired
    private BaseInfoMapper baseInfoMapper;


    //当前项目端口号
    @Value("${server.port}")
    private String port;

    /**
     * 根据院士编码列表查询简历列表
     * @param acadIdList
     * @return
     */
    @Override
    public List<OnlineResume> getAllByAcadIdList(List<String> acadIdList) {
        List<OnlineResume> list = this.baseMapper.selectList(new QueryWrapper<OnlineResume>()
                .eq("deleteflag",1)
                .in("acadecode",acadIdList));
        return list;
    }

    /**
     * 根据院士编码查看简历
     * @param acadecode
     * @return
     */
    @Override
    public OnlineResume getModelByAcadecode(String acadecode) {
        OnlineResume resume = this.baseMapper.selectOne(new QueryWrapper<OnlineResume>()
                .eq("deleteflag",1)
                .eq("acadecode",acadecode));
        return resume;
    }

    /**
     * 根据院士编码在线生成简历
     * @param acadId
     * @return
     */
    @Override
    public RE generateResume(Integer acadId) {
        RE re = new RE();
        re.setStatus(false);
        OnlinePdfUtils onlinePdfUtils = new OnlinePdfUtils();
        //acadId为院士编码，需要先判断库中该院士是否已生成简历(先删后增操作)
        try{
            //根据院士编码，查询院士的基础信息
            ClientAcad clientAcad = this.clientAcadService.getClientAcadByacadId(acadId);
            if(clientAcad != null && clientAcad.getBaseInfo() != null){
                List<OnlinePdfEntity> list = new ArrayList<>();
                OnlinePdfEntity entity = new OnlinePdfEntity();
                entity.setRemark("姓名 ：");
                String cnStrTemp = "";//中文
                String enStrTemp = "";//英文
                String yuanStrTemp = "";//原文
                if(clientAcad.getBaseInfo().getCnName() != null){
                    cnStrTemp = clientAcad.getBaseInfo().getCnName();
                }
                if(clientAcad.getBaseInfo().getEnName() != null){
                    enStrTemp = clientAcad.getBaseInfo().getEnName();
                }
                if(clientAcad.getBaseInfo().getRealName() != null){
                    yuanStrTemp = clientAcad.getBaseInfo().getRealName();
                }
                String Chinese = " 中文    "+cnStrTemp+"\n                  ";
                String enStr = "英文 "+enStrTemp;
                String yuanStr = "原文 "+yuanStrTemp;
                String regex = "(.{40})";
                String mac = enStr;
                String ymac = yuanStr;
                mac = mac.replaceAll(regex,"$1\n                             ");
                mac = mac.substring(0,mac.length() );
                ymac = ymac.replaceAll(regex,"$1\n                             ");
                ymac = ymac.substring(0,ymac.length() );
                entity.setInfo(Chinese+mac+"\n                  "+ymac);
                list.add(entity);
                OnlinePdfEntity entity111 = new OnlinePdfEntity();
                entity111.setRemark("年龄 ： ");
                //先显示出生日期，没有出生日期显示出生日期备注
                if(clientAcad.getBaseInfo().getBirthday() != null && StringUtils.isNotEmpty(String.valueOf(clientAcad.getBaseInfo().getBirthday()))){
                    entity111.setInfo(" "+ clientAcad.getBaseInfo().getBirthday());
                }else{
                    if(clientAcad.getBaseInfo().getBirthdayRemark() != null){
                        entity111.setInfo(" "+clientAcad.getBaseInfo().getBirthdayRemark());
                    }else{
                        entity111.setInfo(" ");
                    }
                }
                list.add(entity111);
                OnlinePdfEntity entitycon = new OnlinePdfEntity();
                entitycon.setRemark("国籍 ： ");
                if(clientAcad.getBaseInfo().getNationPlace() != null){
                    entitycon.setInfo(" "+clientAcad.getBaseInfo().getNationPlace());
                }else{
                    entitycon.setInfo(" ");
                }
                list.add(entitycon);
                OnlinePdfEntity entity1 = new OnlinePdfEntity();
                entity1.setRemark("授衔机构 ：");
                //授衔机构多个进行拼接
                String aosStr = "";
                String aosS = "";
                if(CollUtil.isNotEmpty(clientAcad.getAosList())){
                    for(int i = 0 ; i < clientAcad.getAosList().size(); i++){
                        if(i == 0){
                            aosStr = clientAcad.getAosList().get(i).getAosName();
                        }else{
                            aosStr = aosStr + "," + clientAcad.getAosList().get(i).getAosName();
                        }
                        aosS = aosS + "\n"+(i+1)+". "+clientAcad.getAosList().get(i).getAosName();
                    }
                }
                entity1.setInfo(" "+aosStr);
                list.add(entity1);
                OnlinePdfEntity entity11 = new OnlinePdfEntity();
                entity11.setRemark("研究领域 ：");
                String categoryStr = "";
                if(clientAcad.getBaseInfo().getRsfCategory() != null){
                    if(RsfCategoryType.getByCode(clientAcad.getBaseInfo().getRsfCategory()) != null){
                        categoryStr = RsfCategoryType.getByCode(clientAcad.getBaseInfo().getRsfCategory()).getMsg();
                    }
                }
                entity11.setInfo(" "+categoryStr);
                list.add(entity11);

                //简历分隔符下方显示的内容
                List<OnlinePdfEntity> list2 = new ArrayList<>();
                OnlinePdfEntity entityProfileHand = new OnlinePdfEntity();
                entityProfileHand.setRemark("研究领域 ： ");
                String categoryInfo = "";
                if(StringUtils.isNotEmpty(categoryStr)){
                    categoryInfo = ""+categoryStr;
                }
                if(StringUtils.isNotEmpty(clientAcad.getBaseInfo().getRsfProfile())){
                    categoryInfo += categoryInfo + "\n 介绍："+clientAcad.getBaseInfo().getRsfProfile();
                }
                if(StringUtils.isNotEmpty(clientAcad.getBaseInfo().getRsfInfluence())){
                    categoryInfo += categoryInfo + "\n 影响："+clientAcad.getBaseInfo().getRsfInfluence();
                }
                entityProfileHand.setInfo(categoryInfo);
                list2.add(entityProfileHand);
                OnlinePdfEntity entity2 = new OnlinePdfEntity();
                entity2.setRemark("简介 ： ");
                entity2.setInfo(clientAcad.getBaseInfo().getPersonalProfileHand());
                list2.add(entity2);
                OnlinePdfEntity entity3 = new OnlinePdfEntity();
                entity3.setRemark("教育 ： ");
                List<Education> eduList = clientAcad.getEducationList();
                String eduStr = "";
                if(eduList != null && eduList.size() > 0){
                    for(int i= 0; i < eduList.size();i++){
                        eduStr = enStr + "\n"+(i+1) + eduList.get(i).getSchool();
                        if(eduList.get(i).getEducation() != null){
                            eduStr = eduStr + "   学历： "+ EducationType.getByCode(eduList.get(i).getEducation()).getMsg();
                        }
                        if(eduList.get(i).getGraduationYear() != null){
                            eduStr = eduStr + "   毕业时间： "+ eduList.get(i).getGraduationYear();
                        }

                    }
                }
                entity3.setInfo(eduStr);
                list2.add(entity3);
                OnlinePdfEntity entitywork = new OnlinePdfEntity();
                entitywork.setRemark("工作 ： ");
                List<Work> workList = clientAcad.getWorkList();
                String workStr = "";
                if(workList != null && workList.size() > 0){
                    for(int i= 0; i < workList.size();i++){
                        workStr = workStr + "\n"+(i+1)+"、 "+workList.get(i).getWorkUnit();
                    }
                }
                entitywork.setInfo(workStr);
                list2.add(entitywork);
                OnlinePdfEntity entityaos = new OnlinePdfEntity();
                entityaos.setRemark("授衔 ： ");
                entityaos.setInfo(aosS);
                list2.add(entityaos);
                OnlinePdfEntity entityawards = new OnlinePdfEntity();
                entityawards.setRemark("荣誉 ： ");
                List<Award> awardsList = clientAcad.getAwardList();
                String awardsStr = "";
                if(awardsList != null && awardsList.size() > 0){
                    for(int i= 0; i < awardsList.size();i++){
                        awardsStr = awardsStr + "\n"+(i+1)+"、 "+awardsList.get(i).getAwardName() + "   "+awardsList.get(i).getAwardCategory()+ "  "+awardsList.get(i).getAwardYear();
                    }
                }
                entityawards.setInfo(awardsStr);
                list2.add(entityawards);
                OnlinePdfEntity entitypage = new OnlinePdfEntity();
                entitypage.setRemark("论文 ： ");
                List<Paper> pageList = clientAcad.getPaperList();
                String pageStr = "";
                if(pageList != null && pageList.size() > 0){
                    for(int i= 0; i < pageList.size();i++){
                        Paper  paper = pageList.get(i);
                        if(paper != null){
                            pageStr = pageStr + "\n"+(i+1)+"、 "+paper.getPaperTitle()+"  "+paper.getPublishedTime();
                        }
                    }
                }
                entitypage.setInfo(pageStr);
                list2.add(entitypage);
                OnlinePdfEntity entitypatent = new OnlinePdfEntity();
                entitypatent.setRemark("专利 ： ");
                List<Patent> patentList = clientAcad.getPatentList();
                String patentStr = "";
                if(patentList != null && patentList.size() > 0){
                    for(int i= 0; i < patentList.size();i++){
                        patentStr = patentStr + "\n"+(i+1)+"、 "+patentList.get(i).getPatentName() + "  "+patentList.get(i).getGetTime();
                    }
                }
                entitypatent.setInfo(patentStr);
                list2.add(entitypatent);
                OnlinePdfEntity entityelse = new OnlinePdfEntity();
                entityelse.setRemark("其他 ： ");
                //生活习惯 + 备注
                String otherStr = "";
                if(clientAcad.getBaseInfo().getLivingHabit() != null && clientAcad.getBaseInfo().getLivingHabit().length()>0){
                    otherStr = clientAcad.getBaseInfo().getLivingHabit();
                }
                if(clientAcad.getBaseInfo().getRemark() != null && clientAcad.getBaseInfo().getRemark().length() > 0){
                    otherStr = otherStr + "," + clientAcad.getBaseInfo().getRemark();
                }
                list2.add(entityelse);
                String photostr = "";
                //头像显示展厅的头像
                List<PhotoForm> photoList = clientAcad.getPhotoList();
                if(CollUtil.isNotEmpty(photoList)){
                    for(PhotoForm p : photoList){
                        if(p.getIsHall()){//展厅图片
                            photostr = p.getPhotoUrl();
                            break;
                        }
                    }
                }
                if(StringUtils.isNotEmpty(photostr)){
                    String[] photoUrl = photostr.split(",");
                    if(photoUrl != null && !photoUrl.equals("") && photoUrl.length > 1){
                        photostr = photoUrl[1].substring(photoUrl[1].indexOf("\"url\": \"")+8,photoUrl[1].length()-2);
                    }
                }
                //String path = ResourceUtils.getURL("classpath:").getPath();

                InetAddress inetAddress = InetAddress.getLocalHost();
                String pathStr = "http://"+inetAddress.getHostAddress()+ ":" +this.port;

                byte[] b = onlinePdfUtils.createpdf(list,photostr,list2,pathStr);
                //将 byte[]转MultipartFile:
                InputStream inputStream = new ByteArrayInputStream(b);
                MultipartFile multipartFile = new MockMultipartFile(acadId+".pdf",acadId+".pdf",ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);

                if(acadId != null){
                    LambdaQueryWrapper<OnlineResume> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(OnlineResume::getAcadecode,String.valueOf(acadId)).eq(OnlineResume::getDeleteflag,"1");
                    List<OnlineResume> resumeList = this.list(queryWrapper);
                    if(resumeList != null && resumeList.size() > 0){//简历已生成，需要先删后增(服务器上的简历不进行删除，以防以后需要寻找之前的生成记录)
                        for(OnlineResume onlineResume : resumeList){
                            onlineResume.setDeleteflag("2");
                            onlineResume.setDeletetime(LocalDateTime.now());
                            /*onlineResume.setDeleteperson(JWTUtil.getUser().getUserName());
                            onlineResume.setDeletepersonid(JWTUtil.getUser().getUserId());*/
                        }
                        this.updateBatchById(resumeList);
                    }
                    Map<String,Object> map = this.remoteFdfsService.upload(multipartFile);

                    String url = "";
                    if(map != null){
                        List<Map<String,Object>> results = (List<Map<String,Object>>)map.get("object");
                        if(CollUtil.isNotEmpty(results)){
                            url = String.valueOf(results.get(0).get("url"));
                        }
                    }

                    log.info("简历url:   "+url);
                    //每次新增时都需要根据院士编码，判断是否存在，若存在则需要先删后增（假删）
                    //OnlineResume------此为存放记录的实体类

                    OnlineResume resume = new OnlineResume();
                    /*resume.setAddpersonid(JWTUtil.getUser().getUserId());
                    resume.setAddperson(JWTUtil.getUser().getUserName());*/
                    resume.setAddtime(LocalDateTime.now());
                    resume.setAcadecode(String.valueOf(acadId));
                    //院士姓名显示顺序：真实姓名、中文名字、英文名字
                    if(org.apache.commons.lang.StringUtils.isNotBlank(clientAcad.getBaseInfo().getRealName())){
                        resume.setAcadename(clientAcad.getBaseInfo().getRealName());
                    }else if(org.apache.commons.lang.StringUtils.isNotBlank(clientAcad.getBaseInfo().getCnName())){
                        resume.setAcadename(clientAcad.getBaseInfo().getCnName());
                    }else if(org.apache.commons.lang.StringUtils.isNotBlank(clientAcad.getBaseInfo().getEnName())){
                        resume.setAcadename(clientAcad.getBaseInfo().getEnName());
                    }
                    resume.setDeleteflag("1");
                    resume.setResumeurl(url);
                    this.save(resume);
                    re.setErrorDesc("生成简历成功");
                    re.setErrorCode(200);
                    re.setStatus(true);
                }else{
                    re.setErrorDesc("生成的简历必须含有院士编码");
                    re.setErrorCode(500);
                }
            }
        }catch(Exception e){
            re.setErrorDesc("生成简历失败");
            re.setErrorCode(500);
            e.printStackTrace();
        }
        return re;
    }

    @Override
    public void initResume() {
        List<BaseInfo> list = this.baseInfoMapper.selectList(new QueryWrapper<BaseInfo>().orderByAsc("acad_id"));
        if(CollUtil.isNotEmpty(list)){
            for(BaseInfo info : list){
                if(info.getAcadId() != null && !String.valueOf(info.getAcadId()).equals("")){//院士编码不能为空
                    this.generateResume(info.getAcadId());
                }
            }
        }
    }

    public static InputStream byteByUrl(String urlOrPath) throws IOException {
        InputStream in = null;
        byte[] bytes;
        if (urlOrPath.toLowerCase().startsWith("https")) {
            //bytes = HttpsUtils.doGet(urlOrPath);
            bytes = null;
        } else if (urlOrPath.toLowerCase().startsWith("http")) {
            URL url = new URL(urlOrPath);
            return url.openStream();
        } else {
            File file = new File(urlOrPath);
            if (!file.isFile() || !file.exists() || !file.canRead()) {
                return null;
            }
            return new FileInputStream(file);
        }
        return new ByteArrayInputStream(bytes);
    }

}
