import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TreeSet;

public record Backup(StatusManager statusManager) implements ServerAction {
    private static DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

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
        String formatedCurrentDateTime = currentDateTime.format(customFormatter);
        ScriptManager.backup(formatedCurrentDateTime);
    }

    public static ArrayList<String> listDirectoryNames(Path directory) throws IOException {
        ArrayList<String> result = new ArrayList<>();

        try (var paths = Files.list(directory)) {
            paths
                    .filter(Files::isDirectory)          // only directories
                    .filter(path -> {
                        try {
                            return !Files.isHidden(path); // exclude hidden
                        } catch (IOException e) {
                            return false;                 // skip if can't check
                        }
                    })
                    .map(path -> path.getFileName().toString()) // name only
                    .forEach(result::add);
        }

        return result;
    }

    public static Duration timeSinceLastBackup() throws IOException {
        //Get all the backups
        ArrayList<String> backupNames = listDirectoryNames(Path.of(FilePaths.backupWorldPath));

        //Check the newest one 2026-01-08_10-30-24
        TreeSet<LocalDateTime> backupLocalDateTime = new TreeSet<>();
        for(String name : backupNames){
            backupLocalDateTime.add(LocalDateTime.parse(name, customFormatter));
        }
        LocalDateTime topLocalDateTime = backupLocalDateTime.pollFirst();

        assert topLocalDateTime != null;
        Duration duration = Duration.between(topLocalDateTime, LocalDateTime.now());
        return duration;
    }

    @Override
    public boolean checkActionWorked() throws InterruptedException, IOException {
        System.out.println("time since last backup in minutes :" + timeSinceLastBackup().toMinutes());
        return(timeSinceLastBackup().toMinutes() < 20);
    }

    @Override
    public boolean run() throws IOException, InterruptedException {
        if(timeSinceLastBackup().toMinutes() < 3){return false;}
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
