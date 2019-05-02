/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: RemarkService
 * Author:   Liubing
 * Date:     2018/5/9 15:45
 * Description: 备注查询service
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.info.back.service;

import com.info.web.util.DataSourceContextHolder;
import com.info.back.dao.IRemarkDao;
import com.info.back.vo.jxl.Remark;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈备注查询service〉
 *
 * @author Liubing
 * @create 2018/5/9
 * @since 1.0.0
 */

@Service
public class RemarkServiceImpl implements IRemarkService {

    @Resource
    private IRemarkDao remarkDao;

    @Override
    public List<Remark> queryBorrowRemark(String userId) {
        DataSourceContextHolder.setDbType("dataSourcexjx");
        List<Remark> remarks = remarkDao.queryBorrowRemark(userId);
        DataSourceContextHolder.setDbType("dataSourcecs");
        return remarks;
    }
}
