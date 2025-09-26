const pool = require("./db.js");

/**
 * Check if a user exists with given username and password.
 * @param {string} username
 * @param {string} password
 * @returns {Promise<Object|null>} user row or null if not found
 */
async function getUserByUserName(username, password) {
  const query = "SELECT * FROM user_entity WHERE email = $1";
  const values = [username];

  const result = await pool.query(query, values);

  if (result.rows.length > 0) {
    return result.rows[0];
  } else {
    return null;
  }
}

module.exports = { getUserByUserName };
