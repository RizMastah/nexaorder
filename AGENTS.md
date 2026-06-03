# AGENTS.md — NexaOrder Development Guide

## 1. Project Identity

**Website name:** NexaOrder  
**Project title:** Web-Based Order Management System  
**Package name:** `com.raihan.nexaorder`  
**Deadline target:** End of June  

NexaOrder is a Spring Boot + Thymeleaf web application for managing products, orders, payments, inventory, notifications, invoices, and reports. The project must also clearly demonstrate software design patterns in the source code.

The website should feel like a modern small-business order management dashboard, not a generic school assignment template.

---

## 2. Final Tech Stack

### Backend
- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- MySQL
- Maven
- Lombok
- Bean Validation

### Frontend
- Thymeleaf
- Tailwind CSS
- Flowbite
- Alpine.js
- Chart.js

### Authentication
- Use simple custom login with `HttpSession`.
- Do not use Spring Security unless explicitly requested later.

---

## 3. Main Scope

### Admin Features
- Login
- Manage users
- Manage products
- Upload product image
- Manage inventory
- View inventory logs
- View all orders
- Update order status
- Confirm bank transfer payments
- View notifications
- View report dashboard
- Export report
- View invoice/order receipt

### Customer Features
- Register
- Login
- View products
- View product detail
- Create order
- Choose payment method
- View payment status
- View order history
- View order detail
- Cancel order if allowed
- View notifications
- View invoice/order receipt

---

## 4. Payment Rules

Supported payment methods:

```text
CASH_ON_DELIVERY
BANK_TRANSFER
EWALLET
```

Virtual Account is removed.

| Method | Behavior |
|---|---|
| E-Wallet | Payment is instantly successful. Order becomes `PAID`. |
| Bank Transfer | Payment status becomes `WAITING_CONFIRMATION`. Admin must confirm it. |
| Cash on Delivery | Payment remains pending until the order is completed. |

---

## 5. Order Status Flow

```text
PENDING → PAID → PROCESSING → COMPLETED
   ↓
CANCELLED
```

Rules:
- `PENDING` can move to `PAID` or `CANCELLED`.
- `PAID` can move to `PROCESSING`.
- `PROCESSING` can move to `COMPLETED`.
- `COMPLETED` cannot be changed.
- `CANCELLED` cannot be changed.
- Stock is reduced when an order becomes `PAID`.
- Inventory changes should be recorded in `inventory_logs`.

---

## 6. Database Tables

Use these tables:

```text
users
products
orders
order_items
payments
notifications
inventory_logs
invoices
```

### users
```text
id
name
email
password
role
created_at
updated_at
```

Roles:
```text
ADMIN
CUSTOMER
```

### products
```text
id
name
category
price
stock
description
image_url
created_at
updated_at
```

### orders
```text
id
user_id
total_price
status
created_at
updated_at
```

### order_items
```text
id
order_id
product_id
quantity
price
subtotal
```

### payments
```text
id
order_id
payment_method
payment_status
amount
paid_at
created_at
```

Payment status:
```text
PENDING
WAITING_CONFIRMATION
PAID
FAILED
```

### notifications
```text
id
user_id
title
message
is_read
created_at
```

### inventory_logs
```text
id
product_id
change_type
quantity_change
description
created_at
```

Inventory change type:
```text
IN
OUT
RESTORE
ADJUSTMENT
```

### invoices
```text
id
order_id
invoice_number
issued_at
total_amount
```

---

## 7. Spring Boot Package Structure

```text
src/main/java/com/raihan/nexaorder
│
├── config
├── controller
├── dto
├── entity
├── enums
├── repository
├── service
├── service/impl
├── pattern/payment
├── pattern/state
├── pattern/observer
└── util
```

Responsibilities:

| Folder | Responsibility |
|---|---|
| `config` | Interceptor, session checking, upload config |
| `controller` | HTTP request handling |
| `dto` | Request/response objects |
| `entity` | JPA entities |
| `enums` | Role, payment, order, inventory enums |
| `repository` | Spring Data JPA repositories |
| `service` | Business service interfaces |
| `service/impl` | Business service implementations |
| `pattern/payment` | Strategy + Factory Method for payment |
| `pattern/state` | State Pattern for order status |
| `pattern/observer` | Observer Pattern for order events |
| `util` | Helper classes |

---

## 8. Required Design Patterns

