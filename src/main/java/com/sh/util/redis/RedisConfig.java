package com.sh.util.redis;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;

public class RedisConfig extends JedisPoolConfig {

    private static String REDIS_CONFIG_PATH = "redis.properties";
    private static String IP = null;
    private static String PORT = null;
    private static Integer DB0 = null;
    private static Integer DB1 = null;
    private static Integer DB2 = null;
    private static Integer DB3 = null;
    private static String MAX_ACTIVE = null;
    private static String MAX_IDLE = null;
    private static String MAX_WAIT = null;
    private static String TEST_ON_BORROW = null;
    private static String TIME_OUT = null;

    private static Properties props = null;

    static {
        try {
            props = PropertiesLoaderUtils.loadAllProperties(REDIS_CONFIG_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("load redis config failed", e);
        }
        IP = props.getProperty("redis.pool.ip");
        PORT = props.getProperty("redis.pool.port");
        DB0 = Integer.valueOf(props.getProperty("redis.pool.db0"));
        DB1 = Integer.valueOf(props.getProperty("redis.pool.db1"));
        DB2 = Integer.valueOf(props.getProperty("redis.pool.db2"));
        DB3 = Integer.valueOf(props.getProperty("redis.pool.db3"));
        MAX_ACTIVE = props.getProperty("redis.pool.max_active");
        MAX_IDLE = props.getProperty("redis.pool.max_idle");
        MAX_WAIT = props.getProperty("redis.pool.max_wait");
        TEST_ON_BORROW = props.getProperty("redis.pool.test_on_borrow");
        TIME_OUT = props.getProperty("redis.pool.timeout");
    }

    private String ip;
    private Integer port;
    private Integer[] dbs;
    private Integer timeout;

    public RedisConfig() {
        super();
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(MAX_ACTIVE));
        config.setMaxIdle(Integer.valueOf(MAX_IDLE));
        config.setMaxWaitMillis(Integer.valueOf(MAX_WAIT));
        config.setTestOnBorrow(Boolean.valueOf(TEST_ON_BORROW));
        this.setIp(IP);
        this.setPort(Integer.valueOf(PORT));
        Integer[] dbs = {DB0, DB1, DB2, DB3};
        this.setDbs(dbs);
        this.setTimeout(Integer.valueOf(TIME_OUT));
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer[] getDbs() {
        return dbs;
    }

    public void setDbs(Integer[] dbs) {
        this.dbs = dbs;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

}
