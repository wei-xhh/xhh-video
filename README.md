# xhh-video

## 开发技术

前端：

- [微信小程序](https://developers.weixin.qq.com/miniprogram/dev/framework/config.html)
- [搜索插件](https://github.com/mindawei/wsSearchView)

后端：

- [springBoot](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/)（版本2.2.5）
- myBatis
- [reids](http://www.redis.cn/)
- [ffmpeg](http://ffmpeg.org/)
- mariadb
- 分页框架
- swagger2

## 效果

参见博客： https://blog.csdn.net/weixin_44889138/article/details/105467742 

## 使用说明

1.  创建数据库（在源码中有）
2.  使用逆向生成工具（生成对应的po，xml）
3.  springBoot中注册资源目录

```java
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:E:/workspace-video/videos-userFace/")
                .addResourceLocations("classpath:/META-INF/resources/");
    }
```

4. window下：根据file:E:/workspace-video/videos-userFace/，在该目录下创建bgm目录，可放入你喜欢的音频，上传头像/上传视频功能会放在这个目录下
5. 数据表bgm插入音频相对地址
6. redis配置，下载redis并启动，在配置文件中初始化redis地址，密码(默认没有)

```java
spring.redis.host=127.0.0.1
spring.redis.password=redis
```

window10下载redis推荐

- 先下载window10内嵌linux（搜索WSL）

7. 启动springBoot，修改微信小程序app.js中的serverUrl为你的主机地址
