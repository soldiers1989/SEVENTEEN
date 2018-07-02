Page({

  /**
   * 页面的初始数据
   */
  data: {
    roomId:"",
    array: [
      '14:00 ~ 15:00',
      '15:00 ~ 16:00',
      '16:00 ~ 17:00',
      '17:00 ~ 18:00',
      '18:00 ~ 19:00',
      '19:00 ~ 20:00',
      '20:00 ~ 21:00',
      '21:00 ~ 22:00',
      '22:00 ~ 23:00',
      '23:00 ~ 24:00',
      '入住次日凌晨'
      ],
    objectArray: [
      {
        id: 0,
        name: '14:00 ~ 15:00'
      },
      {
        id: 1,
        name: '15:00 ~ 16:00'
      },
      {
        id: 2,
        name: '16:00 ~ 17:00'
      },
      {
        id: 3,
        name: '17:00 ~ 18:00'
      },
      {
        id: 4,
        name: '18:00 ~ 19:00'
      },
      {
        id: 4,
        name: '19:00 ~ 20:00'
      }
      ,
      {
        id: 6,
        name: '20:00 ~ 21:00'
      }
      ,
      {
        id: 7,
        name: '21:00 ~ 22:00'
      }
      ,
      {
        id: 8,
        name: '22:00 ~ 23:00'
      },
      {
        id: 9,
        name: '23:00 ~ 24:00'
      }
      ,
      {
        id: 10,
        name: '入住次日凌晨'
      }
      
    ],

    array1: [
      '1',
      '2',
      '3'
    ],
    

    index: 0, index1: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
    this.data.roomId=options.roomId;
    console.log(this.data.roomId);
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
    
  }, 
  bindPickerChange: function (e) {
    // console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      index: e.detail.value
    })
    //赋值
    console.log(this.data.objectArray[this.data.index].name);
  },

   bindPickerChange1: function (e) {
    // console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      index1: e.detail.value
    })
    //赋值
    console.log(this.data.array1[this.data.index1]);
  }
})