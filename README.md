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


![timeseries diagram](https://github.com/user-attachments/assets/28f77f05-a753-426c-9618-eff956574479)
