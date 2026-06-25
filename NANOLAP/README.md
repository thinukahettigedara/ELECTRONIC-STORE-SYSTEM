# ⚡ NANOLAP - Electronic Items E-Commerce

Dark-themed Sri Lankan electronics store built with Spring Boot + Java + SSMS + HTML/CSS/JS.

---

## 🗂️ Project Structure

```
NANOLAP/
├── src/main/java/com/nanolap/
│   ├── NanolapApplication.java       # Main Spring Boot app
│   ├── model/
│   │   ├── Product.java
│   │   ├── Order.java
│   │   └── OrderItem.java
│   ├── repository/
│   │   ├── ProductRepository.java
│   │   └── OrderRepository.java
│   ├── service/
│   │   ├── ProductService.java
│   │   └── OrderService.java
│   ├── controller/
│   │   ├── ProductController.java    # REST API /api/products
│   │   ├── OrderController.java      # REST API /api/orders
│   │   └── PageController.java
│   └── config/
│       └── DataInitializer.java      # Auto loads sample products
│
├── src/main/resources/
│   ├── application.properties        # DB config here
│   └── static/
│       ├── index.html                # Homepage
│       ├── shop.html                 # Product listing
│       ├── cart.html                 # Cart + Checkout
│       ├── admin.html                # Admin panel
│       ├── css/style.css             # Dark theme styles
│       └── js/main.js               # Frontend JS
│
├── database-setup.sql                # Run this in SSMS first!
└── pom.xml
```

---

## 🚀 Setup Guide

### Step 1: Database Setup (SSMS)
1. Open **SQL Server Management Studio (SSMS)**
2. Connect to your SQL Server instance
3. Open `database-setup.sql`
4. Execute it (F5) — creates `NanolapDB` and all tables

### Step 2: Configure Database Connection
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=NanolapDB;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourPassword123
```
Replace `YourPassword123` with your actual SA password.

### Step 3: Run the Application
You need Java 17+ and Maven installed.

```bash
cd NANOLAP
mvn spring-boot:run
```

Or build a JAR:
```bash
mvn clean package
java -jar target/nanolap-1.0.0.jar
```

### Step 4: Open in Browser
- 🏠 **Homepage:** http://localhost:8080
- 🛍️ **Shop:** http://localhost:8080/shop.html
- 🛒 **Cart:** http://localhost:8080/cart.html
- 🔧 **Admin:** http://localhost:8080/admin.html

---

## 🌐 REST API Endpoints

### Products
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/products` | All products |
| GET | `/api/products?category=Laptops` | By category |
| GET | `/api/products?search=samsung` | Search |
| GET | `/api/products/featured` | Featured only |
| GET | `/api/products/{id}` | Single product |
| POST | `/api/products` | Add product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Soft delete |

### Orders
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/orders` | Place order |
| GET | `/api/orders` | All orders |
| GET | `/api/orders/{orderNumber}` | Single order |
| PUT | `/api/orders/{id}/status` | Update status |

---

## 💡 Features

- ⚡ Dark cyberpunk theme with neon cyan/purple accents
- 📱 Fully responsive (mobile + desktop)
- 🔍 Real-time product search
- 🛒 Cart with local storage persistence
- 📦 Order placement with backend integration
- 🔧 Admin panel (products + orders management)
- 🎨 12 sample products auto-loaded on first run
- ⭐ Product ratings, brands, categories
- 💰 Price with discount percentage display

---

## ⚙️ Requirements

- Java 17+
- Maven 3.6+
- SQL Server 2019+ (SSMS 20)
- Any modern browser

---

Built with ❤️ — NANOLAP © 2024
