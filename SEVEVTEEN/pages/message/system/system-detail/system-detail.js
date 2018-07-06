const app = getApp()
// pages/message/system/system.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imgUrl: app.globalData.ImgUrl,
    systemUrl: app.globalData.baseUrl + '/order'

  },
  addLiverTap: function (event) {
    var orderid = event.currentTarget.dataset.orderid;

    if (this.data.orderStatus === 2 || this.data.orderStatus === 1) {
      return;
    }
    wx.navigateTo({
      url: './add-liver/add-liver?orderid=' + orderid,
    })
  },
  liverTap: function (event) {
    if (this.data.orderStatus === 1) {
      wx.navigateTo({
        url: '/pages/order/order',
      })
    }

  },
  wifiTap: function (event) {
    if (this.data.orderStatus === 2) {
      return;
    }
    wx.showModal({
      title: '提示',
      showCancel: false,
      content: 'wifi密码：1123546X23'
    })
  },
  cancelTap: function (event) {
    if (this.data.systemDetail.remark === '不支持退订') {
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: '该房间类型不支持退订'
      })
      return;
    }
    wx.showModal({
      title: '提示',
      content: '是否申请房间退订',
      success: function (res) {
        if (res.confirm) {
          console.log('用户点击确定')
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })
  },
  callClientTap: function (event) {

    wx.makePhoneCall({
      phoneNumber: '1340000' //仅为示例，并非真实的电话号码
    })
  },
  replyToTap: function (e) {
    if (this.data.orderStatus === 0) {
      return;
    }
    wx.navigateTo({
      url: '/pages/message/reply/evaluate/evaluate',
    })
  },
  openDoorTap: function (event) {
    if (this.data.orderStatus != 1) {
      return;
    }
    wx.showModal({
      title: '提示',
      content: '是否打开房门',
      success: function (res) {
        if (res.confirm) {
          console.log('用户点击确定')
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })
  },
  cleanTap: function (event) {
    if (this.data.orderStatus != 1) {
      return;
    }
    wx.showModal({
      title: '提示',
      content: '房间清洁时间于每天的12: 00~14:00 是否需要清洁您的房间?',
      success: function (res) {
        if (res.confirm) {
          console.log('用户点击确定')
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })
  },
  getData: function (id) {
    let that = this;
    wx.request({
      url: this.data.systemUrl + '/' + id + "/detail",
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      // data: { pageInfo: pageInfo  },
      success: function (data) {
        if (data.data.resultCode === 200) {
          let status = data.data.data.status;
          let orderStatus;
          if (status === '已下订' || status === '退订中') {
            orderStatus = 0;
          } else if (status === '已入住') {
            orderStatus = 1;
          } else if (status === '已退订' || status === '订单完成') {
            orderStatus = 2;
          }
          that.setData({
            systemDetail: data.data.data,
            orderStatus
          })
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
    let orderid = options.orderid;
    this.getData(orderid);
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

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})