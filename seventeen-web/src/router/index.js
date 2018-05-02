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
            children: [
                {
                    path: '/',
                    component: resolve => require(['../components/page/Readme.vue'], resolve),
                    meta: {requiresAuth: true},
                },
                {
                    path: '/sys/authorities',
                    component: resolve => require(['../components/page/SysAuthorities.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/sys/roles',
                    component: resolve => require(['../components/page/SysRoles.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/sys/users',
                    component: resolve => require(['../components/page/SysUsers.vue'], resolve),
                    meta: {requiresAuth: true}
                }, {
                    path: '/sys/file',
                    component: resolve => require(['../components/page/SysFiles.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/doc/swaggerAPI',
                    component: resolve => require(['../components/page/SwaggerApi.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/doc/chrsAPI',
                    component: resolve => require(['../components/page/ChrsApi.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/doc/dataCleanAPI',
                    component: resolve => require(['../components/page/DataCleanApi.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/food/book/clawer',
                    component: resolve => require(['../components/page/FoodBookCrawlerList.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    name: 'foodBookClawerEdit',
                    path: '/food/book/clawer/edit',
                    component: resolve => require(['../components/page/FoodBookCrawlerEdit.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/food/books',
                    component: resolve => require(['../components/page/FoodBookList.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    name: 'foodBookView',
                    path: '/food/books/view',
                    component: resolve => require(['../components/page/FoodBookView.vue'], resolve),
                    meta: {requiresAuth: true}
                },
                {
                    path: '/vueeditor',
                    component: resolve => require(['../components/page/VueEditor.vue'], resolve)    // Vue-Quill-Editor组件
                },
                {
                    path: '/markdown',
                    component: resolve => require(['../components/page/Markdown.vue'], resolve)     // Vue-Quill-Editor组件
                },
                {
                    path: '/upload',
                    component: resolve => require(['../components/page/Upload.vue'], resolve)       // Vue-Core-Image-Upload组件
                },
                {
                    path: '/basecharts',
                    component: resolve => require(['../components/page/BaseCharts.vue'], resolve)   // vue-schart组件
                },
                {
                    path: '/drag',
                    component: resolve => require(['../components/page/DragList.vue'], resolve)    // 拖拽列表组件
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

