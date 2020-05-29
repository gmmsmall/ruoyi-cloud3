/*
package com.ruoyi.javamail.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.javamail.domain.ResponseResult;
import com.ruoyi.javamail.entity.OnlinePdfEntity;
import com.ruoyi.javamail.entity.OnlineResume;
import com.ruoyi.javamail.exception.FebsException;
import com.ruoyi.javamail.service.IOnlineResumeService;
import com.ruoyi.javamail.util.CheckFileSize;
import com.ruoyi.javamail.util.FastDFSClient;
import com.ruoyi.javamail.util.FebsUtil;
import com.ruoyi.javamail.util.OnlinePdfUtils;
import io.swagger.annotations.Api;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
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

*/
/**
 * @author gmm
 *//*

@Slf4j
@RestController
@RequestMapping("/javamail/onlineResume")
@Api(tags = "OnlineResumeController", description = "OnlineResumeController | 在线生成简历")
public class OnlineResumeController {

    private String message;

    private boolean flag = true;

    @Value("${fdfs.http.secret_key}")
    private String fastdfsToken;
    @Value("${fdfs.web-server-url}")
    private String fastdfsUrl;

    @Autowired
    private FastDFSClient fdfsClient;

    @Autowired
    private IOnlineResumeService resumeService;

    @Autowired
    private IBaseInfoService baseInfoService;

    @Log("新增或修改院士信息时生成简历，已存在的院士用定时任务进行生成")
    @PostMapping("/add")
    //@ApiOperation(value="简历生成", notes="请求参数：院士编码，院士姓名等基础信息")
    public ResponseResult createonlineResume(*/
/*@ApiJsonObject(name = "resumemsg", value = {
            @ApiJsonProperty(key = "acadecode",description = "院士编码"),
            @ApiJsonProperty(key = "acadename",description = "院士姓名")
    })*//*
 @RequestBody String resumemsg) throws FebsException {
        Map<String,Object> map = new HashMap<>();
        JSONObject json = JSONObject.parseObject(resumemsg);
        OnlinePdfUtils onlinePdfUtils = new OnlinePdfUtils();
        //假设id为院士编码，需要先判断库中该院士是否已生成简历
        try{
            //根据院士编码，查询院士的基础信息
            ClientBaseInfo baseInfo = baseInfoService.getBaseInfoById(Integer.valueOf(json.getString("acadecode")));
            Map<String,Object> result = baseInfoService.transFormClientBaseInfo(baseInfo,"4");
            List<OnlinePdfEntity> list = new ArrayList<>();
            OnlinePdfEntity entity = new OnlinePdfEntity();
            entity.setRemark("姓名 ：");
            String Chinese = " 中文    "+result.get("cnName")+"\n                  ";
            String enStr = "英文 "+result.get("enName");
            String yuanStr = "原文 "+result.get("realName");
            String regex = "(.{40})";
            String mac = enStr;
            String ymac = yuanStr;
            mac = mac.replaceAll(regex,"$1\n                             ");
            mac = mac.substring(0,mac.length() );
            ymac = ymac.replaceAll(regex,"$1\n                             ");
            ymac = ymac.substring(0,ymac.length() );
            entity.setInfo(Chinese+mac+"\n                  "+ymac);
            list.add(entity);
            OnlinePdfEntity entity112 = new OnlinePdfEntity();
            entity112.setRemark("性别 ：");
            entity112.setInfo(" "+String.valueOf(result.get("gender")));
            list.add(entity112);
            OnlinePdfEntity entity111 = new OnlinePdfEntity();
            entity111.setRemark("出生日期 ： ");
            entity111.setInfo(" "+String.valueOf(result.get("birthday")));
            list.add(entity111);
            OnlinePdfEntity entitycon = new OnlinePdfEntity();
            entitycon.setRemark("籍贯 ： ");
            entitycon.setInfo(" "+String.valueOf(result.get("nativePlace")));
            list.add(entitycon);
            OnlinePdfEntity entity1 = new OnlinePdfEntity();
            entity1.setRemark("授衔机构 ：");
            entity1.setInfo(" "+String.valueOf(result.get("positive")));
            list.add(entity1);
            OnlinePdfEntity entity1s = new OnlinePdfEntity();
            entity1s.setRemark("授衔研究领域分类 ：");
            entity1s.setInfo(" "+String.valueOf(result.get("rsfCategory")));
            list.add(entity1s);
            OnlinePdfEntity entity1sd = new OnlinePdfEntity();
            entity1sd.setRemark("联系状态 ：");
            String contactStatuseStr = "";
            if(result.get("contactStatus") != null && !result.get("contactStatus").equals("")){
                switch(String.valueOf(result.get("contactStatus"))){
                    case "1":
                        contactStatuseStr = " 已通讯";
                        break;
                    case "2":
                        contactStatuseStr = " 已到访";
                        break;
                    case "3":
                        contactStatuseStr = " 已签约";
                        break;
                    case "4":
                        contactStatuseStr = " 未通讯";
                        break;
                }
            }
            entity1sd.setInfo(contactStatuseStr);
            list.add(entity1sd);
            OnlinePdfEntity entity1ss = new OnlinePdfEntity();
            entity1ss.setRemark("签约类型 ：");
            String signTypeStr = "";
            if(result.get("signType") != null && !result.get("signType").equals("")){
                switch(String.valueOf(result.get("signType"))){
                    case "1":
                        signTypeStr = " 全职";
                        break;
                    case "2":
                        signTypeStr = " 刚性";
                        break;
                    case "3":
                        signTypeStr = " 柔性";
                        break;
                    case "4":
                        signTypeStr = " 注册";
                        break;
                    case "5":
                        signTypeStr = " 其他";
                        break;
                }
            }
            entity1ss.setInfo(signTypeStr);
            list.add(entity1ss);
            OnlinePdfEntity entity1sq = new OnlinePdfEntity();
            entity1sq.setRemark("宗教信仰 ：");
            entity1sq.setInfo(" "+String.valueOf(result.get("religion")));
            list.add(entity1sq);
            */
