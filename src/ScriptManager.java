import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;

public class ScriptManager {
    public final static String screenName = "MCServer";

    public void execute(){
        
    }

    public void start(String pathToMCServer){
        
    }

    public static ServerManager.serverStatus status() throws IOException, InterruptedException{
        String[] arguments = {"bash", "-c", "." + FilePaths.scriptsPath + "/STATUS.sh"};
        ArrayList<String> output = scriptRunner(arguments);
        String concatinatedOutput = "";
        for(String line : output){
            concatinatedOutput += line;
        }
        if(concatinatedOutput.contains(screenName)){
            return ServerManager.serverStatus.ONLINE;
        }else{
            return ServerManager.serverStatus.OFFLINE;
        }
    }

    public void stop(){
        
    }



    private static ArrayList<String> scriptRunner(String[] arguments)throws IOException, InterruptedException{
        // Use ProcessBuilder to execute the script
        ProcessBuilder processBuilder = new ProcessBuilder();
        
        // Command to run the script using bash
        processBuilder.command(arguments);
        
        // Optionally, redirect error stream to standard output
        processBuilder.redirectErrorStream(true); 

        Process process = processBuilder.start();

        // Read the output from the script
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        ArrayList<String> output = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            output.add(line);
        }

        // Wait for the script to finish
        int exitCode = process.waitFor();
        System.out.println("\nScript exited with code: " + exitCode);
        return output;
    }



    
}
