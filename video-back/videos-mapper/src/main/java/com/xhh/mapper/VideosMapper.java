package com.xhh.mapper;

import com.xhh.pojo.Videos;
import com.xhh.utils.MyMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface VideosMapper extends MyMapper<Videos> {
    public void addLikeCount(String videoId);

    public void reduceLikeCount(String videoId);
}