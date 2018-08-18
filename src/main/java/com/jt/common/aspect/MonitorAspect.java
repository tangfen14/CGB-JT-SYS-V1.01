package com.jt.common.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * 定义一个监控切面,监控打印出执行业务操作时候的具体类名,时间和方法  的日志
 * 通知结构关系如下：
 * try{
 *   @Before
 *   target.method();//目标对象方法执行
 *   @AfterReturning
 * }catch(Exception e){
 *   @AfterThrowing
 *   throw e;
 * }finally{
 *   @After
 * }
 * 假如有Returning,先执行@After再执行@AfterReturning
 */
@Order(2)
@Aspect//申明为切面
@Service//申明为扩展业务也可以写@Component
public class MonitorAspect {//横切面
	//构建日志对象 ...java.util.logging.Logger
	private Logger log=//传入当前类(切面)的名字
	Logger.getLogger(MonitorAspect.class.getName());
	/**
	 * 定义切入点:PointCut(在哪些点切入扩展功能)
	 * 在当前应用中指的是对哪些方法的执行   进行业务监控
	 * 其中bean为AOP中的一个切入点表达式
	 */
	//@Pointcut("bean(sysConfigServiceImpl)") ,默认的名字就是业务类名首字母小写
    @Pointcut("bean(*ServiceImpl)")//定义切入点,之后执行操作监控*ServiceImpl中的所有方法
	public void monitorPointCut(){}
    
    /**
     * Before通知(切点方法执行之前执行)
     */
    @Before("monitorPointCut()")//注意不要引错包,("")里是切点方法
    public void beforeMethod(JoinPoint joinPoint/*连接点*/){
      log.info(doGetClassMethodInfo(joinPoint)+"  ----1. before method");
    }
    
    /**
     * @AfterReturning 返回正常通知(方法正常结束时执行，after以后执行)
     * @param joinPoint
     */
    @AfterReturning("monitorPointCut()")
    public void afterReturningMethod(JoinPoint joinPoint){
      log.info(doGetClassMethodInfo(joinPoint)+"  ----3. 正常  returning");
    }
    
    /**
     * @AfterThrowing 返回异常通知(方法出现异常时执行,after之后执行)
     * @param joinPoint
     */
    @AfterThrowing("monitorPointCut()")
    public void afterThrowingMethod(JoinPoint joinPoint){
    	log.info(doGetClassMethodInfo(joinPoint)+"  ----3. throwing exception");
    }
    
    /**
     * After通知(切入点方法执行之后执行)
     */
    @After("monitorPointCut()")
    public void afterMethod(JoinPoint joinPoint){
        log.info(doGetClassMethodInfo(joinPoint)+"  ----2. after method");
    }
    
    
    //将通用方法提出
    private String doGetClassMethodInfo(JoinPoint joinPoint){
    	//获取方法签名
    	MethodSignature signature =(MethodSignature)joinPoint.getSignature();
    	//获取目标对象
        Class<?> c=joinPoint.getTarget().getClass();
        String methodName=signature.getName();
    	return c.getName()+"."+methodName;
    }
}
