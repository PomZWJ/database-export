# 该镜像需要依赖的基础镜像
FROM java:8
# 指定维护者的名字
MAINTAINER PomZWJ
# 设置工作目录，进入到容器中的初始目录,不存在会自动创建
ENV MYPATH /home/pomzwj/database-export
WORKDIR $MYPATH
# 将当前目录下的jar包复制到docker容器的/目录下
COPY target/database-export-4.0.0.jar database-export.jar
# 对外端口号 jar包运行的端口号
EXPOSE 9999
# 运行cmd命令，会在启动容器时运行。
CMD java -jar database-export.jar
