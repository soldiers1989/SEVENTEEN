<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>价格管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="el-icon-plus" @click="handlePriceTypeAdd">新建</el-button>
            </div>

            <el-table :data="typePriceData" border style="width: 100%" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column prop="name" label="类型" >
                </el-table-column>
                <el-table-column prop="remark" label="订金">
                </el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button size="small" @click="handleEdit(scope.id, scope.row)">编辑</el-button>
                        <el-button size="small" type="danger" @click="handlePriceDelete(scope.id, scope.row)">删除</el-button>
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- 新增房间价格类型 -->
        <el-dialog title="新增房间价格类型" :visible.sync="priceVisible" width="55%" :before-close="handleClose">
            <el-form :model="rulePriceForm" :rules="rulesPrice" ref="rulePriceForm" label-width="100px"
                     class="demo-ruleForm">
                <el-form-item label="名称" prop="name">
                    <el-input v-model="rulePriceForm.name" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="订金" prop="remark">
                    <el-radio-group v-model="rulePriceForm.remark">
                        <el-radio label="支持退订">支持退订</el-radio>
                        <el-radio label="不支持退订">不支持退订</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleAddPriceTag('rulePriceForm')">保存</el-button>
                    <el-button @click="resetForm('rulePriceForm');priceVisible=false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 删除提示框 -->
        <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
            <div class="del-dialog-cnt">删除不可恢复，是否确定删除？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="delVisible = false">取 消</el-button>
                <el-button type="primary" @click="deletePriceType">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    var qs = require('qs');

    export default {
        data() {
            return {
                RoomUrl: this.$global.baseUrl + "/room",
                typePriceData: [],
                curPage: 1,
                total: 0,
                priceVisible:false,
                delVisible:false,
                multipleSelection: [],
                is_search: false,
                delVisible: false,
                edit: false,
                add: false,
                typeIds: '',
                idx: -1,
                rulePriceForm: {
                    name: '',
                    remark: ''
                },
                rulesPrice: {
                    name: [
                        {required: true, message: '请输入名称', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur'}
                    ],
                    remark: [
                        {required: true, message: '请选择订金类型', trigger: 'change'}
                    ],
                },
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
            handlePriceTypeAdd() {
                this.priceVisible = true;
            },
            handleClose(done) {
                const that = this;
                that.resetForm('rulePriceForm');
                done();
            },

            getData(page) {
                this.priceTypeFlag = true;
                this.add = false;
                this.edit = false;

                const param = {
                    "type": 't'
                }
                this.$axios.get(this.RoomUrl + '/tags', {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.typePriceData = res.data.data;
                    }
                })
            },
            handleAddPriceTag(formName) {
                let that = this;

                if (this.edit) {
                    this.$refs[formName].validate((valid) => {
                        if (valid) {
                            const param = {
                                "id":this.rulePriceForm.id,
                                "type": 't',
                                "name": this.rulePriceForm.name,
                                "remark": this.rulePriceForm.remark
                            }
                            this.$axios.post(this.RoomUrl + '/tags/update', param).then((res) => {
                                if (res.data.resultCode == 200) {
                                    this.typeData = res.data.data;
                                    this.$message.success(`保存成功`);
                                    this.getData();
                                } else {
//                                    this.$message.error(res.data.message);
                                    return false;
                                }
                            });
                            that.resetForm('rulePriceForm');
                            this.priceVisible = false;
                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    });
                    this.edit = false;
                } else {
                    this.$refs[formName].validate((valid) => {
                        if (valid) {
                            const param = {
                                "type": 't',
                                "name": this.rulePriceForm.name,
                                "remark": this.rulePriceForm.remark
                            }
                            this.$axios.post(this.RoomUrl + '/tags', param).then((res) => {
                                if (res.data.resultCode == 200) {
                                    this.typeData = res.data.data;
                                    this.$message.success(`新建成功`);
                                    this.getData();
                                } else {
                                    this.$message.error(res.data.message);
                                    return false;
                                }
                            });
                            that.resetForm('rulePriceForm');
                            this.priceVisible = false;
                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    });
                }

            },
            handlePriceDelete(index, row) {
                if (!this.typeIds) {
                    this.typeIds = row.id
                }
                this.delVisible = true;
            },
            // 确定删除
            deletePriceType() {
                let url = this.RoomUrl + "/tags";
                let param = {"ids": this.typeIds};
                this.$axios.delete(url, {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.getData()
                        this.typeIds = '';
                        this.$message.success('删除成功');
                    } else {
                        this.$message.error('删除失败');
                    }
                })
                this.delVisible = false;
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
            handleEdit(index, row) {
                this.idx = index;
                let that = this;

                this.$axios.get(this.RoomUrl + '/tag?id='+row.id).then((res) => {
                    if (res.data.resultCode === 200) {
                        that.rulePriceForm = res.data.data;

                    } else {
                        this.$message.error('查看失败');
                        return false;
                    }
                })
                this.edit = true;
                this.priceVisible = true;
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
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
