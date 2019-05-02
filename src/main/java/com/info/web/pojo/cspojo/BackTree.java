package com.info.web.pojo.cspojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 类描述：后台树形结构 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-29 下午02:35:02 <br>
 * 
 */
@Data
public class BackTree implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 节点ID
	 */
	public int id;
	/**
	 * 父节点ID
	 */
	public int pid;
	/**
	 * 节点名称
	 */
	public String name;
	public String file;
	public String url;
	/**
	 * 是否折叠，默认为false
	 */
	public boolean open;
	public boolean isParent;
	public String target;
	public String title;

	/**
	 * 组ID
	 */
	public int gid;

}
