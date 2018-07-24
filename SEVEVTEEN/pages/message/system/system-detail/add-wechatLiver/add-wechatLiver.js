const app = getApp()
Page({
  data: {
    livers: [{
      name: ''
    }
    ],
    imgUrl: app.globalData.ImgUrl
  },
  getCodeTap: function () {
    var all = "azxcvbnmsdfghjklqwertyuiopZXCVBNMASDFGHJKLQWERTYUIOP0123456789";
    var b = "";
    for (var i = 0; i < 4; i++) {
      var index = Math.floor(Math.random() * 62);
      b += all.charAt(index);
    }
    this.setData({
      jsCheck: b
    });
  },
  getPhoneCheckTap: function () {
    let flag = true;
    if (!this.data.phone) {
      flag = false
    };

    if (!flag) {
      wx.showToast({
        title: '成功',
        icon: 'success',
        duration: 2000
      })
    }

  },
  onLoad: function (options) {
    this.getCodeTap();
  }
})