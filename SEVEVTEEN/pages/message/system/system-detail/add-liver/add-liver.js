const app = getApp()
Page({
  data: {
    livers: [{
      name: ''
    }
    ],

    imgUrl: app.globalData.ImgUrl
  },
  onShareAppMessage: function (res) {
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
    }
    return {
      title: '添加入住人',
      path: '../add-wechatLiver/add-wechatLiver'
    }
  },
  inputTap:function(e){
    let id = e.currentTarget.dataset.id;
    let val= e.detail.value;
    for (var index in this.data.livers) {
      if (index == id){
        let liver = {
          name: val
        }
        this.data.livers[index] = liver;
      }
    }
    this.setData({
      livers: this.data.livers
    });
  },
  contolTap: function (event) {
    let id = event.currentTarget.dataset.id;
    let liver = {
      name: ''
    }
    if (id === 0) {
      this.data.livers.push(liver);
      this.setData({
        livers: this.data.livers
      });
    } else {
      this.data.livers.splice(id, 1);
      this.setData({
        livers: this.data.livers
      });
    }
  }

})