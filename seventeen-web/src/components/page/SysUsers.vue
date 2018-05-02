<template>
    <div class="table" v-loading.body="loading">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i> 系统管理</el-breadcrumb-item>
                <el-breadcrumb-item>用户管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="handle-box">
            <el-button type="primary" icon="edit" @click="handleAdd">新增</el-button>
            <el-button type="primary" icon="delete" @click="delAll">批量删除</el-button>
        </div>
        <el-table :data="tableData" border style="width: 100%" ref="multipleTable"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50"/>
            <el-table-column prop="id" label="ID" width="220"/>
            <el-table-column prop="username" label="用户名" width="150"/>
            <el-table-column prop="description" label="描述"/>
            <el-table-column prop="createDate" label="创建时间" width="170" sortable/>
            <el-table-column prop="modifyDate" label="修改时间" width="170" sortable/>
            <el-table-column label="操作" width="150">
                <template slot-scope="scope">
                    <el-button size="small"
                               @click="handleEdit(scope.$index, scope.row)">编辑
                    </el-button>
                    <el-button size="small" type="danger"
                               @click="handleDelete(scope.$index, scope.row)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagination">
            <el-pagination
                @current-change="handleCurrentChange"
                layout="prev, pager, next, jumper"
                :total="total">
            </el-pagination>
        </div>

        <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" @close="handleClose()">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
                <el-form-item label="用户名" prop="username" :label-width="formLabelWidth">
                    <el-input v-model="ruleForm.username" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password" :label-width="formLabelWidth">
                    <el-input type="password" v-model="ruleForm.password" placeholder="修改时留空则不修改密码"
                              auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description" :label-width="formLabelWidth">
                    <el-input type="textarea" v-model="ruleForm.description" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="角色" :label-width="formLabelWidth">
                    <el-checkbox-group v-model="ruleForm.roleIds">
                        <el-checkbox v-for="role in roles" :label="role.id" :key="role.id">
                            {{role.code}}
                        </el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" :plain="true" @click="submitForm('ruleForm')">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                userUrl: this.$global.baseUrl + "/sys/users",
                roleUrl: this.$global.baseUrl + "/sys/roles",
                userRoleUrl: this.$global.baseUrl + "/sys/roles/user",
                tableData: [],//表格数据
                roles: [],//所有的角色数据
                curPage: 1,
                multipleSelection: [],
                total: 0,
                dialogTitle: "",
                dialogFormVisible: false,
                formLabelWidth: "80px",
                ruleForm: {username: null, description: null, password: null, roleIds: []},
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'}
                    ]
                },
                loading: false
            }
        },
        created(){
            this.getData();
        },
        computed: {},
        methods: {
            handleCurrentChange(val){
                this.curPage = val;
                this.getData(val);
            },
            getData(page){
                this.$axios.get(this.userUrl, {params: {"pageNum": page}}).then((res) => {
                    if (res.data.status == 200) {
                        this.tableData = res.data.data;
                        this.total = res.data.pageInfo.total;
                        this.curPage = res.data.pageInfo.pageNum;
                    }
                })
            },
            getRoles(){//获取所有角色数据
                this.$axios.get(this.roleUrl + "/all", {}).then((res) => {
                    if (res.data.status == 200) {
                        this.roles = res.data.data;
                    }
                })
            },
            getUserRoleIds(userId){//获取某个用户下所有的角色数据
                this.$axios.get(this.userRoleUrl + "/" + userId, {}).then((res) => {
                    if (res.data.status == 200) {
                        for (let role of res.data.data) {
                            this.ruleForm.roleIds.push(role.id);
                        }
                    }
                })
            },
            handleAdd(){
                this.rules = {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'}
                    ]
                };

                this.dialogTitle = "新增用户";
                this.getRoles();
                this.dialogFormVisible = true;
            },
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        if (this.currentId) {//存在id就是修改
                            this.$axios.put(this.userUrl + "/" + this.currentId, this.ruleForm).then((response) => {
                                if (response.data.status == 200) {
                                    this.$message.success(response.data.data)
                                    this.dialogFormVisible = false;
                                    this.getData(this.curPage);
                                } else if (response.data.error == 500001) {
                                    this.$message.error("修改失败，用户名不可重复")
                                } else {
                                    this.$message.error("修改失败，" + response.data.message)
                                }
                                this.loading = false;
                            }).catch((error) => {
                                this.loading = false;
                            });
                        } else {
                            this.$axios.post(this.userUrl, this.ruleForm).then((response) => {
                                if (response.data.status == 200) {
                                    this.$message.success(response.data.data)
                                    this.dialogFormVisible = false;
                                    this.getData(this.curPage);
                                } else if (response.data.error == 500001) {
                                    this.$message.error("新增失败，用户名不可重复")
                                } else {
                                    this.$message.error("新增失败，" + response.data.message)
                                }
                                this.loading = false;
                            }).catch((error) => {
                                this.loading = false;
                            });
                        }
                    } else {
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            handleEdit(index, row) {
                this.rules = {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ]
                };
                this.currentId = row.id;
                this.ruleForm.username = row.username;
                this.ruleForm.description = row.description;
                this.dialogTitle = "修改用户";
                this.getRoles();
                this.getUserRoleIds(this.currentId);
                this.dialogFormVisible = true;
            },
            handleDelete(index, row) {
                this.$confirm('删除id【' + row.id + '】的数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.delete(this.userUrl + "/" + row.id, {}).then((response) => {
                        if (response.data.status == 200) {
                            this.$message.success(response.data.data);
                            this.getData(this.curPage);
                        } else {
                            this.$message.error("删除失败，" + response.data.message)
                        }
                        this.loading = false;
                    }).catch((error) => {
                        this.loading = false;
                    });
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            handleClose(){//关闭弹窗时，清空数据
                this.currentId = null;
                this.ruleForm = {username: null, password: null, description: null, roleIds: []};
            },
            delAll(){
                const length = this.multipleSelection.length;
                if (length <= 0) {
                    this.$message.warning("至少选择一条数据");
                    return false;
                }
                this.$confirm('删除' + length + '条数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let ids = '';
                    for (let i = 0; i < length; i++) {
                        if (i < length - 1) {
                            ids += this.multipleSelection[i].id + ',';
                        } else {
                            ids += this.multipleSelection[i].id;
                        }
                    }
                    this.$axios.delete(this.userUrl, {params: {userIds: ids}}).then((response) => {
                        if (response.data.status == 200) {
                            this.$message.success(response.data.data);
                            this.multipleSelection = [];
                            this.getData(this.curPage);
                        } else {
                            this.$message.error("删除失败，" + response.data.message)
                        }
                        this.loading = false;
                    }).catch((error) => {
                        this.loading = false;
                    });
                })
            }
        }
    }
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }
</style>
