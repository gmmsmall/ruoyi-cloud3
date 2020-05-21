package com.ruoyi.fabric.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ruoyi.fabric.bean.Aos;
import com.ruoyi.fabric.bean.Role;
import com.ruoyi.fabric.bean.Token;
import com.ruoyi.fabric.service.IBlockUser;
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
    public List<Role> queryUserRole(String userId) {
        JSONArray js = new JSONArray();
        js.add("s1");
        js.add("s2");

        Role role = new Role();
        role.setAosNos(js);
        role.setRemark("描述信息");
        role.setRoleId(278223223);
        role.setRoleName("管理员");
        role.setTokenNos(js);

        List<Role> list = new ArrayList<>();
        list.add(role);
        list.add(role);

        return list;
    }

    @Override
    public List<Token> queryUserToken(String userId) {
        Token token = new Token();
        token.setType(1);
        token.setPerms("user:view");
        token.setParentNo("1585559448441");
        token.setOrderNum(1);
        token.setName("用户管理");
        token.setTokenNo("1585559448441");

        List<Token> list = new ArrayList<>();
        list.add(token);
        list.add(token);


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
