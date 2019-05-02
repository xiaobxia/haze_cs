package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;

@Data
public class FengKong {
	private Integer id;
	/** 风控标签名 */
	private String fkLabel;
	/** 可用状态  "0"可用      "1"禁用 */
	private String status;
	/** 创建日期 */
	private Date createtime;
	/** 修改日期 */
	private Date updatetime;
}
