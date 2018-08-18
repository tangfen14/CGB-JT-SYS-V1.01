package com.jt.sys.service.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jt.common.anno.RequestLog;
import com.jt.sys.dao.SysMenuDao;
import com.jt.sys.dao.SysRoleMenuDao;
import com.jt.sys.dao.SysUserDao;
import com.jt.sys.dao.SysUserRoleDao;
import com.jt.sys.entity.SysUser;

@Service
public class ShiroUserRealm extends AuthorizingRealm {
    @Autowired
	private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private SysMenuDao sysMenuDao;
    
    /**
     * 设置凭证(Credentials)匹配器
     * 默认就是"MD5一次加密"此方法不写也可以
     */
	@Override
	public void setCredentialsMatcher(
        CredentialsMatcher credentialsMatcher) {
		HashedCredentialsMatcher cMatcher=
				new HashedCredentialsMatcher();
		cMatcher.setHashAlgorithmName("MD5");
		//设置加密的次数(这个次数应该与保存密码时那个加密次数一致)
		//cMatcher.setHashIterations(5);
		super.setCredentialsMatcher(cMatcher);
	}
	/**负责完成认证领域信息的获取以及封装
	 * 说明:此方法由认证管理器调用*/
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
	     AuthenticationToken token) throws AuthenticationException {
		//token中封装了用户信息的对象(它有个子类UsernamePasswordToken)
		System.out.println("==doGetAuthenticationInfo===");
		//1.从token对象获取用户名(用户输入的),必须强转才能取出用户名和密码
		UsernamePasswordToken upToken=
				(UsernamePasswordToken)token;
		String username=upToken.getUsername();
		//2.基于用户名查询用户信息并进行身份校验
		SysUser user=
		sysUserDao.findUserByUserName(username);
		if(user==null)
		throw new AuthenticationException("此用户不存在");
		if(user.getValid()==0)
		throw new AuthenticationException("此用户已被禁用");
		//3.对用户信息进行封装
				//获取盐值可以用一个内部的工具类方法转换成框架指定的类型
		ByteSource credentialsSalt=
		ByteSource.Util.bytes(user.getSalt());
		
		SimpleAuthenticationInfo info=
			new SimpleAuthenticationInfo(
				user,//principal 用户身份
				user.getPassword(),//hashedCredentials已加密的凭证
				credentialsSalt,//credentialsSalt 密码加密时使用的盐
				getName());//realmName 当前方法所在对象(ShiroUserRealm对象)的名字
		return info;//返回给认证管理器
	}
	
	
	/**负责完成用户权限领域信息的获取以及封装*/
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
		PrincipalCollection principals) {
		//principals代表多个身份,一个用户会有多个身份,比如指纹,密码虹膜等等
		//目前user是我们的主身份,所有的PrincipalCollection集合中必须有个主身份,一般就指注册时的那个身份
		//就是上面方法中SimpleAuthenticationInfo中传入的那个身份,上面传的什么,下面取什么
		System.out.println("==doGetAuthorizationInfo===");
		//1.获取用户对象(此对应依赖与认证时封装的用户身份)
		SysUser user=(SysUser)principals.getPrimaryPrincipal();
		//2.基于用户id查找角色id
		List<Integer> roleIds=
		sysUserRoleDao.findRoleIdsByUserId(user.getId());
		//3.基于角色id查找菜单id
			//注意下方转换的方式,roleIds.toArray()是个object类型的,
			//而方法中需要的是Integer[] 数组
		Integer[] array={};
		List<Integer>  menuIds=
		sysRoleMenuDao.findMenuIdsByRoleId(
		roleIds.toArray(array));
		//4.基于菜单id查找权限标识
		List<String> permissions=
		sysMenuDao.findPermissions(menuIds.toArray(array));
		//5.对权限标识进行去重和空的操作(因为每个菜单的权限标识有重复的,也有可能在添加菜单的时候没有填写权限标识)
			//set集合可以去空去重
		Set<String> set=new HashSet<String>();
		for(String permission:permissions){
			if(!StringUtils.isEmpty(permission)){
				set.add(permission);
			}
		}
		//5.对权限标识信息进行封装
		SimpleAuthorizationInfo info=
		new SimpleAuthorizationInfo();
		info.setStringPermissions(set);
		return info;//返回给授权管理器对象
	}
}
