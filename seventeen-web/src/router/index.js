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
                    path: '/tabs',
                    component: resolve => require(['../components/page/Tabs.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/form',
                    component: resolve => require(['../components/page/BaseForm.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    // 富文本编辑器组件
                    path: '/editor',
                    component: resolve => require(['../components/page/VueEditor.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    // 图片上传组件
                    path: '/upload',
                    component: resolve => require(['../components/page/Upload.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    // vue-schart组件
                    path: '/charts',
                    component: resolve => require(['../components/page/BaseCharts.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    // 拖拽列表组件
                    path: '/drag',
                    component: resolve => require(['../components/page/DragList.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    // 权限页面
                    path: '/permission',
                    component: resolve => require(['../components/page/Permission.vue'], resolve),
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
