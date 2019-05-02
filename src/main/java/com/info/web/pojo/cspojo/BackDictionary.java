/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: BackDictionary
 * Author:   Liubing
 * Date:     2018/5/11 16:25
 * Description: 字典类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.info.web.pojo.cspojo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈字典类〉
 *
 * @author Liubing
 * @create 2018/5/11
 * @since 1.0.0
 */

@Slf4j
public class BackDictionary implements Serializable {


    private String dataValue;

    private String dataName;

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public static Map<String, Object> getDictionaryTranslateMap(List<BackDictionary> backDictionaries) {
        Map<String, Object> dicMap = new HashMap<>();
        if (backDictionaries == null || backDictionaries.size() == 0) {
            log.warn("getDictionaryTranslateMap参数错误");
            return dicMap;
        }
        for (BackDictionary backDictionary : backDictionaries) {
            if (StringUtils.isNotEmpty(backDictionary.getDataValue())) {
                dicMap.put(backDictionary.getDataValue(), backDictionary.getDataName());
            }
        }
        return dicMap;
    }
}
