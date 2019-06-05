// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.utils;

public class ExceptionHandler implements UncaughtExceptionHandler
{
    private static final UncaughtExceptionHandler defaultHandler;
    
    static {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }
    
    @Override
    public void uncaughtException(final Thread thread, final Throwable t) {
        Logger.e("UncaughtExceptionHandler", "uncaughtException " + thread.getName(), t);
        ExceptionHandler.defaultHandler.uncaughtException(thread, t);
    }
}
