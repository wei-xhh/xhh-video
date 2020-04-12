package com.xhh.controller;

import com.xhh.pojo.Users;
import com.xhh.pojo.vo.UsersVO;
import com.xhh.service.UserService;
import com.xhh.utils.ApiJSONResult;
import com.xhh.utils.MD5Utils;
import com.xhh.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.UUID;

/**
 * @description
 * @author: weiXhh
 * @create: 2020-03-30 15:50
 **/
@Api(value="用户注册登录的接口", tags= {"注册和登录的controller"})
@RestController
public class RegisterLoginController extends BasicController {

    @Autowired
    private UserService userService;

    @ApiOperation(value="用户注册", notes="用户注册的接口")
    @PostMapping("/register")
    public ApiJSONResult register(@RequestBody Users user) throws Exception {
        // 1.判断用户名或密码是否为空
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return ApiJSONResult.errorMsg("用户名或密码为空");
        }
        // 2.判断用户名是否存在
        boolean exitUsername = userService.queryUserByUsername(user.getUsername());
        if (!exitUsername) {
            // 3.保存用户
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setFollowCounts(0);
            user.setReceiveLikeCounts(0);
            userService.saveUser(user);

        } else {
            return ApiJSONResult.errorMsg("该用户已经存在,请重新输入");
        }
        user.setPassword(null);
        UsersVO usersVO = setUserRedisSession(user);
        return ApiJSONResult.ok(usersVO);
    }

    @ApiOperation(value = "用户登录",notes = "用户登录的接口")
    @PostMapping("/login")
    public ApiJSONResult login(@RequestBody Users user) throws Exception {

        if(StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())){
            return ApiJSONResult.errorMsg("用户名或密码为空");
        }

        // 根据用户名和密码查询
        Users result = userService.queryUserForLogin(user.getUsername(),
                MD5Utils.getMD5Str(user.getPassword()));

        if (result != null) {
            result.setPassword(null);
            UsersVO usersVO = setUserRedisSession(result);
            return ApiJSONResult.ok(usersVO);
        } else {
            return ApiJSONResult.errorMsg("账号或密码错误，请重试");
        }
    }

    @ApiOperation(value = "用户注销",notes = "用户注销的接口")
    @PostMapping("/logout/{userId}")
    public ApiJSONResult logout(@PathVariable("userId") String userId) {
        redisOperator.del(USER_REDIS_SESSION + ":" + userId);
        return ApiJSONResult.ok();
    }

    //封装的方法，设置用户session,利用redis
    private UsersVO setUserRedisSession(Users user){
        String uniqueToken = UUID.randomUUID().toString();
        redisOperator.set(USER_REDIS_SESSION + ":" + user.getId(), uniqueToken, 1000 * 60 * 30);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUserToken(uniqueToken);

        return usersVO;
    }
}
