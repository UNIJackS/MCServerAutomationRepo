import java.io.IOException;

public class ServerManager {
    private StatusManager serverStatusManager;

    public ServerManager() throws IOException, InterruptedException{
         serverStatusManager = new StatusManager();
    }

    public Status getServerStatus(){
        return serverStatusManager.getStatusManagerStatus();
    }

    public boolean start() throws IOException, InterruptedException {
        return new Start(serverStatusManager).run();
    }

    public boolean stop() throws IOException, InterruptedException {
        return new Stop(serverStatusManager).run();
    }

    public boolean backup() throws IOException, InterruptedException {
        //If it has been more than a day since the last backup
        //Then run another backup
        if(Backup.timeSinceLastBackup().toDays() >1){
            return new Backup(serverStatusManager).run();
        }
        return true;
    }

    public boolean reccover() throws IOException, InterruptedException {
        return new Recover(serverStatusManager).run();
    }

}
