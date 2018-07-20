//hello
//hello
//index.js
//获取应用实例
const app = getApp()
const commont = require("commont-template/commont-template.js");
Page({
  data: {
    token: "21",

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

    popupIconList: [{
        icon: '/imgs/timg.jpg',
        name: '无线WIFI覆盖'
      },
      {
        icon: '/imgs/timg.jpg',
        name: '书桌'
      },
      {
        icon: '/imgs/timg.jpg',
        name: '电脑'
      }
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
      imgUrl:""
    }],
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

    wx.navigateTo({
      url: '/pages/order/order?roomId=' + _datasetId,
    })
  },

  //切换tab页
  tabSel: function(e) {

    var _datasetId = e.target.dataset.id;
    console.log(e.currentTarget.dataset.id)
    var sid = e.currentTarget.dataset.id;
    var _obj = {};
    _obj.curHdIndex = _datasetId;
    // _obj.curBdIndex = _datasetId;
    this.setData({
      tabArr: _obj
    });

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
    })
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
        roomTypes:res.data.data
      })
    }
  })
}