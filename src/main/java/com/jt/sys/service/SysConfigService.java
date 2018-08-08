package com.jt.sys.service;

import com.jt.common.vo.PageObject;
import com.jt.sys.entity.SysConfig;

public interface SysConfigService {
	
	/**
	 * 更新配置信息
	 * @param entity
	 * @return
	 */
	int updateObject(SysConfig entity);
	
	int saveObject(SysConfig entity);
	
	//批量删除
	int deleteObjects(Integer... ids);
	
	/**其中返回值有两部分内容
	 * 1.查询当前页数据
	 * 2.查询总记录数
	 * @param name 查询时的参数名
	 * @param pageCurrent 当前页的页码
	 * @return
	 */
	PageObject<SysConfig> findPageObjects(
		String name,Integer pageCurrent);
}
