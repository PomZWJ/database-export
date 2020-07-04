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

------------

**windows打包方法**
>mvn clean package -f pom-win.xml 
然后将target下的lib、resources、database-export-x.x.x-RELEASE.jar提取出来，放到windows服务器上，运行\startup-script\start-dbexport-win.bat(需要自行调整)脚本即可



**linux打包方法**
>mvn clean package -f pom-linux.xml
然后将target下的lib、resources、database-export-x.x.x.jar提取出来，放到linux服务器上，运行\startup-script\start-dbexport-linux.sh(需要自行调整)脚本即可



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



Discussing
----------

- [submit issue](https://github.com/PomZWJ/database-export/issues/new)
- E-mail: 1513041820@qq.com
- WX:ZWJ1513041820

