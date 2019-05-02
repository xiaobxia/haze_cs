/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: IBackDictionary
 * Author:   Liubing
 * Date:     2018/5/11 16:08
 * Description: 字典表查询
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.info.back.dao;

import com.info.web.pojo.cspojo.BackDictionary;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈字典表查询〉
 *
 * @author Liubing
 * @create 2018/5/11
 * @since 1.0.0
 */
@Repository
public interface IBackDictionaryDao {
    List<BackDictionary> findDictionary(String dictionary);
}
