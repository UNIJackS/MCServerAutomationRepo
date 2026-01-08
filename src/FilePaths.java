import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FilePaths {
    public static final String logsPath = "/media/bigDrive/MCAutomation/logs";
    public static final String runPath = "/media/bigDrive/MCAutomation/activeWorld/run.sh";
    public static final String activeWorldPath = "/media/bigDrive/MCAutomation/activeWorld/world";
    public static final String backupWorldPath = "/media/bigDrive/MCAutomation/backups/";

    public static boolean ensureDirectoryExists(String pathToCheck)throws IOException{
        //Check if the directory exists
        //If it does then dont worry and return
        if(checkDirectorieExists(pathToCheck)){return true;}
        //If it does not then create the directory
        return createDirectory(pathToCheck);
    }

    private static boolean checkDirectorieExists(String pathToCheck){
        Path dir = Path.of(pathToCheck);
        if (Files.exists(dir) && Files.isDirectory(dir)) {
            //If the directory exists then do nothing.
            System.out.println("Directory exists : " + pathToCheck);
            return true;
        } else {
            //If the directory does not exist then log it.
            LogManager.createLog(pathToCheck+" : Does not exist","From checkDirectorieExists function in FilePaths class."+pathToCheck+" does not exist",LogManager.LogType.SETUP,false);
            return false;
        }
    }

    private static boolean createDirectory(String pathToCheck) throws IOException{
        Path dir = Path.of(pathToCheck);
        try{
            Files.createDirectory(dir);
        }catch(FileAlreadyExistsException e){
            LogManager.createLog(pathToCheck+" : Already exists","From createDirectory function in FilePaths class."+pathToCheck+" attempted to create but already exists",LogManager.LogType.SETUP,false);
        }

        LogManager.createLog(pathToCheck+" Created","From createDirectory function in FilePaths class."+pathToCheck+" created",LogManager.LogType.SETUP,true);
        return true;
    }
}
