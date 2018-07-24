const app = getApp()

// pages/message/reply/reply.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imgUrl: app.globalData.ImgUrl,
    liveSelected: 39,
    targetSelected: -1,
    replyUrl: app.globalData.baseUrl + '/order',
    assessUrl: app.globalData.baseUrl + '/assess',
    content:'',
    live: [{
      id: '39',
      img: app.globalData.ImgUrl + '/imgs/icon/goode.png',
      selectedImg: app.globalData.ImgUrl + '/imgs/icon/selectgoode.png',
      title: '好评',
      name: '非常满意'
    }, {
      id: '40',
      img: app.globalData.ImgUrl + '/imgs/icon/normaler.png',
      selectedImg: app.globalData.ImgUrl + '/imgs/icon/selectnormal.png',
      title: '中评',
      name: '感觉一般'
    }, {
      id: '41',
      img: app.globalData.ImgUrl + '/imgs/icon/bader.png',
      selectedImg: app.globalData.ImgUrl + '/imgs/icon/selectbader.png',
      title: '差评',
      name: '非常不满意'
    }, ],
    point: [{
        id: '30',
        title: '设施评分',
        star: ['1', '1', '1', '1', '1']
      },
      {
        id: '31',
        title: '环境评分',
        star: ['1', '1', '1', '1', '1']
      }, {
        id: '33',
        title: '舒适评分',
        star: ['1', '1', '1', '1', '1']
      }
    ],
    targets: [{
        id: '33',
        name: '商务出差'
      },
      {
        id: '34',
        name: '情侣出游'
      },
      {
        id: '35',
        name: '家庭出游'
      },
      {
        id: '36',
        name: '朋友出游'
      },
      {
        id: '37',
        name: '独自出游'
      },
      {
        id: '38',
        name: '其他'
      }
    ]
  },
  bindTextAreaBlur: function (e) {
    this.setData({
      content: e.detail.value
    })
  },   
  liveTap: function(e) {
    var liveSelected = e.currentTarget.dataset.live
    this.setData({
      liveSelected
    })
  },
  targetTap: function(e) {
    var targetSelected = e.currentTarget.dataset.target
    this.setData({
      targetSelected
    })
  },
  starTap: function(e) {
    let point = e.currentTarget.dataset.point
    let star = e.currentTarget.dataset.star
    let data = this.data.point[point];
    for (let k = 0; k < data.star.length; k++) {
      if (star < k) {
        data.star[k] = '0';
      } else {
        data.star[k] = '1';
      }
    }
    this.data.point[point] = data;

    this.setData({
      point: this.data.point
    })
  },
  addEvaluateTap: function() {
    let liveSelected=this.data.liveSelected;
    let targetSelected= this.data.targetSelected;
    let content = this.data.content;
    let point= this.data.point;
    let orderId = this.data.orderId;

    if (content === '' || content===null){
      wx.showToast({
        title: '请填写评价内容',
        icon: 'none',
        duration: 2000
      })
      return;
    }

    let that = this;
    wx.request({
      url: this.data.assessUrl + "/wx",
      method: 'post',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      data: {
        orderId,
        liveSelected,
        targetSelected,
        content,
        point},
      success: function(data) {
        if (data.data.resultCode === 200) {
          wx.showToast({
            title: '评价成功',
            icon: 'success',
            duration: 2000
          })
          wx.redirectTo({
            url: '../reply?success=1',
          })
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
  getReplyData: function(id) {
    let that = this;
    wx.request({
      url: this.data.replyUrl + "/wx/" + id + "/detail",
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      success: function(data) {
        if (data.data.resultCode === 200) {
          that.setData({
            reply: data.data.data,
            orderId: id
          })
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
    let orderid = options.orderid;
    this.getReplyData(orderid);
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