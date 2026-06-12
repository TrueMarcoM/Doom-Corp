//Server code, uses a bunch of HTTP requests right?
//client/server communication?

//this is code that will be on the server. So im giving the server instructions on what to give exactly?


//TODO:
/* Setup te google cloud server for which will be the web api?*/

//Fully understand each object/methods purpose.

//Atm will set up local server communication
const express = require("express"); //same as import x from 'express'? //import express from 'express'
                                    //require function to load in modules (installed packages).
                                    //the require function returns the exported contents from that module. In this case, the express module exports a function.
                                    //express variable is assigned the return value of the require function. (it returns the exported content of the module, express)
                                    //express module exports a function, specifically createApplication
const pool = require("./db"); //Loading in the db module which is found at this path. pool variable is assigned the return value of the function (the exported contents of the module)

const app = express(); //app variable is assigned the return value of the express function (app, which is a function).
                       //Eventually at some point, app function gets added properties that we can use.
const PORT = process.env.PORT || 3000;

app.use(express.json()); //-- review use and json functions

//This represents the server receiving and handling a POST request which happens at this route path.
//URL routing
//Maps the URL you want to go to/are requesting to the code/resource/etc.
//So going to the URL ending with path /create-account would give me 
//This function is an example of a handler. It handles the event/action (the user reaching/requesting from that URL, sending a POST request)

//Pretty sure this is saying that
//When a POST request at this URL,...,perform these steps.
app.post("/create-account", async (req, res) => { 
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

  try {
    const [existingUsers] = await pool.query(
      "SELECT user_id FROM users WHERE username = ?",
      [username]
    );

    if (existingUsers.length > 0) {
      return res.status(409).send("Username already exists.");
    }

    await pool.query(
      "INSERT INTO users (username, password) VALUES (?, ?)",
      [username, password]
    );

    res.status(201).send("Account created successfully.");

  } catch (error) {
    console.error(error);
    res.status(500).send("Server error.");
  }
});

app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});

