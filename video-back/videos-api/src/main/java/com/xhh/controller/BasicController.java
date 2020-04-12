package com.xhh.controller;

import com.xhh.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @author: weiXhh
 * @create: 2020-03-31 19:36
 **/
@RestController
public class BasicController {

    @Autowired
    public RedisOperator redisOperator;

    public static final String USER_REDIS_SESSION = "user-id";

    public static final String FFMPEG_COM = "F:\\ffmpeg\\bin\\ffmpeg.exe";
    // 存入硬盘的地址包括视频
    public static final String FACE_PLACE = "E:/workspace-video/videos-userFace";

    //一页的大小
    public static final Integer PAGE_SIZE = 6;
}
