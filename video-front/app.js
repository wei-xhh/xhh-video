//app.js
App({
  serverUrl: "http://192.168.0.110:8081",
  // userInfo: null,
  toIndex: false, //上传作品后到首页重新加载index
  setGlobalUserInfo(data){
    wx.setStorageSync('userInfo', data)
  },
  getGlobalUserInfo(){
    return wx.getStorageSync('userInfo')
  },
  isSaveRecord: 0, // 查询视频是否要保存热搜词，页面跳转需要的参数(由于调到页面不能传参)
  searchValue: "",  // 热搜词
  searchToVideo: false, //搜索后跳转到videoInfo
  followMeToVideo: false //关注刷新 

})