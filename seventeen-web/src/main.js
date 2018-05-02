import 'element-ui/lib/theme-default/index.css';    // 默认主题
import "babel-polyfill";
import Vue from 'vue';
import axios from 'axios';
import ElementUI from 'element-ui';
import router from './router';
import App from './App';
import global from './Global';
import {Message} from 'element-ui';
import store from './store'

Vue.prototype.$axios = axios;
Vue.prototype.$global = global;
Vue.use(ElementUI);

//发起网络请求拦截器
axios.interceptors.request.use(config => {
    const token = sessionStorage.getItem("token");
    if (token) {  // 判断是否存在token，如果存在的话，则每个http header都加上token
        config.headers.Authorization = "Bearer " + token;
    }
    return config;
}, error => {
    return Promise.reject(error);
});


//网络请求返回结果拦截器
axios.interceptors.response.use(response => {
    return response;
}, error => {
    if (error.response) {
        switch (error.response.status) {
            case 401:
                // 返回 401 清除token信息并跳转到登录页面
                sessionStorage.removeItem("token");
                router.replace({
                    path: '/login',
                    query: {redirect: router.currentRoute.fullPath}
                });
                break;
            case 403:
                console.debug(error.response)
                Message.error("无操作权限");
                break;
        }
    } else {
        Message.error("网络连接失败，请检查");
    }
    return Promise.reject(error);
});


new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');

