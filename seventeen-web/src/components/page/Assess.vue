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
                    <el-date-picker type="date" placeholder="订单开始日期" v-model="startTime"
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
                <el-select v-model="select_cate" placeholder="房间状态" class="handle-select mr10">
                    <el-option key="0" label="全部" value=""></el-option>
                    <el-option key="1" label="已回复" value="1"></el-option>
                    <el-option key="2" label="未回复" value="0"></el-option>
                </el-select>
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>

                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>

            </div>

            <el-table :data="tableData" border style="width: 100%" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="40"></el-table-column>
                <el-table-column prop="orderId" label="订单号" sortable width="100">
                </el-table-column>
                <el-table-column prop="userName" label="用户" width="150">
                </el-table-column>
                <el-table-column prop="shopName" label="门店" >
                </el-table-column>
                <el-table-column prop="apNum" label="房号">
                </el-table-column>
                <el-table-column prop="roomType" label="房间类型">
                </el-table-column>
                <el-table-column prop="totalPoint" label="总评分">
                </el-table-column>
                <el-table-column prop="isCheck" label="回复状态">
                </el-table-column>
                <el-table-column prop="orderTime" label="订单时间">
                </el-table-column>
                <el-table-column prop="assessTime" label="评价时间">
                </el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button size="small" @click="handleEdit(scope.id, scope.row)">查看</el-button>
                        <el-button size="small"type="danger" @click="handleDelete(scope.id, scope.row)">删除</el-button>
                        </el-button>
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

        <!-- 查看 -->
        <el-dialog title="查看评价" :visible.sync="visible" width="40%">
            <el-form :model="ruleForm"  ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="订单号">
                    <el-input  :disabled="true" v-model="ruleForm.orderId" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="用户" >
                    <el-input :disabled="true" v-model="ruleForm.userName" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="门店" >
                    <el-input :disabled="true" v-model="ruleForm.shopName" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="房号" >
                    <el-input :disabled="true" v-model="ruleForm.apNum" class="handle-input"></el-input>
                </el-form-item>
                </el-form-item>
                <el-form-item label="订单时间" >
                    <el-input :disabled="true" v-model="ruleForm.orderTime" class="handle-input"></el-input>
                </el-form-item>
                <el-table
                    :data="ruleForm.seAssessPoints"
                    style="width:80%;margin-left: 50px;">
                    <el-table-column
                        prop="name"
                        label="项目"
                        width="180">
                    </el-table-column>
                    <el-table-column
                        prop="point"
                        label="评分"
                        sortable
                        width="180">
                    </el-table-column>
                </el-table>
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span>评价内容</span>
                        <span style="float: right; padding: 3px 0" type="text">评价总分：{{ruleForm.totalPoint}}分</span>
                    </div>
                    <div v-for="o in ruleForm.seAssessContents"  class="text item">
                        {{o.createBy +'：'+ o.content }}
                    </div>
                    <el-input placeholder="请输入回复内容" v-model="input" style="margin-top: 15px;">
                        <el-button slot="append"  @click="addAssess()" icon="el-icon-plus"></el-button>
                    </el-input>
                </el-card>
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
                AssessUrl: this.$global.baseUrl + "/assess",
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
                input:'',
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
                    "isCheck": this.select_cate === null ? "" : this.select_cate.trim(),
                    "startTime": this.startTime === null ? "" : this.startTime.trim(),
                    "endTime": this.endTime === null ? "" : this.endTime.trim()
                }
                this.$axios.get(this.AssessUrl, {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.tableData = res.data.data;
                        this.total = res.data.pageInfo.total;
                        this.curPage = res.data.pageInfo.pageNum;
                    }
                })
            },
            addAssess(){

                const param = {
                    "assessId": this.ruleForm.contentId === null ? "" : this.ruleForm.contentId.trim(),
                    "content": this.input === null ? "" : this.input.trim()
                }
                this.$axios.post(this.AssessUrl, param).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.ruleForm.seAssessContents = res.data.data;
                        this.input = '';
                        this.getData();
                    }else if(res.data.resultCode == 500){
                        this.$message.error('暂无评论');
                    }else {
                        this.$message.error('回复失败');
                        return false;
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
                const param = {
                    "type": 'g'
                }
                this.$axios.get(this.OrderUrl + '/tags', {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        that.goods = res.data.data;
                    }
                })

                this.$axios.get(this.OrderUrl + '/' + row.id + '/detail').then((res) => {
                    if (res.data.resultCode === 200) {
                        that.ruleForm = res.data.data.seApartment;
                        that.fileList = res.data.data.seApartmentImg;
                    } else {
                        this.$message.error('编辑失败');
                        return false;
                    }
                })
                this.visible = true;
            },
            handleEdit(index, row) {
                this.idx = index;
                let that = this;
                this.$axios.get(this.AssessUrl + '/' + row.assessId + '/detail').then((res) => {
                    if (res.data.resultCode === 200) {
                        that.ruleForm = res.data.data;
                    } else {
                        this.$message.error('查看失败');
                        return false;
                    }
                })
                this.visible = true;
            },
            handleDelete(index, row) {
                if (!this.ids) {
                    this.ids = row.assessId;
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
                this.$axios.delete(this.AssessUrl, {params: param}).then((res) => {
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
