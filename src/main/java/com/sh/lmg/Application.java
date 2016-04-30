package com.sh.lmg;

import com.sh.util.redis.JedisUtil;
import org.apache.log4j.Logger;

/**
 * Created by liaomengge on 16/4/30.
 */
public class Application {

    private static final Logger _log = Logger.getLogger(Application.class);
    private static final String REDIS_KEY = "logstash:redis";// 此处的key必须和logstash中定义的key一致

    public static void main(String[] args) throws InterruptedException {
        _log.error("zhangqingming");
        JedisUtil.rpushKey(REDIS_KEY, "liaomengge".getBytes());//这里仅做demo, 此处配置, 可以将redis作为中间队列配置
        System.out.println("init success");
    }
}
