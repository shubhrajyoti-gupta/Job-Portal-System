# 🚀 Job Portal Backend System

A secure, role-based REST API for a modern job portal built with **Spring Boot**, **JWT Authentication**, and **MySQL**. Designed with enterprise-grade security, data integrity, and scalability in mind.

> 💼 Built to demonstrate full-stack backend engineering skills — ideal for roles at firms like **Deloitte**, **Accenture**, and **Infosys**.

---

## 🔐 Features

- **Role-Based Access Control (RBAC)**  
  - `Admin`: Manage users & moderate content  
  - `Recruiter`: Post jobs & view applicants  
  - `Candidate`: Browse jobs & apply securely  

- **Secure Authentication**  
  - JWT-based stateless auth  
  - BCrypt password hashing  
  - Role validation on every endpoint  

- **Data Integrity & Safety**  
  - Prevents duplicate job applications using **database-level unique constraints**  
  - Foreign key relationships with cascade delete  
  - Input validation & global exception handling  

- **Developer Experience**  
  - Pagination & filtering (`?page=0&size=10&location=Remote`)  
  - Comprehensive unit tests (JUnit + Mockito)  
  - Postman-ready REST endpoints  

---

## 🛠️ Tech Stack

| Layer          | Technology                     |
|----------------|-------------------------------|
| **Language**   | Java 17                       |
| **Framework**  | Spring Boot 3.2               |
| **Security**   | Spring Security + JWT         |
| **Database**   | MySQL 8                       |
| **ORM**        | JPA / Hibernate               |
| **Testing**    | JUnit 5, Mockito              |
| **API Client** | Postman                       |
| **Build Tool** | Maven                         |

---

## 📁 Project Structure

```text
src/
├── main/
│   ├── java/com/example/jobportal/
│   │   ├── config/         # Security & Web config
│   │   ├── controller/     # REST endpoints
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── exception/      # Global exception handler
│   │   ├── repository/     # Database interfaces
│   │   ├── security/       # JWT & Auth logic
│   │   └── service/        # Business logic
│   └── resources/
│       └── application.properties   # DB & JWT config
│
└── test/
    └── java/com/example/jobportal/service/
        └── *.java          # Unit tests (Mockito)
```




---

## ⚙️ Setup & Run Locally

### Prerequisites
- JDK 17
- MySQL 8+
- IntelliJ IDEA (or any Java IDE)

### Steps

1. **Create MySQL Database**
   ```sql
   CREATE DATABASE jobportal;

2. **Initialize Database Schema**
Run the following SQL script in your MySQL client:

```sql
USE jobportal;

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    INDEX idx_email (email)
);

CREATE TABLE jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    company VARCHAR(100),
    location VARCHAR(100),
    recruiter_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (recruiter_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    candidate_id BIGINT NOT NULL,
    status ENUM('PENDING','ACCEPTED','REJECTED') DEFAULT 'PENDING',
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_application (job_id, candidate_id),
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO roles (name) VALUES 
('ROLE_ADMIN'),
('ROLE_RECRUITER'),
('ROLE_CANDIDATE');
```

---

## 🌐 API Endpoints

| Endpoint                                   | Method | Role      |
|--------------------------------------------|--------|-----------|
| `/api/auth/register`                       | POST   | Public    |
| `/api/auth/login`                          | POST   | Public    |
| `/api/candidate/jobs`                      | GET    | Candidate |
| `/api/candidate/apply/{id}`                | POST   | Candidate |
| `/api/recruiter/jobs`                      | POST   | Recruiter |
| `/api/recruiter/jobs/{id}/applicants`      | GET    | Recruiter |
| `/api/admin/jobs/{id}`                     | DELETE | Admin     |


---

## 📬 Contact
Built by Shubhrajyoti Gupta 
- [LinkedIn ↗️](https://www.linkedin.com/in/shubhrajyoti-gupta) | shubhrajyotigupta2005@gmail.com
