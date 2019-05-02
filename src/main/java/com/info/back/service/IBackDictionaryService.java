/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: IBackDictionaryService
 * Author:   Liubing
 * Date:     2018/5/11 20:48
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.info.back.service;

import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Liubing
 * @create 2018/5/11
 * @since 1.0.0
 */
public interface IBackDictionaryService {
    Map<String,Object> findDictionary(String dictionary);
}
