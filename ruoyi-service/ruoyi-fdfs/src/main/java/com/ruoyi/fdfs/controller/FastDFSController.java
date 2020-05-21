package com.ruoyi.fdfs.controller;

import com.ruoyi.fdfs.utils.CheckFileSize;
import com.ruoyi.fdfs.client.InfoReview;
import com.ruoyi.fdfs.client.FastDFSClient;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jxd
 * @Title: FastDFSController
 * @ProjectName
 * @Description:
 * @date 2019/6/17 15:14
 */
@RestController
@RequestMapping("fdfs")
public class FastDFSController {

    @Autowired
    private FastDFSClient fdfsClient;

    /**
     * 文件上传
     *
     * @param files
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "上传文件", notes = "选择文件上传")
    @ApiImplicitParam(name = "files", paramType = "MultipartFile[]", value = "选择上传的文件", required = true)
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, headers = "content-type=multipart/form-data")
    public Map<String, Object> upload(MultipartFile[] files) {
        Map<String, Object> result = new HashMap<>();
        if (files.length == 0) {
            result.put("status", false);
            result.put("errorCode", 200);
            result.put("errorDesc", "请选择图片上传");
            return result;
        }
        try {
            List<Map> list = new ArrayList<>();
            for (MultipartFile file : files) {
                boolean ifsize = CheckFileSize.check(file, 10240, "M");
                if (ifsize) {
                    Map<String, Object> map = new HashMap<>();
                    String url = fdfsClient.uploadFile(file);
                    map.put("filename", file.getOriginalFilename());
                    String extname = file.getOriginalFilename().split("\\.")[1];
                    map.put("extname", extname);
                    if (!"jpg".equals(extname) && !"jpeg".equals(extname) && !"png".equals(extname)) {
                        map.put("url", url + "?attname=" + file.getOriginalFilename());
                    } else {
                        map.put("url", url);
                    }
                    list.add(map);
                } else {
                    result.put("errorCode", 200);
                    result.put("errorDesc", "允许上传最大为10G！");
                    result.put("status", false);
                    return result;
                }
            }
            result.put("errorCode", 200);
            result.put("status", true);
            result.put("errorDesc", "上传成功");
            result.put("object", list);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.put("status", false);
            result.put("errorCode", 500);
            result.put("errorDesc", "系统错误");
            return result;
        }
    }

    @ApiOperation(value = "上传文件", notes = "选择文件上传")
    @ApiImplicitParam(name = "files", paramType = "MultipartFile[]", value = "选择上传的文件", required = true)
    @RequestMapping(value = "/upload1", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, headers = "content-type=multipart/form-data")
    public Map<String, Object> upload1(MultipartFile[] files) {
        Map<String, Object> result = new HashMap<>();
        if (files.length == 0) {
            result.put("status", false);
            result.put("code", 200);
            result.put("msg", "请选择图片上传");
            return result;
        }
        try {
            List<Map> list = new ArrayList<>();
            for (MultipartFile file : files) {
                boolean ifsize = CheckFileSize.check(file, 10240, "M");
                if (ifsize) {
                    Map<String, Object> map = new HashMap<>();
                    String url = fdfsClient.uploadFile(file);
                    map.put("filename", file.getOriginalFilename());
                    String extname = file.getOriginalFilename().split("\\.")[1];
                    map.put("extname", extname);
                    if (!"jpg".equals(extname) && !"jpeg".equals(extname) && !"png".equals(extname)) {
                        map.put("url", url + "?attname=" + file.getOriginalFilename());
                    } else {
                        map.put("url", url);
                    }
                    list.add(map);
                } else {
                    result.put("code", 200);
                    result.put("msg", "允许上传最大为10G！");
                    result.put("status", false);
                    return result;
                }
            }
            result.put("code", 200);
            result.put("status", true);
            result.put("msg", "上传成功");
            result.put("urls", list);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.put("status", false);
            result.put("code", 500);
            result.put("msg", "系统错误");
            return result;
        }
    }

    /**
     * 图片上传，色情识别
     *
     * @param files
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "图片上传", notes = "图片上传，色情识别")
    @ApiImplicitParam(name = "file", paramType = "File", value = "选择上上传的文件", required = true)
    @RequestMapping(value = "/imgUpload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, headers = "content-type=multipart/form-data")
    public Map<String, Object> imgUpload(MultipartFile[] files) {
        Map<String, Object> result = new HashMap<>();
        if (files.length == 0) {
            result.put("status", false);
            result.put("errorCode", 200);
            result.put("errorDesc", "请选择图片上传");
            return result;
        }
        try {
            List<Map> list = new ArrayList<>();
            for (MultipartFile file : files) {
                byte[] bytes = file.getBytes();
                JSONObject response = InfoReview.getClient().imageCensorUserDefined(bytes, null);

                if ("合规".equals(response.getString("conclusion"))) {
                    boolean ifsize = CheckFileSize.check(file, 10240, "M");
                    if (ifsize) {
                        Map<String, Object> map = new HashMap<>();
                        String url = fdfsClient.uploadFile(file);
                        map.put("filename", file.getOriginalFilename());
                        String extname = file.getOriginalFilename().split("\\.")[1];
                        map.put("extname", extname);
                        if (!"jpg".equals(extname) && !"jpeg".equals(extname) && !"png".equals(extname)) {
                            map.put("url", url + "?attname=" + file.getOriginalFilename());
                        } else {
                            map.put("url", url);
                        }
                        list.add(map);
                    } else {
                        result.put("errorCode", 200);
                        result.put("status", false);
                        result.put("errorDesc", "允许上传最大为10G！");
                        return result;
                    }
                } else {
                    result.put("status", false);
                    result.put("errorCode", 200);
                    result.put("errorDesc", "图片包含色情等敏感信息");
                    return result;
                }
            }
            result.put("status", true);
            result.put("errorCode", 200);
            result.put("errorDesc", "上传成功");
            result.put("object", list);
            return result;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            result.put("status", false);
            result.put("errorCode", 500);
            result.put("errorDesc", "系统错误");
            return result;
        }
    }

    /**
     * 文件下载
     *
     * @param fileUrl  url 开头从组名开始
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "下载文件", notes = "根据url下载文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "fileUrl", value = "文件路径")})
    @RequestMapping(value = "/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public void download(String fileUrl, HttpServletResponse response) throws Exception {

        byte[] data = fdfsClient.download(fileUrl);

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("Aest.7z", "UTF-8"));

        // 写出
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.write(data, outputStream);
    }

}
