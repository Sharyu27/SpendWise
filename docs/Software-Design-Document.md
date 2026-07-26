# SpendWise Software Design Document

**Version:** 1.0  
**Project Name:** SpendWise - Smart Personal Expense Tracker  
**Tagline:** Track Smarter. Spend Wiser. Save Better.  
**Primary Users:** College students and young professionals  
**Core Differentiator:** Tracks the actual item purchased, not only the payment merchant.

## 1. Project Overview

SpendWise is a Java desktop fintech application that helps users record meaningful expense details, manage budgets, track savings goals, and understand spending behavior through analytics and monthly insights.

Students and young professionals often make many small digital payments through apps like Google Pay and PhonePe. These apps usually show the merchant and amount, but they do not help users remember what was actually purchased. Over time, this makes it difficult to analyze habits, explain spending patterns, or stay within a budget.

SpendWise solves this by storing each expense with:

- Merchant
- Item purchased
- Category
- Payment method
- Date
- Notes

For example, a vague payment record like `Rs.50 to ABC Store` becomes `Lay's Chips from ABC Store (Food)`.

## 2. Objectives

- Build a desktop fintech application using Java 17+.
- Use JavaFX for a polished desktop UI.
- Store persistent data using MySQL and JDBC.
- Use Maven for dependency and project management.
- Apply DSA concepts in realistic product features.
- Follow clean software engineering practices.
- Host and document the project professionally on GitHub.
- Create a project that can be confidently explained from UI to database to DSA.

## 3. Technology Stack

| Component | Technology |
| --- | --- |
| Language | Java 17+ |
| UI | JavaFX |
| Database | MySQL |
| Database Connectivity | JDBC |
| Build Tool | Maven |
| Version Control | Git |
| Repository | GitHub |
| IDE | VS Code |
| Charts | JavaFX Charts |
| Styling | JavaFX CSS |

## 4. User Persona

**Name:** Sharyu  
**Age:** 20  
**Income Sources:** Pocket money and internship stipend  
**Pain Points:** Small expenses are easy to forget, budgets are hard to follow, and UPI history lacks item-level detail.

SpendWise is designed around this kind of user: someone who does not need a complex accounting system, but does need better memory and insight around everyday spending.

## 5. Information Architecture

The application contains the following major screens:

- Splash Screen
- Login
- Register
- Dashboard
- Add Expense
- Add Income
- Transaction History
- Budget Planner
- Savings Goals
- Analytics
- Recurring Bills
- Monthly Report
- Profile and Settings

## 6. User Flow

```text
Splash Screen
        |
        v
 Login / Register
        |
        v
    Dashboard
        |
 +------+--------+---------+
 |      |        |         |
 v      v        v         v
Income Expense Budget Analytics
 |       |        |         |
 +-------+--------+---------+
        |
        v
Transaction History
        |
        v
 Monthly Report
        |
        v
      Logout
```

## 7. UI Screen Specifications

### 7.1 Splash Screen

**Purpose:** Introduce the application and load the login experience.

**Elements:**

- App logo
- App name
- Tagline
- Loading animation

**Behavior:**

- Shows for approximately 2 seconds.
- Uses a fade-in animation.
- Automatically navigates to Login.

### 7.2 Login

**Purpose:** Authenticate existing users.

**Fields and Actions:**

- Email
- Password
- Remember Me
- Login
- Register
- Forgot Password, optional future enhancement

**Validation:**

- Empty email
- Invalid email format
- Empty password
- Wrong credentials

### 7.3 Register

**Purpose:** Create a new user account.

**Fields and Actions:**

- Name
- Email
- Password
- Confirm Password
- Monthly Budget, optional
- Terms and Privacy checkbox
- Create Account

**Password Rules:**

- Minimum 8 characters
- At least one uppercase letter
- At least one number

### 7.4 Dashboard

**Purpose:** Give users a quick overview of their financial position.

**Displays:**

- Current Balance
- Total Income
- Total Expenses
- Remaining Budget
- Today's Safe Spending Limit
- Financial Score
- Recent Transactions
- Savings Goal Progress
- Upcoming Bills
- Smart Insight
- Quick Action Buttons

**Example Insight:**

`You spent 28% more on Food than last month. Limiting cafe purchases to Rs.250 for the next 3 days keeps you within budget.`

**Backend Calls:**

- `TransactionDAO.getRecentTransactions()`
- `BudgetService.calculateRemainingBudget()`
- `InsightService.generateInsights()`
- `ReportService.calculateFinancialScore()`

### 7.5 Add Expense

**Purpose:** Record detailed expense information.

**Fields:**

- Amount
- Merchant
- Item Purchased
- Category
- Payment Method
- Date
- Notes

**Actions:**

- Save Expense
- Cancel

**Validation:**

- Amount must be positive.
- Merchant cannot be empty.
- Item Purchased cannot be empty.
- Category must be selected.
- Payment method must be selected.
- Date cannot be in an invalid format.

