//hello
//hello
//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    tabArr: {
      curHdIndex: "t1"
      //  curBdIndex: 0 
    }
  },
  tabSel: function (e) {

    var _datasetId = e.target.dataset.id;
    console.log(e.currentTarget.dataset.id)
    var _obj = {};
    _obj.curHdIndex = _datasetId;
    // _obj.curBdIndex = _datasetId;
    this.setData({ tabArr: _obj });
  },
  //日历模态框
  showDates: function () {
    wx.navigateTo({
      url: '../dates/date'
    })
  },
  showMap: function () {
    wx.navigateTo({
      url: '../maps/map'
    })
  }

  // wx.showModal({
  //   title: '提示',
  //   content: '<text class="addres-text">体育西路13号骏汇大厦2楼 体育西路G出口</text>',
  //   success: function (res) {
  //     if (res.confirm) {
  //       console.log('用户点击确定')
  //     } else if (res.cancel) {
  //       console.log('用户点击取消')
  //     }
  //   }
  // })


  // onLoad:function(){
  //   var _obj = {};
  //   _obj.curHdIndex = "t1";
  //   this.setDate({ tabArr: _obj })
  // }



  // data: {
  //   motto: 'Hello World',
  //   userInfo: {},
  //   hasUserInfo: false,
  //   canIUse: wx.canIUse('button.open-type.getUserInfo')
  // },
  // //事件处理函数
  // bindViewTap: function() {
  //   wx.navigateTo({
  //     url: '../logs/logs'
  //   })
  // },
  // onLoad: function () {
  //   if (app.globalData.userInfo) {
  //     this.setData({
  //       userInfo: app.globalData.userInfo,
  //       hasUserInfo: true
  //     })
  //   } else if (this.data.canIUse){
  //     // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
  //     // 所以此处加入 callback 以防止这种情况
  //     app.userInfoReadyCallback = res => {
  //       this.setData({
  //         userInfo: res.userInfo,
  //         hasUserInfo: true
  //       })
  //     }
  //   } else {
  //     // 在没有 open-type=getUserInfo 版本的兼容处理
  //     wx.getUserInfo({
  //       success: res => {
  //         app.globalData.userInfo = res.userInfo
  //         this.setData({
  //           userInfo: res.userInfo,
  //           hasUserInfo: true
  //         })
  //       }
  //     })
  //   }
  // },
  // getUserInfo: function(e) {
  //   console.log(e)
  //   app.globalData.userInfo = e.detail.userInfo
  //   this.setData({
  //     userInfo: e.detail.userInfo,
  //     hasUserInfo: true
  //   })
  // }
})
