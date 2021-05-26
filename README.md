## 尾牙小程序
### 本`WeiYa`项目使用 `Spring Boot Starter` 实现微信公众号开发功能。
#### 本项目为`WeiYa`的实现基于微信公众号的签到、抽奖、发送弹幕程序


1. 修改对应配置为正确的配置，
2. 运行demo，执行命令 ：`http get :8080/test`

```
HTTP/1.1 200
Connection: keep-alive
Content-Length: 5
Content-Type: text/plain;charset=UTF-8
Date: Fri, 04 Sep 2020 01:28:06 GMT
Keep-Alive: timeout=60

appId
```

### 使用gib

1. 打包镜像：

```
mvn compile jib:buildTar
```
2. 导入镜像：

```
docker load < target/jib-image.tar
```

3. 启动容器

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

4. 浏览器打开/查看

```
curl http://127.0.0.1:9090/admin/index.html
```