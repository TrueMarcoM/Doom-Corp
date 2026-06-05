//this java app represents the client.
//recall clients make HTTP requests to a server.
//we need packages that allow us to achieve these client-server communications in the app.
//Confirm with official documentation later

import java.io.*;
import java.util.Scanner;
import java.net.URI; //this is used for the address of the API endpoint. Its how we'll store the destination url? //a string isnt enough?
import java.net.http.HttpClient; //this is used to represent the object that actually sends the request. We are going to make a HTTP client object. (our app?)
import java.net.http.HttpRequest; //this is used for the app (client) to send HTTP requests to the server.
import java.net.http.HttpResponse; //this is used for the app (client) to store the HTTP responses from the server.

public class DoomCorpApp {
    static final String API_BASE_URL = "http://localhost:3000";

    public static void main(String[] args) throws IOException, InterruptedException {
        displayTitle();
        displayMenu();
    }

    static void displayTitle() throws IOException {
        File inputFile = new File("DoomCorpTitleASCII.txt");
        Scanner inputFileScanner = new Scanner(inputFile);

        while (inputFileScanner.hasNextLine()) {
            String line = inputFileScanner.nextLine();
            System.out.println(line);
        }
        System.out.println();
        inputFileScanner.close();
    }

    static void displayMenu() { //this is the authentication menu
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
        
    }

    static void createAccount() {
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
        
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE_URL + "/create-account"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Server response: " + response.body());
    
    }
}

    
    