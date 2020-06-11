package com.ruoyi.hdfs.service;

import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface HdfsService {

    public FileSystem getFileSystem() throws Exception;

    public boolean mkdir(String path) throws Exception;

    public boolean existFile(String path) throws Exception;

    public List<Map<String, Object>> readPathInfo(String path) throws Exception;

    public void createFile(String path, MultipartFile file) throws Exception;

    public String readFile(String path) throws Exception;

    public List<Map<String, String>> listFile(String path) throws Exception;

    public boolean renameFile(String oldName, String newName) throws Exception;

    public boolean deleteFile(String path) throws Exception;

    public void uploadFile(String path, String uploadPath) throws Exception;

    public void downloadFile(String path, String downloadPath) throws Exception;

    public void copyFile(String sourcePath, String targetPath) throws Exception;

    public byte[] openFileToBytes(String path) throws Exception;

    public BlockLocation[] getFileBlockLocations(String path) throws Exception;

}
