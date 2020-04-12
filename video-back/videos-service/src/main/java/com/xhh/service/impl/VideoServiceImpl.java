package com.xhh.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xhh.mapper.*;
import com.xhh.pojo.SearchRecords;
import com.xhh.pojo.Users;
import com.xhh.pojo.UsersLikeVideos;
import com.xhh.pojo.Videos;
import com.xhh.pojo.vo.VideosVO;
import com.xhh.service.VideoService;
import com.xhh.utils.PagedResult;
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
 * @create: 2020-04-05 13:11
 **/
@Service

public class VideoServiceImpl implements VideoService {

    @Autowired
    VideosMapper videosMapper;

    @Autowired
    VideosMapperCustom videosMapperCustom;

    @Autowired
    SearchRecordsMapper recordsMapper;

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveVideos(Videos videos) {
        String videoId = sid.nextShort();
        videos.setId(videoId);
        videosMapper.insertSelective(videos);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult queryAllVideos(Videos videos, Integer isSaveRecord, Integer page, Integer pageSize) {

        // 保存热搜词
        String videoDesc = videos.getVideoDesc();
        // 如果有userId，根据userId获取
        String userId = videos.getUserId();
        if(isSaveRecord != null && isSaveRecord == 1){
            SearchRecords searchRecords = new SearchRecords();
            String recordId = sid.nextShort();
            searchRecords.setId(recordId);
            searchRecords.setContent(videoDesc);
        }

        PageHelper.startPage(page, pageSize);
        List<VideosVO> list = videosMapperCustom.queryAllVideos(videoDesc,userId);
        PageInfo<VideosVO> pageInfo = new PageInfo<>(list);

        PagedResult result = new PagedResult();
        result.setPage(page);
        result.setTotal(pageInfo.getPages());
        result.setRecords(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<VideosVO> queryVideosList() {
        List<VideosVO> list = videosMapperCustom.queryVideosList();
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getHotWords() {
        List<String> hotWords = recordsMapper.getHotWords();

        return hotWords;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addLikeVideos(String userId, String videoId, String createUserId) {
        // 保存userId(点赞id)，videoId(视频id)
        String ulvId = sid.nextShort();
        UsersLikeVideos ulv = new UsersLikeVideos();
        ulv.setId(ulvId);
        ulv.setUserId(userId);
        ulv.setVideoId(videoId);
        usersLikeVideosMapper.insert(ulv);

        // 根据视频创建的id更新
        usersMapper.addReceiveLikeCount(createUserId);

        // 根据视频的videoId更新
        videosMapper.addLikeCount(videoId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reduceLikeVideos(String userId, String videoId, String createUserId) {
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        usersLikeVideosMapper.deleteByExample(example);

        // 根据视频创建的id更新
        usersMapper.reduceReceiveLikeCount(createUserId);

        // 根据视频的videoId更新
        videosMapper.reduceLikeCount(videoId);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryLikeByVideoAndUserId(String userId, String videoId) {
        UsersLikeVideos ulv = new UsersLikeVideos();
        ulv.setUserId(userId);
        ulv.setVideoId(videoId);
        UsersLikeVideos result = usersLikeVideosMapper.selectOne(ulv);
        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        List<VideosVO> list = videosMapperCustom.queryMyLikeVideos(userId);
        PageInfo<VideosVO> pageInfo = new PageInfo<>(list);

        PagedResult result = new PagedResult();
        result.setPage(page);
        result.setTotal(pageInfo.getPages());
        result.setRecords(pageInfo.getTotal());
        result.setRows(list);

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<VideosVO> queryUserFollow(String userId,String nickname) {
        List<VideosVO> list = videosMapperCustom.queryUserFollow(userId,nickname);
        return list;
    }

}
