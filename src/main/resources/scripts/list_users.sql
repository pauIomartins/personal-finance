-- Script to list all users in the database
SELECT 
    id,
    name,
    email,
    account_non_expired,
    account_non_locked,
    credentials_non_expired,
    enabled
FROM users
ORDER BY id;
