package com.ruoyi.acad.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.BaseInfoMapper;
import com.ruoyi.acad.dao.OnlineResumeMapper;
import com.ruoyi.acad.domain.*;
import com.ruoyi.acad.enums.PeriodicalType;
import com.ruoyi.acad.form.PhotoForm;
import com.ruoyi.acad.service.IClientAcadService;
import com.ruoyi.acad.service.IOnlineResumeService;
import com.ruoyi.acad.utils.OnlinePdfUtils;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.enums.EducationType;
import com.ruoyi.common.enums.RsfCategoryType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fdfs.feign.RemoteFdfsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author gmm
 */
@Service
@Slf4j
public class OnlineResumeServiceImpl extends ServiceImpl<OnlineResumeMapper, OnlineResume> implements IOnlineResumeService {

    //获取院士简历基础信息
    @Autowired
    private IClientAcadService clientAcadService;

    //文件上传下载
    @Autowired
    private RemoteFdfsService remoteFdfsService;

    @Autowired
    private BaseInfoMapper baseInfoMapper;


    //当前项目端口号
    @Value("${server.port}")
    private String port;

    //当前ip
    @Value("${localIp}")
    private String localIp;

    /**
     * 根据院士编码列表查询简历列表
     * @param acadIdList
     * @return
     */
    @Override
    public List<OnlineResume> getAllByAcadIdList(List<String> acadIdList) {
        List<OnlineResume> list = this.baseMapper.selectList(new QueryWrapper<OnlineResume>()
                .eq("deleteflag",1)
                .in("acadecode",acadIdList));
        return list;
    }

    /**
     * 根据院士编码查看简历
     * @param acadecode
     * @return
     */
    @Override
    public OnlineResume getModelByAcadecode(String acadecode) {
        OnlineResume resume = this.baseMapper.selectOne(new QueryWrapper<OnlineResume>()
                .eq("deleteflag",1)
                .eq("acadecode",acadecode));
        return resume;
    }

