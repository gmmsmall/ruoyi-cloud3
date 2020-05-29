package com.ruoyi.fabric.service.impl;

import com.ruoyi.fabric.service.IBlockToken;
import com.ruoyi.fabric.utils.Page;
import com.ruoyi.system.domain.Token;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FabricTokenService implements IBlockToken {

    @Override
    public String add(Token fabricToken) {
        //具体实现暂时直接返回字段
        return "0";
    }

    @Override
    public String update(Token fabricToken) {
        //具体实现暂时直接返回字段
        return "0";
    }

    @Override
    public Page query(int pageNum, int pageSize, String tokenNo, String name, String perms, String type) {
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

        Page page = new Page();
        page.setTotalPage(23);
        page.setCurrentPage(3);
        page.setTotalCount(550);
        page.setRows(list);

        return page;
    }


    @Override
    public int delete(String tokenNos) {
        return 0;
    }

}
