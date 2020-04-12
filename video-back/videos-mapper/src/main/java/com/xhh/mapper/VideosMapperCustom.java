package com.xhh.mapper;

import com.xhh.pojo.Videos;
import com.xhh.pojo.vo.VideosVO;
import com.xhh.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideosMapperCustom extends MyMapper<Videos> {

    public List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc,@Param("userId") String userId);

    public List<VideosVO> queryVideosList();

    public List<VideosVO> queryMyLikeVideos(@Param("userId") String userId);

    //@Param如果不写，对应的值一定要与xml相同
    public List<VideosVO> queryUserFollow(@Param("userId") String userId,@Param("nickname") String nickname);
}