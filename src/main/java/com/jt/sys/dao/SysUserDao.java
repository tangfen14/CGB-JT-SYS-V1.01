package com.jt.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.sys.entity.SysUser;
import com.jt.sys.vo.SysUserDeptResult;

public interface SysUserDao {
	
	/**
	 * 根据用户名查询用户对象的方法定义(shiro权限)
	 * @param username
	 * @return
	 */
	SysUser findUserByUserName(String username);
	
	
	
	
	
	int updateObject(SysUser entity);
	
	/**
	 * 根据用户id查询用户以及对应的部门信息
	 * @param id (用户id)
	 * @return
	 */
	SysUserDeptResult findObjectById(Integer id);
	
	List<SysUserDeptResult> findPageObjects(
			@Param("username") String username,
			@Param("startIndex")Integer startIndex,
			@Param("pageSize")Integer pageSize);
	
	int getRowCount(@Param("username") String username);
	
	int validById(
			@Param("id")Integer id,
			@Param("valid")Integer valid,
			@Param("modifiedUser")String modifiedUser);

	/**
	 * 负责将用户信息写入到数据库
	 * @param entity
	 * @return
	 */
	int insertObject(SysUser entity);

	

}

