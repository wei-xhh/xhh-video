package com.xhh.service.impl;

import com.xhh.mapper.UsersFansMapper;
import com.xhh.mapper.UsersMapper;
import com.xhh.pojo.Users;
import com.xhh.pojo.UsersFans;
import com.xhh.pojo.UsersLikeVideos;
import com.xhh.service.UserService;
import org.apache.tomcat.jni.User;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @description
 * @author: weiXhh
 * @create: 2020-03-31 12:42
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UsersFansMapper usersFansMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserByUsername(String username) {
        Users user = new Users();
        user.setUsername(username);
        Users result = usersMapper.selectOne(user);
        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users user) {
        String userId = sid.nextShort();
        user.setId(userId);
        usersMapper.insert(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username,String password) {
//        Users user = new Users();
//        user.setUsername(username);
//        user.setPassword(password);
//        Users result = usersMapper.selectOne(user);
        //也可以这样写
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",password);
        Users result = usersMapper.selectOneByExample(userExample);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserInfo(Users user) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id",user.getId());
        usersMapper.updateByExampleSelective(user, userExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id",userId);
        Users user = usersMapper.selectOneByExample(userExample);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUserFanRelation(String userId, String fanId) {
        // 保存对应关系
        UsersFans usersFans = new UsersFans();
        String ufId = sid.nextShort();
        usersFans.setId(ufId);
        usersFans.setFanId(fanId);
        usersFans.setUserId(userId);
        usersFansMapper.insert(usersFans);

        usersMapper.addFansCount(userId);
        usersMapper.addFollowCount(fanId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserFanRelation(String userId, String fanId) {
        // 删除对应关系
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fanId);
        usersFansMapper.deleteByExample(example);

        usersMapper.reduceFansCount(userId);
        usersMapper.reduceFollowCount(fanId);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryFollowByFanAndUserId(String userId, String fanId) {

        UsersFans usersFans = new UsersFans();
        usersFans.setUserId(userId);
        usersFans.setFanId(fanId);
        UsersFans result = usersFansMapper.selectOne(usersFans);
        return result == null ? false : true;
    }

}
