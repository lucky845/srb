package com.atguigu.srb.core.controller.admin;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.atguigu.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lucky845
 */
@Api(tags = "积分等级管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {

    @Resource
    private IntegralGradeService integralGradeService;

    /**
     * 展示管理员积分等级列表
     */
    @ApiOperation(value = "获取积分等级列表")
    @GetMapping("/list")
    public R listAll() {
        List<IntegralGrade> list = integralGradeService.list();
        return R.ok().data("list", list);
    }

    /**
     * 根据id删除积分等级信息
     *
     * @param id 数据id
     */
    @ApiOperation(value = "根据id删除积分等级", notes = "逻辑删除")
    @DeleteMapping("/remove/{id}")
    public R removeById(
            @ApiParam(name = "id", value = "数据id", required = true, example = "100")
            @PathVariable Long id) {

        boolean result = integralGradeService.removeById(id);

        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    /**
     * 新增积分等级
     *
     * @param integralGrade 积分等级对象
     */
    @ApiOperation(value = "新增积分等级")
    @PostMapping("/save")
    public R save(
            @ApiParam(name = "integralGrade", value = "积分等级对象", required = true)
            @RequestBody IntegralGrade integralGrade) {

        // 如果借款额度为空就手动抛出一个自定义的异常！
//        if (integralGrade.getBorrowAmount() == null) {
//            // BORROW_AMOUNT_NULL_ERROR(-201,"借款额度不能为空")
//            throw new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
//        }
        // 使用断言
        Assert.isNull(integralGrade, ResponseEnum.BORROW_AMOUNT_NULL_ERROR);

        boolean result = integralGradeService.save(integralGrade);

        if (result) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }

    /**
     * 根据id获取积分等级
     *
     * @param id 数据id
     */
    @ApiOperation(value = "根据id获取积分等级")
    @GetMapping("/get/{id}")
    public R getById(
            @ApiParam(name = "id", value = "数据id", required = true)
            @PathVariable Long id) {

        IntegralGrade integralGrade = integralGradeService.getById(id);

        if (integralGrade != null) {
            return R.ok().data("record", integralGrade);
        } else {
            return R.error().message("数据不存在");
        }
    }

    /**
     * 根据id更新积分等级
     *
     * @param integralGrade 积分等级对象
     */
    @ApiOperation(value = "更新积分等级")
    @PutMapping("/update")
    public R updateById(
            @ApiParam(name = "integralGrade", value = "积分等级对象", required = true)
            @RequestBody IntegralGrade integralGrade) {

        boolean result = integralGradeService.updateById(integralGrade);

        if (result) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }

}
