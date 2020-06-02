package com.ruoyi.system.service;


import com.ruoyi.system.domain.Token;
import com.ruoyi.system.domain.TokenForQuery;
import com.ruoyi.system.domain.TokenTree;
import com.ruoyi.system.result.TokenResult;

import java.util.List;

/**
 * @author jxd
 */
public interface ITokenService {


    int createToken(Token token);

    int updateToken(Token menu);

    int deleteTokens(String[] tokenNos);

    TokenTree findTokens(TokenForQuery tokenForQuery);

    List<TokenResult> getList();
}
