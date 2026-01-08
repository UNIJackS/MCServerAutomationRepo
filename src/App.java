public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Server Automation script started");
        System.out.println("Args :" + args);
        setup();

        ServerManager serverManager = new ServerManager();
        serverManager.start();

        while(true){
            Status currentStatus = serverManager.getServerStatus();
            switch(currentStatus.get()){
                case Status.statusEnum.ONLINE:
                    //Check if backup needed
                    serverManager.backup();
                    break;

                case Status.statusEnum.OFFLINE:
                    //Start up server
                    serverManager.start();
                    break;

                default:
                    serverManager.reccover();
                    break;
            }
            System.out.println("waiting 5 mins ...");
            Thread.sleep(300000);
        }

    }

    private static void setup() throws Exception{
        System.out.println("Setup beginning ...");
        LogManager.ensureLoggingDirectorysExist();
        //ScriptManager.ensureScriptDirectoryExists();
        System.out.println("Setup finished ...");
    }
}


