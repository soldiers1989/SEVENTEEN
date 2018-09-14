//app.js
const wxApiInterceptors = require('/utils/wxApiInterceptors');

wxApiInterceptors({
  request: {
    response(res) {
      const code= res.statusCode;
      // 如果data里的code等于401就响应为失败,失效token
      if (code === 401) {
        wx.redirectTo({
          url: '/pages/auth/auth?flag=false'
        })
      }
      return res;
    },
  },
});

App({
  globalData: {
     baseUrl:"https://www.17inn.com/seventeen",
    // baseUrl:"http://localhost",
    ImgUrl:"https://www.17inn.com/img/wxApp"
  }
})