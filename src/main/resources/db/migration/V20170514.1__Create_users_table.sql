CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR,
  email VARCHAR,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  updated_at TIMESTAMP NOT nULL DEFAULt CURRENT_TIMESTAMP
)