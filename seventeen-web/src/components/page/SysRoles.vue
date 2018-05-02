<template>
    <div class="table" v-loading.body="loading">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i> 系统管理</el-breadcrumb-item>
                <el-breadcrumb-item>角色管理</el-breadcrumb-item>
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
            <el-table-column prop="code" label="角色编码" width="150"/>
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
                <el-form-item label="角色编码" prop="code" :label-width="formLabelWidth">
                    <el-input v-model="ruleForm.code" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description" :label-width="formLabelWidth">
                    <el-input type="textarea" v-model="ruleForm.description" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="权限" :label-width="formLabelWidth">
                    <el-checkbox-group v-model="ruleForm.authorityIds">
                        <el-checkbox v-for="authority in authorities" :label="authority.id" :key="authority.id">
                            {{authority.code}}
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
                roleUrl: this.$global.baseUrl + "/sys/roles",
                authorityUrl: this.$global.baseUrl + "/sys/authorities",
                roleAuthorityUrl: this.$global.baseUrl + "/sys/authorities/role",
                tableData: [],//表格数据
                authorities: [],//所有的权限数据
                curPage: 1,
                multipleSelection: [],
                total: 0,
                dialogTitle: "",
                dialogFormVisible: false,
                formLabelWidth: "80px",
                ruleForm: {code: "", description: "", authorityIds: []},
                rules: {
                    code: [
                        {required: true, message: '请输入角色编码', trigger: 'blur'}
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
                this.$axios.get(this.roleUrl, {params: {"pageNum": page}}).then((res) => {
                    if (res.data.status == 200) {
                        this.tableData = res.data.data;
                        this.total = res.data.pageInfo.total;
                        this.curPage = res.data.pageInfo.pageNum;
                    }
                })
            },
            getAuthorities(){//获取所有权限数据
                this.$axios.get(this.authorityUrl + "/all", {}).then((res) => {
                    if (res.data.status == 200) {
                        this.authorities = res.data.data;
                    }
                })
            },
            getRoleAuthorityIds(roleId){//获取某个角色下所有的权限数据
                this.$axios.get(this.roleAuthorityUrl + "/" + roleId, {}).then((res) => {
                    if (res.data.status == 200) {
                        for (let authority of res.data.data) {
                            this.ruleForm.authorityIds.push(authority.id);
                        }
                    }
                })
            },
            handleAdd(){
                this.dialogTitle = "新增角色";
                this.getAuthorities();
                this.dialogFormVisible = true;
            },
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        if (this.currentId) {//存在id就是修改
                            this.$axios.put(this.roleUrl + "/" + this.currentId, {
                                "code": this.ruleForm.code,
                                "description": this.ruleForm.description,
                                "authorityIds": this.ruleForm.authorityIds
                            }).then((response) => {
                                if (response.data.status == 200) {
                                    this.$message.success(response.data.data)
                                    this.dialogFormVisible = false;
                                    this.getData(this.curPage);
                                } else if (response.data.error == 500001) {
                                    this.$message.error("修改失败，角色编码不可重复")
                                } else {
                                    this.$message.error("修改失败，" + response.data.message)
                                }
                                this.loading = false;
                            }).catch((error) => {
                                this.loading = false;
                            });
                        } else {
                            this.$axios.post(this.roleUrl, {
                                "code": this.ruleForm.code,
                                "description": this.ruleForm.description,
                                "authorityIds": this.ruleForm.authorityIds
                            }).then((response) => {
                                if (response.data.status == 200) {
                                    this.$message.success(response.data.data)
                                    this.dialogFormVisible = false;
                                    this.getData(this.curPage);
                                } else if (response.data.error == 500001) {
                                    this.$message.error("新增失败，角色编码不可重复")
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
                this.currentId = row.id;
                this.ruleForm.code = row.code;
                this.ruleForm.description = row.description;
                this.dialogTitle = "修改角色";
                this.getAuthorities();
                this.getRoleAuthorityIds(this.currentId);
                this.dialogFormVisible = true;
            },
            handleDelete(index, row) {
                this.$confirm('删除id【' + row.id + '】的数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.delete(this.roleUrl + "/" + row.id, {}).then((response) => {
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
                this.ruleForm = {code: "", description: "", authorityIds: []};
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
                    this.$axios.delete(this.roleUrl, {params: {roleIds: ids}}).then((response) => {
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
