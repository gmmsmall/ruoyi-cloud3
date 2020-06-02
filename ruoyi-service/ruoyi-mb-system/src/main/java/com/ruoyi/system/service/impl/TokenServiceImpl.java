package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.RuoyiException;
import com.ruoyi.system.domain.Token;
import com.ruoyi.system.domain.TokenForQuery;
import com.ruoyi.system.domain.TokenTree;
import com.ruoyi.system.feign.RemoteIBlockTokenService;
import com.ruoyi.system.mapper.TokenMapper;
import com.ruoyi.system.result.FabricResult;
import com.ruoyi.system.result.TokenResult;
import com.ruoyi.system.service.ITokenService;
import com.ruoyi.system.util.IdGenerator;
import com.ruoyi.system.util.TokenTreeUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jxd
 */
@Service
public class TokenServiceImpl implements ITokenService {

    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private RemoteIBlockTokenService remoteIBlockTokenService;

    @Override
    @Transactional
    public int createToken(Token token) {
        token.setTokenNo(IdGenerator.getId());
        token.setCreateTime(LocalDateTime.now());
        if (StringUtil.isNullOrEmpty(token.getParentNo())) {
            token.setParentNo("0");
        }
        setToken(token);
//        tokenMapper.insertToken(token);
        String result = remoteIBlockTokenService.addToken(token);
        return result != null && JSON.parseObject(result, FabricResult.class).getCode() == FabricResult.RESULT_SUCC ? 1 : 0;
    }

    @Override
    @Transactional
    public int updateToken(Token token) {
        token.setUpdateTime(LocalDateTime.now());
        setToken(token);
//        tokenMapper.updateToken(token);
        String result = remoteIBlockTokenService.updateToken(token);
        return result != null && JSON.parseObject(result, FabricResult.class).getCode() == FabricResult.RESULT_SUCC ? 1 : 0;
    }

    @Override
    @Transactional
    public int deleteTokens(String[] tokenNos) {
//        for (String tokenNo : tokenNos) {
//            tokenMapper.deleteTokens(tokenNo);
//        }
        Map<String, Object> params = new HashMap<>();
        params.put("tokenNos", tokenNos);
        String result = remoteIBlockTokenService.deleteToken(params);
        return result != null && JSON.parseObject(result, FabricResult.class).getCode() == FabricResult.RESULT_SUCC ? 1 : 0;
    }

    @Override
    public TokenTree findTokens(TokenForQuery tokenForQuery) {
        List<Token> tokenList = null;
        String result = remoteIBlockTokenService.queryTokens(tokenForQuery);
        if (null != result) {
            FabricResult fabricResult = JSON.parseObject(result, FabricResult.class);
            if (fabricResult.getCode() == FabricResult.RESULT_SUCC && fabricResult.getTokenList() != null) {
                tokenList = fabricResult.getTokenList();
            }
        } else {
            throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
        }
        List<TokenTree<Token>> trees = new ArrayList<>();
        buildTrees(trees, tokenList);
        TokenTree<Token> tokenTree = TokenTreeUtil.build(trees);
        return tokenTree;
    }

    @Override
    public List<TokenResult> getList() {
        TokenForQuery tokenForQuery = new TokenForQuery();
        tokenForQuery.setPageNum(1);
        tokenForQuery.setPageSize(99999999);
        String result = remoteIBlockTokenService.queryTokens(tokenForQuery);
        if (null != result) {
            FabricResult fabricResult = JSON.parseObject(result, FabricResult.class);
            if (fabricResult.getCode() == FabricResult.RESULT_SUCC && fabricResult.getTokenList() != null) {
                List<TokenResult> tokenResultList = new ArrayList<>();
                for (Token token : fabricResult.getTokenList()) {
                    TokenResult tokenResult = new TokenResult();
                    BeanUtils.copyProperties(token, tokenResult);
                    tokenResultList.add(tokenResult);
                }
                return tokenResultList;
            }
        } else {
            throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
        }
        return null;
    }

    private void buildTrees(List<TokenTree<Token>> trees, List<Token> tokenList) {
        tokenList.forEach(token -> {
            TokenTree<Token> tree = new TokenTree<>();
            tree.setCreateTime(token.getCreateTime());
            tree.setName(token.getName());
            tree.setOrderNum(token.getOrderNum());
            tree.setParentNo(token.getParentNo());
            tree.setPerms(token.getPerms());
            tree.setRoute(token.getRoute());
//            tree.setTokenId(token.getTokenId());
            tree.setTokenNo(token.getTokenNo());
            tree.setTokenType(token.getType());
            tree.setUpdateTime(token.getUpdateTime());
            trees.add(tree);
        });
    }

    private void setToken(Token token) {
        if (token.getParentNo() == null) {
            token.setParentNo("");
        }
    }
}
