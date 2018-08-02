
var app = getApp();
// pages/message/system/system.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    pageNum:1,
    pageSize:10,
    pageTotal: 0,
    imgUrl: app.globalData.ImgUrl,
    hasMoreData:true,
    systemUrl: app.globalData.baseUrl +'/order'
  },
  getDetailTap: function (event) {

    var orderid = event.currentTarget.dataset.orderid;

    wx.navigateTo({
      url: './system-detail/system-detail?orderid=' + orderid,
    })
  },
  getData: function (pageNum){
    let that = this;
    wx.request({
      url: this.data.systemUrl + "?pageNum=" + pageNum,
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      // data: { pageInfo: pageInfo  },
      success: function (data) {
        if (data.data.resultCode === 200) {
          let hasMoreData = true; 
          if (data.data.data.length < that.data.pageSize){
            hasMoreData = false;
          }
          if (pageNum>1){
            that.setData({
              systemOrder: that.data.systemOrder.concat(data.data.data),
              pageTotal: data.data.pageInfo.total,
              pageNum: data.data.pageInfo.pageNum,
              hasMoreData
            })
          }else{
            that.setData({
              systemOrder: data.data.data,
              pageTotal: data.data.pageInfo.total,
              pageNum: data.data.pageInfo.pageNum,
              hasMoreData
            })
          }
        } else {
          wx.showToast({
            title: '系统异常',
            icon: 'none',
            duration: 2000
          })
        }
      },
      fail: function () {
        wx.showToast({
          title: '网络异常',
          icon: 'none',
          duration: 2000
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getData(1);
  },
  onReachBottom: function () {
    if (this.data.hasMoreData) {
      let pageNum = this.data.pageNum +1
      this.getData(pageNum)
    } else {
      wx.showToast({
        title: '没有更多数据',
      })
    }
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
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})