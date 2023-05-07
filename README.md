**[转到Gitee](https://gitee.com/pomz/database-export)**

**[转到Github](https://github.com/PomZWJ/database-export)**

![](https://img-blog.csdnimg.cn/2021062719254559.png#pic_center)

**database-export V3.4.0**
=========================

![Spring Boot 2.6.3](https://img.shields.io/badge/Spring%20Boot-2.6.3-brightgreen.svg)
![Element-UI 2.0](https://img.shields.io/badge/ElementUI-2.0-green.svg)
![POI-TL](https://img.shields.io/badge/POITL-1.12-brightgreen.svg)
![MySQL](https://img.shields.io/badge/MySQL-8-blue.svg)
![ORACLE](https://img.shields.io/badge/ORACLE-11g-red.svg)
![SqlServer](https://img.shields.io/badge/SqlServer-2008-red.svg)
![Postgresql](https://img.shields.io/badge/Postgresql-14-blue.svg)
![Clickhouse](https://img.shields.io/badge/Clickhouse-22-yellow.svg)
![Sqlite](https://img.shields.io/badge/sqlite-3-blue.svg)

database-export是一款多线程生成数据库结构文档的开源springboot工程，能支持最新的数据库版本，可以导出docx格式和xlsx的文档，也能直接在网页上预览


现已支持的数据库
------------
* MySQL
* ORACLE
* SqlServer
* Postgresql
* Clickhouse
* Sqlite3


现已支持导出的类型
------------
* word
* excel
* markdown

项目特点
------------

* 导出sql支持多线程查询，导出速度更快
* 使用element-ui，界面更美观
* 支持导出word和excel，更支持网页预览
* 导出速度高于现在的所有导出工具

想加入技术开发群的加我，备注加群即可(定制模板请备注定制模板)
----------

<img width="300px" height="300px" alt="加群" src="src/main/resources/static/assetss/images/v2/mywxqr.png"/>

支持一下我
----------
<img width="300px" height="400px" alt="加群" src="src/main/resources/static/assetss/images/wechat_pay.png"/>
<img width="300px" height="400px" alt="加群" src="src/main/resources/static/assetss/images/ali_pay.jpg"/>


How to use
------------




## 1.下载release下的最新打包的压缩包(推荐)


cmd/shell执行java -jar xxx.jar 即可启动


## 2.下载最新的源码启动

------------

**源码运行方法**
>执行DatabaseExportApplication.java即可





## 3.运行访问的地址


> http://localhost:9999/dbExport/

> 旧的界面访问 http://localhost:9999/dbExport/v1
> (支持IE)



## 4.项目截图

**首页**
![](https://img-blog.csdnimg.cn/73b2f2a8c7484bcca3b596d307240ca5.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5oOK5Ye65bCY,size_20,color_FFFFFF,t_70,g_se,x_16)

**ORACLE**
![](https://img-blog.csdnimg.cn/8a2df0c45c994b6b93926f1c39c1b7df.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5oOK5Ye65bCY,size_20,color_FFFFFF,t_70,g_se,x_16)

**MySQL**
![](https://img-blog.csdnimg.cn/f285524f4ef749e4a8a6dc495d698a09.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5oOK5Ye65bCY,size_20,color_FFFFFF,t_70,g_se,x_16)

**SQLServer**
![](https://img-blog.csdnimg.cn/83bb44a0563d40a19976121e9e5e5240.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5oOK5Ye65bCY,size_20,color_FFFFFF,t_70,g_se,x_16)

**其他**
![](https://img-blog.csdnimg.cn/509f9ed6bb8b4fc4b61f06bb8b47340e.png#pic_center)


**其他图片**
![](https://img-blog.csdnimg.cn/2021062719315467.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1YW5ndXRhMTE3OA==,size_16,color_FFFFFF,t_70#pic_center)

![](https://img-blog.csdnimg.cn/202106271932216.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1YW5ndXRhMTE3OA==,size_16,color_FFFFFF,t_70#pic_center)

![](https://img-blog.csdnimg.cn/20210627193227925.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1YW5ndXRhMTE3OA==,size_16,color_FFFFFF,t_70#pic_center)

## 5.更新日志

**2022-10-29(V3.4.0)**

>1.新增支持Sqlite

>2.导出支持Markdown

**2022-10-27(V3.3.0)**

>1.新增支持ClickHouse

>2.修复已知bug

**2022-03-27(V3.2.0)**

>1.支持Postgresql

>2.修复已知BUG



**2021-12-16(V3.1.0)**

>1.数据库连接池改成工厂模式，引入了druid和hikaricp两种连接池，可在yml自定义配置

>2.前端html预览美化，加入了导航

>3.修复已知BUG


**2021-06-27(V3.0.0)**

>1.查询数据库的时候加入了多线程查询,使得导出速度更快

>2.抛弃了之前使用的原生JDBC连接,引入了druid和JdbcTemplate

>3.支持导出excel

>4.支持前端html预览

**2021-06-05(V2.2.0)**

>1.改变java设计模式为工厂模式，方便用户自己扩展

>2.优化了后台代码,尽量把代码实现高度配置化

>3.BUG改进

**2020-07-15(V2.1.0)**

>1.去除了打包后，application需要重新配置模板文件,使部署更简单，不需要在改动到配置文件

>2.去除了自定义文件生成目录

>3.让生成的文件能够下载

>4.BUG改进

**2019-09-26(V2.0.0)**

>1.使用了VUE+Element-UI重新修改了界面，抛弃了原本的Bootstrap

>2.BUG改进

**2019-04-20(V1.0.0)**

>1.使用springboot2.x+Bootstrap完成界面

>2.能导出docx类型的文档,支持mysql,oracle,sql server

Discussing
----------
- [CSDN](https://blog.csdn.net/huanguta1178/article/details/83690318)
- [submit issue](https://github.com/PomZWJ/database-export/issues/new)

