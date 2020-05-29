package com.ruoyi.fabric.service.impl;

import com.ruoyi.fabric.service.IBlockUser;
import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.Token;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FabricUserService implements IBlockUser {

    @Override
    public String uploadUserRole(String userId, String roleIds) {
        //具体实现暂时直接返回字段
        return "0";
    }

    @Override
    public List<SysRole> queryUserRole(String userId) {
        String[] js = new String[2];
        js[0] = "s1";
        js[1] = "s2";

        SysRole role = new SysRole();
        role.setAosNos(js);
        role.setRemark("描述信息");
        role.setRoleId(11L);
        role.setRoleName("管理员");
        role.setTokenNos(js);

        List<SysRole> list = new ArrayList<>();
        list.add(role);
        list.add(role);

        return list;
    }

    @Override
    public List<Token> queryUserToken(String userId) {
        Token fabricToken = new Token();
        fabricToken.setType(1);
        fabricToken.setPerms("user:view");
        fabricToken.setParentNo("1585559448441");
        fabricToken.setOrderNum(1);
        fabricToken.setName("用户管理");
        fabricToken.setTokenNo("1585559448441");

        List<Token> list = new ArrayList<>();
        list.add(fabricToken);
        list.add(fabricToken);


        return list;
    }

    @Override
    public List<Aos> queryUserAos(String userId) {
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
