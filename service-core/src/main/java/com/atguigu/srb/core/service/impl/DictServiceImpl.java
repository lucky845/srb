package com.atguigu.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.srb.core.listener.ExcelDictDTOListener;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author lucky845
 * @since 2022-03-03
 */
@Slf4j
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private RedisTemplate<String, List<Dict>> redisTemplate;

    /**
     * 读取Excel文件
     * 出现异常就回滚
     *
     * @param inputStream 读取Excel文件的输入流
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(InputStream inputStream) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(inputStream, ExcelDictDTO.class, new ExcelDictDTOListener(baseMapper)).sheet().doRead();
        log.info("Excel导入成功");
    }

    /**
     * 导出Excel
     */
    @Override
    public List<ExcelDictDTO> listDictData() {
        List<Dict> dictList = baseMapper.selectList(null);
        //创建ExcelDictDTO列表，将Dict列表转换成ExcelDictDTO列表
        ArrayList<ExcelDictDTO> excelDictDTOList = new ArrayList<>(dictList.size());
        dictList.forEach(dict -> {

            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict, excelDictDTO);
            excelDictDTOList.add(excelDictDTO);
        });
        return excelDictDTOList;
    }

    /**
     * 根据父节点查询所有子节点
     *
     * @param parentId 父节点id
     */
    @Override
    public List<Dict> listByParentId(Long parentId) {

        List<Dict> dictList = null;
        try {
            // 先查询Redis中是否存在数据列表
            dictList = redisTemplate.opsForValue().get("srb:core:dictList:" + parentId);
            if (dictList != null) {
                // 如果存在则从Redis中直接返回数据列表
                log.info("从redis中取值");
                return dictList;
            }
        } catch (Exception e) {
            // 此处不抛出异常，继续执行后面的代码
            log.error("redis服务器异常：" + ExceptionUtils.getStackTrace(e));
        }
        // 若果不存在则查询数据库
        log.info("从数据库中取值");
        dictList = baseMapper.selectList(new QueryWrapper<Dict>().eq("parent_id", parentId));
        dictList.forEach(dict -> {
            // 如果有子节点,则是非叶子节点
            boolean hasChildren = this.hasChildren(dict.getId());
            // 设置是否有子节点
            dict.setHasChildren(hasChildren);
        });

        // 将数据存储到Redis中
        try {
            redisTemplate.opsForValue().set("srb:core:dictList:" + parentId, dictList, 5, TimeUnit.MINUTES);
            log.info("数据存入Redis");
        } catch (Exception e) {
            // 此处不抛出异常，继续执行后面的代码
            log.error("Redis服务器异常: " + ExceptionUtils.getStackTrace(e));
        }
        // 返回数据列表
        return dictList;
    }

    /**
     * 判断当前id下是否有子节点该节点是否有子节点
     *
     * @param id 当前id
     */
    private boolean hasChildren(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<Dict>().eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

}
