var vue = new Vue({
    el: '#app',
    data: {
        value: new Date(),
        loading: false,
        iconUrl: ctx+"assetss/images/fav.png",
        iconTitle: "database-export",
        iconNotes: "数据库表结构导出工具V2.0",
        o_ip: '',
        o_port: "1521",
        o_dataname: "orcl",
        o_username: "",
        o_password: "",
        o_filepath: "D://",
        m_ip: '',
        m_port: "3306",
        m_dataname: "",
        m_username: "",
        m_password: "",
        m_filepath: "D://",
        s_ip: '',
        s_port: "1433",
        s_dataname: "",
        s_username: "",
        s_password: "",
        s_filepath: "D://",
        welcomeDivShow: true,
        oracleDivShow: false,
        mysqlDivShow: false,
        sqlserverDivShow: false,
        welcomeIconImg: ctx+"/assetss/images/v2/welcome-icon-click.png",
        oracleIconImg: ctx+"/assetss/images/v2/oracle-icon-unclick.png",
        mysqlIconImg: ctx+"/assetss/images/v2/mysql-icon-unclick.png",
        sqlserverIconImg: ctx+"/assetss/images/v2/sqlserver-icon-unclick.png",
        welcomeBarStyle: {
            borderBottom: "1px solid rgb(235, 233, 233)",
            background: "#F2F6FC"
        },
        oracleBarStyle: {
            borderBottom: "1px solid rgb(235, 233, 233)",
            background: "rgb(255,255,255)"
        },
        mysqlBarStyle: {
            borderRight: "1px solid rgb(235, 233, 233)",
            background: "rgb(255,255,255)"
        },
        sqlServerBarStyle: {
            borderTop: "1px solid rgb(235, 233, 233)",
            background: "rgb(255,255,255)"
        },
        contentStyle: {
            background: "#F2F6FC",
            width: "100%",
            height: "100%"
        }
    },
    methods: {
        generateWord(sqlKind) {
            let ip;
            let port;
            let dataname;
            let username;
            let password;
            let filepath;
            if (sqlKind == 'oracle') {
                ip = this.o_ip;
                port = this.o_port;
                dataname = this.o_dataname;
                username = this.o_username;
                password = this.o_password;
                filepath = this.o_filepath;
            } else if (sqlKind == 'mysql') {
                ip = this.m_ip;
                port = this.m_port;
                dataname = this.m_dataname;
                username = this.m_username;
                password = this.m_password;
                filepath = this.m_filepath;
            } else if (sqlKind == 'sqlserver') {
                ip = this.s_ip;
                port = this.s_port;
                dataname = this.s_dataname;
                username = this.s_username;
                password = this.s_password;
                filepath = this.s_filepath;
            }

            if (ip == '') {
                this.$notify.error({
                    title: '错误',
                    message: 'IP不能为空',
                    duration: 0
                });
                return;
            }
            if (port == '') {
                this.$notify.error({
                    title: '错误',
                    message: '端口不能为空',
                    duration: 0
                });
                return;
            }
            if (dataname == '') {
                this.$notify.error({
                    title: '错误',
                    message: '实例/数据库名称不能为空',
                    duration: 0
                });
                return;
            }
            if (username == '') {
                this.$notify.error({
                    title: '错误',
                    message: '用户名不能为空',
                    duration: 0
                });
                return;
            }
            if (password == '') {
                this.$notify.error({
                    title: '错误',
                    message: '密码不能为空',
                    duration: 0
                });
                return;
            }
            if (filepath == '') {
                this.$notify.error({
                    title: '错误',
                    message: '保存路径不能为空',
                    duration: 0
                });
                return;
            }
            this.loading = true;
            axios.post('/dbExport/makeWord', {
                ip: ip,
                port: port,
                dbName: dataname,
                userName: username,
                password: password,
                dbKind: sqlKind
            }).then((response)=> {
                this.loading = false;
                this.$notify.error({
                    message: response.resultMsg,
                    duration: 0,
                    type:success
                });
            }).catch((error)=> {
                this.loading = false;
                this.$notify.error({
                    title: '错误',
                    message: '网络错误请重试',
                    duration: 0
                });
            });
        },
        barClick(sqlKind) {
            if (sqlKind == 'welcome') {
                this.welcomeDivShow = true;
                this.oracleDivShow = false;
                this.mysqlDivShow = false;
                this.sqlserverDivShow = false;
                this.welcomeBarStyle.background = "#F2F6FC";
                this.mysqlBarStyle.background = "rgb(255,255,255)";
                this.sqlServerBarStyle.background = "rgb(255,255,255)";
                this.oracleBarStyle.background = "rgb(255,255,255)";
                //图片
                this.welcomeIconImg = ctx+"/assetss/images/v2/welcome-icon-click.png";
                this.oracleIconImg = ctx+"/assetss/images/v2/oracle-icon-unclick.png";
                this.mysqlIconImg = ctx+"/assetss/images/v2/mysql-icon-unclick.png";
                this.sqlserverIconImg = ctx+"/assetss/images/v2/sqlserver-icon-unclick.png";
            } else if (sqlKind == 'oracle') {
                this.welcomeDivShow = false;
                this.oracleDivShow = true;
                this.mysqlDivShow = false;
                this.sqlserverDivShow = false;
                this.welcomeBarStyle.background = "rgb(255,255,255)";
                this.mysqlBarStyle.background = "rgb(255,255,255)";
                this.sqlServerBarStyle.background = "rgb(255,255,255)";
                this.oracleBarStyle.background = "#F2F6FC";
                //图片
                this.welcomeIconImg = ctx+"/assetss/images/v2/welcome-icon-unclick.png";
                this.oracleIconImg = ctx+"/assetss/images/v2/oracle-icon-click.png";
                this.mysqlIconImg = ctx+"/assetss/images/v2/mysql-icon-unclick.png";
                this.sqlserverIconImg = ctx+"/assetss/images/v2/sqlserver-icon-unclick.png";
            } else if (sqlKind == 'mysql') {

                this.welcomeDivShow = false;
                this.oracleDivShow = false;
                this.mysqlDivShow = true;
                this.sqlserverDivShow = false;
                this.mysqlBarStyle.background = "#F2F6FC";
                this.welcomeBarStyle.background = "rgb(255,255,255)";
                this.sqlServerBarStyle.background = "rgb(255,255,255)";
                this.oracleBarStyle.background = "rgb(255,255,255)";
                //图片
                this.welcomeIconImg = ctx+"/assetss/images/v2/welcome-icon-unclick.png";
                this.oracleIconImg = ctx+"/assetss/images/v2/oracle-icon-unclick.png";
                this.mysqlIconImg = ctx+"/assetss/images/v2/mysql-icon-click.png";
                this.sqlserverIconImg = ctx+"/assetss/images/v2/sqlserver-icon-unclick.png";
            } else if (sqlKind == 'sqlserver') {
                this.welcomeDivShow = false;
                this.oracleDivShow = false;
                this.mysqlDivShow = false;
                this.sqlserverDivShow = true;
                this.welcomeBarStyle.background = "rgb(255,255,255)";
                this.mysqlBarStyle.background = "rgb(255,255,255)";
                this.oracleBarStyle.background = "rgb(255,255,255)";
                this.sqlServerBarStyle.background = "#F2F6FC";
                //图片
                this.welcomeIconImg = ctx+"/assetss/images/v2/welcome-icon-unclick.png";
                this.oracleIconImg = ctx+"/assetss/images/v2/oracle-icon-unclick.png";
                this.mysqlIconImg = ctx+"/assetss/images/v2/mysql-icon-unclick.png";
                this.sqlserverIconImg = ctx+"/assetss/images/v2/sqlserver-icon-click.png";

            }
            this.contentStyle.background = "#F2F6FC";

        }
    }
});