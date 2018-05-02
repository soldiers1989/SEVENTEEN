<template>
    <div v-loading.body="loading">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{path:'/food/book/clawer'}"><i class="el-icon-time"></i> 数据清洗
                </el-breadcrumb-item>
                <el-breadcrumb-item :to="{path:'/food/books'}">入库数据</el-breadcrumb-item>
                <el-breadcrumb-item>查看</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-row :gutter="0">
            <el-col :span="12">
                <div class="form-box">
                    <el-form ref="form" :model="form" label-width="80px">
                        <el-form-item label="编号">
                            <span>{{form.fbId}}</span>
                        </el-form-item>
                        <el-form-item label="菜肴名称">
                            <span>{{form.fbName}}</span>
                        </el-form-item>
                        <el-form-item label="修改人">
                            <span>{{form.lastModiUsername}}</span>
                        </el-form-item>
                        <el-form-item label="修改时间">
                            <span>{{form.lastModiTime}}</span>
                        </el-form-item>
                        <el-form-item label="菜肴图片">
                            <el-card :body-style="{ padding: '0px'}">
                                <img :src="file.url" class="image"
                                     style="margin-top:10px;margin-left:8px;height:160px;width:160px;"
                                     v-for="file in fileList">
                            </el-card>
                        </el-form-item>
                        <el-form-item label="烹饪步骤">
                            <span>{{form.fbMakestep}}</span>
                        </el-form-item>
                        <el-form-item>
                            <el-button @click="onCancel">返回</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-col>
            <el-col :span="12">
                <el-table :data="tableData" border style="width: 100%" ref="multipleTable">
                    <el-table-column prop="fmOptZh" label="分类" width="88px" sortable/>
                    <el-table-column prop="fmName" label="食材名称"/>
                    <el-table-column prop="fmWeight" label="重量"/>
                    <el-table-column prop="fmUnitZh" label="单位"/>
                </el-table>
            </el-col>
        </el-row>

    </div>
</template>

<script>
    export default {
        data() {
            return {
                loading: false,
                url: this.$global.baseUrl + "/dataClean/food/books",
                tableUrl: this.$global.baseUrl + "/dataClean/food/books/cbfm",
                headers: {
                    Authorization: "Bearer " + sessionStorage.getItem("token")
                },
                form: {
                    fbId: this.$route.query.id,
                    fbName: null,
                    fbMakestep: null
                },
                fileList: [],
                tableData: [],//表格数据
                formLabelWidth: "80px",
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
            }
        },
        created() {
            this.search();
            this.searchTable();
        },
        computed: {},

    }
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }
</style>
