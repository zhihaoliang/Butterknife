package com.zhihaoliang.butterknife.compiler;

import com.google.auto.service.AutoService;
import com.zhihaoliang.butterknife.compiler.bean.ElementBean;
import com.zhihaoliang.butterknife.compiler.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * 创建日期：2019-08-27
 * 描述:用于生成要绑定的类的核心代码
 * 作者:支豪亮
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.zhihaoliang.butterknife.annoation.OnClick", "com.zhihaoliang.butterknife.annoation.BindView"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)

public class BindProcessor extends AbstractProcessor {

    private Messager mMessager;

    private Filer mFiler;

    private Elements mElementUtils;

    private Log log;

    private Map<String, ElementBean> mMaps = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
        log = new Log(mMessager);
        log.e("=========init============");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        log.e("=========process000 ============");
        return false;
    }
}