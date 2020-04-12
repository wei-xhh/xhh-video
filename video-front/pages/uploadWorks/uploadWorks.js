//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    notLogin: true,
    bgmList: []
  },
  onLoad() {
    var me = this;
    var serverUrl = app.serverUrl;

    wx.request({
      url: serverUrl + "/bgm/list",
      method: "POST",
      header: {
        'content-type': 'application/json', // 默认值
      },
      success: function (res) {
        console.log(res.data.data)
        if(res.data.status == 200){
          var bgmList = res.data.data;
          me.setData({
            serverUrl: serverUrl,
            bgmList: bgmList
          })
        }
      }
    })
  },

  onShow() {
    var user = app.getGlobalUserInfo(); //app.userInfo
    var me = this;

    setTimeout(function () {
      if (user != null && user != '' && user != undefined) {
        me.setData({
          notLogin: false
        });
      } else {
        me.setData({
          notLogin: true
        });
      }
    }, 1000)
  },

  // 上传视频通过相册
  uploadVideoUseB() {
    var me = this;
    
    wx.chooseVideo({
      sourceType: ['album'],
      maxDuration: 60,
      camera: 'back',
      success(res) {
        console.log(res);
        var duration = res.duration;
        var height = res.height;
        var width = res.width;
        var tempVideoUrl = res.tempFilePath;

        if(duration > 60){
          wx.showToast({
            title: '视频时长不能超过40秒',
            icon: "none",
            duration: 2000
          })
        } else if(duration < 3){
          wx.showToast({
            title: '视频时长不能小于3秒',
            icon: "none",
            duration: 2000
          })
        } else {
          // 将数据保存下来
          wx.showToast({
            title: '已保存',
            icon: 'none',
            duration: 3000
          })
          me.setData({
            duration: duration,
            height: height,
            width: width,
            tempVideoUrl: tempVideoUrl
          });
        }
      }
    })
  },
  // 表单触发提交事件
  upload(e) {
    var me = this;
    var serverUrl = app.serverUrl;
    var user = app.getGlobalUserInfo(); //app.userInfo
    var bgmId = e.detail.value.bgmId;
    var desc = e.detail.value.desc;
    var duration = me.data.duration;
    var height = me.data.height;
    var width = me.data.width;
    var tempVideoUrl = me.data.tempVideoUrl;

    if(duration == null || duration == '' || duration == undefined){
      wx.showToast({
        title: '您还没有上传视频呢',
        icon: 'none',
        duration: 2000
      })
      return;
    }
    // 上传视频到后台
    wx.showLoading({
      title: '上传中...',
    })
    wx.uploadFile({
      url: serverUrl + "/video/upload/" + user.id,
      filePath: tempVideoUrl,
      formData:{
        bgmId: bgmId,
        videoSeconds: duration,
        videoWidth: width,
        videoHeight: height,
        desc: desc
      },
      name: 'file',
      method: 'POST',
      success(res) {
        wx.hideLoading();
        var data = JSON.parse(res.data);
        if (data.status == 200) {
          wx.showToast({
            title: "上传成功！",
            icon: 'success',
            duration: 2000
          });
          console.log("11111")
          app.toIndex = true;
          console.log(app.toIndex)
          wx.switchTab({
            url: '../index/index'
          })
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
})