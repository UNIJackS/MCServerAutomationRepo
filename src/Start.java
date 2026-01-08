import java.io.IOException;

public class Start extends Action{

    public Start(StatusManager serverStatusManager) {
        super(serverStatusManager);
    }

    @Override
    protected boolean attemptRun() throws InterruptedException, IOException {
        System.out.println("Status at call:" + serverStatusManager.getStatusString());
        if(startCheck()){
            LogManager.createLog("Not Starting Server", "Not starting server as Server is not offline so can not start from attemptStart method in ServerManager class", LogManager.LogType.SERVERSTARTED, true);
            return true;
        }
        System.out.println("Status after check :" + serverStatusManager.getStatusString());
        LogManager.createLog("Starting Server", "Starting server from attemptStart method in ServerManager class", LogManager.LogType.SERVERSTARTED, true);
        ScriptManager.start();

        for(int timeOut = 10; !finishCheck(); timeOut -=1){
            System.out.println("Status fail finish check :" + serverStatusManager.getStatusString());
            Thread.sleep(5000);
            if(timeOut <= 0){
                //Server has not come up
                return false;
            }
        }
        return true;
    }


    @Override
    protected boolean startCheck() throws InterruptedException, IOException {
        return serverStatusManager.changeStatus(Status.statusEnum.OFFLINE,Status.statusEnum.STARTING);
    }

    @Override
    protected boolean finishCheck() throws InterruptedException, IOException {
        //Check server has come up
        if(ScriptManager.status() == Status.statusEnum.ONLINE){
            //If it has change the state to online from starting
            return serverStatusManager.changeStatus(Status.statusEnum.STARTING,Status.statusEnum.ONLINE);
        }
        return false;
    }
}
