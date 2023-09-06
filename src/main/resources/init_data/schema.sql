CREATE TABLE member
(
    member_id     INT AUTO_INCREMENT,
    user_name     VARCHAR(255) NOT NULL,
    remark        VARCHAR(200),
    created_date  TIMESTAMP,
    created_by    VARCHAR(255) DEFAULT 'SYSTEM',
    modified_date TIMESTAMP,
    modified_by   VARCHAR(255) DEFAULT 'SYSTEM',
    PRIMARY KEY (member_id)
);

CREATE TABLE stock
(
    stock_id      INT PRIMARY KEY AUTO_INCREMENT,
    stock_code    VARCHAR(30)  NOT NULL UNIQUE,
    stock_name    VARCHAR(255) NOT NULL,
    remark        VARCHAR(200),
    created_date  TIMESTAMP,
    created_by    VARCHAR(255) DEFAULT 'SYSTEM',
    modified_date TIMESTAMP,
    modified_by   VARCHAR(255) DEFAULT 'SYSTEM'
);

CREATE TABLE stock_price
(
    stock_price_id INT PRIMARY KEY AUTO_INCREMENT,
    stock_id       INT          NOT NULL,
    timestamp      TIMESTAMP(6) NOT NULL,
    time_interval  INT          NOT NULL,
    open_price     INT          NOT NULL,
    close_price    INT          NOT NULL,
    high_price     INT          NOT NULL,
    low_price      INT          NOT NULL,
    volume         INT          NOT NULL,
    remark         VARCHAR(200),
    created_date   TIMESTAMP,
    created_by     VARCHAR(255) DEFAULT 'SYSTEM',
    modified_date  TIMESTAMP,
    modified_by    VARCHAR(255) DEFAULT 'SYSTEM',
    FOREIGN KEY (stock_id) REFERENCES stock (stock_id)
);

CREATE INDEX idx_stock_id_timestamp ON stock_price (stock_id, timestamp);

CREATE TABLE stock_view
(
    stock_view_id INT PRIMARY KEY AUTO_INCREMENT,
    stock_id      INT          NOT NULL,
    member_id     INT          NOT NULL,
    timestamp     TIMESTAMP(6) NOT NULL,
    remark        VARCHAR(200),
    created_date  TIMESTAMP,
    created_by    VARCHAR(255) DEFAULT 'SYSTEM',
    modified_date TIMESTAMP,
    modified_by   VARCHAR(255) DEFAULT 'SYSTEM',
    FOREIGN KEY (stock_id) REFERENCES stock (stock_id),
    FOREIGN KEY (member_id) REFERENCES member (member_id)
);