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


    int createToken(Token token) throws Exception;

    int updateToken(Token menu) throws Exception;

    int deleteTokens(String[] tokenNos) throws Exception;

    TokenTree findTokens(TokenForQuery tokenForQuery);

    List<TokenResult> getList();
}
