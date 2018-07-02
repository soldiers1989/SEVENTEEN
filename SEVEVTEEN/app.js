//app.js
const wxApiInterceptors = require('/utils/wxApiInterceptors');


wxApiInterceptors({
  request: {
    response(res) {
      const code= res.statusCode;
      // 如果data里的code等于401就响应为失败,失效token
      if (code === 401) {
         wx.showToast({
            title: '身份失效',
            icon: 'fail',
            duration: 1500,
            mask:true
        })
        wx.redirectTo({
          url: '/pages/auth/auth'
        })
      }
      return res;
    },
  },
});

App({
  globalData: {
    baseUrl:"http://172.16.14.30:80",
    ImgUrl:"https://www.17inn.com/img/wxApp"
  }
  
})