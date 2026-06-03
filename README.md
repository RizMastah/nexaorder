# NexaOrder

NexaOrder is a web-based order management system for small-business product, inventory, order, payment, notification, invoice, and report workflows. It is built as a Spring Boot + Thymeleaf dashboard application with clear package separation and visible design pattern implementations.

## Tech Stack

- Java, Spring Boot, Spring MVC, Spring Data JPA
- MySQL, Maven, Lombok, Bean Validation
- Thymeleaf, Tailwind CSS, Flowbite, Alpine.js, Chart.js
- Custom `HttpSession` authentication and role-based route protection

## Features

- Admin and customer login/register/logout
- Admin user account overview
- Admin product CRUD with image upload
- Inventory adjustment and inventory logs
- Customer product browsing and product detail pages
- Customer order creation, order history, order detail, and pending cancellation
- Simulated payment flow for E-Wallet, Bank Transfer, and Cash on Delivery
- Admin bank-transfer payment confirmation
- State-controlled order transitions: `PENDING -> PAID -> PROCESSING -> COMPLETED`, with `PENDING -> CANCELLED`
- Automatic notifications and safe inventory stock reduction when orders become `PAID`
- Admin report dashboard with Chart.js charts and CSV export
- Customer and admin invoice pages with print support

## Design Patterns

- MVC Pattern: Spring MVC controllers, services, repositories, and Thymeleaf views
- Repository Pattern: Spring Data JPA repositories for database access
- DTO Pattern: request/response DTOs for forms and page data
- Strategy Pattern: payment behavior per payment method
- Factory Method Pattern: `PaymentFactory` selects the correct payment strategy
- State Pattern: order status transitions are controlled by `OrderState` classes
- Observer Pattern: order status events trigger notification and inventory observers
- Singleton Pattern: Spring services, components, and repositories are singleton beans by default

## Demo Accounts

The app seeds these accounts if they do not already exist:

| Role | Email | Password |
| --- | --- | --- |
| Admin | `admin@nexaorder.com` | `admin123` |
| Customer | `customer@nexaorder.com` | `customer123` |

The app also seeds demo products when the `products` table is empty.

## Database

Database name:

```text
nexaorder_db
```

Create the database in MySQL before running the application:

```sql
CREATE DATABASE nexaorder_db;
```

Check `src/main/resources/application.properties` and adjust these values for your local MySQL setup:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nexaorder_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

## How To Run

From the project root:

```powershell
.\mvnw.cmd spring-boot:run
```

Then open:

```text
http://localhost:8080
```

To compile without starting the app:

```powershell
.\mvnw.cmd -DskipTests compile
```

To run tests:

```powershell
.\mvnw.cmd test
```

## Demo Flow

1. Log in as admin and review dashboard metrics.
2. Open Products, create or edit a product, and optionally upload an image.
3. Open Inventory and adjust stock for a product.
4. Log in as customer and browse products.
5. Create an order from a product detail page.
6. Pay with E-Wallet for instant `PAID`, Bank Transfer for admin confirmation, or Cash on Delivery for pending payment with processable order status.
7. Log in as admin and move paid orders to `PROCESSING`, then `COMPLETED`.
8. Open Reports, verify charts, and export CSV.
9. Open an order detail page and view/print the invoice.

## Image Uploads

Uploaded product images are stored under:

```text
src/main/resources/static/uploads/products
```

Only the image URL is stored in the product record. If a product has no uploaded image, the UI shows a clean placeholder panel.

## Exporting The Database

Using command line:

```powershell
mysqldump -u root -p nexaorder_db > nexaorder_db.sql
```

Using Laragon/HeidiSQL:

1. Open Laragon and start MySQL.
2. Open HeidiSQL from Laragon.
3. Select the `nexaorder_db` database.
4. Use Export database as SQL.
5. Save the file as `nexaorder_db.sql`.

## Screenshot Checklist

- Login page
- Admin dashboard
- Product management page
- Inventory page
- Admin order detail page
- Customer payment page
- Report dashboard
- Invoice page
- Customer product list
- Customer order detail page
