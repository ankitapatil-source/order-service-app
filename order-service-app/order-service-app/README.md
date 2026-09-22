# Order Service App

A small order-management service used as a training sandbox for **AI-assisted
debugging and repository investigation with MCP**.

It models a very small shop: products, customers, coupons, stock, and orders.
When an order is placed it is priced, discounted, and saved.

## Requirements

- JDK 11 or newer — check with `java -version`
- Maven 3.8+ — check with `mvn -version`

If `mvn` is not found, install it first (macOS: `brew install maven`; or use
SDKMAN: `sdk install maven`). Maven needs a JDK on your `PATH` / `JAVA_HOME`.

## Build, run, and test (from a terminal)

Run these from the project root — the folder that contains `pom.xml`:

```bash
# Build: compile, run the tests, and package a jar into target/
mvn clean package

# Run the application (prints the scenarios, writes logs/app.log)
mvn -q compile exec:java

# Run only the unit tests
mvn test
```

To build without running the tests, add `-DskipTests`
(for example `mvn clean package -DskipTests`).

The first build needs internet access so Maven can download its dependencies
(including JUnit) into your local `~/.m2` cache.

## What's in here

```
src/main/java/com/rsl/orderservice
├── App.java            # entry point – runs a few order scenarios
├── model/              # Product, Customer, Coupon, Order, OrderItem, ...
├── repository/         # in-memory stores
├── service/            # the business logic
└── util/               # logging setup
```

> Note: this README is intentionally light. Part of the exercise is to explore the
> codebase yourself and work out how the pieces fit together.
