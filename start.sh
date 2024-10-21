#!/bin/bash


pid=`ps -ef|grep stock |grep -v grep  |awk '{print $2}'`

if [ -n "$pid" ]; then
  echo "Process with keyword Stock is running, killing process..."
  kill -9 "$pid"
  echo "Process killed."
else
  echo "No running process found with keyword Stock."
fi

# git 拉取最新代码
git pull

# mvn 编译
mvn clean package -Dmaven.test.skip=true

# 启动jar包
nohup java -jar target/stock-1.0.jar &

