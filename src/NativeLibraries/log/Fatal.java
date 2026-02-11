package NativeLibraries.log;

import NativeLibraries.log.logMessage;

import java.awt.*;


public class Fatal extends logMessage {

    public Fatal(String message) {
        super("[!!FATAL!!] " + message); // Префикс добавляется перед вызовом родительского конструктора
    }
}