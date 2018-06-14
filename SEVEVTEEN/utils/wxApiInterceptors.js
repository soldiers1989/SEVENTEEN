'use strict';

var regeneratorRuntime = require('./runtime');

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

function _asyncToGenerator(fn) { return function () { var gen = fn.apply(this, arguments); return new Promise(function (resolve, reject) { function step(key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { return Promise.resolve(value).then(function (value) { step("next", value); }, function (err) { step("throw", err); }); } } return step("next"); }); }; }

module.exports = function () {
  var interceptors = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

  var oldWx = _extends({}, wx);

  wx = new Proxy({}, {
    get: function get(receiver, name) {
      var _this = this;

      if (name === 'request') {
        return receiver.request;
      }
      if (name === 'old') {
        return oldWx;
      }
      return function () {
        for (var _len = arguments.length, arg = Array(_len), _key = 0; _key < _len; _key++) {
          arg[_key] = arguments[_key];
        }

        var _arg$ = arg[0],
          params = _arg$ === undefined ? {} : _arg$;

        var handleIntercept = function () {
          var _ref = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee2() {
            var isAsync = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

            var resFn, _interceptors$name, _interceptors$name$re, request, _interceptors$name$re2, response;

            return regeneratorRuntime.wrap(function _callee2$(_context2) {
              while (1) {
                switch (_context2.prev = _context2.next) {
                  case 0:
                    resFn = function resFn(res, cb) {
                      cb(res);
                    };

                    if (!interceptors[name]) {
                      _context2.next = 21;
                      break;
                    }

                    _interceptors$name = interceptors[name], _interceptors$name$re = _interceptors$name.request, request = _interceptors$name$re === undefined ? function () {
                      return params;
                    } : _interceptors$name$re, _interceptors$name$re2 = _interceptors$name.response, response = _interceptors$name$re2 === undefined ? function (obj) {
                      return obj;
                    } : _interceptors$name$re2;
                    _context2.prev = 3;

                    if (!isAsync) {
                      _context2.next = 8;
                      break;
                    }

                    _context2.t1 = request(params);
                    _context2.next = 11;
                    break;

                  case 8:
                    _context2.next = 10;
                    return request(params);

                  case 10:
                    _context2.t1 = _context2.sent;

                  case 11:
                    _context2.t0 = _context2.t1;

                    if (_context2.t0) {
                      _context2.next = 14;
                      break;
                    }

                    _context2.t0 = params;

                  case 14:
                    params = _context2.t0;
                    _context2.next = 20;
                    break;

                  case 17:
                    _context2.prev = 17;
                    _context2.t2 = _context2['catch'](3);
                    throw console.error(_context2.t2);

                  case 20:
                    resFn = function () {
                      var _ref2 = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee(res, cb) {
                        return regeneratorRuntime.wrap(function _callee$(_context) {
                          while (1) {
                            switch (_context.prev = _context.next) {
                              case 0:
                                if (!isAsync) {
                                  _context.next = 4;
                                  break;
                                }

                                _context.t1 = response(res);
                                _context.next = 7;
                                break;

                              case 4:
                                _context.next = 6;
                                return response(res);

                              case 6:
                                _context.t1 = _context.sent;

                              case 7:
                                _context.t0 = _context.t1;

                                if (_context.t0) {
                                  _context.next = 10;
                                  break;
                                }

                                _context.t0 = res;

                              case 10:
                                res = _context.t0;

                                cb(res);

                              case 12:
                              case 'end':
                                return _context.stop();
                            }
                          }
                        }, _callee, _this);
                      }));

                      return function resFn(_x3, _x4) {
                        return _ref2.apply(this, arguments);
                      };
                    }();

                  case 21:
                    return _context2.abrupt('return', resFn);

                  case 22:
                  case 'end':
                    return _context2.stop();
                }
              }
            }, _callee2, _this, [[3, 17]]);
          }));

          return function handleIntercept() {
            return _ref.apply(this, arguments);
          };
        }();
        var isAsync = oldWx.canIUse(name + '.success') || !oldWx.canIUse(name + '.return') && oldWx.canIUse(name + '.object') || interceptors[name];
        if (interceptors[name] && !isAsync) {
          handleIntercept(true);
          arg[0] = params;
        } else if (isAsync || interceptors[name]) {
          return new Promise(function () {
            var _ref3 = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee3(resolve, reject) {
              var _params, _params$success, _success, _params$fail, _fail, resFn;

              return regeneratorRuntime.wrap(function _callee3$(_context3) {
                while (1) {
                  switch (_context3.prev = _context3.next) {
                    case 0:
                      _params = params, _params$success = _params.success, _success = _params$success === undefined ? function () {
                        return '';
                      } : _params$success, _params$fail = _params.fail, _fail = _params$fail === undefined ? function () {
                        return '';
                      } : _params$fail;
                      _context3.next = 3;
                      return handleIntercept();

                    case 3:
                      resFn = _context3.sent;

                      oldWx[name](Object.assign(params, {
                        success: function success(res) {
                          return resFn(res, function (newRes) {
                            resolve(newRes);
                            _success(newRes);
                          });
                        },
                        fail: function fail(res) {
                          return resFn(res, function (newRes) {
                            reject(newRes);
                            _fail(newRes);
                          });
                        }
                      }));

                    case 5:
                    case 'end':
                      return _context3.stop();
                  }
                }
              }, _callee3, _this);
            }));

            return function (_x5, _x6) {
              return _ref3.apply(this, arguments);
            };
          }());
        }
        return oldWx[name].apply(oldWx, arg);
      };
    }
  });

  wx.request = new Proxy(function () {
    var _ref4 = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee5(url) {
      var params = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

      var _interceptors$request, request;

      return regeneratorRuntime.wrap(function _callee5$(_context5) {
        while (1) {
          switch (_context5.prev = _context5.next) {
            case 0:
              if ((typeof url === 'undefined' ? 'undefined' : _typeof(url)) === 'object') {
                params = url;
                url = url.url;
              } else {
                params.url = url;
              }

              if (!interceptors.request) {
                _context5.next = 15;
                break;
              }

              _interceptors$request = interceptors.request.request, request = _interceptors$request === undefined ? function (obj) {
                return obj;
              } : _interceptors$request;
              _context5.prev = 3;
              _context5.next = 6;
              return request(_extends({}, params, { url: url }));

            case 6:
              _context5.t0 = _context5.sent;

              if (_context5.t0) {
                _context5.next = 9;
                break;
              }

              _context5.t0 = params;

            case 9:
              params = _context5.t0;
              _context5.next = 15;
              break;

            case 12:
              _context5.prev = 12;
              _context5.t1 = _context5['catch'](3);
              throw console.error(_context5.t1);

            case 15:
              return _context5.abrupt('return', new Promise(function (resolve, reject) {
                var resFn = function () {
                  var _ref5 = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee4(res, cb) {
                    var _res, statusCode, _interceptors$request2, response;

                    return regeneratorRuntime.wrap(function _callee4$(_context4) {
                      while (1) {
                        switch (_context4.prev = _context4.next) {
                          case 0:
                            _res = res, statusCode = _res.statusCode;

                            if (!interceptors.request) {
                              _context4.next = 16;
                              break;
                            }

                            _interceptors$request2 = interceptors.request.response, response = _interceptors$request2 === undefined ? function () {
                              return res;
                            } : _interceptors$request2;
                            _context4.prev = 3;
                            _context4.next = 6;
                            return response(res);

                          case 6:
                            _context4.t0 = _context4.sent;

                            if (_context4.t0) {
                              _context4.next = 9;
                              break;
                            }

                            _context4.t0 = res;

                          case 9:
                            res = _context4.t0;
                            _context4.next = 16;
                            break;

                          case 12:
                            _context4.prev = 12;
                            _context4.t1 = _context4['catch'](3);

                            reject(_context4.t1);
                            _fail2(_context4.t1);

                          case 16:
                            if (!(statusCode >= 400)) {
                              _context4.next = 20;
                              break;
                            }

                            reject(res);
                            _fail2(res);
                            return _context4.abrupt('return');

                          case 20:
                            cb(res);

                          case 21:
                          case 'end':
                            return _context4.stop();
                        }
                      }
                    }, _callee4, this, [[3, 12]]);
                  }));

                  return function resFn(_x9, _x10) {
                    return _ref5.apply(this, arguments);
                  };
                }();

                var _params2 = params,
                  _params2$success = _params2.success,
                  _success2 = _params2$success === undefined ? function () {
                    return '';
                  } : _params2$success,
                  _params2$fail = _params2.fail,
                  _fail2 = _params2$fail === undefined ? function () {
                    return '';
                  } : _params2$fail;

                oldWx.request(Object.assign(params, {
                  success: function success(res) {
                    return resFn(res, function (newRes) {
                      _success2(newRes);
                      resolve(newRes);
                    });
                  },
                  fail: function fail(res) {
                    return resFn(res, function (newRes) {
                      _fail2(newRes);
                      reject(newRes);
                    });
                  }
                }));
              }));

            case 16:
            case 'end':
              return _context5.stop();
          }
        }
      }, _callee5, undefined, [[3, 12]]);
    }));

    return function (_x7) {
      return _ref4.apply(this, arguments);
    };
  }(), {
      get: function get(receiver, method) {
        return function (url) {
          var params = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

          if (params.success || params.fail) {
            return oldWx.request(params);
          }
          Object.assign(params, {
            method: method
          });
          return wx.request(url, params);
        };
      }
    });
};