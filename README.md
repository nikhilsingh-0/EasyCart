# EasyCart (E-Commerce Backend)

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.x-blue)
![Kafka](https://img.shields.io/badge/Kafka-Event--Driven-black)
![Redis](https://img.shields.io/badge/Redis-Caching-red)
![Microservices](https://img.shields.io/badge/Architecture-Microservices-blueviolet)

An **E-Commerce backend** built with **Spring Boot microservices**, featuring **API Gateway routing**, **JWT-based security**, **service discovery**, **Redis caching**, and **resilient communication**. The system supports **synchronous inter-service calls using OpenFeign** and **event-driven workflows with Kafka**, ensuring **high performance**, **fault tolerance**, and **scalability**.

---

## 🚀 Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Cloud
- Spring Security (JWT)
- Spring Data JPA
- Hibernate
- OpenFeign
- Kafka (Event-driven communication)

### Infrastructure
- API Gateway (Spring Cloud Gateway)
- Service Discovery (Eureka)
- Centralized Configuration (Config Server)
- MySQL
- Docker
---

## 🧩 Microservices Overview

| Service | Description |
|------|------------|
| **Config Server** | Centralized configuration management |
| **Service Registry** | Eureka for service discovery |
| **API Gateway** | Single entry point, routing, security, header validation |
| **User Service** | User registration, login, JWT generation,User profile management |
| **Product Service** | Product catalog, inventory management |
| **Order Service** | Order placement, order tracking |
| **Notification Service** | Async notifications via Kafka |

---

## 🏗️ High Level Architecture

<img width="781" height="521" alt="Easy_cart_PNG_HLD drawio (3)" src="https://github.com/user-attachments/assets/4ca39269-4d29-4396-8019-f383542dc127" />

---

## 🔐 Security

- JWT-based authentication & authorization
- Gateway-level security enforcement
- Services reject direct calls (only Gateway allowed)

---

## 📦 Key Features

- ✅ Microservice architecture
- ✅ Stateless JWT authentication
- ✅ API Gateway routing & filtering
- ✅ Inter-service communication using OpenFeign
- ✅ Event-driven communication with Kafka
- ✅ Centralized configuration
- ✅ Database per service
- ✅ Production-ready exception handling

---

## 📡 Inter-Service Communication

- **Synchronous** → OpenFeign
- **Asynchronous** → Kafka events

## 📈 Future Enhancements

- Distributed Tracing using Zipkin
- Payment gateway integration

---

## 👨‍💻 Author

**Nikhil Singh**  
Backend Developer | Java | Spring Boot | Microservices

## 🤝 Contributing 

If you like this project. Give it a ⭐ and feel free to **fork** or **contribute**!
