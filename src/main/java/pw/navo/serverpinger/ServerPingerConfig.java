package pw.navo.serverpinger;

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Getter
public class ServerPingerConfig {

    private File configFile = new File("serverpinger.properties");

    private List<String> servers = new ArrayList<String>() {{
        this.add("1.1.1.1");
    }};
    private String pushover_token = "";
    private String pushover_user = "";
    private boolean initial_notification = true;
    private int timeout = 3000;
    private int period = 5000;
    private int notifytime = 20000;

    public void save() throws IOException {
        Properties properties = new Properties();

        properties.setProperty("servers", servers.stream().collect(Collectors.joining(",")));
        properties.setProperty("pushover_token", this.pushover_token);
        properties.setProperty("pushover_user", this.pushover_user);
        properties.setProperty("initial_notification", Boolean.toString(this.initial_notification));
        properties.setProperty("timeout", Integer.toString(this.timeout));
        properties.setProperty("period", Integer.toString(this.period));
        properties.setProperty("notifytime", Integer.toString(this.notifytime));

        if (!this.configFile.exists()) {
            this.configFile.createNewFile();
        }

        properties.store(new FileWriter(this.configFile), null);
    }

    public void load() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader((this.configFile)));

        this.servers = Arrays.asList(properties.getProperty("servers").split(","));
        this.pushover_token = properties.getProperty("pushover_token");
        this.pushover_user = properties.getProperty("pushover_user");
        this.initial_notification = Boolean.parseBoolean(properties.getProperty("initial_notification"));
        this.timeout = Integer.parseInt(properties.getProperty("timeout"));
        this.period = Integer.parseInt(properties.getProperty("period"));
        this.notifytime = Integer.parseInt(properties.getProperty("notifytime"));
    }

}
