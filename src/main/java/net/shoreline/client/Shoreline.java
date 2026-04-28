package net.shoreline.client;

import net.shoreline.client.api.font.FontManager;
import net.shoreline.client.impl.Managers;
import net.shoreline.client.impl.file.ModConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Client main class. Handles main client mod initializing of static handler
 * instances and client managers.
 *
 * @author linus
 * @see ShorelineMod
 * @since 2.0
 */
public class Shoreline
{
    private static final Logger LOGGER = LogManager.getLogger("Shoreline");

    public static final long UPTIME = System.currentTimeMillis();

    public static ModConfiguration CONFIG;

    // Client shutdown hooks which will run once when the MinecraftClient
    // game instance is shutdown.
    public static ShutdownHook SHUTDOWN;

    /**
     * Called during {@link ShorelineMod#onInitializeClient()}
     */
    public static void init()
    {
        info("Starting Shoreline...");

        Managers.init();

        CONFIG = new ModConfiguration();
        CONFIG.loadModConfiguration();

        SHUTDOWN = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(SHUTDOWN);
    }

    public static void postInit()
    {
        FontManager.init();
    }

    public static native Object showErrorWindow(Object message);

    public static void info(String message)
    {
        LOGGER.info(String.format("[Shoreline] %s", message));
    }

    public static void info(String message,
                            Object... params)
    {
        LOGGER.info(String.format("[Shoreline] %s", message), params);
    }

    public static void error(String message)
    {
        LOGGER.error(String.format("[Shoreline] %s", message));
    }

    public static void error(String message,
                             Object... params)
    {
        LOGGER.error(String.format("[Shoreline] %s", message), params);
    }
}
