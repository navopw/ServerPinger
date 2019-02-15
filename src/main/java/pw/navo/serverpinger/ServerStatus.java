package pw.navo.serverpinger;

import lombok.Data;

@Data
public class ServerStatus {

    /**
     * The last tested ping of this server in ms
     */
    private long lastPing;

    /**
     * Last state change as currentTimeMillis (reset when server goes from offline->online or online->offline)
     */
    private long lastStateChangeTimestamp;

    /**
     * True if last push notification was a positive notification (e.g. 1.1.1.1 is online again)
     */
    private boolean lastNotify;

    /**
     * @return Returns true if last ping was successful (not -1)
     */
    public boolean lastPingSuccessful() {
        return lastPing != -1;
    }

}
