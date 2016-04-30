### ELK搭建简单的日志系统
##### 该demo主要是通过logstash的plugins的log4j和redis来实现的

#### 有兴趣的朋友,可以用kafka替换redis作为redis日志缓存容器,因为redis缓存日志,有如下隐患:
* redis的RDB持久化,在宕机的情况下,会丢失日志