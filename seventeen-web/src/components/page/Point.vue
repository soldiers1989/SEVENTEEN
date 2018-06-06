<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>积分管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                <el-select v-model="select_cate" placeholder="房间状态" class="handle-select mr10">
                    <el-option key="0" label="全部" value=""></el-option>
                    <el-option key="1" label="可用" value="1"></el-option>
                    <el-option key="2" label="已过期" value="2"></el-option>
                </el-select>
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                <el-button type="primary"  icon="el-icon-plus" @click="handleAdd">新建积分</el-button>
                <el-button type="primary"  icon="el-icon-search" @click="handleAdd">使用记录</el-button>
            </div>
            <el-table :data="tableData" border style="width: 100%" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="id" label="兑换号" sortable width="201">
                </el-table-column>
                <el-table-column prop="name" label="优惠券名称" width="100">
                </el-table-column>
                <el-table-column prop="status" label="状态" width="70">
                </el-table-column>
                <el-table-column prop="startTime" label="起始时间">
                </el-table-column>
                <el-table-column prop="endTime" label="结束时间">
                </el-table-column>
                <el-table-column prop="sendType" label="发放模式">
                </el-table-column>
                <el-table-column prop="maxPrice" label="金额使用上限">
                </el-table-column>
                <el-table-column prop="price" label="优惠金额" width="80">
                </el-table-column>

                <!--<el-table-column label="操作" width="180">-->
                    <!--<template slot-scope="scope">-->
                        <!--<el-button size="small" @click="handleEdit(scope.id, scope.row)">编辑</el-button>-->
                        <!--<el-button size="small" type="danger" @click="handleDelete(scope.id, scope.row)">删除-->
                        <!--</el-button>-->
                    <!--</template>-->
                <!--</el-table-column>-->
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
        <el-dialog :title="title" :visible.sync="visible" width="55%" :before-close="handleClose">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="优惠券名称" prop="name">
                    <el-input v-model="ruleForm.name" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="发放模式" prop="sendTypeArr">
                    <el-checkbox-group v-model="ruleForm.sendTypeArr">
                        <el-checkbox label="新用户"></el-checkbox>
                        <el-checkbox label="分享"></el-checkbox>
                        <el-checkbox label="普通"></el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="有效时间" required>

                    <el-col :span="7">
                        <el-form-item prop="startTime">
                            <el-date-picker type="date" placeholder="选择日期" v-model="ruleForm.startTime"
                                            style="width: 100%;" value-format="yyyy-MM-dd"></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col class="line" :span="1">至</el-col>

                    <el-col :span="7">
                        <el-form-item prop="endTime">
                            <el-date-picker
                                v-model="ruleForm.endTime"
                                type="date"
                                placeholder="选择日期"
                                value-format="yyyy-MM-dd">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-form-item>
                <el-form-item label="优惠金额" prop="price">
                    <el-input v-model="ruleForm.price" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="金额上限" prop="maxPrice">
                    <el-input placeholder="满多少立减" v-model="ruleForm.maxPrice" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="ruleForm.remark"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
                    <el-button v-if="add" @click="resetForm('ruleForm')">重置</el-button>
                    <el-button v-if="edit" @click="resetForm('ruleForm');visible=false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>


        <!-- 优惠券使用记录 -->
        <el-dialog :title="优惠券使用记录" :visible.sync="couponLog" width="80%" >
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="优惠券名称" prop="name">
                    <el-input v-model="ruleForm.name" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="发放模式" prop="sendTypeArr">
                    <el-checkbox-group v-model="ruleForm.sendTypeArr">
                        <el-checkbox label="新用户"></el-checkbox>
                        <el-checkbox label="分享"></el-checkbox>
                        <el-checkbox label="普通"></el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="有效时间" required>

                    <el-col :span="7">
                        <el-form-item prop="startTime">
                            <el-date-picker type="date" placeholder="选择日期" v-model="ruleForm.startTime"
                                            style="width: 100%;" value-format="yyyy-MM-dd"></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col class="line" :span="1">至</el-col>

                    <el-col :span="7">
                        <el-form-item prop="endTime">
                            <el-date-picker
                                v-model="ruleForm.endTime"
                                type="date"
                                placeholder="选择日期"
                                value-format="yyyy-MM-dd">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-form-item>
                <el-form-item label="优惠金额" prop="price">
                    <el-input v-model="ruleForm.price" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="金额上限" prop="maxPrice">
                    <el-input placeholder="满多少立减" v-model="ruleForm.maxPrice" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="ruleForm.remark"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
                    <el-button v-if="add" @click="resetForm('ruleForm')">重置</el-button>
                    <el-button v-if="edit" @click="resetForm('ruleForm');visible=false">取消</el-button>
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
                CouponUrl: this.$global.baseUrl + "/coupon",
                tableData: [],
                curPage: 1,
                total: 0,
                title: '',
                multipleSelection: [],
                select_cate: '',
                select_word: '',
                del_list: [],
                is_search: false,
                visible: false,
                delVisible: false,
                add: false,
                edit: false,
                ids: '',
                couponFlag: false,
                ruleForm: {
                    name: '',
                    price: '',
                    maxPrice: '',
                    sendTypeArr: [],
                    remark: '',
                    startTime: '',
                    endTime: '',
                },
                rules: {
                    name: [
                        {required: true, message: '优惠券名称', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur'}
                    ],
                    price: [
                        {required: true, message: '请输入优惠金额', trigger: 'blur'},
                        {min: 1, max: 10, message: '长度在 1 到 10 个位数字', trigger: 'blur'}
                    ],
                    maxPrice: [
                        {required: true, message: '请输入立减金额', trigger: 'blur'},
                        {min: 0, max: 10, message: '长度在 0 到 10 个位数字', trigger: 'blur'}
                    ],
                    sendTypeArr: [
                        {type: 'array', required: true, message: '请至少选择一个优惠券方法模式', trigger: 'change'}
                    ],
                    startTime: [
                        {required: true, message: '请选择开始日期', trigger: 'blur'},
                    ],
                    endTime: [
                        {required: true, message: '请选择结束时间', trigger: 'blur'},
                    ],
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
            submitForm(formName) {
                let that = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {

                        if(that.ruleForm.startTime>that.ruleForm.endTime){
                            this.$message.error('结束时间不能大于开始时间');
                            return false;
                        }

                        if (that.add) {
                            this.$axios.post(this.CouponUrl, this.ruleForm).then((res) => {
                                if (res.data.resultCode === 200) {
                                    this.$message.success('创建成功');
                                    that.resetForm('ruleForm');
                                    this.getData();
                                    this.visible = false;
                                } else {
                                    this.$message.error('创建失败');
                                    return false;
                                }
                            })
                        } else {
                            this.$axios.post(this.CouponUrl + "/update", this.ruleForm).then((res) => {
                                if (res.data.resultCode === 200) {
                                    this.$message.success('更新成功');
                                    this.getData();
                                    that.resetForm('ruleForm');
                                    this.visible = false;
                                } else {
                                    this.$message.error('更新失败');
                                    return false;
                                }
                            })
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.handleAdd()
            },

            getData(page) {
                const param = {
                    "pageNum": this.curPage,
                    "remark": this.select_word == null ? "" : this.select_word.trim(),
                    "status": this.select_cate == null ? "" : this.select_cate.trim()
                }
                this.$axios.get(this.CouponUrl, {params: param}).then((res) => {
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
            formatter(row, column) {
                return row.address;
            },
            filterTag(value, row) {
                return row.tag === value;
            },
            handleAdd() {
                this.visible = true;
                this.title = "新建";
                this.add = true;
                this.edit = false;
                this.couponFlag = false;
            },

            handleEdit(index, row) {
                this.idx = index;
                let that = this;
                this.$axios.get(this.CouponUrl + '/' + row.id + '/detail').then((res) => {
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
            // 保存编辑
            saveEdit() {
                this.$set(this.tableData, this.idx);
                this.visible = false;
                this.$message.success(`修改第 ${this.idx + 1} 行成功`);
            },
            // 确定删除
            deleteRow() {
                const param = {
                    "ids": this.ids
                }
                this.$axios.delete(this.CouponUrl, {params: param}).then((res) => {
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
