//app.js
const wxApiInterceptors = require('/utils/wxApiInterceptors');

wxApiInterceptors({
  request: {
    response(res) {
      const code= res.statusCode;
      // 如果data里的code等于-1就响应为失败
      if (code === 401) {
        return Promise.reject(res);
      }
      return res;
    },
  },
});

App({
  globalData: {
    baseUrl:"http://localhost:80"
  }
})