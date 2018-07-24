Page({
  /**
   * 页面的初始数据
   */
  data: {
    coupon: 100,
    point: 1,
    card: 1,
    image: "https://www.17inn.com/img/wxApp/qianbao_beijing.jpg",
    name: "飞科剃须刀超级无敌"
  },
  recharge: function (event) {
    wx.navigateTo({
      url: 'recharge/recharge',
    })
  },
  pointDetail: function (event) {
    wx.navigateTo({
      url: 'point-detail/point-detail',
    })
  },
  couponDetail: function (event) {
    wx.navigateTo({
      url: 'coupon-detail/coupon-detail',
    })
  },
  vipCard: function (event) {
    wx.navigateTo({
      url: 'vip-card/vip-card',
    })
  },
  balanceCharge: function (event) {
    wx.navigateTo({
      url: 'balance-charge/balance-charge',
    })
  }
})