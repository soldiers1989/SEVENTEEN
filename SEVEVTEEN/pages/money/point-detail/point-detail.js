const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userPoint: 0,
    imgUrl: app.globalData.ImgUrl,
    baseUrl: app.globalData.baseUrl,
    userPointDto:null,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var token = wx.getStorageSync('token');
    var that = this;
    wx.request({
      url: that.data.baseUrl + "/point/detailList",
      method: 'GET',
      header: {
        Authorization: 'Bearer ' + token
      },
      success: function (res) {
         console.log(res.data.data)
        that.setData({
          userPointDto: res.data.data
        })
      }
    })
  }
  
})