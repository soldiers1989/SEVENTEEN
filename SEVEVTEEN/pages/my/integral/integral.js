const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userPoint:0,
    imgUrl: app.globalData.ImgUrl,
    baseUrl: app.globalData.baseUrl,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var token = wx.getStorageSync('token');
    var that=this;
    wx.request({
      url: that.data.baseUrl + "/point/userPoint",
      method: 'GET',
      header: {
        Authorization: 'Bearer ' + token
      },
      success: function (res) {
        // console.log(res.data.data)
        that.setData({
          userPoint: res.data.data
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    
  },

  toJFMX:function(){
    wx.navigateTo({
      url: '/pages/money/point-detail/point-detail'
    })
  }

})