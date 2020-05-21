package com.ruoyi.fabric.service;

import java.io.IOException;

public interface IBlockExplorer {

    // 获取链上当前区块高度
    public long getBlockHeight() ;
    // 获取链上当前交易数量
    public long getTransactionCount() ;
    // 获取网络基本信息
    public long getBlockBaseInfo() throws IOException;
    // 根据区块高度获取区块信息
    public String getBlockInfoByheight(long height);

}
