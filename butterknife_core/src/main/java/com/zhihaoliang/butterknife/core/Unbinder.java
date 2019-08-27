package com.zhihaoliang.butterknife.core;

import android.support.annotation.UiThread;

/**
 * 创建日期：2019-08-27
 * 描述:接触绑定的接口
 * 作者:支豪亮
 */
public interface Unbinder {
    @UiThread
    void unbind();

    Unbinder EMPTY = new Unbinder() {
        @Override
        public void unbind() {

        }
    };
}
