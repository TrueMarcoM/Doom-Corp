//review this, fully/better understand it.
//Server code, uses a bunch of HTTP requests right?
//client/server communication?

require("dotenv").config(); //?

const express = require("express");
const cors = require("cors");
const bcrypt = require("bcrypt");
const pool = require("./db");

const app = express();