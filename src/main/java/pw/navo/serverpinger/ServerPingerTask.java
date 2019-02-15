package pw.navo.serverpinger;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Set;
import java.util.TimerTask;
import java.util.function.BiConsumer;

@AllArgsConstructor
public class ServerPingerTask extends TimerTask {

    private Set<String> servers;
    private int timeout;
    private BiConsumer<String, Long> callback;

    @Override
    public void run() {
        for (String server : servers) {
            try {
                long ms = ServerPingerTask.pingServer(server, timeout);

                callback.accept(server, ms);
            } catch (IOException exception) {
                ServerPingerLogger.info(server, "Ping failed");
            }
        }
    }

    /**
     * A method to determine the ping of a server in milliseconds
     *
     * @param ip      The servers address of the server you want to ping
     * @param timeout The time window in which the destination has time to respond
     * @return If the server is reachable within the timout it returns the ping time in ms, otherwise -1
     * @throws IOException
     */
    public static long pingServer(String ip, int timeout) throws IOException {
        InetAddress address = InetAddress.getByName(ip);
        long currentTime = System.currentTimeMillis();
        boolean reachable = address.isReachable(timeout);
        long endTime = System.currentTimeMillis() - currentTime;

        return (reachable ? (endTime < timeout ? endTime : -1) : -1);
    }

}
