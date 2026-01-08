import java.io.IOException;

interface ServerAction {
    StatusManager statusManager();
    LogManager.LogType logType();
    Status.statusEnum initialStatus();
    Status.statusEnum transitionStatus();
    Status.statusEnum finalStatus();
    
    public void actualAction() throws IOException, InterruptedException;
    public boolean checkActionWorked() throws InterruptedException, IOException;

    default boolean run() throws IOException, InterruptedException {
        if(!attemptRun()){
            //Attempt failed enter recovery
            recover();
            //Attempt to perform action again
            return attemptRun();
        }
        return true;
    }

    default void recover() throws IOException, InterruptedException {
        //Sets status to unknown
        statusManager().enterRecovery();
        //Sleep for 30 seconds to let system settle
        Thread.sleep(30000);
        //Checks what state the server is in. Either offline or online
        statusManager().exitRecovery();
    }

    default boolean attemptRun() throws InterruptedException, IOException {
        //Checks if server is at initial status
        if(!(statusManager().getStatusManagerStatus().get() == initialStatus())){
            LogManager.createLog("Status need to be " + initialStatus().toString()+" is currently " + statusManager().getStatusManagerStatus().getString(), "", logType(), true);
            //Recovery not needed as server is already online
            return true;
        }

        //Move to starting server status from offline status
        if(!statusManager().changeStatus(initialStatus(), transitionStatus())){
            LogManager.createLog("Could not move to Transition status" + transitionStatus().toString(), "", logType(), false);
            //Recovery needed as can not change state
            return false;
        }

        //Starts server
        LogManager.createLog("Started Action", "", logType(), true);
        actualAction();

        if(!checkActionWorked()){
            LogManager.createLog("failed Action", "", logType(), false);
            //Recovery as failed to start for some reason
            return false;
        }

        //Move to Online server status from starting status
        if(!statusManager().changeStatus(transitionStatus(), finalStatus())){
            LogManager.createLog("Could not move to final status " + finalStatus().toString(), "", logType(), false);
            //Recovery needed as can not change state
            return false;
        }

        LogManager.createLog("Finished Action", "", logType(), true);
        return true;
    }
}