### MVC Pattern
Use Spring MVC naturally:

```text
Controller → Service → Repository → Database
```

### Repository Pattern
Use Spring Data JPA repositories:

```text
UserRepository
ProductRepository
OrderRepository
OrderItemRepository
PaymentRepository
NotificationRepository
InventoryLogRepository
InvoiceRepository
```

### DTO Pattern
Use DTOs for form input and page response data:

```text
LoginRequestDTO
RegisterRequestDTO
ProductRequestDTO
ProductResponseDTO
CreateOrderRequestDTO
OrderItemRequestDTO
OrderResponseDTO
PaymentRequestDTO
PaymentResponseDTO
NotificationResponseDTO
ReportSummaryDTO
InvoiceResponseDTO
```

### Factory Method + Strategy Pattern for Payment

Package:

```text
pattern/payment
```

Structure:

```text
PaymentStrategy
├── CashOnDeliveryStrategy
├── BankTransferStrategy
└── EWalletStrategy

PaymentFactory
```

### State Pattern for Order Status

Package:

```text
pattern/state
```

Structure:

```text
OrderState
├── PendingState
├── PaidState
├── ProcessingState
├── CompletedState
└── CancelledState
```

### Observer Pattern for Order Events

Package:

```text
pattern/observer
```

Structure:

```text
OrderEventObserver
├── NotificationObserver
└── InventoryObserver

OrderEventPublisher
```

### Singleton Pattern
Spring `@Service`, `@Component`, and `@Repository` beans are singleton by default. Mention this in the report and comments where useful.

---

# 9. UI / Frontend Blueprint

## 9.1 UI Direction

NexaOrder must look like a modern business dashboard.

Important updated decision:

```text
The frontend should avoid decorative animations.
GSAP should not be used.
The UI should be clean, fast, stable, and practical.
```

Design keywords:

```text
clean
modern
structured
premium but simple
business dashboard
not generic
not AI-generated
not too colorful
not too animated
```

### Visual Style
- Background: light gray / off-white
- Main surface: white cards
- Primary accent: emerald / green
- Neutral color: slate / gray
- Border: soft gray
- Shadow: soft and subtle
- Radius: rounded-xl or rounded-2xl
- Typography: clear hierarchy with consistent font sizes
- Layout: sidebar + top navbar + content area

### Avoid
- Too many gradients
- Too many colors
- Random illustrations
- Generic Bootstrap-like template appearance
- Inconsistent spacing
- Overly dramatic animations

---

## 9.2 Frontend Libraries

Use CDN for development and demo:

```html
<script src="https://cdn.tailwindcss.com"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/flowbite/2.5.2/flowbite.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/flowbite/2.5.2/flowbite.min.js"></script>
<script defer src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
```

---

## 9.3 Role of Each Frontend Tool

### Tailwind CSS
Use for:
- Layout
- Spacing
- Typography
- Color
- Cards
- Buttons
- Forms
- Tables
- Badges
- Responsive design

### Flowbite
Use for:
- Sidebar
- Navbar
- Dropdown
- Modal
- Alert
- Badge
- Table
- Pagination
- Form components

### Alpine.js
Use for lightweight UI interactivity:
- Sidebar toggle
- User dropdown
- Notification dropdown
- Modal open/close state
- Show/hide password
- Product image preview
- Payment method selection
- Quantity selector
- Filter state
- Mark notification as read UI behavior


### Chart.js
Use for:
- Order status distribution chart
- Best-selling products chart
- Sales summary chart

---

## 9.4 Animation and Interaction Policy

NexaOrder is a practical business dashboard, not a landing page or visual showcase. Avoid decorative page-load animations because they can make the application feel generic or AI-generated.

### Main Rule

```text
Do not use GSAP.
Do not use page-load fade-in animations.
Do not use stagger animations for dashboard cards, product cards, charts, or tables.
All important content should be visible immediately when the page loads.
```

### Allowed Micro-Interactions

Use only functional and lightweight interactions:

```text
Tailwind hover transitions
Flowbite modal/dropdown behavior
Alpine.js x-transition for dropdowns and modals
Simple toast/alert display for user feedback
Chart.js default chart rendering
```

### Avoid

```text
page content fade-in on load
dashboard card stagger reveal
product card stagger reveal
chart container reveal
auth card entrance animation
order timeline reveal animation
bounce-heavy animation
spin animation
glitch effect
random parallax
infinite movement
large movement
too many animations on one page
```

