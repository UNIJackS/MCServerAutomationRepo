import java.io.IOException;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class LogManager {
    public enum LogType{
        SERVERSTARTED,
        SERVERSTOPPED,
        BACKUP,
        SETUP,
        LOGGING;

        public Path getPath(){return Path.of(FilePaths.logsPath+"/"+this.name());}
        public String getPathString(){return FilePaths.logsPath + "/" + this.name();}
    }

    
    /**
     * Ensures all the logging directorys exist
     * 
     * @return a boolean of if the log was created sucessfully.
     */
    public static boolean ensureLoggingDirectorysExist() throws IOException{
        for(LogType logtype : LogType.values()){
            FilePaths.ensureDirectoryExists(logtype.getPathString());
        }
        return true;
    }



    /**
     * Creates a log file
     *
     * @param logContent is the title for the log.
     * @param logContent is the text for the log.
     * @param logType is the type of log to be created.
     * @param success is if the action was a sucess or failure.
     *
     * @return a boolean of if the log was created sucessfully.
     */
    public static boolean createLog(String logTitle,String logContent, LogType  logtype, Boolean success){
        String fileName = createFileName(logTitle,logtype,success);
        try {
            Path path = Path.of(logtype.getPathString() + "/" + fileName);
            Files.writeString(path,logContent,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Sucessfully Created log file : " + fileName);
        } catch (IOException e) {
            System.out.println("Failed creating log file : " + fileName);
            e.printStackTrace();
        }
        //Created log with no isses
        return true;
    }


    private static String sucessOrFailure(Boolean success){
        if(success){return "Sucess";}
        return "Failure";
    }

    private static String createFileName(String logTitle,LogType  logtype, Boolean success){
        LocalDate currentDate = LocalDate.now();
        // Format DATE__logtype__success/failure__Title:(logTitle)
        return currentDate.toString() +"__"+ logtype.toString() +"__"+ sucessOrFailure(success) +"__Title-"+ logTitle +".txt";
    }

    
}
