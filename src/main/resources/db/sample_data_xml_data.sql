-- =====================================================================
-- Sample seed data for AI Reporting Studio - table: xml_data
-- Database: ai_reporting (MariaDB)
--
-- This script is executed automatically by Spring Boot on every startup
-- (spring.sql.init.mode=always). It is IDEMPOTENT: rows are inserted with
-- INSERT IGNORE, so an existing title (unique constraint) is skipped.
-- Data below was exported from the local MariaDB (ai_reporting) on 2026-08-31.
--
-- Manual execution:
--   mysql -u ai_reporting -p ai_reporting < src/main/resources/db/sample_data_xml_data.sql
-- =====================================================================

-- ---------------------------------------------------------------------
-- XML Data (sample XML datasets used to design the report)
-- ---------------------------------------------------------------------
INSERT IGNORE INTO xml_data (xml_data_id, title, content, created_at, updated_at, is_deleted) VALUES ('10000000-0000-0000-0000-000000000001', 'Sales Orders Sample', '<?xml version="1.0" encoding="UTF-8"?>
<salesOrders>
    <order>
        <orderId>SO-10011</orderId>
        <customer>John Smith</customer>
        <product>Laptop Pro 15</product>
        <category>Electronics</category>
        <quantity>2</quantity>
        <unitPrice>1299.99</unitPrice>
        <total>2599.98</total>
        <orderDate>2026-07-01</orderDate>
        <status>Completed</status>
    </order>
    <order>
        <orderId>SO-1002</orderId>
        <customer>Mary Johnson</customer>
        <product>Wireless Keyboard</product>
        <category>Accessories</category>
        <quantity>5</quantity>
        <unitPrice>79.90</unitPrice>
        <total>399.50</total>
        <orderDate>2026-07-02</orderDate>
        <status>Shipped</status>
    </order>
    <order>
        <orderId>SO-1003</orderId>
        <customer>David Lee</customer>
        <product>27-inch Monitor</product>
        <category>Electronics</category>
        <quantity>1</quantity>
        <unitPrice>349.00</unitPrice>
        <total>349.00</total>
        <orderDate>2026-07-03</orderDate>
        <status>Completed</status>
    </order>
    <order>
        <orderId>SO-1004</orderId>
        <customer>Sarah Tan</customer>
        <product>USB-C Hub</product>
        <category>Accessories</category>
        <quantity>3</quantity>
        <unitPrice>45.50</unitPrice>
        <total>136.50</total>
        <orderDate>2026-07-05</orderDate>
        <status>Pending</status>
    </order>
    <order>
        <orderId>SO-1005</orderId>
        <customer>James Wong</customer>
        <product>Noise-Cancelling Headset</product>
        <category>Audio</category>
        <quantity>4</quantity>
        <unitPrice>199.00</unitPrice>
        <total>796.00</total>
        <orderDate>2026-07-08</orderDate>
        <status>Completed</status>
    </order>
</salesOrders>', '2026-08-21 21:44:18.000000', '2026-08-22 12:39:21.428744', 0);
INSERT IGNORE INTO xml_data (xml_data_id, title, content, created_at, updated_at, is_deleted) VALUES ('10000000-0000-0000-0000-000000000002', 'Employee Directory Sample', '<?xml version="1.0" encoding="UTF-8"?>
<employees>
    <employee>
        <employeeId>EMP-001</employeeId>
        <firstName>Alice</firstName>
        <lastName>Wong</lastName>
        <department>Engineering</department>
        <position>Software Engineer</position>
        <salary>6500.00</salary>
        <hireDate>2021-03-15</hireDate>
    </employee>
    <employee>
        <employeeId>EMP-002</employeeId>
        <firstName>Benjamin</firstName>
        <lastName>Chen</lastName>
        <department>Engineering</department>
        <position>Senior Developer</position>
        <salary>8500.00</salary>
        <hireDate>2019-08-01</hireDate>
    </employee>
    <employee>
        <employeeId>EMP-003</employeeId>
        <firstName>Chloe</firstName>
        <lastName>Lim</lastName>
        <department>Marketing</department>
        <position>Marketing Manager</position>
        <salary>7200.00</salary>
        <hireDate>2020-01-10</hireDate>
    </employee>
    <employee>
        <employeeId>EMP-004</employeeId>
        <firstName>Daniel</firstName>
        <lastName>Ong</lastName>
        <department>Sales</department>
        <position>Sales Executive</position>
        <salary>4800.00</salary>
        <hireDate>2022-06-20</hireDate>
    </employee>
    <employee>
        <employeeId>EMP-005</employeeId>
        <firstName>Emma</firstName>
        <lastName>Ng</lastName>
        <department>Finance</department>
        <position>Accountant</position>
        <salary>5600.00</salary>
        <hireDate>2018-11-05</hireDate>
    </employee>
</employees>', '2026-08-21 21:44:18.000000', '2026-08-21 21:44:18.000000', 0);
