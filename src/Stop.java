import java.io.IOException;

public class Stop extends Action{
    public Stop(StatusManager serverStatusManager) {
        super(serverStatusManager);
    }

    @Override
    protected boolean attemptRun() throws InterruptedException, IOException {
        if(!startCheck()){
            LogManager.createLog("Not Stopping Server", "Not Stopping server as Server is not online so can not start from attemptStop method in ServerManager class", LogManager.LogType.SERVERSTARTED, true);
            return true;
        }
        LogManager.createLog("Stopping Server", "Stopping server from attemptStop method in ServerManager class", LogManager.LogType.SERVERSTARTED, true);
        ScriptManager.say("Server Shutting down in 1 min");
        Thread.sleep(30000);
        ScriptManager.say("Server Shutting down in 30 sec");
        Thread.sleep(30000);
        ScriptManager.stop();
        //Wait for chunks to save for 15 seconds
        Thread.sleep(15000);
        ScriptManager.closeScreen();

        for(int timeOut = 10; !finishCheck(); timeOut -=1){
            Thread.sleep(5000);
            if(timeOut <= 0){
                //Server has not stopped
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean startCheck() throws InterruptedException, IOException {
        return serverStatusManager.changeStatus(Status.statusEnum.ONLINE,Status.statusEnum.SHUTTINGDOWN);
    }

    @Override
    protected boolean finishCheck() throws InterruptedException, IOException {
        //Check server has shut down
        if(ScriptManager.status() == Status.statusEnum.OFFLINE){
            //If it has change the state to online from starting
            return serverStatusManager.changeStatus(Status.statusEnum.SHUTTINGDOWN,Status.statusEnum.OFFLINE);
        }
        return false;
    }


}
