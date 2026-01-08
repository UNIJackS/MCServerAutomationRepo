import java.io.IOException;

public record Stop(StatusManager statusManager) implements ServerAction {
    @Override
    public LogManager.LogType logType() {
        return LogManager.LogType.STOPSERVER;
    }

    @Override
    public Status.statusEnum initialStatus() {
        return Status.statusEnum.ONLINE;
    }

    @Override
    public Status.statusEnum transitionStatus() {
        return Status.statusEnum.STOPPING;
    }

    @Override
    public Status.statusEnum finalStatus() {
        return Status.statusEnum.OFFLINE;
    }

    @Override
    public void actualAction() throws IOException, InterruptedException {
        //Stops server
        ScriptManager.say("Server Shutting down in 1 min");
        Thread.sleep(30000);
        ScriptManager.say("Server Shutting down in 30 sec");
        Thread.sleep(30000);
        ScriptManager.stop();
        //Wait for chunks to save for 15 seconds
        Thread.sleep(15000);
        ScriptManager.closeScreen();
    }

    @Override
    public boolean checkActionWorked() throws InterruptedException, IOException {
        for(int timeOut = 10; !(ScriptManager.status() == Status.statusEnum.OFFLINE); timeOut -=1){
            Thread.sleep(5000);
            if(timeOut <= 0){
                //Server has not stopped
                return false;
            }
        }
        return true;

    }
}


