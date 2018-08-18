package com.jt.sys.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jt.common.vo.PageObject;
import com.jt.common.vo.ServiceException;
import com.jt.sys.dao.SysUserDao;
import com.jt.sys.dao.SysUserRoleDao;
import com.jt.sys.entity.SysUser;
import com.jt.sys.service.SysUserService;
import com.jt.sys.vo.SysUserDeptResult;

@Service
public class SysUserServiceImpl implements SysUserService {

	@Resource
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;

	@Override
	public PageObject<SysUserDeptResult> findPageObjects(String username, Integer pageCurrent) {
		// 1.数据合法性验证
		if (pageCurrent == null || pageCurrent <= 0)
			throw new ServiceException("参数不合法");
		// 2.依据条件获取总记录数
		int rowCount = sysUserDao.getRowCount(username);
		if (rowCount == 0)
			throw new ServiceException("记录不存在");
		// 3.计算startIndex的值
		int pageSize = 3;
		int startIndex = (pageCurrent - 1) * pageSize;
		// 4.依据条件获取当前页数据
		List<SysUserDeptResult> records = sysUserDao.findPageObjects(username, startIndex, pageSize);
		// 5.封装数据
		PageObject<SysUserDeptResult> pageObject = new PageObject<>();
		pageObject.setPageCurrent(pageCurrent);
		pageObject.setRowCount(rowCount);
		pageObject.setPageSize(pageSize);
		pageObject.setRecords(records);
		return pageObject;
	}
	
	/**
	 * 访问业务方法需要权限检测时需要添加下面的注解
	 * 并且指定访问该方法时需要的权限标识,
	 * 如下示例,想要执行禁用启用操作,那么需要满足"sys:user:valid"标识;
	 * 当底层系统运行时检测到方法上使用了@RequiresPermissions注解,
     * 就会为业务对象创建一个代理对象,然后在代理对象(com.sun.proxy.$Proxy73)中调用
     * subject.isPermitted("sys:user:valid")方法进行权限检测操作
	 */
	@RequiresPermissions("sys:user:valid")//此注解属于shiro框架的
	@Override
	public int validById(Integer id, Integer valid, String modifiedUser) {
		// 1.合法性验证
		if (id == null || id <= 0)
			throw new ServiceException("参数不合法,id=" + id);
		if (valid != 1 && valid != 0)
			throw new ServiceException("参数不合法,valie=" + valid);
		if (StringUtils.isEmpty(modifiedUser))
			throw new ServiceException("修改用户不能为空");
		// 2.执行禁用或启用操作
		int rows = 0;
		try {
			rows = sysUserDao.validById(id, valid, modifiedUser);
		} catch (Throwable e) {
			e.printStackTrace();
			// 报警,给维护人员发短信
			throw new ServiceException("底层正在维护");
		}
		// 3.判定结果,并返回
		if (rows == 0)
			throw new ServiceException("此记录可能已经不存在");
		return rows;
	}
	


	@Override
	public int saveObject(SysUser entity, Integer[] roleIds) {
		// 1.验证数据合法性
		if (entity == null)
			throw new ServiceException("保存对象不能为空");
		if (StringUtils.isEmpty(entity.getUsername()))
			throw new ServiceException("用户名不能为空");
		if (StringUtils.isEmpty(entity.getPassword()))
			throw new ServiceException("密码不能为空");
		if (roleIds == null || roleIds.length == 0)
			throw new ServiceException("至少要为用户分配角色");

		// 2.将数据写入数据库
		// 下面方法每次产生的值基本上都不同,然后将盐加入实体对象中
		String salt = UUID.randomUUID().toString();
		// 加密(先了解,讲shiro时再说),md5加密的特点:只能加密,不能解密,将盐和密码放在一起进行加密
		SimpleHash sHash = new SimpleHash("MD5",entity.getPassword(),salt);
		entity.setPassword(sHash.toString());
		entity.setSalt(salt);
    	//2.4设置对象其它属性默认值
    	entity.setCreatedTime(new Date());
    	entity.setModifiedTime(new Date());
    	
		// 保存用户自身信息
		int rows = sysUserDao.insertObject(entity);
		// 保存关系数据信息
		sysUserRoleDao.insertObject(entity.getId(), // 通过Mapper中提取的主键
				roleIds);// "1,2,3,4";
		// 3.返回结果
		return rows;
	}

	@Override
	public Map<String, Object> findObjectById(Integer id) {
		// 1.参数有效性验证
		if (id == null || id < 1)
			throw new IllegalArgumentException("id的值无效");
		// 2.查询用户以及部门信息
		SysUserDeptResult user = sysUserDao.findObjectById(id);
		if (user == null)
			throw new ServiceException("此用户可能已经不存在");
		// 3.查询用户对应的角色信息
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(id);
		// 4.封装数据并返回.
		Map<String, Object> map = new HashMap<>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}
	
	@Override
	public int updateObject(SysUser entity,
			Integer[] roleIds) {
		//1.合法验证
	    if(entity==null)
	    throw new ServiceException("用户信息不能为空");
	    if(StringUtils.isEmpty(entity.getUsername()))
	    throw new ServiceException("用户名不能为空");
	    //当用户没有分配角色时弹框.
	    if(roleIds==null||roleIds.length==0)
	    throw new ServiceException("用户必须选一个角色");
	  //假如密码栏用户没有输入值，那么就不更新密码
	    if(!StringUtils.isEmpty(entity.getPassword())){
	    	//对密码加密
	    	String salt=UUID.randomUUID().toString();
	    	//将盐加入到实体对象中
	    	entity.setSalt(salt);
	    	SimpleHash hash=//shiro
	    	new SimpleHash(
	    			"MD5",
	    			entity.getPassword(),
	    			salt);
	    	entity.setPassword(hash.toString());
	    }
		//2.更新数据
	    int rows=0;
	    try{
	    rows=sysUserDao.updateObject(entity);
	    sysUserRoleDao.deleteObjectsByUserId(entity.getId());
	    sysUserRoleDao.insertObject(
	    entity.getId(),roleIds);
	    }catch(Throwable e){
	     e.printStackTrace();
	     //发起报警信息
	     throw new ServiceException("服务端现在异常,请稍后访问");
	    }
		//3.返回结果
		return rows;
	}


}
