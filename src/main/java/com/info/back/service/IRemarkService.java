/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: IRemarkService
 * Author:   Liubing
 * Date:     2018/5/9 15:44
 * Description: 备注查询service
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.info.back.service;

import com.info.back.vo.jxl.Remark;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈备注查询service〉
 *
 * @author Liubing
 * @create 2018/5/9
 * @since 1.0.0
 */
public interface IRemarkService {
    List<Remark> queryBorrowRemark(String userId);
}
