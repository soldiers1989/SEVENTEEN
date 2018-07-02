const app = getApp()

// pages/message/reply/reply.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imgUrl: app.globalData.ImgUrl,
    liveSelected: 0,
    targetSelected: -1,
    live: [{
      id: '1',
      img: app.globalData.ImgUrl + '/imgs/icon/goode.png',
      selectedImg: app.globalData.ImgUrl + '/imgs/icon/selectgoode.png',
      title: '好评',
      name: '非常满意'
    }, {
      id: '2',
      img: app.globalData.ImgUrl + '/imgs/icon/normaler.png',
      selectedImg: app.globalData.ImgUrl + '/imgs/icon/selectnormal.png',
      title: '中评',
      name: '感觉一般'
    }, {
      id: '3',
      img: app.globalData.ImgUrl + '/imgs/icon/bader.png',
      selectedImg: app.globalData.ImgUrl + '/imgs/icon/selectbader.png',
      title: '差评',
      name: '非常不满意'
    },
    ],
    point: [
      {
        id: '1',
        title: '设施评分',
        star: ['1', '1', '1', '1', '1']
      },
      {
        id: '2',
        title: '环境评分',
        star: ['1', '1', '1', '1', '1']
      }, {
        id: '3',
        title: '舒适评分',
        star: ['1', '1', '1', '1', '1']
      }
    ],
    targets: [{
      id: '1',
      name: '商务出差'
    },
    {
      id: '2',
      name: '情侣出游'
    },
    {
      id: '3',
      name: '家庭出游'
    },
    {
      id: '4',
      name: '朋友出游'
    },
    {
      id: '5',
      name: '独自出游'
    },
    {
      id: '6',
      name: '其他'
    }
    ]
  },
  liveTap: function (e) {
    var liveSelected = e.currentTarget.dataset.live
    this.setData({
      liveSelected
    })
  },
  targetTap: function (e) {
    var targetSelected = e.currentTarget.dataset.target
    this.setData({
      targetSelected
    })
  },
  starTap: function (e) {
    let point = e.currentTarget.dataset.point
    let star = e.currentTarget.dataset.star
    let data =this.data.point[point];
    for (let k = 0; k < data.star.length; k++){
      if (star<k){
        data.star[k] = '0';
      }else{
        data.star[k] = '1';
      }
    }
    this.data.point[point] = data;
    
    this.setData({
      point :this.data.point
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