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
    var sid=e.currentTarget.dataset.id;
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
    wx.getLocation({
      type: 'gcj02', // 默认为 wgs84 返回 gps 坐标，gcj02 返回可用于 wx.openLocation 的坐标  
      success: function (res) {
        // success  
        wx.openLocation({
          latitude: res.latitude, // 纬度，范围为-90~90，负数表示南纬  
          longitude: res.longitude, // 经度，范围为-180~180，负数表示西经  
          scale: 28, // 缩放比例   
          name:"巨大创意园",
          address:"会江地铁站C出口"         
        })
      }
    });
    // wx.navigateTo({
    //   url: '../maps/map'
    // })
  }

 
})
