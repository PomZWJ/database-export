![](https://github.com/PomZWJ/database-export/blob/master/screenshot/fav.png?raw=true)

**database-export V2.1.0**
=========================

![Spring Boot 2.0.6](https://img.shields.io/badge/Spring%20Boot-2.0.6-brightgreen.svg)
![Vue 2.0](https://img.shields.io/badge/Vue-2.0-green.svg)
![Element-UI 2.0](https://img.shields.io/badge/ElementUI-2.0-green.svg)
![Thymeleaf 3.0](https://img.shields.io/badge/Thymeleaf-3.0-yellow.svg)
![JDK 1.8](https://img.shields.io/badge/JDK-1.8-brightgreen.svg)
![Maven](https://img.shields.io/badge/Maven-3.5.0-yellowgreen.svg)

database-export是一款能生成数据库结构文档的开源springboot工程，能支持最新的数据库版本，可以导出支持office2007版本以上的docx格式的文档


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



## 2.下载最新的源码启动

------------

**windows打包运行方法**
>运行mvn clean package
运行java -jar database-export-x.x.x-RELEASE.jar即可



**linux打包方法**
>运行mvn clean package
运行java -jar database-export-x.x.x-RELEASE.jar即可

**linux后台运行方法**
>请把start-dbexport-linux.sh放到和jar同一个位置,并使用
>nohup ./start-dbexport-linux.sh & 
>启动
(如果遇到提示说start-dbexport-linux.sh is not directoryxxx,需要用到dos2unix转化，可自行百度)


------------




## 3.运行访问的地址


> http://localhost:9999/dbExport/



## 4.项目截图

**ORACLE**
![](https://github.com/PomZWJ/database-export/blob/master/screenshot/v2/indexv2-1.png?raw=true)

**MYSQL**
![](https://github.com/PomZWJ/database-export/blob/master/screenshot/v2/indexv2-2.png?raw=true)

**SQL**
![](https://github.com/PomZWJ/database-export/blob/master/screenshot/v2/indexv2-3.png?raw=true)

## 5.想加入技术开发群的加我，备注加群即可
<img src="https://raw.githubusercontent.com/PomZWJ/colornote-vue/master/screenshot/wx_icon.jpg" width="300" height="300"/>

## 6.更新日志


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

