var vue = new Vue({
    el: '#app',
    data: {
        fit: 'fill',
        value: new Date(),
        loading: false,
        iconUrl: ctx+"assetss/images/fav.png",
        iconTitle: "database-export",
        iconNotes: "数据库表结构导出工具V3.0.0",
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
        postgresql_ip: '',
        postgresql_port: "1433",
        postgresql_dataname: "",
        postgresql_username: "",
        postgresql_password: "",
        welcomeDivShow: true,
        oracleDivShow: false,
        mysqlDivShow: false,
        sqlserverDivShow: false,
        postgresqlDivShow: false,
        oraclePopoverVisible: false,
        mysqlPopoverVisible: false,
        sqlserverPopoverVisible: false,
        postgresqlPopoverVisible: false,
        welcomeIconImg: ctx+"/assetss/images/v2/welcome-icon-click.png",
        oracleIconImg: ctx+"/assetss/images/v2/oracle-icon-unclick.png",
        mysqlIconImg: ctx+"/assetss/images/v2/mysql-icon-unclick.png",
        sqlserverIconImg: ctx+"/assetss/images/v2/sqlserver-icon-unclick.png",
        postgresqlIconImg: ctx+"/assetss/images/v2/postgresql-icon-unclick.png",
        githubIconImg: ctx+"/assetss/images/v2/GitHub-icon.png",
        emailIconImg: ctx+"/assetss/images/v2/email-icon.png",
        weChatIconImg: ctx+"/assetss/images/v2/wechat-icon.png",
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
        postgresqlBarStyle: {
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
        generateWord(sqlKind,exportFileType) {
            let ip;
            let port;
            let dataname;
            let username;
            let password;
            if(exportFileType==undefined || exportFileType == ''){
                this.$message.error('导出文件类型不能为空');
                return;
            }
            if (sqlKind == 'oracle') {
                this.oraclePopoverVisible = false;
                ip = this.o_ip;
                port = this.o_port;
                dataname = this.o_dataname;
                username = this.o_username;
                password = this.o_password;
            } else if (sqlKind == 'mysql') {
                this.mysqlPopoverVisible = false;
                ip = this.m_ip;
                port = this.m_port;
                dataname = this.m_dataname;
                username = this.m_username;
                password = this.m_password;
            } else if (sqlKind == 'sqlserver') {
                this.sqlserverPopoverVisible = false;
                ip = this.s_ip;
                port = this.s_port;
                dataname = this.s_dataname;
                username = this.s_username;
                password = this.s_password;
            }else if(sqlKind == 'postgresql'){
                this.postgresqlPopoverVisible = false;
                ip = this.postgresql_ip;
                port = this.postgresql_port;
                dataname = this.postgresql_dataname;
                username = this.postgresql_username;
                password = this.postgresql_password;
            }

            if (ip == '') {
                this.$message.error('IP不能为空');
                return;
            }
            if (port == '') {
                this.$message.error('端口不能为空');
                return;
            }
            if (dataname == '') {
                this.$message.error('实例/数据库名称不能为空');
                return;
            }
            if (username == '') {
                this.$message.error('用户名不能为空');
                return;
            }
            if (password == '') {
                this.$message.error('密码不能为空');
                return;
            }
            if(exportFileType == 'excel'){
                window.open("/dbExport/v2/makeExcel?dbKind="+sqlKind+"&ip="+ip+"&port="+port+"&dbName="+dataname+"&userName="+username+"&password="+password+"&exportFileType="+exportFileType);
            }else if(exportFileType == 'word'){
                window.open("/dbExport/v2/makeWord?dbKind="+sqlKind+"&ip="+ip+"&port="+port+"&dbName="+dataname+"&userName="+username+"&password="+password+"&exportFileType="+exportFileType);
            }

        },
        barClick(sqlKind) {
            if (sqlKind == 'welcome') {
                this.welcomeDivShow = true;
                this.oracleDivShow = false;
                this.mysqlDivShow = false;
                this.sqlserverDivShow = false;
                this.postgresqlDivShow = false;
                this.welcomeBarStyle.background = "#F2F6FC";
                this.mysqlBarStyle.background = "rgb(255,255,255)";
                this.sqlServerBarStyle.background = "rgb(255,255,255)";
                this.oracleBarStyle.background = "rgb(255,255,255)";
                this.postgresqlBarStyle.background = "rgb(255,255,255)";
                //图片
                this.welcomeIconImg = ctx+"/assetss/images/v2/welcome-icon-click.png";
                this.oracleIconImg = ctx+"/assetss/images/v2/oracle-icon-unclick.png";
                this.mysqlIconImg = ctx+"/assetss/images/v2/mysql-icon-unclick.png";
                this.sqlserverIconImg = ctx+"/assetss/images/v2/sqlserver-icon-unclick.png";
                this.postgresqlIconImg = ctx+"/assetss/images/v2/postgresql-icon-unclick.png";
            } else if (sqlKind == 'oracle') {
                this.welcomeDivShow = false;
                this.oracleDivShow = true;
                this.mysqlDivShow = false;
                this.sqlserverDivShow = false;
                this.postgresqlDivShow = false;
                this.welcomeBarStyle.background = "rgb(255,255,255)";
                this.mysqlBarStyle.background = "rgb(255,255,255)";
                this.sqlServerBarStyle.background = "rgb(255,255,255)";
                this.oracleBarStyle.background = "#F2F6FC";
                this.postgresqlBarStyle.background = "rgb(255,255,255)";
                //图片
                this.welcomeIconImg = ctx+"/assetss/images/v2/welcome-icon-unclick.png";
                this.oracleIconImg = ctx+"/assetss/images/v2/oracle-icon-click.png";
                this.mysqlIconImg = ctx+"/assetss/images/v2/mysql-icon-unclick.png";
                this.sqlserverIconImg = ctx+"/assetss/images/v2/sqlserver-icon-unclick.png";
                this.postgresqlIconImg = ctx+"/assetss/images/v2/postgresql-icon-unclick.png";
            } else if (sqlKind == 'mysql') {
                this.postgresqlDivShow = false;
                this.welcomeDivShow = false;
                this.oracleDivShow = false;
                this.mysqlDivShow = true;
                this.sqlserverDivShow = false;
                this.mysqlBarStyle.background = "#F2F6FC";
                this.welcomeBarStyle.background = "rgb(255,255,255)";
                this.sqlServerBarStyle.background = "rgb(255,255,255)";
                this.oracleBarStyle.background = "rgb(255,255,255)";
                this.postgresqlBarStyle.background = "rgb(255,255,255)";
                //图片
                this.welcomeIconImg = ctx+"/assetss/images/v2/welcome-icon-unclick.png";
                this.oracleIconImg = ctx+"/assetss/images/v2/oracle-icon-unclick.png";
                this.mysqlIconImg = ctx+"/assetss/images/v2/mysql-icon-click.png";
                this.sqlserverIconImg = ctx+"/assetss/images/v2/sqlserver-icon-unclick.png";
                this.postgresqlIconImg = ctx+"/assetss/images/v2/postgresql-icon-unclick.png";
            } else if (sqlKind == 'sqlserver') {
                this.welcomeDivShow = false;
                this.oracleDivShow = false;
                this.mysqlDivShow = false;
                this.sqlserverDivShow = true;
                this.postgresqlDivShow = false;
                this.welcomeBarStyle.background = "rgb(255,255,255)";
                this.mysqlBarStyle.background = "rgb(255,255,255)";
                this.oracleBarStyle.background = "rgb(255,255,255)";
                this.sqlServerBarStyle.background = "#F2F6FC";
                this.postgresqlBarStyle.background = "rgb(255,255,255)";
                //图片
                this.welcomeIconImg = ctx+"/assetss/images/v2/welcome-icon-unclick.png";
                this.oracleIconImg = ctx+"/assetss/images/v2/oracle-icon-unclick.png";
                this.mysqlIconImg = ctx+"/assetss/images/v2/mysql-icon-unclick.png";
                this.sqlserverIconImg = ctx+"/assetss/images/v2/sqlserver-icon-click.png";
                this.postgresqlIconImg = ctx+"/assetss/images/v2/postgresql-icon-unclick.png";

            }else if (sqlKind == 'postgresql') {
                this.welcomeDivShow = false;
                this.oracleDivShow = false;
                this.mysqlDivShow = false;
                this.sqlserverDivShow = false;
                this.postgresqlDivShow = true;
                this.welcomeBarStyle.background = "rgb(255,255,255)";
                this.mysqlBarStyle.background = "rgb(255,255,255)";
                this.oracleBarStyle.background = "rgb(255,255,255)";
                this.postgresqlBarStyle.background = "#F2F6FC";
                this.sqlServerBarStyle.background = "rgb(255,255,255)";
                //图片
                this.welcomeIconImg = ctx+"/assetss/images/v2/welcome-icon-unclick.png";
                this.oracleIconImg = ctx+"/assetss/images/v2/oracle-icon-unclick.png";
                this.mysqlIconImg = ctx+"/assetss/images/v2/mysql-icon-unclick.png";
                this.sqlserverIconImg = ctx+"/assetss/images/v2/sqlserver-icon-unclick.png";
                this.postgresqlIconImg = ctx+"/assetss/images/v2/postgresql-icon-click.png";

            }
            this.contentStyle.background = "#F2F6FC";

        },
        openMyWxQr(){
            let html = "<img style='width:300px;height: 300px;' src='"+ctx+"/assetss/images/v2/mywxqr.png'"+"/>";
            this.$alert(html, '扫一扫加我微信进技术群', {
                dangerouslyUseHTMLString: true,
                center: true,
                type: "success"
            });
        },
        generateDocHtml(sqlKind) {
            let ip;
            let port;
            let dataname;
            let username;
            let password;
            if (sqlKind == 'oracle') {
                ip = this.o_ip;
                port = this.o_port;
                dataname = this.o_dataname;
                username = this.o_username;
                password = this.o_password;
                //filepath = this.o_filepath;
            } else if (sqlKind == 'mysql') {
                console.log(this.m_ip);
                ip = this.m_ip;
                port = this.m_port;
                dataname = this.m_dataname;
                username = this.m_username;
                password = this.m_password;
            } else if (sqlKind == 'sqlserver') {
                ip = this.s_ip;
                port = this.s_port;
                dataname = this.s_dataname;
                username = this.s_username;
                password = this.s_password;
            }else if(sqlKind == 'postgresql'){
                ip = this.postgresql_ip;
                port = this.postgresql_port;
                dataname = this.postgresql_dataname;
                username = this.postgresql_username;
                password = this.postgresql_password;
            }

            if (ip == '') {
                this.$message.error('IP不能为空');
                return;
            }
            if (port == '') {
                this.$message.error('端口不能为空');
                return;
            }
            if (dataname == '') {
                this.$message.error('实例/数据库名称不能为空');
                return;
            }
            if (username == '') {
                this.$message.error('用户名不能为空');
                return;
            }
            if (password == '') {
                this.$message.error('密码不能为空');
                return;
            }
            window.open("/dbExport/v2/viewDocHtml?dbKind="+sqlKind+"&ip="+ip+"&port="+port+"&dbName="+dataname+"&userName="+username+"&password="+password);
        }
    }
});