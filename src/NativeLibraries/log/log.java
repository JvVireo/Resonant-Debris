package NativeLibraries.log;

import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayList;
import NativeLibraries.log.*;

public class log {
    public log(){
        super();
    }
    static ArrayList<logMessage> log = new ArrayList<logMessage>();

    public static void Debug(String message) {
        log.add(new debug(message));
    }

    public static void Info(String message) {
        log.add(new info(message));
    }

    public static void Warn(String message) {
        log.add(new Warn(message));
    }

    public static void Error(String message) {
        log.add(new Error(message));
    }

    public static void Fatal(String message) {
        log.add(new Fatal(message));
    }

    public static String ToString() {
        StringBuilder sb = new StringBuilder();

        for (logMessage logo : log) {
            sb.append(logo.Message).append("\n");
        }
        return sb.length() > 0 ? sb.toString() : "";
    }
}