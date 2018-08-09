<template>
    <div class="sidebar">
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color="#324157"
                 text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index" :key="item.index">
                        <template slot="title">
                            <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                        </template>
                        <el-menu-item v-for="(subItem,i) in item.subs" :key="i" :index="subItem.index">
                            {{ subItem.title }}
                        </el-menu-item>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" :key="item.index">
                        <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                collapse: false,
                items: [
                    {
                        icon: 'el-icon-setting',
                        index: 'dashboard',
                        title: '系统首页'
                    },
                    {
                        icon: 'el-icon-location',
                        index: 'shop',
                        title: '门店管理'
                    },
                    {
                        icon: 'el-icon-star-off',
                        index: 'room',
                        title: '房间列表'
                    },
                    {
                        icon: 'el-icon-setting',
                        index: 'clean',
                        title: '清洁列表'
                    },
                    {
                        icon: 'el-icon-info',
                        index: 'user',
                        title: '用户管理'
                    },
                    {
                        icon: 'el-icon-star-on',
                        index: 'coupon',
                        title: '优惠券管理'
                    },
                    {
                        icon: 'el-icon-news',
                        index: 'point',
                        title: '积分管理'
                    }
                    ,
                    {
                        icon: 'el-icon-goods',
                        index: 'pointExchange',
                        title: '积分兑换'
                    }
                    ,
                    {
                        icon: 'el-icon-tickets',
                        index: 'order',
                        title: '订单中心'
                    },
                    {
                        icon: 'el-icon-message',
                        index: 'assess',
                        title: '评价中心'
                    },
                    {
                        icon: 'el-icon-service',
                        index: 'advise',
                        title: '意见反馈'
                    },
                    {
                        icon: 'el-icon-error',
                        index: '404',
                        title: '404页面'
                    }
                ]
            }
        },
        computed: {
            onRoutes() {
                return this.$route.path.replace('/', '');
            }
        }
    }
</script>

<style scoped>
    .sidebar {
        display: block;
        position: absolute;
        left: 0;
        top: 70px;
        bottom: 0;
    }

    .sidebar-el-menu:not(.el-menu--collapse) {
        width: 250px;
    }

    .sidebar > ul {
        height: 100%;
    }
</style>
