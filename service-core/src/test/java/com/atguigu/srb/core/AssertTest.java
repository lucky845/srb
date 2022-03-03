package com.atguigu.srb.core;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

/**
 * @author lucky845
 * @date 2022年03月03日 14:32
 */
public class AssertTest {

    /**
     * if else 的用法
     */
    @Test
    public void test1() {
        Object o = null;
        if (o == null) {
            throw new IllegalArgumentException("参数错误");
        }
    }

    /**
     * 断言的用法: 更为简洁
     */
    @Test
    public void test2() {
        Object o = null;
        Assert.notNull(o, "参数错误");
    }

}
