const app = getApp()

Page({
    data: {
      
    },
    doLogin:function(e){
      // 获得表单的值
      var formObject = e.detail.value;
      var username = formObject.username;
      var password = formObject.password;

      // 判断账号密码是否为空
      if (username.length == 0 || password.length == 0) {
        wx.showToast({
          title: '账号或密码为空',
          icon: "none",
          duration: 3000
        })
      } else {
        var serverUrl = app.serverUrl;
        wx.showLoading({
          title: '请等待...',
        });
        // 登录请求
        wx.request({
          url: serverUrl + "/login",
          method: "POST",
          header: {
            'content-type': 'application/json' // 默认值
          },
          data:{
            username: username,
            password: password
          },
          success: function(res){
            wx.hideLoading();
            var status = res.data.status;
            if (status == 200) {
              wx.showToast({
                title: '登录成功!~~',
                icon: "success",
                duration: 3000
              });
              // 将用户信息存入全局userInfo
              // app.userInfo = res.data.data;
              // 用户信息存入缓存
              app.setGlobalUserInfo(res.data.data)
              console.log(res.data);
              //成功后跳转mine
              wx.switchTab({
                url: '../mine/mine',
              })
              
            } else if (status == 500) {
              wx.showToast({
                title: res.data.msg,
                icon: "none",
                duration: 3000
              });
            }
          }
        })
      }
    },
    goRegistPage:function(){
      wx.navigateTo({
        url: '../userRegist/regist',
      })
    }
})