package com.atguigu.srb.oss.service;

import java.io.InputStream;

/**
 * 文件上传功能
 *
 * @author lucky845
 */
public interface FileService {

    /**
     * 蒋文件上传至阿里云
     *
     * @param inputStream 输入流
     * @param module      文件夹名
     * @param fileName    文件名
     * @return 阿里云文件的绝对路径
     */
    String upload(InputStream inputStream, String module, String fileName);

    /**
     * 文件删除
     *
     * @param url 文件url地址
     */
    void removeFile(String url);
}
