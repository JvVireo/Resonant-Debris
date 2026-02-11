package NativeLibraries.log;

import NativeLibraries.log.logMessage;

import java.awt.*;


public class Warn extends logMessage {

    public Warn(String message) {
        super("[W] " + message);

    }
}