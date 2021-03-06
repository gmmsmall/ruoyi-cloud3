package com.ruoyi.acad.feign;

import com.ruoyi.acad.feign.factory.RemoteAcadBaseInfoFallbackFactory;
import com.ruoyi.common.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.RE;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * feign调用院士的相关信息
 * @Author guomiaomiao
 * @Date 2020/6/5 10:26
 * @Version 1.0
 */
@FeignClient(name = ServiceNameConstants.ACAD_BASE_INFO,fallbackFactory = RemoteAcadBaseInfoFallbackFactory.class)
public interface RemoteAcadBaseInfoService {

    /**
     * 根据院士编码查询院士姓名
     * @param acadId
     * @return
     */
    @GetMapping("/baseInfo/getNameByAcadId")
    public RE getNameByAcadId(@RequestParam("acadId") Integer acadId);

    /**
     * 根据院士姓名模糊查询院士编码集合
     * @param name
     * @return
     */
    @GetMapping("/baseInfo/getAcadListByName")
    public RE getAcadListByName(@RequestParam("name") String name);

    /**
     * 根据院士编码列表查询院士姓名
     * @param list
     * @return
     */
    @GetMapping("/baseInfo/getAcadNameByAcadList")
    public RE getAcadNameByAcadList(@RequestParam("list")List<String> list);
}
