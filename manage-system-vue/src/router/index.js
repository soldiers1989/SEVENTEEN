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
                    path: '/',
                    component: resolve => require(['../components/page/Readme.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/basetable',
                    component: resolve => require(['../components/page/BaseTable.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/vuetable',
                    component: resolve => require(['../components/page/VueTable.vue'], resolve),
                    meta: {requiresAuth: true}     // vue-datasource组件
                },
                {
                    path: '/baseform',
                    component: resolve => require(['../components/page/BaseForm.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/vueeditor',
                    component: resolve => require(['../components/page/VueEditor.vue'], resolve),
                    meta: {requiresAuth: true}    // Vue-Quill-Editor组件
                },
                {
                    path: '/markdown',
                    component: resolve => require(['../components/page/Markdown.vue'], resolve),
                    meta: {requiresAuth: true}     // Vue-Quill-Editor组件
                },
                {
                    path: '/upload',
                    component: resolve => require(['../components/page/Upload.vue'], resolve),
                    meta: {requiresAuth: true}       // Vue-Core-Image-Upload组件
                },
                {
                    path: '/basecharts',
                    component: resolve => require(['../components/page/BaseCharts.vue'], resolve),
                    meta: {requiresAuth: true}   // vue-schart组件
                },
                {
                    path: '/drag',
                    component: resolve => require(['../components/page/DragList.vue'], resolve),
                    meta: {requiresAuth: true}    // 拖拽列表组件
                }
            ]
        },
        {
            path: '/login',
            component: resolve => require(['../components/page/Login.vue'], resolve)
        },
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


