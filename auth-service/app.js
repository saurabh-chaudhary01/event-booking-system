const express = require("express");
const cors = require("cors");

const { loginController, validateController } = require("./controller.js");

const app = express();

app.use(cors());
app.use(express.json());

app.get("/api/auth/status", (_, res) => {
  return res.json({
    status: "up",
    message: "welcome to auth-service",
  });
});

app.post("/api/auth/login", loginController);
app.post("/api/auth/validate", validateController);

app.listen(3000, () => {
  console.log("auth server running at port 3000");
});
