package com.ruoyi.fdfs.feign;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.fdfs.feign.factory.RemoteFdfsFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

import com.ruoyi.common.constant.ServiceNameConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.MediaType;
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
    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, headers = "content-type=multipart/form-data")
    public Map<String, Object> upload(MultipartFile file);

    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public R delete(String fileUrl) throws Exception;

}
