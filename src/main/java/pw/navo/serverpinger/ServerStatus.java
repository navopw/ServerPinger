package pw.navo.serverpinger;

import lombok.Data;

@Data
public class ServerStatus {

    //The last known ping in ms
    private long lastPing;
    //Last state change as timestamp (currentTimeMillis)
    private long lastStateChangeTimestamp;
    //Whether the last push notification was positive or negative
    private boolean lastNotify;

    /**
     * @return Returns true if last ping was successful (not -1)
     */
    public boolean lastPingSuccessful() {
        return lastPing != -1;
    }

}
