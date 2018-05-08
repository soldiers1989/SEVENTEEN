<template>
    <div class="login-wrap" v-loading="loading">
        <div class="ms-title">SEVENTEEN后台管理系统</div>
        <div class="ms-login">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="demo-ruleForm">
                <el-form-item prop="username">
                    <el-input v-model="ruleForm.username" placeholder="用户名"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" placeholder="密码" v-model="ruleForm.password"
                              @keyup.enter.native="submitForm('ruleForm')"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button class="login-btn-submit" type="primary" @click="submitForm('ruleForm')">登录</el-button>
                    <el-button type="danger" :plain="true" @click="resetForm('ruleForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        data: function () {
            return {
                ruleForm: {
                    username: '',
                    password: ''
                },
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
        methods: {
            submitForm(formName) {
                const self = this;
                self.url = self.$global.baseUrl + "/token/auth";
                self.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        self.$axios.post(self.url, {
                            "username": self.ruleForm.username,
                            "password": self.ruleForm.password
                        }).then((response) => {
                            if (response.data.status == 200) {
                                self.$message.success("登录成功")
                                sessionStorage.setItem('token', response.data.data.token);
                                sessionStorage.setItem('username', self.ruleForm.username);
                                self.$router.push('/readme');
                            } else {
                                self.$message.warning("登录失败，" + response.data.message)
                            }
                            this.loading = false;
                        }).catch((error) => {
                            this.loading = false;
                        });
                    } else {
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }
    }
</script>

<style scoped>
    .login-wrap {
        width: 100%;
        height: 100%;
    }

    .ms-title {
        position: absolute;
        top: 50%;
        width: 100%;
        margin-top: -230px;
        text-align: center;
        font-size: 30px;
        color: #fff;
    }

    .ms-login {
        position: absolute;
        left: 50%;
        top: 50%;
        width: 300px;
        height: 160px;
        margin: -150px 0 0 -190px;
        padding: 40px;
        border-radius: 5px;
        background: #fff;
    }

    .login-btn-submit {
        width: 220px;
    }
</style>
