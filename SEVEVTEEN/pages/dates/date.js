var amapFile = require('../../libs/amap-wx.js');
var util = require('../../utils/util.js');
var app = getApp();


Page({
  data: {
    currentTab: 1,
    tabArray: ["重选", "确认"],
    weatherImg: "",
    selected: "0",
    startDate: null,
    endDate: null,
    date: {},
    imgUrl: app.globalData.ImgUrl,
    orderUrl: app.globalData.baseUrl + '/order',
    roomUrl: app.globalData.baseUrl + '/room'
  },

  swichNav: function(e) {
    let that = this;
    var time = 0;
    var start = this.data.startDate;
    var sDate = 0;
    var end = this.data.endDate;
    var eDate = 0;
    var day = 0;

    var currentTab = e.target.dataset.current

    var pages = getCurrentPages();
    var currPage = pages[pages.length - 1]; //当前页面
    var prevPage = pages[pages.length - 2]; //上一个页面

    if (currentTab === 0) {
      this.dateData();
      this.setData({
        startDate: null,
        endDate: null
      })
    } else {
      if (this.data.startDate === null) {
        wx.showToast({
          title: "请选择入住日期",
          duration: 2000
        })
        return;
      }
      if (this.data.startDate && this.data.endDate) {
        var date = this.data.date;
        var enddate = this.data.endDate.split("/");
        var startdate = this.data.startDate.split("/");
        end = date[enddate[0]][enddate[1]];
        start = date[startdate[0]][startdate[1]];
        time = this.data.startDate + "-" + this.data.endDate

        sDate = start.date;
        eDate = end.date;

        // var ssDate = new Date(2018, 8, 21)
        var start_date = new Date(start.re.replace(/-/g, "/"));
        var end_date = new Date(end.re.replace(/-/g, "/"));
        var days = end_date.getTime() - start_date.getTime();
        day = parseInt(days / (1000 * 60 * 60 * 24));
        // day = end.date - start.date;
        // if (day > 0) {
        //   this.setData({
        //     num: day
        //   })
        // }

      }
      var chooseDate = {
        time: time,
        start: start,
        sDate: sDate,
        end: end,
        eDate: eDate,
        day: day

      }

      wx.request({
        url: this.data.roomUrl + '/wx/getApartmentByTime?startTime=' + start.re + "&endTime=" + end.re + "&roomType=" + that.data.roomType,
        method: 'get',
        header: {
          'Authorization': 'Bearer ' + wx.getStorageSync('token'),
        },
        // data: { pageInfo: pageInfo  },
        success: function(data) {
          if (data.data.resultCode === 200) {
            //直接调用上一个页面的setData()方法，把数据存到上一个页面中去

            prevPage.setData({
              chooseDate: chooseDate,
              roomList: data.data.data
            })
            wx.navigateBack({
              url: '../order/order'
            })
          } else {
            wx.showToast({
              title: '系统异常',
              icon: 'none',
              duration: 2000
            })
          }
        },
        fail: function() {
          wx.showToast({
            title: '网络异常',
            icon: 'none',
            duration: 2000
          })
        }
      })
    }

    if (this.data.currentTab === currentTab) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      })
    }
  },

  dateData: function() {
    let dataAll = [] //总日历数据
    let dataAll2 = [] //总日历数据
    let dataMonth = [] //月日历数据
    let date = new Date //当前日期
    let year = date.getFullYear() //当前年
    let week = date.getDay(); //当天星期几
    let weeks = []
    let month = date.getMonth() + 1 //当前月份
    let day = date.getDate() //当天
    let daysCount = 183 //一共显示多少天
    let dayscNow = 0 //计数器
    let monthList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12] //月份列表
    let nowMonthList = [] //本年剩余年份
    let startday = 0;
    let startmonth = 0;
    let endmonth = 0;
    let endday = 0;
    let eDate = 0;
    let sDate = 0;

    if (this.data.eDate) {
      eDate = this.data.eDate;
      sDate = this.data.sDate;
    }

    for (let i = month; i < 13; i++) {
      nowMonthList.push(i)
    }
    let yearList = [year] //年份最大可能
    for (let i = 0; i < daysCount / 183 + 2; i++) {
      yearList.push(year + i + 1)
    }
    let leapYear = function(Year) { //判断是否闰年 
      if (((Year % 4) == 0) && ((Year % 100) != 0) || ((Year % 400) == 0)) {
        return (true);
      } else {
        return (false);
      }
    }
    for (let i = 0; i < yearList.length; i++) { //遍历年
      let mList
      if (yearList[i] == year) { //判断当前年份
        mList = nowMonthList
      } else {
        mList = monthList
      }
      for (let j = 0; j < mList.length; j++) { //循环月份
        dataMonth = []
        let t_days = [31, 28 + leapYear(yearList[i]), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
        let t_days_thisYear = []
        if (yearList[i] == year) {
          for (let m = 0; m < nowMonthList.length; m++) {
            t_days_thisYear.push(t_days[mList[m] - 1])
          }
          t_days = t_days_thisYear
        } else {
          t_days = [31, 28 + leapYear(yearList[i]), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
        }
        for (let k = 0; k < t_days[j]; k++) { //循环每天
          dayscNow++
          let nowData
          if (dayscNow < daysCount) { //如果计数器没满
            let days = k + 1
            if (days < 10) {
              days = "0" + days
            }

            let selected = 0;
            let canUse = 1
            let live = "";
            if (this.data.fullDate != null) {
              let dayNum = Number(days);
              if (this.data.fullDate.indexOf(yearList[i] + "-" + mList[j] + '-' + dayNum) != '-1') {
                selected = 1;
                live = "满房";
                canUse = 0
              }
            }
            if (Number(yearList[i] + "" + mList[j] + days) >= sDate) {
              if (Number(yearList[i] + "" + mList[j] + days) <= eDate) {
                selected = 1;
              }
              if (Number(yearList[i] + "" + mList[j] + days) == sDate) {
                live = "入住";
              } else if (Number(yearList[i] + "" + mList[j] + days) == eDate) {
                live = "离店";
              }
            }


            if (yearList[i] == year && mList[j] == month) { //判断当年当月
              if (k + 1 >= day) {
                nowData = {
                  year: yearList[i],
                  month: mList[j],
                  day: k + 1,
                  date: yearList[i] + "" + mList[j] + days,
                  selected: selected,
                  canUse: canUse,
                  live: live,
                  re: yearList[i] + "-" + mList[j] + "-" + days,
                }
                dataMonth.push(nowData)
                if (k + 1 == day) {
                  let date = new Date(yearList[i] + "-" + mList[j] + "-" + (k + 1))
                  let weekss = date.getDay() //获取每个月第一天是周几
                  weeks.push(weekss)
                }
              }
            } else { //其他情况
              nowData = { //组装自己需要的数据
                year: yearList[i],
                month: mList[j],
                day: k + 1,
                date: yearList[i] + "" + mList[j] + days,
                selected: selected,
                canUse: canUse,
                live: live,
                re: yearList[i] + "-" + mList[j] + "-" + days,
              }
              dataMonth.push(nowData)
              if (k == 0) {
                let date = new Date(yearList[i] + "-" + mList[j] + "-" + k + 1)
                let weekss = date.getDay() //获取每个月第一天是周几
                weeks.push(weekss)
              }
            }
          } else {
            break
          }
        }
        dataAll.push(dataMonth)
      }
    }
    for (let i = 0; i < dataAll.length; i++) {
      if (dataAll[i].length != 0) {
        dataAll2.push(dataAll[i]);
      }
    }


    this.setData({
      eDate: null,
      sDate: null,
      chooseDate: null,
      date: dataAll2,
      weeks: weeks
    })
  },
  onLoad: function(options) {

    var chooseDate = options.chooseDate;
    var roomType = options.roomType;

    var eDate = options.eDate;
    var sDate = options.sDate;
    var that = this;


    var myAmapFun = new amapFile.AMapWX({
      key: "bc738944276ec1564452d91e8e88b634"
    });
    myAmapFun.getWeather({
      success: function(data) {
        var weather = data.weather.data;
        if (weather.indexOf("晴") != -1) {
          that.setData({
            weatherImg: that.data.imgUrl + "/imgs/weather/jingtian.png"
          });
        } else if (weather.indexOf("云") != -1) {
          that.setData({
            weatherImg: that.data.imgUrl + "/imgs/weather/duoyun.png"
          });
        } else if (weather.indexOf("雨") != -1) {
          that.setData({
            weatherImg: that.data.imgUrl + "/imgs/weather/xiayu.png"
          });
        } else if (weather.indexOf("晚") != -1 || weather.indexOf("夜") != -1) {
          that.setData({
            weatherImg: that.data.imgUrl + "/imgs/weather/wan.png"
          });
        } else if (weather.indexOf("阴") != -1 || weather.indexOf("雾") != -1 || weather.indexOf("霾") != -1) {
          that.setData({
            weatherImg: that.data.imgUrl + "/imgs/weather/yintian.png"
          });
        } else {
          that.setData({
            weatherImg: that.data.imgUrl + "/imgs/weather/duoyun.png"
          });
        }

        that.setData({
          weather: data,
          roomType
        });
      },
      fail: function(info) {
        wx.showModal({
          title: info.errMsg
        })
      }
    });

    var time = util.formatTime2(new Date());
    // 再通过setData更改Page()里面的data，动态更新页面的数据 

    this.setData({
      time: time
    });
    if (chooseDate) {
      let time = options.time.split("-");
      let startDate = time[0];
      let endDate = time[1];
      this.setData({
        startDate: startDate,
        endDate: endDate,
        chooseDate: chooseDate,
        eDate: eDate,
        sDate: sDate
      });
    }
    this.dateData();
    this.getOrderTime(roomType);

  },
  selectday: function(event) {
    var that = this
    var lastdate = "";
    var nextdate = "";
    var day = event.currentTarget.dataset.indexs;
    var month = event.currentTarget.dataset.index;
    var date = this.data.date;

    // 旧业务 
    // if (this.data.startDate && this.data.endDate && this.data.startDate != this.data.endDate) {
    //   var enddate = this.data.endDate.split("/");
    //   var startdate = this.data.startDate.split("/");
    //   var end = date[enddate[0]][enddate[1]];
    //   var start = date[startdate[0]][startdate[1]];
    //   nextdate = date[month][day];
    //   if (Number(end.date) > Number(nextdate.date) && Number(nextdate.date) >= Number(start.date)) {
    //     return;
    //   }
    // }

    if (this.data.startDate && this.data.endDate && this.data.startDate != this.data.endDate) {
      if (date[month][day].canUse === 0) {
        return;
      }
      this.dateData();
      var date = this.data.date;
      date[month][day].selected = !date[month][day].selected;
      date[month][day].live = "入住";

      var startDate = month + "/" + day;
      this.setData({
        startDate,
        endDate: null,
        date
      });
      return;
    }
    if (date[month][day].canUse === 1) {
      date[month][day].selected = !date[month][day].selected
    }

    if (this.data.startDate) {
      var lastdate = this.data.startDate.split("/");
      lastdate = date[lastdate[0]][lastdate[1]];
      nextdate = date[month][day];
    }
    if (!this.data.startDate || Number(lastdate.date) > Number(nextdate.date)) {
      if (this.data.startDate && this.data.endDate) {
        lastdate.selected = true;
        lastdate.live = "";
        this.setDate(month, day);
      } else if (this.data.startDate) {
        lastdate.selected = false;
        lastdate.live = "";
      }
      if (date[month][day].canUse == 0) {
        return;
      }
      this.data.startDate = month + '/' + day;
      date[month][day].live = "入住";
    } else if (this.data.startDate === month + '/' + day) {
      this.data.startDate = null;
      this.data.endDate = null;
      date[month][day].live = "";
    } else {
      this.setDate(month, day);
      return null;
    };
    //设置当前样式
    this.setData({
      date
    })
  },
  getOrderTime: function(id) {
    let that = this;
    wx.request({
      url: this.data.orderUrl + '/wx/getOrderDate?roomType=' + id,
      method: 'get',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
      },
      // data: { pageInfo: pageInfo  },
      success: function(data) {
        if (data.data.resultCode === 200) {
          var date = that.data.date;
          for (var d = 0; d < data.data.data.length; d++) {
            let fullHouseDate = data.data.data[d].split('-');
            let month = fullHouseDate[1] - date[0][0].month;
            if (month < 0) {
              month = month + 12
            }
            let day = fullHouseDate[2] - date[month][0].day;
            if (day<0){
              continue;
            }
            date[month][day].selected = true;
            date[month][day].live = '满房';
            date[month][day].canUse = 0;
          }
          that.setData({
            date,
            fullDate: data.data.data
          });

        } else {
          wx.showToast({
            title: '系统异常',
            icon: 'none',
            duration: 2000
          })
        }
      },
      fail: function() {
        wx.showToast({
          title: '网络异常',
          icon: 'none',
          duration: 2000
        })
      }
    })
  },
  setDate: function(month, day) {
    var date = this.data.date;
    let oldDate = this.data.date;

    this.setData({
      oldDate: this.data.date
    });


    this.data.endDate = null;



    var startDate = this.data.startDate.split('/');
    var startDay = Number(startDate[1]) + 1;
    var startMonth = Number(startDate[0]);

    var flag = true;

    if (this.data.endDate) {
      var endDate = this.data.endDate.split("/");
      var startDate = this.data.startDate.split("/");

      var startdayt = date[startDate[0]][startDate[1]].date;
      var endday = date[endDate[0]][endDate[1]].date;
      var nextday = date[month][day].date;
      if (Number(endday) < Number(nextday)) {
        flag = false;
        this.data.endDate = month + '/' + day;
      } else {
        this.data.endDate = null;
      }
      if (Number(startdayt) > Number(nextday)) {
        startDay = day;
        startMonth = month;
        this.data.startDate = startMonth + "/" + startDay;
        day = Number(endDate[1]);
        month = Number(endDate[0]);
        flag = false;
        this.data.endDate = month + '/' + day;
      }
    } else {
      if (date[month][day].canUse === 0) {
        this.data.endDate = null;
      } else {
        this.data.endDate = month + '/' + day;
      }
    }



    var spEndDay = 34;
    var flag = true;

    for (var m = startDate[0]; m <= month; m++) {
      var days = date[m];

      // 月份不同判断方法不同
      if (startDate[0] == month) {
        for (var d = startDay; d < day; d++) {
          if (date[m][d].canUse === 0) {
            flag = false;
          }
          var selected = !date[startDate[0]][d].selected
          if (!flag) {
            selected = true;
          }
          date[startDate[0]][d].selected = selected;
          date[startDate[0]][d].live = "";
        }
      } else {
        for (var d = startDay; d < spEndDay; d++) {
          if (date[m][d]) {
            if (date[m][d].canUse === 0) {
              flag = false;
            }
            var selected = !date[m][d].selected;
            if (!flag) {
              selected = true;
            }
            date[m][d].selected = selected;
            date[m][d].live = "";

          } else {
            if (Number(m) + 1 == month) {
              spEndDay = day;
            }
            startDay = 0;
            break;
          }
        }
      }
    }

    if (date[month][day].canUse === 1) {
      date[month][day].live = "离店";
    }
    if (flag === false) {
      this.dateData();
      this.setData({
        startDate: null,
        endDate: null
      })
      return null;
    }

    //设置当前样式
    this.setData({
      date
    })
  }
})