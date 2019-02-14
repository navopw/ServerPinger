package pw.navo.serverpinger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ServerPingerLogger {

    public static void info(String message) {
         System.out.println("[" + ServerPingerLogger.getFormattedDateString() + "] " + message);
    }

    public static void info(String server, String message) {
        System.out.println("[" + ServerPingerLogger.getFormattedDateString() + "] [" + server + "] " + message);
    }

    public static String getFormattedDateString() {
        return new SimpleDateFormat().format(Calendar.getInstance().getTime());
    }

}
