![](https://github.com/PomZWJ/database-export/blob/master/screenshot/fav.png?raw=true)

**database-export V2.0**
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


启动run.bat(win)即可(打包的暂时只支持windows环境，linux可自行打包)

下载

|            |     WIN_NO_JRE                    |  WIN_WITH_JRE                           |  LINUX_NO_JRE | LINUX_WITH_JRE
| -------    |     :-----:                       |     :----:                              |    :-----:    |  :-----:      |  
| V.1.0.0    | [database-export-1.0.0_noJre8.rar](https://github.com/PomZWJ/database-export/releases/download/1.0.0/database-export-1.0.0_noJre8.rar)  |   [database-export-1.0.0_withJre8.rar](https://github.com/PomZWJ/database-export/releases/download/1.0.0/database-export-1.0.0_withJre8.rar)    |
| V.2.0.0    | [database-export-2.0.0_noJre8.rar](https://github.com/PomZWJ/database-export/releases/download/2.0.0/database-export-2.0.0_noJre8.rar)      |   [database-export-2.0.0_withJre8.rar](https://github.com/PomZWJ/database-export/releases/download/2.0.0/database-export-2.0.0_withJre8.rar)    |

生成的文件名称是export.docx，如果需要修改，在`package\resources\application.yml`文件中，按照注释修改即可



## 2.下载最新的源码启动


**注意:在运行项目前，可能需要先把oracle的jar打包到你本地的maven库，不然会报错，找不到驱动程序**
生成的文件名称是export.docx，如果需要修改，在`resources\application.yml`文件中，按照注释修改即可


------------
**(如果你不需要使用到oracle数据库，此步骤可忽略，直接注释在pom.xml里面的oracle依赖即可)**

oracle jar的地址在源码的extrajar/ojdbc7-12.1.0.2.jar下

打包到本地仓库的命令是
>mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0.2 -Dpackaging=jar -Dfile=D:\ojdbc7-12.1.0.2.jar -DgeneratePom=true

>注意

自行使用maven打包的时候，可能会遇到以下BUG
>[1.SpringBoot分离lib和resources打包后，手动添加oracle驱动到lib后，运行时一直提示无法找到驱动程序的解决办法](https://blog.csdn.net/huanguta1178/article/details/101374286)


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




Discussing
----------

- [submit issue](https://github.com/PomZWJ/database-export/issues/new)
- E-mail: 1513041820@qq.com
- WX:ZWJ1513041820

