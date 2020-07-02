package com.ruoyi.acad.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * pdf多页背景图片解决方法
 * @Author guomiaomiao
 * @Date 2020/7/2 10:45
 * @Version 1.0
 */

public class BackGroundImage extends PdfPageEventHelper {

    private String picPath = "";//背景图片路径

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public BackGroundImage(){

    }

    public BackGroundImage(String path){
        this.picPath = path;
    }

    @Override
    public void onStartPage(PdfWriter pdfWriter, Document document) {
        try {
            Image image = Image.getInstance(picPath);
            image.setAlignment(image.UNDERLYING);
            image.setAbsolutePosition(0,0);
            image.scaleAbsolute(595,842);
            image.setAlignment(Image.UNDERLYING);
            // 设置图片旋转
//            image.setRotation((float) (-Math.PI / 6));
            document.add(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStartPage(pdfWriter, document);
    }


}
