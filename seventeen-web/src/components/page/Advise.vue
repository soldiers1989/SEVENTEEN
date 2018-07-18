<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>房间列表</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-col :span="3">
                    <el-date-picker type="date" placeholder="开始日期" v-model="startTime"
                                    style="width: 100%;" value-format="yyyy-MM-dd"></el-date-picker>
                </el-col>
                <el-col class="line" :span="0.1">-</el-col>
                <el-col :span="3">
                    <el-date-picker
                        type="date" placeholder="结束日期"
                        style="width: 100%;" value-format="yyyy-MM-dd" v-model="endTime">
                    </el-date-picker>
                </el-col>
                <!--<el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>-->
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
            </div>
            <el-table :data="tableData" border style="width: 100%" >
                <el-table-column prop="id" label="ID" sortable width="100">
                </el-table-column>
                <el-table-column prop="createBy" label="用户" width="150">
                </el-table-column>
                <el-table-column prop="content" label="内容">
                </el-table-column>
                <el-table-column prop="phone" label="联系号码" >
                </el-table-column>
                <el-table-column prop="createTime" label="时间">
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
    var qs = require('qs');

    export default {
        data() {
            return {
                AdviseUrl: this.$global.baseUrl + "/room",
                tableData: [],
                userData: [],
                curPage: 1,
                total: 0,
                startTime:'',
                endTime:'',
                ruleForm: {
                    id: '',
                    apId: '',
                    apNum:'',
                    userName: '',
                    status: '',
                    arriveTime: '',
                    remark: '',
                    liveTime: ''
                }
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
            getData(page) {
                const param = {
                    "pageNum": this.curPage,
                    "startTime": this.startTime === null ? "" : this.startTime.trim(),
                    "endTime": this.endTime === null ? "" : this.endTime.trim()
                }
                this.$axios.get(this.AdviseUrl+"/advise", {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.tableData = res.data.data;
                        this.total = res.data.pageInfo.total;
                        this.curPage = res.data.pageInfo.pageNum;
                    }
                })
            },
            resetForm(formName) {
                if(this.$refs[formName]){
                    this.$refs[formName].resetFields();
                }
                this.getData();
            },
            search() {
                this.is_search = true;
                this.getData()
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
