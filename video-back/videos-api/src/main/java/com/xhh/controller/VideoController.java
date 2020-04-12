package com.xhh.controller;

import com.xhh.enums.VideoStatusEnum;
import com.xhh.mapper.UsersMapper;
import com.xhh.pojo.Bgm;
import com.xhh.pojo.Users;
import com.xhh.pojo.Videos;
import com.xhh.pojo.vo.VideosVO;
import com.xhh.service.BgmService;
import com.xhh.service.VideoService;
import com.xhh.utils.*;
import io.swagger.annotations.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @description
 * @author: weiXhh
 * @create: 2020-03-30 15:50
 **/
@Api(value="上传视频的接口", tags= {"上传视频的controller"})
@RestController
@RequestMapping("/video")
public class VideoController extends BasicController {

    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideoService videoService;


    @ApiOperation(value = "用户上传视频",notes = "用户上传视频的接口")
    @PostMapping(value = "/upload/{userId}", headers="content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name="bgmId", value = "背景音乐ID", required = false,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoSeconds", value="视频时长", required = true,
                    dataType="double", paramType="form"),
            @ApiImplicitParam(name="videoWidth", value="视频宽度", required = true,
                    dataType="int", paramType="form"),
            @ApiImplicitParam(name="videoHeight", value="视频高度", required = true,
                    dataType="int", paramType="form"),
            @ApiImplicitParam(name="desc", value="视频描述", required = false,
                    dataType="String", paramType="form")
    })
    public ApiJSONResult upload(
            @PathVariable("userId") String userId,
            String bgmId,
            double videoSeconds,
            int videoWidth,
            int videoHeight,
            String desc,
            @ApiParam(value="短视频", required=true)
            MultipartFile file) throws Exception {

        // 存入数据库的地址
        String videoPlaceDB = "/" + userId + "/video";
        // 数据库中封面保存的位置
        String coverPlaceDB = "/" + userId + "/video";
        // 最终保存路径
        String finalPlace = "";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if(file != null){
                // 名称
                String videoName = file.getOriginalFilename();
                String videoNamePrefix = videoName.split("\\.")[0];


                if(!StringUtils.isEmpty(videoName)){
                    finalPlace = FACE_PLACE + videoPlaceDB + "/" + videoName;
                    videoPlaceDB += ("/" +videoName);
                    coverPlaceDB = coverPlaceDB + "/" + videoNamePrefix + ".jpg";

                    File outFile = new File(finalPlace);
                    if(outFile.getParentFile() == null || !outFile.getParentFile().isDirectory()){
                        // 如果没有，生成E:\workspace-video\videos-userFace\200331F5PW5ZWG9P\video所有目录
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            } else {
                return ApiJSONResult.errorMsg("上传出错啦...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiJSONResult.errorMsg("上传出错啦...");
        } finally {
            if(fileOutputStream != null){
                fileOutputStream.flush();;
                fileOutputStream.close();
            }
        }

        // bgmId不为空，进行视频合成
        if(!StringUtils.isEmpty(bgmId)){
            Bgm bgm = bgmService.queryBgmById(bgmId);
            // 背景音乐的保存路径
            String audioInputPath = FACE_PLACE + bgm.getPath();
            // 获得视频路径E:\workspace-video\videos-userFace\200331C1NRK68ACH\video
            String videoInputPath = finalPlace;
            // 消除后原声放的位置+命名
            String videoOutputName = UUID.randomUUID().toString() + ".mp4";
            videoPlaceDB = "/" + userId + "/video" + "/" + videoOutputName;
            finalPlace = FACE_PLACE + videoPlaceDB;

            VideoMergeAudio mergeAudio = new VideoMergeAudio(FFMPEG_COM);
            // 消除声音后的路径=finalPlace
            String removeSoundVideoPath = mergeAudio.removeOriginalSound(videoInputPath, finalPlace);
            // 合成后放的位置+命名
            String videoMergeName = UUID.randomUUID().toString() + ".mp4";
            videoPlaceDB = "/" + userId + "/video" + "/" + videoMergeName;
            finalPlace = FACE_PLACE + videoPlaceDB;

            mergeAudio.convert(removeSoundVideoPath, audioInputPath, videoSeconds, finalPlace);
        }

        // 封面截取
        FetchVideoCover fetchVideoCover = new FetchVideoCover(FFMPEG_COM);
        fetchVideoCover.getCover(finalPlace, FACE_PLACE + coverPlaceDB);

        Videos videos = new Videos();
        videos.setAudioId(bgmId);
        videos.setUserId(userId);
        videos.setVideoPath(videoPlaceDB);
        videos.setVideoSeconds((float)videoSeconds);
        videos.setVideoWidth(videoWidth);
        videos.setVideoHeight(videoHeight);
        videos.setVideoDesc(desc);
        videos.setStatus(VideoStatusEnum.SUCCESS.value);
        videos.setCreateTime(new Date());
        videos.setCoverPath(coverPlaceDB);
        // 保存到数据库
        videoService.saveVideos(videos);

        return ApiJSONResult.ok();
    }

    // 分页和搜索查询视频列表
    @ApiOperation(value = "分页,查询所有视频",notes = "分页,查询所有视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page", value="视频分页", required = false,
                    dataType="Integer", paramType="query"),
            @ApiImplicitParam(name="isSaveRecord", value="视频是否合法", required = false,
                    dataType="Integer", paramType="query")
    })
    @PostMapping("/getAll")
    public ApiJSONResult getAll(@RequestBody Videos videos, Integer isSaveRecord , Integer page){

        if(page == null){
            page = 1;
        }
        PagedResult result = videoService.queryAllVideos(videos,isSaveRecord,page, PAGE_SIZE);
        return ApiJSONResult.ok(result);
    }

    @ApiOperation(value = "查询所有视频",notes = "查询所有视频的接口")
    @PostMapping("/getList")
    public ApiJSONResult queryVideosList(){
        List<VideosVO> list = videoService.queryVideosList();
        return ApiJSONResult.ok(list);
    }

    // 查询所有热搜词
    @ApiOperation(value = "查询所有热搜词",notes = "查询所有热搜词的接口")
    @PostMapping("/hot")
    public ApiJSONResult hot(){
        return ApiJSONResult.ok(videoService.getHotWords());
    }

    // 查询所有热搜词
    @ApiOperation(value = "喜欢视频",notes = "喜欢视频的接口")
    @PostMapping("/likeVideo")
    public ApiJSONResult likeVideo(String userId, String videoId, String createUserId){
        videoService.addLikeVideos(userId,videoId,createUserId);
        return ApiJSONResult.ok();
    }

    @ApiOperation(value = "不喜欢视频",notes = "不喜欢视频的接口")
    @PostMapping("/unlikeVideo")
    public ApiJSONResult unlikeVideo(String userId, String videoId, String createUserId){
        videoService.reduceLikeVideos(userId, videoId, createUserId);
        return ApiJSONResult.ok();
    }

    @ApiOperation(value = "查询用户是否喜欢视频",notes = "查询用户是否喜欢视频的接口")
    @PostMapping("/isLike")
    public ApiJSONResult isLike(String userId, String videoId){
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(videoId)){
            return ApiJSONResult.errorMsg("用户id或视频id为空");
        }
        boolean b = videoService.queryLikeByVideoAndUserId(userId, videoId);
        return ApiJSONResult.ok(b);
    }

    @ApiOperation(value = "查询用户的喜欢视频",notes = "查询用户的喜欢视频接口")
    @PostMapping("/showMyLike")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required = true,
                    dataType="String", paramType="query"),
            @ApiImplicitParam(name="page", value="视频分页", required = false,
                    dataType="Integer", paramType="query")
    })
    public ApiJSONResult showMyLike(String userId, Integer page){
        if(StringUtils.isEmpty(userId)){
            return ApiJSONResult.errorMsg("");
        }
        if(page == null){
            page = 1;
        }
        PagedResult result = videoService.queryMyLikeVideos(userId, page, PAGE_SIZE);
        return ApiJSONResult.ok(result);
    }

    @PostMapping("/showMyFollow")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required = true,
                    dataType="String", paramType="query"),
            @ApiImplicitParam(name="nickname", value="用户昵称", required = false,
                    dataType="String", paramType="query")
    })
    public ApiJSONResult showMyFollow(String userId,String nickname){
        if(StringUtils.isEmpty(userId)){
            return ApiJSONResult.errorMsg("");
        }
        List<VideosVO> list = videoService.queryUserFollow(userId,nickname);
        return ApiJSONResult.ok(list);
    }
}
