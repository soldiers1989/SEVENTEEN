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
                </el-select>
                <el-select v-model="select_type" clearable placeholder="请选择房间类型">
                    <el-option
                        v-for="roomType in roomTypes"
                        :key="roomType.id"
                        :label="roomType.name"
                        :value="roomType.id">
                    </el-option>
                    <el-option
                        label="无关联房间类型"
                        value="0">
                    </el-option>
                </el-select>
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建</el-button>
                <el-button type="primary" icon="el-icon-edit-outline" @click="handleRoomType">房间类型管理</el-button>

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
                <el-table-column prop="status" label="状态">
                </el-table-column>
                <el-table-column prop="roomType" label="房间类型" width="120">
                </el-table-column>
                <el-table-column prop="type" label="租凭类型">
                </el-table-column>
                <el-table-column prop="price" label="价格">
                </el-table-column>
                <el-table-column prop="structure" label="户型">
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
        <el-dialog :title="title" :visible.sync="visible" width="55%" :before-close="handleClose">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="房号" prop="apNum">
                    <el-input :disabled="roomFlag" v-model="ruleForm.apNum" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="房名" prop="name">
                    <el-input v-model="ruleForm.name"></el-input>
                </el-form-item>
                <el-form-item label="租凭类型" prop="type">
                    <el-radio-group v-model="ruleForm.type">
                        <el-radio label="1">短租</el-radio>
                        <el-radio label="2">长租</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="房间分类" prop="roomType">
                    <el-radio-group v-model="ruleForm.roomType">
                        <el-radio-button v-for="roomType in roomTypes" :label="roomType.id" :key="roomType.id">{{roomType.name}}
                        </el-radio-button>
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
                <el-form-item label="床位" prop="bed">
                    <el-input placeholder="填写数字即可" v-model="ruleForm.bed" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="户型" prop="structure">
                    <el-input v-model="ruleForm.structure" class="handle-input"></el-input>
                </el-form-item>
                <el-form-item label="房间物品" prop="good">
                    <el-checkbox-group v-model="ruleForm.good">
                        <el-checkbox v-for="good in goods" :label="good.id" :key="good.id">{{good.name}}
                        </el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="图片上传">
                    <el-upload
                        class="upload-demo"
                        ref="upload"
                        :action="uploadUrl"
                        :on-preview="handlePreview"
                        :on-remove="handleRemove"
                        :file-list="fileList"
                        :headers="headers"
                        multiple
                        list-type="picture"
                        :auto-upload="false">
                        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                        <!--<el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器-->
                        </el-button>
                        <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过50mb,默认第一张为主图，可点击图片修改</div>
                    </el-upload>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
                    <el-button v-if="add" @click="resetForm('ruleForm')">重置</el-button>
                    <el-button v-if="edit" @click="resetForm('ruleForm');visible=false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 房间类型管理 -->
        <el-dialog title="房间类型管理" :visible.sync="roomTypeFlag" width="30%" :before-close="handleClose">
            <div style="color: red">
                删除类型后，请给之前的房间重新关联，否则小程序查询不到
            </div>
            <div class="container">
                <el-table
                    :data="typeData"
                    stripe
                    style="width: 100%">
                    <el-table-column prop="name" label="类型" sortable width="300">
                    </el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button size="small" type="danger" @click="handleTypeDelete(scope.id, scope.row)">删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-input placeholder="添加房间类型" v-model='inputTag' class="input-with-select">
                    <el-button slot="append" @click="handleAddTag()" icon="el-icon-plus"></el-button>
                </el-input>
            </div>
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
                RoomUrl: this.$global.baseUrl + "/room",
                uploadUrl: this.$global.baseUrl + "/file",
                tableData: [],
                typeData: [],
                fileList: [],
                roomTypeFlag: false,
                removeFileList: '',
                select_type:'',
                inputTag: '',
                headers: {
                    Authorization: "Bearer " + sessionStorage.getItem("token"),
                    Room: '',
                },
                curPage: 1,
                master: '0',
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
                typeIds: '',
                roomFlag: false,
                ruleForm: {
                    apNum: '',
                    name: '',
                    price: '',
                    area: '',
                    floor: '',
                    structure: '',
                    type: '',
                    roomType: '',
                    bed:0,
                    good: [],
                    id: ''
                },
                goods: [],
                roomTypes: [],
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
                    bed: [
                        {required: true, message: '请输入床位', trigger: 'blur'},
                        {min: 1, max: 10, message: '长度在 0 到 10 个字符', trigger: 'blur'}
                    ],
                    area: [
                        {required: true, message: '请输入房间面积', trigger: 'blur'},
                        {min: 1, max: 10, message: '长度在 0 到 10 个字符', trigger: 'blur'}
                    ],
                    floor: [
                        {required: true, message: '请输入房间楼层', trigger: 'blur'},
                        {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
                    ],
                    structure: [
                        {required: true, message: '请输入房间户型', trigger: 'blur'},
                        {min: 1, max: 10, message: '长度在 0 到 10 个字符', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请选择租凭类型', trigger: 'change'}
                    ],
                    roomType: [
                        {required: true, message: '请选择房间类型', trigger: 'change'}
                    ],
                    good: [
                        {type: 'array', required: true, message: '请至少选择一个房间物品', trigger: 'change'}
                    ]
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
            handleRemove(file, fileList) {
                this.removeFileList += file.id + ',';
            },
            removeFile() {

                this.$axios.delete(this.uploadUrl, {params: {ids: this.removeFileList}}).then((res) => {
                    if (res.data.resultCode === 200) {
                        this.removeFileList = '';
                    } else {
                        this.$message.error('失败');
                        return false;
                    }
                })
            },

            handlePreview(file) {
                let id = file.id;

                this.$notify({
                    title: '成功',
                    message: '该图片成功设置成主图',
                    type: 'success'
                });

                if (this.edit) {
                    this.master = file.id;
                }
            },
            submitUpload() {
                this.$refs.upload.headers.Room = this.ruleForm.id + "_" + this.master;
                this.$refs.upload.submit();
            },
            submitForm(formName) {
                let that = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (that.$refs.upload.uploadFiles.length == 0) {
                            this.$message.error('请选择至少一张图片上传');
                            return false
                        }
                        if (that.add) {
                            this.$axios.get(this.RoomUrl + '/' + this.ruleForm.apNum).then((res) => {
                                if (res.data.resultCode === 200) {
                                    if (!res.data.data) {
                                        this.$axios.post(this.RoomUrl, this.ruleForm).then((res) => {
                                            if (res.data.resultCode === 200) {
                                                this.$message.success('创建成功');
                                                that.ruleForm.id = res.data.data.id;
                                                that.submitUpload();
                                                that.resetForm('ruleForm');
                                                this.visible = false;

                                            } else {
                                                this.$message.error('创建失败');
                                                return false;
                                            }
                                        })
                                    } else {
                                        this.$message.error('房号已存在，请更改或者删除已经存在房号！');
                                        return false;
                                    }

                                } else {
                                    this.$message.error('创建失败');
                                    return false;
                                }
                            })
                        } else {

                            this.$axios.post(this.RoomUrl + "/update", this.ruleForm).then((res) => {
                                if (res.data.resultCode === 200) {
                                    this.$message.success('更新成功');
                                    that.ruleForm.id = res.data.data.id;
                                    that.removeFile();
                                    that.submitUpload();
                                    that.resetForm('ruleForm');
                                    this.visible = false;

                                } else {
                                    this.$message.error('更新失败');
                                    return false;
                                }
                            })

                            this.$axios.post(this.uploadUrl + "/updateMaster", qs.stringify({id: this.master})).then((res) => {
                                if (res.data.resultCode === 200) {
                                } else {
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
                this.ruleForm.good = [];
                this.fileList = [];
                this.master = '0';
                this.removeFileList = '';
                if(this.$refs[formName]){
                    this.$refs[formName].resetFields();
                }
                this.getData();
//                this.handleAdd()
            },

            getData(page) {
                const param = {
                    "pageNum": this.curPage,
                    "remark": this.select_word == null ? "" : this.select_word.trim(),
                    "status": this.select_cate == null ? "" : this.select_cate.trim(),
                    "roomType": this.select_type == null ? "" : this.select_type.trim()
                }
                this.$axios.get(this.RoomUrl + '/tags', {params: {"type": 'r'}}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.roomTypes = res.data.data;
                    }
                })
                this.$axios.get(this.RoomUrl, {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.tableData = res.data.data;
                        this.total = res.data.pageInfo.total;
                        this.curPage = res.data.pageInfo.pageNum;
                    }
                })
            },
            search() {
                this.is_search = true;
                this.curPage = 1;
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
                this.roomFlag = false;


                this.$axios.get(this.RoomUrl + '/tags', {params: {"type": 'g'}}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.goods = res.data.data;
                    }
                })



            },
            handleAddTag() {
                if (this.inputTag === '') {
                    return;
                }
                const param = {
                    "type": 'r',
                    "name": this.inputTag
                }
                this.$axios.post(this.RoomUrl + '/tags', param).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.typeData = res.data.data;
                        this.handleRoomType();
                        this.inputTag = '';
                    }
                })
            },
            handleRoomType() {
                this.roomTypeFlag = true;
                this.add = false;
                this.edit = false;

                const param = {
                    "type": 'r'
                }
                this.$axios.get(this.RoomUrl + '/tags', {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.typeData = res.data.data;
                    }
                })
            },

            handleEdit(index, row) {
                this.idx = index;
                let that = this;
                const param = {
                    "type": 'g'
                }
                this.$axios.get(this.RoomUrl + '/tags', {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        that.goods = res.data.data;
                    }
                })

                this.$axios.get(this.RoomUrl + '/' + row.id + '/detail').then((res) => {
                    if (res.data.resultCode === 200) {
                        that.ruleForm = res.data.data.seApartment;
                        that.fileList = res.data.data.seApartmentImg;
                        that.ruleForm.good = res.data.data.good;

                    } else {
                        this.$message.error('编辑失败');
                        return false;
                    }
                })
                this.title = "编辑";
                this.edit = true;
                this.add = false;
                this.visible = true;
                this.roomFlag = true;
            },

            handleDelete(index, row) {
                if (!this.ids) {
                    this.ids = row.id
                }
                this.delVisible = true;
            },
            handleTypeDelete(index, row) {
                if (!this.typeIds) {
                    this.typeIds = row.id
                }
                this.deleteRoomType()
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
                let url = this.RoomUrl;
                let param = {"ids": this.ids};
                this.$axios.delete(url, {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.getData()
                        this.$message.success('删除成功');
                    } else {
                        this.$message.error('删除失败');
                    }
                })
                this.ids = '';
                this.delVisible = false;
            },

            // 确定删除
            deleteRoomType() {
                let url = this.RoomUrl + "/tags";
                let param = {"ids": this.typeIds};
                this.$axios.delete(url, {params: param}).then((res) => {
                    if (res.data.resultCode == 200) {
                        this.handleRoomType()
                        this.typeIds='';
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
