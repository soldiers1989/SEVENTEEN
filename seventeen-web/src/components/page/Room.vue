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
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建</el-button>
            </div>
            <el-table :data="tableData" border style="width: 100%" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="apNum" label="房号" sortable width="200">
                </el-table-column>
                <el-table-column prop="name" label="房名" width="250">
                </el-table-column>
                <el-table-column prop="status" label="状态" width="250">
                </el-table-column>
                <el-table-column prop="type" label="类型">
                </el-table-column>
                <el-table-column prop="price" label="价格">
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
        <el-dialog title="新建" :visible.sync="addVisible" width="40%">
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
                tableData: [],
                curPage: 1,
                total: 0,
                multipleSelection: [],
                select_cate: '',
                select_word: '',
                del_list: [],
                is_search: false,
                editVisible: false,
                addVisible: false,
                delVisible: false,
                ids: '',
                form: {
                    name: '',
                    date: '',
                    address: ''
                },
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
                        this.$message.success('删除失败');
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