### UI Feeling

The interface should feel fast, stable, and professional. Prioritize clarity, spacing, readable tables, clear status badges, and smooth form interactions over decorative motion.

---

## 9.5 Where to Use Alpine.js and Minimal Transitions

### Login / Register Page
Alpine.js:
- Show/hide password
- Close error alert


### Admin Dashboard
Alpine.js:
- Sidebar toggle
- Profile dropdown
- Notification dropdown


### Sidebar / Navbar
Alpine.js:
- Mobile sidebar open/close
- Dropdown state
- Collapsible menu if needed


### Product Management
Alpine.js:
- Product image preview
- Delete confirmation modal
- Category filter state
- Search UI state


### Customer Product List
Alpine.js:
- Category filter
- Sort dropdown
- Quantity selector
- Product quick-view modal


### Create Order Page
Alpine.js:
- Quantity increment/decrement
- Temporary subtotal calculation
- Selected product state
- Disable submit if quantity invalid


### Payment Page
Alpine.js:
- Payment method selection
- Show/hide payment instructions
- Confirm payment panel


### Order Detail Page
Alpine.js:
- Toggle order item details
- Cancel confirmation modal
- Show/hide invoice preview


### Notification Page / Dropdown
Alpine.js:
- Toggle notification panel
- Filter all/unread
- Mark as read UI state


### Report Dashboard
Alpine.js:
- Date/month filter
- Export button loading state


### Invoice Page
Alpine.js:
- Print button
- Toggle invoice detail if needed


---

## 9.6 Thymeleaf Template Structure

Create reusable Thymeleaf fragments:

```text
templates/fragments/head.html
templates/fragments/sidebar.html
templates/fragments/topbar.html
templates/fragments/footer.html
templates/fragments/status-badge.html
templates/fragments/payment-badge.html
templates/fragments/notification-dropdown.html
templates/fragments/confirm-modal.html
```

Create layouts:

```text
templates/layout/auth-layout.html
templates/layout/admin-layout.html
templates/layout/customer-layout.html
```

Create static assets:

```text
src/main/resources/static/css/app.css
src/main/resources/static/js/app.js
src/main/resources/static/js/charts.js
src/main/resources/static/uploads/products
```

---

## 9.7 Route Structure

### Auth
```text
GET  /auth/login
POST /auth/login
GET  /auth/register
POST /auth/register
POST /auth/logout
```

### Admin
```text
GET  /admin/dashboard
GET  /admin/users
GET  /admin/products
GET  /admin/products/create
POST /admin/products
GET  /admin/products/edit/{id}
POST /admin/products/update/{id}
POST /admin/products/delete/{id}

GET  /admin/orders
GET  /admin/orders/{id}
POST /admin/orders/{id}/status

GET  /admin/payments
POST /admin/payments/{id}/confirm

GET  /admin/inventory
GET  /admin/reports
GET  /admin/reports/export

GET  /admin/notifications
POST /admin/notifications/{id}/read

GET  /admin/invoices/{id}
```

### Customer
```text
GET  /customer/dashboard
GET  /customer/products
GET  /customer/products/{id}

GET  /customer/orders/create
POST /customer/orders
GET  /customer/orders
GET  /customer/orders/{id}
POST /customer/orders/{id}/cancel

GET  /customer/payment/{orderId}
POST /customer/payment/{orderId}

GET  /customer/notifications
POST /customer/notifications/{id}/read

GET  /customer/invoices/{id}
```

---

## 9.8 Page-by-Page UI Plan

### Auth Login Page
- Centered login card
- NexaOrder logo/name
- Email input
- Password input
- Show/hide password toggle
- Login button
- Link to register

### Auth Register Page
- Centered register card
- Name, email, password, confirm password
- Register button
- Link to login

### Admin Dashboard
- Total Orders card
- Total Sales card
- Pending Orders card
- Low Stock Products card
- Order status chart
- Best-selling products chart
- Recent orders table
- Low stock alert card

### Admin Products Page
- Product table
- Search and category filter
- Add product button
- Product image thumbnail
- Stock badge
- Edit/delete actions

### Admin Product Form
- Product name
- Category
- Price
- Stock
- Description
- Image upload
- Live image preview

### Admin Orders Page
- Orders table
- Status filter
- Payment status badge
- View detail button
- Update status action

