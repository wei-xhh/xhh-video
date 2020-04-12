const app = getApp()

Page({
  data: {
    faceUrl: "/images/content/noneface.png",
    notLogin: true,
    //作品，喜欢，关注
    videoSelClass: "video-info",
    isSelectedWork: "video-info-selected",
    isSelectedLike: "",
    isSelectedFollow: "",

    myVideoList: [],
    myVideoPage: 1,
    myVideoTotal: 1,

    likeVideoList: [],
    likeVideoPage: 1,
    likeVideoTotal: 1,

    followList: [],

    myWorkFalg: false,
    myLikesFalg: true,
    myFollowFalg: true
  },
  onLoad: function(){
    // 查询作品
    this.getMyVideoList(1);
  },
  
  //展示页面时，一直调用接口，后期改进
  onShow: function () {
    var serverUrl = app.serverUrl;
    var me = this;
    var user = app.getGlobalUserInfo(); //app.userInfo

    me.setData({
      faceUrl: "/images/content/noneface.png",
      fansCounts: "",
      followCounts: "",
      receiveLikeCounts: "",
      nickname: ""
    })

    if (user != null && user != '' && user != undefined) {
      
      me.setData({
        notLogin: false
      })

      wx.request({
        url: serverUrl + "/user/query/" + user.id,
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
            // wx.showToast({
            //   title: '查询用户成功',
            //   icon: "success",
            //   duration: 2000
            // });
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
            });
          } else if(status == 502){
            wx.showToast({
              title: res.data.msg,
              icon: "none",
              duration: 3000
            });
            // 被挤出后清空缓存,设置没登录
            wx.clearStorage();
            me.setData({
              notLogin: true
            });
          }

        }
      })
    }
  },
  //退出登录
  logout: function () {
    var me = this;
    var serverUrl = app.serverUrl;
    var user = app.getGlobalUserInfo(); //app.userInfo
    wx.showLoading({
      title: '请等待...',
    });
    // 注销请求
    wx.request({
      url: serverUrl + "/logout/" + user.id,
      method: "POST",
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        wx.hideLoading();
        var status = res.data.status;
        if (status == 200) {
          wx.showToast({
            title: '注销成功',
            icon: "success",
            duration: 3000
          });
          //app.userInfo = null;
          wx.clearStorage(); //清理所有缓存
          me.setData({
            notLogin: true,
          })
          // 注销后清空
          me.setData({
            isSelectedWork: "video-info-selected",
            isSelectedLike: "",
            isSelectedFollow: "",
      
            myWorkFalg: false,
            myLikesFalg: true,
            myFollowFalg: true,
      
            myVideoList: [],
            myVideoPage: 1,
            myVideoTotal: 1,
      
            likeVideoList: [],
            likeVideoPage: 1,
            likeVideoTotal: 1,
      
            followList: [],
          });
          wx.switchTab({
            url: '../index/index',
          })
        }
      }
    })
  },
  //到登录页面
  goLogin: function () {
    wx.navigateTo({
      url: '../userLogin/login',
    });
  },
  // 上传头像
  changeFace: function () {
    var me = this;
    var serverUrl = app.serverUrl;
    var user = app.getGlobalUserInfo();

    if (user == null || user == '' || user == undefined) {
      wx.showToast({
        title: '您还没登录呢!',
        icon: 'none',
        duration: 2000
      })
    } else {
      // 选择头像
      wx.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album'],
        success(res) {
          var tempFilePaths = res.tempFilePaths
          console.log(tempFilePaths);

          wx.showLoading({
            title: '上传中...',
          });
          wx.uploadFile({
            url: serverUrl + "/user/uploadFace/" + user.id,
            filePath: tempFilePaths[0],
            name: 'file',
            method: 'POST',
            success(res) {
              var data = JSON.parse(res.data)
              console.log(data)

              wx.hideLoading();
              if (data.status == 200) {
                wx.showToast({
                  title: '上传成功！',
                  icon: 'success',
                  duration: 2000
                });
                // 获取图片保存的路径并设置
                me.setData({
                  faceUrl: serverUrl + data.data
                });

              } else if (data.status == 500) {
                wx.showToast({
                  title: data.msg,
                  icon: 'none',
                  duration: 3000
                });
              }

            }
          })

        }
      });
    }


  },
  // 作品，喜欢，关注tab
  doSelectWork: function () {
    this.setData({
      isSelectedWork: "video-info-selected",
      isSelectedLike: "",
      isSelectedFollow: "",

      myWorkFalg: false,
      myLikesFalg: true,
      myFollowFalg: true,

      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followList: [],
    });
    this.getMyVideoList(1)
  },

  doSelectLike: function () {
    this.setData({
      isSelectedWork: "",
      isSelectedLike: "video-info-selected",
      isSelectedFollow: "",

      myWorkFalg: true,
      myLikesFalg: false,
      myFollowFalg: true,

      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followList: [],
    });
    this.getMyLikesList(1)
  },

  doSelectFollow: function () {
    this.setData({
      isSelectedWork: "",
      isSelectedLike: "",
      isSelectedFollow: "video-info-selected",

      myWorkFalg: true,
      myLikesFalg: true,
      myFollowFalg: false,

      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followList: [],
    });
    this.getMyFollowList()
  },
  // 获得我的作品
  getMyVideoList: function (page) {
    var user = app.getGlobalUserInfo();
    var serverUrl = app.serverUrl;
    var me = this;
    wx.showLoading({
      title: '加载中...',
    })
    wx.request({
      url: serverUrl + "/video/getAll?page=" + page,
      method: 'POST',
      data: {
        userId: user.id
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
  // 我喜欢的视频
  getMyLikesList: function (page) {
    var user = app.getGlobalUserInfo();
    var serverUrl = app.serverUrl;
    var me = this;
    wx.showLoading({
      title: '加载中...',
    })
    wx.request({
      url: serverUrl + "/video/showMyLike?userId=" + user.id + "&page=" + page,
      method: 'POST',
      success: function (res) {
        console.log(res.data);
        var likeVideoList = res.data.data.rows;
        wx.hideLoading();

        var newVideoList = me.data.likeVideoList;
        me.setData({
          likeVideoPage: page,
          likeVideoList: newVideoList.concat(likeVideoList),
          likeVideoTotal: res.data.data.total,
          serverUrl: app.serverUrl
        });
      }
    })
  },
  // 我关注的人
  getMyFollowList: function () {
    var user = app.getGlobalUserInfo();
    var serverUrl = app.serverUrl;
    var me = this;
    wx.showLoading({
      title: '加载中...',
    })
    wx.request({
      url: serverUrl + "/video/showMyFollow?userId=" + user.id + "&nickname=",
      method: 'POST',
      success: function (res) {
        wx.hideLoading();
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
    var myLikesFalg = this.data.myLikesFalg;
    var myFollowFalg = this.data.myFollowFalg;

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
      console.log("1121212")
      var page = currentPage + 1;
      this.getMyVideoList(page);
    } else if (!myLikesFalg) {
      var currentPage = this.data.likeVideoPage;
      var totalPage = this.data.myLikesTotal;
      // 获取总页数进行判断，如果当前页数和总页数相等，则不分页
      if (currentPage === totalPage) {
        wx.showToast({
          title: '已经没有视频啦...',
          icon: "none"
        });
        return;
      }
      var page = currentPage + 1;
      this.getMyLikesList(page);
    } else if (!myFollowFalg) {
      wx.showToast({
        title: '去关注更多人吧！',
        icon: "none"
      });
    }

  },
  toUserInfo(e){
    var me = this;
    console.log(e.target.dataset.arrindex);
    var index = e.target.dataset.arrindex;
    var followList = me.data.followList
    wx.navigateTo({
      url: '../publisherInfo/publisherInfo?publisherInfo=' + JSON.stringify(followList[index]) + "&userFollowMe=true"
    })
  },
  showVideo(e){
    var me = this;
    console.log(e.target.dataset.arrindex);
    var index = e.target.dataset.arrindex;
    var myVideoList = me.data.myVideoList;
    console.log(myVideoList[index]);
    if(myVideoList.length == 0){
      myVideoList = me.data.likeVideoList
    }
    wx.navigateTo({
      url: '../videoInfo/videoInfo?videoItem=' + JSON.stringify(myVideoList[index]),
    })
  }
})