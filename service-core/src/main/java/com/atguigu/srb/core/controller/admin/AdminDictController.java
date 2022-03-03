package com.atguigu.srb.core.controller.admin;


import com.atguigu.common.exception.BusinessException;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
@Api(tags = "数据字典管理")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin/core/dict")
public class AdminDictController {

    @Resource
    private DictService dictService;

    /**
     * Excel数据的批量导入
     *
     * @param file 数据字典文件
     */
    @ApiOperation("Excel数据的批量导入")
    @PostMapping("/import")
    public R batchImport(
            @ApiParam(name = "file", value = "数据字典文件", required = true)
            @RequestParam("file") MultipartFile file) {

        try {
            InputStream inputStream = file.getInputStream();
            dictService.importData(inputStream);
            return R.ok().message("数据字典批量导入成功");
        } catch (Exception e) {
            //UPLOAD_ERROR(-103, "文件上传错误")
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR, e);
        }
    }

}

