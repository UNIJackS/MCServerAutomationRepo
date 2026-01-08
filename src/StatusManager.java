import java.io.IOException;

public class StatusManager {
    private Status status;

    public Status getStatus(){return status;}
    public String getStatusString(){return status.getString();}



    //Constructor
    public StatusManager() throws IOException, InterruptedException{
        status = new Status();
        checkServerStatus();
    }

    public boolean changeStatus(Status.statusEnum from, Status.statusEnum to) throws IOException, InterruptedException {
        checkServerStatus();
        if(status.get() == from){
            status.set(to);
            return true;
        }
        return false;
    }

    public void enterRecovery(){status.set(Status.statusEnum.UNKNOWN);}
    public void exitRecovery() throws IOException, InterruptedException {status.set(ScriptManager.status());}

    public void checkServerStatus() throws IOException, InterruptedException{
        //If an action is happening then we don't want to change the status
        if(status.actionOccuring()){return;}
        status.set(ScriptManager.status());
    }
    
}
