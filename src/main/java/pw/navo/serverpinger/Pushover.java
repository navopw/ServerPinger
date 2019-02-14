package pw.navo.serverpinger;

import lombok.AllArgsConstructor;
import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;

@AllArgsConstructor
public class Pushover {

    public static String URL = "https://api.pushover.net/1/messages.json";

    private String user;
    private String token;

    public void sendNotification(String title, String message) throws IOException {
        String urlParameters = "token=" + this.token + "&user=" + this.user + "&title=" + title + "&message=" + message;

        ServerPingerLogger.info("Sending notification...");

        URL url = new URL(URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.writeBytes(urlParameters);
        dataOutputStream.flush();
        dataOutputStream.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            ServerPingerLogger.info("Notification sent!");
        } else {
            ServerPingerLogger.info("Failed sending notification.");
        }
    }

}
