//this java app represents the client.
//recall clients make HTTP requests to a server.
//we need packages that allow us to achieve these client-server communications in the app.
//Confirm with official documentation later

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient; //this is used for the address of the API endpoint. Its how we'll store the destination url? //a string isnt enough?
import java.net.http.HttpRequest; //this is used to represent the object that actually sends the request. We are going to make a HTTP client object. (our app?)
import java.net.http.HttpResponse; //this is used for the app (client) to send HTTP requests to the server.
import java.util.Scanner; //this is used for the app (client) to store the HTTP responses from the server.

//[=] Review "throws" keyword.
//[=] Review throw/catch exceptions


public class DoomCorpApp {
    // static final String API_BASE_URL = "http://localhost:3000"; //The base URL of the server (no path yet)
    static final String API_BASE_URL = "https://doomcorp-1074518043351.us-west2.run.app";

    public static void main(String[] args) throws IOException, InterruptedException {
        displayTitle();
        displayMenu();
    }

    static void displayTitle() throws IOException {
        File inputFile = new File("./assets/DoomCorpTitleASCII.txt");
        Scanner inputFileScanner = new Scanner(inputFile);

        while (inputFileScanner.hasNextLine()) {
            String line = inputFileScanner.nextLine();
            System.out.println(line);
        }
        System.out.println();
        inputFileScanner.close();
    }

    static void displayMenu() throws IOException, InterruptedException { //this is the authentication menu
        Scanner keyboardScanner = new Scanner(System.in);
        System.out.println("1] Login");
        System.out.println("2] Create Account");
        System.out.println("3] Exit");

        int userChoice = keyboardScanner.nextInt();
        //When do I close the scanner?
        //Scanner for a file? Close it.
        //Scanner for System.in? Usually leave it open until the whole program is done.

        if (userChoice == 1) {
            //display the login screen
            login();
        } else if (userChoice == 2) {
            //display the create account screen
            createAccount();
        } else if (userChoice == 3) {
            //display the exit screen/exit program
        }
    }
    //Supposedly the password condition checking isnt that important. Its mainly important on the server. 

    //However still important. I want them to retype their credentials, or display a new screen based on condiitions (if credentials valid or not). And that would require checking the conditions.
    

    static void login() {
        System.out.println("LOGIN");
        System.out.println("Username: ");
        Scanner keyboardScanner = new Scanner(System.in);
        String username = keyboardScanner.nextLine();
        System.out.println("Password: ");
        String password = keyboardScanner.nextLine();
        //User entered username and password are stored in variables. We're going to send these values to the server in some way.
        //I believe we're going to make an HTTP request which includes these credentials.
        //I imagine that's one way requests work, you have to provide something along with the request (credentials).
        //Then you may get the response you want. 
        //Atm, I have stored client information?
        System.out.println("username and password fields updated. No validation at the moment.");
        
    }

    static void createAccount() throws IOException, InterruptedException {
        //Normally these type of functions return feedback to the user.


        //Input validation / format checks: (Client app checking?)
        // - Is username empty?
        // - Is username too long?
        // - Is password too short?
        // - Does it contain allowed characters?

        // Credential authentication: (Server app checking?)
        // - Does this username exist?
        // - Does this password match the stored password for that user?

        // Database constraints:
        // - Username must be unique
        // - Username cannot be null
        // - Column has max size like VARCHAR(20)

        //Im assuming the condition checking and such in this client code is a subset of the conditions in the server code.

        System.out.println("CREATE ACCOUNT");

        System.out.println("Username: ");
        Scanner keyboardScanner = new Scanner(System.in);
        String username = keyboardScanner.nextLine();

        System.out.println("Password: ");
        String password = keyboardScanner.nextLine();


        if (!(username.length() >= 5 && username.length() <= 20)) {
            System.out.println("Username must be between 5 and 20 characters ");
            return; //To end the function, anything after this wouldnt be run.
        }

        if (!(password.length() >= 5 && password.length() <= 20)) {
            System.out.println("Password must be between 5 and 20 characters ");
            return; //To end the function, anything after this wouldnt be run.
        }
        //Using string format specifiers
        //Note how the json variable only gets populated with valid formatted credentials.
        String json = """
            {
                "username": "%s",
                "password": "%s"
            }
        """.formatted(username, password);

        //Review these classes/objects.
        //This code builds an HTTP request. It does not send it yet.
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder() //newBuilder returns 
            .uri(URI.create(API_BASE_URL + "/create-account")) //this is the URL/path we're going to make the request to. Destination.
            .header("Content-Type", "application/json") //We're setting the Content-Type field from the header to application/json value. Recall packets have headers. 
                                                        //However, why applications/json, why not just JSON?
                                                        //So this is the type (content) of the data we will send.
            .POST(HttpRequest.BodyPublishers.ofString(json))//This means we're settling on a POST request. We're going to be attach the json string we made into the request body.
                                                        //Briefly review different types of requests.
            .build();                                   //This finalizes the request building.

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //[=] Review generic types. Review Object class
                                                    //client.send() can throw an IOException.
        System.out.println("Server response: " + response.body()); //body() returns the body. (In this case, the body of the response).
    

        //client.send() is the one that actually sends the request. send() returns the response.
    }
}

    
    