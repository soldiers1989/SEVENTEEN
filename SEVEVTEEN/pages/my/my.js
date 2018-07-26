const app = getApp()


Page({

  /**
   * 页面的初始数据
   */
  data: {
    routers: [{
        name: '优惠券',
        url: '/pages/money/coupon-detail/coupon-detail',
        icon: '/imgs/my/youh.png',
        code: '10',
        flag: true
      },
      {
        name: '订单',
        url: '/pages/my/myOrder/myOrder',
        icon: '/imgs/my/dingd.png',
        code: '11',
        flag: true
      },
      {
        name: '积分',
        url: '/pages/my/integral/integral',
        icon: '/imgs/my/jif.png',
        code: '10',
        flag: true
      },
      {
        name: '身份验证',
        url: '/pages/my/rlsb/rlsb',
        icon: '/imgs/my/shenfyz.png',
        code: '11',
        flag: true
      },
      {
        name: '评价',
        url: '/pages/message/reply/reply',
        icon: '/imgs/my/pingj.png',
        code: '10',
        flag: true
      },

      {
        name: '会员权益',
        url: '/pages/Course/course',
        icon: '/imgs/my/huiyqy.png',
        code: '10',
        flag: true
      }, {
        name: '客服',
        icon: '/imgs/my/kef.png',
        code: '11',
        flag: true
      },
      {
        name: '入住Q&A',
        icon: '/imgs/my/zhix.png',
        code: '11',
        flag: true
      },
      {
        name: '投诉建议',
        url: '/pages/my/toushu/toushu',
        icon: '/imgs/my/tous.png',
        code: '10',
        flag: true
      }
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    var that = this;
    var token = wx.getStorageSync('token');

    wx.request({
      url: app.globalData.baseUrl + "/rlsb/isVerify",
      method: "GET",
      header: {
        "Authorization": "Bearer " + token
      },
      success: function (res) {
        console.log(res.data.data);

        var _obj = that.data.routers;
        _obj[3].flag = res.data.data==true?false:true;
        that.setData({
          routers:_obj
        })
        console.log(_obj);
      }
    })
    // console.log(data);
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
    // loadIsVerify();
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

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  },
  toPage: function(e) {
    var _datasetId = e.currentTarget.dataset;
    if (_datasetId.flag) {

      wx.navigateTo({
        url: _datasetId.xxurl,
      })
    } else {
      wx.showToast({
        title: '已验证',
        icon: "success",
        duration: 2000,
      })
    }

  }

})

// function loadIsVerify() {
  

// }