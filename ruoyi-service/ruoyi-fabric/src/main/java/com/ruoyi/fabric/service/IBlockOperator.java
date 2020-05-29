package com.ruoyi.fabric.service;

public interface IBlockOperator {

    /**
     * 提交交易数据到链上
     *
     * @param chaincodeFunction 链码函数名
     * @param args              参数集
     * @return 交易标识 TXID
     */
    //List<String> paramList
    public String invoke(String chaincodeFunction, String[] args);

    /**
     * 查询链上业务数据
     *
     * @param chaincodeFunction 链码函数名
     * @param args              参数集
     * @return 交易标识 TXID
     */
    public String query(String chaincodeFunction, String[] args);
    // TODO: 增加其他接口
}