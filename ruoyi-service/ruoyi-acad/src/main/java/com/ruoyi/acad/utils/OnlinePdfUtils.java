package com.ruoyi.acad.utils;

import cn.hutool.core.collection.CollUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ruoyi.acad.domain.OnlinePdfEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        writer.setPageEvent(new BackGroundImage(path+"/static/earthz.png"));
        //Step 3: 打开文档对象
        document.open();

        BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        //BaseFont bfChinese = BaseFont.createFont("src/main/resources/static/malgun.ttf",BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font fontChinese1 = new Font(bfChinese, 13,Font.NORMAL);
        Font fontChinese2 = new Font(bfChinese, 12,Font.NORMAL);
        Font fontChinese = new Font(bfChinese, 16,Font.BOLD);
        fontChinese.setColor(BaseColor.ORANGE);
        fontChinese1.setColor(BaseColor.WHITE);
        fontChinese2.setColor(BaseColor.WHITE);
        Chunk chunkAcad = new Chunk("院士简历",fontChinese);
        chunkAcad.setTextRise(33);
        Paragraph paragraph = new Paragraph(chunkAcad);
        paragraph.setAlignment(1);
        document.add(paragraph);

        //人物图片
        if(imagePath != null && !imagePath.equals("")){
            String imageStr = "";
            if(imagePath.contains("http")){
                imageStr = imagePath;
            }else{
                imageStr = "http://"+imagePath;
            }
            byte[] bb = this.getImg(imageStr);
            if(bb == null){
                document.add(this.getImageByUrl(path+"/static/tongyong.jpg"));
            }else{
                document.add(this.getImageByUrl(imageStr));
            }
        }else{
            document.add(this.getImageByUrl(path+"/static/tongyong.jpg"));
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
                               chunkname.setTextRise(10);
                               tPhrase.add(chunkname);
                               if(entity.getInfo() != null){
                                   Chunk chunknamea = new Chunk(new Chunk(entity.getInfo(), fontChinese2));
                                   chunknamea.setTextRise(10);
                                   tPhrase.add(chunknamea);
                               }

                               tPhrase.add(Chunk.NEWLINE);
                               break;
                           case "授衔机构 ：" :
                               Chunk chunkorg = new Chunk(entity.getRemark(),fontChinese1);
                               chunkorg.setTextRise(7);
                               tPhrase.add(chunkorg);
                               if(entity.getInfo() != null){
                                   Chunk chunkorga = new Chunk(new Chunk(entity.getInfo(), fontChinese2));
                                   chunkorga.setTextRise(7);
                                   tPhrase.add(chunkorga);
                               }

                               tPhrase.add(Chunk.NEWLINE);
                               break;
                           case "研究领域 ：" :
                               Chunk chunkorgtype = new Chunk(entity.getRemark(),fontChinese1);
                               chunkorgtype.setTextRise(6);
                               tPhrase.add(chunkorgtype);
                               if(entity.getInfo() != null){
                                   Chunk chunkorgtypea = new Chunk(new Chunk(entity.getInfo(), fontChinese2));
                                   chunkorgtypea.setTextRise(6);
                                   tPhrase.add(chunkorgtypea);
                               }

                               tPhrase.add(Chunk.NEWLINE);
                               break;
                           case "年龄 ： " :
                               Chunk chunkage = new Chunk(entity.getRemark(),fontChinese1);
                               chunkage.setTextRise(5);
                               tPhrase.add(chunkage);
                               if(entity.getInfo() != null){
                                   Chunk chunkagea = new Chunk(new Chunk(entity.getInfo(), fontChinese2));
                                   chunkagea.setTextRise(5);
                                   tPhrase.add(chunkagea);
                               }

                               tPhrase.add(Chunk.NEWLINE);
                               tPhrase.add(Chunk.NEWLINE);
                               break;
                           /*case "国籍 ： " :
                               Chunk chunkcontr = new Chunk(entity.getRemark(),fontChinese1);
                               chunkcontr.setTextRise(4);
                               tPhrase.add(chunkcontr);
                               if(entity.getInfo() != null){
                                   Chunk chunkcontra = new Chunk(new Chunk(entity.getInfo(), fontChinese2));
                                   chunkcontra.setTextRise(4);
                                   tPhrase.add(chunkcontra);
                               }

                               tPhrase.add(Chunk.NEWLINE);
                               break;*/
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

        /*Image img1 = Image.getInstance(path+"/static/pis.png");
        img1.setAlignment(Image.ALIGN_CENTER);
        img1.setBorder(Image.BOX);
        img1.setBorderWidth(10);
        img1.setBorderColor(BaseColor.WHITE);
        img1.scaleToFit(1500, 8);// 大小
        document.add(img1);*/

        if(detailList != null && detailList.size() > 0){
            for(OnlinePdfEntity entity : detailList){
                if(entity.getInfo() != null && !entity.getInfo().equals("")){//有内容则显示在简历上
                    Phrase tPhrase = new Phrase();
                    tPhrase.setLeading(22f);
                    tPhrase.add(new Chunk(entity.getRemark(),fontChinese1));
                    tPhrase.add(Chunk.NEWLINE);
                    document.add(tPhrase);
                    switch(entity.getRemark()){
                        case "授衔机构 ： ":
                            Map<String,String> map = new HashMap<>();
                            map.put("0","序号");
                            map.put("1","授衔科学院");
                            map.put("2","授衔年份");
                            map.put("3","是否外籍");
                            map.put("4","个人网页");
                            PdfPTable pdfPTable = this.getPdfPTable(5,map,entity.getTableInfo());
                            document.add(pdfPTable);
                            break;
                        case "教育 ： ":
                            Map<String,String> mapEdu = new HashMap<>();
                            mapEdu.put("0","序号");
                            mapEdu.put("1","毕业时间");
                            mapEdu.put("2","学校");
                            mapEdu.put("3","学历");
                            PdfPTable pdfPTableEdu = this.getPdfPTable(4,mapEdu,entity.getTableInfo());
                            document.add(pdfPTableEdu);
                            /*Phrase tPhraseE = new Phrase();
                            tPhraseE.add(new Chunk(new Chunk(entity.getInfo(), fontChinese2)));
                            tPhraseE.add(Chunk.NEWLINE);
                            tPhraseE.add(Chunk.NEWLINE);
                            document.add(tPhraseE);*/
                            break;
                        case "工作 ： ":
                            Map<String,String> mapWork = new HashMap<>();
                            mapWork.put("0","序号");
                            mapWork.put("1","工作起始时间");
                            mapWork.put("2","工作结束时间");
                            mapWork.put("3","工作单位名称（中）");
                            mapWork.put("4","工作单位名称(英)");
                            mapWork.put("5","职务");
                            PdfPTable pdfPTableWork = this.getPdfPTable(6,mapWork,entity.getTableInfo());
                            document.add(pdfPTableWork);
                            break;
                        case "荣誉 ： ":
                            Map<String,String> mapAward = new HashMap<>();
                            mapAward.put("0","序号");
                            mapAward.put("1","奖项名称");
                            mapAward.put("2","奖项类别");
                            mapAward.put("3","获奖时间");
                            /*mapAward.put("4","获奖介绍");*/
                            PdfPTable pdfPTableAward = this.getPdfPTable(4,mapAward,entity.getTableInfo());
                            document.add(pdfPTableAward);
                            break;
                        case "论文 ： ":
                            Map<String,String> mapPaper = new HashMap<>();
                            mapPaper.put("0","序号");
                            mapPaper.put("1","论文标题");
                            mapPaper.put("2","摘要");
                            mapPaper.put("3","论文发表时间");
                            mapPaper.put("4","论文发表刊物名称");
                            mapPaper.put("5","刊物级别");
                            mapPaper.put("6","URL");
                            PdfPTable pdfPTablePaper = this.getPdfPTable(7,mapPaper,entity.getTableInfo());
                            document.add(pdfPTablePaper);
                            break;
                        case "专利 ： ":
                            Map<String,String> mapPatent = new HashMap<>();
                            mapPatent.put("0","序号");
                            mapPatent.put("1","发明/专利名称");
                            mapPatent.put("2","发表时间");
                            mapPatent.put("3","国家权威专利局网站");
                            mapPatent.put("4","URL");
                            PdfPTable pdfPTablePatent = this.getPdfPTable(5,mapPatent,entity.getTableInfo());
                            document.add(pdfPTablePatent);
                            break;
                        default:
                            Phrase tPhraseD = new Phrase();
                            tPhraseD.add(new Chunk(new Chunk(entity.getInfo(), fontChinese2)));
                            tPhraseD.add(Chunk.NEWLINE);
                            document.add(tPhraseD);
                            break;
                    }
                }
            }
        }
        document.close();
        //return new File(newPDFPath);
        byte[] b = baos.toByteArray();//pdf字节数组
        return b;
    }

    /**
     * 返回简历表格中具体显示的院士信息
     * @param column 列数
     * @param map 标题头
     * @param list 表格内容
     * @return
     */
    public PdfPTable getPdfPTable(int column, Map<String,String> map,List<Map<String,Object>> list) throws Exception{
        // 内容字体
        BaseFont bfComic = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);//支持中文
        Font font = new Font(bfComic, 12, Font.NORMAL);//标题
        Font font2 = new Font(bfComic, 10, Font.NORMAL);//内容
        font.setColor(BaseColor.WHITE);
        font2.setColor(BaseColor.WHITE);
        PdfPTable pdfPTable = new PdfPTable(column);
        pdfPTable.setWidthPercentage(99);
        PdfPCell pdfPCell;
        pdfPTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        //遍历表格标题头
        for(int i = 0;i < column;i++){
            pdfPCell = new PdfPCell(new Phrase(map.get(String.valueOf(i)),font));
            pdfPCell.setMinimumHeight(40);
            pdfPCell.setUseAscender(true);
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
            pdfPCell.setBackgroundColor(new BaseColor(0,51,153));
            pdfPTable.addCell(pdfPCell);
        }
        //遍历表格中的数据
        if(CollUtil.isNotEmpty(list)){
            for(Map m : list){
                for(int i = 0;i < column;i++){
                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(m.get(String.valueOf(i))),font2));
                    pdfPCell.setMinimumHeight(33);
                    pdfPCell.setUseAscender(true);
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPTable.addCell(pdfPCell);
                }
            }
        }

        return pdfPTable;
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

    public Image getImageByUrl(String url) throws Exception{
        Image img = Image.getInstance(this.getImg(url));
        img.setAlignment(Image.RIGHT);
        img.setBorder(Image.BOX);
        img.setBorderWidth(10);
        img.setBorderColor(BaseColor.WHITE);
        img.scaleToFit(120, 180);// 大小
        img.setAbsolutePosition(430,660);
        return img;
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
