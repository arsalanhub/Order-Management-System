# 📦 1. Product Service and Entity Design

## Entities

### 🛒 Product
Represents an item that can be ordered.

- **Fields**: `id`, `name`, `description`, `price`, `availableQuantity`, etc.
- **Table**: `product`

---

### 📦 Order
Represents a user’s order.

- **Fields**: `id`, `status` (`PENDING`, `CONFIRMED`, `REJECTED`), `createdAt`, etc.
- **Table**: `order`
- An order is created with `PENDING` status by default.

---

### 🧾 OrderItem
Represents individual items in an order.

- **Fields**: `id`, `orderId`, `productId`, `quantity`
- **Table**: `order_item`
- Acts as a **join table** between `Order` and `Product`.

---

## APIs

### ➕ `POST /orders`
Places a new order.

- Creates entries in `order` and `order_item` tables.
- Triggers an **OrderPlaced event** to Kafka with order details.

---

### 🔍 `GET /orders/{orderId}`
Fetches the current status of a given order.

---

### ✏️ `PATCH /orders/{orderId}`
Updates the status of an existing order (typically used by the consumer service).

---

### 📄 `GET /products`
Lists all products from the `product` table.

# ⚙️ Kafka Consumer and Inventory Management

Once an order is placed, the system communicates asynchronously with the inventory management logic using **Kafka**.

---

## 🔄 OrderPlaced Event

When a new order is created (`POST /orders`), an **OrderPlaced** event is published to a Kafka topic (e.g., `order-placed`).

The event contains:

- `orderId`
- List of `productId` and `quantity`

---

## 🧾 Kafka Consumer Workflow

A separate **Kafka Consumer Service** listens for the **OrderPlaced** event and performs the following steps:

### 1. 📥 Consume Event
Reads the order details from the Kafka topic.

### 2. 📦 Check Inventory
Verifies whether all requested products have enough `availableQuantity`.

### 3. 🔧 Update Inventory

- **If inventory is sufficient:**
  - Deducts the ordered quantity from the respective `Product` entries.
  - Updates the `Order` status to **CONFIRMED**.

- **If inventory is insufficient:**
  - Keeps the inventory unchanged.
  - Updates the `Order` status to **REJECTED**.


![timeseries diagram](https://github.com/user-attachments/assets/28f77f05-a753-426c-9618-eff956574479)
