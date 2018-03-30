var amapFile = require('../../libs/amap-wx.js');
var util = require('../../utils/util.js');


Page({
  data: {
    currentTab: 1,
    tabArray: ["重选", "确认"],
    weatherImg: "",
    selected: "0",
    startDate: null,
    endDate: null,
    date: {}
  },

  swichNav: function (e) {
    var currentTab = e.target.dataset.current
    if (currentTab === 0) {
      this.dateData();
    }
    var that = this;
    if (this.data.currentTab === currentTab) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      })
    }
  },
  dateData: function () {
    let dataAll = []//总日历数据
    let dataAll2 = []//总日历数据
    let dataMonth = []//月日历数据
    let date = new Date//当前日期
    let year = date.getFullYear()//当前年
    let week = date.getDay();//当天星期几
    let weeks = []
    let month = date.getMonth() + 1//当前月份
    let day = date.getDate()//当天
    let daysCount = 365//一共显示多少天
    let dayscNow = 0//计数器
    let monthList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]//月份列表
    let nowMonthList = []//本年剩余年份
    for (let i = month; i < 13; i++) {
      nowMonthList.push(i)
    }
    let yearList = [year]//年份最大可能
    for (let i = 0; i < daysCount / 365 + 2; i++) {
      yearList.push(year + i + 1)
    }
    let leapYear = function (Year) {//判断是否闰年 
      if (((Year % 4) == 0) && ((Year % 100) != 0) || ((Year % 400) == 0)) {
        return (true);
      } else { return (false); }
    }
    for (let i = 0; i < yearList.length; i++) {//遍历年
      let mList
      if (yearList[i] == year) {//判断当前年份
        mList = nowMonthList
      } else {
        mList = monthList
      }
      for (let j = 0; j < mList.length; j++) {//循环月份
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
        for (let k = 0; k < t_days[j]; k++) {//循环每天
          dayscNow++
          let nowData
          if (dayscNow < daysCount) {//如果计数器没满
            let days = k + 1
            if (days < 10) {
              days = "0" + days
            }
            if (yearList[i] == year && mList[j] == month) {//判断当年当月
              if (k + 1 >= day) {
                nowData = {
                  year: yearList[i],
                  month: mList[j],
                  day: k + 1,
                  date: yearList[i] + "" + mList[j] + days,
                  selected: 0,
                  re: yearList[i] + "-" + mList[j] + "-" + days,
                }
                dataMonth.push(nowData)
                if (k + 1 == day) {
                  let date = new Date(yearList[i] + "-" + mList[j] + "-" + (k + 1))
                  let weekss = date.getDay()//获取每个月第一天是周几
                  weeks.push(weekss)
                }
              }
            } else {//其他情况
              nowData = {//组装自己需要的数据
                year: yearList[i],
                month: mList[j],
                day: k + 1,
                date: yearList[i] + "" + mList[j] + days,
                selected: 0,
                re: yearList[i] + "-" + mList[j] + "-" + days,
              }
              dataMonth.push(nowData)
              if (k == 0) {
                let date = new Date(yearList[i] + "-" + mList[j] + "-" + k + 1)
                let weekss = date.getDay()//获取每个月第一天是周几
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
      startDate: null,
      endDate: null,
      date: dataAll2,
      weeks: weeks
    })
  },
  onLoad: function () {
    var that = this;
    var myAmapFun = new amapFile.AMapWX({ key: "bc738944276ec1564452d91e8e88b634" });
    myAmapFun.getWeather({
      success: function (data) {
        var weather = data.weather.data;
        if (weather.indexOf("晴") != -1) {
          that.setData({
            weatherImg: "../../imgs/weather/jingtian.png"
          });
        } else if (weather.indexOf("云") != -1) {
          that.setData({
            weatherImg: "../../imgs/weather/duoyun.png"
          });
        } else if (weather.indexOf("雨") != -1) {
          that.setData({
            weatherImg: "../../imgs/weather/xiayu.png"
          });
        } else if (weather.indexOf("晚") != -1 || weather.indexOf("夜") != -1) {
          that.setData({
            weatherImg: "../../imgs/weather/wan.png"
          });
        } else if (weather.indexOf("阴") != -1 || weather.indexOf("雾") != -1 || weather.indexOf("霾") != -1) {
          that.setData({
            weatherImg: "../../imgs/weather/yintian.png"
          });
        } else {
          that.setData({
            weatherImg: "../../imgs/weather/duoyun.png"
          });
        }

        that.setData({
          weather: data,
        });
      },
      fail: function (info) {
        // wx.showModal({title:info.errMsg})
      }
    });

    var time = util.formatTime2(new Date());
    var date = new Date().getDay();
    // 再通过setData更改Page()里面的data，动态更新页面的数据  
    this.setData({
      time: time
    });
    this.dateData();
  },
  selectday: function (event) {
    var that = this
    var day = event.currentTarget.dataset.indexs;
    var month = event.currentTarget.dataset.index;
    var date = this.data.date;
    var test = this.data.startDate;
    date[month][day].selected = !date[month][day].selected

    if (this.data.startDate && this.data.endDate && this.data.startDate != this.data.endDate && date[month][day].selected === false && this.data.endDate === month + '/' + day) {
      console.log("hello");
      // this.data.endDate = null;
    } else if (this.data.startDate && this.data.endDate && this.data.startDate != this.data.endDate) {
      return;
    }

    if (!this.data.startDate) {
      this.data.startDate = month + '/' + day;
      date[month][day].live = "入住";
    } else if (this.data.startDate === month + '/' + day) {
      this.data.startDate = null;
      this.data.endDate = null;
      date[month][day].live = "";
    } else {
      // this.data.endDate = month + '/' + day;

      date[month][day].live = "离店";

      var startDate = this.data.startDate.split('/');

      var startDay = Number(startDate[1]) + 1;
      var spEndDay = 34;

      for (var m = startDate[0]; m <= month; m++) {
        var days = date[m];

        // 月份不同判断方法不同
        if (startDate[0] == month) {
          for (var d = startDay; d < day; d++) {
            date[startDate[0]][d].selected = !date[startDate[0]][d].selected;
          }
        } else {
          for (var d = startDay; d < spEndDay; d++) {
            if (date[m][d]) {
              date[m][d].selected = !date[m][d].selected;
              console.log(m + "," + d);
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
    };
    //设置当前样式
    this.setData({
      date
    })
  }

})