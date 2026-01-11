import java.io.IOException;

public record Start(StatusManager statusManager) implements ServerAction {
    @Override
    public LogManager.LogType logType() {
        return LogManager.LogType.STARTSERVER;
    }

    @Override
    public Status.statusEnum initialStatus() {
        return Status.statusEnum.OFFLINE;
    }

    @Override
    public Status.statusEnum transitionStatus() {
        return Status.statusEnum.STARTING;
    }

    @Override
    public Status.statusEnum finalStatus() {
        return Status.statusEnum.ONLINE;
    }

    @Override
    public void actualAction() throws IOException, InterruptedException {
        //Starts server
        ScriptManager.start();
    }

    @Override
    public boolean checkActionWorked() throws InterruptedException, IOException {
        //Loops untill the screen comes online
        for(int timeOut = 10; !(ScriptManager.status() == Status.statusEnum.ONLINE); timeOut -=1){
            //Waits 5 seconds
            Thread.sleep(5000);

            //If the sevrer checks 10 times and screen is not up then failed.
            if(timeOut <= 0){return false;}
        }

        //Wait for 10mins for sever to actually start in the screen
        // 600000ms = 10mins
        Thread.sleep(600000);
        return true;

    }
}
