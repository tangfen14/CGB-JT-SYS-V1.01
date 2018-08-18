package com.jt.sys.service;

import java.util.Map;

import com.jt.common.vo.PageObject;
import com.jt.sys.entity.SysUser;
import com.jt.sys.vo.SysUserDeptResult;

public interface SysUserService {
	
	  /**
	   * 基于用户id查找用户信息,部门信息以及角色信息
	   * @param id 用户id
	   * @return
	   */
	  Map<String,Object> findObjectById(Integer id);
	
	 PageObject<SysUserDeptResult> findPageObjects(String name,Integer pageCurrent);
	 
	 int validById(Integer id,
			 Integer valid,
			 String modifiedUser);

	 
	 int saveObject(SysUser entity, Integer[] roleIds);
	 
	 int updateObject(SysUser entity,
				Integer[] roleIds);

}
