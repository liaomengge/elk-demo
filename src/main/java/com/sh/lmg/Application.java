package com.sh.lmg;

import com.sh.util.redis.JedisUtil;
import org.apache.log4j.Logger;

/**
 * Created by liaomengge on 16/4/30.
 */
public class Application {

    private static final Logger _log = Logger.getLogger(Application.class);
    private static final String REDIS_KEY = "logstash:redis";

    public static void main(String[] args) throws InterruptedException {
        _log.info("hello, world");
        JedisUtil.rpushKey(REDIS_KEY, "liaomengge".getBytes());
        System.out.println("init success");
    }
}
