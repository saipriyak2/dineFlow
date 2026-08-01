# Data Model / Schema — DineFlow

This diagram reflects the entities and relationships defined in the Business Requirements Document 

```mermaid
erDiagram
    USER ||--o{ RESERVATION : makes
    USER ||--o{ ORDER : places
    USER ||--o{ REVIEW : writes
    USER ||--o| CART : owns

    MENU_CATEGORY ||--o{ MENU_ITEM : contains

    CART ||--o{ CART_ITEM : contains
    CART_ITEM }o--|| MENU_ITEM : references

    ORDER ||--o{ ORDER_ITEM : contains
    ORDER_ITEM }o--|| MENU_ITEM : references
    ORDER ||--|| PAYMENT : "paid via"
    ORDER ||--o| REVIEW : "reviewed by"

    USER {
        uuid id PK
        string name
        string email UK
        string passwordHash
        string role "CUSTOMER | ADMIN"
        datetime createdAt
    }

    MENU_CATEGORY {
        uuid id PK
        string name
        string description
    }

    MENU_ITEM {
        uuid id PK
        uuid categoryId FK
        string name
        string description
        decimal price
        string imageUrl
        boolean available
    }

    RESERVATION {
        uuid id PK
        uuid userId FK
        string reservationNumber UK
        date reservationDate
        time timeSlot
        int partySize
        string status "CONFIRMED | CANCELLED | COMPLETED"
        datetime createdAt
    }

    CART {
        uuid id PK
        uuid userId FK
        datetime updatedAt
    }

    CART_ITEM {
        uuid id PK
        uuid cartId FK
        uuid menuItemId FK
        int quantity
    }

    ORDER {
        uuid id PK
        uuid userId FK
        string status "PLACED | PREPARING | READY | COMPLETED | CANCELLED | PAID"
        string fulfillmentMethod "DELIVERY | PICKUP"
        decimal totalAmount
        datetime createdAt
    }

    ORDER_ITEM {
        uuid id PK
        uuid orderId FK
        uuid menuItemId FK
        int quantity
        decimal priceAtPurchase
    }

    PAYMENT {
        uuid id PK
        uuid orderId FK
        string method "CARD | UPI | WALLET"
        string status "PENDING | SUCCESS | FAILED | REFUNDED"
        decimal amount
        datetime createdAt
    }

    REVIEW {
        uuid id PK
        uuid orderId FK
        uuid userId FK
        int rating "1-5"
        string text
        datetime createdAt
    }
```

## Key Relationships 

| Relationship | Cardinality | Notes |
|---|---|---|
| User → Reservation / Order / Review | 1—N | A user can have many of each |
| MenuCategory → MenuItem | 1—N | Each item belongs to exactly one category |
| Cart → CartItem → MenuItem | 1—N / N—1 | Cart items reference a menu item |
| Order → OrderItem → MenuItem | 1—N / N—1 | Price is snapshotted at purchase time (`priceAtPurchase`), independent of later menu price changes |
| Order → Payment | 1—1 | One payment record per order |
| Order → Review | 1—0..1 | At most one review per order (US-13) |

## Business-Rule Notes Encoded in the Schema
- **US-7 (no double-booking):** `Reservation` capacity is enforced at the `(reservationDate, timeSlot)` level, not per physical table — a `slot_capacity` config (or table) should track max reservations per slot, checked atomically on insert.
- **US-10 (order integrity):** `Order` + `OrderItem` are created in a single transaction; `totalAmount`/`priceAtPurchase` are persisted at creation time and never recalculated.
- **US-13 (one review per order):** enforce a unique constraint on `Review.orderId`.
- **US-16 (payment lifecycle):** `Payment.status` follows `PENDING → SUCCESS | FAILED`, with `REFUNDED` as an admin-only follow-up transition.
