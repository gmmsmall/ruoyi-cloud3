package com.ruoyi.acad.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.acad.domain.*;
import com.ruoyi.acad.form.BaseInfoAcadIdForm;
import com.ruoyi.acad.service.IOnlineResumeService;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.fdfs.feign.RemoteFdfsService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
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

    //文件上传下载
    @Autowired
    private RemoteFdfsService remoteFdfsService;

    @Autowired
    private IOnlineResumeService resumeService;

    /**
     * Description:根据院士编码列表批量下载简历
     * CreateTime:2020年6月12日上午09:36:00
     * @return
     * @throws Exception
     */
    @PostMapping("/downloadResume")
    @ApiOperation(value = "根据院士编码列表批量下载简历")
    @ApiResponses({@ApiResponse(code = 200,message = "下载成功")})
    public List<OnlineResume> downloadResume(@Valid @RequestBody@ApiParam(value = "院士编码列表",required = true) List<BaseInfoAcadIdForm> acadIdFormList){
        List<String> acadecodeList = acadIdFormList.stream().map(BaseInfoAcadIdForm::getAcadId).collect(Collectors.toList());
        return this.resumeService.getAllByAcadIdList(acadecodeList);//将要下载的院士简历列表
    }

    /**
     * Description:根据院士编码在线查看简历
     * CreateTime:2020年6月12日下午15:22:00
     * @return
     * @throws Exception
     */
    @PostMapping("/getResumeUrl")
    @ApiOperation(value = "根据院士编码在线查看简历")
    @ApiResponses({@ApiResponse(code = 200,message = "获取成功")})
    public OnlineResume getResumeUrl(@ApiParam(value = "院士id",required = true)@RequestParam Integer acadId){
        return this.resumeService.getModelByAcadecode(String.valueOf(acadId));
    }

    /**
     * 根据院士编码在线生成简历 -- 新增或修改时就需要额外调用该接口
     * @param acadId
     * @return
     */
    @ApiOperation(value = "根据院士编码在线生成简历")
    @PostMapping("/add")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功")})
    public RE createonlineResume(@ApiParam(value = "院士id",required = true)@RequestParam Integer acadId){
        return this.resumeService.generateResume(acadId);
    }

    @GetMapping("/initResume")
    @ApiOperation(value = "初始化院士简历", notes = "初始化院士简历")
    @ApiResponses({@ApiResponse(code = 200, message = "初始化简历成功")})
    public RE initResume(){
        this.resumeService.initResume();
        return new RE().ok("初始化简历成功");
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
                    //URL url = new URL("http://"+getUrlWithToken(resume.getResumeurl()));
                    URL url = new URL(resume.getResumeurl() );
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

    /*public String getUrlWithToken(String fid) {
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
    }*/

    @GetMapping("/pdfurl")
//    public String getRealUrl(String url){
//        return getUrlWithToken(url);
//    }


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
