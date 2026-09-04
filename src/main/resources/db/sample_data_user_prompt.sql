-- =====================================================================
-- Sample seed data for AI Reporting Studio - table: user_prompt
-- Database: ai_reporting (MariaDB)
--
-- This script is executed automatically by Spring Boot on every startup
-- (spring.sql.init.mode=always). It is IDEMPOTENT: rows are inserted with
-- INSERT IGNORE, so an existing title (unique constraint) is skipped.
-- Data below was exported from the local MariaDB (ai_reporting) on 2026-08-31.
--
-- Manual execution:
--   mysql -u ai_reporting -p ai_reporting < src/main/resources/db/sample_data_user_prompt.sql
-- =====================================================================

-- ---------------------------------------------------------------------
-- User Prompt (task description)
-- ---------------------------------------------------------------------
INSERT IGNORE INTO user_prompt (user_prompt_id, title, content, created_at, updated_at, is_deleted) VALUES ('40000000-0000-0000-0000-000000000001', 'Generate Sales Summary Report', 'Title: Sales Orders Report
Page Header: Sales Orders - July 2026
Table Header: List of Sales
Column 1 Header: Order ID
Column 2 Header: Customer
Column 3 Header: Product
Column 4 Header: Category
Column 5 Header: Quantity
Column 6 Header: Unit Price
Column 7 Header: Total
Column 8 Header: Order Date
Column 9 Header: Status
Table Header: Summary of Sales by Category
Column 1 Header: Category
Column 2 Header: Number of Orders
Column 3 Header: Total Sales
Total Table: 2
Page Footer: End of Report', '2026-08-21 21:44:18.000000', '2026-08-25 13:11:17.443571', 0);
