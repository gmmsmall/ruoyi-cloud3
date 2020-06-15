package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.dao.AttachmentMapper;
import com.ruoyi.acad.domain.Attachment;
import com.ruoyi.acad.service.IAttachmentService;
/*import com.ruoyi.fdfs.feign.RemoteFdfsService;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description：获奖信息逻辑实现层<br/>
 * CreateTime ：2020年3月20日下午2:54:50<br/>
 * CreateUser：ys<br/>
 */
@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements IAttachmentService {

    @Autowired
    private AttachmentMapper attachmentMapper;

    /*@Autowired
    private RemoteFdfsService remoteFdfsService;*/


    @Override
    public List<Attachment> getModelById(Integer acadId) throws Exception {

        List<Attachment> attachmentList = attachmentMapper.selectList(
                new LambdaQueryWrapper<Attachment>().eq(Attachment::getAcadId, acadId));
        return attachmentList;
    }

    @Override
    public void saveModel(Attachment attachment, Integer acadId) throws Exception {

        attachment.setAcadId(acadId);
        attachmentMapper.insert(attachment);
    }

    @Override
    public void deleteModel(Attachment attachment) throws Exception {

        attachmentMapper.deleteById(attachment);
        //remoteFdfsService.delete(attachment.getAttachmentUrl());
    }

    @Override
    public void deleteModelById(Long id) throws Exception {
        this.attachmentMapper.deleteById(id);
    }
}
