-- Create the database
CREATE DATABASE Woolworths;
GO

-- Use the newly created database
USE Woolworths;
GO

-- Users/Customers table
CREATE TABLE users (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    date_of_birth DATE,
    created_at DATETIME DEFAULT GETDATE(),
    last_login DATETIME,
    is_active BIT DEFAULT 1
);
GO

-- User addresses table
CREATE TABLE user_addresses (
    address_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) DEFAULT 'Australia',
    is_primary BIT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
GO

-- Product categories table
CREATE TABLE categories (
    category_id INT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(100) NOT NULL,
    description NVARCHAR(MAX),
    parent_category_id INT,
    image_url VARCHAR(255),
    FOREIGN KEY (parent_category_id) REFERENCES categories(category_id)
);
GO

-- Products table
CREATE TABLE products (
    product_id INT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    price DECIMAL(10, 2) NOT NULL,
    discounted_price DECIMAL(10, 2),
    category_id INT NOT NULL,
    brand VARCHAR(100),
    unit VARCHAR(50), -- e.g., "each", "kg", "500g pack"
    stock_quantity INT DEFAULT 0,
    min_order_quantity INT DEFAULT 1,
    max_order_quantity INT DEFAULT 10,
    image_url VARCHAR(255),
    rating DECIMAL(2, 1) DEFAULT 0,
    review_count INT DEFAULT 0,
    is_active BIT DEFAULT 1,
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);
GO

-- 1. Create the product_variants table
CREATE TABLE product_variants (
    variant_id INT PRIMARY KEY IDENTITY(1,1),
    product_id INT NOT NULL,
    sku VARCHAR(255) UNIQUE NOT NULL, -- Stock Keeping Unit
    name VARCHAR(100) NOT NULL, -- e.g., "Size", "Color"
    value VARCHAR(100) NOT NULL, -- e.g., "Large", "Red"
    price_modifier DECIMAL(10, 2) DEFAULT 0,
    stock_quantity INT DEFAULT 0,
    image_url VARCHAR(255),
    is_active BIT DEFAULT 1,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);
GO

-- 2. Create the delivery_slots table
CREATE TABLE delivery_slots (
    slot_id INT PRIMARY KEY IDENTITY(1,1),
    slot_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    max_orders INT DEFAULT 50,
    current_orders INT DEFAULT 0,
    is_active BIT DEFAULT 1
);
GO

-- 3. Create the delivery_addresses table
CREATE TABLE delivery_addresses (
    delivery_address_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) DEFAULT 'Australia',
    is_default BIT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
GO

-- Shopping cart table
CREATE TABLE cart (
    cart_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
GO

-- Orders table
CREATE TABLE orders (
    order_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    order_date DATETIME DEFAULT GETDATE(),
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'Pending',
    shipping_address_id INT NOT NULL,
    payment_method VARCHAR(50),
    payment_status VARCHAR(50) DEFAULT 'Pending',
    tracking_number VARCHAR(100),
    estimated_delivery DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (shipping_address_id) REFERENCES user_addresses(address_id)
);
GO

-- Order items table
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY IDENTITY(1,1),
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
GO

-- Payments table
CREATE TABLE payments (
    payment_id INT PRIMARY KEY IDENTITY(1,1),
    order_id INT NOT NULL,
    payment_date DATETIME DEFAULT GETDATE(),
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(255),
    status VARCHAR(50) DEFAULT 'Pending',
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
GO

-- Product reviews table
CREATE TABLE reviews (
    review_id INT PRIMARY KEY IDENTITY(1,1),
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment NVARCHAR(MAX),
    review_date DATETIME DEFAULT GETDATE(),
    is_approved BIT DEFAULT 0,
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
GO

-- Wishlist table
CREATE TABLE wishlist (
    wishlist_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    added_date DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
GO

-- Promotions table
CREATE TABLE promotions (
    promotion_id INT PRIMARY KEY IDENTITY(1,1),
    code VARCHAR(50) UNIQUE NOT NULL,
    description NVARCHAR(MAX),
    discount_type VARCHAR(20),
    discount_value DECIMAL(10, 2) NOT NULL,
    min_order_amount DECIMAL(10, 2) DEFAULT 0,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    usage_limit INT DEFAULT NULL,
    times_used INT DEFAULT 0,
    is_active BIT DEFAULT 1
);
GO


--Alter users Table
ALTER TABLE `users`
ADD COLUMN `title` VARCHAR(10) NULL AFTER `password_hash`;

ALTER TABLE `users`
ADD COLUMN `first_name` VARCHAR(100) NULL AFTER `title`;

ALTER TABLE `users`
ADD COLUMN `last_name` VARCHAR(100) NULL AFTER `first_name`;
