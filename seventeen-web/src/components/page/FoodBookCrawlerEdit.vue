<template>
    <div v-loading.body="loading">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{path:'/food/book/clawer'}"><i class="el-icon-time"></i> 数据清洗
                </el-breadcrumb-item>
                <el-breadcrumb-item :to="{path:'/food/book/clawer'}">爬虫数据</el-breadcrumb-item>
                <el-breadcrumb-item>编辑</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-row :gutter="0">
            <el-col :span="11">
                <div class="form-box">
                    <el-form ref="form" :model="form" :rules="rules2" label-width="80px">
                        <el-form-item label="编号">
                            <span>{{form.fbId}}</span>
                        </el-form-item>
                        <el-form-item label="菜肴名称" prop="fbName">
                            <el-input v-model="form.fbName"></el-input>
                        </el-form-item>
                        <el-form-item label="修改人">
                            <span>{{form.lastModiUsername}}</span>
                        </el-form-item>
                        <el-form-item label="修改时间">
                            <span>{{form.lastModiTime}}</span>
                        </el-form-item>
                        <el-form-item label="菜肴图片">
                            <el-upload
                                list-type="picture-card"
                                :file-list="fileList"
                                :headers="headers"
                                :action="fileUrl"
                                :show-file-list="true"
                                :on-preview="handlePictureCardPreview"
                                :on-remove="handleRemove"
                                :on-success="onSuccess"
                                :on-error="onError"
                                multiple>
                                <i class="el-icon-plus"></i>
                            </el-upload>
                            <el-dialog :visible.sync="dialogVisible" size="tiny">
                                <img width="100%" :src="dialogImageUrl" alt="">
                            </el-dialog>
                        </el-form-item>
                        <el-form-item label="烹饪步骤">
                            <el-input type="textarea" :autosize="{ minRows: 3}" v-model="form.fbMakestep"/>
                        </el-form-item>
                        <el-form-item>
                            <el-button @click="onCancel">取消</el-button>
                            <el-button type="danger" @click="toVoid">作废</el-button>
                            <el-button type="primary" @click="submitForm('form')">提交入库</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-col>
            <el-col :span="13">
                <div class="handle-box">
                    <el-button type="primary" icon="edit" @click="handleAdd">新增</el-button>
                </div>
                <el-table :data="tableData" border style="width: 100%" ref="multipleTable">
                    <el-table-column prop="fmOptZh" label="分类" width="88px" sortable/>
                    <el-table-column prop="fmName" label="食材名称" width="110px"/>
                    <el-table-column prop="fmStandardName" label="别名" width="110px"/>
                    <el-table-column prop="fmWeight" label="常规重量" width="70px"/>
                    <el-table-column prop="fmUnitZh" label="单位" width="70px"/>
                    <el-table-column label="操作">
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
            </el-col>
        </el-row>

        <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" @close="handleClose('ruleForm')">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
                <el-form-item label="分类" prop="fmOpt" :label-width="formLabelWidth">
                    <el-select v-model="ruleForm.fmOpt" placeholder="请选择分类">
                        <el-option
                            v-for="(key,value) in fmOpts"
                            :key="key"
                            :label="value"
                            :value="key">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-row :gutter="0">
                    <el-col :span="12">
                        <el-form-item label="别名" prop="fmId" :label-width="formLabelWidth">
                            <el-select v-model="ruleForm.fmId" filterable
                                       remote :remote-method="searchMaterials" :loading="fmLoading"
                                       placeholder="输入搜索">
                                <el-option
                                    v-for="item in materials"
                                    :key="item.fmId"
                                    :label="item.fmName"
                                    :value="item.fmId">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="食材名称" :label-width="formLabelWidth">
                            {{getFmName}}
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="0">
                    <el-col :span="12">
                        <el-form-item label="常规重量" prop="fmWeight" :label-width="formLabelWidth">
                            <el-input v-model.number="ruleForm.fmWeight" auto-complete="off" style="width: 217px;"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="单位" prop="fmUnit" :label-width="formLabelWidth">
                            <el-select v-model="ruleForm.fmUnit" placeholder="请选择单位">
                                <el-option
                                    v-for="(key,value) in fmUnits"
                                    :key="key"
                                    :label="value"
                                    :value="key">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import ElCol from "element-ui/packages/col/src/col";
    export default {
        components: {ElCol},
        data() {
            var validateFbName = (rule, value, callback) => {
                value = value.trim();
                if (!value) {
                    value = this.form.fbName;
                }
                if (!value) {
                    callback(new Error('请输入菜名'));
                    this.$message.error('请输入菜名');
                }
                this.$axios.get(this.foodBooksUrl, {params: {"name": value}}).then((res) => {
                    if (res.data.status == 200) {
                        if (res.data.data.length != 0) {
                            callback(new Error('此菜肴已入库'));
                            this.$message.error('此菜肴已入库');
                        } else {
                            callback();
                        }
                    } else {
                        this.$message.error("查询失败:" + res.data.message);
                        callback();
                    }
                }).catch((error) => {
                    callback();
                });
            };

            return {
                fmWeight: null,
                loading: false,
                fmLoading: false,
                url: this.$global.baseUrl + "/dataClean/food/book/clawer/tmp",
                fileUrl: this.$global.baseUrl + "/dataClean/food/book/clawer/upload/image/" + this.$route.query.id,
                fbImageUrl: this.$global.baseUrl + "/dataClean/food/book/clawer/tmp/image",
                tableUrl: this.$global.baseUrl + "/dataClean/food/book/clawer/tmp/cbfm",
                fmOptUrl: this.$global.baseUrl + "/dataClean/food/book/clawer/fm/opts",
                fmUnitUrl: this.$global.baseUrl + "/dataClean/food/book/clawer/fm/units",
                materialsUrl: this.$global.baseUrl + "/chrs-2/vrecipe/materials/search",
                foodBooksUrl: this.$global.baseUrl + "/chrs-2/vrecipe/foodBooks/search",
                instoreUrl: this.$global.baseUrl + "/dataClean/food/book/clawer/instore",
                headers: {
                    Authorization: "Bearer " + sessionStorage.getItem("token")
                },
                form: {
                    fbId: this.$route.query.id,
                    fbName: null,
                    fbMakestep: null
                },
                rules2: {
                    fbName: [
                        {validator: validateFbName, trigger: 'blur'}
                    ]
                },
                fileList: [],
                tableData: [],//表格数据
                fmOpts: {},
                fmUnits: {},
                materials: [],
                dialogImageUrl: '',
                dialogVisible: false,
                dialogTitle: "",
                dialogFormVisible: false,
                formLabelWidth: "80px",
                isEdit: false,
                ruleForm: {
                    id: null,
                    fmOpt: null,
                    fmId: null,
                    fmStandardName: null,
                    fmName: null,
                    fmWeight: null,
                    fmUnit: null
                },
                rules: {
                    fmOpt: [
                        {required: true, message: '请选择分类', trigger: 'blur'}
                    ],
                    fmId: [
                        {required: true, message: '请输入标准名称', trigger: 'blur'}
                    ],
                    fmWeight: [
                        {type: "number", required: true, message: '请输入常规重量(数值)', trigger: 'blur'}
                    ],
                    fmUnit: [
                        {required: true, message: '请选择单位', trigger: 'blur'}
                    ],
                }
            }
        },
        methods: {
            onCancel(){
                this.$router.go(-1);
            },
            search(){
                this.loading = true;
                this.$axios.get(this.url + "/" + this.form.fbId).then((res) => {
                    if (res.data.status == 200) {
                        this.form = res.data.data;
                        for (let image of this.form.images) {
                            var fullPath = image;
                            if (!image.includes("http")) {
                                fullPath = this.$global.imgUrl + "/sys/files/show/" + image;
                            }
                            this.fileList.push({name: image, url: fullPath});
                        }
                        this.$refs['form'].validate();
                    } else {
                        this.$message.error("查询失败:" + res.data.message);
                    }
                    this.loading = false;
                }).catch((error) => {
                    this.loading = false;
                });
            },
            searchTable(){
                this.$axios.get(this.tableUrl + "/" + this.form.fbId).then((res) => {
                    if (res.data.status == 200) {
                        this.tableData = res.data.data;
                    } else {
                        this.$message.error("查询失败:" + res.data.message);
                    }
                })
            },
            getFmOpts(){
                this.$axios.get(this.fmOptUrl).then((res) => {
                    if (res.data.status == 200) {
                        this.fmOpts = res.data.data;
                        this.ruleForm.fmOpt = "1";
                    } else {
                        this.$message.error("查询失败:" + res.data.message);
                    }
                })
            },
            getFmUnits(){
                this.$axios.get(this.fmUnitUrl).then((res) => {
                    if (res.data.status == 200) {
                        this.fmUnits = res.data.data;
                        this.ruleForm.fmUnit = "1";
                    } else {
                        this.$message.error("查询失败:" + res.data.message);
                    }
                })
            },
            searchMaterials(fmName){
                if (fmName !== '') {
                    this.fmLoading = true;
                    this.$axios.get(this.materialsUrl, {params: {"name": fmName}}).then((res) => {
                        if (res.data.status == 200) {
                            this.materials = res.data.data;
                        } else {
                            this.$message.error("查询失败:" + res.data.message);
                        }
                        this.fmLoading = false;
                    }).catch((error) => {
                        this.fmLoading = false;
                    });
                } else {
                    this.materials = [];
                }
            },
            onSuccess(response, file, fileList){
                file.name = response.data.fullPath;
            },
            onError(err, file, fileList){
                switch (err.status) {
                    case 401:
                        // 返回 401 清除token信息并跳转到登录页面
                        sessionStorage.removeItem("token");
                        this.$router.replace({
                            path: '/login',
                            query: {redirect: this.$router.currentRoute.fullPath}
                        });
                        break;
                    case 403:
                        this.$message.error("此接口无访问权限");
                        break;
                    default:
                        this.$message.error("此接口无法访问");
                }
            },
            handleRemove(file, fileList) {
                this.$confirm('删除此图片, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.delete(this.fbImageUrl + "/" + this.form.fbId, {params: {fullPath: file.name}}).then((res) => {
                        if (res.data.status == 200) {
                            this.$message.success(res.data.data);
                        } else {
                            this.$message.error("删除图片失败:" + res.data.message);
                        }
                    })
                })
            },
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
            handleAdd(){
                this.dialogTitle = "新增菜肴食材";
                this.dialogFormVisible = true;
            },
            handleEdit(index, row){
                this.isEdit = true;
                this.searchMaterials(row.fmName);
                if (row.fmOpt) {
                    this.ruleForm.fmOpt = row.fmOpt;
                }
                this.ruleForm.id = row.id;
                this.ruleForm.fmName = row.fmName;
                this.ruleForm.fmId = row.fmId;
                this.ruleForm.fmStandardName = row.fmStandardName;
                if (row.fmWeight) {
                    if (isNaN(row.fmWeight)) {
                        this.ruleForm.fmWeight = row.fmWeight;
                    } else {
                        this.ruleForm.fmWeight = parseFloat(row.fmWeight);
                    }
                }
                if (row.fmUnit) {
                    this.ruleForm.fmUnit = row.fmUnit;
                }
                this.dialogTitle = "修改菜肴食材";
                this.dialogFormVisible = true;
            },
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if (formName === 'ruleForm') {
                        if (valid) {
                            this.loading = true;
                            for (let fm of this.materials) {
                                if (fm.fmId == this.ruleForm.fmId) {
                                    this.ruleForm.fmStandardName = fm.fmName;
                                }
                            }
                            if (this.isEdit) {
                                this.$axios.put(this.tableUrl + "/" + this.ruleForm.id, this.ruleForm).then((response) => {
                                    if (response.data.status == 200) {
                                        this.$message.success(response.data.data)
                                        this.searchTable();
                                        this.handleClose(formName);
                                    } else {
                                        this.$message.error("修改失败，" + response.data.message)
                                    }
                                    this.loading = false;
                                }).catch((error) => {
                                    this.loading = false;
                                });
                            } else {
                                this.$axios.post(this.tableUrl + "/" + this.form.fbId, this.ruleForm).then((response) => {
                                    if (response.data.status == 200) {
                                        this.$message.success(response.data.data)
                                        this.searchTable();
                                        this.handleClose(formName);
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
                    } else if (formName === 'form') {
                        if (valid) {
                            this.loading = true;
                            this.$axios.put(this.instoreUrl + "/" + this.form.fbId, this.form).then((response) => {
                                if (response.data.status == 200) {
                                    this.$message.success(response.data.data);
                                    this.$router.go(-1);
                                } else {
                                    this.$message.error("提交入库失败，" + response.data.message)
                                }
                                this.loading = false;
                            }).catch((error) => {
                                this.loading = false;
                            });
                        }
                    }
                })
            },
            toVoid(){
                this.$confirm('作废此菜肴, 是否继续?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    this.$axios.put(this.url + "/" + this.form.fbId + "?status=1").then((response) => {
                        if (response.data.status == 200) {
                            this.$message.success(response.data.data);
                            this.$router.go(-1);
                        } else {
                            this.$message.error("操作失败，" + response.data.message)
                        }
                        this.loading = false;
                    }).catch((error) => {
                        this.loading = false;
                    });
                });
            },
            handleDelete(index, row) {
                this.$confirm('删除【' + row.fmName + '】, 是否继续?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    this.$axios.delete(this.tableUrl + "/" + row.id).then((response) => {
                        if (response.data.status == 200) {
                            this.$message.success(response.data.data);
                            this.searchTable();
                        } else {
                            this.$message.error("删除失败，" + response.data.message)
                        }
                        this.loading = false;
                    }).catch((error) => {
                        this.loading = false;
                    });
                });
            },
            handleClose(formName){//关闭弹窗时，清空数据
                this.ruleForm.id = null;
                this.ruleForm.fmOpt = "1";
                this.ruleForm.fmId = null;
                this.ruleForm.fmStandardName = null;
                this.ruleForm.fmName = null;
                this.ruleForm.fmWeight = null;
                this.ruleForm.fmUnit = "1";
                this.isEdit = false;
                this.materials = [];
                this.dialogFormVisible = false;
            },
        },
        created() {
            this.search();
            this.searchTable();
            this.getFmOpts();
            this.getFmUnits();
        },
        computed: {
            // 计算属性的 getter
            getFmName() {
                if (this.ruleForm.fmName) {
                    return this.ruleForm.fmName;
                }
                for (let fm of this.materials) {
                    if (fm.fmId == this.ruleForm.fmId) {
                        return fm.fmName;
                    }
                }
            }
        },

    }
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }
</style>
