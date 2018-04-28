Page({
  data: {
    tabArray: ["可使用", "已过期", "已使用"],
    focus: false,
    inputValue: '',
    currentTab: 1
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