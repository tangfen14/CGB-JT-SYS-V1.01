package com.jt.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.sys.entity.SysConfig;

public interface SysConfigDao {
	/**
	 * 编辑更新数据
	 * @param entity
	 * @return
	 */
	
	int updateObject(SysConfig entity);
	
	/**
	 * 负责将配置信息持久化
	 * @param entity
	 * @return
	 */
	int insertObject(SysConfig entity);
	
	/**
	 * 基于id执行删除操作
	 * @param integers
	 * @return
	 */
	//当动态sql中使用了方法中的参数,那么这个参数不管是有几个,方法中的参数前面一定要拿@Param修饰并起名
	int deleteObjects(@Param("ids")Integer... ids);
	
	/**
	 * 基于条件查询当前页数据
	 * @param name 查询时输出的参数名
	 * @param startIndex 当前起始位置
	 * @param pageSize 每页最多显示的记录数,页面大小
	 * @return
	 */
	 
	List<SysConfig> findPageObjects(
			@Param("name")String name,//多于一个参数,最好加@Param注解
			@Param("startIndex")Integer startIndex,
			@Param("pageSize")Integer pageSize
			);
	/**
	 * 基于条件查询总记录数
	 * @param name 查询条件
	 * @return
	 */
	//动态sql中的参数要加注解
	int getRowCount(@Param("name")String name);//假如有其他方法在此参数上加了注解,其他方法最好也都加注解
}
