// pages/index/index.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    cover: "cover"
  },

  // 第一次加载时获得所有视频信息
  onLoad(params) {
    console.log(JSON.parse(params.videoItem))
    var videoItem = JSON.parse(params.videoItem);
    var serverUrl = app.serverUrl;
    var videoHeight = videoItem.videoHeight;
    var videoWidth = videoItem.videoWidth;
    var cover = "cover"
    if (videoWidth >= videoHeight) {
      cover = ""
    }
    this.setData({
      videoItem: videoItem,
      serverUrl: serverUrl,
      cover: cover
    });

    // 登录后判断视频是否被点赞，
    var user = app.getGlobalUserInfo();
    if (user != null && user != '' && user != undefined) {
      var userId = user.id;
      var videoId = videoItem.id
      this.queryUserLikeVideo(userId, videoId);
    }
  },
  // 搜索
  showSearch() {
    wx.navigateTo({
      url: '../searchVideo/searchVideo'
    })
  },
  // 点赞
  likeVideoOrNot() {

    var me = this;
    var serverUrl = app.serverUrl;
    var user = app.getGlobalUserInfo();
    var userLikeVideo = me.data.userLikeVideo;

    if (user != null && user != '' && user != undefined) {
      var videoItem = me.data.videoItem;
      var url = "/video/likeVideo?userId=" + user.id + "&videoId=" + videoItem.id + "&createUserId=" + videoItem.userId
      if (userLikeVideo) {
        url = "/video/unlikeVideo?userId=" + user.id + "&videoId=" + videoItem.id + "&createUserId=" + videoItem.userId
      }
      wx.showLoading({
        title: '',
      })
      wx.request({
        url: serverUrl + url,
        method: 'POST',
        success(res) {
          wx.hideLoading();
          if (res.data.status = 200) {
            me.setData({
              userLikeVideo: !userLikeVideo
            })
          }
        }
      })
    } else {
      wx.showToast({
        title: '您还没登录呢！',
        icon: 'none'
      })
    }
  },
  toHome(){
    wx.switchTab({
      url: '../index/index',
    })
  },
  // 登录后判断视频是否被点赞
  queryUserLikeVideo(userId, videoId) {
    var serverUrl = app.serverUrl;
    var me = this;

    wx.request({
      url: serverUrl + '/video/isLike?userId=' + userId + "&videoId=" + videoId,
      method: 'POST',
      success(res) {
        if (res.data.status == 200) {
          if (res.data.data) {
            me.setData({
              userLikeVideo: true
            })
          } else {
            me.setData({
              userLikeVideo: false
            })
          }
        } else if (res.data.status == 500) {
          wx.showToast({
            title: res.data.msg,
            duration: 1000
          })
        }

      }
    })
  },
})