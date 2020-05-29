package com.ruoyi.fabric.service;


import com.ruoyi.system.domain.UserContext;

public interface IBlockCA {

    // 获取链上当前区块高度
    public String enrollAdmin();

    // 获取链上当前交易数量
    public String registerUser(UserContext userContext);

    public String reEnroll(UserContext userContext);
}
