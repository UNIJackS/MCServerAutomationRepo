import java.io.IOException;
import java.time.LocalDate;

public class Backup extends Action{
    public Backup(StatusManager serverStatusManager) {
        super(serverStatusManager);
    }

    @Override
    protected boolean attemptRun() throws InterruptedException, IOException {
        //Check that server is online
        if(serverStatusManager.getStatus().online()){
            LogManager.createLog("Not Backing up Server", "Not Backingup server as Server is not online so can not start from attemptRun method in Backup class", LogManager.LogType.SERVERSTARTED, true);
            return true;
        }
        //Stop the server
        if(!new Stop(serverStatusManager).run()){
            LogManager.createLog("Not Backing up Server", "Not Backingup server as Server will not stop so can not start from attemptRun method in Backup class", LogManager.LogType.SERVERSTARTED, false);
            return false;
        }
        //Move it to backingup status
        if(!startCheck()){
            //Should be offline but if not then not sure whats going on so reccover
            return false;
        }
        LocalDate currentDate = LocalDate.now();
        ScriptManager.backup("/"+currentDate.toString());


        new Start(serverStatusManager).run();
        return true;
    }

    @Override
    protected boolean startCheck() throws InterruptedException, IOException {
        return serverStatusManager.changeStatus(Status.statusEnum.OFFLINE,Status.statusEnum.BACKINGUP);
    }

    @Override
    protected boolean finishCheck() throws InterruptedException, IOException {
        return false;
    }
}
