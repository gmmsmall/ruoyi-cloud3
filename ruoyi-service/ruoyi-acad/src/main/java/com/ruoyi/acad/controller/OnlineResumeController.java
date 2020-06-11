package com.ruoyi.acad.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.domain.*;
import com.ruoyi.acad.form.PhotoForm;
import com.ruoyi.acad.service.IClientAcadService;
import com.ruoyi.acad.service.IOnlineResumeService;
import com.ruoyi.acad.utils.CheckFileSize;
import com.ruoyi.acad.utils.FastDFSClient;
import com.ruoyi.acad.utils.OnlinePdfUtils;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.enums.EducationType;
import com.ruoyi.common.enums.RsfCategoryType;
import com.ruoyi.common.redis.util.JWTUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fdfs.feign.RemoteFdfsService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ProtoCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
 * 在线简历生成
 * @author gmm
 */

@Slf4j
@RestController
@RequestMapping("/onlineResume")
@Api(tags = "在线生成简历")
public class OnlineResumeController {

    private String message;

    @Value("${fdfs.http.secret_key}")
    private String fastdfsToken;
    @Value("${fdfs.web-server-url}")
    private String fastdfsUrl;

    //文件上传下载
    @Autowired
    private RemoteFdfsService remoteFdfsService;

    @Autowired
    private IOnlineResumeService resumeService;

    //获取院士简历基础信息
    @Autowired
    private IClientAcadService clientAcadService;

