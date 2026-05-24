package NativeLibraries.log;

import NativeLibraries.log.logMessage;

import java.awt.*;


public class debug extends logMessage {

    public debug(String message) {
        super("@debug " + message);

    }
}