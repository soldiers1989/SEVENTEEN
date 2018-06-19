<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>用户列表</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="getData(1)">搜索</el-button>
            </div>
            <el-table :data="tableData" border style="width: 100%" ref="multipleTable"
            >
                <el-table-column prop="username" label="姓名" width="100">
                </el-table-column>
                <el-table-column prop="phone" label="电话" width="150">
                </el-table-column>
                <el-table-column prop="lastOrderTime" label="最后订单时间">
                </el-table-column>
                <el-table-column prop="lastLoginTime" label="最后登录时间">
                </el-table-column>
                <el-table-column prop="createDate" :formatter="dateFormat" label="用户创建时间">
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                    @current-change="handleCurrentChange"
                    layout="total,sizes,prev,pager,next,jumper"
                    :total="total" :current-page="curPage">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
    import moment from 'moment';

    export default {
        data() {
            return {
                UserUrl: this.$global.baseUrl + "/sys/users/condition",
                tableData: [],
                curPage: 1,
                total: 0,
                select_word: ''
            }
        },
        created() {
            this.getData(1);
        },
        methods: {
            // 分页导航
            handleCurrentChange(val) {
                this.curPage = val;
                this.getData(val);
            },
            getData(page) {
                const param = {
                    "pageNum": page,
                    "status": this.select_word == null ? "" : this.select_word.trim()
                }
                this.$axios.get(this.UserUrl, {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.tableData = res.data.data;
                        this.total = res.data.pageInfo.total;
                        this.curPage = res.data.pageInfo.pageNum;
                    }
                })
            },
            //时间格式化
            dateFormat: function (row, column) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD");
            }
        }
    }

</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }

    .del-dialog-cnt {
        font-size: 16px;
        text-align: center
    }
</style>
