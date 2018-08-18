package com.jt.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jt.common.vo.Node;
import com.jt.sys.entity.SysMenu;

public interface SysMenuDao {
	/**
	 * 查询所有菜单以及上级菜单信息(菜单名) 
	 * 思考为何将数据信息封装到map中?
	 * 答:分析数据特点:需求查询当前菜单以及上一级菜单,比如这里当前菜单中有一个name,
	 * 上一级菜单中也有一个name,这两个名字如何封装呢,如果写个实体类来封装的话,实体类应该是和表中的字段是一一对应的,
	 * 而这里做不到(比如实体类中要有parentName这个属性,但是表中都是name),所以这次的封装封装到了一个Map中,K值为属性名,V值为属性值
	 * 一行记录对应一个map,多个map放list
	 */
	List<Map<String,Object>> findObjects();
	
	/**
	 * 基于菜单id统计子菜单的个数
	 * @param id
	 * @return
	 */
	int getChildCount(Integer id);
	
	/**
	 * 基于菜单id执行删除操作
	 * @param id
	 * @return
	 */
	int deleteObject(Integer id);
	
	/**
	 * 查询菜单节点,此信息会在客户端
	 * 的zTree对象上进行呈现.
	 * @return
	 */
	List<Node> findZtreeMenuNodes();
	
	/**
	 * 将对象存入数据库
	 * @param entity
	 * @return
	 */
	int insertObject(SysMenu entity);
	
	int updateObject(SysMenu entity);
	
	/**5.	Shiro 框架授权流程应用
	 * 基于菜单id查找权限标识信息
	 * @param menuIds
	 * @return
	 */
	List<String> findPermissions(
			@Param("menuIds") Integer... menuIds);
}
