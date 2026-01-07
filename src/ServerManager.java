public class ServerManager {
    public enum serverStatus{
        ONLINE,
        BACKINGUP,
        OFFLINE
    }
    

    public boolean startServer(){
        return true;
    }


    public boolean stopServer(){
        return true;
    }


    public boolean backupServer(){
        return true;
    }



    private serverStatus checkServerStatus(){
        return serverStatus.OFFLINE;
    }
}
