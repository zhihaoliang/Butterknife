package com.zhihaoliang.butterknife.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.code.Symbol;
import com.zhihaoliang.annoation.OnClick;
import com.zhihaoliang.butterknife.compiler.bean.BindViewBean;
import com.zhihaoliang.butterknife.compiler.bean.ElementBean;
import com.zhihaoliang.butterknife.compiler.util.Log;
import com.zhihaoliang.annoation.BindView;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * 创建日期：2019-08-27
 * 描述:用于生成要绑定的类的核心代码
 * 作者:支豪亮
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)

public class BindProcessor extends AbstractProcessor {

    private final static String TARGET = "target";

    private final static String VIEW = "view";

    private final static String UNBIND = "unbind";

    private final static String UITHREAD = "android.support.annotation.UiThread";

    private Filer mFiler;

    private Elements mElementUtils;

    private Log log;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        Messager messager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
        log = new Log(messager);
        log.e("=========ini==t11711============");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        log.e("=========proce11ss010 ============");
        if (set != null && !set.isEmpty()) {
            Map<String, ElementBean> map = new HashMap<>();
            initBindViewBean(roundEnvironment, map, 0);
            initBindViewBean(roundEnvironment, map, 1);

            for (Map.Entry<String, ElementBean> entry : map.entrySet()) {
                String key = entry.getKey();
                ElementBean elementBean = entry.getValue();
                generateViewBinding(key, elementBean, map);
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();

        annotations.add(OnClick.class);
        annotations.add(BindView.class);

        return annotations;
    }

    private Class<? extends Annotation> getTypeByState(int state) {
        switch (state) {
            case 0:
                return BindView.class;
            default:
                return OnClick.class;
        }
    }

    private void initBindViewBean(RoundEnvironment roundEnvironment, Map<String, ElementBean> map, int state) {
        Set<? extends Element> bindViewElementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(getTypeByState(state));

        for (Element element : bindViewElementsAnnotatedWith) {
            Element classElement = element.getEnclosingElement();
            TypeElement typeElement = (TypeElement) classElement;//父节点MainActivity，是类节点，强转
            TypeMirror superMirror = typeElement.getSuperclass();//父类 BaseActivity
            String superClassName = superMirror.toString();//父类全类名

            String className = classElement.toString();
            ElementBean elementBean = map.get(className);

            if (elementBean == null) {
                elementBean = new ElementBean();
                map.put(className, elementBean);
                //添加父类节点
                if (!superClassName.startsWith("android.") && !superClassName.startsWith("java.")) {
                    elementBean.setSuperClass(superClassName);
                }
            }

            if (elementBean.getBindViewBeans() == null) {
                List<BindViewBean> list = new ArrayList<>();
                elementBean.setBindViewBeans(list);
            }

            initBindViewBean(element,state,elementBean.getBindViewBeans());
        }
    }

    private void initBindViewBean(Element element, int state, List<BindViewBean> list) {
        switch (state) {
            case 0:
                BindViewBean bindViewBean = new BindViewBean();
                bindViewBean.setFiledName(element.getSimpleName().toString());
                bindViewBean.setId(element.getAnnotation(BindView.class).value());
                list.add(bindViewBean);
                break;
            default:
                int[] ids = element.getAnnotation(OnClick.class).value();
                if (ids == null || ids.length == 0) {
                    return;
                }

                Integer[] removeDuplicate = removeDuplicate(ids);

                for (Integer id : removeDuplicate) {
                    bindViewBean = initBindViewBean(list, id);
                    bindViewBean.setMethodName(element.getSimpleName().toString());
                    Symbol.MethodSymbol symbol = (Symbol.MethodSymbol) element;
                    bindViewBean.setParam(symbol.params != null && !symbol.params.isEmpty());
                    bindViewBean.setId(id);
                }
                break;
        }
    }


    //用于去除重复的字段
    public Integer[] removeDuplicate(int[] arr) {
        Set<Integer> set = new HashSet();
        for (int i : arr) {
            set.add(i);
        }
        Integer[] result = new Integer[set.size()];
        return set.toArray(result);
    }


    private BindViewBean initBindViewBean(List<BindViewBean> list, Integer id) {
        for (BindViewBean bindViewBean : list) {
            if (bindViewBean.getId().intValue() == id.intValue()) {
                return bindViewBean;
            }
        }
        BindViewBean bindViewBean = new BindViewBean();
        list.add(bindViewBean);
        return bindViewBean;
    }

    private void generateViewBinding(String key, ElementBean elementBean, Map<String, ElementBean> map) {
        TypeElement typeElement = mElementUtils.getTypeElement(key);

        List<BindViewBean> bindViewBeans = elementBean.getBindViewBeans();

        //生成参数 final MainActivity target
        ParameterSpec targetParameterSpec = ParameterSpec
                .builder(ClassName.get(typeElement), TARGET, Modifier.FINAL)
                .build();

        //生成参数  View source
        ParameterSpec viewParameterSpec = ParameterSpec.builder(ClassName.bestGuess("android.view.View"), VIEW)
                .build();

        //生成构造函数
        MethodSpec.Builder constructorMethodBuilder = MethodSpec.constructorBuilder()
                .addParameter(targetParameterSpec)
                .addParameter(viewParameterSpec)
                .addAnnotation(ClassName.bestGuess(UITHREAD))
                .addModifiers(Modifier.PUBLIC);

        if (hasSuperClass(elementBean.getSuperClass(), map)) {
            constructorMethodBuilder.addStatement("super($L, $L)", TARGET, VIEW);
        }

        //  构造函数中添加代码块
        constructorMethodBuilder.addStatement("this.$L = $L", TARGET, TARGET);

        for (BindViewBean bindViewBean : bindViewBeans) {
            String filedName = bindViewBean.getFiledName();
            if (!isEmpty(filedName)) {
                constructorMethodBuilder.addStatement("$L.$L = $L.findViewById($L)", TARGET, filedName, VIEW, bindViewBean.getId());
            }
            String methodName = bindViewBean.getMethodName();

            if (isEmpty(methodName)) {
                continue;
            }

            if (isEmpty(filedName)) {
                filedName = String.format("%s.findViewById(%d)",VIEW, bindViewBean.getId());
            }else{
                filedName = String.format("%s.%s",TARGET,filedName);
            }

            TypeSpec typeSpec = generateAnonymousInnerClasses(bindViewBean);
            constructorMethodBuilder.addStatement("$L.setOnClickListener($L)", filedName, typeSpec);
        }

        //创建构造函数
        MethodSpec constructor = constructorMethodBuilder.build();

        //生成unbind方法
        MethodSpec.Builder unbindMethodSpec = MethodSpec.methodBuilder(UNBIND)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        for (BindViewBean bindViewBean : bindViewBeans) {

            String filedName = bindViewBean.getFiledName();
            if (isEmpty(filedName)) {
                continue;
            }

            String methodName = bindViewBean.getMethodName();

            if (!isEmpty(methodName)) {
                constructorMethodBuilder.addStatement("$L.$L setOnClickListener(null))", TARGET, filedName);
            }
            unbindMethodSpec.addStatement("target.$L = null", filedName);
        }

        if (hasSuperClass(elementBean.getSuperClass(), map)) {
            unbindMethodSpec.addStatement("super.unbind()");
        }

        unbindMethodSpec.addStatement("this.$L = null", TARGET);


        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(getSimpleName(key) + "_ViewBinding")
                .addField(ClassName.get(typeElement), TARGET, Modifier.PRIVATE)
                .addMethod(constructor)
                .addMethod(unbindMethodSpec.build())
                .addModifiers(Modifier.PUBLIC);

        //如果有父类则继承父类
        if (isEmpty(elementBean.getSuperClass())) {
            typeSpec.addSuperinterface(ClassName.bestGuess("com.zhihaoliang.butterknife.core.Unbinder"));
        } else {
            String baseClassNameString = elementBean.getSuperClass() + "_ViewBinding";
            ClassName baseClassName = ClassName.bestGuess(baseClassNameString);
            typeSpec.superclass(baseClassName);
        }

        //获取包名
        String packageName = mElementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec.build())
                .build();

        try {
            //写入java文件
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            log.e(e.toString());
        }

    }

    private String getSimpleName(String key) {
        int index = key.lastIndexOf(".");
        return key.substring(index + 1);
    }


    private TypeSpec generateAnonymousInnerClasses(BindViewBean bindViewBean) {
        TypeElement typeElement = mElementUtils.getTypeElement("android.view.View.OnClickListener");

        MethodSpec.Builder build = MethodSpec.methodBuilder("onClick")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.bestGuess("android.view.View"), VIEW);

        if (bindViewBean.isParam()) {
            build.addStatement("target.$L($L)", bindViewBean.getMethodName(), VIEW);
        } else {
            build.addStatement("target.$L()", bindViewBean.getMethodName());
        }

        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName.get(typeElement.asType()))
                .addMethod(build.build())
                .build();
    }


    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    private boolean hasSuperClass(String className, Map<String, ElementBean> map) {
        if (isEmpty(className)) {
            return false;
        }

        return map.get(className) != null;
    }
}