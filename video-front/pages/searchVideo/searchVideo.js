// 1 导入js文件
var WxSearch = require('../../wxSearchView/wxSearchView.js');
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    serverUrl:"",
  },

  onLoad: function () {
    var me = this;
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '加载中...'
    })
    wx.request({
      url: serverUrl + '/video/hot',
      method: 'POST',
      success: function(res){
        wx.hideLoading();
        console.log(res.data)
        var hotList = res.data.data
        // 搜索栏初始化
        WxSearch.init(
          me,  // 本页面一个引用
          hotList, // 热点搜索推荐，[]表示不使用
          hotList,// 搜索匹配，[]表示不使用
          me.mySearchFunction, // 提供一个搜索回调函数
          me.myGobackFunction //提供一个返回回调函数
        );
      }
    })
    
  },

  // 3 转发函数，固定部分，直接拷贝即可
  wxSearchInput: WxSearch.wxSearchInput,  // 输入变化时的操作
  wxSearchKeyTap: WxSearch.wxSearchKeyTap,  // 点击提示或者关键字、历史记录时的操作
  wxSearchDeleteAll: WxSearch.wxSearchDeleteAll, // 删除所有的历史记录
  wxSearchConfirm: WxSearch.wxSearchConfirm,  // 搜索函数
  wxSearchClear: WxSearch.wxSearchClear,  // 清空函数

  // 4 搜索回调函数  
  mySearchFunction: function (value) {
    // do your job here
    // 示例：跳转
    wx.showToast({
      title: '搜索成功！',
      icon: 'success',
      duration: 3000
    })
    app.isSaveRecord = 1;
    app.searchValue = value,
    app.searchToVideo = true
    wx.switchTab({
      url: '../index/index'
    })
  },

  // 5 返回回调函数
  myGobackFunction: function () {
    // do your job here
    // 示例：返回
    wx.switchTab({
      url: '../index/index',
    })
  }
})