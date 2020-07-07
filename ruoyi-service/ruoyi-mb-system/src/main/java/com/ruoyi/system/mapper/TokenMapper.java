package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.AcadMstAos;
import com.ruoyi.system.domain.Token;
import com.ruoyi.system.result.TokenResult;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author jxd
 */
@Repository
public interface TokenMapper {

    /**
     * 递归删除菜单/按钮
     *
     * @param tokenId tokenId
     */
    void deleteTokens(String tokenId);

    void insertToken(Token token);

    void updateToken(Token token);

    void deleteToken(String tokenNo);

    List<Token> selectList();

    /**
     * @return java.util.List<com.ruoyi.system.domain.Token>
     * @Author jxd
     * @Description 查询用户的所有权限
     * @Date 14:41 2020/5/27
     * @Param [userId]
     **/
    List<Token> selectTokenByUserId(Long userId);


    @Select(" select token_no,name from token where type = 8 ")
    List<Map<String,String>> selectViewList();
}
