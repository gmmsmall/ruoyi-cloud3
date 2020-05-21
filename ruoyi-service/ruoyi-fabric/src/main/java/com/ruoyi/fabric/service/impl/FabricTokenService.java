package com.ruoyi.fabric.service.impl;

import com.ruoyi.fabric.bean.Token;
import com.ruoyi.fabric.service.IBlockToken;
import com.ruoyi.fabric.utils.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FabricTokenService implements IBlockToken {

    @Override
    public String add(Token token) {
        //具体实现暂时直接返回字段
        return "0";
    }

    @Override
    public String update(Token token) {
        //具体实现暂时直接返回字段
        return "0";
    }

    @Override
    public Page query(int pageNum, int pageSize, String tokenNo, String name, String perms, String type) {
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
