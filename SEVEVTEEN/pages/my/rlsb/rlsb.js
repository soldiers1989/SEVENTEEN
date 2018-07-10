const app = getApp()

Page({

  data: {
    idcrad0: "",
    idcrad1: "",
    userPhoto: "",
    imgUrl: app.globalData.ImgUrl,

    baseUrl: app.globalData.baseUrl
  },

  idcardUpload: function(e) {
    var that = this;
    var _datasetId = e.currentTarget.dataset.id; //表示1-反面,0-正面
    wx.chooseImage({
      count: 1, //最多可以选择的图片总数  
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有  
      success: function(res) {
        var tempFilePaths = res.tempFilePaths


        var redata = upload(tempFilePaths[0], _datasetId, that.data.baseUrl + '/file/wxApp');

        redata.then(res => {

          if (_datasetId == 0) {
            that.setData({
              idcrad0: "http://localhost/" + JSON.parse(res.data).data
            })

          } else if (_datasetId == 2) {
            that.setData({
              userPhoto: "http://localhost/" + JSON.parse(res.data).data
            })
          } else if (_datasetId == 1) {
            that.setData({
              idcrad1: "http://localhost/" + JSON.parse(res.data).data
            })
          }

        })
      },
    })
  },

  subimtREC: function() {
    var that = this;
    var idc0 = that.data.idcrad0;
    var idc1 = that.data.idcrad1;
    var userPhoto = that.data.userPhoto;
    if (idc0 == "" || idc1 == "" || userPhoto == "") {
      wx.showToast({
        title: '您还没有上传完整照片!',
        icon: 'none',
        duration: 2000,
      })
      return;
    }

    wx.showToast({
      title: "正在审核中",
      icon: 'loading',
      duration: 5000,
    })
    var token = wx.getStorageSync('token');

    wx.request({
      url: that.data.baseUrl + '/rlsb/verify',
      method: "GET",
      header: {
        "Authorization": "Bearer " + token
      },
      success: function(res) {
        var dta = res.data;
        if (dta.resultCode != 200) {
          wx.showToast({
            title: dta.data,
            icon: 'none',
            duration: 20000,
          })
        } else {
          wx.showToast({
            title: dta.data,
            icon: 'success',
            duration: 2000,
          })
          setTimeout(function() {

            var pages = getCurrentPages(); // 当前页面  
            var beforePage = pages[pages.length - 2]; //上一个页面
            // var pages = getCurrentPages();
            // var currPage = pages[pages.length - 1];   //当前页面
            // var prevPage = pages[pages.length - 2];  
            wx.navigateBack({ //返回
              success: function() {
                beforePage.onLoad(); // 执行前一个页面的onLoad方法  
              }
            })

          }, 2000);

        }
      }
    })



  }
})

function upload(imgs, _type, _url) {
  var token = wx.getStorageSync('token');
  var data = "";
  return wx.uploadFile({
    url: _url, //仅为示例，非真实的接口地址
    filePath: imgs,
    name: 'file',
    formData: {
      'type': _type
    },
    header: {
      "Authorization": "Bearer " + token
    },
    success: function(res) {

      return res.data;
    },
    fail: function(res) {
      console.log(res)
    }

  })
}