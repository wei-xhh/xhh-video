const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userFollowMe: false,
    toUserInfo: "toUserInfo",
    //作品，喜欢，关注
    videoSelClass: "video-info",
    isSelectedWork: "video-info-selected",
    isSelectedFollow: "",

    myVideoList: [],
    myVideoPage: 1,
    myVideoTotal: 1,

    followList: [],

    myWorkFalg: false,
    myFollowFalg: true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (params) {
    var me = this;
    var publisherInfo = JSON.parse(params.publisherInfo);
    var userFollowMe = params.userFollowMe;
    var user = app.getGlobalUserInfo();
    var serverUrl = app.serverUrl;
    console.log(publisherInfo)
    console.log(userFollowMe)
    // userFollowMe传递过来时为字符串
    if(userFollowMe == "true"){
      userFollowMe = true;
    } else if(userFollowMe == "false") {
      userFollowMe = false;
    }
    me.setData({
      userFollowMe: userFollowMe,
      publisherInfo: publisherInfo
    })

    // 查询用户的作品
    var userId = this.data.publisherInfo.userId;
    this.getMyVideoList(userId,1)

    // 查询用户信息已写过，可封装成全体方法，这里就不用了。。
    wx.showLoading({
      title: '加载中...',
    })
    wx.request({
      url: serverUrl + "/user/query/" + publisherInfo.userId,
      method: "POST",
      header: {
        'content-type': 'application/json', // 默认值
        'userId': user.id,
        'userToken': user.userToken
      },
      success: function (res) {
        var userInfo = res.data.data;
        var status = res.data.status;
        if (status == 200) {
          wx.hideLoading();
          // 判断是否有头像
          var faceUrl = "/images/content/noneface.png";
          if (userInfo.faceImage != '' && userInfo.faceImage != undefined &&
            userInfo.faceImage != null) {
            faceUrl = serverUrl + userInfo.faceImage
          }
          me.setData({
            faceUrl: faceUrl,
            fansCounts: userInfo.fansCounts,
            followCounts: userInfo.followCounts,
            receiveLikeCounts: userInfo.receiveLikeCounts,
            nickname: userInfo.nickname
          })
        } else if(status == 502){
          wx.showToast({
            title: res.data.msg,
            icon: "none",
            duration: 3000
          });
          // 被挤出后清空缓存,设置没登录
          wx.clearStorage();
        }
      }
    })
  },
  // 关注我，取消关注，前面也写过了,可写成全局方法
  followMe(){
    console.log("关注我点击了")
    var me = this;
    var user = app.getGlobalUserInfo();
    var serverUrl = app.serverUrl;
    var publisherInfo = me.data.publisherInfo;
    var userFollowMe = me.data.userFollowMe;
    var url = "/user/beYourFans?userId=" + publisherInfo.userId + "&fanId=" + user.id;
    if (user != null && user != '' && user != undefined) {
      console.log(url);
      console.log(userFollowMe)
      if(userFollowMe){
        url = "/user/dontBeYourFans?userId=" + publisherInfo.userId + "&fanId=" + user.id;
      }
      wx.request({
        url: serverUrl + url,
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
            userFollowMe: !userFollowMe
          });
          app.followMeToVideo = true;
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
  // 作品，喜欢，关注tab
  doSelectWork: function () {
    this.setData({
      isSelectedWork: "video-info-selected",
      isSelectedFollow: "",

      myWorkFalg: false,
      myFollowFalg: true,

      followList: [],
    });

    var userId = this.data.publisherInfo.userId;
    this.getMyVideoList(userId,1)
    
  },


  doSelectFollow: function () {
    this.setData({
      isSelectedWork: "",
      isSelectedFollow: "video-info-selected",

      myWorkFalg: true,
      myFollowFalg: false,

      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      followList: [],
    });
    var userId = this.data.publisherInfo.userId;
    this.getMyFollowList(userId);
  },
  // 获得发布者的作品
  getMyVideoList: function (userId,page) {
    var serverUrl = app.serverUrl;
    var me = this;
    wx.showLoading({
      title: '加载中...',
    })
    wx.request({
      url: serverUrl + "/video/getAll?page=" + page,
      method: 'POST',
      data: {
        userId: userId
      },
      success: function (res) {
        console.log(res.data);
        var myVideoList = res.data.data.rows;
        wx.hideLoading();

        var newVideoList = me.data.myVideoList;
        me.setData({
          myVideoPage: page,
          myVideoList: newVideoList.concat(myVideoList),
          myVideoTotal: res.data.data.total,
          serverUrl: app.serverUrl
        });
      }
    })
  },
    // 我关注的人
  getMyFollowList: function (userId) {
    var serverUrl = app.serverUrl;
    var me = this;
    wx.showLoading({
      title: '加载中...',
    })
    wx.request({
      url: serverUrl + "/video/showMyFollow?userId=" + userId + "&nickname=",
      method: 'POST',
      success: function (res) {
        wx.hideLoading()
        console.log(res.data);
        me.setData({
          serverUrl: serverUrl,
          followList: res.data.data,
        })
      }
    })
  },
  // 到底部后触发加载
  onReachBottom: function () {
    var myWorkFalg = this.data.myWorkFalg;
    var myFollowFalg = this.data.myFollowFalg;
    console.log("342423")
    console.log(myWorkFalg)
    if (!myWorkFalg) {
      var currentPage = this.data.myVideoPage;
      var totalPage = this.data.myVideoTotal;
      // 获取总页数进行判断，如果当前页数和总页数相等，则不分页
      if (currentPage === totalPage) {
        wx.showToast({
          title: '已经没有视频啦...',
          icon: "none"
        });
        return;
      }
      var page = currentPage + 1;
      var userId = this.data.publisherInfo.userId;
      this.getMyVideoList(userId,page);
    } else if (!myFollowFalg) {
      wx.showToast({
        title: '到底了！',
        icon: "none"
      });
    }

  },
  toUserInfo(e){
    console.log(e.target.dataset.arrindex)
    var user = app.getGlobalUserInfo();
    var me = this;
    var followList = me.data.followList
    var index = e.target.dataset.arrindex;
    var userId = user.id; 
    var serverUrl = app.serverUrl;
    if(followList[index].userId == userId){
      wx.switchTab({
        url: '../mine/mine'
      });
      return;
    }
    // 判断你是否关注了该用户(根据nickname)
    wx.showLoading({
      title: '加载中...',
    })
    wx.request({
      url: serverUrl + "/video/showMyFollow?userId=" + user.id + "&nickname=" + followList[index].nickname,
      method: 'POST',
      success: function (res) {
        wx.hideLoading();
        var userFollowMe = true;
        if(res.data.data.length == 0){
          console.log(true);
          userFollowMe = false;
        }
        wx.navigateTo({
          url: '../publisherInfo/publisherInfo?publisherInfo=' + JSON.stringify(followList[index]) + "&userFollowMe=" + userFollowMe,
        })
      }
    })

    
    
  },
  showVideo(e){
    var me = this;
    console.log(e.target.dataset.arrindex);
    var index = e.target.dataset.arrindex;
    var myVideoList = me.data.myVideoList;
    console.log(myVideoList[index]);
    wx.navigateTo({
      url: '../videoInfo/videoInfo?videoItem=' + JSON.stringify(myVideoList[index]),
    })
  }
})