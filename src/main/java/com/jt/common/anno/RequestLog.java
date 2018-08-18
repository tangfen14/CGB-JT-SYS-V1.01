package com.jt.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 自定义的一个注解,注解是一个特殊的类
 * 注解可以理解成一个源数据,因为它是一个用来描述数据的对象
 * 比如我们之前用到的注解为什么可以写在方法上,或者这个注解在什么时有效的,都时下面两个中设置
 * @Target 用于描述自定义的注解能够描述哪些对象
 * @Retention 用于描述自定义的注解何时有效,自写注解一般都是运行时有效
 * 比如:@Override这个注解就是在编译的时候有效
 */
@Retention(RetentionPolicy.RUNTIME)//表示运行时有效
@Target(ElementType.METHOD)//表示此类可以描述方法
public @interface RequestLog {//RequestLog.class
	//添加属性   默认字符串的值是"未定义操作名称"
	String value() default "未定义操作名称";
	 //.....还可以写很多属性
}






