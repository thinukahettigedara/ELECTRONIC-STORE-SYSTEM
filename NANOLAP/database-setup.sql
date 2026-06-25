-- ===================================================
-- NANOLAP - SQL Server Database Setup Script
-- Run this in SSMS before starting the application
-- ===================================================

-- Create Database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'NanolapDB')
BEGIN
    CREATE DATABASE NanolapDB;
    PRINT 'NanolapDB created successfully!';
END
GO

USE NanolapDB;
GO

-- Products Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='products' AND xtype='U')
BEGIN
    CREATE TABLE products (
        id              BIGINT IDENTITY(1,1) PRIMARY KEY,
        name            NVARCHAR(200)   NOT NULL,
        description     NVARCHAR(MAX),
        price           DECIMAL(10,2)   NOT NULL,
        original_price  DECIMAL(10,2),
        category        NVARCHAR(100)   NOT NULL,
        brand           NVARCHAR(100),
        image_url       NVARCHAR(500),
        stock_quantity  INT             DEFAULT 0,
        rating          FLOAT           DEFAULT 0.0,
        review_count    INT             DEFAULT 0,
        is_featured     BIT             DEFAULT 0,
        is_active       BIT             DEFAULT 1,
        created_at      DATETIME2       DEFAULT GETDATE(),
        updated_at      DATETIME2       DEFAULT GETDATE()
    );
    PRINT 'products table created!';
END
GO

-- Orders Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='orders' AND xtype='U')
BEGIN
    CREATE TABLE orders (
        id              BIGINT IDENTITY(1,1) PRIMARY KEY,
        order_number    NVARCHAR(50)    UNIQUE,
        customer_name   NVARCHAR(200)   NOT NULL,
        customer_email  NVARCHAR(200)   NOT NULL,
        customer_phone  NVARCHAR(20),
        shipping_address NVARCHAR(MAX),
        total_amount    DECIMAL(10,2),
        status          NVARCHAR(50)    DEFAULT 'PENDING',
        payment_method  NVARCHAR(50),
        created_at      DATETIME2       DEFAULT GETDATE()
    );
    PRINT 'orders table created!';
END
GO

-- Order Items Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='order_items' AND xtype='U')
BEGIN
    CREATE TABLE order_items (
        id          BIGINT IDENTITY(1,1) PRIMARY KEY,
        order_id    BIGINT          NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
        product_id  BIGINT          NOT NULL REFERENCES products(id),
        quantity    INT             NOT NULL,
        unit_price  DECIMAL(10,2),
        total_price DECIMAL(10,2)
    );
    PRINT 'order_items table created!';
END
GO

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_products_brand ON products(brand);
CREATE INDEX IF NOT EXISTS idx_products_active ON products(is_active);
CREATE INDEX IF NOT EXISTS idx_orders_email ON orders(customer_email);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
GO

-- Backfill missing order numbers (1, 2, 3, ... based on id)
UPDATE orders
SET order_number = CAST(id AS NVARCHAR(50))
WHERE order_number IS NULL OR order_number = 'NL-null';
GO

PRINT '========================================';
PRINT 'NanolapDB setup complete!';
PRINT 'Now update application.properties with your SQL Server credentials';
PRINT '========================================';
