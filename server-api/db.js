require("dotenv").config(); // this again?

const mysql = require("mysql2/promise"); //we're specifically loading in the promise.js file. Although do I still have access to the rest of the package? (Easy check)
//Im assuming something more general like require("mysql2") allows me to call anything from any js file.


const pool = mysql.createPool({
  host: process.env.DB_HOST,
  port: Number(process.env.DB_PORT),
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

module.exports = pool; //means this module object (which refers to this module) will export whatever it is set to. Which is the pool variable, which is a function call.
                       //it is initializing the exports field to pool.
                       //(What data type is exports? Maybe its a variable holding a function, I doubt it) 