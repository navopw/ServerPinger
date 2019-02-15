package pw.navo.serverpinger;

import pw.navo.serverpinger.util.ValidationUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ServerPinger {

    private Pushover pushover;
    private Timer timer;

    private Map<String, ServerStatus> servers;

    public ServerPinger() {
        this.servers = new HashMap<>();
        this.timer = new Timer();
    }

    /**
     * Starts ServerPinger with specified parameters (the parameters are read from the config in the main Method an passed through
     * @param servers All ip addresses/domains the serverpinger should consider
     * @param user The user key from your pushover account
     * @param token The token from your application at the pushover website
     * @param initialNotification If true an overview of the Online/Offline servers will be shown on start/first check
     * @param timeout The time before the ServerPinger aborts the ping try and declares server as not reachable
     * @param period The period in which the ServerPinger should ping the ip addresses
     * @param notifytime The time a server has to be offline or online again to trigger a push notification
     */
    public void start(List<String> servers, String user, String token, boolean initialNotification, int timeout, int period, int notifytime) {
        //Pushover
        this.pushover = new Pushover(user, token);

        //First check
        for (String server : servers) {
            this.runFirstCheck(server, timeout);
        }

        //First notification
        if (initialNotification) {
            String onlineServers = this.getOnlineServers().keySet().stream().collect(Collectors.joining(", "));
            String offlineServers = this.getOfflineServers().keySet().stream().collect(Collectors.joining(", "));

            try {
                this.pushover.sendNotification("ServerPinger [" + ServerPingerLogger.getFormattedDateString() + "]",
                        (!onlineServers.isEmpty() ? "Online: " + onlineServers : "") +
                                (!offlineServers.isEmpty() ? (!onlineServers.isEmpty() ? "\n" : "") + "Offline: " + offlineServers : "")
                );
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        //Start Task
        this.startTask(this.servers.keySet(), timeout, period, notifytime);

        ServerPingerLogger.info("Started task, now checking every " + Math.round(period / 1000) + " seconds...");
    }

    /**
     * Runs first check on server to see whether he is online (sets last ping, lastnotify, lastStateChange, ...)
     */
    public void runFirstCheck(String server, int timeout) {
        this.servers.put(server, new ServerStatus());

        ServerStatus status = this.servers.get(server);

        try {
            long ms = ServerPingerTask.pingServer(server, timeout);
            status.setLastPing(ms);
            status.setLastNotify(status.lastPingSuccessful());
            status.setLastStateChangeTimestamp(System.currentTimeMillis());
            ServerPingerLogger.info(server, "First check: " + ms + "ms (" + (status.lastPingSuccessful() ? "online" : "offline") + ")");
        } catch (IOException exception) {
            status.setLastPing(-1);
            status.setLastNotify(false);
            ServerPingerLogger.info(server, "First check failed");
        }
    }

    /**
     * Start task that pings the servers in a specified period
     */
    public void startTask(Set<String> servers, int timeout, int period, int notifytime) {
        ServerPingerTask task = new ServerPingerTask(servers, timeout, (server, ping) -> {
            ServerStatus status = this.servers.get(server);

            boolean pingSuccessful = (ping != -1);

            //If the state has not changed
            if (pingSuccessful == status.lastPingSuccessful()) {
                long stateTime = System.currentTimeMillis() - status.getLastStateChangeTimestamp();

                ServerPingerLogger.info(server, ping + "ms, lastStateChange: " + stateTime + "ms ago");

                //If the state is the same for {notifytime} milliseconds
                if (stateTime > notifytime) {
                    //If the current state differs from the last notification
                    if (status.isLastNotify() != pingSuccessful) {
                        try {
                            this.pushover.sendNotification("ServerPinger [" + ServerPingerLogger.getFormattedDateString() + "]", server + " is " + (pingSuccessful ? "online again" : "offline"));
                            status.setLastNotify(pingSuccessful);
                        } catch (IOException exception) {
                            ServerPingerLogger.info(server, "Failed sending push notification");
                            exception.printStackTrace();
                        }
                    }
                }
            } else {
                status.setLastStateChangeTimestamp(System.currentTimeMillis());
            }

            status.setLastPing(ping);
        });

        this.timer.scheduleAtFixedRate(task, period, period); //delay = period is no mistake, the second check should start after {period} time
    }

    /**
     * Filters the servers hashmap for servers with last ping successful
     * @return
     */
    public Map<String, ServerStatus> getOnlineServers() {
        return this.servers.entrySet().stream()
                .filter(map -> map.getValue().lastPingSuccessful())
                .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
    }

    /**
     * Filters the servers hashmap for servers with last ping failed
     * @return
     */
    public Map<String, ServerStatus> getOfflineServers() {
        return this.servers.entrySet().stream()
                .filter(map -> !map.getValue().lastPingSuccessful())
                .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
    }

    public static void main(String[] args) {
        ServerPingerLogger.info("Starting ServerPinger...");

        //Config
        ServerPingerConfig config = new ServerPingerConfig();

        if (!config.getConfigFile().exists()) {
            try {
                config.save();
            } catch (IOException exception) {
                ServerPingerLogger.info("Failed saving config!");
                exception.printStackTrace();
            }
            ServerPingerLogger.info("Created config file, please configure before you start ServerPinger!");
            return;
        }

        try {
            config.load();
            ServerPingerLogger.info("Config loaded!");
        } catch (IOException exception) {
            ServerPingerLogger.info("Failed loading config!");
            exception.printStackTrace();
        }

        //Config checks
        if (config.getServers().isEmpty()) {
            ServerPingerLogger.info("Please enter ip addresses seperated by commas to the config!");
            return;
        }

        //Validate ip addresses
        for (String server : config.getServers()) {
            if (!ValidationUtil.isIpAddress(server) && !ValidationUtil.isDomain(server)) {
                ServerPingerLogger.info(server + " is no valid ip address or domain!");
                return;
            }
        }

        //Pushover user & token
        if (config.getPushover_user().isEmpty() || config.getPushover_token().isEmpty()) {
            ServerPingerLogger.info("You can't leave the Pushover user & token empty, you will not get any push notifications!");
            return;
        }

        //Start
        ServerPinger serverPinger = new ServerPinger();
        serverPinger.start(
                config.getServers(),
                config.getPushover_user(),
                config.getPushover_token(),
                config.isInitial_notification(),
                config.getTimeout(),
                config.getPeriod(),
                config.getNotifytime()
        );
    }

}
