const app = getApp()
Page({
  data: {
    livers: [{
      liver: ''
    }
    ],
    systemUrl: app.globalData.baseUrl + '/order',

    imgUrl: app.globalData.ImgUrl
  },
  onShareAppMessage: function (res) {
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
    }
    return {
      title: '添加入住人',
      path: '../add-wechatLiver/add-wechatLiver',
      imageUrl:"https://www.17inn.com/img/wxApp/imgs/index/brand.jpg"
    }
  },
  inputTap:function(e){
    let id = e.currentTarget.dataset.id;
    let val= e.detail.value;
    for (var index in this.data.livers) {
      if (index == id){
        let liver = {
          liver: val
        }
        this.data.livers[index] = liver;
      }
    }
    this.setData({
      livers: this.data.livers
    });
  },
  contolTap: function (event) {
    let id = event.currentTarget.dataset.id;
    let liver = {
      liver: ''
    }
    if (id === 0) {
      this.data.livers.push(liver);
      this.setData({
        livers: this.data.livers
      });
    } else {
      this.data.livers.splice(id, 1);
      this.setData({
        livers: this.data.livers
      });
    }
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
          
          that.setData({
            livers: data.data.data.livers,
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

})