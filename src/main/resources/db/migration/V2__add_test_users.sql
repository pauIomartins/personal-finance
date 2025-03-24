-- Password for all test users is: Test@2024
-- BCrypt hash of Test@2024
INSERT INTO users (email, password, name, account_non_expired, account_non_locked, credentials_non_expired, enabled) VALUES
('admin@example.com', '$2a$10$jh6gQ9nc6knrd2DcI95sV.WCCTvvGXYRU7FYqBFn/YGjhHCw.L0F2', 'Admin User', true, true, true, true),
('john@example.com', '$2a$10$jh6gQ9nc6knrd2DcI95sV.WCCTvvGXYRU7FYqBFn/YGjhHCw.L0F2', 'John Doe', true, true, true, true),
('alice@example.com', '$2a$10$jh6gQ9nc6knrd2DcI95sV.WCCTvvGXYRU7FYqBFn/YGjhHCw.L0F2', 'Alice Smith', true, true, true, true);

-- Add some sample transactions
INSERT INTO transactions (user_id, description, amount, type, category, date) VALUES
(1, 'Salary', 5000.00, 'INCOME', 'Salary', '2025-03-01 09:00:00'),
(1, 'Rent', 1500.00, 'EXPENSE', 'Housing', '2025-03-05 10:00:00'),
(1, 'Groceries', 200.00, 'EXPENSE', 'Food', '2025-03-10 15:30:00'),
(2, 'Freelance Work', 2000.00, 'INCOME', 'Contract', '2025-03-15 14:00:00'),
(2, 'Internet Bill', 89.99, 'EXPENSE', 'Utilities', '2025-03-20 11:00:00'),
(3, 'Part-time Job', 1200.00, 'INCOME', 'Salary', '2025-03-18 16:00:00'),
(3, 'Phone Bill', 75.00, 'EXPENSE', 'Utilities', '2025-03-22 09:30:00');
