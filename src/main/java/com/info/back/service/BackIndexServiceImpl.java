package com.info.back.service;


import com.info.back.dao.IBackIndexDao;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.IndexPage;
import com.info.web.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

@Slf4j
@Service
public class BackIndexServiceImpl implements IBackIndexService {

    @Resource
    private IBackIndexDao backIndexDao;

    @Resource
    JedisCluster jedisCluster;

    @Override
    public void update(IndexPage zbIndex) {
        backIndexDao.update(zbIndex);
        updateCache();
    }

    @Override
    public IndexPage searchIndex() {
        return backIndexDao.searchIndex();
    }

    /**
     * 刷新首页缓存
     */
    public void updateCache() {
        IndexPage index = searchIndex();// 查询数据库
        try {
            jedisCluster.set(Constant.CACHE_INDEX_KEY, JsonUtil
                    .beanToJson(index));// 查询结果放入缓存
        } catch (Exception e) {
            log.error("initIndex-jedisCluster.set-"
                    + Constant.CACHE_INDEX_KEY + "-exception", e);
        }
    }
}
