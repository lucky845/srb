package com.atguigu.srb.core.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.atguigu.common.exception.BusinessException;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;

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

    /**
     * Excel数据的导出
     *
     * @param response 响应正文
     */
    @ApiOperation("Excel数据的导出")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            // 设置响应内容类型
            response.setContentType("application/vnd.ms-excel");
            // 设置编码格式为UTF-8
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encoder可以防止响应正文乱码,和easyExcel没有关系
            String fileName = URLEncoder.encode("MyDict", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 蒋数据写入到文件中
            EasyExcel.write(response.getOutputStream(), ExcelDictDTO.class).sheet("数据字典").doWrite(dictService.listDictData());
        } catch (Exception e) {
            log.error(e.getMessage());
            // EXPORT_DATA_ERROR(104, "数据导出失败")
            throw new BusinessException(ResponseEnum.EXPORT_DATA_ERROR);
        }

    }

}

