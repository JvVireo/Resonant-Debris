package NativeLibraries.log;

import NativeLibraries.log.logMessage;

import java.awt.*;


public class info extends logMessage {

    public info(String message) {
        super("[I] " + message); // Префикс добавляется перед вызовом родительского конструктора
    }
}