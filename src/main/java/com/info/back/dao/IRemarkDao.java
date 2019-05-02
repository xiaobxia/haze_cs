/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: IRemarkDao
 * Author:   Liubing
 * Date:     2018/5/8 11:22
 * Description: 客服备注dao层
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.info.back.dao;

import com.info.back.vo.jxl.Remark;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈客服备注dao层〉
 *
 * @author Liubing
 * @create 2018/5/8
 * @since 1.0.0
 */
@Repository
public interface IRemarkDao {

    List<Remark> queryBorrowRemark(String userId);
}
