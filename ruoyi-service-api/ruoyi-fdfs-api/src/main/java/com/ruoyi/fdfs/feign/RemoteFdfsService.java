package com.ruoyi.fdfs.feign;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.fdfs.feign.factory.RemoteFdfsFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

import com.ruoyi.common.constant.ServiceNameConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.Map;

/**
 * 用户 Feign服务层
 * 
 * @author zmr
 * @date 2019-05-20
 */
@FeignClient(name = ServiceNameConstants.FDFS_SERVICE, fallbackFactory = RemoteFdfsFallbackFactory.class)
public interface RemoteFdfsService
{
    @RequestMapping(value = "/fdfs/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, headers = "content-type=multipart/form-data")
    public Map<String, Object> upload(@RequestPart("file")MultipartFile file);

    @RequestMapping(value = "/fdfs/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public R delete(String fileUrl) throws Exception;

    @RequestMapping(value = "/fdfs/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public void download(String fileUrl) throws Exception;

}
