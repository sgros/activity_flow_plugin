package org.mozilla.telemetry.util;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}
