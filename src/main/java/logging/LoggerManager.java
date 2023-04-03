package logging;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerManager {

    private static final FileHandler fileHandler;
    private static final Path logFilePath = Paths.get("errors.log");

    static {
        try {
            fileHandler = new FileHandler(logFilePath.toString(), true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }

            getLogger(LoggerManager.class.getName()).addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Logger getLogger(String className) {
        Logger logger = Logger.getLogger(className);
        logger.addHandler(fileHandler);
        return logger;
    }
}