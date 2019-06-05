package menion.android.whereyougo.utils;

import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionHandler implements UncaughtExceptionHandler {
    private static final UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

    public void uncaughtException(Thread thread, Throwable ex) {
        Logger.m23e("UncaughtExceptionHandler", "uncaughtException " + thread.getName(), ex);
        defaultHandler.uncaughtException(thread, ex);
    }
}
