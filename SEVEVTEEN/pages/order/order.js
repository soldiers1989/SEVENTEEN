const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    roomId: "",
    price: 0,
    index: 0,
    index1: 0,
    index_sz: 0,
    imgUrl: app.globalData.ImgUrl,
    baseUrl: app.globalData.baseUrl,

    shizu: "钟点房3小时", //

    userInfo: {
      isVerify: false,
      tName: ""

    },
    phone: "",
    tagId: "",
    roomName: "",

    // orderInfo:{
    //   startTime:"",
    //   endTime: "",
    //   tName:"",
    //   phone:"",
    //   planTime:"",
    //   couponId:"",
    //   price:0
    // },

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
    array2: [
      '9:00 ~ 11:00',
      '10:00 ~ 12:00'
    ],
    objectArray2: [{
        id: 0,
        name: '9:00 ~ 11:00'
      },
      {
        id: 1,
        name: '9:00 ~ 11:00'
      }
    ],

    objectArray: [{
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
      },
      {
        id: 6,
        name: '20:00 ~ 21:00'
      },
      {
        id: 7,
        name: '21:00 ~ 22:00'
      },
      {
        id: 8,
        name: '22:00 ~ 23:00'
      },
      {
        id: 9,
        name: '23:00 ~ 24:00'
      },
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

    year: '',
    month: '',
    day: '',
    days: '',
    outday: '',
    outmonth: '',
    outyear: '',
    couponIndex: 0

  },
  choseLiveDateTap: function(e) {
    //日历模态框
    var chooseDate = this.data.chooseDate;
    var time = "";

    if (chooseDate != null) {
      time = this.data.chooseDate.time;
    }
    var url
    if (chooseDate && chooseDate.end) {
      url = '/pages/dates/date?chooseDate=' + this.data.chooseDate.start.date + "-" + this.data.chooseDate.end.date + "&&eDate=" + this.data.chooseDate.eDate + "&&sDate=" + this.data.chooseDate.sDate + "&&time=" + time + "&&roomType=" + this.data.roomId
    } else {
      url = "/pages/dates/date?roomType=" + this.data.roomId;
    }
    wx.getLocation({
      type: 'wgs84',
      success: function(res) {
        wx.navigateTo({
          url: url
        })
      },
      fail: function() {
        wx.getSetting({
          success: (res) => {
            if (res.authSetting['scope.userLocation'] != undefined) { //非初始化进入该页面,且未授权
              wx.showModal({
                title: '警告',
                content: '需要获取您的地理位置，请确认授权，否则天气功能将无法使用',
                success: function(res) {
                  if (res.cancel) {
                    console.info("授权失败返回数据");
                  } else if (res.confirm) {
                    //village_LBS(that);
                    wx.openSetting({
                      success: function(data) {
                        console.log(data);
                        if (data.authSetting["scope.userLocation"] == true) {
                          wx.showToast({
                            title: '授权成功',
                            icon: 'success',
                            duration: 5000
                          })
                          //再次授权，调用getLocationt的API
                          // village_LBS(that);
                        } else {
                          wx.showToast({
                            title: '授权失败',
                            icon: 'success',
                            duration: 5000
                          })
                        }
                      }
                    })
                  }
                }
              })
            }
          }
        })
      }
    })

  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    var date = new Date();

    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    date.setDate(day + 1);
    var outday = date.getDate();
    // console.log(outday)
    var outmonth = date.getMonth() + 1;
    var outyear = date.getFullYear();

    this.setData({
      roomId: options.roomId,
      price: options.price,
      tagId: options.tagId,
      roomName: options.roomName,
      year: year,
      month: month,
      day: day,
      outday: outday,
      outmonth: outmonth,
      outyear: outyear
    })

    //获取用户是否已经做了实名制
    var token = wx.getStorageSync('token');
    getIsVerify(this, token);
    //获取是否还有房间



    //获取优惠券  

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
    // console.log("shohohsodhfosdhfo")
    var rl = this.data.roomList;
    if (rl != null) {
      if (rl.length <= 3) {
        var array1 = new Array();
        for (var i = 0; i < rl.lenght; i++) {
          array1[i] = i + 1;
        }
        this.setData({
          array1: array1
        })
      }
    }

    if (this.data.roomName == this.data.shizu) {
      console.log("时租")
      this.setData({
        array1: ["1"]

      })

    }


    var that = this;
    var cda = this.data.chooseDate;

    if (cda != null) {
      console.log(typeof cda.start.month)
      var sdate = "" + cda.start.year + "-" + (cda.start.month.toString().length == 1 ? "0" + cda.start.month : cda.start.month) + "-" + cda.start.day;
      var edate = "" + cda.end.year + "-" + ("" + cda.end.month.toString().length == 1 ? "0" + cda.end.month : cda.end.month) + "-" + cda.end.day
      // var sdate = "" + cda.start.year + "-" + (cda.start.month.toString().length == 1 ? "0" + cda.start.month : cda.start.month) + "-" + (cda.start.day.toString().length == 1 ? "0" + cda.start.day : cda.start.day);
      // var edate = "" + cda.end.year + "-" + ("" + cda.end.month.toString().length == 1 ? "0" + cda.end.month : cda.end.month) + "-" + ("" + cda.end.day.toString().length == 1 ? "0" + cda.end.day : cda.end.day)
      wx.request({
        url: this.data.baseUrl + '/coupon/wx/getCouponByOrderCanUse',
        data: {
          roomType: this.data.tagId,
          startTime: sdate,
          endTime: edate
        },
        header: {
          "Authorization": "Bearer " + wx.getStorageSync('token')
          // "Content-Type": "application/json"
        },
        method: 'GET',
        // responseType: 'json',
        success: function(res) {
          console.log(res)
          that.setData({
            coupon: res.data.data
          })
        }
      })
    }
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
  bindPickerChange: function(e) {
    // console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      index: e.detail.value
    })
    //赋值
    console.log(this.data.objectArray[this.data.index].name);
  },

  bindPickerChange_sz: function(e) {
    // console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      index_sz: e.detail.value
    })
    //赋值
    console.log(this.data.objectArray2[this.data.index_sz].name);
  },

  bindPickerChange1: function(e) {
    // console.log('picker发送选择改变，携带值为', e.detail.value)
    // var prices = this.data.price * this.data.array1[this.data.index1];
    this.setData({
      index1: e.detail.value,

    })
    //赋值
    console.log(this.data.array1[this.data.index1]);
  },
  bindPickerChangeCoupon: function(e) {
    // console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      couponIndex: e.detail.value
    })
    //赋值
    console.log(this.data.array1[this.data.index1]);
  },

  getPhone: function(e) {
    var val = e.detail.value;
    this.setData({
      phone: val
    });
  },

  //支付
  toPay: function() {
    console.log("支付开始")

    if (this.data.userInfo.isVerify == false) {
      wx.showToast({
        title: '您还没通过实名认证',
        icon: 'none'
      })
      wx.redirectTo({ //关闭当前页面，跳转到应用内的某个页面
        url: '/pages/my/rlsb/rlsb'
      })
      return;
    }

    var roomId = "";
    var rlist = this.data.roomList;

    var roomNum = this.data.array1[this.data.index1];
    if (this.data.roomName != this.data.shizu) {
      if (rlist == null) {
        wx.showToast({
          title: '请先选择日期',
          icon: 'none',
          duration: 2000
        })
        return;
      }

      if (rlist.length > 0) {
        if (roomNum == 1) {
          roomId = rlist[0];
        } else {
          for (var i = 1; i <= roomNum; i++) {
            roomId += rlist[i - 1];
            if (i < roomNum) {
              roomId += ",";
            }
          }
        }
      } else {
        wx.showToast({
          title: '房间已被订满了',
          icon: 'none',
          duration: 2000
        })
        return;
      }

      if (roomId == "") {
        wx.showToast({
          title: '房间已满,请稍后重试',
          icon: 'none',
          duration: 2000
        })
        return;
      }
    }

    var couponid = "0";
    if (this.data.coupon != null) {
      couponid = this.data.coupon[this.data.couponIndex].id;
    }

    var roomType="";
    var planTime = this.data.array[this.data.index];
    if (this.data.roomName == this.data.shizu){
      var price = this.data.price;
      roomType = this.data.roomId;
      planTime = this.data.array2[this.data.index_sz];
    }
    else{
      var price = this.data.price * this.data.array[index];

    }

    var userInfo = this.data.userInfo;
    var phone = this.data.phone;
    if (this.data.chooseDate!=null){
      var startTime = this.data.chooseDate.start.re + " 14:00:00"
      var endTime = this.data.chooseDate.end.re + " 12:00:00"
    }
    if (phone == "") {
      wx.showToast({
        title: '手机号不能为空',
        icon: 'none'
      })
      return;
    }




    var token = wx.getStorageSync('token');

    wx.request({
      url: this.data.baseUrl + '/order/wx/setOrder',
      data: {
        roomId: roomId,
        fee: price,
        startTime: startTime,
        endTime: endTime,
        tName: userInfo.tName,
        phone: phone,
        planTime: planTime,
        couponId: couponid,
        price: price,
        roomNum: roomNum,
        tagId: this.data.tagId,
        roomType: roomType
        // apId: apId
      },
      header: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      },
      method: 'POST',
      //dataType: 'text',
      responseType: 'text',
      success: function(res) {
        console.log(res)
        if (res.statusCode == 200) {
          var dd = res.data.data;
          console.log()
          wx.requestPayment({
            timeStamp: dd.timeStamp,
            nonceStr: dd.nonceStr,
            package: dd.package,
            signType: 'MD5',
            paySign: dd.paySign,
            'success': function(res) {
              console.log(res);
            },
            'fail': function(res) {}
          })
        }

      },
      fail: function(res) {},
      complete: function(res) {},
    })



  }
})

function getIsVerify(that, token) {
  wx.request({
    url: that.data.baseUrl + "/rlsb/isVerify",
    method: 'GET',
    header: {
      Authorization: 'Bearer ' + token
    },
    success: function(res) {
      console.log(res.data.data)
      that.setData({
        userInfo: res.data.data
      })
    }
  })
}


function timeStamp() {
  return parseInt(new Date().getTime() / 1000) + ''
}

function MixedencryMD5(res_paydata, randomString, timeStamp) {
  return "appId=" + "wxe6f9450a01336cb9" + " & nonceStr=" + "9ZB90Dx3KJgZlEQj" + "&package=prepay_id=" + "wx18160543493401328ac1c89c1426699089" + "&signType=MD5" + "&timeStamp=" + timeStamp() + "&key=" + "1TZt37EvkxJW8ctbVT9IPZQnYpISsgLy";
}