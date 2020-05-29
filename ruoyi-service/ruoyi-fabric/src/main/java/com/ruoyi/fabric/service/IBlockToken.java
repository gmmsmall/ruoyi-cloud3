package com.ruoyi.fabric.service;

import com.ruoyi.fabric.utils.Page;
import com.ruoyi.system.domain.Token;

public interface IBlockToken {

    /**
     * 新增令牌
     *
     * @param fabricToken
     * @return
     */
    public String add(Token fabricToken);

    /**
     * 更新令牌
     *
     * @param fabricToken
     * @return
     */
    public String update(Token fabricToken);

    /**
     * 遍历令牌
     *
     * @param pageNum
     * @param pageSize
     * @param tokenNo
     * @param name
     * @param perms
     * @param type
     * @return
     */
    public Page query(int pageNum, int pageSize, String tokenNo, String name, String perms, String type);

    /**
     * 删除令牌
     *
     * @param tokenNos 
     * @return
     */
    public int delete(String tokenNos);


}