/*OnlinePdfEntity entity11 = new OnlinePdfEntity();
            entity11.setRemark("研究领域 ：");
            entity11.setInfo(" 上下五千年文明");
            list.add(entity11);*//*




            List<OnlinePdfEntity> list2 = new ArrayList<>();
            OnlinePdfEntity entityProfileHand = new OnlinePdfEntity();
            entityProfileHand.setRemark("研究领域 ： ");
            entityProfileHand.setInfo(String.valueOf(result.get("personalProfileHand")));
            list2.add(entityProfileHand);
            OnlinePdfEntity entity2 = new OnlinePdfEntity();
            entity2.setRemark("简介 ： ");
            entity2.setInfo(String.valueOf(result.get("personalProfileHand")));
            list2.add(entity2);
            OnlinePdfEntity entity3 = new OnlinePdfEntity();
            entity3.setRemark("教育 ： ");
            List<String> eduList = (List<String>)result.get("education");
            if(eduList != null && eduList.size() > 0){
                String eduStr = "";
                for(int i= 0; i < eduList.size();i++){
                    eduStr = eduStr + "\n"+(i+1)+"、 "+eduList.get(i);
                }
                entity3.setInfo(eduStr);
            }else{
                entity3.setInfo("");
            }
            list2.add(entity3);
            OnlinePdfEntity entitywork = new OnlinePdfEntity();
            entitywork.setRemark("工作 ： ");
            List<String> workList = (List<String>)result.get("work");
            if(workList != null && workList.size() > 0){
                String workStr = "";
                for(int i= 0; i < workList.size();i++){
                    workStr = workStr + "\n"+(i+1)+"、 "+workList.get(i);
                }
                entitywork.setInfo(workStr);
            }else{
                entitywork.setInfo("");
            }
            list2.add(entitywork);
            OnlinePdfEntity entityaos = new OnlinePdfEntity();
            entityaos.setRemark("授衔 ： ");
            List<String> aosList = (List<String>)result.get("aosList");
            if(aosList != null && aosList.size() > 0){
                String aosStr = "";
                for(int i= 0; i < aosList.size();i++){
                    aosStr = aosStr + "\n"+(i+1)+"、 "+aosList.get(i);
                }
                entityaos.setInfo(aosStr);
            }else{
                entityaos.setInfo("");
            }
            list2.add(entityaos);
            OnlinePdfEntity entityawards = new OnlinePdfEntity();
            entityawards.setRemark("荣誉 ： ");
            List<String> awardsList = (List<String>)result.get("awards");
            if(awardsList != null && awardsList.size() > 0){
                String awardsStr = "";
                for(int i= 0; i < awardsList.size();i++){
                    awardsStr = awardsStr + "\n"+(i+1)+"、 "+awardsList.get(i);
                }
                entityawards.setInfo(awardsStr);
            }else{
                entityawards.setInfo("");
            }
            list2.add(entityawards);
            OnlinePdfEntity entitypage = new OnlinePdfEntity();
            entitypage.setRemark("论文 ： ");
            List<Paper> pageList = (List<Paper>)result.get("paperList");
            if(pageList != null && pageList.size() > 0){
                String pageStr = "";
                for(int i= 0; i < pageList.size();i++){
                    Paper  paper = pageList.get(i);
                    if(paper != null){
                        pageStr = pageStr + "\n"+(i+1)+"、 "+paper.getPaperTitle()+"  "+paper.getPublishedTime();
                    }
                }
                entitypage.setInfo(pageStr);
            }else{
                entitypage.setInfo("");
            }
            list2.add(entitypage);
            OnlinePdfEntity entitypatent = new OnlinePdfEntity();
            entitypatent.setRemark("专利 ： ");
            List<String> patentList = (List<String>)result.get("patent");
            if(patentList != null && patentList.size() > 0){
                String patentStr = "";
                for(int i= 0; i < patentList.size();i++){
                    patentStr = patentStr + "\n"+(i+1)+"、 "+patentList.get(i);
                }
                entitypatent.setInfo(patentStr);
            }else{
                entitypatent.setInfo("");
            }
            list2.add(entitypatent);
            OnlinePdfEntity entityelse = new OnlinePdfEntity();
            entityelse.setRemark("其他 ： ");
            String ivingHabit = String.valueOf(result.get("ivingHabit"));
            String remark = String.valueOf(result.get("remark"));
            String elseStr = "";
            if(ivingHabit != null && !ivingHabit.equals("")){
                elseStr = "\n1、 "+ivingHabit;
                if(remark != null && !remark.equals("")){
                    elseStr += "\n2、 "+remark;
                }
            }else if(remark != null && !remark.equals("")){
                elseStr = "\n1、 "+remark;
            }
            entityelse.setInfo(elseStr);
            list2.add(entityelse);
            String photostr = "";
            if(result.get("photoUrl") != null && !result.get("photoUrl").equals("")){
                String[] photoUrl = String.valueOf(result.get("photoUrl")).split(",");
                if(photoUrl != null && !photoUrl.equals("") && photoUrl.length > 1){
                    photostr = photoUrl[1].substring(photoUrl[1].indexOf("\"url\": \"")+8,photoUrl[1].length()-2);
                }
            }

            File file = onlinePdfUtils.createpdf(list,photostr,list2,"D:/miaomiao.pdf");
            InputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
            Boolean ifsize = CheckFileSize.check(multipartFile,10240,"M");
            if(ifsize){
                if(json.getString("acadecode") != null && !json.getString("acadecode").equals("")){
                    LambdaQueryWrapper<OnlineResume> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(OnlineResume::getAcadecode,json.getString("acadecode")).eq(OnlineResume::getDeleteflag,"1");
                    List<OnlineResume> resumeList = resumeService.list(queryWrapper);
                    if(resumeList != null && resumeList.size() > 0){//简历已生成，需要先删后增(服务器上的简历不进行删除，以防以后需要寻找之前的生成记录)
                        for(OnlineResume onlineResume : resumeList){
                            onlineResume.setDeleteflag("2");
                            onlineResume.setDeletetime(LocalDateTime.now());
                            onlineResume.setDeleteperson(FebsUtil.getCurrentUser().getUsername());
                            onlineResume.setDeletepersonid(FebsUtil.getCurrentUser().getUserId());
                        }
                        resumeService.updateBatchById(resumeList);
                    }
                    String url = fdfsClient.uploadFile(file);
                    System.out.println("此时url:   "+url);
                    //每次新增时都需要根据院士编码，判断是否存在，若存在则需要先删后增（假删）
                    //OnlineResume------此为存放记录的实体类

                    OnlineResume resume = new OnlineResume();
                    resume.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
                    resume.setAddperson(FebsUtil.getCurrentUser().getUsername());
                    resume.setAddtime(LocalDateTime.now());
                    resume.setAcadecode(json.getString("acadecode"));
                    resume.setAcadename(json.getString("acadename"));
                    resume.setDeleteflag("1");
                    resume.setResumeurl(url);
                    resumeService.save(resume);
                }else{
                    map.put("code", 500);
                    map.put("msg", "生成的简历必须含有院士编码");
                }
            }else {
                map.put("code", 500);
                map.put("msg", "允许上传最大为10G！");
            }
            message = "生成简历成功";
        }catch(Exception e){
            flag = false;
            message = "生成简历失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,map);
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

    @Log("简历的下载（单次或批量）")
    @PostMapping("/downloadlist")
    public void downloadResumelist(@RequestBody String msg, HttpServletResponse response) throws FebsException{
        */
/**
         * 请求的数据格式：以逗号分隔的院士编码字符串
         * {
         *     "data":"1,10000002"
         * }
         *//*

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String zipname = */
/*simpleDateFormat.format(new Date())+*//*
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
            flag = false;
            message = "下载批量简历失败";
            log.error(message, e);
            throw new FebsException(message);
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


    */
/**
     * 获取带防盗链的连接
     * @param fid
     * @return
     *//*

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


    */
/**
     * 文件下载（单个）
     *
     * @param msg
     * @param response
     * @throws Exception
     *//*

    @PostMapping("/download")
   // @RequestMapping(value = "/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public void download(@RequestBody String msg, HttpServletResponse response) throws Exception {
        */
/**
         * 请求参数
         * {
         *     "urlstr":"www.mingbyte.com:1234/group1/M00/00/00/wKgIgV6UFyKAHJ1oAAAyj5cVZHw324.pdf",
         *     "urlname":"简历院士1a"
         * }
         *//*


        JSONObject json = JSONObject.parseObject(msg);
        byte[] data = fdfsClient.download(json.getString("urlstr"));

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" +  URLEncoder.encode(json.getString("urlname")+".pdf", "UTF-8"));

        // 写出
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.write(data, outputStream);
    }


}
*/
