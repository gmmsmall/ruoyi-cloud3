package com.ruoyi.system.domain;

public class Car {
    private  String carName;//车名
    private  String carBrand;//品牌
    private  String carNum;//编号
    private  String carColour;//颜色
    private  String carOwner;//归属人

    public Car() {

    }
    public Car(String carName, String carBrand, String carNum, String carColour, String carOwner) {
        this.carName = carName;
        this.carBrand = carBrand;
        this.carNum = carNum;
        this.carColour = carColour;
        this.carOwner = carOwner;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCarColour() {
        return carColour;
    }

    public void setCarColour(String carColour) {
        this.carColour = carColour;
    }

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }
}
