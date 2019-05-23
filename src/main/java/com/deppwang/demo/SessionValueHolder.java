package com.deppwang.demo;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author WangXQ
 * @date 5/23/2019 8:19 PM
 */
public class SessionValueHolder {
    private static JedisPool jedisPool;
    private String sessionCacheKey;

    SessionValueHolder() {
        sessionCacheKey = "***";
    }

    public void putValue(String key, Serializable value) {
        putValue(sessionCacheKey, key, value);
        expireKey(key, 1800);
    }

    private void putValue(String sessionCacheKey, String valueKey, Serializable value) {
        Jedis jedis = null;
        boolean flag = true;
        try {
            jedis = getResource();
            jedis.connect();

            jedis.hset(sessionCacheKey.getBytes(), valueKey.getBytes(), toBytes(value));
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (flag) {
                returnResource(jedis);
            } else {
                returnBrokenResource(jedis);
            }
        }
    }

    private void expireKey(String key, int expireSeconds) {
        Jedis jedis = null;
        boolean flag = true;
        try {
            jedis = getResource();
            jedis.expire(key, expireSeconds);
        } catch (Exception e) {
            flag = false;
        } finally {
            if (flag) {
                returnResource(jedis);
            } else {
                returnBrokenResource(jedis);
            }
        }
    }

    public Object getValue(String valueKey) {
        return getValue(sessionCacheKey, valueKey);
    }

    private Object getValue(String sessionCacheKey, String valueKey) {
        Object value = null;
        Jedis jedis = null;
        boolean flag = true;
        try {
            jedis = jedisPool.getResource();

            byte[] data = jedis.hget(sessionCacheKey.getBytes(), valueKey.getBytes());
            if (data != null) {
                value = toObject(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (flag) {
                returnResource(jedis);
            } else {
                returnBrokenResource(jedis);
            }
        }

        return value;
    }

    private static Jedis getResource() throws JedisException {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisException e) {
            returnBrokenResource(jedis);
            throw e;
        }
        return jedis;
    }

    private static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 归还资源
     *
     * @param jedis
     */
    private static void returnBrokenResource(Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnBrokenResource(jedis);
        }
    }

    private static byte[] toBytes(Object object) {
        return SerializeUtil.serialize(object);
    }

    private static Object toObject(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return SerializeUtil.unserialize(bytes);
    }
}
