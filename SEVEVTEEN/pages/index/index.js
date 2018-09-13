//hello
//hello
//index.js
//获取应用实例
const app = getApp()
const commont = require("commont-template/commont-template.js");
Page({
  data: {
    token: "21",
    pageNum: 1,
    pageSize: 10,
    pageTotal: 0,
    hasMoreData: true,
    assessUrl: app.globalData.baseUrl + '/assess',
    roomUrl: app.globalData.baseUrl + '/room',

    tabArr: {
      curHdIndex: "t1"
      //  curBdIndex: 0 
    },
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    all_evaluete: {
      score: "5.0",
      ss: [1, 1, 1, 0, 0],
      ws: [1, 1, 1, 0, 0],
      ssg: [1, 0, 0, 0, 0],
      comCount: 160
    },

    isShowPay: {
      flag: false,
      showId: ""

    },

    popupShow: false, //弹窗是否显示

    popupIconList: [
      // {
      //     icon: '/imgs/timg.jpg',
      //     name: '无线WIFI覆盖'
      //   }

    ],

    Shops: [{
      address: "番禺区大学城北亭",
      createBy: "seventeen",
      createTime: "2018-07-12 17:36:07",
      id: "20180712173607612892549",
      latitude: "1",
      leader: "狗蛋",
      longitude: "1",
      name: "17Inn北亭店",
      phone: "178XXX",
      remark: null,
      status: "1",
      wifi: "123!@#QWE"
    }],

    roomTypes: [{
      area: "999",
      createTime: null,
      name: "大床房",
      price: "9999",
      remark: null,
      roomType: "20180719101454135398418",
      imgUrl: ""

    }],

    roomTypePrice: [{

      name: "",
      Price: '',
      tagId: "",
      name: ""

    }],

    userVip: {},

    imgUrl: app.globalData.ImgUrl,
    baseUrl: app.globalData.baseUrl
  },

  // 点击弹窗显示
  showPopup: function() {
    var that = this;
    var _Arr = this.data.popupIconList;
    wx.request({
      url: this.data.baseUrl + '/room/tags?type=intro',
      method: "get",
      header: {
        "Authorization": "Bearer " + this.data.token
      },
      success: function(res) {
        for (var index in res.data.data) {
          var _obj = {};
          _obj.icon = that.data.imgUrl + res.data.data[index].value;
          _obj.name = res.data.data[index].name;
          _Arr[index] = _obj;
        }

        that.setData({
          popupIconList: _Arr
        })
      }
    })

    this.setData({
      popupShow: true
    })


  },
  // 关闭弹窗
  closePopup: function() {
    this.setData({
      popupShow: false
    })
  },

  //
  setOrder: function(e) {
    var _datasetId = e.target.dataset.id;
    var money = e.target.dataset.money;
    var tagId = e.target.dataset.tagid;
    var roomName = e.target.dataset.roomname;
    wx.navigateTo({
      url: '/pages/order/order?roomId=' + _datasetId + "&price=" + money + "&tagId=" + tagId + "&roomName=" + roomName,
    })
  },
  getTotalAssess: function(pageNum) {
    let that = this;
    wx.request({
      url: this.data.assessUrl + "/wx/allAssess?pageNum=" + pageNum,
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      success: function(data) {
        if (data.data.resultCode === 200) {
          let hasMoreData = true;
          if (data.data.data.assessContent.length < that.data.pageSize) {
            hasMoreData = false;
          }
          if (pageNum > 1) {
            that.setData({
              assess: that.data.assess.assessContent.concat(data.data.data.assessContent),
              pageTotal: data.data.pageInfo.total,
              pageNum: data.data.pageInfo.pageNum,
              hasMoreData
            })
          } else {
            that.setData({
              assess: data.data.data,
              pageTotal: data.data.pageInfo.total,
              pageNum: data.data.pageInfo.pageNum,
              hasMoreData
            })
          }
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
  showImgs: function(e) {
    var roomType = e.currentTarget.dataset.roomType;

    let that = this;
    wx.request({
      url: this.data.roomUrl + "/wx/img?roomType=" + roomType,
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      success: function(data) {
        if (data.data.resultCode === 200) {
          wx.previewImage({
            current: data.data.data[0], // 当前显示图片的http链接
            urls: data.data.data // 需要预览的图片http链接列表
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
  //切换tab页
  tabSel: function(e) {
    let that = this;
    var _datasetId = e.target.dataset.id;
    console.log(e.currentTarget.dataset.id)
    var sid = e.currentTarget.dataset.id;
    var _obj = {};
    _obj.curHdIndex = _datasetId;
    // _obj.curBdIndex = _datasetId;
    this.setData({
      tabArr: _obj
    });
    if (_datasetId == "t2") {
      this.getTotalAssess(1);
    }
  },
  //日历模态框
  showDates: function() {
    var chooseDate = this.data.chooseDate;
    var time = "";
    var url

    if (chooseDate && chooseDate.end) {
      url = '../dates/date?chooseDate=' + this.data.chooseDate.start + "-" + this.data.chooseDate.end + "&&eDate=" + this.data.chooseDate.eDate + "&&sDate=" + this.data.chooseDate.sDate
    } else {
      url = "../dates/date";
    }
    wx.navigateTo({
      url: url
    })
  },
  showMap: function() {
    wx.getLocation({
      type: 'gcj02', // 默认为 wgs84 返回 gps 坐标，gcj02 返回可用于 wx.openLocation 的坐标  
      success: function(res) {
        // success  
        wx.openLocation({
          latitude: res.latitude, // 纬度，范围为-90~90，负数表示南纬  
          longitude: res.longitude, // 经度，范围为-180~180，负数表示西经  
          scale: 28, // 缩放比例   
          name: "巨大创意园",
          address: "会江地铁站C出口"
        })
      }
    });
    // wx.navigateTo({
    //   url: '../maps/map'
    // })
  },
  onLoad: function(options) {
    var that = this;
    var token = wx.getStorageSync('token');
   
    this.setData({
      token: token
    });


    //加载店铺数据-默认取第一条数据
    wx.request({
      url: this.data.baseUrl + "/app/index/getShops",
      method: 'GET',
      header: {
        Authorization: 'Bearer ' + token
      },
      success: function(res) {
        console.log(res.data.data)
        that.setData({
          Shops: res.data.data
        })
        getRoomTypes(that, that.data.Shops[0].id, token);
      }
    });

    wx.request({
      url: that.data.baseUrl + "/sys/users/userVip",
      method: 'GET',
      header: {
        Authorization: 'Bearer ' + token
      },
      success: function(res) {
        //console.log(res.data)
        wx.setStorageSync('userVip', res.data.data)
        that.setData({
          userVip: res.data.data
        })
      }
    });

  },
  onReachBottom: function() {
    if (this.data.tabArr.curHdIndex == "t2") {
      if (this.data.hasMoreData) {
        let pageNum = this.data.pageNum + 1
        this.getTotalAssess(pageNum)
      } else {
        wx.showToast({
          title: '没有更多数据',
        })
      }
    }

  },
  onShow: function() {
    var chooseDate = this.data.chooseDate;
    var time = "";
    if (chooseDate && chooseDate.end) {
      var date = chooseDate.time.split("-");
      var startDate = date[0].split("/");
      var endDate = date[1].split("/");
      time = startDate[0] + "月" + startDate[1] + "日" + "-" + endDate[0] + "月" + endDate[1] + "日";
    } else {
      time = "入住日期"
    }
    this.setData({
      time
    });
  },

  showPay: function(e) {

    var _datasetId = e.currentTarget.dataset.id;
    console.log(_datasetId)
    getRoomTypePrice(this, _datasetId, this.data.token);

    var curObj = this.data.isShowPay;
    if (curObj.showId == "") { //初始状态
      curObj.showId = _datasetId;
      curObj.flag = true;

    } else if (curObj.showId == _datasetId) {
      if (curObj.flag == true) { //还原所有状态
        curObj.flag = false;
        curObj.showId = "";
      }
    } else {
      curObj.showId = _datasetId;
      curObj.flag = true;
    }
    this.setData({
      isShowPay: curObj
    });
  }
})

function getCouponByRoomType(that, token) {
  var anys = that.data.roomTypePrice;
  var arys = new Array();
  for (var i = 0; i < anys.length; i++) {
    var _obj = new Object();
    // console.log(anys[i])
    _obj.name = anys[i].name;
    _obj.tagId = anys[i].tagId;
    _obj.price = anys[i].price;
    if (i / 2 == 0)
      _obj.isCoupon = 500;
    else
      _obj.isCoupon = 200;
    arys[i] = _obj
  }
  // wx.request({
  //   url: that.data.baseUrl + "/coupon/wx/" + anys[i].tagId,
  //   method: 'GET',
  //   header: {
  //     Authorization: 'Bearer ' + token
  //   },
  //   success: function (res) {
  //     _obj.isCoupon = res.data.resultCode;
  //     // console.log()

  //   },
  //   fail: function (res) {
  //     console.log(res)
  //   }
  // })

  // console.log("------------")
  // console.log(arys)
  // console.log("------------")
  that.setData({
    roomTypePrice: arys
  })

}

function getRoomTypePrice(that, typeCode, token) {
  wx.request({
    url: that.data.baseUrl + "/app/index/getRoomPirce",
    method: 'GET',
    data: {
      typeCode: typeCode
    },
    header: {
      Authorization: 'Bearer ' + token
    },
    success: function(res) {

      that.setData({
        roomTypePrice: res.data.data
      })
      getCouponByRoomType(that, token);
    }
  })
}


function getRoomTypes(that, shopId, token) {
  wx.request({
    url: that.data.baseUrl + "/app/index/getTypeRooms",
    method: 'GET',
    data: {
      shopId: shopId
    },
    header: {
      Authorization: 'Bearer ' + token
    },
    success: function(res) {
      // console.log(res.data.data)
      that.setData({
        roomTypes: res.data.data
      })
    }
  })
}