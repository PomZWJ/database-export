#!/bin/sh
java -jar /database-export-x.x.x.jar
echo $! > /var/run/database-export.pid