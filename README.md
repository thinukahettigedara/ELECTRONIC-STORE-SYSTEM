⚡ NANOLAP — Electronics E-Commerce System
A full-stack web application that allows customers to browse electronic products, add items to cart, and place orders online.
The system also includes an admin panel for managing products, inventory, and customer orders efficiently.

🚀 Features
👤 Customer Features


Browse electronic products (smartphones, laptops, audio, tablets, TVs, etc.)
Search and filter products by category
View featured and latest arrivals
Register and log in as a customer
Manage profile (name, phone, address) from a customer dashboard
Add items to cart
Place orders online and view past order history
Easy and responsive user interface


🛠️ Admin Features


Add, update, and soft-delete products
Manage stock quantity and product availability
View all customer orders
Update order status (e.g. PENDING → SHIPPED → DELIVERED)


🧑‍💻 Tech Stack


Java 17 + Spring Boot 3.2 (Spring Web, Spring Data JPA, Validation)
Thymeleaf (templating engine)
Microsoft SQL Server (primary database via SSMS)
H2 (in-memory database, for local debugging)
HTML / CSS / JavaScript (static frontend, no framework)
Maven (build tool)


📊 Languages Used

LanguagePurposeJavaBackend logic — models, repositories, services, REST controllersHTMLStatic frontend pages (homepage, shop, cart, login, dashboard, admin)CSSStyling for all static pagesJavaScriptFrontend interactivity (cart, search, API calls)SQLDatabase schema setup (database-setup.sql)PropertiesApp configuration (application.properties)

📱 Installation

# Clone the repository
git clone https://github.com/thinukahettigedara/NANOLAP.git

# Navigate to project folder
cd NANOLAP

# Set up the database
# Open database-setup.sql in SQL Server Management Studio (SSMS) and run it (F5)

# Configure your DB credentials in:
# src/main/resources/application.properties

# Run the project
mvn spring-boot:run

The app starts on http://localhost:8080

Default demo accounts (auto-seeded on first run):

RoleEmailPasswordAdminadmin@nanolap.lkadmin123Customercustomer@nanolap.lkcustomer123

📂 Project Structure

NANOLAP/
├── src/main/java/com/nanolap/
│   ├── NanolapApplication.java       # Main Spring Boot app
│   ├── model/                        # Product, User, Order, OrderItem
│   ├── repository/                   # Spring Data JPA repositories
│   ├── service/                      # ProductService, OrderService
│   ├── controller/                   # REST controllers + page routing
│   └── config/
│       └── DataInitializer.java      # Auto-loads sample products & users
│
├── src/main/resources/
│   ├── application.properties        # DB config
│   └── static/
│       ├── index.html                # Homepage
│       ├── shop.html                 # Product listing
│       ├── cart.html                 # Cart + checkout
│       ├── login.html                # Login / registration
│       ├── customer-dashboard.html   # Customer profile & order history
│       ├── admin.html                # Admin panel
│       ├── css/style.css
│       └── js/main.js
│
├── database-setup.sql                # Run this in SSMS first
└── pom.xml

🌐 REST API Endpoints

Auth

MethodURLDescriptionPOST/api/auth/registerRegister a new userPOST/api/auth/loginLog inPUT/api/auth/profileUpdate profile

Products

MethodURLDescriptionGET/api/productsAll active productsGET/api/products?category=LaptopsFilter by categoryGET/api/products?search=samsungSearch productsGET/api/products/featuredFeatured productsGET/api/products/latestLatest productsGET/api/products/categoriesList all categoriesGET/api/products/{id}Single productPOST/api/productsAdd product (admin)PUT/api/products/{id}Update product (admin)DELETE/api/products/{id}Soft-delete product (admin)

Orders

MethodURLDescriptionPOST/api/ordersPlace order (logged-in customer)GET/api/ordersAll orders (admin)GET/api/orders/{orderNumber}Single orderGET/api/orders/my-orders?email=A customer's order historyPUT/api/orders/{id}/statusUpdate order status (admin)

🎯 Future Improvements


Online payment integration
Stronger authentication (JWT + hashed passwords instead of the current demo-grade token/plaintext check)
Wishlist feature
Customer-facing order tracking with status notifications
Product reviews/ratings submitted by real customers


⚠️ Notes for Production Use


application.properties currently has live DB credentials checked in — move these to environment variables before deploying or making the repo public.
Login uses a simple in-memory token (simple-token-{id}) and plaintext password comparison; swap in Spring Security + BCrypt + JWT before going live.


🤝 Developed By


Thinuka Hettigedara
