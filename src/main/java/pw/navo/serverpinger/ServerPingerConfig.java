package pw.navo.serverpinger;

import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private int timeout = 3000;
    private int period = 5000;

    public void save() {
        Properties properties = new Properties();

        properties.setProperty("servers", servers.stream().collect(Collectors.joining(",")));
        properties.setProperty("pushover_token", this.pushover_token);
        properties.setProperty("pushover_user", this.pushover_user);
        properties.setProperty("timeout", Integer.toString(this.timeout));
        properties.setProperty("period", Integer.toString(this.period));

        try {
            if (!this.configFile.exists()) {
                this.configFile.createNewFile();
            }

            properties.store(new FileWriter(this.configFile), null);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void load() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader((this.configFile)));

            this.servers = Arrays.asList(properties.getProperty("servers").split(","));
            this.pushover_token = properties.getProperty("pushover_token");
            this.pushover_user = properties.getProperty("pushover_user");
            this.timeout = Integer.parseInt(properties.getProperty("timeout"));
            this.period = Integer.parseInt(properties.getProperty("period"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
