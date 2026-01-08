import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record Backup(StatusManager statusManager) implements ServerAction {
    @Override
    public LogManager.LogType logType() {
        return LogManager.LogType.BACKUP;
    }

    @Override
    public Status.statusEnum initialStatus() {
        return Status.statusEnum.OFFLINE;
    }

    @Override
    public Status.statusEnum transitionStatus() {
        return Status.statusEnum.BACKINGUP;
    }

    @Override
    public Status.statusEnum finalStatus() {
        return Status.statusEnum.OFFLINE;
    }

    @Override
    public void actualAction() throws IOException, InterruptedException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        ScriptManager.backup(currentDateTime.toString());
    }

    @Override
    public boolean checkActionWorked() throws InterruptedException, IOException {
        //Check if backup file exists
        return false;
    }

    @Override
    public boolean run() throws IOException, InterruptedException {

        if (!new Stop(statusManager).run()) {
            LogManager.createLog("Could not stop Server", "", LogManager.LogType.BACKUP, false);
            return false;
        }

        if(!attemptRun()){
            //Attempt failed enter recovery
            recover();
            //Attempt to run backup again
            return attemptRun();
        }

        if (!new Start(statusManager).run()) {
            LogManager.createLog("Could not start Server", "", LogManager.LogType.BACKUP, false);
            return false;
        }

        ScriptManager.say("Backup Successful");
        return true;
    }


}
