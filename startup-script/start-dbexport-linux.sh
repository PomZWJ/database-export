#!/bin/sh
java -Dloader.path="lib,resources" -jar /usr/local/database-export-x.x.x.jar
echo $! > /var/run/database-export.pid