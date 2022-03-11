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

    /**
     * 根据父节点查询所有子节点
     *
     * @param parentId 父节点id
     */
    List<Dict> listByParentId(Long parentId);

    /**
     * 根据dictCode获取下集节点
     *
     * @param dictCode 节点编码
     */
    List<Dict> findByDictCode(String dictCode);

    /**
     * 根据父字典code和值获取名字
     *
     * @param dictCode 父字典code
     * @param value    值
     */
    String getNameByParentDictCodeAndValue(String dictCode, Integer value);
}
