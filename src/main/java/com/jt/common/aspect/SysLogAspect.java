package com.jt.common.aspect;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.anno.RequestLog;
import com.jt.common.util.IPUtils;
import com.jt.common.util.ShiroUtils;
import com.jt.sys.dao.SysLogDao;
import com.jt.sys.entity.SysLog;

/**
 * 日志切面
 *
 */
@Order(1)//当存在多个切面的时候,标记顺序
@Aspect
@Component//相当于@service注解
public class SysLogAspect {
	private Logger log=Logger.getLogger(SysLogAspect.class);
    @Autowired
	private SysLogDao sysLogDao;
    
    /*下行代码错误分析:如果"within(com.jt.sys.service..*)"这样写,包括了service下的我们自写的ShiroUserRealm
     * 那么启动服务器时候报错,原因:当检测到配置类中有@EnableAspectJAutoProxy启用AOP注解标识时,
     * 底层会自动为切点处指定的目标对象创建代理对象;而在shiro部分,ShiroUserRealm本来就是AuthorizingRealm的子类,
     * 在AppRootConfig中配置shiro核心權限管理對象时,所用方法的参数为(AuthorizingRealm realm),传入的realm
     * 就是自写的ShiroUserRealm;而在AOP部分产生的动态代理类和AuthorizingRealm并不是同一类型也不是其子类,
     * (注:此处产生代理对象也不是ShiroUserRealm类型或者子类型,是兄弟类型的,平级别关系;重点:当你的对象实现类一个接口的时候,
     * spring底层为这个对象产生的代理对象也会实现此接口,比如A实现了C,那么生成的A的代理对象B也会实现C,因此A和B是兄弟关系;)
     * 所以在配置shiro的时会注入失败,报如下错误
     * Bean named 'shiroUserRealm' is expected to be of type 'org.apache.shiro.realm.AuthorizingRealm'
     * but was actually of type 'com.sun.proxy.$Proxy40'
     */
    //切点表达式有很多种,这里举了2种方式
    // @Pointcut("within(com.jt.sys.service.impl.*)")
    /*下行表示用@annotation所修饰的方法要添加日志监控功能,
     * ()中的类是我们的自定义注解,其中定义了该注解1.为修饰方法的注解2.运行时有效3.字符串属性默认值内容*/
    @Pointcut("@annotation(com.jt.common.anno.RequestLog)")
	public void logPointCut(){}
	
    @Around("logPointCut()")//环绕通知
    public Object around(ProceedingJoinPoint //连接点 ,是JoinPoint的一个子接口
    		jointPoint) throws Throwable{
    	long startTime=System.currentTimeMillis();
    	//执行目标方法(result为目标方法的执行结果)
    	Object result=jointPoint.proceed();
    	long endTime=System.currentTimeMillis();
    	long totalTime=endTime-startTime;
    	//日志输出所得时间
    	log.info("方法执行的总时长为:"+totalTime);
    	//自写方法将日志信息保存起来
    	saveSysLog(jointPoint,totalTime);
    	return result;
    }
    
    private void saveSysLog(ProceedingJoinPoint point,
    		  long totleTime) throws NoSuchMethodException, SecurityException, JsonProcessingException{
    	//1.获取日志信息
    		//-通过point参数获取方法签名
    	MethodSignature ms=
    			(MethodSignature)point.getSignature();
    		//-通过point参数获取执行的类
    	Class<?> targetClass=
    			point.getTarget().getClass();
    			//+获取执行类的类名
    	String className=targetClass.getName();
    			//+获取接口声明的方法
    	String methodName=ms.getMethod().getName();
    			//+通过方法签名获取具体方法再获取接口方法的参数类型
    	Class<?>[] parameterTypes=ms.getMethod().getParameterTypes();
    			//+通过执行类传入两个参数 ,获取目标对象方法
    	Method targetMethod=targetClass.getDeclaredMethod(
    			    methodName,//接口的方法名和目标对象的方法名一样的
    			    parameterTypes);//参数类型
    	
    	
    		//^判定目标方法上是否有RequestLog注解
    	boolean flag=
    			targetMethod.isAnnotationPresent(RequestLog.class);
    	//通过自写工具类获取登录中的用户名
    	String username=
    			ShiroUtils.getPrincipal().getUsername();
    		//通过point获取方法参数(实参)
    	Object[] paramsObj = point.getArgs();
    	System.out.println("paramsObj="+paramsObj);
    		//将参数转换为字符串,  ObjectMapper中有方法可以把一个把对象转换为一个Json串
    	String params=new ObjectMapper().writeValueAsString(paramsObj);
    	//2.封装日志信息
    	SysLog log=new SysLog();
    	log.setUsername(username);//登陆的用户
    		//^假如目标方法对象上有注解,我们获取注解定义的操作值
    	if(flag){
    	RequestLog requestLog=
    	targetMethod.getDeclaredAnnotation(RequestLog.class);
    	log.setOperation(requestLog.value());
    	}
    	log.setMethod(className+"."+methodName+"()");//className.methodName()
    	log.setParams(params);//method params这里我们要拿的是实参,不是形参
    	log.setIp(IPUtils.getIpAddr());//直接借助工具类获取ip 地址
    	log.setTime(totleTime);//
    	log.setCreateDate(new Date());
    	//3.保存日志信息
    	sysLogDao.insertObject(log);
    }
}
