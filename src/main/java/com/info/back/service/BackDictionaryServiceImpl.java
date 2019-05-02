/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: BackDictionaryService
 * Author:   Liubing
 * Date:     2018/5/11 20:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.info.back.service;

import com.info.back.dao.IBackDictionaryDao;
import com.info.web.pojo.cspojo.BackDictionary;
import com.info.web.util.DataSourceContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Liubing
 * @create 2018/5/11
 * @since 1.0.0
 */
@Service
public class BackDictionaryServiceImpl implements IBackDictionaryService {

    @Resource
    private IBackDictionaryDao backDictionaryDao;

    @Override
    public Map<String, Object> findDictionary(String dictionary) {
        DataSourceContextHolder.setDbType("dataSourcexjx");
        List<BackDictionary> dictionaries = backDictionaryDao.findDictionary(dictionary);
        Map<String,Object> map = BackDictionary.getDictionaryTranslateMap(dictionaries);
        DataSourceContextHolder.setDbType("dataSourcecs");
        return map;
    }
}
