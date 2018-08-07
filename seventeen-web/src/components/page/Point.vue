<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>积分管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
            </div>
            <el-table :data="tableData" border style="width: 100%">
                <el-table-column prop="id" label="ID" sortable width="201">
                </el-table-column>
                <el-table-column prop="userName" label="用户">
                </el-table-column>
                <el-table-column prop="point" label="总分">
                </el-table-column>
                <el-table-column prop="updateTime" label="更新时间">
                </el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button size="small" @click="handleEdit(scope.id, scope.row)">查看详情</el-button>
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

        <!-- 新建弹出框 -->
        <el-dialog :title="title" :visible.sync="visible" width="50%" :before-close="handleClose">
            <el-form :model="ruleForm" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="用户">
                    <el-input v-model="ruleForm.userName" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="总分">
                    <el-input v-model="ruleForm.point" class="handle-input"></el-input>
                </el-form-item>
                <el-alert
                    title="历史记录"
                    type="success"
                    :closable="false">
                </el-alert>
                <el-table
                    :data="ruleForm.seUserPointLogs"
                    height="250"
                    border
                    style="width: 100%">
                    <el-table-column
                        label="订单Id"
                        prop="orderId"
                        width="180">
                    </el-table-column>
                    <el-table-column
                        label="分数+/-"
                        prop="point">
                    </el-table-column>
                    <el-table-column
                        label="时间"
                        prop="createTime">
                    </el-table-column>
                    <el-table-column
                        label="记录"
                        prop="remark">
                    </el-table-column>
                </el-table>
                <el-row>
                    <el-col :span="12">
                        <el-input v-model="point" placeholder="积分修改（+/- 分数）例如：+50"></el-input>
                    </el-col>
                    <el-col :span="12">
                        <el-input v-model="remark" placeholder="备注">
                            <el-button slot="append" @click="addPoint()" icon="el-icon-plus"></el-button>
                        </el-input>
                    </el-col>
                </el-row>

            </el-form>

        </el-dialog>
    </div>
</template>

<script>
    var qs = require('qs');

    export default {
        data() {
            return {
                PointUrl: this.$global.baseUrl + "/point",
                tableData: [],
                curPage: 1,
                total: 0,
                title: '',
                remark:'',
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
            handleClose(done) {
                const that = this;
                that.resetForm('ruleForm');
                done();
            },
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
                    "remark": this.select_word == null ? "" : this.select_word.trim()
                }
                this.$axios.get(this.PointUrl, {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.tableData = res.data.data;
                        this.total = res.data.pageInfo.total;
                        this.curPage = res.data.pageInfo.pageNum;
                    }
                })
            },
            addPoint() {
                const param = {
                    "userId": this.ruleForm.userId,
                    "point": this.point == null ? "" : this.point.trim(),
                    "remark": this.remark == null ? "" : this.remark.trim()

                }
                this.$axios.post(this.PointUrl, param).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.$axios.get(this.PointUrl + '/' + this.ruleForm.id + '/detail').then((res) => {
                            if (res.data.resultCode === 200) {
                                this.ruleForm = res.data.data;
                            } else {
                                this.$message.error('编辑失败');
                                return false;
                            }
                        })

                        this.point='';
                        this.remark='';
                    }

                })
            },
            search() {
                this.is_search = true;
                this.getData()
            },
            formatter(row, column) {
                return row.address;
            },
            filterTag(value, row) {
                return row.tag === value;
            },

            handleEdit(index, row) {
                this.idx = index;
                let that = this;
                this.$axios.get(this.PointUrl + '/' + row.id + '/detail').then((res) => {
                    if (res.data.resultCode === 200) {
                        that.ruleForm = res.data.data;
                    } else {
                        this.$message.error('编辑失败');
                        return false;
                    }
                })
                this.title = "编辑";
                this.edit = true;
                this.add = false;
                this.visible = true;
                this.couponFlag = true;
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
