package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface DictService extends IService<Dict> {

    /**
     * 读取Excel文件
     *
     * @param inputStream 读取Excel文件的输入流
     */
    void importData(InputStream inputStream);

    /**
     * 导出Excel
     */
    List<ExcelDictDTO> listDictData();

}
