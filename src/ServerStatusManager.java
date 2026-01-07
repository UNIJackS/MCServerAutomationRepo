import java.io.IOException;

public class ServerStatusManager {
    

    public enum serverStatusEnum{
        ONLINE,
        STARTING,
        SHUTTINGDOWN,
        OFFLINE,
        UNKNOWN,
        BACKINGUP
    }

    public ServerStatusManager() throws IOException, InterruptedException{
        checkServerStatus();
    }

    public serverStatusEnum getEnum(){
        return currentStatus;
    }
    public String getString(){
        return currentStatus.name();
    }


    public void checkServerStatus() throws IOException, InterruptedException{
        if(currentStatus != serverStatusEnum.STARTING && currentStatus != serverStatusEnum.SHUTTINGDOWN && currentStatus != serverStatusEnum.BACKINGUP)
        currentStatus = ScriptManager.status();
        LogManager.createLog("Status Change to "+currentStatus," Status change to " + currentStatus+" from checkServerStatus in ServerStatusManager",LogManager.LogType.STATUS,true);
    }

    public boolean initiateStartCheck() throws IOException, InterruptedException{
        checkServerStatus();
        if(currentStatus == serverStatusEnum.OFFLINE){
            changeStatus(serverStatusEnum.STARTING);
            LogManager.createLog("Status Change to "+currentStatus," Status change to " + currentStatus+" from checkServerStatus in ServerStatusManager",LogManager.LogType.STATUS,true);
            return true;
        }
        return false;
    }

    public boolean finishStartCheck() throws IOException, InterruptedException{
        if(ScriptManager.status() == serverStatusEnum.ONLINE){
            changeStatus(serverStatusEnum.ONLINE);
            return true;
        }
        return false;
    }

    private serverStatusEnum currentStatus = serverStatusEnum.UNKNOWN;
    private void changeStatus(serverStatusEnum newStatus){
        currentStatus = newStatus;
        LogManager.createLog("Status Change to "+currentStatus," Status change to " + currentStatus+" from checkServerStatus in ServerStatusManager",LogManager.LogType.STATUS,true);
    }
    
}