    /**
     * 根据院士编码在线生成简历
     * @param acadId
     * @return
     */
    @Override
    public RE generateResume(Integer acadId) {
        RE re = new RE();
        re.setStatus(false);
        OnlinePdfUtils onlinePdfUtils = new OnlinePdfUtils();
        //acadId为院士编码，需要先判断库中该院士是否已生成简历(先删后增操作)
        try{
            //根据院士编码，查询院士的基础信息
            ClientAcad clientAcad = this.clientAcadService.getClientAcadByacadId(acadId);
            if(clientAcad != null && clientAcad.getBaseInfo() != null){
                List<OnlinePdfEntity> list = new ArrayList<>();
                OnlinePdfEntity entity = new OnlinePdfEntity();
                entity.setRemark("姓名 ：");
                String Chinese = " 中文    "+this.getValueByName(clientAcad.getBaseInfo().getCnName())+"\n                  ";
                String enStr = "英文 "+this.getValueByName(clientAcad.getBaseInfo().getEnName());
                String yuanStr = "原文 "+this.getValueByName(clientAcad.getBaseInfo().getRealName());
                String regex = "(.{25})";
                String mac = enStr;
                String ymac = yuanStr;
                mac = mac.replaceAll(regex,"$1\n                             ");
                mac = mac.substring(0,mac.length() );
                ymac = ymac.replaceAll(regex,"$1\n                             ");
                ymac = ymac.substring(0,ymac.length() );
                entity.setInfo(Chinese+mac+"\n                  "+ymac);
                list.add(entity);
                OnlinePdfEntity entity1 = new OnlinePdfEntity();
                entity1.setRemark("授衔机构 ：");
                //授衔机构多个进行拼接
                String aosStr = "";
                List<Map<String,Object>> mapAosList = new ArrayList<>();//授衔机构表格中显示数据
                if(CollUtil.isNotEmpty(clientAcad.getAosList())){
                    for(int i = 0 ; i < clientAcad.getAosList().size(); i++){
                        Map<String,Object> map = new HashMap<>();
                        if(clientAcad.getAosList().get(i).getAosName() != null && clientAcad.getAosList().get(i).getAosName().equals("暂无")){
                            break;
                        }else{
                            int j = 0;
                            map.put(String.valueOf(j),1+i);
                            map.put(String.valueOf(j+1),this.getValueByName(clientAcad.getAosList().get(i).getAosName()));//授衔机构
                            map.put(String.valueOf(j+2),this.getValueByName(clientAcad.getAosList().get(i).getElectedYear()));//授衔年份
                            //是否外籍
                            if(clientAcad.getAosList().get(i).getAosMemberType() != null && clientAcad.getAosList().get(i).getAosMemberType() == 1){
                                map.put(String.valueOf(j+3),"否");
                            }else{
                                map.put(String.valueOf(j+3),"是");
                            }
                            //个人网页链接
                            map.put(String.valueOf(j+4),this.getValueByName(clientAcad.getAosList().get(i).getAcadWebsiteLink()));
                            //只显示授衔结构名称
                            if(i == 0){
                                aosStr = clientAcad.getAosList().get(i).getAosName();
                            }else{
                                aosStr = aosStr + "," + clientAcad.getAosList().get(i).getAosName();
                            }
                            mapAosList.add(map);
                        }
                    }
                }
                entity1.setInfo(" "+aosStr);
                list.add(entity1);
                OnlinePdfEntity entity11 = new OnlinePdfEntity();
                entity11.setRemark("研究领域 ：");
                String categoryStr = "";
                if(clientAcad.getBaseInfo().getRsfCategory() != null){
                    if(RsfCategoryType.getByCode(clientAcad.getBaseInfo().getRsfCategory()) != null){
                        categoryStr = RsfCategoryType.getByCode(clientAcad.getBaseInfo().getRsfCategory()).getMsg();
                    }
                }
                entity11.setInfo(" "+categoryStr);
                list.add(entity11);
                OnlinePdfEntity entity111 = new OnlinePdfEntity();
                entity111.setRemark("年龄 ： ");
                //先显示出生日期，没有出生日期显示出生日期备注
                if(clientAcad.getBaseInfo().getBirthday() != null && StringUtils.isNotEmpty(String.valueOf(clientAcad.getBaseInfo().getBirthday()))){
                    //计算年龄
                    entity111.setInfo(" "+ DateUtil.ageOfNow(DateUtil.parse(clientAcad.getBaseInfo().getBirthday())));
                }else{
                    if(clientAcad.getBaseInfo().getBirthdayRemark() != null){
                        entity111.setInfo(" "+clientAcad.getBaseInfo().getBirthdayRemark());
                    }else{
                        entity111.setInfo(" ");
                    }
                }
                list.add(entity111);
                /*OnlinePdfEntity entitycon = new OnlinePdfEntity();
                entitycon.setRemark("国籍 ： ");
                if(clientAcad.getBaseInfo().getNationPlace() != null){
                    entitycon.setInfo(" "+clientAcad.getBaseInfo().getNationPlace());
                }else{
                    entitycon.setInfo(" ");
                }
                list.add(entitycon);*/

                //简历分隔符下方显示的内容
                List<OnlinePdfEntity> list2 = new ArrayList<>();
                OnlinePdfEntity entity3 = new OnlinePdfEntity();
                entity3.setRemark("教育 ： ");
                List<Education> eduList = clientAcad.getEducationList();
                List<Map<String,Object>> mapEduList = new ArrayList<>();//教育表格中显示数据
                if(eduList != null && eduList.size() > 0){
                    for(int i= 0; i < eduList.size();i++){
                        Map<String,Object> map = new HashMap<>();
                        int j = 0;
                        map.put(String.valueOf(j),1+i);
                        map.put(String.valueOf(j+1),this.getValueByName(eduList.get(i).getGraduationYear())+"年");//毕业时间
                        map.put(String.valueOf(j+2),this.getValueByName(eduList.get(i).getSchool()));//学校
                        map.put(String.valueOf(j+3),EducationType.getByCode(eduList.get(i).getEducation()).getMsg());//学历
                        mapEduList.add(map);

                    }
                }
                if(CollUtil.isNotEmpty(mapEduList)){
                    entity3.setInfo("1");
                }
                entity3.setTableInfo(mapEduList);
                list2.add(entity3);
                OnlinePdfEntity entityaos = new OnlinePdfEntity();
                entityaos.setRemark("授衔机构 ： ");
                if(CollUtil.isNotEmpty(mapAosList)){
                    entityaos.setInfo("1");
                }
                entityaos.setTableInfo(mapAosList);
                list2.add(entityaos);
                /*OnlinePdfEntity entityProfileHand = new OnlinePdfEntity();
                entityProfileHand.setRemark("研究领域 ： ");
                String categoryInfo = "";
                if(StringUtils.isNotEmpty(categoryStr)){
                    categoryInfo = ""+categoryStr;
                }
                if(StringUtils.isNotEmpty(clientAcad.getBaseInfo().getRsfProfile())){
                    categoryInfo += categoryInfo + "\n 介绍："+clientAcad.getBaseInfo().getRsfProfile();
                }
                if(StringUtils.isNotEmpty(clientAcad.getBaseInfo().getRsfInfluence())){
                    categoryInfo += categoryInfo + "\n 影响："+clientAcad.getBaseInfo().getRsfInfluence();
                }
                entityProfileHand.setInfo(categoryInfo);
                list2.add(entityProfileHand);*/
                /*OnlinePdfEntity entity2 = new OnlinePdfEntity();
                entity2.setRemark("简介 ： ");
                entity2.setInfo(clientAcad.getBaseInfo().getPersonalProfileHand());
                list2.add(entity2);*/
                OnlinePdfEntity entitywork = new OnlinePdfEntity();
                entitywork.setRemark("工作 ： ");
                List<Work> workList = clientAcad.getWorkList();
                List<Map<String,Object>> mapWorkList = new ArrayList<>();//工作表格中显示数据
                if(workList != null && workList.size() > 0){
                    for(int i= 0; i < workList.size();i++){
                        Map<String,Object> map = new HashMap<>();
                        int j = 0;
                        map.put(String.valueOf(j),i+1);
                        map.put(String.valueOf(j+1),this.getValueByName(workList.get(i).getJobStartYear()));//工作起始时间
                        map.put(String.valueOf(j+2),this.getValueByName(workList.get(i).getJobEndYear()));//工作结束时间
                        map.put(String.valueOf(j+3),this.getValueByName(workList.get(i).getWorkUnitTrans()));//工作单位名称（中）
                        map.put(String.valueOf(j+4),this.getValueByName(workList.get(i).getWorkUnit()));//工作单位名称(英)
                        map.put(String.valueOf(j+5),this.getValueByName(workList.get(i).getJobTitle()));//职务
                        //map.put(String.valueOf(j+6),workList.get(i).getWorkUnit());//工作单位
                        mapWorkList.add(map);
                    }
                }
                if(CollUtil.isNotEmpty(mapWorkList)){
                    entitywork.setInfo("1");
                }
                entitywork.setTableInfo(mapWorkList);
                list2.add(entitywork);
                OnlinePdfEntity entityawards = new OnlinePdfEntity();
                entityawards.setRemark("荣誉 ： ");
                List<Award> awardsList = clientAcad.getAwardList();
                List<Map<String,Object>> mapAwardList = new ArrayList<>();//荣誉表格中显示数据
                if(awardsList != null && awardsList.size() > 0){
                    for(int i= 0; i < awardsList.size();i++){
                        Map<String,Object> map = new HashMap<>();
                        int j = 0;
                        map.put(String.valueOf(j),i+1);
                        map.put(String.valueOf(j+1),this.getValueByName(awardsList.get(i).getAwardName()));//奖项名称
                        map.put(String.valueOf(j+2),this.getValueByName(awardsList.get(i).getAwardCategory()));//奖项类别
                        map.put(String.valueOf(j+3),this.getValueByName(awardsList.get(i).getAwardYear()));//获奖时间
                        //map.put(String.valueOf(j+4),awardsList.get(i).getAwardProfile());//获奖介绍
                        mapAwardList.add(map);
                    }
                }
                if(CollUtil.isNotEmpty(mapAwardList)){
                    entityawards.setInfo("1");
                }
                entityawards.setTableInfo(mapAwardList);
                list2.add(entityawards);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                OnlinePdfEntity entitypage = new OnlinePdfEntity();
                entitypage.setRemark("论文 ： ");
                List<Paper> pageList = clientAcad.getPaperList();
                List<Map<String,Object>> mapPagerList = new ArrayList<>();//论文表格中显示数据
                if(pageList != null && pageList.size() > 0){
                    for(int i= 0; i < pageList.size();i++){
                        Paper  paper = pageList.get(i);
                        if(paper != null){
                            Map<String,Object> map = new HashMap<>();
                            int j = 0;
                            map.put(String.valueOf(j),i+1);
                            map.put(String.valueOf(j+1),this.getValueByName(paper.getPaperTitle()));//论文标题
                            map.put(String.valueOf(j+2),this.getValueByName(paper.getPaperAbstract()));//摘要
                            if(paper.getPublishedTime() != null){
                                map.put(String.valueOf(j+3),simpleDateFormat.format(paper.getPublishedTime()));//论文发表时间
                            }else{
                                map.put(String.valueOf(j+3),"");//论文发表时间
                            }
                            map.put(String.valueOf(j+4),this.getValueByName(paper.getPaper_publication()));//论文发表刊物名称
                            if(paper.getPeriodical() != null && !paper.getPeriodical().equals("")){
                                map.put(String.valueOf(j+5), PeriodicalType.of(paper.getPeriodical()).getDesc());//刊物级别
                            }else{
                                map.put(String.valueOf(j+5), "");//刊物级别
                            }
                            map.put(String.valueOf(j+6),this.getValueByName(paper.getPaperWebsiteLink()));//URL
                            mapPagerList.add(map);
                        }
                    }
                }
                if(CollUtil.isNotEmpty(mapPagerList)){
                    entitypage.setInfo("1");
                }
                entitypage.setTableInfo(mapPagerList);
                list2.add(entitypage);
                OnlinePdfEntity entitypatent = new OnlinePdfEntity();
                entitypatent.setRemark("专利 ： ");
                List<Patent> patentList = clientAcad.getPatentList();
                List<Map<String,Object>> mapPatentList = new ArrayList<>();//论文表格中显示数据
                if(patentList != null && patentList.size() > 0){
                    for(int i= 0; i < patentList.size();i++){
                        Map<String,Object> map = new HashMap<>();
                        int j = 0;
                        map.put(String.valueOf(j),i+1);
                        map.put(String.valueOf(j+1),this.getValueByName(patentList.get(i).getPatentName()));//发明/专利名称
                        if(patentList.get(i).getGetTime() != null){
                            map.put(String.valueOf(j+2),simpleDateFormat.format(patentList.get(i).getGetTime()));//发表时间
                        }else{
                            map.put(String.valueOf(j+2),"");//发表时间
                        }

                        map.put(String.valueOf(j+3),this.getValueByName(patentList.get(i).getPatentWebsite()));//国家权威专利局网站
                        map.put(String.valueOf(j+4),this.getValueByName(patentList.get(i).getUrl()));//URL
                        mapPatentList.add(map);
                    }
                }
                if(CollUtil.isNotEmpty(mapPatentList)){
                    entitypatent.setInfo("1");
                }
                entitypatent.setTableInfo(mapPatentList);
                list2.add(entitypatent);
                OnlinePdfEntity entityelse = new OnlinePdfEntity();
                entityelse.setRemark("其他 ： ");
                //生活习惯 + 宗教信仰 + 备注
                String otherStr = "";
                if(clientAcad.getBaseInfo().getLivingHabit() != null && clientAcad.getBaseInfo().getLivingHabit().length()>0){
                    otherStr = "\n生活习惯： " + clientAcad.getBaseInfo().getLivingHabit();
                }
                if(clientAcad.getBaseInfo().getReligion() != null && clientAcad.getBaseInfo().getReligion().length() > 0){
                    otherStr = otherStr + "\n宗教信仰： " + clientAcad.getBaseInfo().getReligion();
                }
                if(clientAcad.getBaseInfo().getRemark() != null && clientAcad.getBaseInfo().getRemark().length() > 0){
                    otherStr = otherStr + "\n备注： " + clientAcad.getBaseInfo().getRemark();
                }
                entityelse.setInfo(otherStr);
                list2.add(entityelse);
                String photostr = "";
                //头像显示展厅的头像
                List<PhotoForm> photoList = clientAcad.getPhotoList();
                if(CollUtil.isNotEmpty(photoList)){
                    for(PhotoForm p : photoList){
                        if(p.getIsHall()){//展厅图片
                            photostr = p.getPhotoUrl();
                            break;
                        }
                    }
                }
                if(StringUtils.isNotEmpty(photostr)){
                    String[] photoUrl = photostr.split(",");
                    if(photoUrl != null && !photoUrl.equals("") && photoUrl.length > 1){
                        photostr = photoUrl[1].substring(photoUrl[1].indexOf("\"url\": \"")+8,photoUrl[1].length()-2);
                    }
                }
                //String path = ResourceUtils.getURL("classpath:").getPath();

                log.info("localIp="+this.localIp);
                String hostIp = "";
                if(StringUtils.isNotEmpty(this.localIp) && this.localIp.length() > 2){
                    hostIp = this.localIp.substring(0,this.localIp.lastIndexOf(":"));
                }else{
                    hostIp = this.localIp;
                }
                String pathStr = hostIp+ ":" +this.port;
                log.info("静态资源路径"+pathStr);

                byte[] b = onlinePdfUtils.createpdf(list,photostr,list2,pathStr);
                //将 byte[]转MultipartFile:
                InputStream inputStream = new ByteArrayInputStream(b);
                MultipartFile multipartFile = new MockMultipartFile(acadId+".pdf",acadId+".pdf",ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);

                if(acadId != null){
                    LambdaQueryWrapper<OnlineResume> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(OnlineResume::getAcadecode,String.valueOf(acadId)).eq(OnlineResume::getDeleteflag,"1");
                    List<OnlineResume> resumeList = this.list(queryWrapper);
                    if(resumeList != null && resumeList.size() > 0){//简历已生成，需要先删后增(服务器上的简历不进行删除，以防以后需要寻找之前的生成记录)
                        for(OnlineResume onlineResume : resumeList){
                            onlineResume.setDeleteflag("2");
                            onlineResume.setDeletetime(LocalDateTime.now());
                            /*onlineResume.setDeleteperson(JWTUtil.getUser().getUserName());
                            onlineResume.setDeletepersonid(JWTUtil.getUser().getUserId());*/
                        }
                        this.updateBatchById(resumeList);
                    }
                    Map<String,Object> map = this.remoteFdfsService.upload(multipartFile);

                    String url = "";
                    if(map != null){
                        List<Map<String,Object>> results = (List<Map<String,Object>>)map.get("object");
                        if(CollUtil.isNotEmpty(results)){
                            url = String.valueOf(results.get(0).get("url"));
                        }
                    }

                    log.info("简历url:   "+url);
                    //每次新增时都需要根据院士编码，判断是否存在，若存在则需要先删后增（假删）
                    //OnlineResume------此为存放记录的实体类

                    OnlineResume resume = new OnlineResume();
                    /*resume.setAddpersonid(JWTUtil.getUser().getUserId());
                    resume.setAddperson(JWTUtil.getUser().getUserName());*/
                    resume.setAddtime(LocalDateTime.now());
                    resume.setAcadecode(String.valueOf(acadId));
                    //院士姓名显示顺序：真实姓名、中文名字、英文名字
                    if(org.apache.commons.lang.StringUtils.isNotBlank(clientAcad.getBaseInfo().getRealName())){
                        resume.setAcadename(clientAcad.getBaseInfo().getRealName());
                    }else if(org.apache.commons.lang.StringUtils.isNotBlank(clientAcad.getBaseInfo().getCnName())){
                        resume.setAcadename(clientAcad.getBaseInfo().getCnName());
                    }else if(org.apache.commons.lang.StringUtils.isNotBlank(clientAcad.getBaseInfo().getEnName())){
                        resume.setAcadename(clientAcad.getBaseInfo().getEnName());
                    }
                    resume.setDeleteflag("1");
                    resume.setResumeurl(url);
                    this.save(resume);
                    re.setErrorDesc("生成简历成功");
                    re.setErrorCode(200);
                    re.setStatus(true);
                }else{
                    re.setErrorDesc("生成的简历必须含有院士编码");
                    re.setErrorCode(500);
                }
            }
        }catch(Exception e){
            re.setErrorDesc("生成简历失败");
            re.setErrorCode(500);
            e.printStackTrace();
        }
        return re;
    }

    public String getValueByName(Object object){
        String str = "";
        if(object != null){
            str = String.valueOf(object);
        }
        return str;
    }

    @Override
    public void initResume() {
        List<BaseInfo> list = this.baseInfoMapper.selectList(new QueryWrapper<BaseInfo>().orderByAsc("acad_id"));
        if(CollUtil.isNotEmpty(list)){
            for(BaseInfo info : list){
                if(info.getAcadId() != null && !String.valueOf(info.getAcadId()).equals("")){//院士编码不能为空
                    this.generateResume(info.getAcadId());
                }
            }
        }
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
