var app = getApp();

Page({
  data: {
    canUsePageNum: 1,
    canUsePageSize: 10,
    canUsePageTotal: 0,
    hadUsePageNum: 1,
    hadUsePageSize: 10,
    hadUsePageTotal: 0,
    overduePageNum: 1,
    overduePageSize: 10,
    overduePageTotal: 0,
    canUseHasMoreData: true,
    hadUseHasMoreData: true,
    overdueHasMoreData: true,
    imgUrl: app.globalData.ImgUrl,
    tabArray: ["可使用", "已过期", "已使用"],
    focus: false,
    inputValue: '',
    currentTab: 0,
    canUse: [],
    hadUse: [],
    overdue: [],
    couponUrl: app.globalData.baseUrl + '/coupon'
  },
  swichNav: function(e) {
    var currentTab = e.currentTarget.dataset.current
    this.setData({
      currentTab
    })
  },
  getCanUseCouponData: function(status, pageNum) {
    let that = this;
    wx.request({
      url: this.data.couponUrl + "/wx?status=" + status + "&&pageNum=" + pageNum,
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      // data: { pageInfo: pageInfo  },
      success: function(data) {
        if (data.data.resultCode === 200) {
          let canUseHasMoreData = true;
          if (data.data.data.length < that.data.canUsePageSize) {
            canUseHasMoreData = false;
          }
          if (pageNum > 1) {
            that.setData({
              canUse: that.data.canUse.concat(data.data.data),
              canUsePageTotal: data.data.pageInfo.total,
              canUsePageNum: data.data.pageInfo.pageNum,
              canUseHasMoreData
            })
          } else {
            that.setData({
              canUse: data.data.data,
              canUsePageTotal: data.data.pageInfo.total,
              canUsePageNum: data.data.pageInfo.pageNum,
              canUseHasMoreData
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
  getHadUseCouponData: function(status, pageNum) {
    let that = this;
    wx.request({
      url: this.data.couponUrl + "/wx?status=" + status + "&&pageNum=" + pageNum,
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      // data: { pageInfo: pageInfo  },
      success: function(data) {
        if (data.data.resultCode === 200) {
          let hadUseHasMoreData = true;
          if (data.data.data.length < that.data.hadUsePageSize) {
            hadUseHasMoreData = false;
          }
          if (pageNum > 1) {
            that.setData({
              hadUse: that.data.hadUse.concat(data.data.data),
              hadUsePageTotal: data.data.pageInfo.total,
              hadUsePageNum: data.data.pageInfo.pageNum,
              hadUseHasMoreData
            })
          } else {
            that.setData({
              hadUse: data.data.data,
              hadUsePageTotal: data.data.pageInfo.total,
              hadUsePageNum: data.data.pageInfo.pageNum,
              hadUseHasMoreData
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
  getOverdueCouponData: function(status, pageNum) {
    let that = this;
    wx.request({
      url: this.data.couponUrl + "/wx?status=" + status + "&&pageNum=" + pageNum,
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      // data: { pageInfo: pageInfo  },
      success: function(data) {
        if (data.data.resultCode === 200) {
          let overdueHasMoreData = true;
          if (data.data.data.length < that.data.overduePageSize) {
            overdueHasMoreData = false;
          }
          if (pageNum > 1) {
            that.setData({
              overdue: that.data.overdue.concat(data.data.data),
              overduePageTotal: data.data.pageInfo.total,
              overduePageNum: data.data.pageInfo.pageNum,
              overdueHasMoreData
            })
          } else {
            that.setData({
              overdue: data.data.data,
              overduePageTotal: data.data.pageInfo.total,
              overduePageNum: data.data.pageInfo.pageNum,
              overdueHasMoreData
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
  getCouponData: function() {
    let that = this;
    wx.request({
      url: this.data.couponUrl + "/wx/exchange?id=" + that.data.inputValue,
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      // data: { pageInfo: pageInfo  },
      success: function(data) {
        if (data.data.resultCode === 200) {
          that.getCanUseCouponData(1, 1);
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
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {
    if (this.data.currentTab === 0) {
      if (this.data.canUseHasMoreData) {
        let canUsePageNum = this.data.canUsePageNum + 1
        this.getCanUseCouponData(1, canUsePageNum)
      } else {
        wx.showToast({
          title: '没有更多数据',
        })
      }
    } else if (this.data.currentTab === 1) {
      if (this.data.hadUseHasMoreData) {
        let hadUsePageNum = this.data.hadUsePageNum + 1
        this.getNotUseCouponData(1, hadUsePageNum)
      } else {
        wx.showToast({
          title: '没有更多数据',
        })
      }
    } else {
      if (this.data.overdueHasMoreData) {
        let overduePageNum = this.data.overduePageNum + 1
        this.getOverdueCouponData(1, overduePageNum)
      } else {
        wx.showToast({
          title: '没有更多数据',
        })
      }
    }
  },
  listenerInput: function(e) {
    this.data.inputValue = e.detail.value;
  },
  useCouponTap: function() {
    wx.switchTab({
      url: '/pages/index/index',
    })
  },
  bindButtonTap: function() {
    console.log(this.data.inputValue);
    this.getCouponData();
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.getCanUseCouponData(1, 1);
    this.getOverdueCouponData(2, 1);
    this.getHadUseCouponData(3, 1);
  },
})