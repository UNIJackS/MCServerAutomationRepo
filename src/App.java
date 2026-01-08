import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Server Automation script started");
        System.out.println("Args :" + args);
        setup();
        


        System.out.println("Attempting to start ...");

        ServerManager serverManager = new ServerManager();
        serverManager.start();

        Scanner scanner = new Scanner(System.in); // Create a Scanner object

        System.out.println("Enter to contnue :");
        String fullName = scanner.nextLine();
        serverManager.stop();
    }

    private static void setup() throws Exception{
        System.out.println("Setup begginging ...");
        LogManager.ensureLoggingDirectorysExist();
        //ScriptManager.ensureScriptDirectoryExists();
        System.out.println("Setup finished ...");
    }
}


