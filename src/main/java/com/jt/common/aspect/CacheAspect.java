package com.jt.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**假设需要缓存,此项目没有实现
 * 将查询到的结果缓存起来
 *自写切面,用于将来在缓存中取数据
 */
@Aspect
@Component
public class CacheAspect {//缓存切面,...
	//@Pointcut("@annotation(com.jt.common.anno.RequestCache)")
	public void logPointCut(){}
    //@Around("logPointCut()")
    public Object around(ProceedingJoinPoint //连接点
    		jointPoint) throws Throwable{
    	//从缓存取,缓存有则直接返回缓存数据,return
    	//缓存没有数据,则执行查询
    	Object result=jointPoint.proceed();
    	//将查询结果存储到缓存.
    	return result;
    }
}
