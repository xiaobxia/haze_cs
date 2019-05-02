package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.List;

/**
 * 
 * 类描述：系统菜单类 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-27 下午06:01:20 <br>
 * 
 *
 */
@Data
public class BackModule {

	private Integer id;

	private String moduleName;

	private String moduleUrl;
	private String moduleStyle;

	private String moduleDescribe;
	private Integer moduleSequence;
	private Integer moduleView;

	private Integer moduleParentId;

	private List<BackModule> moduleList;

}