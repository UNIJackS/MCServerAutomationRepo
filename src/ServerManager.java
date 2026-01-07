import java.io.IOException;

public class ServerManager {
    private ServerStatusManager serverStatusManager;

    public ServerManager() throws IOException, InterruptedException{
         serverStatusManager = new ServerStatusManager();
    }

    public boolean startServer() throws IOException, InterruptedException{
        System.out.println("Current server status :" + serverStatusManager.getString());

        if(serverStatusManager.initiateStartCheck()){
            LogManager.createLog("Starting Server", "Starting server from startServer method in ServerManager class", LogManager.LogType.SERVERSTARTED, true);
            ScriptManager.start();
        }else{
            LogManager.createLog("Not Starting Server", "Not starting server as Server is not offline so can not start from startServer method in ServerManager class", LogManager.LogType.SERVERSTARTED, false);
            return false;
        }
 
        Thread.sleep(10000);

        finishStartCheck()

        return true;
    }


    public boolean stopServer(){
        return true;
    }


    public boolean backupServer(){
        return true;
    }



}
