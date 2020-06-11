package com.ruoyi.hdfs.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.hdfs.service.HdfsService;
import org.apache.hadoop.fs.BlockLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author ：
 * @date ：Created in 2019/11/5 13:09
 * @description：
 * @modified By：
 * @version:
 */
@RestController
@RequestMapping("/hdfs")
public class HdfsController {

    @Autowired
    private HdfsService hdfsService;

    /**
     * 创建文件夹 (已测试)
     * @param path
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mkdir", method = RequestMethod.GET)
    @ResponseBody
    public String mkdir(@RequestParam("path") String path) throws Exception {
        if (path.isEmpty()) {
//            LOGGER.debug("请求参数为空");
            return "请求参数为空";
        }
        // 创建空文件夹
        boolean isOk = hdfsService.mkdir(path);
        if (isOk) {
            System.out.println("文件夹创建成功");
            return "文件夹创建成功";
        } else {
//            LOGGER.debug("文件夹创建失败");
            return "文件夹创建失败";
        }
    }


    /**
     * 读取文件夹信息（(已测试)）
     * @param path
     * @return
     * @throws Exception
     */
    @GetMapping("/readPathInfo")
    public JSONObject  readPathInfo(@RequestParam("path") String path) throws Exception {
        System.out.println(path);
        List<Map<String, Object>> list = hdfsService.readPathInfo(path);
        System.out.println(list.size());
        list.stream().forEach(System.out::println);
        return new JSONObject();
    }

    /**
     * 查看文件或文件夹是否已存在（已测试）
     * @param path
     * @return
     * @throws Exception
     */
    @GetMapping("/existFile")
    public  String  existFile(@RequestParam("path") String path) throws Exception {
        boolean isExist = hdfsService.existFile(path);
        return ""+isExist;
    }


    /**
     * 获取HDFS文件在集群中的位置
     * @param path
     * @return
     * @throws Exception
     */
    @GetMapping("/getFileBlockLocations")
    public String getFileBlockLocations(@RequestParam("path") String path) throws Exception {
        BlockLocation[] blockLocations = hdfsService.getFileBlockLocations(path);
        return  "获取HDFS文件在集群中的位置"+ blockLocations ;
    }

    /**
     * 创建文件
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/createFile")
    public String  createFile(@RequestParam("path") String path, @RequestParam("file") MultipartFile file)
            throws Exception {
        if (path.isEmpty() || null == file.getBytes()) {
            return   "请求参数为空" ;
        }
        hdfsService.createFile(path, file);
        return    "创建文件成功" ;
    }

    /**
     * 读取HDFS文件内容
     * @param path
     * @return
     * @throws Exception
     */
    @GetMapping("/readFile")
    public String readFile(@RequestParam("path") String path) throws Exception {
        String targetPath = hdfsService.readFile(path);
        return   "读取HDFS文件内容:"+targetPath ;
    }

//    /**
//     * 读取HDFS文件转换成Byte类型
//     * @param path
//     * @return
//     * @throws Exception
//     */
//    @GetMapping("/openFileToBytes")
//    public Result openFileToBytes(@RequestParam("path") String path) throws Exception {
//        byte[] files = HdfsService.openFileToBytes(path);
//        return new Result(Result.SUCCESS, "读取HDFS文件转换成Byte类型", files);
//    }

//    /**
//     * 读取HDFS文件装换成User对象
//     * @param path
//     * @return
//     * @throws Exception
//     */
//    @GetMapping("/openFileToUser")
//    public String openFileToUser(@RequestParam("path") String path) throws Exception {
//        User user = HdfsService.openFileToObject(path, User.class);
//        return new Result(Result.SUCCESS, "读取HDFS文件装换成User对象", user);
//    }

    /**
     * 读取文件列表
     * @param path
     * @return
     * @throws Exception
     */
    @GetMapping("/listFile")
    public String listFile(@RequestParam("path") String path) throws Exception {
        if (path.isEmpty()) {
            return  "请求参数为空"  ;
        }
        List<Map<String, String>> returnList = hdfsService.listFile(path);
        return "读取文件列表成功"+returnList.size()
                ;
    }

    /**
     * 重命名文件 (已测试)
     * @param oldName
     * @param newName
     * @return
     * @throws Exception
     */
    @GetMapping("/renameFile")
    public String renameFile(@RequestParam("oldName") String oldName, @RequestParam("newName") String newName)
            throws Exception {
        if (oldName.isEmpty() || newName.isEmpty()) {
            return "请求参数为空";
        }
        boolean isOk = hdfsService.renameFile(oldName, newName);
        if (isOk) {
            return "文件重命名成功";
        } else {
            return "文件重命名失败";
        }
    }

    /**
     * 删除文件
     * @param path
     * @return
     * @throws Exception
     */
    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam("path") String path) throws Exception {
        boolean isOk = hdfsService.deleteFile(path);
        if (isOk) {
            return "delete file success";
        } else {
            return "delete file fail";
        }
    }

    /**
     * 上传文件
     * @param path
     * @param uploadPath
     * @return
     * @throws Exception
     */
    @GetMapping("/uploadFile")
    public String uploadFile(@RequestParam("path") String path, @RequestParam("uploadPath") String uploadPath)
            throws Exception {
        hdfsService.uploadFile(path, uploadPath);
        return  "upload file success";
    }

    /**
     * 下载文件
     * @param path
     * @param downloadPath
     * @return
     * @throws Exception
     */
    @GetMapping("/downloadFile")
    public String downloadFile(@RequestParam("path") String path, @RequestParam("downloadPath") String downloadPath)
            throws Exception {
        hdfsService.downloadFile(path, downloadPath);
        return "download file success";
    }

    /**
     * HDFS文件复制
     * @param sourcePath
     * @param targetPath
     * @return
     * @throws Exception
     */
    @GetMapping("/copyFile")
    public String copyFile(@RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath)
            throws Exception {
        hdfsService.copyFile(sourcePath, targetPath);
        return  "copy file success";
    }



}
