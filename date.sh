#!/bin/bash

# 获取当前日期的星期几（0为周日，1为周一，以此类推）
weekday=$(date +%w)

# 判断如果是周六（6）或周日（0），则不执行任务
if [ "$weekday" -eq 6 ] || [ "$weekday" -eq 0 ]; then
    echo "今天是周末，不执行任务。"
    exit 1
else
    echo "执行任务..."
    # 在这里添加您的任务代码
fi
