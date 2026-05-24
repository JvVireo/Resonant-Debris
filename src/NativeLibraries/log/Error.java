package NativeLibraries.log;

import NativeLibraries.log.logMessage;

import java.awt.*;


public class Error extends logMessage {

    public Error(String message) {
        super(message);
        message = "[E] " + message;
        this.Message = message;

    }
}