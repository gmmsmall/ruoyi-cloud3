package com.ruoyi.fabric.service;

import com.ruoyi.fabric.bean.Aos;

import java.util.List;

public interface IBlockAos {

    /**
     * 新增科学院
     * @param aos
     * @return
     */
    public String add(Aos aos);


    /**
     * 更新科学院
     * @param aos
     * @return
     */
    public String update(Aos aos);


    /**
     * 遍历科学院
     * @param aosNo
     * @return
     */
    public List<Aos> query(String aosNo);



}
