package com.xhh.service;


import com.xhh.pojo.Users;
import com.xhh.pojo.Videos;
import com.xhh.pojo.vo.VideosVO;
import com.xhh.utils.PagedResult;

import java.util.List;

/**
 * @description
 * @author: weiXhh
 * @create: 2020-03-31 12:40
 **/
public interface VideoService {

    /**
     * 根据videos保存视频信息
     * @param videos
     */
    public void saveVideos(Videos videos);

    /**
     * 查询视频列表
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult queryAllVideos(Videos videos, Integer isSaveRecord, Integer page, Integer pageSize);

    // 获取所有视频
    public List<VideosVO> queryVideosList();

    // 查询热搜词列表
    public List<String> getHotWords();

    // 点赞
    public void addLikeVideos(String userId, String videoId, String createUserId);

    // 取消点赞
    public void reduceLikeVideos(String userId, String videoId, String createUserId);

    // 查询视频是否被点赞
    public boolean queryLikeByVideoAndUserId(String userId, String videoId);

    // 查询我喜欢的视频
    public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize);

    // 查询我关注的人儿,如果nickname不为空，则根据这两个参数查询，查看其他用户时关注人我是否也关注了
    public List<VideosVO> queryUserFollow(String userId,String nickname);
}
