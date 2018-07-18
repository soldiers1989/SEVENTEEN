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
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                <el-button type="primary"  icon="el-icon-plus" @click="handleAdd">新建</el-button>
            </div>
            <el-table :data="tableData" border style="width: 100%" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="id" label="ID" sortable width="201">
                </el-table-column>
                <el-table-column prop="name" label="店名" >
                </el-table-column>
                <el-table-column prop="address" label="地址" >
                </el-table-column>
                <el-table-column prop="phone" label="联系电话">
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间">
                </el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button size="small" @click="handleEdit(scope.id, scope.row)">编辑</el-button>
                        <el-button size="small" type="danger" @click="handleDelete(scope.id, scope.row)">删除
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

        <!-- 新建弹出框 -->
        <el-dialog :title="title" :visible.sync="visible" width="55%" :before-close="handleClose">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="店名" prop="name">
                    <el-input v-model="ruleForm.name" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="地址" prop="address">
                    <el-input v-model="ruleForm.address" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="联系电话" prop="phone">
                    <el-input v-model="ruleForm.phone" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="负责人" prop="leader">
                    <el-input v-model="ruleForm.leader" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="经度" prop="longitude">
                    <el-input v-model="ruleForm.longitude" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="纬度" prop="latitude">
                    <el-input v-model="ruleForm.latitude" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="wifi密码" prop="wifi">
                    <el-input v-model="ruleForm.wifi" class="handle-input"></el-input>
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

    export default {
        data() {
            return {
                ShopUrl: this.$global.baseUrl + "/shop",
                tableData: [],
                curPage: 1,
                total: 0,
                title: '',
                multipleSelection: [],
                select_word: '',
                del_list: [],
                is_search: false,
                visible: false,
                delVisible: false,
                add: false,
                edit: false,
                ids: '',
                ruleForm: {
                    name: '',
                    address: '',
                    phone:'',
                    leader: '',
                    longitude: '',
                    latitude: '',
                    wifi: ''
                },
                rules: {
                    name: [
                        {required: true, message: '请输入店名', trigger: 'blur'},
                        {min: 1, max: 100, message: '长度在 1 以上的字符', trigger: 'blur'}
                    ],
                    address: [
                        {required: true, message: '请输入详细地址', trigger: 'blur'},
                        {min: 1, max: 100, message: '长度在 1 以上的字符', trigger: 'blur'}
                    ],
                    phone: [
                        {required: true, message: '请输入联系号码', trigger: 'blur'},
                        {min: 1, max: 16, message: '长度在 1 以上的数字', trigger: 'blur'}
                    ],
                    leader: [
                        {required: true, message: '请输入联系号码', trigger: 'blur'},
                        {min: 1, max: 50, message: '长度在 1 以上的数字', trigger: 'blur'}
                    ],
                    longitude: [
                        {required: true, message: '请输入联系号码', trigger: 'blur'},
                        {min: 1, max: 16, message: '长度在 1 以上的数字', trigger: 'blur'}
                    ],
                    latitude: [
                        {required: true, message: '请输入联系号码', trigger: 'blur'},
                        {min: 1, max: 16, message: '长度在 1 以上的数字', trigger: 'blur'}
                    ],
                    wifi: [
                        {required: true, message: '请输入门店WiFi密码', trigger: 'blur'},
                        {min: 1, max: 15, message: '长度在 1 以上的字符', trigger: 'blur'}
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
                            this.$axios.post(this.ShopUrl, this.ruleForm).then((res) => {
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
                            this.$axios.post(this.ShopUrl + "/update", this.ruleForm).then((res) => {
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
                }
                this.$axios.get(this.ShopUrl, {params: param}).then((res) => {
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
            },

            handleEdit(index, row) {
                this.idx = index;
                let that = this;
                this.$axios.get(this.ShopUrl + '/' + row.id + '/detail').then((res) => {
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
                this.$axios.delete(this.ShopUrl, {params: param}).then((res) => {
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
