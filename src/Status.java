public class Status {
    public enum statusEnum {
        ONLINE,
        STARTING,
        SHUTTINGDOWN,
        OFFLINE,
        UNKNOWN,
        BACKINGUP
    }

    private statusEnum currentStatus;
    //Constructor
    public Status(){currentStatus = statusEnum.UNKNOWN;}

    public statusEnum get(){return currentStatus;}

    public String getString(){return currentStatus.name();}

    public void set(statusEnum newStatus){
        currentStatus = newStatus;
        LogManager.createLog("Status Change to "+currentStatus," Status change to " + currentStatus+" from checkServerStatus in ServerStatusManager",LogManager.LogType.STATUS,true);
    }

    public boolean actionOccuring(){
        //If that status is not online or offline then we are transitioning and should do nothing
        return !(currentStatus == statusEnum.ONLINE || currentStatus == statusEnum.OFFLINE);
    }

    public boolean online(){return currentStatus == statusEnum.ONLINE;}
    public boolean starting(){return currentStatus == statusEnum.STARTING;}
    public boolean shuttingdown(){return currentStatus == statusEnum.SHUTTINGDOWN;}
    public boolean offline(){return currentStatus == statusEnum.OFFLINE;}
    public boolean unknown(){return currentStatus == statusEnum.UNKNOWN;}
    public boolean backingup(){return currentStatus == statusEnum.BACKINGUP;}

}
