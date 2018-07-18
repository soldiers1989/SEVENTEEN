const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    content:'',
    phone:'',
    adviseUrl: app.globalData.baseUrl + '/room/wx/advise',
  },
  bindTextAreaBlur: function (e) {
    this.setData({
      content: e.detail.value
    })
  }, 
  bindPhoneAreaBlur: function (e) {
    this.setData({
      phone: e.detail.value
    })
  },
  addAdviseTap: function (e) {
    let content = this.data.content;
    let phone = this.data.phone;

    if (content === '' || content === null) {
      wx.showToast({
        title: '请填写评价内容',
        icon: 'none',
        duration: 2000
      })
      return;
    }
    if (phone === '' || phone === null) {
      wx.showToast({
        title: '请留下你的联系电话',
        icon: 'none',
        duration: 2000
      })
      return;
    }

    let that = this;
    wx.request({
      url: this.data.adviseUrl,
      method: 'post',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      data: {
        content,
        phone
      },
      success: function (data) {
        if (data.data.resultCode === 200) {
          wx.showToast({
            title: '提交成功',
            icon: 'success',
            duration: 2000
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