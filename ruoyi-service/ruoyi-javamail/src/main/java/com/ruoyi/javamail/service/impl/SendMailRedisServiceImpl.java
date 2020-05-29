package com.ruoyi.javamail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.javamail.dao.SendMailRedisMapper;
import com.ruoyi.javamail.entity.SendMailRedis;
import com.ruoyi.javamail.service.ISendMailRedisService;
import org.springframework.stereotype.Service;

/**
 * @author gmm
 */
@Service
public class SendMailRedisServiceImpl extends ServiceImpl<SendMailRedisMapper, SendMailRedis> implements ISendMailRedisService {

}
