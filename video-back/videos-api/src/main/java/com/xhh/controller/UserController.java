package com.xhh.controller;

import com.xhh.pojo.Users;
import com.xhh.pojo.vo.UsersVO;
import com.xhh.service.UserService;
import com.xhh.utils.ApiJSONResult;
import com.xhh.utils.FileDeleteUtils;
import com.xhh.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * @description
 * @author: weiXhh
 * @create: 2020-03-30 15:50
 **/
@Api(value="用户相关业务的接口", tags= {"用户相关业务的controller"})
@RestController
@RequestMapping("/user")
public class UserController extends BasicController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户上传头像",notes = "用户上传头像的接口")
    @PostMapping("/uploadFace/{userId}")
    public ApiJSONResult uploadFace(@PathVariable("userId") String userId,
                                    @RequestParam("file") MultipartFile[] files) throws Exception {

        // 存入硬盘的地址
        String facePlace = "E:/workspace-video/videos-userFace";
        // 存入数据库的地址
        String facePlaceDB = "/" + userId + "/face";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if(files != null && files.length > 0){
                // 头像名称
                String faceName = files[0].getOriginalFilename();

                if(!StringUtils.isEmpty(faceName)){
                    String finalPlace = facePlace + facePlaceDB + "/" + faceName;
                    facePlaceDB += ("/" +faceName);

                    File outFile = new File(finalPlace);
                    if(outFile.getParentFile() == null || !outFile.getParentFile().isDirectory()){
                        // 如果没有，生成E:\workspace-video\videos-userFace\200331F5PW5ZWG9P\face所有目录
                        outFile.getParentFile().mkdirs();
                    } else {
                        // 如果存在，说明用户已经上传了头像，再次上传时把前一个删除
                        FileDeleteUtils.deleteFile(outFile.getParentFile());
                    }
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = files[0].getInputStream();
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
        Users user = new Users();
        user.setId(userId);
        user.setFaceImage(facePlaceDB);
        userService.updateUserInfo(user);

        return ApiJSONResult.ok(facePlaceDB);
    }

    @ApiOperation(value = "查询用户信息",notes = "查询用户信息的接口")
    @PostMapping("/query/{userId}")
    public ApiJSONResult uploadFace(@PathVariable("userId") String userId) {

        if(StringUtils.isEmpty(userId)){
            return ApiJSONResult.errorMsg("用户id为空");
        }
        Users user = userService.queryUserInfo(userId);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        return ApiJSONResult.ok(usersVO);
    }

    @ApiOperation(value = "关注用户",notes = "关注用户的接口")
    @PostMapping("/beYourFans")
    public ApiJSONResult beYourFans(String userId, String fanId){
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(fanId)){
            return ApiJSONResult.errorMsg("用户id或粉丝id为空");
        }
        userService.saveUserFanRelation(userId, fanId);
        return ApiJSONResult.ok("关注成功");
    }

    @ApiOperation(value = "取消关注用户",notes = "取消关注用户的接口")
    @PostMapping("/dontBeYourFans")
    public ApiJSONResult dontBeYourFans(String userId, String fanId){
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(fanId)){
            return ApiJSONResult.errorMsg("用户id或粉丝id为空");
        }
        userService.deleteUserFanRelation(userId, fanId);
        return ApiJSONResult.ok("取消关注成功");
    }

    @ApiOperation(value = "查询是否关注了用户",notes = "查询是否关注了用户的接口")
    @PostMapping("/isFollow")
    public ApiJSONResult isFollow(String userId, String fanId){
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(fanId)){
            return ApiJSONResult.errorMsg("用户id或粉丝id为空");
        }
        boolean b = userService.queryFollowByFanAndUserId(userId, fanId);
        return ApiJSONResult.ok(b);
    }
}
