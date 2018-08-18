package com.jt.common.util;

import org.apache.shiro.SecurityUtils;

import com.jt.sys.entity.SysUser;
/**
 * 获取登录中的用户身份
 */
public class ShiroUtils {

	 public static SysUser getPrincipal(){
		 return (SysUser)SecurityUtils.getSubject().getPrincipal();
	 }
}