    /**
     * 根据院士编码在线生成简历 -- 新增或修改时就需要额外调用该接口
     * @param acadId
     * @return
     */
    @ApiOperation(value = "根据院士编码在线生成简历")
    @PostMapping("/add")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功")})
    public RE createonlineResume(@ApiParam(value = "院士id",required = true)@RequestParam Integer acadId){
        RE re = new RE();
        re.setStatus(false);
        OnlinePdfUtils onlinePdfUtils = new OnlinePdfUtils();
        //acadId为院士编码，需要先判断库中该院士是否已生成简历(先删后增操作)
        try{
            //根据院士编码，查询院士的基础信息
            ClientAcad clientAcad = this.clientAcadService.getClientAcadByacadId(acadId);
            if(clientAcad != null){
                List<OnlinePdfEntity> list = new ArrayList<>();
                OnlinePdfEntity entity = new OnlinePdfEntity();
                entity.setRemark("姓名 ：");
                String Chinese = " 中文    "+clientAcad.getBaseInfo().getCnName()+"\n                  ";
                String enStr = "英文 "+clientAcad.getBaseInfo().getEnName();
                String yuanStr = "原文 "+clientAcad.getBaseInfo().getRealName();
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
                    entity111.setInfo(" "+ DateUtil.format(clientAcad.getBaseInfo().getBirthday(),"yyyy-MM-dd"));
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
                    categoryInfo = "1."+categoryStr;
                }
                if(StringUtils.isNotEmpty(clientAcad.getBaseInfo().getRsfProfile())){
                    categoryInfo += categoryInfo + "\n 2.介绍："+clientAcad.getBaseInfo().getRsfProfile();
                }
                if(StringUtils.isNotEmpty(clientAcad.getBaseInfo().getRsfInfluence())){
                    categoryInfo += categoryInfo + "\n 2.影响："+clientAcad.getBaseInfo().getRsfInfluence();
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
                entityelse.setInfo(clientAcad.getBaseInfo().getLivingHabit()+clientAcad.getBaseInfo().getRemark());
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

                File file = onlinePdfUtils.createpdf(list,photostr,list2,"D:/miaomiao.pdf");
                InputStream inputStream = new FileInputStream(file);
                MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
                Boolean ifsize = CheckFileSize.check(multipartFile,10240,"M");
                if(ifsize){
                    if(acadId != null){
                        LambdaQueryWrapper<OnlineResume> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(OnlineResume::getAcadecode,String.valueOf(acadId)).eq(OnlineResume::getDeleteflag,"1");
                        List<OnlineResume> resumeList = resumeService.list(queryWrapper);
                        if(resumeList != null && resumeList.size() > 0){//简历已生成，需要先删后增(服务器上的简历不进行删除，以防以后需要寻找之前的生成记录)
                            for(OnlineResume onlineResume : resumeList){
                                onlineResume.setDeleteflag("2");
                                onlineResume.setDeletetime(LocalDateTime.now());
                                onlineResume.setDeleteperson(JWTUtil.getUser().getUserName());
                                onlineResume.setDeletepersonid(JWTUtil.getUser().getUserId());
                            }
                            this.resumeService.updateBatchById(resumeList);
                        }
                        String url = String.valueOf(this.remoteFdfsService.uploadFile(file).getObject());
                        System.out.println("此时url:   "+url);
                        //每次新增时都需要根据院士编码，判断是否存在，若存在则需要先删后增（假删）
                        //OnlineResume------此为存放记录的实体类

                        OnlineResume resume = new OnlineResume();
                        resume.setAddpersonid(JWTUtil.getUser().getUserId());
                        resume.setAddperson(JWTUtil.getUser().getUserName());
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
                        this.resumeService.save(resume);
                        re.setErrorDesc("生成简历成功");
                        re.setErrorCode(200);
                        re.setStatus(true);
                    }else{
                        re.setErrorDesc("生成的简历必须含有院士编码");
                        re.setErrorCode(500);
                    }
                }else {
                    re.setErrorDesc("允许上传最大为10G！");
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

    protected byte[] getResumeByte(String photourl) {
        byte[] data = null;
        try {
            URL url = new URL(photourl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //默认是get请求   如果想使用post必须指明
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);

            int code = connection.getResponseCode();
            if(code == 200){
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100];
                int rc = 0;
                while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                    byteArrayOutputStream.write(buff, 0, rc);
                }
                data = byteArrayOutputStream.toByteArray();
                inputStream.close();
                byteArrayOutputStream.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 简历的下载（单次或批量）
     * @param msg
     * @param response
     */
    @PostMapping("/downloadlist")
    public void downloadResumelist(@RequestBody String msg, HttpServletResponse response) {

        /*
         * 请求的数据格式：以逗号分隔的院士编码字符串
         * {
         *     "data":"1,10000002"
         * }
        */

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String zipname =
simpleDateFormat.format(new Date())+
"resume.zip";
        OutputStream outputStream = null;
        ZipOutputStream zos = null;
        try{
            response.setContentType("multipart/form-data");
            //response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(zipname, "UTF-8"));

            outputStream = response.getOutputStream();
            zos = new ZipOutputStream(outputStream);
            JSONObject json = JSONObject.parseObject(msg);
            if(json.get("data") != null && !json.get("data").equals("")){
                List<String> list = Arrays.asList(json.getString("data").split(","));
                if(list != null && list.size() > 0){
                    LambdaQueryWrapper<OnlineResume> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(OnlineResume::getDeleteflag,"1").in(OnlineResume::getAcadecode,list);
                    List<OnlineResume> resumeList = resumeService.list(queryWrapper);
                    if(resumeList != null && resumeList.size() > 0){
                        this.downloadTolocal(zos,resumeList);
                    }
                }

            }
            //fdfsClient.download();
            message = "下载成功";
        }catch(Exception e){
            message = "下载批量简历失败";
            log.error(message, e);
        }finally {
            if(zos != null) {
                try {
                    zos.close();
                } catch (Exception e2) {
                    log.info("关闭输入流时出现错误",e2);
                }
            }
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e2) {
                    log.info("关闭输入流时出现错误",e2);
                }
            }

        }
       // return new ResponseResult(flag,200,message,map);
    }

    private void downloadTolocal(ZipOutputStream zos,List<OnlineResume> resumeList) throws IOException {

        for(OnlineResume resume : resumeList){
            if(resume.getResumeurl() != null && !resume.getResumeurl().equals("")){
                ZipEntry entry = new ZipEntry(resume.getAcadename()+"-"+resume.getAcadecode()+".pdf");
                InputStream is = null;
                BufferedInputStream in = null;
                try{
                    URL url = new URL("http://"+getUrlWithToken(resume.getResumeurl()));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //默认是get请求   如果想使用post必须指明
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    int code = connection.getResponseCode();
                    if(code == 200){
                        is = connection.getInputStream();
                        in = new BufferedInputStream(is);
                        zos.putNextEntry(entry);
                        byte[] buffer = new byte[1024];
                        int len;
                        //文件流循环写入ZipOutputStream
                        while ((len = in.read(buffer)) != -1 ) {
                            zos.write(buffer, 0, len);
                        }
                    }
                }catch (Exception e) {
                    log.info("xxx--下载全部附件--压缩文件出错",e);
                }finally {
                    if(entry != null) {
                        try {
                            zos.closeEntry();
                        } catch (Exception e2) {
                            log.info("xxx下载全部附件--zip实体关闭失败",e2);
                        }
                    }
                    if(in != null) {
                        try {
                            in.close();
                        } catch (Exception e2) {
                            log.info("xxx下载全部附件--文件输入流关闭失败",e2);
                        }
                    }
                    if(is != null) {
                        try {
                            is.close();
                        }catch (Exception e) {
                            log.info("xxx下载全部附件--输入缓冲流关闭失败",e);
                        }
                    }


                }
            }
        }
    }


    /*
     * 获取带防盗链的连接
     * @param fid
     * @return
    */

    public String getUrlWithToken(String fid) {
        String secret_key = fastdfsToken;
        String IP = fastdfsUrl;
        String substring = fid.substring(fid.indexOf("/M00") + 1);
        //unix时间戳 以秒为单位
        int ts = (int) (System.currentTimeMillis() / 1000);
        String token = "";
        try {
            token = ProtoCommon.getToken(substring, ts, secret_key);
        } catch (UnsupportedEncodingException | MyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String result = IP + "/group1/" + substring + "?token=" + token + "&ts=" + ts;
        return result;
    }

    @GetMapping("/pdfurl")
    public String getRealUrl(String url){
        return getUrlWithToken(url);
    }


    /*
     * 文件下载（单个）
     *
     * @param msg
     * @param response
     * @throws Exception
    */
    @PostMapping("/download")
   // @RequestMapping(value = "/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public void download(@RequestBody String msg, HttpServletResponse response) throws Exception {
        /*
         * 请求参数
         * {
         *     "urlstr":"www.mingbyte.com:1234/group1/M00/00/00/wKgIgV6UFyKAHJ1oAAAyj5cVZHw324.pdf",
         *     "urlname":"简历院士1a"
         * }

            */

        this.remoteFdfsService.download(msg);
        /*JSONObject json = JSONObject.parseObject(msg);
        byte[] data = this.remoteFdfsService.download(json.getString("urlstr"));

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" +  URLEncoder.encode(json.getString("urlname")+".pdf", "UTF-8"));

        // 写出
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.write(data, outputStream);*/
    }


}
