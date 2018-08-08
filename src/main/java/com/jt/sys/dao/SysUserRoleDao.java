package com.jt.sys.dao;

public interface SysUserRoleDao {
      /**
       * 基于角色id删除用户与角色关系表中的数据
       * @param roleId
       * @return
       */
	  int deleteObjectsByRoleId(
			  Integer roleId);
}
