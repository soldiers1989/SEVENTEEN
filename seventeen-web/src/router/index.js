import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

const router = new Router({
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/readme',
            component: resolve => require(['../components/common/Home.vue'], resolve),
            meta: {requiresAuth: true},
            children:[
                {
                    path: '/dashboard',
                    component: resolve => require(['../components/page/Dashboard.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/room',
                    component: resolve => require(['../components/page/Room.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/user',
                        component: resolve => require(['../components/page/User.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/order',
                        component: resolve => require(['../components/page/Order.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/assess',
                        component: resolve => require(['../components/page/Assess.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/coupon',
                        component: resolve => require(['../components/page/Coupon.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/point',
                        component: resolve => require(['../components/page/Point.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/advise',
                        component: resolve => require(['../components/page/Advise.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/shop',
                        component: resolve => require(['../components/page/Shop.vue'], resolve),
                    meta: {requiresAuth: true}
                }

            ]
        },
        {
            path: '/login',
            component: resolve => require(['../components/page/Login.vue'], resolve)
        },
        {
            path: '/404',
            component: resolve => require(['../components/page/404.vue'], resolve)
        },
        {
            path: '/403',
            component: resolve => require(['../components/page/403.vue'], resolve)
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
});
router.beforeEach((to, from, next) => {
    if (to.meta.requiresAuth) {  // 判断该路由是否需要登录权限
        if (sessionStorage.getItem("token")) {  // 获取当前的token是否存在
            next();
        } else {
            next({
                path: '/login',
                query: {redirect: to.fullPath},  // 将跳转的路由path作为参数，登录成功后跳转到该路由
            })
        }
    } else {
        if (to.matched.length === 0) {//如果找不到此路由，则不跳转。

        } else {
            next();
        }
    }
});

export default router;
