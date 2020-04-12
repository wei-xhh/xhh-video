package com.xhh.service;


import com.xhh.pojo.Users;

import java.util.List;

/**
 * @description
 * @author: weiXhh
 * @create: 2020-03-31 12:40
 **/
public interface UserService {
    /**
     * 根据username查询是否存在该用户
     * @param username
     * @return
     */
    public boolean queryUserByUsername(String username);

    /**
     * 根据用户信息user保存用户
     * @param user
     */
    public void saveUser(Users user);

    /**
     * 根据username和password查询用户
     * @param username
     * @param password
     * @return
     */
    public Users queryUserForLogin(String username,String password);

    /**
     * 根据用户信息user更新用户
     * @param user
     */
    public void updateUserInfo(Users user);

    /**
     * 根据用户信息userId查询用户
     * @param userId
     * @return
     */
    public Users queryUserInfo(String userId);

    /**
     * 关注
     * @param userId
     * @param fanId
     */
    public void saveUserFanRelation(String userId, String fanId);

    /**
     * 取消关注
     * @param userId
     * @param fanId
     */
    public void deleteUserFanRelation(String userId, String fanId);

    // 查询用户是否已经被关注
    public boolean queryFollowByFanAndUserId(String userId, String fanId);

}
