const envObj = {
  jwtSecretKey: process.env.JWT_SEC_KEY,
  db_host: process.env.DB_HOST,
  db_name: process.env.DB_NAME,
  db_user: process.env.DB_USER,
  db_password: process.env.DB_PASSWORD,
  db_port: process.env.DB_PORT,
};

module.exports = envObj;
