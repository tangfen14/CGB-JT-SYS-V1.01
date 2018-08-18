package com.jt.common.controller;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.JsonResult;
/**
 * 全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	   @ExceptionHandler(RuntimeException.class)
	   @ResponseBody//这个注解,表示将返回值JsonResult转换成json格式的字符串
	   public JsonResult doHandleRuntimeException(
			   RuntimeException e){
		   e.printStackTrace();//打印异常信息,便于维护的时候可以看到具体信息
		   return new JsonResult(e);//封装错误信息,调用错误数据的构造函数,构造函数内部会修改状态码,和消息
	   }
	   
	   /**
	    * 认证操作异常处理
	    * @param e
	    * @return
	    */
	   @ExceptionHandler(ShiroException.class)
	   @ResponseBody
	   public JsonResult doHandleShiroException(
			  ShiroException e){
		   e.printStackTrace();
		   if(e instanceof IncorrectCredentialsException){
			   return new JsonResult(0,"密码不正确");
		   }else if(e instanceof AuthorizationException){
			   return new JsonResult(0,"您没权限执行此操作");
		   }
		   return new JsonResult(e);
	   }
}
