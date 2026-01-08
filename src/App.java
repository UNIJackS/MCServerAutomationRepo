

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Server Automation script started");
        System.out.println("Args :" + args);
        setup();
        
        //LogManager.createLog("seems legit","Seems legit test log",LogManager.LogType.SERVERSTARTED,true);

        //ServerManager serverManager = new ServerManager();

        System.out.println("Attempting to start ...");

        ScriptManager.start();

    }

    private static void setup() throws Exception{
        System.out.println("Setup begginging ...");
        LogManager.ensureLoggingDirectorysExist();
        //ScriptManager.ensureScriptDirectoryExists();
        System.out.println("Setup finished ...");
    }
}


