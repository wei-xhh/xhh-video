package com.xhh.controller;

import com.xhh.pojo.Users;
import com.xhh.pojo.vo.UsersVO;
import com.xhh.service.BgmService;
import com.xhh.service.UserService;
import com.xhh.utils.ApiJSONResult;
import com.xhh.utils.FileDeleteUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @description
 * @author: weiXhh
 * @create: 2020-03-30 15:50
 **/
@Api(value="用户相关Bgm的接口", tags= {"用户相关Bgm的controller"})
@RestController
@RequestMapping("/bgm")
public class BgmController extends BasicController {

    @Autowired
    private BgmService bgmService;

    @ApiOperation(value = "查询所有bgm",notes = "查询所有bgm的接口")
    @PostMapping("/list")
    public ApiJSONResult list() {

        return ApiJSONResult.ok(bgmService.queryBgmList());
    }
}
