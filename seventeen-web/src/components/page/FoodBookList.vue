<template>
    <div class="table" v-loading.body="loading">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{path:'/food/book/clawer'}"><i class="el-icon-time"></i> 数据清洗
                </el-breadcrumb-item>
                <el-breadcrumb-item>入库数据</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <el-row :gutter="50">
            <el-col :span="4">
                <div class="handle-box">
                    <el-input v-model="fbName" placeholder="请输入菜名"
                              @keyup.enter.native="search()"></el-input>
                </div>
            </el-col>
            <el-col :span="4">
                <el-select v-model="username" clearable placeholder="请选择修改人" @change="search()">
                    <el-option
                        v-for="value in usernameList"
                        :key="value"
                        :label="value"
                        :value="value">
                    </el-option>
                </el-select>
            </el-col>
            <el-col :span="10">
                <div class="block">
                    <span class="demonstration">时间：</span>
                    <el-date-picker
                        v-model="choseTime"
                        type="daterange"
                        align="left"
                        placeholder="选择日期范围"
                        :picker-options="pickerOptions2" @change="search()">
                    </el-date-picker>
                    <el-button type="primary" icon="search" @click="search()">搜 索</el-button>
                    <el-button type="primary" icon="close" @click="reset()">重 置</el-button>
                </div>
            </el-col>
        </el-row>


        <el-table :data="tableData" border style="width: 100%" ref="multipleTable"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50"/>
            <el-table-column prop="fbId" label="编号" width="216"/>
            <el-table-column prop="fbName" label="菜肴名称" width="210"/>
            <el-table-column prop="lastModiUsername" label="修改人" width="150"/>
            <el-table-column prop="icoPath" label="缩略图" width="200px">
                <template slot-scope="scope">
                    <img :src="convert(scope.row.icoPath)" height="80px" style="margin-top: 8px"
                         v-if="scope.row.icoPath"/>
                </template>
            </el-table-column>
            <el-table-column prop="lastModiTime" label="修改时间" width="170" sortable/>
            <el-table-column prop="statusZh" label="状态"/>
            <el-table-column label="操作" width="170">
                <template slot-scope="scope">
                    <el-button size="small"
                               @click="handleEdit(scope.$index, scope.row)">查看
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagination">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                layout="total,sizes,prev,pager,next,jumper"
                :total="total" :current-page="curPage">
            </el-pagination>
        </div>
    </div>
</template>
<script>
    import {formatDate} from '../common/date.js'
    import {mapGetters, mapActions} from 'vuex'

    export default {
        data() {
            return {
                url: this.$global.baseUrl + "/dataClean/food/books",
                showFileUrl: this.$global.imgUrl + "/sys/files/show/",
                usernamesUrl: this.$global.baseUrl + "/dataClean/food/books/usernames",
                statusUrl: this.$global.baseUrl + "/dataClean/food/book/clawer/status",
                dataUrl: "",
                tableData: [],//表格数据
                multipleSelection: [],
                total: 0,
                dialogTitle: "",
                dialogFormVisible: false,
                formLabelWidth: "80px",
                fileList: [],
                usernameList: [],
                username2: '',
                statusList: [],
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
                }
            }
        },
        created() {
            this.getUsernames();
            this.search();
        },
        computed: {
            ...mapGetters({
                curPage: 'instoreCurPage',
                pageSize: 'instorePageSize',
            }),
            fbName: {
                get () {
                    return this.$store.state.instore.fbName;
                },
                set (value) {
                    this.setFbName(value);
                }
            },
            username: {
                get () {
                    return this.$store.state.instore.username;
                },
                set (value) {
                    this.setUsername(value);
                }
            },
            status: {
                get () {
                    return this.$store.state.instore.status;
                },
                set (value) {
                    this.setStatus(value);
                }
            },
            choseTime: {
                get () {
                    return this.$store.state.instore.choseTime;
                },
                set (value) {
                    this.setChoseTime(value);
                }
            },
        },
        methods: {
            ...mapActions({
                setCurPage: 'setInstoreCurPage',
                setPageSize: 'setInstorePageSize',
                setFbName: 'setInstoreFbName',
                setUsername: 'setInstoreUsername',
                setChoseTime: 'setInstoreChoseTime'
            }),
            reset(){
                this.setFbName('');
                this.setUsername('');
                this.setChoseTime([])
                this.search();
            },
            getUsernames(){
                this.$axios.get(this.usernamesUrl).then((res) => {
                    if (res.data.status == 200) {
                        this.usernameList = res.data.data;
                    } else {
                        this.$message.error("获取来源失败:" + res.data.message);
                    }
                });
            },
            getStatus(){
                this.$axios.get(this.statusUrl).then((res) => {
                    if (res.data.status == 200) {
                        this.statusList = res.data.data;
                    } else {
                        this.$message.error("获取状态失败:" + res.data.message);
                    }
                });
            },
            search(){
                const param = {
                    "pageNum": this.curPage,
                    "pageSize": this.pageSize,
                    "fbName": this.fbName == null ? "" : this.fbName.trim(),
                    "username": this.username == null ? "" : this.username.trim(),
                    "status": this.status == null ? "" : this.status.trim()
                };
                if (this.choseTime[0]) {
                    let date = new Date(this.choseTime[0]);
                    const startDate = formatDate(date, 'yyyy-MM-dd');
                    let date2 = new Date(this.choseTime[1]);
                    const endDate = formatDate(new Date(date2.getTime() + 24 * 60 * 60 * 1000 - 1), 'yyyy-MM-dd hh:mm:ss');
                    Object.assign(param, {
                        "begin": startDate,
                        "end": endDate
                    })
                }
                this.loading = true;
                this.$axios.get(this.url, {params: param}).then((res) => {
                    if (res.data.status == 200) {
                        this.tableData = res.data.data;
                        this.total = res.data.pageInfo.total;
                    } else {
                        this.$message.error("查询失败:" + res.data.message);
                    }
                    this.loading = false;
                }).catch((error) => {
                    this.loading = false;
                });
            },
            convert(icoPath){
                if (!icoPath.includes("http")) {
                    return this.$global.imgUrl + "/sys/files/show/" + icoPath;
                }
                return icoPath;
            },
            handleSizeChange(val){
                this.setPageSize(val);
                this.search();
            },
            handleCurrentChange(val){
                this.setCurPage(val);
                this.search();
            },
            handleEdit(index, row){
                this.$router.push({name: 'foodBookView', query: {id: row.fbId}})
            },
            handleSelectionChange(val){
                this.multipleSelection = val;
            }
        }
    }
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }
</style>
