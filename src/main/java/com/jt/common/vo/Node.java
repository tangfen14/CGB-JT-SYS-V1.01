package com.jt.common.vo;

import java.io.Serializable;
/**
 * 业务分析:这里也可以跟之前一样放在一个Map中,
 * 但是这里用Node值对象比较好,因为 我们要建立一个zTree树,而在树对象中应该有名字,
 * 有id唯一标识,其中每一个元素都应该有一个parentId来指向它的父元素,用Node值对象,
 * 可以一个Node值对象代表一个树中的节点,
 * 一个节点中至少要包含三部分信息,1.id 2.name 3.parentId,,其它数据在这里没有用处,如果都定义了,在内存中都会初始化为null,那么就会造成浪费
 */
public class Node implements Serializable{
	
	private static final long serialVersionUID = 4351174414771192644L;
	private Integer id;
	private String name;
	private Integer parentId;//这个参数名一定叫parentId,否则后面树插件不认识
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
}
