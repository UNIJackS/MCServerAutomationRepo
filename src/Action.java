import java.io.IOException;

public abstract class Action {
    public final boolean run () throws IOException, InterruptedException{
        if(!attemptRun()){
            //Attempt to start failed. Enter recovery mode
            recover();
            //Attempt to start the server again
            return attemptRun();
        }
        return true;
    };

    public StatusManager serverStatusManager;
    public Action(StatusManager serverStatusManager){
        this.serverStatusManager =serverStatusManager;
    }

    protected abstract boolean attemptRun() throws InterruptedException, IOException;
    protected abstract boolean startCheck() throws InterruptedException, IOException;
    protected abstract boolean finishCheck() throws InterruptedException, IOException;


    private void recover() throws IOException, InterruptedException {
        //Sets status to unknown
        serverStatusManager.enterRecovery();
        //Sleep for 30 seconds to let system settle
        Thread.sleep(30000);
        //Checks what state the server is in. Either offline or online
        serverStatusManager.exitRecovery();
    }

}
