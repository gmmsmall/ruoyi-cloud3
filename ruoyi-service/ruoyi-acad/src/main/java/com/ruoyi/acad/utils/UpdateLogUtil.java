package com.ruoyi.acad.utils;

import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 日志更新记录表
 * @Author guomiaomiao
 * @Date 2020/6/15 9:59
 * @Version 1.0
 */

public class UpdateLogUtil {
    public static String UpdateLog(Object obj_old,Object obj_new) throws Exception {
        Class clas_old = obj_old.getClass();
        Class clas_new = obj_new.getClass();
        if(!(clas_old.isInstance(obj_new))){
            //System.out.println("UpdateLogUtil.UpdateLog:传入的两个java对象类型不一致！");
            return "UpdateLogUtil.UpdateLog:传入的两个java对象类型不一致！";
        }
        Field[] fields = clas_old.getDeclaredFields();
        String remark = "";
        for (Field field : fields) {
            String name = field.getName();
            String type = field.getType().getName();
            field.setAccessible(true); //设置些属性是可以访问的
            Object val_old = field.get(obj_old);//得到此属性的修改前值
            Object val_new = field.get(obj_new);//得到此属性的修改后值
//bigdecimal 类型的数据要去掉小数点后尾部的0不一致造成数据比对差异
            if(type.equals("java.math.BigDecimal") && val_old!=null && val_new!=null ){
                BigDecimal val_old_big = new BigDecimal(String.valueOf(val_old));
                BigDecimal val_new_big = new BigDecimal(String.valueOf(val_new));
                if(String.valueOf(val_old_big).indexOf(".")!= -1 || String.valueOf(val_new_big).indexOf(".")!= -1 ){//由于无法获取精度值，只能对所有带小数点的数据进行处理
                    DecimalFormat formatter1=new DecimalFormat("0.00");
                    val_old = formatter1.format(val_old_big);
                    val_new = formatter1.format(val_new_big);
                }
            }
//DATE 类型的数据要格式化
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(type.equals("java.util.Date")){
                if (val_old != null) {
                    val_old = sdf.format(val_old)+"；";
                }
                if (val_new != null) {
                    val_new = sdf.format(val_new)+"；";
                }
            }
            if(!String.valueOf(val_old).equals(String.valueOf(val_new))){
            //保存处理数据
                //System.out.println("UpdateLogUtil.UpdateLog:"+name+"--"+val_old+"----"+val_new);
                //1、获取属性上的指定类型的注解
                Annotation annotation = field.getAnnotation(ApiModelProperty.class);
                //有该类型的注解存在
                if (annotation!=null) {
                    //强制转化为相应的注解
                    ApiModelProperty xmlElement = (ApiModelProperty)annotation;
                    //3、获取属性上的指定类型的注解的指定方法
                    String str_val_old = "",str_val_new = "";
                    if (!"null".equals(String.valueOf(val_old))) {
                        str_val_old = String.valueOf(val_old);
                    }
                    if (!"null".equals(String.valueOf(val_new))) {
                        str_val_new = String.valueOf(val_new);
                    }
                    if (xmlElement.value().equals("##default")) {
                        //System.out.println("UpdateLogUtil.UpdateLog:属性【"+name+"】注解使用的name是默认值: "+xmlElement.value());
                        //remark += xmlElement.value()+"修改前：" + str_val_old+"、修改后："+str_val_new+"；";
                        remark += xmlElement.value()+",";
                    }else {
                        //System.out.println("UpdateLogUtil.UpdateLog:属性【"+name+"】注解使用的name是自定义的值: "+xmlElement.value());
                        //remark += xmlElement.value()+"修改前：" + str_val_old+"、修改后："+str_val_new+"；";
                        remark += xmlElement.value()+",";
                    }
                }
            }
        }
        return remark;
    }

    //单个实体类修改前后，记录修改字段
    public static String UpdateBatchLog(Object obj_new) throws Exception {
        Class clas_new = obj_new.getClass();
        Field[] fields = clas_new.getDeclaredFields();
        String remark = "";
        for (Field field : fields) {
            field.setAccessible(true); //设置些属性是可以访问的
            Object val_new = field.get(obj_new);//得到此属性的修改后值
            if(val_new != null && StringUtils.isNotEmpty(String.valueOf(val_new))){
                //1、获取属性上的指定类型的注解
                Annotation annotation = field.getAnnotation(ApiModelProperty.class);
                //有该类型的注解存在
                if (annotation!=null) {
                    //强制转化为相应的注解
                    ApiModelProperty xmlElement = (ApiModelProperty)annotation;
                    if (xmlElement.value().equals("##default")) {
                        remark += xmlElement.value()+",";
                    }else {
                        remark += xmlElement.value()+",";
                    }
                }
            }
        }
        return remark;
    }

}
