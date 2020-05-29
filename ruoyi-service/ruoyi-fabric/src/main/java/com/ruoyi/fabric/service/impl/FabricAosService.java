package com.ruoyi.fabric.service.impl;

import com.ruoyi.fabric.service.IBlockAos;
import com.ruoyi.system.domain.Aos;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FabricAosService implements IBlockAos {

    @Override
    public String add(Aos aos) {
        //具体实现暂时直接返回字段
        return "0";
    }

    @Override
    public String update(Aos aos) {
        //具体实现暂时直接返回字段
        return "0";
    }

    @Override
    public List<Aos> query(String aosNo) {
        Aos aos = new Aos();
        aos.setCountryId("123");
        aos.setAosNo("user:view");
        aos.setAosLogoUrl("1585559448441");
        aos.setAosHomePage("http://dhshsh");
        aos.setAosEnname("用户管理");
        aos.setAosCnname("1585559448441");

        List<Aos> list = new ArrayList<>();
        list.add(aos);
        list.add(aos);

        return list;
    }


}
