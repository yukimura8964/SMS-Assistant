package moe.gkd.smsassistant.event;

public class SmsBroadcastServiceStatusEvent {
    private boolean isRunning;

    public SmsBroadcastServiceStatusEvent(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
