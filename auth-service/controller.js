const jwt = require("jsonwebtoken");
const { jwtSecretKey } = require("./env.js");
const { getUserByUserName } = require("./userService.js");
const bcrypt = require("bcryptjs");

const loginController = async (req, res) => {
  const { username, password } = req.body;

  if (!username || !password) {
    return res.sendStatus(400);
  }

  try {
    // fetch user by username
    const user = await getUserByUserName(username);

    if (!user) {
      return res.sendStatus(400);
    }

    const userId = user.id;
    const role = user.role;
    const hashedPassword = user.password;

    // compare password
    const isMathch = await bcrypt.compare(password, hashedPassword);
    if (!isMathch) {
      return res.sendStatus(400);
    }

    // generate jwt token
    const token = jwt.sign({ userId, role }, jwtSecretKey, {
      subject: String(userId),
      expiresIn: "1h",
      algorithm: "HS256",
    });

    res.json({ token });
  } catch (error) {
    res.sendStatus(500);
  }
};

/**
 *
 * @param {import("express").Request} req
 * @param {import("express").Response} res
 */
const validateController = (req, res) => {
  const token = req.headers.authorization;

  // check if token is present and well formed
  if (!token || !token.startsWith("Bearer ")) {
    return res.sendStatus(400);
  }

  const jwtToken = token.split(" ")[1];

  if (jwtToken.length < 10) {
    return res.sendStatus(400);
  }

  jwt.verify(jwtToken, jwtSecretKey, (err, decoded) => {
    if (err) {
      return res.sendStatus(401);
    }

    const userId = decoded.userId;
    const role = decoded.role;

    if (!userId || !role) {
      return res.sendStatus(401);
    }

    res.setHeader("x-user-id", userId);
    res.setHeader("x-user-role", role);

    return res.json({
      valid: true,
      userId: userId,
      roles: [role],
    });
  });
};

module.exports = {
  loginController,
  validateController,
};
