package org.apolunin.learning;

import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * ############################################################################################
 * Task description
 * ############################################################################################
 *
 * Enhance the lazy logging technique by providing conditional logging. A typical call would be
 * logIf(Level.FINEST, () -> i == 10, () -> "a[10] = " + a[10]).
 * Don’t evaluate the condition if the logger won’t log the message.
 *
 * ############################################################################################
 */
public class Exercise1 {
    public static void main(final String[] args) {
        final Handler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);

        final Logger logger = Logger.getLogger("org.apolunin.learning");
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);

        // ##################################################################################
        // Log level passes, condition is true
        // ##################################################################################
        logIf(logger, Level.INFO,
                () -> {
                    logger.info("evaluating true condition for Level.INFO");
                    return true;
                },
                () -> {
                    logger.info("building log message for true condition and Level.INFO");
                    return "log Level.INFO message with true condition";
                });

        // ##################################################################################
        // Log level doesn't pass, condition is true
        // ##################################################################################
        logIf(logger, Level.CONFIG,
                () -> {
                    logger.info("evaluating true condition for Level.CONFIG");
                    return true;
                },
                () -> {
                    logger.info("building log message for true condition and Level.CONFIG");
                    return "log Level.CONFIG message with true condition";
                });

        // ##################################################################################
        // Log level passes, condition is false
        // ##################################################################################
        logIf(logger, Level.INFO,
                () -> {
                    logger.info("evaluating false condition for Level.INFO");
                    return false;
                },
                () -> {
                    logger.info("building log message for false condition and Level.INFO");
                    return "log Level.INFO message with false condition";
                });

        // ##################################################################################
        // Log level doesn't pass, condition is false
        // ##################################################################################
        logIf(logger, Level.CONFIG,
                () -> {
                    logger.info("evaluating false condition for Level.CONFIG");
                    return false;
                },
                () -> {
                    logger.info("building log message for false condition and Level.CONFIG");
                    return "log Level.CONFIG message with false condition";
                });
    }

    public static void logIf(final Logger logger, final Level level, final Supplier<Boolean> condition,
            final Supplier<String> message) {
        if (logger.isLoggable(level) && Boolean.TRUE.equals(condition.get())) {
            logger.log(level, message.get());
        }
    }
}
