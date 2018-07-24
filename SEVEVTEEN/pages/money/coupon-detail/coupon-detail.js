var app = getApp();

Page({
  data: {
    imgUrl: app.globalData.ImgUrl,
    tabArray: ["可使用", "已过期", "已使用"],
    focus: false,
    inputValue: '',
    currentTab: 0
  },
  swichNav: function (e) {
    var currentTab = e.currentTarget.dataset.current
    this.setData({
      currentTab
    })
  },

  bindButtonTap: function () {

  }
})