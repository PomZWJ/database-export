![](https://img-blog.csdnimg.cn/2021062719254559.png#pic_center)

**database-export V3.0.0**
=========================

![Spring Boot 2.0.6](https://img.shields.io/badge/Spring%20Boot-2.0.6-brightgreen.svg)
![Vue 2.0](https://img.shields.io/badge/Vue-2.0-green.svg)
![Element-UI 2.0](https://img.shields.io/badge/ElementUI-2.0-green.svg)
![Thymeleaf 3.0](https://img.shields.io/badge/Thymeleaf-3.0-yellow.svg)
![JDK 1.8](https://img.shields.io/badge/JDK-1.8-brightgreen.svg)
![Maven](https://img.shields.io/badge/Maven-3.5.0-yellowgreen.svg)

database-export是一款多线程生成数据库结构文档的开源springboot工程，能支持最新的数据库版本，可以导出docx格式和xlsx的文档，也能直接在网页上预览

项目使用技术
------------

* JDK1.8
* VUE2.0
* Element-UI
* Axios
* Thymeleaf
* Spring Boot
* Maven
* [POI-TL](http://deepoove.com/poi-tl)


项目特点
------------

* 导出sql支持多线程查询，导出速度更快
* 使用element-ui，界面更美观
* 支持导出word和excel，更支持网页预览
* 支持MySQL8.0版本,Oracle 11g以上以及sqlserver
* 导出速度高于现在的所有导出工具


How to use
------------




## 1.下载release下的最新打包的压缩包(推荐)


cmd执行jar -jar xxx.jar 即可启动

下载

|            |     WIN_NO_JRE                    |  WIN_WITH_JRE                           
| -------    |     :-----:                       |     :----:                              |
| V.1.0.0    | [database-export-1.0.0_noJre8.rar](https://github.com/PomZWJ/database-export/releases/download/1.0.0/database-export-1.0.0_noJre8.rar)  |   [database-export-1.0.0_withJre8.rar](https://github.com/PomZWJ/database-export/releases/download/1.0.0/database-export-1.0.0_withJre8.rar)    |
| V.2.0.0    | [database-export-2.0.0_noJre8.rar](https://github.com/PomZWJ/database-export/releases/download/2.0.0/database-export-2.0.0_noJre8.rar)      |   [database-export-2.0.0_withJre8.rar](https://github.com/PomZWJ/database-export/releases/download/2.0.0/database-export-2.0.0_withJre8.rar)    |
| V.2.1.0    | (后续版本都不含jre1.8,win和linux都是同一个包,不再区分)  https://pan.baidu.com/s/1A7EttMBIdVy3oAkFd0zo-w  提取码6gdr |
| V.2.2.0    | https://pan.baidu.com/s/1geYr9ksIXvf1R_xWiw_KDg  提取码kzmg |
| V.3.0.0    | https://pan.baidu.com/s/10U9pfj6No47WmCdjS-5daA  提取码oqep |


## 2.下载最新的源码启动

------------

**源码运行方法**
>执行DatabaseExportApplication.java即可

------------




## 3.运行访问的地址


> http://localhost:9999/dbExport/

> 旧的界面访问 http://localhost:9999/dbExport/v1
> (支持IE)



## 4.项目截图

**ORACLE**
![](https://img-blog.csdnimg.cn/20210627192751555.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1YW5ndXRhMTE3OA==,size_16,color_FFFFFF,t_70#pic_center)

**MYSQL**
![](https://img-blog.csdnimg.cn/20210627192804437.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1YW5ndXRhMTE3OA==,size_16,color_FFFFFF,t_70#pic_center)

**SQLServer**
![](https://img-blog.csdnimg.cn/20210627192817733.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1YW5ndXRhMTE3OA==,size_16,color_FFFFFF,t_70#pic_center)

**其他图片**
![](https://img-blog.csdnimg.cn/2021062719315467.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1YW5ndXRhMTE3OA==,size_16,color_FFFFFF,t_70#pic_center)

![](https://img-blog.csdnimg.cn/202106271932216.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1YW5ndXRhMTE3OA==,size_16,color_FFFFFF,t_70#pic_center)

![](https://img-blog.csdnimg.cn/20210627193227925.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1YW5ndXRhMTE3OA==,size_16,color_FFFFFF,t_70#pic_center)


## 5.想加入技术开发群的加我，备注加群即可
<img src="https://img-blog.csdnimg.cn/2021062719334713.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1YW5ndXRhMTE3OA==,size_16,color_FFFFFF,t_70#pic_center" width="300" height="300"/>

## 6.更新日志

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

- [submit issue](https://github.com/PomZWJ/database-export/issues/new)
- E-mail: 1513041820@qq.com
- WX:ZWJ1513041820

