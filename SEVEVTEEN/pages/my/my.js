Page({

  /**
   * 页面的初始数据
   */
  data: {
    routers: [{
        name: '优惠券',
        url: '/pages/money/coupon-detail/coupon-detail',
        icon: '/imgs/my/youh.png',
        code: '10'
      },
      {
        name: '订单',
        url: '/pages/my/myOrder/myOrder',
        icon: '/imgs/my/dingd.png',
        code: '11'
      },
      {
        name: '积分',
        url:'/pages/my/integral/integral',
        icon: '/imgs/my/jif.png',
        code: '10'
      },
      {
        name: '身份验证',
        url: '/pages/my/rlsb/rlsb',
        icon: '/imgs/my/shenfyz.png',
        code: '11'
      },
      {
        name: '评价',
        url: '/pages/Course/course',
        icon: '/imgs/my/pingj.png',
        code: '10'
      },

      {
        name: '会员权益',
        url: '/pages/Course/course',
        icon: '/imgs/my/huiyqy.png',
        code: '10'
      }, {
        name: '客服',
        icon: '/imgs/my/kef.png',
        code: '11'
      },
      {
        name: '入住Q&A',
        icon: '/imgs/my/zhix.png',
        code: '11'
      },
      {
        name: '投诉建议',
        url: '/pages/my/toushu/toushu',
        icon: '/imgs/my/tous.png',
        code: '10'
      }
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

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

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})