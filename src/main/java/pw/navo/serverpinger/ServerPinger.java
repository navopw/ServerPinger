package pw.navo.serverpinger;

import pw.navo.serverpinger.util.IpAddressUtil;

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

    public void start(List<String> servers, String user, String token, int timeout, int period) {
        //Pushover
        this.pushover = new Pushover(user, token);

        //First check
        for (String server : servers) {
            this.runFirstCheck(server, timeout);
        }

        //First notification
        String onlineServers = this.getOnlineServers().keySet().stream().collect(Collectors.joining(", "));
        String offlineServers = this.getOfflineServers().keySet().stream().collect(Collectors.joining(", "));
        try {
            this.pushover.sendNotification("ServerPinger [" + ServerPingerLogger.getFormattedDateString() + "]",
                    (!onlineServers.isEmpty() ? "Online: " + onlineServers : "") +
                            (!offlineServers.isEmpty() ? "\nOffline: " + offlineServers : "")
            );
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        //Start Task
        this.startTask(this.servers.keySet(), 3, timeout, period);

        ServerPingerLogger.info("Started task, now checking every " + Math.round(period / 1000) + " seconds...");
    }

    public void runFirstCheck(String server, int timeout) {
        this.servers.put(server, new ServerStatus());

        ServerStatus status = this.servers.get(server);

        try {
            long ms = ServerPingerTask.pingServer(server, timeout);
            status.setLastPing(ms);
            status.setLastNotify(status.getLastPing());
            ServerPingerLogger.info(server, "First check: " + ms + "ms (" + (status.getLastPing() ? "online" : "offline") + ")");
        } catch (IOException exception) {
            status.setLastPing(-1);
            status.setLastNotify(false);
            ServerPingerLogger.info(server, "First check failed");
        }
    }

    public void startTask(Set<String> servers, int streakLimit, int timeout, int period) {
        ServerPingerTask task = new ServerPingerTask(servers, timeout, (server, ping) -> {
            ServerStatus status = this.servers.get(server);

            boolean pingResultSuccessful = (ping != -1);

            if (pingResultSuccessful == status.getLastPing()) {
                status.incrementStreak();

                ServerPingerLogger.info(server, ping + "ms, Streak: " + status.getStreak());

                if (status.getStreak() >= streakLimit && status.isLastNotify() != pingResultSuccessful) {
                    try {
                        this.pushover.sendNotification("ServerPinger [" + ServerPingerLogger.getFormattedDateString() + "]", server + " ist " + (pingResultSuccessful ? "wieder online" : "offline"));
                        status.setLastNotify(pingResultSuccessful);
                    } catch (IOException exception) {
                        ServerPingerLogger.info(server, "Failed sending push notification");
                        exception.printStackTrace();
                    }
                }
            } else {
                status.resetStreak();
            }

            status.setLastPing(ping);
        });

        this.timer.scheduleAtFixedRate(task, 0, period);
    }

    public Map<String, ServerStatus> getOnlineServers() {
        return this.servers.entrySet().stream()
                .filter(map -> map.getValue().getLastPing())
                .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
    }

    public Map<String, ServerStatus> getOfflineServers() {
        return this.servers.entrySet().stream()
                .filter(map -> !map.getValue().getLastPing())
                .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
    }

    public static void main(String[] args) {
        ServerPingerLogger.info("Starting ServerPinger...");

        //Config
        ServerPingerConfig config = new ServerPingerConfig();

        if (!config.getConfigFile().exists()) {
            config.save();
            ServerPingerLogger.info("Created config file, please configure before you start ServerPinger!");
            return;
        }

        config.load();
        ServerPingerLogger.info("Config loaded!");

        //Config checks
        if (config.getServers().isEmpty()) {
            ServerPingerLogger.info("Please enter ip addresses seperated by commas to the config!");
            return;
        }

        //Validate ip addresses
        for (String server : config.getServers()) {
            if (!IpAddressUtil.isIpAddress(server)) {
                ServerPingerLogger.info(server + " is no valid ip address!");
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
        serverPinger.start(config.getServers(), config.getPushover_user(), config.getPushover_token(), config.getTimeout(), config.getPeriod());
    }

}
