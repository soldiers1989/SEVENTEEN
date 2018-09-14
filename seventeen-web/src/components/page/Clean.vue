<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>打扫列表</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="select_word" placeholder="房号" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                <el-button type="primary" icon="el-icon-search" @click="cleanListToday()">今天清洁名单</el-button>
            </div>
            <el-table :data="tableData" border style="width: 100%">
                <el-table-column prop="shopName" label="门店" sortable width="201">
                </el-table-column>
                <el-table-column prop="apNum" label="房号">
                </el-table-column>
                <el-table-column prop="createTime" label="申请时间">
                </el-table-column>
                <el-table-column prop="isCleaned" label="是否打扫">
                </el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button size="small" @click="handleClean(scope.id, scope.row)">打扫</el-button>
                    </template>
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

        <!-- 优惠券使用记录 -->
        <el-dialog title="今天清洁列表" :visible.sync="clean" width="70%">
            <div class="container">
                <el-table
                    :data="cleanList"
                    stripe
                    style="width: 100%">
                    <el-table-column prop="shopName" label="门店" sortable width="201">
                    </el-table-column>
                    <el-table-column prop="apNum" label="房号">
                    </el-table-column>
                    <el-table-column prop="createTime" label="申请时间">
                    </el-table-column>
                </el-table>
            </div>
        </el-dialog>


    </div>
</template>

<script>
    var qs = require('qs');

    export default {
        data() {
            return {
                cleanUrl: this.$global.baseUrl + "/room/clean",
                tableData: [],
                curPage: 1,
                total: 0,
                title: '',
                remark:'',
                clean:false,
                cleanList:[],
                multipleSelection: [],
                select_word: '',
                is_search: false,
                visible: false,
                add: false,
                edit: false,
                point:'',
                ids: '',
                ruleForm: {

                },
                idx: -1
            }
        },
        created() {
            this.getData();
        },
        methods: {
            // 分页导航
            handleCurrentChange(val) {
                this.curPage = val;
                this.getData(val);
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            getData(page) {
                const param = {
                    "pageNum": this.curPage,
                    "apNum": this.select_word == null ? "" : this.select_word.trim()
                }
                this.$axios.get(this.cleanUrl, {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.tableData = res.data.data;
                        this.total = res.data.pageInfo.total;
                        this.curPage = res.data.pageInfo.pageNum;
                    }
                })
            },
            search() {
                this.is_search = true;
                this.getData()
            },
            handleClean(index, row){
                const param = {
                    "apId":  row.apId
                }
                this.$axios.put(this.cleanUrl, {apId:row.apId}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.getData();
                    }
                })
            },
            cleanListToday() {
                this.clean = true;
                this.$axios.get(this.cleanUrl+"/cleanToday").then((res) => {
                    if (res.data.resultCode == 200) {
                        this.cleanList = res.data.data;
                    }
                })
            },
            formatter(row, column) {
                return row.address;
            },
            filterTag(value, row) {
                return row.tag === value;
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

    .el-select .el-input {
        width: 130px;
    }

    .input-with-select .el-input-group__prepend {
        background-color: #fff;
    }
</style>
