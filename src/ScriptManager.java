import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;

public class ScriptManager {
    public final static String screenName = "MCServer";

    public static void say(String whatToSay) throws IOException, InterruptedException{
        String[] arguments = {"bash","-c","screen -S "+screenName+" -X stuff say"+whatToSay+"\n"};
        scriptRunner(arguments);
    }

    public static void start() throws IOException, InterruptedException{
        String[] arguments = {"bash","-c","screen -dmS"+screenName+FilePaths.pathToMCServer};
        scriptRunner(arguments);
    }

    public static void stop() throws IOException, InterruptedException{
        String[] arguments = {"bash","-c","screen -S "+screenName+" -X stuff stop\n"};
        scriptRunner(arguments);
    }

    public static ServerStatusManager.serverStatusEnum status() throws IOException, InterruptedException{
        String[] arguments = {"bash","-c","screen -list"};
        ArrayList<String> output = scriptRunner(arguments);

        if(String.join(", ", output).contains(screenName)){
            return ServerStatusManager.serverStatusEnum.ONLINE;
        }else{
            return ServerStatusManager.serverStatusEnum.OFFLINE;
        }
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
