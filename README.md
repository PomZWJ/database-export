![](https://github.com/PomZWJ/database-export/blob/master/screenshot/fav.png?raw=true)

**database-export**
=========================

![Spring Boot 2.0.6](https://img.shields.io/badge/Spring%20Boot-2.0-brightgreen.svg)
![Thymeleaf 3.0](https://img.shields.io/badge/Thymeleaf-3.0-yellow.svg)
![JDK 1.8](https://img.shields.io/badge/JDK-1.8-brightgreen.svg)
![Maven](https://img.shields.io/badge/Maven-3.5.0-yellowgreen.svg)

database-export是一款能生成数据库结构文档的开源springboot工程，能支持最新的数据库版本，可以导出支持office2007版本以上的docx格式的文档

[github地址](https://github.com/PomZWJ/database-export)

项目使用技术
------------

* Bootstrap
* jQuery
* Thymeleaf
* Spring Boot
* Spring Boot Mail
* [POI-TL](http://deepoove.com/poi-tl)

How to use
------------
1.下载最新的源码启动(推荐)

**注意:生成oracle文档的时候，可能需要先把oracle的jar打包到你本地的maven库，不然会报错，找不到驱动程序**

**注:Oracle的jar包在最新的release下的打包文件里面的`package\lib\ojdbc14.jar`**


2.下载release下的最新打包的压缩包,启动run.bat(win)即可,生成的文档在D盘下，也可以自行修改生成文件的盘符，在`package\resources\application.yml`文件
![](https://github.com/PomZWJ/database-export/blob/master/screenshot/index.png?raw=true)

**注意:release打包的程序已经包含oracle的jar包，所以在lib下无需再引入oracle的jar**

Discussing
----------

- [submit issue](https://github.com/PomZWJ/database-export/issues/new)
- E-mail: 1513041820@qq.com
- WX:ZWJ1513041820

