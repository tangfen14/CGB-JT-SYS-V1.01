package com.jt.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SysUserRoleDao {
    
	  /**
	   * 根据用户id查询用户所拥有的角色信息
	   * @param userId
	   * @return
	   */
	  List<Integer> findRoleIdsByUserId(Integer userId);
	
	/**
       * 基于角色id删除用户与角色关系表中的数据
       * @param roleId
       * @return
       */
	  int deleteObjectsByRoleId(
			  Integer roleId);
	  
		/**
		 * 负责将用户与角色的关系数据写入到数据库
		 * @param userId 用户id
		 * @param roleIds 多个角色id
		 * @return
		 */
		int insertObject(
				@Param("userId")Integer userId,
				@Param("roleIds")Integer[]roleIds);
		/**
		 * 定义删除方法,先删除关系数据再添加
		 * @param userId
		 * @return
		 */
		int deleteObjectsByUserId(Integer userId);
		
}
