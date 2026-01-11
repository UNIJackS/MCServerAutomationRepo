import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TreeSet;

public class Backup implements ServerAction {
    private static DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private LocalDateTime timeOfLastBackupCheck;
    private StatusManager statusManager;

    public Backup(StatusManager statusManager){
        this.statusManager = statusManager;
        timeOfLastBackupCheck = LocalDateTime.MIN;
    }

    @Override
    public StatusManager statusManager() {
        return statusManager;
    }

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

    //If this breaks look here first
    //Some AI bullshit in here
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
        LocalDateTime topLocalDateTime = backupLocalDateTime.pollLast();

        assert topLocalDateTime != null;
        Duration duration = Duration.between(topLocalDateTime, LocalDateTime.now());
        LogManager.createLog("Time since last backup in hours_" + duration.toHours(), "", LogManager.LogType.BACKUPTIME, true);
        return duration;
    }



    @Override
    public boolean checkActionWorked() throws InterruptedException, IOException {
        System.out.println("time since last backup in minutes :" + timeSinceLastBackup().toMinutes());
        return(timeSinceLastBackup().toMinutes() < 20);
    }


    //Checks if a backup of the server should be perfomred
    private boolean backupRequired() throws IOException, InterruptedException{
        long timeSinceLastBackup = timeSinceLastBackup().toHours();
        //If it is 4am and the last backup was more than 12 hours ago then we should backup the server.
        //The 12 hour check is to ensure that multiple backups are not made at 4am.
        //This also allows for a bit of lee way with the emergency backups
        if(LocalDateTime.now().getHour() == 4 && timeSinceLastBackup > 12){
            ScriptManager.say("Daily backup starting as it is 4am");
            ScriptManager.say("The server will automatically start up afer the backup");
            ScriptManager.say("Please allow around 15mins for the backup");
            LogManager.createLog("Daily Backup Started. Last backup " + timeSinceLastBackup+ " hours ago", "", LogManager.LogType.BACKUPTIME, true);
            return true;
        }
        //If backup has not been performed in 36 hours then perform emergeny backup
        if(timeSinceLastBackup > 36){
            ScriptManager.say("Time since last sucsessful backup was more than 36 hours ago.");
            ScriptManager.say("Please let the server opterator know something is wrong.");
            ScriptManager.say("Attempting to perfrom emergency backup now ...");
            LogManager.createLog("Emergency Backup Started. Last backup " + timeSinceLastBackup+ " hours ago", "", LogManager.LogType.BACKUPTIME, false);
            return true;
        }

        //sends a message to the server and logs when the last sucessful backup was every hour.
        Duration timeSinceLastBackupCheck = Duration.between(timeOfLastBackupCheck, LocalDateTime.now());
        if(timeSinceLastBackupCheck.toMinutes() > 60){
            timeOfLastBackupCheck = LocalDateTime.now();
            LogManager.createLog("Last backup " + timeSinceLastBackup+ " hours ago", "", LogManager.LogType.BACKUPTIME, false);
            ScriptManager.say("Last sucessful backup was " + timeSinceLastBackup +" hours ago.");
            ScriptManager.say("(Server backsup daily at 4am");
        }
        return false;
    }




    @Override
    public boolean run() throws IOException, InterruptedException {
        //dont remember why this check is here ? so commenting out
        //if(timeSinceLastBackup().toMinutes() < 3){return false;}

        if(!backupRequired()){return true;}

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
