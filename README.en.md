# Stock Information Management System

## Project Overview
This is a stock information management system built on Spring Boot, providing real-time query, monitoring, and analysis functionalities for stock data. Key features include:

- Real-time stock quote queries
- Stock K-line data visualization
- Custom watchlist monitoring
- Stock anomaly alerts
- WeChat message notifications

## Technology Stack
The project utilizes a mainstream Java technology stack:
- Spring Boot 2.x
- MyBatis Plus
- Elasticsearch
- WeChat Official Account API integration
- Scheduled task scheduling

## Main Modules
1. **Stock Data Collection**: Fetches real-time stock data from sources such as Tencent and Sina Finance
2. **Data Persistence**: Uses MyBatis Plus for database operations
3. **Search Functionality**: Integrates Elasticsearch for stock information search
4. **WeChat Notifications**: Sends stock anomaly alerts via WeChat Official Account
5. **Scheduled Tasks**: Automatically updates stock data and monitors abnormal price fluctuations

## API Documentation

### Stock Information API
- `GET /stock/index`: Paginated query of stock information
- `GET /stock/add/{stockSymbol}`: Add a stock to watchlist
- `DELETE /stock/delete/{stockSymbol}`: Remove a stock from watchlist
- `GET /stock/update`: Update stock data
- `GET /stock/kline/{symbol}`: Retrieve stock K-line data
- `POST /stock/current-price`: Batch retrieve real-time stock prices

### Watchlist API
- `GET /api/watchlist/list`: Retrieve the watchlist

### WeChat API
- `GET /wx/sendMessage`: Send a WeChat message
- `GET /wx/weChatToken`: Validate WeChat token

## Database Design
Main database tables include:
- `stock_info`: Basic stock information
- `stock_info_dto`: Stock statistical data
- `stock_watchlist`: Custom watchlist
- `stock_change`: Stock anomaly records

## Usage Instructions
1. Configure database connection and WeChat parameters in `application.yml`
2. Execute SQL scripts to create database tables
3. Start the project and access it via the provided API endpoints

## Notes
- WeChat Official Account token validation must be configured
- Stock data sources require stable network connectivity
- Scheduled task frequency can be adjusted according to requirements

## Contribution Guidelines
Contributions are welcome! Please follow these steps:
1. Fork the repository
2. Create a new branch
3. Commit your changes
4. Open a Pull Request

## License
This project is licensed under the Apache 2.0 License. See the LICENSE file for details.