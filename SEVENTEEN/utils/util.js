const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}


const formatTime2 = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  const dayCn = new Date().getDay();
  var text="";
  switch (dayCn) {
    case 0:
      text = "周日";
      break;
    case 1:
      text = "周一";
      break;
    case 2:
      text = "周二";
      break;
    case 3:
      text = "周三";
      break;
    case 4:
      text = "周四";
      break;
    case 5:
      text = "周五";
      break;
    case 6:
      text = "周六";
      break;
  }

  return month + "月" + day + "日，" + text;
}
module.exports = {
  formatTime: formatTime,
  formatTime2: formatTime2
}
