# E-Scooter Rental System
This project is a comprehensive e-scooter rental system consisting of three main components:
- Backend Service (Cloud-based)
- User Mobile Application
- Admin Management System

## Source Code

The complete source code for this project is available on GitHub:
- Repository: [https://github.com/Zhoujiayi-Ryann/escooter](https://github.com/Zhoujiayi-Ryann/escooter)
- Language Distribution:
  - Java: 43.6%
  - Vue: 37.2%
  - TypeScript: 13.6%
  - Less: 2.5%
  - HTML: 1.8%
  - JavaScript: 0.8%
  - Other: 0.5%

## System Architecture

### Backend Service (Cloud-based)
The backend service is deployed on Sealos Cloud infrastructure and provides RESTful APIs for both the user mobile application and admin system.

- **Deployment Information**:
  - Platform: Sealos Cloud DevBox
  - Image Source: hub.hzh.sealos.run/ns-x3h6hlzn/escooter-backend
  - Environment: Spring Boot 3.3.2
  - Creation Time: 2025-03-08 10:56
  - Running Time: 34m (varies)

- **Resource Specifications**:
  - CPU Limit: 1 Core
  - Memory Limit: 2 G
  - Current CPU Usage: 0.35%
  - Current Memory Usage: 13.47%

- **Network Configuration**:
  - Internal Access:
    - URL: http://escooter-backend.ns-x3h6hlzn.svc.cluster.local:8080
    - Port: 8080
  - External Access:
    - URL: https://aadujkrrrwxp.sealoshzh.site
    - SSL/TLS Enabled

- **SSH Access**:
  - Connection String: ssh -i hzh.sealos.run_ns-x3h6hlzn_escooter-backend devbox@hzh.sealos.run -p 33332

- **Version History**:
  - v1.7: Bug fixes for order duration calculation
  - v1.6: HTTPS implementation
  - v1.5: Performance optimization

- **Technology Stack**:
  - Spring Boot
  - MySQL Database (Cloud-hosted)
  - Maven
  - JWT Authentication
  - MyBatis

- **Key Features**:
  - User Authentication & Authorization
  - Order Management
  - E-scooter Status Management
  - Payment Processing
  - Notification System
  - Data Analytics

- **Environment Setup**:
  1. Install JDK 17 or later
  2. Install Maven
  3. Configure application.properties:
     ```properties
     # Production environment (Sealos Cloud)
     spring.profiles.active=prod
     
     # API Endpoints
     server.port=8080
     
     # Internal service URL (within Sealos Cloud)
     service.internal.url=http://escooter-backend.ns-x3h6hlzn.svc.cluster.local:8080
     
     # External service URL
     service.external.url=https://aadujkrrrwxp.sealoshzh.site
     ```
  4. Important Notes for Development:
     - Request access to Sealos Cloud DevBox
     - Configure your local timezone to Asia/Shanghai
     - Ensure your JWT secret matches the production environment
     - Use the provided SSH key for direct access

### User Mobile Application (Frontend)
A mobile application for end users to rent and manage e-scooters.

- **Technology Stack**:
  - uni-app
  - Vue.js
  - TypeScript
  - Vite

- **Key Features**:
  - User Registration & Login
  - E-scooter Rental
  - Real-time Location Tracking
  - Payment Integration
  - Order History
  - User Profile Management

- **Installation & Setup**:
  1. Install Node.js (v16 or later)
  2. Install HBuilderX IDE
  3. Clone the frontend repository
  4. Install dependencies:
     ```bash
     npm install
     ```
  5. Configure API endpoints in the environment file
  6. For development:
     ```bash
     npm run dev
     ```
  7. For production build:
     ```bash
     npm run build
     ```

### Admin Management System
A web-based dashboard for company staff to monitor and manage the e-scooter fleet.

- **Technology Stack**:
  - React
  - TypeScript
  - Ant Design Pro
  - Umi.js

- **Key Features**:
  - Dashboard Analytics
  - Order Management
  - User Management
  - E-scooter Fleet Management
  - System Configuration
  - Staff Access Control

- **Installation & Setup**:
  1. Install Node.js (v16 or later)
  2. Clone the admin frontend repository
  3. Install dependencies:
     ```bash
     npm install
     ```
  4. Configure environment variables:
     ```bash
     # .env
     API_URL=https://your-cloud-api-endpoint
     ```
  5. For development:
     ```bash
     npm start
     ```
  6. For production build:
     ```bash
     npm run build
     ```

## Important Notes

### Timezone Configuration
- All servers and applications should be configured to use Asia/Shanghai timezone
- Database timestamps are stored in Asia/Shanghai timezone
- Frontend applications should handle timezone conversion appropriately

### Cloud Infrastructure
- **Platform**: Sealos Cloud DevBox
- **Database Service**: 
  - Type: apecloud-mysql
  - Version: ac-mysql-8.0.31
  - Internal Connection:
    - Host: escoot-mysql.ns-x3h6hlzn.svc
    - Port: 3306
  - External Connection:
    - Host: dbconn.sealoshzh.site
    - Port: 47581
  - Database Name: escoot

- **Database Configuration**:
  ```properties
  # Internal access (within Sealos Cloud)
  spring.datasource.url=jdbc:mysql://escoot-mysql.ns-x3h6hlzn.svc:3306/escoot?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
  
  # External access
  spring.datasource.url=jdbc:mysql://dbconn.sealoshzh.site:47581/escoot?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
  ```

- **Resource Specifications**:
  - CPU: 1 Core
  - Memory: 1 Gi
  - Storage: 1 Gi

### Development Setup

1. **Database Access**:
   - Request database credentials from administrator
   - Default username: root
   - Use the provided password
   - Configure your database client with the external connection details

2. **Local Development**:
   - Use the external database connection string for local development
   - Ensure your IP is whitelisted if required
   - Test connection before starting development

3. **Environment Variables**:
   ```properties
   # Required environment variables for database connection
   DB_HOST=dbconn.sealoshzh.site
   DB_PORT=47581
   DB_NAME=escoot
   DB_USERNAME=root
   DB_PASSWORD=[contact administrator]
   ```

### Security
- All API endpoints are protected with JWT authentication
- Sensitive data is encrypted
- HTTPS is enforced for all communications
- Rate limiting is implemented on API endpoints

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/Zhoujiayi-Ryann/escooter.git
   cd escooter
   ```
2. Request access to the cloud infrastructure (contact system administrator)
3. Set up your development environment following the instructions above
4. Configure your timezone settings correctly
5. Test the connection to cloud services
6. Start with the development

