const { rateLimit } = require("express-rate-limit") //object destructuring, so it seems the module returns an object. 
                                                    // In other examples, the modules were returning functions, etc.
const bcrypt = require("bcrypt"); //for hashing passwords




const express = require("express"); //same as import x from 'express'? //import express from 'express'
                                    //require function to load in modules (installed packages).
                                    //the require function returns the exported contents from that module. In this case, the express module exports a function.
                                    //express variable is assigned the return value of the require function. (it returns the exported content of the module, express)
                                    //express module exports a function, specifically createApplication
const pool = require("./db"); //Loading in the db module which is found at this path. pool variable is assigned the return value of the function (the exported contents of the module)

const app = express(); //app variable is assigned the return value of the express function (app, which is a function).
                       //Eventually at some point, app function gets added properties that we can use.
const PORT = process.env.PORT || 3000;

app.use(express.json({limit: "10kb"})); //[=] Review use and json functions, and their arguments
                                        //[=] Review significance of request limitation here vs with server code (username/password length limitation)

const createAccountLimiter = rateLimit({ //argument type for this function is an object
  windowMs: 15 * 60 * 1000,
  limit: 5,
  message: "Too many account creation attempts. Try again later.",
});









//This represents the server receiving and handling a POST request which happens at this route path.
//URL routing
//Maps the URL you want to go to/are requesting to the code/resource/etc.
//So going to the URL ending with path /create-account would give me 
//This function is an example of a handler. It handles the event/action (the user reaching/requesting from that URL, sending a POST request)

//Pretty sure this is saying that
//When a POST request at this URL,...,perform these steps.
app.post("/create-account", createAccountLimiter, async (req, res) => { //Added an additional argument (createAccountLimiter), what is its purpose in the post function? (Read docs). What is it considered as, middleware? (What is middleware)
  const { username, password } = req.body; //object destructuring, setting the variables as the values from the body property (also an object) from the request object.

  if (!username || !password) {
    return res.status(400).send("Username and password are required.");
  }

  if (username.length < 5 || username.length > 20) {
    return res.status(400).send("Username must be between 5 and 20 characters.");
  }

  if (password.length < 5 || password.length > 20) {
    return res.status(400).send("Password must be between 5 and 20 characters.");
  }

  //This wil help prevent empty spaces, and other undesired characters from being accepted.
  if (!/^[a-zA-Z0-9_]+$/.test(username)) { //regular expression object being accessed.
    return res.status(400).send("Username can only contain letters, numbers, and underscores.");
  }

  try {
    const [existingUsers] = await pool.query( //[=] Review "[existingUsers]" syntax
      "SELECT user_id FROM users WHERE username = ?", //[=]
      [username]
    );

    if (existingUsers.length > 0) {
      return res.status(409).send("Username already exists."); //[=]
    }

    const passwordHash = await bcrypt.hash(password, 10); //VS Code shows the package's type definitons (Union Types?). -> hash(data: string | Buffer, saltOrRounds: string | number): Promise<string> (Promise<string>) is the return type

    await pool.query(
      "INSERT INTO users (username, password) VALUES (?, ?)",
      [username, passwordHash]
    );

    res.status(201).send("Account created successfully.");

  } catch (error) {
    console.error(error);
    res.status(500).send("Server error.");
  }
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});

