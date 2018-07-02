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
    imgUrl: app.globalData.ImgUrl,
    baseUrl: app.globalData.baseUrl
  },

  // 点击弹窗显示
  showPopup: function() {
    
    var _Arr = [];
    wx.request({
      url: this.data.baseUrl + '/room/tags?type=intro',
      method:"get",
      header: {
        "Authorization": "Bearer " + this.data.token
      },
      success: function(res) {
        console.log(res.data.data)
        // for (var index in res.data.data){
         for (var i = 0; i < res.data.data;i++){
           console.log(res.data.data[i])
          _obj={};
          _obj.icon = res.data[i].value;
          _obj.name = res.data[i].name;
          _Arr[index]=_obj;
        }
      }
    })

    this.setData({
      popupShow: true,
      popupIconList: _Arr
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

    var token = wx.getStorageSync('token');

    this.setData({
      token: token
    });

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