package com.jt.sys.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.JsonResult;
import com.jt.common.vo.PageObject;
import com.jt.sys.entity.SysUser;
import com.jt.sys.service.SysUserService;
import com.jt.sys.vo.SysUserDeptResult;

@Controller
@RequestMapping("/user/")
public class SysUserController {
	@Autowired
	private SysUserService sysUserService;
	
	@RequestMapping("doUserListUI")
	public String doUserListUI(){
		return "sys/user_list";
	}
	
	@RequestMapping("doFindPageObjects")
	@ResponseBody
	public JsonResult doFindPageObjects(String name,Integer pageCurrent){
		PageObject<SysUserDeptResult> pageObject = sysUserService.findPageObjects(name, pageCurrent);
		return new JsonResult(pageObject);
	}
	
	@RequestMapping("doValidById")
	@ResponseBody
	public JsonResult doValidById(
			Integer id,
			Integer valid){
		//shiro代码:获取登录用户(主体用户)
		SysUser user = 
				(SysUser)SecurityUtils.getSubject().getPrincipal();
		       
		sysUserService.validById(
				id,
				valid, 
				user.getUsername());//"admin"用户将来是登陆用户
		return new JsonResult("update ok");
	}

	
	@RequestMapping("doUserEditUI")
	public String doUserEditUI(){
		return "sys/user_edit";
	}
	
	@RequestMapping("doSaveObject")
	@ResponseBody
	public JsonResult doSaveObject(SysUser entity,Integer[] roleIds){
		sysUserService.saveObject(entity, roleIds);
		return new JsonResult("save ok");
	}
	
 	  @RequestMapping("doFindObjectById")
 	  @ResponseBody
 	  public JsonResult doFindObjectById(
 			Integer id){
 		Map<String,Object> map=
 		sysUserService.findObjectById(id);
 		return new JsonResult(map);
 	  }

 	  
 		@RequestMapping("doUpdateObject")
 		@ResponseBody
 		public JsonResult doUpdateObject(
 		    SysUser entity,Integer[] roleIds){
 			sysUserService.updateObject(entity,
 					roleIds);
 			return new JsonResult("update ok");
 		}

}