### 7.6 Add Income

**Purpose:** Record income entries.

**Fields:**

- Amount
- Source
- Date
- Notes

**Actions:**

- Save Income
- Cancel

### 7.7 Transaction History

**Purpose:** View, search, sort, edit, and delete transactions.

**Features:**

- Search
- Filter by type, category, payment method, and date range
- Sort by date, amount, category, or merchant
- Edit transaction
- Delete transaction
- View transaction details
- Undo delete

### 7.8 Budget Planner

**Purpose:** Help users define and monitor spending limits.

**Features:**

- Monthly budget
- Category-wise budgets
- Budget progress indicators
- Over-budget warnings

**Daily Limit Formula:**

```text
Remaining Budget = Monthly Budget - Expenses
Days Left = Total Days in Month - Current Day
Safe Daily Limit = Remaining Budget / Days Left
```

### 7.9 Savings Goals

**Purpose:** Track progress toward planned financial goals.

**Fields and Displays:**

- Goal Name
- Target Amount
- Current Savings
- Deadline
- Progress Bar
- Estimated Completion

### 7.10 Analytics

**Purpose:** Show spending behavior visually.

**Displays:**

- Category distribution pie chart
- Monthly spending bar chart
- Top spending categories
- Spending trends
- Weekend vs weekday spending

**Sample Category Data:**

| Category | Amount |
| --- | ---: |
| Food | 3800 |
| Shopping | 2450 |
| Travel | 1800 |
| Education | 950 |

**Sample Monthly Spending Data:**

| Month | Spent |
| --- | ---: |
| Feb | 5200 |
| Mar | 6100 |
| Apr | 5400 |
| May | 6900 |
| Jun | 6400 |
| Jul | 7230 |

### 7.11 Monthly Report

**Purpose:** Generate a summary of the user's financial month.

**Displays:**

- Income
- Expenses
- Savings
- Financial Score
- Expense Memory
- Spending Insights

**Expense Memory Examples:**

- Total transactions recorded
- Most visited merchant
- Most purchased item
- Largest expense
- Best saving week
- Financial score improvement

**Future Enhancement:**

- Export report as PDF.

## 8. Feature Specification

### 8.1 Core Features

- User Authentication
- Dashboard
- Add Income
- Add Expense
- Transaction History
- Edit and Delete Transactions
- Search and Filter
- Budget Planner
- Savings Goals
- Analytics Dashboard
- Recurring Bills
- Monthly Report

### 8.2 Smart Features

**Spending Insights**

- Highest spending category
- Month-over-month comparisons
- Weekend vs weekday spending
- Category-based warnings

**Daily Spending Limit**

- Calculates how much the user can safely spend per day based on remaining budget and remaining days.

**Spending Streak**

- Tracks consecutive days under budget.
- Tracks no unnecessary shopping streak.

**Top Spending Categories**

- Shows the highest spending categories for the selected month.

**Expense Memory**

- Finds the most visited merchant.
- Finds the most purchased item.
- Finds the largest expense.
- Finds the best saving week.
- Tracks financial score changes.

**Item Purchased Tracking**

- Stores merchant, item purchased, and category for every expense.
- Makes spending analysis more meaningful than merchant-only payment history.

## 9. Database Design

### 9.1 Users

| Column | Type | Notes |
| --- | --- | --- |
| user_id | INT | Primary key |
| name | VARCHAR | User's full name |
| email | VARCHAR | Unique login email |
| password | VARCHAR | Hashed password |
| monthly_budget | DECIMAL | Optional default monthly budget |
| created_at | DATETIME | Account creation time |

### 9.2 Transactions

| Column | Type | Notes |
| --- | --- | --- |
| transaction_id | INT | Primary key |
| user_id | INT | Foreign key to users |
| type | ENUM | Income or Expense |
| amount | DECIMAL | Transaction amount |
| merchant | VARCHAR | Merchant name for expenses |
| item_name | VARCHAR | Item purchased |
| category | VARCHAR | Spending or income category |
| payment_method | VARCHAR | Cash, UPI, card, bank transfer, etc. |
| notes | TEXT | Optional notes |
| transaction_date | DATE | Date of transaction |

### 9.3 Budgets

| Column | Type | Notes |
| --- | --- | --- |
| budget_id | INT | Primary key |
| user_id | INT | Foreign key to users |
| month | VARCHAR | Budget month |
| category | VARCHAR | Category name |
| budget_amount | DECIMAL | Category budget amount |

### 9.4 Savings Goals

| Column | Type | Notes |
| --- | --- | --- |
| goal_id | INT | Primary key |
| user_id | INT | Foreign key to users |
| goal_name | VARCHAR | Goal title |
| target_amount | DECIMAL | Target amount |
| saved_amount | DECIMAL | Current saved amount |
| deadline | DATE | Target completion date |

### 9.5 Recurring Bills

