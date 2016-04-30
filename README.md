### ELK搭建简单的日志系统
##### 该demo主要是通过logstash的plugins的log4j和redis来实现的

#### 有兴趣的朋友,可以用kafka替换redis作为redis日志缓存容器,因为redis缓存日志,有如下隐患:
* redis的RDB持久化,在宕机的情况下,会丢失日志

中心配置logstash-indexer.conf:
input {
 	redis {
		host 		=> "127.0.0.1"
		port		=> "6379"
		data_type 	=>"list"
		key 		=> "logstash:redis"
		type 		=> "redis-input"
	}
	log4j {
         mode    => "server"
         host     => "127.0.0.1"
         port     => "8066"
         type     => "log4j"
    }
}

output {
	elasticsearch { 
        hosts	=> ["localhost:9200"]
	}
	stdout { 
		codec => rubydebug 
	}
}

代理配置logstash-shipper.conf:
input {
  file {
    path  => ["/Users/liaomengge/ELK/log/error.log"]
    type    => "log-error"
  }
}

output {
  stdout {
  }
  redis {
    host    => "127.0.0.1"
    port    => "6379"
    data_type   =>"list"
    key     => "logstash:redis"
    codec   => "json"
  }
}
