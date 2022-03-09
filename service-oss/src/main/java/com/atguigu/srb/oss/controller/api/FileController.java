package com.atguigu.srb.oss.controller.api;

import com.atguigu.common.exception.BusinessException;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author lucky845
 * @date 2022年03月06日 16:01
 */
@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/api/oss/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 文件上传
     *
     * @param file   文件
     * @param module 模块
     * @return 文件的url地址
     */
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R upload(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file,

            @ApiParam(name = "module", value = "模块", required = true)
            @RequestParam("module") String module) {

        try {
            // 获取文件输入流
            InputStream inputStream = file.getInputStream();
            // 获取文件的原始文件名
            String originalFilename = file.getOriginalFilename();
            String url = fileService.upload(inputStream, module, originalFilename);
            return R.ok().message("文件上传成功").data("url", url);
        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR, e);
        }
    }

    /**
     * 文件删除
     *
     * @param url 文件url地址
     */
    @ApiOperation("删除OSS文件")
    @DeleteMapping("/remove")
    public R remove(
            @ApiParam(value = "要删除的文件路径", required = true)
            @RequestParam("url") String url) {
        fileService.removeFile(url);
        return R.ok().message("删除成功");
    }

}
