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
        console.log();
        var tempFilePaths = res.tempFilePaths
        if (_datasetId == 0) {
          that.setData({
            idcrad0: tempFilePaths
          })
        } else if (_datasetId == 2) {
          that.setData({
            userPhoto: tempFilePaths
          })
        } else if (_datasetId == 1) {
          that.setData({
            idcrad1: tempFilePaths
          })
        }
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
    var token = wx.getStorageSync('token');

    // var imgArr = [idc0, idc1, userPhoto];

    
    wx.uploadFile({
      url: this.data.baseUrl +'/file', //仅为示例，非真实的接口地址
      filePath: idc0[0],
      name: 'file', 
      header: {
        "Authorization": "Bearer " + token
      },
      success: function (res) {
        var data = res.data
        //do something
      },
      fail: function (res) {
        console.log(res)
      }

    })
  }
})