package com.jt.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SysRoleMenuDao {
	/*
	 * 写Dao的时候,一般一个接口只对应一个表,因为现在我们的系统正在从单体向分布式架构发展,
	 * 如果一个Dao对应多张表的话,后面如果做切分的话,尤其是从业务的角度做水平切分的时候就会有很大的困难,
	 * 而且很少有表与表之间建立物理关系,都是逻辑上的关系,建立物理关系了就拆不开了
	 */
	
/*shiro中有这个可代替它	*//**
	 * 基于角色查询菜单id
	 * @param roleId
	 * @return
	 *//*
	List<Integer> findMenuIdsByRoleId(
			Integer roleId);*/
	
	 
	 /**Shiro 框架授权流程应用
	  * 基于角色id查找菜单id信息
	  * @param roleIds 一个用户可以有多个角色
	  * @return
	  */
	List<Integer> findMenuIdsByRoleId(
				@Param("roleIds")Integer...roleIds);
	
	 /**
	  * 插入角色和菜单的关系数据 ,sys_role_menus这个表,
	  * 一个role_id(角色id)可以对应多个menu_id(菜单id),因此我们menu_id采用数组
	  * @param roleId
	  * @param menuIds
	  * @return
	  */
	 int insertObject(
			 @Param("roleId") Integer roleId,
			 @Param("menuIds")Integer[] menuIds);
	
	/**
	  * 基于菜单id删除角色和菜单关系表中的数据
	  * @param menuId
	  * @return
	  */
	 int deleteObjectsByMenuId(Integer menuId);
	 
	 /**
	  * 基于角色id删除角色菜单关系中的记录
	  * @param roleId
	  * @return
	  */
	 
	 int deleteObjectsByRoleId(Integer roleId);
	 


}
