package com.atguigu.srb.core.mapper;

import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 批量插入数据到字典
     * @param list 数据集合
     */
    void insertBatch(List<ExcelDictDTO> list);

}
