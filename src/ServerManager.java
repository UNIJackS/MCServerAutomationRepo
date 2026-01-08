import java.io.IOException;

public class ServerManager {
    private StatusManager serverStatusManager;

    public ServerManager() throws IOException, InterruptedException{
         serverStatusManager = new StatusManager();
    }

    public boolean start() throws IOException, InterruptedException {
        return new Start(serverStatusManager).run();
    }

    public boolean stop() throws IOException, InterruptedException {
        return new Stop(serverStatusManager).run();
    }

    public boolean backup() throws IOException, InterruptedException {
        return new Backup(serverStatusManager).run();
    }


    public boolean backupServer(){
        return true;
    }


}
