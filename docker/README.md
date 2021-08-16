# 使用 docker-compose 服务编排服务

## 打包镜像
```shell script
jib:dockerBuild -f pom.xml
```

## 打tag
```shell script
docker tag workcheng/hellojib:0.0.1-SNAPSHOT 192.168.0.161:5000/weiya
```

## 上传到本地 docker 自建仓库
```shell script
docker push 192.168.0.161:5000/weiya
```

## 启动服务
```shell script
docker-compose up
```

# ref
- [docker 容器间访问几种方式](https://www.cnblogs.com/shenh/p/9714547.html)
- [自建docker私有仓库](https://blog.csdn.net/studywinwin/article/details/104860443)

# TODO
- docker-compose.yml 使用`env_file`配置参数文件，统一从配置文件中取配置