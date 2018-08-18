package com.jt.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.common.vo.CheckBox;
import com.jt.sys.entity.SysRole;
import com.jt.sys.entity.SysUser;

public interface SysRoleDao {
    


	
	List<CheckBox> findObjects();
	
	int updateObject(SysRole entity);
	/**
	 * 基于id查询角色自身信息
	 * @param id
	 * @return
	 */
	SysRole findObjectById(Integer id);
	
	 /**
	  * 保存角色自身信息
	  * @param entity
	  * @return
	  */
	 int insertObject(SysRole entity);
	
	 /**
	  * 基于角色id删除角色自身信息
	  * @param id
	  * @return
	  */
	 int deleteObject(Integer id);
	 
	/**
     * 分页查询角色信息
     * @param startIndex 上一页的结束位置
     * @param pageSize 每页要查询的记录数
     * @return
     */
	 List<SysRole> findPageObjects(
			 @Param("name") String name,
			 @Param("startIndex")Integer startIndex,
			 @Param("pageSize") Integer pageSize);
	 
	 /**
	  * 依据条件查询总记录数(要依据这个值计算总页数)
	  * @param name
	  * 当参数应用在了动态sql中时,这个参数最好使用
	  * (@Param) 注解进行修饰.
	  */
	 int getRowCount(@Param("name")String name);
}
