package com.atguigu.common.exception;

import com.atguigu.common.result.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 断言类
 *
 * @author lucky845
 * @date 2022年03月03日 14:37
 */
@Slf4j
public class Assert {

    /**
     * 断言对象不为空
     * 如果对象obj为空，则抛出异常
     *
     * @param obj          待判断对象
     * @param responseEnum 异常信息
     */
    public static void notNull(Object obj, ResponseEnum responseEnum) {
        if (obj == null) {
            log.info("obj is null...............");
            throw new BusinessException(responseEnum);
        }
    }


    /**
     * 断言对象为空
     * 如果对象obj不为空，则抛出异常
     *
     * @param obj          待判断对象
     * @param responseEnum 异常信息
     */
    public static void isNull(Object obj, ResponseEnum responseEnum) {
        if (obj != null) {
            log.info("obj is not null......");
            throw new BusinessException(responseEnum);
        }
    }

    /**
     * 断言表达式为真
     * 如果不为真，则抛出异常
     *
     * @param expression 是否成功
     */
    public static void isTrue(boolean expression, ResponseEnum responseEnum) {
        if (!expression) {
            log.info("fail...............");
            throw new BusinessException(responseEnum);
        }
    }

    /**
     * 断言两个对象不相等
     * 如果相等，则抛出异常
     *
     * @param m1           待判断对象
     * @param m2           待判断对象
     * @param responseEnum 异常信息
     */
    public static void notEquals(Object m1, Object m2, ResponseEnum responseEnum) {
        if (m1.equals(m2)) {
            log.info("equals...............");
            throw new BusinessException(responseEnum);
        }
    }

    /**
     * 断言两个对象相等
     * 如果不相等，则抛出异常
     *
     * @param m1           待判断对象
     * @param m2           待判断对象
     * @param responseEnum 异常信息
     */
    public static void equals(Object m1, Object m2, ResponseEnum responseEnum) {
        if (!m1.equals(m2)) {
            log.info("not equals...............");
            throw new BusinessException(responseEnum);
        }
    }

    /**
     * 断言参数不为空
     * 如果为空，则抛出异常
     *
     * @param s            待判断对象
     * @param responseEnum 异常信息
     */
    public static void notEmpty(String s, ResponseEnum responseEnum) {
        if (StringUtils.isEmpty(s)) {
            log.info("is empty...............");
            throw new BusinessException(responseEnum);
        }
    }

}