### Admin Order Detail Page
- Order summary
- Customer info
- Order item table
- Payment info
- Status timeline
- Update status form
- Invoice link

### Admin Inventory Page
- Product stock table
- Low stock badge
- Manual stock adjustment
- Inventory log table

### Admin Report Page
- Summary cards
- Order status distribution chart
- Best-selling product chart
- Recent orders table
- Export report button

### Customer Dashboard
- Welcome card
- Recent orders
- Active order status
- Notifications
- Quick link to products

### Customer Product List
- Product grid
- Search input
- Category filter
- Product card with image, price, stock, detail button

### Customer Product Detail
- Product image
- Product information
- Quantity selector
- Add/create order button
- Stock warning if low

### Customer Create Order Page
- Selected product/order items
- Quantity input
- Price and subtotal
- Order summary card
- Submit order button

### Customer Payment Page
- Order summary
- Payment method cards
- Dynamic payment instruction based on selected method
- Confirm payment button

### Customer Orders Page
- Order history table/cards
- Status badge
- Payment badge
- Detail button

### Customer Order Detail Page
- Order summary
- Order item table
- Payment information
- Status timeline
- Cancel order button if allowed
- Invoice link

### Notification Page
- All/unread filter
- Notification list
- Mark as read action

### Invoice Page
- Invoice number
- Customer information
- Order date
- Product list
- Quantity
- Price
- Subtotal
- Payment method
- Payment status
- Order status
- Total amount
- Print button

---

## 9.9 UI Quality Checklist

Before considering any page finished, check:

- Is spacing consistent?
- Is typography consistent?
- Are buttons visually consistent?
- Are table columns aligned?
- Are badges clear and readable?
- Is mobile layout acceptable?
- Does the page have enough whitespace?
- Does the page avoid unnecessary decoration?
- Are interactions subtle and functional?
- Does the page look like a real business dashboard?

---

## 10. Dummy Data

### Demo Accounts
```text
Admin:
email: admin@nexaorder.com
password: admin123

Customer:
email: customer@nexaorder.com
password: customer123
```

### Product Categories
```text
Electronics
Accessories
Office Supplies
Home & Living
```

### Dummy Products
```text
Wireless Mouse
Mechanical Keyboard
USB-C Cable
Laptop Stand
Bluetooth Speaker
Notebook
Desk Lamp
Webcam
Office Chair
Power Bank
```

---

## 11. Recommended Development Order

### Phase 1 — Setup
- Create Spring Boot project
- Set package to `com.raihan.nexaorder`
- Add dependencies
- Configure MySQL
- Create first Thymeleaf page
- Add Tailwind + Flowbite + Alpine.js + Chart.js via CDN

### Phase 2 — Core Backend
- Create enums
- Create entities
- Create repositories
- Confirm tables are generated in MySQL

### Phase 3 — Authentication
- Register customer
- Login admin/customer
- Session management
- Logout
- Route protection using interceptor

### Phase 4 — Layout and UI Base
- Build auth layout
- Build admin layout
- Build customer layout
- Build sidebar
- Build topbar
- Build status badge fragments
- Build base interaction file

### Phase 5 — Product and Inventory
- Product CRUD
- Image upload
- Product list/detail
- Inventory logs
- Low stock warning

### Phase 6 — Order
- Create order
- Order items
- Total calculation
- Stock check
- Order history/detail

### Phase 7 — Payment Patterns
- PaymentStrategy
- PaymentFactory
- COD, Bank Transfer, E-Wallet strategies
- Payment page UI

### Phase 8 — State and Observer Patterns
- OrderState classes
- Status transition logic
- OrderEventPublisher
- NotificationObserver
- InventoryObserver

### Phase 9 — Reports and Invoice
- Report dashboard
- Chart.js charts
- Export report
- Invoice page

### Phase 10 — Polish and Demo
- Add dummy data
- Improve UI consistency
- Keep transitions minimal and functional
- Fix bugs
- Take screenshots
- Export SQL file
- Prepare final demo

---

## 12. Coding Style Rules

- Keep controllers thin.
- Put business logic in services.
- Do not put database logic in controllers.
- Do not expose entity objects directly when DTO is more appropriate.
- Use clear method names.
- Add comments only for design pattern logic and non-obvious business rules.
- Keep UI consistent across admin and customer pages.
- Avoid overengineering.
- Make the project easy to demo.
