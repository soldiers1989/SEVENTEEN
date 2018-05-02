<template>
    <div class="sidebar">
        <el-menu :default-active="onRoutes" class="el-menu-vertical-demo" theme="dark" unique-opened router>
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index">
                        <template slot="title"><i :class="item.icon"></i>{{ item.title }}</template>
                        <el-menu-item v-for="(subItem,i) in item.subs" :key="i" :index="subItem.index">
                            {{ subItem.title }}
                        </el-menu-item>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index">
                        <i :class="item.icon"></i>{{ item.title }}
                    </el-menu-item>
                </template>
            </template>
            <a :href="this.$global.eurekaUrl" target="_blank" v-if="eureka">
                <el-menu-item index="/sys/logs02"><i class="el-icon-time"></i>1721602服务状态
                </el-menu-item>
            </a>
            <a :href="this.$global.eurekaUrl2" target="_blank" v-if="eureka">
                <el-menu-item index="/sys/logs03"><i class="el-icon-time"></i>1721603服务状态
                </el-menu-item>
            </a>
            <a :href="this.$global.zipkinUrl" target="_blank" v-if="zipkin">
                <el-menu-item index="/sys/logskin"><i class="el-icon-time"></i>接口调用跟踪
                </el-menu-item>
            </a>
        </el-menu>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                url: this.$global.baseUrl + "/sys/authorities",
                subs: [],
                doc: [],
                dataClean: [],
                items: [{
                    icon: 'el-icon-setting',
                    index: '/readme',
                    title: '自述'
                }],
                eureka: false,
                zipkin: false
            }
        },
        created() {
            this.createMenu();
        },
        computed: {
            onRoutes() {
                return this.$route.path;
            }
        },
        methods: {
            //根据权限生成菜单数据
            createMenu() {
                //获取当前用户权限
                this.$axios.get(this.url + "/user/current", {}).then((res) => {
                    if (res.data.status == 200) {
                        this.authorities = res.data.data;
                        for (let authority of this.authorities) {
                            let code = authority.code;
                            if (code.startsWith("sys_")) {//权限中包含系统管理权限
                                this.hasSys = true;
                                if (code.endsWith("_authority")) {
                                    this.subs.push({
                                        index: '/sys/authorities',
                                        title: '权限管理'
                                    });
                                } else if (code.endsWith("_role")) {
                                    this.subs.push({
                                        index: '/sys/roles',
                                        title: '角色管理'
                                    });
                                } else if (code.endsWith("_user")) {
                                    this.subs.push({
                                        index: '/sys/users',
                                        title: '用户管理'
                                    });
                                } else if (code.endsWith("_file:get")) {
                                    this.subs.push({
                                        index: '/sys/file',
                                        title: '文件管理'
                                    });
                                }
                            }
                            if (code.startsWith("dataClean")) {
                                this.dataClean.push({
                                    index: '/food/book/clawer',
                                    title: '爬虫数据'
                                });
                                this.dataClean.push({
                                    index: '/food/books',
                                    title: '入库数据'
                                });
                                this.items.push({
                                    icon: 'el-icon-time',
                                    index: '/dataClean',
                                    title: '数据清洗',
                                    subs: this.dataClean
                                });
                            }
                            if (code.startsWith("eureka")) {
                                this.eureka = true;
                            }
                            if (code.startsWith("zipkin")) {
                                this.zipkin = true;
                            }
                            if (code.startsWith("doc")) {
                                this.doc.push({
                                    index: '/doc/swaggerAPI',
                                    title: '系统管理API'
                                });
                                this.doc.push({
                                    index: '/doc/chrsAPI',
                                    title: '云食谱接口API'
                                });
                                this.doc.push({
                                    index: '/doc/dataCleanAPI',
                                    title: '数据清洗接口API'
                                });
                                this.items.push({
                                    icon: 'el-icon-document',
                                    index: '/doc',
                                    title: '系统文档',
                                    subs: this.doc
                                });
                            }
                        }
                        if (this.hasSys) {
                            this.items.push({
                                icon: 'el-icon-menu',
                                index: '/sys',
                                title: '系统管理',
                                subs: this.subs
                            });
                            this.subs = [];
                        }
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .sidebar {
        display: block;
        position: absolute;
        width: 250px;
        left: 0;
        top: 70px;
        bottom: 0;
        background: #2E363F;
    }

    .sidebar > ul {
        height: 100%;
    }

    a {
        color: inherit;
    }

    a:hover {
        color: inherit;
    }
</style>
