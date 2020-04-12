package com.xhh.interceptor;

import com.xhh.utils.ApiJSONResult;
import com.xhh.utils.JsonUtils;
import com.xhh.utils.RedisOperator;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @description
 * @author: weiXhh
 * @create: 2020-04-09 14:48
 **/
public class MyInterceptor implements HandlerInterceptor {

    @Autowired
    public RedisOperator redisOperator;

    public static final String USER_REDIS_SESSION = "user-id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("userId");
        String userToken = request.getHeader("userToken");

        if(!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(userToken)){
            String uniqueToken = redisOperator.get(USER_REDIS_SESSION + ":" + userId);
            if(StringUtils.isEmpty(uniqueToken)){
                System.out.println("请登录...");
                returnErrorResponse(response, ApiJSONResult.errorTokenMsg("请登录..."));
                return false;
            } else {
                if(!uniqueToken.equals(userToken)) {
                    System.out.println("账号被挤出...");
                    returnErrorResponse(response, ApiJSONResult.errorTokenMsg("账号被挤出..."));
                    return false;
                }
            }
        } else {
            System.out.println("请登录...");
            returnErrorResponse(response, ApiJSONResult.errorTokenMsg("请登录..."));
            return false;
        }
        return true;
    }

    public void returnErrorResponse(HttpServletResponse response, ApiJSONResult result) throws IOException {
        OutputStream out = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } finally {
            if(out != null){
                out.close();;
            }
        }
    }
}
