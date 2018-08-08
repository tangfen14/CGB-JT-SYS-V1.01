package com.jt.sys.service;

import java.util.Map;

import com.jt.common.vo.PageObject;
import com.jt.sys.entity.SysRole;

public interface SysRoleService {
	
	/**
	  * 基于角色id查询对应的菜单
	  * @param id
	  * @return 返回值是两部分数据,因此用Map来存储.
	  * 业务分析:查询分两部分,一部分是角色自身信息,另一部分是角色对应的菜单信息.
	  * 在控制层拿到这个map后,将map转换成Json串传到客户端,客户端从这个串中取到其中的值
	  */
	 Map<String,Object> findObjectById(
			 Integer id) ;
	
	
	 int updateObject(
			 SysRole entity,
			 Integer[] menuIds);
	 
	 int saveObject(
			 SysRole entity,
			 Integer[] menuIds);
	
	/**
	 * 基于id执行删除操作
	 * @param id
	 * @return
	 */
	 int deleteObject(Integer id);
	
	 PageObject<SysRole> findPageObjects(
			 String name,
			 Integer pageCurrent);
}
