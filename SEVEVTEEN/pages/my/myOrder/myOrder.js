const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    allOrderPageNum: 1,
    allOrderPageSize: 10,
    allOrderPageTotal: 0,
    waitLivePageNum: 1,
    waitLivePageSize: 10,
    waitLivePageTotal: 0,
    allOrderHasMoreData: true,
    waitLiveHasMoreData: true,
    tabArray: ["全部", "待入住"],
    currentTab: 0,
    allOrder: [],
    waitLive: [],
    systemUrl: app.globalData.baseUrl + '/order'
  },
  swichNav: function(e) {
    var currentTab = e.currentTarget.dataset.current
    this.setData({
      currentTab
    })
  },
  getDetailTap: function(e) {
    var orderid = e.currentTarget.dataset.orderid;
    wx.navigateTo({
      url: '/pages/message/system/system-detail/system-detail?orderid=' + orderid,
    })
  },
  deleteOrderTap: function(e) {
    var orderid = e.currentTarget.dataset.orderid;
    let that = this;
    wx.request({
      url: this.data.systemUrl + "/wx" + "?id=" + orderid,
      method: 'delete',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      // data: {
      //   id : orderid
      // },
      success: function(data) {
        if (data.data.resultCode === 200) {
          that.getAllOrderData(1);
          wx.showToast({
            title: '删除成功',
            icon: 'success',
            duration: 2000
          });
        } else if (data.data.resultCode === 300){
          wx.showToast({
            title: data.data.message,
            icon: 'none',
            duration: 2500
          });
        }
        else {
          wx.showToast({
            title: '系统异常',
            icon: 'none',
            duration: 2000
          })
        }
      },
      fail: function() {
        wx.showToast({
          title: '网络异常',
          icon: 'none',
          duration: 2000
        })
      }
    })
  },

  getAllOrderData: function(pageNum) {
    let that = this;
    wx.request({
      url: this.data.systemUrl + "?pageNum=" + pageNum,
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      // data: { pageInfo: pageInfo  },
      success: function(data) {
        if (data.data.resultCode === 200) {
          let allOrderHasMoreData = true;
          if (data.data.data.length < that.data.allOrderPageSize) {
            allOrderHasMoreData = false;
          }
          if (pageNum > 1) {
            that.setData({
              allOrder: that.data.allOrder.concat(data.data.data),
              allOrderPageTotal: data.data.pageInfo.total,
              allOrderPageNum: data.data.pageInfo.pageNum,
              allOrderHasMoreData
            })
          } else {
            that.setData({
              allOrder: data.data.data,
              allOrderPageTotal: data.data.pageInfo.total,
              allOrderPageNum: data.data.pageInfo.pageNum,
              allOrderHasMoreData
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
      fail: function() {
        wx.showToast({
          title: '网络异常',
          icon: 'none',
          duration: 2000
        })
      }
    })
  },
  getWaitLiveData: function(pageNum) {
    let that = this;
    wx.request({
      url: this.data.systemUrl + "?pageNum=" + pageNum + "&&status=1",
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      // data: { pageInfo: pageInfo  },
      success: function(data) {
        if (data.data.resultCode === 200) {
          let waitLiveHasMoreData = true;
          if (data.data.data.length < that.data.waitLivePageSize) {
            waitLiveHasMoreData = false;
          }
          if (pageNum > 1) {
            that.setData({
              waitLive: that.data.waitLive.concat(data.data.data),
              waitLivePageTotal: data.data.pageInfo.total,
              waitLivePageNum: data.data.pageInfo.pageNum,
              waitLiveHasMoreData
            })
          } else {
            that.setData({
              waitLive: data.data.data,
              waitLivePageTotal: data.data.pageInfo.total,
              waitLivePageNum: data.data.pageInfo.pageNum,
              waitLiveHasMoreData
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
      fail: function() {
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
  onLoad: function(options) {
    this.getAllOrderData(1);
    this.getWaitLiveData(1);
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {
    if (this.data.currentTab === 0) {
      if (this.data.allOrderHasMoreData) {
        let pageNum = this.data.allOrderPageNum + 1
        this.getAllOrderData(pageNum)
      } else {
        wx.showToast({
          title: '没有更多数据',
        })
      }
    } else {
      if (this.data.waitLiveHasMoreData) {
        let pageNum = this.data.waitLivePageNum + 1
        this.getWaitLiveData(pageNum)
      } else {
        wx.showToast({
          title: '没有更多数据',
        })
      }
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})