package com.jt.common.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class WebAppInitializer 
       extends AbstractAnnotationConfigDispatcherServletInitializer{
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
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