| Column | Type | Notes |
| --- | --- | --- |
| bill_id | INT | Primary key |
| user_id | INT | Foreign key to users |
| bill_name | VARCHAR | Bill title |
| amount | DECIMAL | Bill amount |
| due_date | DATE | Upcoming due date |
| category | VARCHAR | Bill category |

### 9.6 Relationships

- One user has many transactions.
- One user has many budgets.
- One user has many savings goals.
- One user has many recurring bills.

## 10. DSA Mapping

| Feature | DSA | Purpose |
| --- | --- | --- |
| Transaction List | ArrayList | Store and display fetched transactions |
| Category Analytics | HashMap | Fast category total lookup |
| Undo Delete | Stack | Restore recently deleted transaction |
| Recurring Bills | Queue | Process bills in due-date order |
| Top Spending Categories | PriorityQueue | Retrieve highest spending categories |
| Monthly Reports | TreeMap | Maintain chronological ordering |
| Search | Binary Search | Efficient search after sorting |
| Sorting | Merge Sort | Custom transaction sorting implementation |

Since transaction data is stored in MySQL, these structures are mainly used for in-memory processing, analytics, reporting, and DSA demonstration after fetching data from the database.

## 11. Proposed Architecture

### 11.1 Layers

| Layer | Package | Responsibility |
| --- | --- | --- |
| Model | `model` | Plain Java objects representing application entities |
| DAO | `dao` | JDBC database access |
| Service | `service` | Business logic, calculations, insights, reports |
| Controller | `controller` | JavaFX screen behavior |
| DSA | `dsa` | Custom DSA utilities and algorithms |
| Utility | `util` | Database connection, validation, alerts, constants |

### 11.2 Example Classes

| Package | Example Classes |
| --- | --- |
| `model` | `User`, `Transaction`, `Budget`, `SavingsGoal`, `RecurringBill` |
| `dao` | `UserDAO`, `TransactionDAO`, `BudgetDAO`, `SavingsGoalDAO`, `RecurringBillDAO` |
| `service` | `AuthService`, `BudgetService`, `InsightService`, `ReportService`, `AnalyticsService` |
| `controller` | `LoginController`, `DashboardController`, `AddExpenseController`, `HistoryController` |
| `dsa` | `MergeSortUtil`, `TransactionSearch`, `TopCategoryHeap`, `UndoDeleteStack` |
| `util` | `DatabaseConnection`, `InputValidator`, `SceneManager`, `AlertUtil` |

## 12. Maven Project Structure

```text
SpendWise/
|
|-- pom.xml
|-- README.md
|
|-- src/
|   |-- main/
|   |   |-- java/
|   |   |   |-- model/
|   |   |   |-- dao/
|   |   |   |-- service/
|   |   |   |-- controller/
|   |   |   |-- dsa/
|   |   |   |-- util/
|   |   |   `-- Main.java
|   |   |
|   |   `-- resources/
|   |       |-- fxml/
|   |       |-- css/
|   |       |-- images/
|   |       `-- icons/
|   |
|   `-- test/
|
|-- database/
|   |-- schema.sql
|   `-- sample_data.sql
|
|-- docs/
|   |-- wireframes/
|   |-- er-diagram/
|   |-- feature-specification/
|   `-- Software-Design-Document.md
|
`-- screenshots/
```

## 13. Development Plan

### Sprint 1: Project Foundation

- Create Maven project.
- Configure JavaFX.
- Add MySQL JDBC dependency.
- Create database schema.
- Implement database connection utility.
- Build Login screen.
- Build Register screen.

### Sprint 2: Core Expense Flow

- Create models and DAOs for users and transactions.
- Build Dashboard shell.
- Build Add Expense screen.
- Build Add Income screen.
- Save and fetch transactions from MySQL.

### Sprint 3: History and Budgeting

- Build Transaction History.
- Add search, filter, sort, edit, and delete.
- Implement undo delete using Stack.
- Build Budget Planner.
- Calculate safe daily spending limit.

### Sprint 4: Analytics and Reports

- Build Analytics screen with JavaFX charts.
- Implement category totals using HashMap.
- Implement top categories using PriorityQueue.
- Build Monthly Report screen.
- Implement Expense Memory.

### Sprint 5: Polish and Documentation

- Improve styling with JavaFX CSS.
- Add screenshots.
- Add sample data.
- Update README.
- Document setup steps.
- Prepare GitHub repository.

## 14. Future Enhancements

- PDF export for monthly reports.
- Password reset.
- Dark mode.
- CSV import and export.
- Cloud backup.
- Notification reminders for recurring bills.
- More advanced financial scoring.

## 15. Success Criteria

The project is successful when a user can:

- Register and log in.
- Add income and expenses.
- Record the item purchased for every expense.
- View and manage transaction history.
- Set monthly and category budgets.
- Track savings goals.
- See analytics and monthly reports.
- Understand their spending habits better than they could from payment app history alone.

