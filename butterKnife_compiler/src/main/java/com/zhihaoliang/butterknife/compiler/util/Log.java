package com.zhihaoliang.butterknife.compiler.util;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * 创建日期：2019-08-27
 * 描述:打印Log的工具类
 * 作者:支豪亮
 */
public class Log {
    private Messager messager;

    public Log(Messager messager) {
        this.messager = messager;
    }

    public void e(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }
}
