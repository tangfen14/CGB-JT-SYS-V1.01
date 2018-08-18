package com.jt.common.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class WebAppInitializer 
       extends AbstractAnnotationConfigDispatcherServletInitializer{
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		//super.onStartup(servletContext);//此ost方法中先注册了监听器,然后注册了servlet
		
		//也可以这样写,调用父类的注册监听器方法和注册servlet的方法,然后在中间加入我们的自写方法注册过滤器
		//注意这个顺序,先监听,然后过滤,然后注册servlet
			//调用父类方法注册监听器
		registerContextLoaderListener(servletContext);
			//注册过滤器(自写)
		registerFilter(servletContext);
			//调用父类方法注册springmvc前端控制器(servlet)
		registerDispatcherServlet(servletContext);
	}
	
	private void registerFilter(ServletContext servletContext) {
		//注册Filter过滤器对象
		//什么时候需要采用此方式进行注册?
		//项目没有web.xml并且此filter不是自己写的
		FilterRegistration.Dynamic dy=
		servletContext.addFilter("filterProxy",//这个是给过滤器起的名字
				DelegatingFilterProxy.class);//我们构建的dy对象就是此类型的对象,它是由shiro提供的,它是一个过滤器,
		//它拦截到所有的请求以后,会对请求做一个初步的处理,并且调用指定的bean(下面的"shiroFilterFactoryBean")相关的方法,
		//来对请求中的参数做一些基本设置和封装
		
		
		//通过dy来设置,第一设置初始化参数,第二个设置拦截所有的请求
		//下方法第一个参数targetBeanName不能变,对应的是构建的dy对象的set方法,
		//初始化参数名shiroFilterFactoryBean必须和AppRootConfig.java中的"配置Shiro的過濾器Bean工廠"的名字一致
		dy.setInitParameter("targetBeanName","shiroFilterFactoryBean");
		
		dy.addMappingForUrlPatterns(
				null,//EnumSet<DispatcherType>,派发的类型不用去管直接传一个空值和false,由底层自动的确定参数的类型
				false,"/*");//url-pattern,/*表示拦截所有的路径
}


	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{AppRootConfig.class};
	}

	
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{AppServletConfig.class};
	}
	@Override
	protected String[] getServletMappings() {
		return new String[]{"*.do"};
	}
}
