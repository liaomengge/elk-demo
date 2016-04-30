package com.sh.util.redis;

import com.sh.util.serialization.JRedisSerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;

public class JedisUtil {

    private static final Logger _log = Logger.getLogger(JedisUtil.class);
    private static Map<RedisDBEnum, JedisPool> map = new HashMap<RedisDBEnum, JedisPool>();

    private static void initialPoolConfig() {
        final RedisConfig config = new RedisConfig();
        try {
            for (int i = 0, length = config.getDbs().length; i < length; i++) {
                JedisPool jedisPool = new JedisPool(config, config.getIp(), config.getPort(), config.getTimeout(), null, config.getDbs()[i]);
                map.put(RedisDBEnum.getByCode(i), jedisPool);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("init redis failed", e);
        }
    }

    private static synchronized void poolInit() {
        if (null == map || map.isEmpty()) {
            initialPoolConfig();
        }
    }

    public synchronized static Jedis getJedis(RedisDBEnum redisDBEnum) {
        boolean is_echo = false;
        if (null == map || map.isEmpty()) {
            poolInit();
            is_echo = true;
        }
        JedisPool jedisPool = map.get(redisDBEnum);
        Jedis jedis = null;
        try {
            if (null != jedisPool) {
                jedis = jedisPool.getResource();
            }
        } catch (JedisConnectionException e) {
            if (is_echo) {
                _log.error("connection redis exception", e);
            }
            throw e;
        } catch (Exception e) {
            _log.error("get jedis instance failed", e);
        }
        return jedis;
    }

    private static void releaseResource(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }

    public static void destroyPool() {
        if (null != map && !map.isEmpty()) {
            for (Map.Entry<RedisDBEnum, JedisPool> map : map.entrySet()) {
                JedisPool jedisPool = map.getValue();
                if (null != jedisPool) {
                    jedisPool.destroy();
                }
            }
            map.clear();
        }
    }

    public static void setStringKey(String key, byte[] bytes) {
        setStringKey(RedisDBEnum.DB0, key, bytes);
    }

    public static void setStringKey(RedisDBEnum redisDBEnum, String key, byte[] bytes) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            jedis.set(key.getBytes(), bytes);
        } catch (JedisException e) {
            _log.error("for jedis, 'setStringKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
    }

    public static void rpushKey(String key, byte[] bytes) {
        rpushKey(RedisDBEnum.DB0, key, bytes);
    }

    public static void rpushKey(RedisDBEnum redisDBEnum, String key, byte[] bytes) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            jedis.rpush(key.getBytes(), bytes);
        } catch (JedisException e) {
            _log.error("for jedis, 'rpushKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
    }

    public static void lpushKey(String key, byte[] bytes) {
        lpushKey(RedisDBEnum.DB0, key, bytes);
    }

    public static void lpushKey(RedisDBEnum redisDBEnum, String key, byte[] bytes) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            jedis.lpush(key.getBytes(), bytes);
        } catch (JedisException e) {
            _log.error("for jedis, 'lpushKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
    }

    public static List<byte[]> lrangeKey(String key) {
        return lrangeKey(RedisDBEnum.DB0, key);
    }

    public static List<byte[]> lrangeKey(RedisDBEnum redisDBEnum, String key) {
        Jedis jedis = null;
        List<byte[]> list = null;
        try {
            jedis = getJedis(redisDBEnum);
            list = jedis.lrange(key.getBytes(), 0, -1);
        } catch (JedisException e) {
            _log.error("for jedis, 'lrangeKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
        return list;
    }

    public static List<byte[]> lrangeKey(String key, long start, long end) {
        return lrangeKey(RedisDBEnum.DB0, key, start, end);
    }

    public static List<byte[]> lrangeKey(RedisDBEnum redisDBEnum, String key, long start, long end) {
        Jedis jedis = null;
        List<byte[]> list = null;
        try {
            jedis = getJedis(redisDBEnum);
            list = jedis.lrange(key.getBytes(), start, end);
        } catch (JedisException e) {
            _log.error("for jedis, 'lrangeKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
        return list;
    }

    public static void setStringKey(String key, String value) {
        setStringKey(RedisDBEnum.DB0, key, value);
    }

    public static void setStringKey(RedisDBEnum redisDBEnum, String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            jedis.set(key, value);
        } catch (JedisException e) {
            _log.error("for jedis, 'setStringKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
    }

    public static void setListKey(String key, String... value) {
        setListKey(RedisDBEnum.DB0, key, value);
    }

    public static void setListKey(RedisDBEnum redisDBEnum, String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            jedis.lpush(key, value);
        } catch (JedisException e) {
            _log.error("for jedis, 'setListKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
    }

    public static void setSetKey(String key, String... value) {
        setSetKey(RedisDBEnum.DB0, key, value);
    }

    public static void setSetKey(RedisDBEnum redisDBEnum, String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            jedis.sadd(key, value);
        } catch (JedisException e) {
            _log.error("for jedis, 'setSetKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
    }

    public static void setMapKey(String key, Map<String, String> map) {
        setMapKey(RedisDBEnum.DB0, key, map);
    }

    public static void setMapKey(RedisDBEnum redisDBEnum, String key, Map<String, String> map) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            jedis.hmset(key, map);
        } catch (JedisException e) {
            _log.error("for jedis, 'setMapKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
    }

    public static void updateMapElement(String key, String field, String value) {
        updateMapElement(RedisDBEnum.DB0, key, field, value);
    }

    public static void updateMapElement(RedisDBEnum redisDBEnum, String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            jedis.hset(key, field, value);
        } catch (JedisException e) {
            _log.error("for jedis, 'updateMapElement' method call failed");
        } finally {
            releaseResource(jedis);
        }
    }

    public static byte[] getByteByKey(String key) {
        return getByteByKey(RedisDBEnum.DB0, key);
    }

    public static byte[] getByteByKey(RedisDBEnum redisDBEnum, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            if (!jedis.exists(key)) {
                return null;
            }
            return jedis.get(key.getBytes());
        } catch (JedisException e) {
            _log.error("for jedis, 'getByteByKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
        return null;
    }

    public static String getStringByKey(String key) {
        return getStringByKey(RedisDBEnum.DB0, key);
    }

    public static String getStringByKey(RedisDBEnum redisDBEnum, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            if (!jedis.exists(key)) {
                return null;
            }
            return jedis.get(key);
        } catch (JedisException e) {
            _log.error("for jedis, 'getStringByKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
        return null;
    }

    public static List<String> getListBykey(String key) {
        return getListByKey(key, 0l, -1l);
    }

    public static List<String> getListByKey(String key, Long start, long end) {
        return getListByKey(RedisDBEnum.DB0, key, start, end);
    }

    public static List<String> getListByKey(RedisDBEnum redisDBEnum, String key, Long start, long end) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            if (!jedis.exists(key)) {
                return null;
            }
            return jedis.lrange(key, start, end);
        } catch (JedisException e) {
            _log.error("for jedis, 'getListByKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
        return Collections.emptyList();
    }

    public static Set<String> getSetByKey(String key) {
        return getSetByKey(RedisDBEnum.DB0, key);
    }

    public static Set<String> getSetByKey(RedisDBEnum redisDBEnum, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            if (!jedis.exists(key)) {
                return null;
            }
            return jedis.smembers(key);
        } catch (JedisException e) {
            _log.error("for jedis, 'getSetByKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
        return Collections.emptySet();
    }

    public static String getMapByKey(String key, String field) {
        return getMapByKey(RedisDBEnum.DB0, key, field);
    }

    public static String getMapByKey(RedisDBEnum redisDBEnum, String key, String field) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            if (!jedis.exists(key)) {
                return null;
            }
            return jedis.hget(key, field);
        } catch (JedisException e) {
            _log.error("for jedis, 'getMapByKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
        return StringUtils.EMPTY;
    }

    public static byte[] getMapByKey(String key, Object obj) {
        return getMapByKey(RedisDBEnum.DB0, key, obj);
    }

    public static byte[] getMapByKey(RedisDBEnum redisDBEnum, String key, Object obj) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            if (!jedis.exists(key)) {
                return null;
            }
            return jedis.hget(key.getBytes(), JRedisSerializationUtils.fastSerialize(obj));
        } catch (JedisException e) {
            _log.error("for jedis, 'getMapByKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
        return StringUtils.EMPTY.getBytes();
    }

    public static void clearSetElement(String key) {
        clearSetElement(RedisDBEnum.DB0, key);
    }

    public static void clearSetElement(RedisDBEnum redisDBEnum, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            if (!jedis.exists(key)) {
                return;
            }
            Set<String> set = jedis.smembers(key);
            for (String member : set) {
                jedis.srem(key, member);
            }
        } catch (JedisException e) {
            _log.error("for jedis, 'clearSetElement' method call failed");
        } finally {
            releaseResource(jedis);
        }
    }

    public static void removeStringKey(String key) {
        removeStringKey(RedisDBEnum.DB0, key);
    }

    public static void removeStringKey(RedisDBEnum redisDBEnum, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            if (jedis.exists(key)) {
                jedis.del(key);
            }
        } catch (JedisException e) {
            _log.error("for jedis, 'removeStringKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
    }

    public static void removeByteKey(String key) {
        removeByteKey(RedisDBEnum.DB0, key);
    }

    public static void removeByteKey(RedisDBEnum redisDBEnum, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            if (jedis.exists(key.getBytes())) {
                jedis.del(key.getBytes());
            }
        } catch (JedisException e) {
            _log.error("for jedis, 'removeByteKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
    }

    @Deprecated
    public static Set<String> keys() {
        return keys(RedisDBEnum.DB0);
    }

    @Deprecated
    public static Set<String> keys(RedisDBEnum redisDBEnum) {
        Jedis jedis = null;
        Set<String> set = null;
        try {
            jedis = getJedis(redisDBEnum);
            set = jedis.keys("*");
            if (set == null) {
                set = new HashSet<String>();
            }
        } catch (JedisException e) {
            _log.error("for jedis, 'keys' method call failed");
        } finally {
            releaseResource(jedis);
        }
        return set;
    }

    public static Set<String> scan(String cursor) {
        return keys(RedisDBEnum.DB0);
    }

    public static ScanResult<String> scan(RedisDBEnum redisDBEnum, String cursor) {
        Jedis jedis = null;
        ScanResult<String> result = null;
        try {
            jedis = getJedis(redisDBEnum);
            result = jedis.scan(cursor);
        } catch (JedisException e) {
            _log.error("for jedis, 'scan' method call failed");
        } finally {
            releaseResource(jedis);
        }
        return result;
    }

    public static Boolean isExistKey(String key) {
        return isExistKey(RedisDBEnum.DB0, key);
    }

    public static Boolean isExistKey(RedisDBEnum redisDBEnum, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisDBEnum);
            return jedis.exists(key);
        } catch (JedisException e) {
            _log.error("for jedis, 'isExistKey' method call failed");
        } finally {
            releaseResource(jedis);
        }
        return false;
    }

}
