package org.mapsforge.core.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class IOUtils {
    private static final Logger LOGGER = Logger.getLogger(IOUtils.class.getName());

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.log(Level.FINE, e.getMessage(), e);
            }
        }
    }

    private IOUtils() {
        throw new IllegalStateException();
    }
}
