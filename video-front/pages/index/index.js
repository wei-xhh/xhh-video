// pages/index/index.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userLikeVideo: false,
    userFollowMe: false,
    cover: "cover",
    videoList: [],
    serverUrl: "",
    index: 0,
    videoItem: {},
    searchValue: "",
    page: 1,
    totalPage: 1
  },

  // 第一次加载时获得所有视频信息
  onLoad() {
    var me = this;
    var isSaveRecord = app.isSaveRecord;
    var page = me.data.page;

    me.getAllVideoList(page, isSaveRecord)

  },

  // 上拉时重新加载页面
  onPullDownRefresh() {
    wx.showNavigationBarLoading();
    this.setData({
      videoList: [],
      serverUrl: "",
      index: 0,
      videoItem: {},
      searchValue: "",
      page: 1,
      totalPage: 1
    })
    this.onLoad()
  },
  onReachBottom: function () {
    // 页面触底时执行
    var me = this;
    var index = me.data.index;
    var videoList = me.data.videoList;
    if (index >= videoList.length - 1) {
      wx.showToast({
        title: '没有了,可点击左上角刷新',
        icon: 'none'
      });

    } else {
      index = index + 1;
      // 判断页面宽高
      var videoList = me.data.videoList;
      var videoHeight = videoList[index].videoHeight;
      var videoWidth = videoList[index].videoWidth;

      var cover = "cover"
      if (videoWidth >= videoHeight) {
        cover = ""
      }
      me.setData({
        cover: cover
      })

      // 登录后判断视频是否被点赞
      var user = app.getGlobalUserInfo();
      if (user != null && user != '' && user != undefined) {
        var userId = user.id;
        var videoId = videoList[index].id
        me.queryUserLikeVideo(userId, videoId)
      }
      // 判断视频发布者是否已经被当前用户关注
      if (user != null && user != '' && user != undefined) {
        // 由于参数的不同，在判断一次
        var fanId = user.id;
        var fanToken = user.userToken;
        var userId = videoList[index].userId;
        // 由于springboot做了user路径下单拦截，
        // 所以需要在header上添加参数，fanId,fanToken
        me.queryUserFollowMe(userId, fanId, fanToken);
      }

      me.getVideoItem(index);
      console.log(index)
    }
  },
  // 点击做上角刷新视频(拿第2,3..页)
  refresh() {

    var me = this;
    console.log("111222")
    console.log(me.data.index)
    var page = me.data.page + 1;
    var totalPage = me.data.totalPage;
    if (page <= totalPage) {
      me.getAllVideoList(page, 0)
      // 初始化值(totalPage不用)
      me.setData({
        page: page,
        index: 0,
        videoList: [],
        videoItem: {}
      })
      wx.showToast({
        title: '已刷新啦！',
        icon: 'success',
        duration: 2000
      })
    } else {
      wx.showToast({
        title: '没有可推荐的视频啦,向上拉刷新吧',
        icon: 'none',
        duration: 2000
      })
    }

  },
  //根据page,isSaveRecord获取视频
  getAllVideoList: function (page, isSaveRecord) {
    var me = this;
    var serverUrl = app.serverUrl;
    var searchValue = app.searchValue;

    wx.showLoading({
      title: '加载中...',
    })
    wx.request({
      url: serverUrl + "/video/getAll?page=" + page + "&isSaveRecord=" + isSaveRecord,
      method: 'POST',
      data: {
        videoDesc: searchValue
      },
      success: function (res) {
        wx.hideLoading();
        wx.hideNavigationBarLoading();
        wx.stopPullDownRefresh();

        console.log(res.data)

        var videoList = res.data.data.rows;
        var index = me.data.index;
        // 判断视频的宽高比
        var videoHeight = videoList[index].videoHeight;
        var videoWidth = videoList[index].videoWidth;
        var cover = "cover"
        if (videoWidth >= videoHeight) {
          cover = ""
        }

        // 登录后判断视频是否被点赞，
        var user = app.getGlobalUserInfo();
        if (user != null && user != '' && user != undefined) {
          var userId = user.id;
          var videoId = videoList[index].id
          me.queryUserLikeVideo(userId, videoId);
        }
        // 判断视频发布者是否已经被当前用户关注
        if (user != null && user != '' && user != undefined) {
          // 由于参数的不同，在判断一次
          var fanId = user.id;
          var fanToken = user.userToken;
          var userId = videoList[index].userId;
          // 由于springboot做了user路径下单拦截，
          // 所以需要在header上添加参数，fanId,fanToken
          me.queryUserFollowMe(userId, fanId, fanToken);
        }


        me.setData({
          videoList: videoList,
          videoItem: videoList[index],
          serverUrl: serverUrl,
          totalPage: res.data.data.total,
          cover: cover
        });

        // 调用成功后设置为默认值
        app.isSaveRecords = 0;
        app.searchValue = "";
      }
    })
  },
  // 根据下标展示视频
  getVideoItem(index) {
    var me = this;
    var videoList = me.data.videoList;
    me.setData({
      index: index,
      videoItem: videoList[index]
    });
  },
  onShow() {
    var me = this;
    // 用户从上传作品后跳转到info刷新
    if (app.toIndex == true) {
      app.toIndex = false;
      // 初始化值
      me.setData({
        page: 1,
        videoList: [],
        index: 0,
        videoItem: {},
        totalPage: 1
      });
      var page = me.data.page;
      var isSaveRecord = app.isSaveRecord;

      me.getAllVideoList(page, isSaveRecord)
      console.log(app.toIndex)
    }
    console.log(app.searchToVideo)
    // 从查询页面跳转过来刷新
    if (app.searchToVideo) {
      app.searchToVideo = false;
      // 初始化值
      me.setData({
        page: 1,
        videoList: [],
        index: 0,
        videoItem: {},
        totalPage: 1
      });
      var page = me.data.page;
      var isSaveRecord = app.isSaveRecord;

      me.getAllVideoList(page, isSaveRecord)

    }

    //关注/取消关注时刷新
    var followMeToVideo = app.followMeToVideo
    console.log("22323")
    console.log(followMeToVideo)
    if(followMeToVideo){
       // 判断视频发布者是否已经被当前用户关注
       var user = app.getGlobalUserInfo();
       var videoItem = me.data.videoItem;
       if (user != null && user != '' && user != undefined) {
        // 由于参数的不同，在判断一次
        var fanId = user.id;
        var fanToken = user.userToken;
        var userId = videoItem.userId;
        // 由于springboot做了user路径下单拦截，
        // 所以需要在header上添加参数，fanId,fanToken
        me.queryUserFollowMe(userId, fanId, fanToken);
      }
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

  // 关注我
  flowMe() {
    console.log("关注我点击了")
    var me = this;
    var user = app.getGlobalUserInfo();
    var serverUrl = app.serverUrl;
    if (user != null && user != '' && user != undefined) {

      var videoItem = me.data.videoItem;
      console.log(videoItem)
      wx.request({
        url: serverUrl + "/user/beYourFans?userId=" + videoItem.userId + "&fanId=" + user.id,
        method: "POST",
        header: {
          'content-type': 'application/json', // 默认值
          'userId': user.id,
          'userToken': user.userToken
        },
        success: function (res) {
          wx.showToast({
            title: res.data.data,
            duration: 2000,
            icon: 'success'
          });
          me.setData({
            userFollowMe: true
          })
        }
      })
    } else {
      wx.showToast({
        title: '您还没登录呢!',
        duration: 2000,
        icon: 'none'
      })
    }
  },

  // 登录后查询是否关注了该用户
  queryUserFollowMe(userId, fanId, fanToken) {
    console.log(fanToken)
    var serverUrl = app.serverUrl;
    var me = this;
    if (userId == fanId) {
      // 如果相等，说明发布者是自己
      me.setData({
        userFollowMe: true
      });
    } else {
      wx.request({
        url: serverUrl + "/user/isFollow?userId=" + userId + "&fanId=" + fanId,
        method: "POST",
        header: {
          'content-type': 'application/json', // 默认值
          'userId': fanId,
          'userToken': fanToken
        },
        success: function (res) {
          if (res.data.data) {
            me.setData({
              userFollowMe: true
            })
          } else {
            me.setData({
              userFollowMe: false
            })
          }
        }
      })
    }
  },
  // 跳转到发布者信息
  showPublisher() {
    var user = app.getGlobalUserInfo();
    if (user != null && user != '' && user != undefined) {
      var me = this;
      var videoItem = me.data.videoItem;
      var publisherId = videoItem.userId; //视频发布者id
      var userId = user.id; // 当前登录者id
      if(publisherId == userId){
        wx.switchTab({
          url: '../mine/mine'
        })
      } else {
        // 把videoItem转换成字符串
        wx.navigateTo({
          url: '../publisherInfo/publisherInfo?publisherInfo=' + JSON.stringify(videoItem) + "&userFollowMe=" + me.data.userFollowMe,
        })
      }
    } else {
      wx.showToast({
        title: '您还没登录呢!',
        icon: 'none'
      })
    }

  }
})