package pw.navo.serverpinger;

import lombok.Data;

@Data
public class ServerStatus {

    private int streak;
    private long lastPing;
    private boolean lastNotify;

    /**
     * @return Returns true if last ping was successful
     */
    public boolean getLastPing() {
        return lastPing != -1;
    }

    public void incrementStreak() {
        this.streak++;
    }

    public void resetStreak() {
        this.streak = 0;
    }

}
