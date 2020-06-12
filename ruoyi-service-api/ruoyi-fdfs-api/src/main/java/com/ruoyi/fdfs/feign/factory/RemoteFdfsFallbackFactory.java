package com.ruoyi.fdfs.feign.factory;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.fdfs.feign.RemoteFdfsService;
import org.springframework.stereotype.Component;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RemoteFdfsFallbackFactory implements FallbackFactory<RemoteFdfsService>
{/* (non-Javadoc)
 * @see feign.hystrix.FallbackFactory#create(java.lang.Throwable)
 */
    @Override
    public RemoteFdfsService create(Throwable throwable)
    {
        log.error(throwable.getMessage());
        return new RemoteFdfsService()
        {
            @Override
            public Map<String, Object> upload(MultipartFile file) {

                Map<String, Object> result = new HashMap<>();
                result.put("status", false);
                result.put("errorCode", 200);
                result.put("errorDesc", "文件服务异常");
                return result;
            }

            @Override
            public R delete(String fileUrl) {

                return  R.error();
            }
            @Override
            public void download(String fileUrl) {
                log.info("下载文件失败");
            }
        };
    }
}
