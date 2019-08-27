# Butterknife  
## 1.项目介绍
模仿Butterknife实现View的注入BindView和Click的注入OnClick主要是模仿[使用annotationProcessor打造编译时的注解](https://www.jianshu.com/p/9594d2329392)主要的依赖[javapoet](https://github.com/square/javapoet/releases)  

## 2.项目结构
### 2.1.moduel之间的依赖的关系
![blockchain](https://raw.githubusercontent.com/zhihaoliang/Butterknife/master/image/framework_map.png "依赖关系")  

### 2.2.module介绍
+ 【java lib】butterKnife_annoation：专门存放注解的module  
+ 【java lib】butterKnife_compiler : 专门处理注解的module  
+ 【android lib】butterKnife-core: 使用编译生成的代码并提供api供上层使用

## 3.项目构建
### 3.1.butterKnife_annoation的实现以BindView为例
```java
@Target(ElementType.FIELD)
//SOURCE：在原文件中有效，被编译器丢弃 CLASS：在class文件有效，可能会被虚拟机忽略 RUNTIME：在运行时有效
@Retention(RetentionPolicy.SOURCE)
public @interface BindView {
    int value();
}
```
### 3.2.绑定已经生成代码类的实现
主要通过反射实例化**_ViewBinding 达到初始化View并事项onClick的操作，主要代买如下：
```java
String clsName = cls.getName();
bindingClass = cls.getClassLoader().loadClass(clsName + "_ViewBinding");
constructor.newInstance(target, source);
```
### 3.3.绑定已经生成代码类的实现

#### 3.3.1 代码绑定
在butterKnife-compilermodule的main目录下创建resources/META-INF/services文件夹，再创建javax.annotation.processing.Processor文件，文件中写BindViewProcessor的全类名

