-- Create the database
CREATE DATABASE IF NOT EXISTS chat_application;

-- Use the database
USE chat_application;

-- Drop the old table if it exists (optional - only if you want to start fresh)
-- DROP TABLE IF EXISTS messages;

-- Create the messages table with all necessary columns
CREATE TABLE IF NOT EXISTS messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender VARCHAR(100) NOT NULL,
    receiver VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_timestamp (timestamp),
    INDEX idx_sender (sender),
    INDEX idx_receiver (receiver)
);

-- If you already have the table without timestamp, use this instead:
-- ALTER TABLE messages ADD COLUMN timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- View table structure to confirm
DESCRIBE messages;

-- Optional: Clear all old messages (use with caution!)
-- TRUNCATE TABLE messages;

-- Optional: View all messages
-- SELECT * FROM messages ORDER BY timestamp DESC;