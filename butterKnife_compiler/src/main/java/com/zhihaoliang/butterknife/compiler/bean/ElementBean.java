package com.zhihaoliang.butterknife.compiler.bean;

import java.util.List;

/**
 * 创建日期：2019-08-27
 * 描述:方便处理代码生成
 * 作者:支豪亮
 */
public class ElementBean {

    private String superClass;

    private List<BindViewBean> bindViewBeans ;

    public List<BindViewBean> getBindViewBeans() {
        return bindViewBeans;
    }

    public void setBindViewBeans(List<BindViewBean> bindViewBeans) {
        this.bindViewBeans = bindViewBeans;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public String getSuperClass() {
        return superClass;
    }
}
