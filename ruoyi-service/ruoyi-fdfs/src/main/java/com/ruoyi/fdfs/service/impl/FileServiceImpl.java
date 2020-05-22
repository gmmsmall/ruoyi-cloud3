package com.ruoyi.fdfs.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.ruoyi.fdfs.domain.*;
import com.ruoyi.fdfs.service.FileService;
import com.ruoyi.fdfs.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈文件接口实现〉
 *
 * @author xxxx
 * @create 2019/7/1
 * @since 1.0.0
 */
@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Autowired
    private AppendFileStorageClient appendFileStorageClient;
    @Autowired
    private RedisService redisService;

    @Override
    public RespMsgBean checkFile(Long userId, String fileMd5) throws RedisConnectException {
        if (StringUtils.isEmpty(fileMd5)) {
            return RespMsgBean.failure("fileMd5不能为空");
        }
        if (userId == null) {
            return RespMsgBean.failure("userId不能为空");
        }
        String userIdStr = userId.toString();
        CheckFileDto checkFileDto = new CheckFileDto();


        //模拟从mysql中查询文件表的md5,这里从redis里查询
        List<String> fileList = redisService.getListAll(UpLoadConstant.completedList);
        if (CollUtil.isNotEmpty(fileList)) {
            for (String e : fileList) {
                JSONObject obj = JSONUtil.parseObj(e);
                if (obj.get("md5").equals(fileMd5)) {
                    checkFileDto.setTotalSize(obj.getLong("length"));
                    checkFileDto.setViewPath(obj.getStr("url"));
                    return RespMsgBean.success(checkFileDto);
                }
            }
        }

        // 查询是否有相同md5文件已存在,已存在直接返回
//        FileDo fileDo = fileDao.findOneByColumn("scode", fileMd5);
//        if (fileDo != null) {
//            FileVo fileVo = doToVo(fileDo);
//            return RespMsgBean.success("文件已存在", fileVo);
//        } else {
        // 查询锁占用
        String lockName = UpLoadConstant.currLocks + fileMd5;
        long lock = redisService.incrBy(lockName, 1);
        String lockOwner = UpLoadConstant.lockOwner + fileMd5;
        String chunkCurrkey = UpLoadConstant.chunkCurr + fileMd5;
        if (lock > 1) {
            checkFileDto.setLock(1);
            // 检查是否为锁的拥有者,如果是放行
            String oWner = redisService.get(lockOwner);
            if (StringUtils.isEmpty(oWner)) {
                return RespMsgBean.failure("无法获取文件锁拥有者");
            } else {
                if (oWner.equals(userIdStr)) {
                    String chunkCurr = redisService.get(chunkCurrkey);
                    if (StringUtils.isEmpty(chunkCurr)) {
                        return RespMsgBean.failure("无法获取当前文件chunkCurr");
                    }
                    checkFileDto.setChunkCurr(Convert.toInt(chunkCurr));
                    return RespMsgBean.success("", null);
                } else {
                    return RespMsgBean.failure("当前文件已有人在上传,您暂无法上传该文件");
                }
            }
        } else {
            // 初始化锁.分块
            redisService.set(lockOwner, userIdStr);
            // 第一块索引是0,与前端保持一致
            redisService.set(chunkCurrkey, "0");
            checkFileDto.setChunkCurr(0);
            return RespMsgBean.success("验证成功", null);
        }
//        }
    }

    @Override
    public RespMsgBean uploadBigFileChunk(MultipartFile file, Long userId, String fileMd5, String fileName, Integer chunks, Integer chunk, Integer chunkSize, String bizId, String bizCode) throws RedisConnectException {
//        ServiceAssert.isTrue(!file.isEmpty(), 0, "文件不能为空");
//        ServiceAssert.notNull(userId, 0, "用户id不能为空");
//        ServiceAssert.isTrue(StringUtils.isNotBlank(fileMd5), 0, "文件fd5不能为空");
//        ServiceAssert.isTrue(!"undefined".equals(fileMd5), 0, "文件fd5不能为undefined");
//        ServiceAssert.isTrue(StringUtils.isNotBlank(fileName), 0, "文件名称不能为空");
//        ServiceAssert.isTrue(chunks != null && chunk != null && chunkSize != null, 0, "文件块数有误");
        // 存储在fastdfs不带组的路径
        String noGroupPath = "";
        logger.info("当前文件的Md5:{}", fileMd5);
        String chunkLockName = UpLoadConstant.chunkLock + fileMd5;

        // 真正的拥有者
        boolean currOwner = false;
        Integer currentChunkInFront = 0;
        try {
            if (chunk == null) {
                chunk = 0;
            }
            if (chunks == null) {
                chunks = 1;
            }

            long lock = redisService.incrBy(chunkLockName, 1);
            if (lock > 1) {
                logger.info("请求块锁失败");
                return RespMsgBean.failure("请求块锁失败");
            }
            // 写入锁的当前拥有者
            currOwner = true;

            // redis中记录当前应该传第几块(从0开始)
            String currentChunkKey = UpLoadConstant.chunkCurr + fileMd5;
            String currentChunkInRedisStr = redisService.get(currentChunkKey);
            logger.info("当前块的大小:{}", chunkSize);
            if (StringUtils.isEmpty(currentChunkInRedisStr)) {
                logger.info("无法获取当前文件chunkCurr");
                return RespMsgBean.failure("无法获取当前文件chunkCurr");
            }
            Integer currentChunkInRedis = Convert.toInt(currentChunkInRedisStr);
            currentChunkInFront = chunk;

            if (currentChunkInFront < currentChunkInRedis) {
                logger.info("当前文件块已上传");
                return RespMsgBean.failure("当前文件块已上传", "001");
            } else if (currentChunkInFront > currentChunkInRedis) {
                logger.info("当前文件块需要等待上传,稍后请重试");
                return RespMsgBean.failure("当前文件块需要等待上传,稍后请重试");
            }

            logger.info("***********开始上传第{}块**********", currentChunkInRedis);
            StorePath path = null;
            if (!file.isEmpty()) {
                try {
                    if (currentChunkInFront == 0) {
                        redisService.set(currentChunkKey, Convert.toStr(currentChunkInRedis + 1));
                        logger.info("{}:redis块+1", currentChunkInFront);
                        try {
                            path = appendFileStorageClient.uploadAppenderFile(UpLoadConstant.DEFAULT_GROUP, file.getInputStream(),
                                    file.getSize(), FileUtil.extName(fileName));
                            // 记录第一个分片上传的大小
                            redisService.set(UpLoadConstant.fastDfsSize + fileMd5, String.valueOf(chunkSize));
                            logger.info("{}:更新完fastDfs", currentChunkInFront);
                            if (path == null) {
                                redisService.set(currentChunkKey, Convert.toStr(currentChunkInRedis));
                                logger.info("获取远程文件路径出错");
                                return RespMsgBean.failure("获取远程文件路径出错");
                            }
                        } catch (Exception e) {
                            redisService.set(currentChunkKey, Convert.toStr(currentChunkInRedis));
                            logger.error("初次上传远程文件出错", e);
                            return RespMsgBean.failure("上传远程服务器文件出错");
                        }
                        noGroupPath = path.getPath();
                        redisService.set(UpLoadConstant.fastDfsPath + fileMd5, path.getPath());
                        logger.info("上传文件 result = {}", path);
                    } else {
                        redisService.set(currentChunkKey, Convert.toStr(currentChunkInRedis + 1));
                        logger.info("{}:redis块+1", currentChunkInFront);
                        noGroupPath = redisService.get(UpLoadConstant.fastDfsPath + fileMd5);
                        if (noGroupPath == null) {
                            logger.info("无法获取已上传服务器文件地址");
                            return RespMsgBean.failure("无法获取已上传服务器文件地址");
                        }
                        try {
                            String alreadySize = redisService.get(UpLoadConstant.fastDfsSize + fileMd5);
                            // 追加方式实际实用如果中途出错多次,可能会出现重复追加情况,这里改成修改模式,即时多次传来重复文件块,依然可以保证文件拼接正确
                            assert alreadySize != null;
                            appendFileStorageClient.modifyFile(UpLoadConstant.DEFAULT_GROUP, noGroupPath, file.getInputStream(),
                                    file.getSize(), Long.parseLong(alreadySize));
                            // 记录分片上传的大小
                            redisService.set(UpLoadConstant.fastDfsSize + fileMd5, String.valueOf(Long.parseLong(alreadySize) + chunkSize));
                            logger.info("{}:更新完fastdfs", currentChunkInFront);
                        } catch (Exception e) {
                            redisService.set(currentChunkKey, Convert.toStr(currentChunkInRedis));
                            logger.error("更新远程文件出错", e);
                            return RespMsgBean.failure("更新远程文件出错");
                        }
                    }
                    if (currentChunkInFront + 1 == chunks) {
                        // 最后一块,清空upload,写入数据库
                        long size = Long.parseLong(Objects.requireNonNull(redisService.get(UpLoadConstant.fastDfsSize + fileMd5)));
                        // 持久化上传完成文件,也可以存储在mysql中
                        noGroupPath = redisService.get(UpLoadConstant.fastDfsPath + fileMd5);
                        String url = UpLoadConstant.DEFAULT_GROUP + "/" + noGroupPath;
                        FileDo fileDo = new FileDo(fileName, url, "", size, bizId, bizCode);
                        fileDo.setCreateUser(userId);
                        fileDo.setUpdateUser(userId);
//                        FileVo fileVo = saveFileDo4BigFile(fileDo, fileMd5);
                        redisService.del(UpLoadConstant.chunkCurr + fileMd5,
                                UpLoadConstant.fastDfsPath + fileMd5,
                                UpLoadConstant.currLocks + fileMd5,
                                UpLoadConstant.lockOwner + fileMd5,
                                UpLoadConstant.fastDfsSize + fileMd5);
                        logger.info("***********正常结束**********");
                        return RespMsgBean.success("success");
                    }
                    if (currentChunkInFront + 1 > chunks) {
                        return RespMsgBean.failure("文件已上传结束");
                    }
                } catch (Exception e) {
                    logger.error("上传文件错误", e);
                    return RespMsgBean.failure("上传错误 " + e.getMessage());
                }
            }
        } finally {
            // 锁的当前拥有者才能释放块上传锁
            if (currOwner) {
                redisService.set(chunkLockName, "0");
            }
        }
        logger.info("***********第{}块上传成功**********", currentChunkInFront);
        return RespMsgBean.success("第" + currentChunkInFront + "块上传成功");
    }

}