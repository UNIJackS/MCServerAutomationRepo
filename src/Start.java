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
        for(int timeOut = 10; !(ScriptManager.status() == Status.statusEnum.ONLINE); timeOut -=1){
            Thread.sleep(5000);
            if(timeOut <= 0){
                //Server has not come up
                return false;
            }
        }

        Thread.sleep(5000);
        return true;

    }
}
