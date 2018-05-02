<template>
    <div class="table" v-loading.body="loading">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i> 系统管理</el-breadcrumb-item>
                <el-breadcrumb-item>文件管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="handle-box">
            <el-button type="primary" icon="edit" @click="handleAdd">上传文件</el-button>
            <el-button type="primary" icon="delete" @click="delAll">批量删除</el-button>
        </div>


        <el-row :gutter="50">
            <el-col :span="4">
                <div class="handle-box">
                    <el-input v-model="fileName" placeholder="请输入文件名" @keyup.enter.native="searchFile()"></el-input>
                </div>
            </el-col>
            <el-col :span="4">
                <div>
                    <el-input v-model="username" placeholder="请输入上传用户名" @keyup.enter.native="searchFile()"></el-input>
                </div>
            </el-col>
            <div class="block">
                <span class="demonstration">时间：</span>
                <el-date-picker
                    v-model="choseTime"
                    type="daterange"
                    align="left"
                    placeholder="选择日期范围"
                    :picker-options="pickerOptions2" @change="searchFile()">
                </el-date-picker>
                <el-button type="primary" icon="search" @click="searchFile()">搜 索</el-button>
                <el-button type="primary" icon="close" @click="reset()">重 置</el-button>
            </div>
        </el-row>


        <el-table :data="tableData" border style="width: 100%" ref="multipleTable"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50"/>
            <el-table-column prop="id" label="ID" width="220"/>
            <el-table-column prop="fileName" label="文件名"/>
            <!--<el-table-column prop="fileGroup" label="文件分组" width="100"/>-->
            <!--<el-table-column prop="filePath" label="文件路径"/>-->
            <el-table-column prop="thumbImagePath" label="缩略图" width="200px">
                <template slot-scope="scope">
                    <img :src="showFileUrl + scope.row.thumbImagePath" height="80px" style="margin-top: 8px"
                         v-if="scope.row.thumbImagePath"/>
                </template>
            </el-table-column>
            <el-table-column prop="username" label="上传用户" width="100"/>
            <el-table-column prop="createDate" label="创建时间" width="170" sortable/>
            <el-table-column label="操作" width="170">
                <template slot-scope="scope">
                    <el-button size="small"
                               @click="handleEdit(scope.$index, scope.row)">查看/下载
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


        <el-dialog size="tiny" width="900" :title="dialogTitle" :visible.sync="dialogFormVisible"
                   :close-on-click-modal="false"
                   @close="closeFile()">
            <el-form width="900">
                <div>
                    <el-upload
                        class="upload-demo"
                        ref="upload"
                        :action="fileUrl"
                        :on-preview="handlePreview"
                        :on-remove="handleRemove"
                        :on-success="handleAvatarSuccess"
                        :on-error="onError"
                        :headers="headers"
                        multiple
                        :file-list="fileList"
                        list-type="picture">
                        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                        <div slot="tip" class="el-upload__tip">上传文件 / 图片，且不超过500M</div>
                    </el-upload>
                </div>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="closeFile()">关 闭</el-button>
            </div>
        </el-dialog>

    </div>
</template>
<script>
    import {formatDate} from '../common/date.js'

    export default {
        data() {
            return {
                fileUrl: this.$global.baseUrl + "/sys/files",
                showFileUrl: this.$global.imgUrl + "/sys/files/show/",
                dataUrl: "",
                tableData: [],//表格数据
                curPage: 1,
                multipleSelection: [],
                total: 0,
                dialogTitle: "",
                dialogFormVisible: false,
                formLabelWidth: "80px",
                fileList: [],
                fileName: "",
                username: "",
                headers: {
                    Authorization: "Bearer " + sessionStorage.getItem("token")
                },
                loading: false,
                pickerOptions2: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
                choseTime: ''
            }
        },
        created() {
            this.searchFile();
        },

        computed: {},
        methods: {
            reset() {
                this.choseTime = "";
                this.fileName = "";
                this.username = ""
                this.searchFile();
            },
            closeFile() {
                this.fileList = [];
                this.dialogFormVisible = false;
                this.searchFile();
            },
            searchFile() {
                const param = {
                    "pageNum": this.curPage,
                    "fileName": this.fileName == null ? "" : this.fileName.trim(),
                    "username": this.username == null ? "" : this.username.trim(),
                }
                if (this.choseTime[0]) {
                    let date = new Date(this.choseTime[0]);
                    const startDate = formatDate(date, 'yyyy-MM-dd');
                    let date2 = new Date(this.choseTime[1]);
                    const endDate = formatDate(new Date(date2.getTime() + 24 * 60 * 60 * 1000 - 1), 'yyyy-MM-dd hh:mm:ss');
                    Object.assign(param, {
                        "createBeginDate": startDate,
                        "createEndDate": endDate
                    })
                }
                this.loading = true;
                this.$axios.get(this.fileUrl, {params: param}).then((res) => {
                    if (res.data.status == 200) {
                        this.tableData = res.data.data;
                        this.total = res.data.pageInfo.total;
                        this.curPage = res.data.pageInfo.pageNum;
                    } else {
                        this.$message.error("查询失败:" + res.data.message);
                    }
                    this.loading = false;
                })
            },
            handleRemove(file, fileList) {
                console.log(file, fileList);
            },
            handleAvatarSuccess(res, file) {

            },
            handlePreview(file) {
                console.log(file);
            },
            handleCurrentChange(val) {
                this.curPage = val;
                this.searchFile();
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
            handleAdd() {
                this.dialogTitle = "文件上传";
                this.dialogFormVisible = true;
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            handleEdit(index, row) {
                this.currentId = row.id;
                this.dialogTitle = "查看图片";
                this.dataUrl = this.showFileUrl + row.fileGroup + '/' + row.filePath;
                var extStart = row.fileName.lastIndexOf(".");
                var ext = row.fileName.substring(extStart, row.fileName.length).toUpperCase();
                if (ext == ".BMP" || ext == ".PNG" || ext == ".GIF" || ext == ".JPG" || ext == ".JPEG") {
                    window.open(this.dataUrl);
                } else {
                    window.location.href = this.dataUrl;
                }
            },
            handleDelete(index, row) {
                this.$confirm('删除id【' + row.id + '】的数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    this.$axios.delete(this.fileUrl + "/" + row.id, {}).then((response) => {
                        if (response.data.status == 200) {
                            this.$message.success(response.data.data);
                            this.searchFile();
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
            handleClose() {//关闭弹窗时，清空数据
                this.currentId = null;
            },
            delAll() {
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
                    this.loading = true;
                    this.$axios.delete(this.fileUrl, {params: {fileIds: ids}}).then((response) => {
                        if (response.data.status == 200) {
                            this.$message.success(response.data.data);
                            this.multipleSelection = [];
                            this.searchFile();
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
