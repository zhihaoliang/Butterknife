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
### 3.3. 绑定已经生成代码类的实现

#### 3.3.1. 代码绑定运行
在butterKnife-compilermodule的main目录下创建resources/META-INF/services文件夹，再创建javax.annotation.processing.Processor文件，文件中写BindViewProcessor的全类名

#### 3.3.2. 查看gradle的编译log
![blockchain](https://raw.githubusercontent.com/zhihaoliang/Butterknife/master/image/gradle_log.png "示例图") 

#### 3.3.3. butterKnife_compiler debug的设置  

##### 3.3.3.1. remote module 创建
![blockchain](https://raw.githubusercontent.com/zhihaoliang/Butterknife/master/image/go.png "示例图") 

##### 3.3.3.2. remote module 的copy
copy下面红色选中的文章
![blockchain](https://raw.githubusercontent.com/zhihaoliang/Butterknife/master/image/remote.png "示例图") 
##### 3.3.3.3. gradlew 的修改
把上一步copy替换到gradlew文件中
![blockchain](https://raw.githubusercontent.com/zhihaoliang/Butterknife/master/image/gradlew.png "示例图") 

##### 3.3.3.4. 运行Unnamed 然后再选中APP运行就可以或者在项目根目录直接编译“gradlew compileDebugJavaWithJavac”
![blockchain](https://raw.githubusercontent.com/zhihaoliang/Butterknife/master/image/debug.png "示例图") 

### 3.4. 编辑代码的实现  
#### 3.3.1. 依赖的类库
```gradle
implementation project(':butterKnife_annoation')
implementation 'com.google.auto.service:auto-service:1.0-rc6'
implementation 'com.squareup:javapoet:1.11.1'
```
还要加入java的tool.jar的依赖，位置/Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home/lib




