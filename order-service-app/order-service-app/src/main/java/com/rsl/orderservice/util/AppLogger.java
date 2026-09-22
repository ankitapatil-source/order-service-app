package com.rsl.orderservice.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Central logging setup for the app.
 *
 * <p>Log lines are written both to the console and to <code>logs/app.log</code>
 * so they can be retrieved and inspected later.</p>
 */
public final class AppLogger {

    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = LOG_DIR + "/app.log";
    private static boolean configured = false;

    private AppLogger() {
    }

    /** Returns a logger for the given class, configuring file output once. */
    public static synchronized Logger get(Class<?> owner) {
        if (!configured) {
            configureRootFileHandler();
            configured = true;
        }
        return Logger.getLogger(owner.getName());
    }

    private static void configureRootFileHandler() {
        try {
            Files.createDirectories(Path.of(LOG_DIR));
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SingleLineFormatter());
            fileHandler.setLevel(Level.ALL);

            Logger root = Logger.getLogger("");
            root.setLevel(Level.INFO);
            root.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Could not set up file logging: " + e.getMessage());
        }
    }

    /** Compact one-line-per-record format, with full stack traces on errors. */
    private static final class SingleLineFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%1$tF %1$tT", record.getMillis()))
              .append(" [").append(record.getLevel()).append("] ")
              .append(shortName(record.getLoggerName()))
              .append(" - ")
              .append(formatMessage(record))
              .append(System.lineSeparator());

            if (record.getThrown() != null) {
                Throwable t = record.getThrown();
                sb.append(t).append(System.lineSeparator());
                for (StackTraceElement el : t.getStackTrace()) {
                    sb.append("    at ").append(el).append(System.lineSeparator());
                }
            }
            return sb.toString();
        }

        private String shortName(String loggerName) {
            if (loggerName == null) {
                return "root";
            }
            int i = loggerName.lastIndexOf('.');
            return i >= 0 ? loggerName.substring(i + 1) : loggerName;
        }
    }
}
