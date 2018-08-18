package com.jt.sys.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jt.common.anno.RequestLog;
import com.jt.common.vo.PageObject;
import com.jt.common.vo.ServiceException;
import com.jt.sys.dao.SysConfigDao;
import com.jt.sys.entity.SysConfig;
import com.jt.sys.service.SysConfigService;

@Service
@Transactional//如果需要此类中所有方法都加上事务,那么将此注解移到类上
public class SysConfigServiceImpl implements SysConfigService {
	@Autowired
	private SysConfigDao sysConfigDao;
	
	
	@Transactional(readOnly=true,//只读事务
			timeout=30,//超时时间,在查询时,查询时间超30s,那么该事务就不在执行,默认值是-1(即无限等待)
			isolation=Isolation.READ_COMMITTED)//隔离级别,READ_COMMITTED表示只读取别人已提交的事务
	//多个事务并发执行时,可能导致"脏读,不可重复读,幻读"的问题,为防止问题,可以采用隔离级别去控制,其他级别见下面文档详情
			//rollbackFor=Throwable.class)此属性是设置事务在什么情况的异常下回滚,默认为运行时异常回滚,这样写表示所有异常都回滚
			
	/*当类上有整体的事务注释时,类中单独的方法不需要加入事务,那么可以如此标记为只读事务
	 * 只读事务与读写事务有何不同?
	 * 只读事务的锁是个轻量级的锁,轻量级的锁不会在并发性能上有太多的影响,比如此方法为查询,
	 * 就是大家都可以去读,不会锁住太多对象,但是如果是插入或者是修改数据的同时,那么就要控制别人此时不能修改数据,
	 * 因此这种操作的方法就应该标注为读写事务,@Transactional这个注解的默认就为readOnly=false,
	*/
	@RequestLog("分页查询配置")//自定义注解,详情查看SysLogAspect.中的解析
	@Override
	public PageObject<SysConfig> findPageObjects(
		String name, Integer pageCurrent) {
		//1.参数的合法性校验
		if(pageCurrent==null||pageCurrent<1)
		throw new IllegalArgumentException("当前页码值无效");
		//2.查询总记录数;
		int rowCount=sysConfigDao.getRowCount(name);
		//3.验证总记录数(假如总记录数为0,则抛出异常)
		if(rowCount==0)
	    throw new ServiceException("系统中没有找到对应数据");
		//4.查询当前页数据(配置信息)
		int pageSize=2;//自定义的
		int startIndex=(pageCurrent-1)*pageSize;//计算的
		List<SysConfig> pageRecords=
		sysConfigDao.findPageObjects(name,
				startIndex, pageSize);
		//5.封装数据并返回.
		PageObject<SysConfig> pageObject=
				new PageObject<SysConfig>();
		pageObject.setPageCurrent(pageCurrent);
		pageObject.setPageSize(pageSize);
		pageObject.setRowCount(rowCount);
		pageObject.setRecords(pageRecords);
		return pageObject;
	}

	@RequestLog("删除配置")
	@Override
	public int deleteObjects(Integer... ids) {
		//1.验证有效性
		if(ids==null||ids.length==0)
		throw new IllegalArgumentException("必须选中要删除的内容");
		//2.执行删除操作
		int rows=0;
		try{
		rows=sysConfigDao.deleteObjects(ids);
		}catch(Throwable e){
		e.printStackTrace();
		//给运维人员发短信(拓展)
		throw new RuntimeException("系统修复中");
		}
		//3.判定结果并返回
		if(rows==0)
		throw new ServiceException("对应的记录已经不存在");
		return rows;
	}


	//一种形式是:Controller-->访问
	//还有一种是:RPC(跨进程访问,就是我们要访问第三方进程,就叫跨进程访问)-->访问
	//@Transactional//如果需要此类中所有方法都加上事务,那么将此注解移到类上
	//上行事务注解解析:在spring中配置好事务后,spring底层有一个已经写好的切面,检测到此方法上有此注解修饰时,
	//那么它会在此方法开始之前开启事务,方法执行后提交事务,如果方法过程中出现异常了,那么会回滚事务,回滚下行有案例解析.
	//事务为业务操作,因此加在业务层
	@Override
	public int saveObject(SysConfig entity) {
		//1.合法校验(还可以添加,比如名字不能重复呀,其他数据的格式呀,等等)
		if(entity==null)
		throw new IllegalArgumentException("保存对象不能为空");
		//注意StringUtils这个工具类用springframework包中的(拓展如果今后需要写一个自己的字符串工具类的话可以去查这个工具类然后模仿)
		if(StringUtils.isEmpty(entity.getName()))
		throw new IllegalArgumentException("参数名不能为空");
		if(StringUtils.isEmpty(entity.getValue()))
		throw new IllegalArgumentException("参数值不能为空");
		//2.保存数据
		int rows=0;
		try{
		rows=sysConfigDao.insertObject(entity);
		}catch(Throwable e){
		e.printStackTrace();
		//给运维人员发短信.
		throw new ServiceException("系统故障,正在修复中");
		}
		
		//回滚事务案例解析:下行代码模拟运行时异常的事务回滚.
		//如果没有加@Transactional事务,那么当异常时,报出弹框保存失败,但是添加的这条数据依然会被加入到数据库中
		//当遇到多个操作时,其中有一个操作失败了,那么这个事务就应该回滚,再比如在保存角色以及角色和菜单的关系数据时,如果有任意一条
		//保存执行失败,那么理应两条数据都回滚.因此在那个业务层也应该加入事务注解
		/*if(rows==1)//应该是成功了,这里为演示回滚事务
		throw new ServiceException("保存失败");*/
		
		
		//3.返回更新的行数
		return rows;
	}
	
	
	//Controller-->访问
	//RPC-->访问
	@RequestLog("更新配置")
	@Override
	public int updateObject(SysConfig entity) {
		//1.合法校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("参数名不能为空");
		if(StringUtils.isEmpty(entity.getValue()))
			throw new IllegalArgumentException("参数值不能为空");
		//2.保存数据
		int rows=0;
		try{
			rows=sysConfigDao.updateObject(entity);
		}catch(Throwable e){
			e.printStackTrace();
			//给运维人员发短信.
			throw new ServiceException("系统故障,正在修复中");
		}
		//3.返回更新的行数
		return rows;
	}
}
