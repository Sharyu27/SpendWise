USE spendwise;

INSERT INTO users (name, email, password, monthly_budget)
VALUES ('Sharyu', 'demo@spendwise.app', 'Demo1234', 10000.00)
ON DUPLICATE KEY UPDATE name = VALUES(name);

SET @demo_user_id = (SELECT user_id FROM users WHERE email = 'demo@spendwise.app');

INSERT INTO transactions (user_id, type, amount, merchant, item_name, category, payment_method, notes, transaction_date)
VALUES
(@demo_user_id, 'INCOME', 15000.00, NULL, NULL, 'Pocket Money', 'Bank Transfer', 'Monthly allowance', CURDATE()),
(@demo_user_id, 'EXPENSE', 120.00, 'College Canteen', 'Cold Coffee', 'Food', 'UPI', 'After class', CURDATE()),
(@demo_user_id, 'EXPENSE', 220.00, 'Stationery Shop', 'DSA Notebook', 'Education', 'UPI', 'Algorithms notes', CURDATE()),
(@demo_user_id, 'EXPENSE', 340.00, 'D-Mart', 'Milk and Bread', 'Groceries', 'Card', 'Weekly essentials', DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
(@demo_user_id, 'EXPENSE', 180.00, 'Metro', 'Student pass recharge', 'Travel', 'UPI', 'Commute', DATE_SUB(CURDATE(), INTERVAL 2 DAY));

INSERT INTO budgets (user_id, month, category, budget_amount)
VALUES
(@demo_user_id, DATE_FORMAT(CURDATE(), '%Y-%m'), 'Food', 3000.00),
(@demo_user_id, DATE_FORMAT(CURDATE(), '%Y-%m'), 'Travel', 1500.00),
(@demo_user_id, DATE_FORMAT(CURDATE(), '%Y-%m'), 'Shopping', 2500.00);

INSERT INTO savings_goals (user_id, goal_name, target_amount, saved_amount, deadline)
VALUES (@demo_user_id, 'Laptop', 65000.00, 42000.00, DATE_ADD(CURDATE(), INTERVAL 85 DAY));

INSERT INTO recurring_bills (user_id, bill_name, amount, due_date, category)
VALUES (@demo_user_id, 'Mobile Recharge', 299.00, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 'Bills');

