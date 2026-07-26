# SpendWise

SpendWise is a JavaFX desktop expense tracker for students and young professionals. It records income, expenses, budgets, analytics, and the actual item purchased for each expense.

## Feature

- User Authentication (Login & Registration)
- Dashboard with financial overview
- Income and Expense Management
- Transaction History
- Search, Filter, Sort, Edit & Delete Transactions
- Monthly and Category-wise Budget Planning
- Savings Goals Tracking
- Spending Analytics with Charts
- Monthly Financial Reports
- Smart Spending Insights
- Undo Delete using Stack
- Item Purchased Tracking

## What Makes SpendWise Different?

Instead of storing only:

```text
Rs.120 → College Canteen
```

SpendWise stores:

```text
College Canteen
Item: Cold Coffee
Category: Food
Payment Method: UPI
Amount: Rs.120
Date: 25 July 2026
```
This provides much richer insights into spending habits and helps users remember exactly where their money went.

## Tech Stack

- Java 17+
- JavaFX
- Maven
- MySQL
- JDBC
- JUnit 5

## DSA Used

| Feature          | DSA                    |
| ---------------- | ---------------------- |
| Transaction list | ArrayList              |
| Category totals  | HashMap                |
| Undo delete      | Stack                  |
| Recurring bills  | Queue / PriorityQueue  |
| Top categories   | PriorityQueue          |
| Sorting          | Merge Sort             |
| Amount search    | Binary Search          |

## Project Structure

```text
src/main/java/com/spendwise/
|-- Main.java
|-- model/
|-- dao/
|-- service/
|-- dsa/
`-- util/

src/main/resources/
|-- css/
|-- fxml/
|-- images/
`-- icons/

database/
|-- schema.sql
`-- sample_data.sql
```

## Database Setup

1. Create a MySQL database.
2. Execute:

```text
database/schema.sql
database/sample_data.sql
```

3. Configure the database credentials as environment variables.

**PowerShell**

```powershell
$env:SPENDWISE_DB_URL="jdbc:mysql://localhost:3306/spendwise"
$env:SPENDWISE_DB_USER="root"
$env:SPENDWISE_DB_PASSWORD="your_password"
```

If MySQL is not connected, the app runs with demo data.

## Installation & Run

Clone the repository:

```bash
git clone <repository-url>
cd SpendWise
```

Run the application:

```bash
mvn javafx:run
```

Run tests:

```bash
mvn test
```

## Demo Login

```text
Email: demo@spendwise.app
Password: Demo1234
```

## Main Screens

- Splash
- Login / Register
- Dashboard
- Add Expense
- Add Income
- Transaction History
- Budget Planner
- Analytics
- Monthly Report

## Documentation

The complete Software Design Document, including architecture, database design, user flow, UI specifications, and DSA mapping, is available here:


[docs/Software-Design-Document.md](docs/Software-Design-Document.md)

## Future Enhancements

- PDF Export
- CSV Import/Export
- Dark Mode
- Cloud Backup
- Notification Reminders
- Advanced Financial Scoring

---

## Author

**Sharyu Chavan**

Computer Engineering Student

Java | DSA | JavaFX | MySQL | Software Development