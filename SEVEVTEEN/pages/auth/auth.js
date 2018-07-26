var app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    imgUrl: app.globalData.ImgUrl
   
  },
  bindGetUserInfo: function (e) {

    console.log(e.detail.userInfo);
    if (e.detail.userInfo===undefined){
      wx.showModal({
        title: '警告',
        content: '尚未进行授权，请点击确定跳转到授权页面进行授权。',
        success: function (res) {
          return;
        }
      });
    }
    wx.login({
      
      success: function (r) {
        var code = r.code;//登录凭证
        if (code) {
          //2、调用获取用户信息接口
          wx.getSetting({
            success: function (res) {
              if (res.authSetting['scope.userInfo']) {
                // 已经授权，可以直接调用 getUserInfo 获取头像昵称
                wx.getUserInfo({
                  success: function (res) {
                    console.log({ encryptedData: res.encryptedData, iv: res.iv, code: code })
                    //3.请求自己的服务器，解密用户信息 获取unionId等加密信息
                    wx.request({
                      url: app.globalData.baseUrl+'/app/sys/decodeUserInfo',//自己的服务接口地址
                      method: 'post',
                      header: {
                        'content-type': 'application/x-www-form-urlencoded'
                      },
                      data: { encryptedData: res.encryptedData, iv: res.iv, code: code },
                      success: function (data) {

                        //4.解密成功后 获取自己服务器返回的结果
                        if (data.data.resultCode === 200) {
                          wx.setStorageSync('token', data.data.data);
                          wx.switchTab({
                            url: '/pages/index/index',
                          })
                        } else {
                          console.log('解密失败')
                        }
                      },
                      fail: function () {
                        console.log('系统错误')
                      }
                    })
                  },
                  fail: function () {
                    console.log('获取用户信息失败')
                  }
                })
              }
            }
          })
        } else {
          console.log('获取用户登录态失败！' + r.errMsg)
        }
      },
      fail: function () {
        console.log('登陆失败')
      }
    });
  }
})