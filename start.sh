#!/bin/bash

# git 拉取最新代码
git pull

# mvn 编译
mvn clean package -Dmaven.test.skip=true

# 启动jar包
nohup java -jar target/stock-1.0.jar &

