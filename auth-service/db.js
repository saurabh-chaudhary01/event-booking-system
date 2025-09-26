const pg = require("pg");

const { db_host, db_name, db_user, db_password, db_port } = require("./env.js");

const pool = new pg.Pool({
  host: db_host,
  database: db_name,
  user: db_user,
  password: db_password,
  port: db_port,
});

module.exports = pool;
