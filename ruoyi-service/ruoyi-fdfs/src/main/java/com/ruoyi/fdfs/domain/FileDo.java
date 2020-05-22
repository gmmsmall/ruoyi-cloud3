package com.ruoyi.fdfs.domain;

import lombok.Data;

@Data
public class FileDo {

    private String fileName;

    private String url;

    private String sl;

    private Long size;

    private String bizId;

    private String bizCode;

    private Long createUser;

    private Long updateUser;

    public FileDo(String fileName, String url, String sl, Long size, String bizId, String bizCode) {
        this.fileName = fileName;
        this.url = url;
        this.sl = sl;
        this.size = size;
        this.bizId = bizId;
        this.bizCode = bizCode;
    }
}
