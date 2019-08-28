package com.zhihaoliang.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建日期：2019-08-27
 * 描述:用也View注释点击操作
 * 作者:支豪亮
 */
@Target(ElementType.FIELD)
//SOURCE：在原文件中有效，被编译器丢弃 CLASS：在class文件有效，可能会被虚拟机忽略 RUNTIME：在运行时有效
@Retention(RetentionPolicy.SOURCE)

public @interface BindView {
    int value();
}
