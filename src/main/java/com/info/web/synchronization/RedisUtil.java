package com.info.web.synchronization;

import com.info.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class RedisUtil {


    @Resource
    JedisCluster jedisCluster;

    /**
     * 删除redis中的key
     */
    public void delRedisKey(String key) {
        try {
            jedisCluster.del(key);
        } catch (Exception e) {
            log.error("RedisUtil delRedisKey", e);
        }
    }

    /**
     * 获取所有的redis数据
     */
    public Set<String> keys(String pattern) {
        log.info("RedisUtil Start getting keys...");
        Set<String> keys = new HashSet<>();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for (String k : clusterNodes.keySet()) {
            log.info("RedisUtil Getting keys from = " + k);
            JedisPool jp = clusterNodes.get(k);
            Jedis connection = jp.getResource();
            try {
                keys.addAll(connection.keys(pattern));
            } catch (Exception e) {
                log.error("RedisUtil Getting keys error: {}", e);
            } finally {
                log.debug("RedisUtil Connection closed.");
                connection.close();
            }
        }
        log.info("RedisUtil cluster Keys gotten!");
        return keys;
    }

    //获取后台推送的订单
    public List<String> getAllCSKeys() {
        List<String> allList = new ArrayList<>();
        List<String> overdue = getAllKeys(Constant.TYPE_OVERDUE_ + "*");
        List<String> renewal = getAllKeys(Constant.TYPE_RENEWAL_ + "*");
        List<String> typeRepay = getAllKeys(Constant.TYPE_REPAY_ + "*");
        List<String> typeWithhold = getAllKeys(Constant.TYPE_WITHHOLD_ + "*");
        if (overdue != null && overdue.size() > 0) {
            allList.addAll(overdue);
        }
        if (renewal != null && renewal.size() > 0) {
            allList.addAll(renewal);
        }
        if (typeRepay != null && typeRepay.size() > 0) {
            allList.addAll(typeRepay);
        }
        if (typeWithhold != null && typeWithhold.size() > 0) {
            allList.addAll(typeWithhold);
        }
        return allList;
    }

    public List<String> getAllKeys(String pattern) {
        List<String> keyList = new ArrayList<>();
        try {
            Set<String> keys = keys(pattern);
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                log.info("RedisUtil key = " + key);
                keyList.add(key);
            }
            return keyList;
        } catch (Exception e) {
            log.error("RedisUtil getAllkeys error {}", e);
            return null;
        }
    }

    public boolean exists(String key) {
        return jedisCluster.exists(key);
    }

}
