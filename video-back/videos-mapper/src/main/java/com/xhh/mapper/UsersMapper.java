package com.xhh.mapper;

import com.xhh.pojo.Users;
import com.xhh.utils.MyMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public interface UsersMapper extends MyMapper<Users> {
    public void addReceiveLikeCount(String userId);
    public void reduceReceiveLikeCount(String userId);
    public void addFansCount(String userId);
    public void addFollowCount(String userId);
    public void reduceFansCount(String userId);
    public void reduceFollowCount(String userId);
}