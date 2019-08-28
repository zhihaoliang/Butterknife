package com.zhihaoliang.butterknife.compiler.bean;

/**
 * 创建日期：2019-08-28
 * 描述:
 * 作者:支豪亮
 */
public class BindViewBean {
    private Integer id;

    private String filedName;

    private String methodName;

    private boolean isParam;

    public Integer getId() {
        return id;
    }

    public String getFiledName() {
        return filedName;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean isParam() {
        return isParam;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParam(boolean param) {
        isParam = param;
    }
}
