# 股票信息管理系统

## 项目简介
这是一个基于Spring Boot的股票信息管理系统，提供股票数据的实时查询、监控和分析功能。主要特性包括：

- 股票实时行情查询
- 股票K线数据展示
- 自选股监控列表
- 股票异动信息推送
- 微信消息通知

## 技术架构
项目采用主流Java技术栈：
- Spring Boot 2.x
- MyBatis Plus
- Elasticsearch
- 微信公众号API集成
- 定时任务调度

## 主要模块
1. **股票数据采集**：从腾讯、新浪财经等渠道获取实时股票数据
2. **数据持久化**：使用MyBatis Plus进行数据库操作
3. **搜索功能**：集成Elasticsearch实现股票信息搜索
4. **微信通知**：通过微信公众号推送股票异动提醒
5. **定时任务**：自动更新股票数据和监控异常波动

## API接口文档

### 股票信息接口
- `GET /stock/index`：分页查询股票信息
- `GET /stock/add/{stockSymbol}`：添加自选股
- `DELETE /stock/delete/{stockSymbol}`：删除自选股
- `GET /stock/update`：更新股票数据
- `GET /stock/kline/{symbol}`：获取股票K线数据
- `POST /stock/current-price`：批量获取实时股价

### 自选股接口
- `GET /api/watchlist/list`：获取自选股列表

### 微信接口
- `GET /wx/sendMessage`：发送微信消息
- `GET /wx/weChatToken`：微信token验证

## 数据库设计
主要包含以下数据表：
- stock_info：股票基本信息
- stock_info_dto：股票统计信息
- stock_watchlist：自选股列表
- stock_change：股票异动记录

## 使用说明
1. 配置application.yml中的数据库连接和微信参数
2. 执行SQL脚本创建数据库表
3. 启动项目后，通过提供的API接口进行访问

## 注意事项
- 需要配置微信公众号的token验证
- 股票数据来源需要保持网络连通性
- 定时任务执行频率可根据需求调整

## 贡献指南
欢迎贡献代码，请遵循以下步骤：
1. Fork仓库
2. 创建新分支
3. 提交代码修改
4. 创建Pull Request

## 许可证
本项目采用Apache 2.0许可证，详细请参阅LICENSE文件。