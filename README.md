# WeiYa
尾牙小程序
#### `WeiYa`基于微信公众号的签到、抽奖、发送弹幕程序

1. 前端使用 Jquery，弹幕使用第三方 Canvas 插件[【JBarrager】](https://github.com/Sailiy/JBarrager)
1. 后端使用 Spring Boot，数据库使用 Redis，微信 API 操作使用第三方 SDK [【weixin-java-tools】](https://github.com/Wechat-Group/weixin-java-tools/releases)
1. 弹幕使用 Websocket 建立长连接，前端使用 Canvas，经过测试可以经得住`1000+`人以上的交互发送消息
1. 登陆账号密码:root/root
1. 配置文件：
    - (application.properties)
        - spring.redis.* redis配置
        - weiya.username password 后台登陆配置（默认过期时间3小时）（3 * 60 * 60 * 1000）
        - wx.mp.* 微信公众号配置
        - weiya.sinOpenTime 签到时间配置（限制签到）
        - weiya.openTime 活动开始时间（限制后台使用和签到）
    - resources\sensitive\sensitiveWord.txt 和谐词库配置
    - resources\static\config\config.js 前端url配置

## 编译启动

### 打成jar包

```
mvn clean package -f pom.xml
```

1. 修改对应配置为正确的配置，
1. 运行demo，执行命令 ：`http get :8080`

```
HTTP/1.1 200
Connection: keep-alive
Content-Length: 5
Content-Type: text/plain;charset=UTF-8
Date: Fri, 04 Sep 2020 01:28:06 GMT
Keep-Alive: timeout=60

```

### 使用Jib（谷歌开源Java镜像构建工具）

- 1、打包镜像：

```
mvn compile jib:buildTar
```
- 2、导入镜像：

```
docker load < target/jib-image.tar
```

- 3、启动容器

```
docker run \
        --name=magical_beaver \
        --hostname=7b9f6db0ad40 \
        --mac-address=02:42:ac:11:00:03 \
        --env=PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin \
        --env=JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk \
        --env=JAVA_VERSION=8u171 \
        --env=JAVA_ALPINE_VERSION=8.171.11-r0 \
        --env=LANG=C.UTF-8 \
        -p 0.0.0.0:9090:8080 \
        -t \
        workcheng/hellojib:0.0.1-SNAPSHOT
```

- 4、浏览器打开/查看

```
curl http://127.0.0.1:9090/admin/index.html
```

#### todo
1. 数据库默认使用`h2db`,可配置redis
1. PaaS模式？提供可配置多租户，多公众号配置？用户体系？
1. 增加可多次中奖的开关