# 📦 Product Service and Entity Design

## Entities

### 🛒 Product
Represents an item that can be ordered.

- **Fields**: `id`, `name`, `description`, `price`, `availableQuantity`, etc.
- **Table**: `product`

### 📦 Order
Represents a user’s order.

- **Fields**: `id`, `status` (`PENDING`, `CONFIRMED`, `REJECTED`), `createdAt`, etc.
- **Table**: `order`
- An order is created with `PENDING` status by default.

### 🧾 OrderItem
Represents individual items in an order.

- **Fields**: `id`, `orderId`, `productId`, `quantity`
- **Table**: `order_item`
- Acts as a **join table** between `Order` and `Product`.

## APIs

### ➕ `POST /orders`
Places a new order.

- Creates entries in `order` and `order_item` tables.
- Triggers an **OrderPlaced event** to Kafka with order details.

### 🔍 `GET /orders/{orderId}`
Fetches the current status of a given order.

### ✏️ `PATCH /orders/{orderId}`
Updates the status of an existing order (typically used by the consumer service).

### 📄 `GET /products`
Lists all products from the `product` table.

# ⚙️ Kafka Consumer and Inventory Management

Once an order is placed, the system communicates asynchronously with the inventory management logic using **Kafka**.

## 🔄 OrderPlaced Event

When a new order is created (`POST /orders`), an **OrderPlaced** event is published to a Kafka topic (e.g., `order-placed`).

The event contains:

- `orderId`
- List of `productId` and `quantity`

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

# 🚀 Order Placement System Setup

Follow these steps to run the entire Order Placement System locally.

## 🧑‍💻 Prerequisites

* Java 17+
* IntelliJ (or any preferred IDE)
* Docker & Docker Compose
* Kafka + AQHQ UI (for topic management)
* Git

## 🧱 Step 1: Clone the Repository

```bash
git clone [https://github.com/your-username/order-placement-system.git](https://github.com/your-username/order-placement-system.git)
cd order-placement-system
```

## ⚙️ Step 2: Start Kafka Broker Using Docker Compose

Navigate to the Kafka setup directory (or wherever your `docker-compose.yml` resides) and run:

```bash
docker-compose up -d
```

This will start:

* Kafka broker
* Zookeeper (required for Kafka)
* Optional: AQHQ UI (Kafka topic management UI)

## 📊 Step 3: Create Kafka Topic

Once Kafka is running:

1.  Open AQHQ UI in your browser (usually at `http://localhost:8080` or the port you've mapped).
2.  Create a topic named (for example):

    ```css
    order-placed
    ```

    Ensure your producer and consumer configurations point to this topic.

## 🧩 Step 4: Run the Services

1.  Open the project in IntelliJ.
2.  There are two Spring Boot applications:
    * **Order Service**: Contains APIs and Kafka producer.
    * **Inventory Consumer Service**: Kafka consumer.
3.  Start both applications via IntelliJ (right-click `main()` → `Run`).

Once running:

* Place an order via `POST /orders`
* View the status via `GET /orders/{orderId}`
* Kafka consumer will auto-update the order status based on inventory logic

## ✅ Verifying the Flow

1.  Hit the `POST /orders` endpoint with an order payload.
2.  Check that a message is sent to Kafka.
3.  Consumer service picks the message, checks inventory, and updates order status.
4.  Fetch the updated order via `GET /orders/{orderId}`.


![timeseries diagram](https://github.com/user-attachments/assets/28f77f05-a753-426c-9618-eff956574479)
