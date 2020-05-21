package com.ruoyi.fabric.service;


public interface IBlockAcad {

    // 院士信息链上存证
    public String add(String acadNo,String mekeletree);

    // 院士信息链上更新
    public String update(String acadNo,String mekeletree);

}
