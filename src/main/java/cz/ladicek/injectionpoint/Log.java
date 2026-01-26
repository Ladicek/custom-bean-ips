package cz.ladicek.injectionpoint;

import java.util.logging.Logger;

public class Log {
    private static final Logger LOG = Logger.getLogger(Log.class.getName());

    public static void info(String message) {
        LOG.info(message);
    }
}
