TITLE dr port 9999
set os=package
set osl=%os%\lib
set osr=%os%\resources
java -Xmx512M -Xms512M -classpath ".;%osl%;%osr%;;%os%/database-export-1.0.0-RELEASE.jar"  com.pomzwj.DatabaseExportApplication

pause