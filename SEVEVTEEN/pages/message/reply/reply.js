const app = getApp()

// pages/message/reply/reply.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    pageNum: 1,
    pageSize: 10,
    pageTotal: 0,
    replyPageNum: 1,
    replyPageSize: 10,
    replyPageTotal: 0,
    noReplyhasMoreData: true,
    replyhasMoreData: true,
    tabArray: ["已点评","待点评"],
    currentTab: 0,
    imgUrl: app.globalData.ImgUrl,
    replyUrl: app.globalData.baseUrl + '/order',
    assessUrl: app.globalData.baseUrl + '/assess'
  },
  swichNav: function (e) {
    var currentTab = e.currentTarget.dataset.current
    this.setData({
      currentTab
    })
  },
  replyToTap: function (e) {
    var orderid = e.currentTarget.dataset.orderid;

    wx.navigateTo({
      url: './evaluate/evaluate?orderid=' + orderid,
    })
  },
  getReplyData: function (replyPageNum) {
    let that = this;
    wx.request({
      url: this.data.assessUrl + "/wx?pageNum=" + replyPageNum,
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      success: function (data) {
        if (data.data.resultCode === 200) {
          let replyhasMoreData = true;
          if (data.data.data.length < that.data.replyPageSize) {
            replyhasMoreData = false;
          }
          if (replyPageNum > 1) {
            that.setData({
              reply: that.data.systemOrder.concat(data.data.data),
              replyPageTotal: data.data.pageInfo.total,
              replyPageNum: data.data.pageInfo.pageNum,
              replyhasMoreData
            })
          } else {
            that.setData({
              reply: data.data.data,
              replyPageTotal: data.data.pageInfo.total,
              replyPageNum: data.data.pageInfo.pageNum,
              replyhasMoreData
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
  getNoReplyData: function (pageNum) {
    let that = this;
    wx.request({
      url: this.data.replyUrl + "/wx?pageNum=" + pageNum + '&&reply=' + 0,
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      // data: { pageInfo: pageInfo  },
      success: function (data) {
        if (data.data.resultCode === 200) {
          let noReplyhasMoreData = true;
          if (data.data.data.length < that.data.pageSize) {
            noReplyhasMoreData = false;
          }
          if (pageNum > 1) {
            that.setData({
              noReply: that.data.noReply.concat(data.data.data),
              pageTotal: data.data.pageInfo.total,
              pageNum: data.data.pageInfo.pageNum,
              noReplyhasMoreData
            })
          } else {
            that.setData({
              noReply: data.data.data,
              pageTotal: data.data.pageInfo.total,
              pageNum: data.data.pageInfo.pageNum,
              noReplyhasMoreData
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
    let flag = options.success;
    if (flag==="1"){
      wx.showToast({
        title: '评价成功',
        icon: 'success',
        duration: 2000
      })
    }
    this.getNoReplyData(1);
    this.getReplyData(1);
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
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    if (this.data.currentTab===1){
      if (this.data.noReplyhasMoreData) {
        let pageNum = this.data.pageNum + 1
        this.getNoReplyData(pageNum)
      } else {
        wx.showToast({
          title: '没有更多数据',
        })
      }
    }else{
      if (this.data.replyhasMoreData) {
        let replyPageNum = this.data.replyPageNum + 1
        this.getReplyData(replyPageNum)
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
  onShareAppMessage: function () {
  
  }
})