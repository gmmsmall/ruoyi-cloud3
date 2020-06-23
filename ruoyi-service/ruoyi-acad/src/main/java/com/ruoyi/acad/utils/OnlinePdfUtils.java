package com.ruoyi.acad.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.ruoyi.acad.domain.OnlinePdfEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class OnlinePdfUtils {

    public byte[] createpdf(List<OnlinePdfEntity> infoList, String imagePath, List<OnlinePdfEntity> detailList,String path) throws Exception{
        //Step 1: 实例化文档对象，设置文档背景，大小等
        Rectangle rectPageSize = new Rectangle(PageSize.A4);// A4纸张
        rectPageSize.setBackgroundColor(BaseColor.WHITE);//文档的背景色
        //创建一个文档对象，设置初始化大小和页边距
        Document document = new Document(rectPageSize, 40, 40, 40, 40);// 上、下、左、右间距
        //Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        //Step 2: 创建 PdfWriter 对象:第一个参数是文档对象的引用，第二个参数是输出将写入的文件的绝对名称
        //InputStream inputStream = this.byteByUrl(path+"/static/acadtemp.pdf");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//构建字节输出流
        PdfWriter writer = PdfWriter.getInstance(document,baos);
        //Step 3: 打开文档对象
        document.open();
        BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        //BaseFont bfChinese = BaseFont.createFont("src/main/resources/static/malgun.ttf",BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font fontChinese1 = new Font(bfChinese, 13,Font.BOLD);
        Font fontChinese2 = new Font(bfChinese, 12,Font.NORMAL);
        Font fontChinese3 = new Font(bfChinese, 12,Font.UNDERLINE);
        //人物图片
        if(imagePath != null && !imagePath.equals("")){
            Image img = Image.getInstance(this.getImg("http://"+imagePath));
            img.setAlignment(Image.RIGHT);
            img.setBorder(Image.BOX);
            img.setBorderWidth(10);
            img.setBorderColor(BaseColor.WHITE);
            img.scaleToFit(120, 180);// 大小
            //img.setAbsolutePosition(1000,20);
            document.add(img);
        }else{
            Image img = Image.getInstance(path+"/static/tongyong.jpg");
            img.setAlignment(Image.RIGHT);
            img.setBorder(Image.BOX);
            img.setBorderWidth(10);
            img.setBorderColor(BaseColor.WHITE);
            img.scaleToFit(120, 200);// 大小
            //img.setAbsolutePosition(1000,20);
            document.add(img);
        }
        //显示顺序：姓名、性别、年龄、籍贯、授衔机构、授衔研究领域分类、联系状态、签约类型、宗教信仰
        //人物基本信息
        if(infoList != null && infoList.size() > 0){
            Phrase tPhrase = new Phrase();
            tPhrase.setLeading(22f);
            for(OnlinePdfEntity entity : infoList){
                //if(entity.getInfo() != null && !entity.getInfo().equals("")){//有内容则显示在简历上
                    //Paragraph pragraph=new Paragraph(entity.getRemark(), fontChinese);
                    if(entity.getRemark() != null){
                       switch (entity.getRemark()){
                           case "姓名 ：" :
                               Chunk chunkname = new Chunk(entity.getRemark(),fontChinese1);
                               chunkname.setTextRise(100);
                               tPhrase.add(chunkname);
                               if(entity.getInfo() != null){
                                   Chunk chunknamea = new Chunk(new Chunk(entity.getInfo(), fontChinese2));
                                   chunknamea.setTextRise(100);
                                   tPhrase.add(chunknamea);
                               }

                               tPhrase.add(Chunk.NEWLINE);
                               break;
                           case "年龄 ： " :
                               Chunk chunkage = new Chunk(entity.getRemark(),fontChinese1);
                               chunkage.setTextRise(80);
                               tPhrase.add(chunkage);
                               if(entity.getInfo() != null){
                                   Chunk chunkagea = new Chunk(new Chunk(entity.getInfo(), fontChinese2));
                                   chunkagea.setTextRise(80);
                                   tPhrase.add(chunkagea);
                               }

                               tPhrase.add(Chunk.NEWLINE);
                               break;
                           case "国籍 ： " :
                               Chunk chunkcontr = new Chunk(entity.getRemark(),fontChinese1);
                               chunkcontr.setTextRise(60);
                               tPhrase.add(chunkcontr);
                               if(entity.getInfo() != null){
                                   Chunk chunkcontra = new Chunk(new Chunk(entity.getInfo(), fontChinese2));
                                   chunkcontra.setTextRise(60);
                                   tPhrase.add(chunkcontra);
                               }

                               tPhrase.add(Chunk.NEWLINE);
                               break;
                           case "授衔机构 ：" :
                               Chunk chunkorg = new Chunk(entity.getRemark(),fontChinese1);
                               chunkorg.setTextRise(39);
                               tPhrase.add(chunkorg);
                               if(entity.getInfo() != null){
                                   Chunk chunkorga = new Chunk(new Chunk(entity.getInfo(), fontChinese2));
                                   chunkorga.setTextRise(39);
                                   tPhrase.add(chunkorga);
                               }

                               tPhrase.add(Chunk.NEWLINE);
                               break;
                           case "研究领域 ：" :
                               Chunk chunkorgtype = new Chunk(entity.getRemark(),fontChinese1);
                               chunkorgtype.setTextRise(20);
                               tPhrase.add(chunkorgtype);
                               if(entity.getInfo() != null){
                                   Chunk chunkorgtypea = new Chunk(new Chunk(entity.getInfo(), fontChinese2));
                                   chunkorgtypea.setTextRise(20);
                                   tPhrase.add(chunkorgtypea);
                               }

                               tPhrase.add(Chunk.NEWLINE);
                               break;
                           default:
                               Chunk chunk = new Chunk(entity.getRemark(),fontChinese1);
                               tPhrase.add(chunk);
                               if(entity.getInfo() != null){

                               }
                               Chunk chunkinfo = new Chunk(new Chunk(entity.getInfo(), fontChinese2));
                               tPhrase.add(chunkinfo);
                               tPhrase.add(Chunk.NEWLINE);
                       }
                    }
                    /*document.add(new Chunk(entity.getInfo(), fontChinese));
                    document.add(Chunk.NEWLINE);*/
                //}
            }
            document.add(tPhrase);
        }

        Image img1 = Image.getInstance(path+"/static/pis.png");
        img1.setAlignment(Image.ALIGN_CENTER);
        img1.setBorder(Image.BOX);
        img1.setBorderWidth(10);
        img1.setBorderColor(BaseColor.WHITE);
        img1.scaleToFit(1500, 8);// 大小
        document.add(img1);

        if(detailList != null && detailList.size() > 0){
            Phrase tPhrase = new Phrase();
            tPhrase.setLeading(22f);
            for(OnlinePdfEntity entity : detailList){
                if(entity.getInfo() != null && !entity.getInfo().equals("")){//有内容则显示在简历上
                    //Paragraph pragraph=new Paragraph(entity.getRemark(), fontChinese);
                    Chunk chunk1 = new Chunk(entity.getRemark(),fontChinese1);
                    tPhrase.add(chunk1);
                    Chunk chunk2 = new Chunk(new Chunk(entity.getInfo(), fontChinese2));
                    tPhrase.add(chunk2);
                    tPhrase.add(Chunk.NEWLINE);
                }
            }
            document.add(tPhrase);
        }
        document.close();
        //return new File(newPDFPath);
        byte[] b = baos.toByteArray();//pdf字节数组
        return b;
    }

    protected byte[] getImg(String photourl) {
        try {
            URL url = new URL(photourl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //默认是get请求   如果想使用post必须指明
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            int code = connection.getResponseCode();
            if(code == 200){
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100];
                int rc = 0;
                while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                    byteArrayOutputStream.write(buff, 0, rc);
                }
                return byteArrayOutputStream.toByteArray();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    // inputStream转outputStream
    public ByteArrayOutputStream parse(final InputStream in) throws Exception {
        final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream;
    }
    public static InputStream byteByUrl(String urlOrPath) throws IOException {
        InputStream in = null;
        byte[] bytes;
        if (urlOrPath.toLowerCase().startsWith("https")) {
            //bytes = HttpsUtils.doGet(urlOrPath);
            bytes = null;
        } else if (urlOrPath.toLowerCase().startsWith("http")) {
            URL url = new URL(urlOrPath);
            return url.openStream();
        } else {
            File file = new File(urlOrPath);
            if (!file.isFile() || !file.exists() || !file.canRead()) {
                return null;
            }
            return new FileInputStream(file);
        }
        return new ByteArrayInputStream(bytes);
    }

}
