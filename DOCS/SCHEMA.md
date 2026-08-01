# Data Model / Schema — Restaurant Web Application (MVP)

This diagram reflects the entities and relationships defined in the Business Requirements Document.

```mermaid
classDiagram
    class User {
        +UUID id
        +String name
        +String email
        +String passwordHash
        +String role
        +DateTime createdAt
    }

    class MenuCategory {
        +UUID id
        +String name
        +String description
    }

    class MenuItem {
        +UUID id
        +UUID categoryId
        +String name
        +String description
        +Decimal price
        +String imageUrl
        +Boolean available
    }

    class Reservation {
        +UUID id
        +UUID userId
        +String reservationNumber
        +Date reservationDate
        +Time timeSlot
        +Int partySize
        +String status
        +DateTime createdAt
    }

    class Cart {
        +UUID id
        +UUID userId
        +DateTime updatedAt
    }

    class CartItem {
        +UUID id
        +UUID cartId
        +UUID menuItemId
        +Int quantity
    }

    class Order {
        +UUID id
        +UUID userId
        +String status
        +String fulfillmentMethod
        +Decimal totalAmount
        +DateTime createdAt
    }

    class OrderItem {
        +UUID id
        +UUID orderId
        +UUID menuItemId
        +Int quantity
        +Decimal priceAtPurchase
    }

    class Payment {
        +UUID id
        +UUID orderId
        +String method
        +String status
        +Decimal amount
        +DateTime createdAt
    }

    class Review {
        +UUID id
        +UUID orderId
        +UUID userId
        +Int rating
        +String text
        +DateTime createdAt
    }

    User "1" --> "0..*" Reservation : makes
    User "1" --> "0..*" Order : places
    User "1" --> "0..*" Review : writes
    User "1" --> "0..1" Cart : owns

    MenuCategory "1" --> "0..*" MenuItem : offers

    Cart "1" --> "0..*" CartItem : contains
    CartItem "0..*" --> "1" MenuItem : references

    Order "1" --> "0..*" OrderItem : contains
    OrderItem "0..*" --> "1" MenuItem : references
    Order "1" --> "1" Payment : paid via
    Order "1" --> "0..1" Review : reviewed by

    classDef userColor fill:#FFE066,stroke:#D4A017,stroke-width:1px,color:#3d2e00
    classDef menuColor fill:#A9C9FF,stroke:#4C7CD4,stroke-width:1px,color:#0b2447
    classDef orderColor fill:#FFB3D9,stroke:#D4508A,stroke-width:1px,color:#4b0f2c
    classDef reservationColor fill:#FFCC99,stroke:#D4780F,stroke-width:1px,color:#4a2600
    classDef paymentColor fill:#C9B3FF,stroke:#7B4FD4,stroke-width:1px,color:#2c1a4a
    classDef reviewColor fill:#B3E6A8,stroke:#5FA83A,stroke-width:1px,color:#1a3d0f

    class User:::userColor
    class MenuCategory:::menuColor
    class MenuItem:::menuColor
    class Reservation:::reservationColor
    class Cart:::orderColor
    class CartItem:::orderColor
    class Order:::orderColor
    class OrderItem:::orderColor
    class Payment:::paymentColor
    class Review:::reviewColor
```

**Color key:** 🟡 Identity (`User`) · 🔵 Menu catalog (`MenuCategory`, `MenuItem`) · 🩷 Cart & ordering (`Cart`, `CartItem`, `Order`, `OrderItem`) · 🟠 Reservations · 🟣 Payment · 🟢 Reviews

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
