package com.ruoyi.acad.feign.factory;

import com.ruoyi.acad.feign.RemoteAcadBaseInfoService;
import com.ruoyi.common.core.domain.RE;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 院士信息的fallback
 * @Author guomiaomiao
 * @Date 2020/6/5 10:45
 * @Version 1.0
 */
@Slf4j
@Component
public class RemoteAcadBaseInfoFallbackFactory implements FallbackFactory<RemoteAcadBaseInfoService> {
    @Override
    public RemoteAcadBaseInfoService create(Throwable throwable) {
        return new RemoteAcadBaseInfoService(){
            @Override
            public RE getNameByAcadId(Integer acadId){
                log.info("根据院士编码查询院士姓名失败[{}]",acadId);
                return null;
            }

            @Override
            public RE getAcadListByName(String name){
                log.info("根据院士姓名模糊查询院士编码集合[{}]",name);
                return null;
            }

            @Override
            public RE getAcadNameByAcadList(List<String> list) {
                log.info("根据院士编码列表查询院士姓名[{}]",list);
                return null;
            }
        };
    }
}
