<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>房间状态</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                房间状态：
                <el-select v-model="select_cate" clearable placeholder="请选择" style="width: 100px;padding-right: 20px">
                    <el-option key="0" label="全部" value=""></el-option>
                    <el-option key="1" label="已出租" value="1"></el-option>
                    <el-option key="2" label="空房" value="2"></el-option>
                </el-select>
                房型：
                <el-select v-model="select_cate" placeholder="房间状态" style="width: 150px;padding-right: 20px" class="handle-select mr10">
                    <el-option key="0" label="全部" value=""></el-option>
                    <el-option key="1" label="大床房" value="1"></el-option>
                    <el-option key="2" label="单间" value="2"></el-option>
                </el-select>
                楼层：
                <el-select v-model="select_cate" placeholder="房间状态" style="width: 100px;padding-right: 20px" class="handle-select mr10">
                    <el-option key="0" label="全部" value=""></el-option>
                    <el-option key="1" label="1" value="1"></el-option>
                    <el-option key="2" label="2" value="2"></el-option>
                </el-select>

                房号：
                <el-input v-model="select_word" style="width: 100px" class="handle-input mr10"></el-input>

                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>

            </div>
            <el-tag type="success">空房</el-tag>
            <el-tag>待入住</el-tag>
            <el-tag type="danger">入住中</el-tag>
            <el-tag type="warning">已离店</el-tag>
            <el-tag type="info">维护中</el-tag>

            <div class="container">
                <div>
                    <text>
                        1楼
                    </text>
                    <div>
                        <text>
                            1001
                        </text>
                        <text>
                            豪华单间
                        </text>
                        <text>
                            李先生
                        </text>
                    </div>
                </div>
            </div>

            <div class="pagination">
                <el-pagination
                    @current-change="handleCurrentChange"
                    layout="total,sizes,prev,pager,next,jumper"
                    :total="total" :current-page="curPage">
                </el-pagination>
            </div>
        </div>

        <!-- 查看 -->
        <el-dialog title="订单详情" :visible.sync="visible" width="55%">
            <el-form :model="ruleForm"  ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="订单号">
                    <el-input :disabled="orderDetail" v-model="ruleForm.id" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="门店" >
                    <el-input v-model="ruleForm.shopName"></el-input>
                </el-form-item>
                <el-form-item label="房号" >
                    <el-input v-model="ruleForm.apNum"></el-input>
                </el-form-item>
                <el-form-item label="下订用户" >
                    <el-input v-model="ruleForm.userName"></el-input>
                </el-form-item>
                </el-form-item>
                <el-form-item label="订单状态" >
                    <el-input v-model="ruleForm.status"></el-input>
                </el-form-item>
                <el-form-item label="备注" >
                    <el-input v-model="ruleForm.remark"></el-input>
                </el-form-item>
                <el-form-item label="入住时间" >
                    <el-input v-model="ruleForm.liveTime"></el-input>
                </el-form-item>
                <el-table
                    :data="userData"
                    style="width: 100%">
                    <el-table-column
                        prop="liver"
                        label="姓名"
                        width="180">
                    </el-table-column>
                    <el-table-column
                        prop="idCard"
                        label="身份证"
                        width="180">
                    </el-table-column>
                    <el-table-column
                        prop="phone"
                        label="手机号码">
                    </el-table-column>
                </el-table>
                <el-form-item>
                    <el-button v-if="edit" @click="resetForm('ruleForm');visible=false">关闭</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 删除提示框 -->
        <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
            <div class="del-dialog-cnt">删除不可恢复，是否确定删除？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="delVisible = false">取 消</el-button>
                <el-button type="primary" @click="deleteRow">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    var qs = require('qs');

    export default {
        data() {
            return {
                OrderUrl: this.$global.baseUrl + "/order",
                tableData: [],
                userData: [],
                curPage: 1,
                total: 0,
                orderDetail:true,
                multipleSelection: [],
                select_cate: '',
                select_word: '',
                startTime:'',
                endTime:'',
                del_list: [],
                is_search: false,
                visible: false,
                delVisible: false,
                edit: false,
                ids: '',
                idx: -1,
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
                    "pageNum": this.CurPage,
                    "remark": this.select_word === null ? "" : this.select_word.trim(),
                    "status": this.select_cate === null ? "" : this.select_cate.trim(),
                    "startTime": this.startTime === null ? "" : this.startTime.trim(),
                    "endTime": this.endTime === null ? "" : this.endTime.trim()
                }
                this.$axios.get(this.OrderUrl, {params: param}).then((res) => {
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
            },
            formatter(row, column) {
                return row.address;
            },
            filterTag(value, row) {
                return row.tag === value;
            },
            handleCancel(index, row) {
                this.idx = index;
                let that = this;
                this.$axios.put(this.OrderUrl + '/cancel?order='+row.id).then((res) => {
                    if (res.data.resultCode == 200) {
                        that.goods = res.data.data;
                    }
                })
            },
            handleEdit(index, row) {
                this.idx = index;
                let that = this;
                const param = {
                    "type": 'g'
                }
                this.$axios.get(this.OrderUrl + '/' + row.id + '/detail').then((res) => {
                    if (res.data.resultCode === 200) {
                        that.ruleForm = res.data.data;
                        this.userData = res.data.data.livers;

                    } else {
                        this.$message.error('查看失败');
                        return false;
                    }
                })
                this.visible = true;
            },
            handleDelete(index, row) {
                if (!this.ids) {
                    this.ids = row.id
                }
                this.delVisible = true;
            },
            delAll() {
                const length = this.multipleSelection.length;
                let ids = '';
                let str = '';
                this.del_list = this.del_list.concat(this.multipleSelection);
                for (let i = 0; i < length; i++) {
                    ids += this.multipleSelection[i].id + ',';
                }
                this.ids = ids
                this.handleDelete()
                this.multipleSelection = [];
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            // 确定删除
            deleteRow() {
                const param = {
                    "ids": this.ids
                }
                this.$axios.delete(this.OrderUrl, {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.getData()
                        this.$message.success('删除成功');
                    } else {
                        this.$message.error('删除失败');
                    }
                })
                this.ids = '';
                this.delVisible = false;
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
