Page({
  /**
   * 页面的初始数据
   */
  data: {
    coupon: 100,
    point: 1,
    card: 1,
    image: "http://119.29.196.106:8080/seventeen/qianbao_beijing.jpg",
    name: "飞科剃须刀超级无敌"
  },
  recharge:function(event){
    console.log("1231232")
    wx.navigateTo({
      url: 'recharge/recharge',
    })
  }



})