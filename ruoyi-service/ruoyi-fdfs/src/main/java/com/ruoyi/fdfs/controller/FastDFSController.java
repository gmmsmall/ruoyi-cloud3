package com.ruoyi.fdfs.controller;

import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.exception.RuoyiException;
import com.ruoyi.fdfs.client.FastDFSClient;
import com.ruoyi.fdfs.domain.FileResult;
import com.ruoyi.fdfs.domain.RedisConnectException;
import com.ruoyi.fdfs.domain.RespMsgBean;
import com.ruoyi.fdfs.service.FileService;
import com.ruoyi.fdfs.utils.CheckFileSize;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ProtoCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jxd
 * @Title: FastDFSController
 * @ProjectName
 * @Description:
 * @date 2019/6/17 15:14
 */
@RestController
@RequestMapping("fdfs")
@Api(value = "fdfs", description = "文件服务--上传下载")
public class FastDFSController {
    @Autowired
    private FastDFSClient fdfsClient;
    @Value("${fdfs.http.secret_key}")
    private String fastdfsToken;
    @Value("${fdfs.web-server-url}")
    private String fastdfsUrl;
    @Autowired
    private FileService fileService;

    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "上传文件", notes = "选择文件上传")
    @ApiImplicitParam(name = "files", paramType = "MultipartFile[]", value = "选择上传的文件", required = true)
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, headers = "content-type=multipart/form-data")
    public List<FileResult> upload(@RequestPart("file")MultipartFile[] files) throws Exception {
        if (files.length == 0 || "".equals(files[0].getOriginalFilename())) {
            throw new RuoyiException("请选择图片上传", 200);
        }
        List<FileResult> fileResults = new ArrayList<>();
        for (MultipartFile file : files) {
            boolean ifsize = CheckFileSize.check(file, 10240, "M");
            if (ifsize) {
                FileResult fileResult = new FileResult();
                String url = fdfsClient.uploadFile(file);
                fileResult.setFileName(file.getOriginalFilename());
                String extname = file.getOriginalFilename().split("\\.")[1];
                fileResult.setExtName(extname);
                if (!"jpg".equals(extname) && !"jpeg".equals(extname) && !"png".equals(extname)) {
                    fileResult.setUrl(url + "?attname=" + file.getOriginalFilename());
                } else {
                    fileResult.setUrl(url);
                }
                fileResults.add(fileResult);
            } else {
                throw new RuoyiException("文件过大", 200);
            }
        }
        return fileResults;
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
        //response.setContentType("application/x-download");
        //response.setContentType("multipart/form-data");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("test.7z", "UTF-8"));

        // 写出
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.write(data, outputStream);
    }

    /**
     * 文件下载
     *
     * @param fileUrl url 开头从组名开始
     * @throws Exception
     */
    @ApiOperation(value = "删除文件", notes = "根据url删除文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "fileUrl", value = "文件路径")})
    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public RE delete(String fileUrl) throws Exception {

        try {
            fdfsClient.deleteFile(fileUrl);
            return RE.ok("删除文件成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RE.error();
        }

    }

    /**
     * 获取访问服务器的token，拼接到地址后面
     *
     * @param fid 文件路径 http://www.mingbyte.com:1234/group1/M00/00/00/wKgIgV6GqLaAcAv4AAKnZ9K1ed4745.png
     * @return 返回token，如： token=078d370098b03e9020b82c829c205e1f&ts=1508141521
     */
    @ApiOperation(value = "获取带防盗链的连接", notes = "获取带防盗链的连接")
    @ApiImplicitParams({@ApiImplicitParam(name = "fid", value = "文件url")})
    @GetMapping("getUrlWithToken")
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


    @GetMapping("/check_before_upload")
    @ApiOperation("分片上传前的检测")
    public RespMsgBean checkBeforeUpload(@RequestParam("userId") Long userId, @RequestParam("fileMd5") String fileMd5) throws RedisConnectException {
        return fileService.checkFile(userId, fileMd5);
    }

    @PostMapping("/upload_big_file_chunk")
    @ApiOperation("分片上传大文件")
    public RespMsgBean uploadBigFileChunk(@RequestParam("file") @ApiParam(value = "文件", required = true) MultipartFile file,
                                          @RequestParam("userId") @ApiParam(value = "用户id", required = true) Long userId,
                                          @RequestParam("fileMd5") @ApiParam(value = "文件MD5值", required = true) String fileMd5,
                                          @RequestParam("fileName") @ApiParam(value = "文件名称", required = true) String fileName,
                                          @RequestParam("totalChunks") @ApiParam(value = "总块数", required = true) Integer totalChunks,
                                          @RequestParam("chunkNumber") @ApiParam(value = "当前块数", required = true) Integer chunkNumber,
                                          @RequestParam("currentChunkSize") @ApiParam(value = "当前块的大小", required = true) Integer currentChunkSize,
                                          @RequestParam("bizId") @ApiParam(value = "业务Id", required = true) String bizId,
                                          @RequestParam("bizCode") @ApiParam(value = "业务编码", required = true) String bizCode) throws RedisConnectException {
        return fileService.uploadBigFileChunk(file, userId, fileMd5, fileName, totalChunks, chunkNumber, currentChunkSize, bizId, bizCode);
    }

    @RequestMapping("/")
    public String toIndex() {
        return "toUpload";
    }

}
