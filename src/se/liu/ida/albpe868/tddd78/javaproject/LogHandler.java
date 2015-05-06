package se.liu.ida.albpe868.tddd78.javaproject;

import java.io.IOException;
import java.util.logging.*;

/**
 * This class uses java.util.logging to log errors and program events.
 */
public final class LogHandler {
    private static final Logger LOGGER = Logger.getLogger(LogHandler.class.getName());
    private static boolean logHandlerSet = false;

    private LogHandler() {
    }

    private static void logHandlerInit() {
        try {
            Handler fileHandler = new FileHandler("src/gamefiles/game.log", true);
            Formatter plainText = new SimpleFormatter();
            fileHandler.setFormatter(plainText);
            LOGGER.addHandler(fileHandler);
        } catch (IOException ex) {
            LOGGER.log(Level.INFO, "LogHandler logEvent error.", ex);
            System.out.println("Logging exception" + ex);
        }
    }

    public static void log(Level level, String msg, Exception ex) {
        if (!logHandlerSet) {
            logHandlerInit();
            logHandlerSet = true;
        }
        if (ex == null) {
            LOGGER.log(level, msg);
        } else {
            LOGGER.log(level, msg, ex);
        }
    }
}
