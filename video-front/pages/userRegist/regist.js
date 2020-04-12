const app = getApp()

Page({
    data: {

    },
    doRegist: function(e){
      // 获得表单的值
      var formObject = e.detail.value;
      var username = formObject.username;
      var password = formObject.password;
      
      // 判断账号密码是否为空
      if(username.length == 0 || password.length == 0){
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
        wx.request({
          url: serverUrl + "/register",
          method: "POST",
          data:{
            username:username,
            password:password
          },
          header: {
            'content-type': 'application/json' // 默认值
          },
          success: function(res){
            wx.hideLoading();
            var status = res.data.status;
            if(status == 200){
              wx.showToast({
                title: '注册成功!~~',
                icon: "none",
                duration: 3000
              });
              // 将用户信息存入全局userInfo
              // app.userInfo = res.data.data;
              console.log(res.data);

              // 跳转登录页面
              wx.navigateTo({
                url: '../userLogin/login',
              })
            } else if(status == 500){
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
    goLoginPage:function(){
      wx.navigateTo({
        url: '../userLogin/login',
      })
    }
})