<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i>房间列表</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                <el-select v-model="select_cate" placeholder="房间状态" class="handle-select mr10">
                    <el-option key="0" label="全部" value=""></el-option>
                    <el-option key="1" label="入住" value="1"></el-option>
                    <el-option key="2" label="空房" value="2"></el-option>
                    <el-option key="3" label="订单中" value="3"></el-option>
                    <el-option key="4" label="已下订" value="4"></el-option>
                    <el-option key="5" label="待退房" value="5"></el-option>
                </el-select>
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建</el-button>
            </div>
            <el-table :data="tableData" border style="width: 100%" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="apNum" label="房号" sortable width="100">
                </el-table-column>
                <el-table-column prop="name" label="房名" width="150">
                </el-table-column>
                <el-table-column prop="inTime" label="入住时间">
                </el-table-column>
                <el-table-column prop="outTIme" label="离房时间">
                </el-table-column>
                <el-table-column prop="status" label="状态" width="150">
                </el-table-column>
                <el-table-column prop="type" label="类型">
                </el-table-column>
                <el-table-column prop="price" label="价格">
                </el-table-column>
                <el-table-column prop="remark" label="备注">
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
        <el-dialog title="新建" :visible.sync="addVisible" width="55%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="房号" prop="apNum">
                    <el-input v-model="ruleForm.apNum" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="房名" prop="name">
                    <el-input v-model="ruleForm.name"></el-input>
                </el-form-item>
                <el-form-item label="类型" prop="type">
                    <el-radio-group v-model="ruleForm.type">
                        <el-radio label="1">短租</el-radio>
                        <el-radio label="2">长租</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="价格" prop="price">
                    <el-input v-model="ruleForm.price" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="楼层" prop="floor">
                    <el-input v-model="ruleForm.floor" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="面积" prop="area">
                    <el-input v-model="ruleForm.area" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="户型" prop="structure">
                    <el-input v-model="ruleForm.structure" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="房间物品" prop="goods">
                    <el-checkbox-group v-model="ruleForm.goodsCheck">
                        <el-checkbox v-for="good in ruleForm.goods" :label="good.id" :key="good.id">{{good.name}}
                        </el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="图片上传" prop="img">
                    <el-upload
                        class="upload-demo"
                        ref="upload"
                        :action="uploadUrl"
                        :on-preview="handlePreview"
                        :on-remove="handleRemove"
                        :file-list="fileList2"
                        :headers="headers"
                        :auto-upload="false"
                        multiple
                        list-type="picture">
                        <el-button size="small" type="primary">点击上传</el-button>
                        <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过50mb,默认第一张为主图，可点击图片修改</div>
                    </el-upload>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
                    <el-button @click="resetForm('ruleForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="40%">
            <el-form ref="form" :model="form" label-width="50px">
                <el-form-item label="日期">
                    <el-date-picker type="date" placeholder="选择日期" v-model="form.date" value-format="yyyy-MM-dd"
                                    style="width: 100%;"></el-date-picker>
                </el-form-item>
                <el-form-item label="姓名">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="地址">
                    <el-input v-model="form.address"></el-input>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
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
                ApartmentUrl: this.$global.baseUrl + "/room",
                uploadUrl: this.$global.baseUrl + "/file",
                tableData: [],
                fileList2: [],
                headers: {
                    Authorization: "Bearer " + sessionStorage.getItem("token"),
                    Room: this.roomId
                },
                curPage: 1,
                checkFlag: false,
                total: 0,
                roomId:'',
                multipleSelection: [],
                select_cate: '',
                select_word: '',
                del_list: [],
                is_search: false,
                editVisible: false,
                addVisible: false,
                delVisible: false,
                ids: '',
                ruleForm: {
                    apNum: '',
                    name: '',
                    price: '',
                    area: '',
                    floor: '',
                    structure: '',
                    type: '',
                    goods: [],
                    goodsCheck: [],
                },
                rules: {
                    apNum: [
                        {required: true, message: '请输入房号，如1001', trigger: 'blur'},
                        {min: 1, max: 7, message: '长度在 1 到 7 个字符', trigger: 'blur'},
                    ],
                    name: [
                        {required: true, message: '请输入房名', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur'}
                    ],
                    price: [
                        {required: true, message: '请输入房间价格', trigger: 'blur'},
                        {min: 1, max: 10, message: '长度在 0 到 10 个字符', trigger: 'blur'}
                    ],
                    area: [
                        {required: true, message: '请输入房间面积', trigger: 'blur'},
                        {min: 1, max: 10, message: '长度在 0 到 10 个字符', trigger: 'blur'}
                    ],
                    floor: [
                        {required: true, message: '请输入房间楼层', trigger: 'blur'},
                        {min: 1, max: 2, message: '长度在 1 到 2 个字符', trigger: 'blur'}
                    ],
                    structure: [
                        {required: true, message: '请输入房间户型', trigger: 'blur'},
                        {min: 1, max: 10, message: '长度在 0 到 10 个字符', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请选择房间类型', trigger: 'change'}
                    ],
                    goods: [
                        {type: 'array', required: true, message: '请至少选择一个房间物品', trigger: 'change'}
                    ],
                },
                form: {
                    name: '',
                    region: '',
                    date1: '',
                    date2: '',
                    delivery: false,
                    type: [],
                    resource: '',
                    desc: ''
                }
                ,
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
            handleRemove(file, fileList) {
                console.log(file, fileList);
            },
            handlePreview(file) {
                console.log(file);
            },
            submitForm(formName) {
                var that = this
                this.$axios.get(this.ApartmentUrl + '/' + this.ruleForm.apNum).then((res) => {
                    if (res.data.resultCode == 200) {
                        if (!res.data.data) {
                            this.$refs[formName].validate((valid) => {
                                if (valid) {
                                    this.$axios.post(this.ApartmentUrl, this.ruleForm).then((res) => {
                                        if (res.data.resultCode == 200) {
                                            this.tableData = res.data.data;
                                            this.roomId = res.data.data.id
                                            that.$refs.upload.submit();
                                            this.$message.success('创建成功');
                                        } else {
                                            this.$message.error('创建失败');
                                        }
                                    })
                                } else {
                                    console.log('error submit!!');
                                    return false;
                                }
                            });
                        } else {
                            this.$message.error('房号已存在，请更改或者删除已经存在房号！');
                        }
                    } else {
                        this.$message.error('创建失败');
                    }
                })
            },
            resetForm(formName) {
                this.ruleForm.goodsCheck = [];
                this.fileList2 = [];
                this.$refs[formName].resetFields();
                this.handleAdd()
            },

            getData(page) {
                const param = {
                    "pageNum": this.curPage,
                    "remark": this.select_word == null ? "" : this.select_word.trim(),
                    "status": this.select_cate == null ? "" : this.select_cate.trim()

                }
                this.$axios.get(this.ApartmentUrl, {params: param}).then((res) => {
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
                this.addVisible = true;
                const param = {
                    "type": 'g'
                }
                this.$axios.get(this.ApartmentUrl + '/tags', {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.ruleForm.goods = res.data.data;
                    }
                })
            },

            handleEdit(index, row) {
                this.idx = index;
                const item = this.tableData[index];
                this.form = {
                    name: item.name,
                    date: item.date,
                    address: item.address
                }
                this.editVisible = true;
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
                this.$set(this.tableData, this.idx, this.form);
                this.editVisible = false;
                this.$message.success(`修改第 ${this.idx + 1} 行成功`);
            },
            // 确定删除
            deleteRow() {
                const param = {
                    "ids": this.ids
                }
                this.$axios.delete(this.ApartmentUrl, {params: param}).then((res) => {
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
