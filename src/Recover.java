import java.io.IOException;

record Recover(StatusManager statusManager) implements ServerAction {
    @Override
    public LogManager.LogType logType() {
        return null;
    }

    @Override
    public Status.statusEnum initialStatus() {
        return null;
    }

    @Override
    public Status.statusEnum transitionStatus() {
        return null;
    }

    @Override
    public Status.statusEnum finalStatus() {
        return null;
    }

    @Override
    public void actualAction() throws IOException, InterruptedException {

    }

    @Override
    public boolean checkActionWorked() throws InterruptedException, IOException {
        return false;
    }

    public boolean run() throws IOException, InterruptedException {
        recover();
        return true;
    }
}
