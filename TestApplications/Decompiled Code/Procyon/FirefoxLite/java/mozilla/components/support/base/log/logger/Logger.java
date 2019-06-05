// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.support.base.log.logger;

import mozilla.components.support.base.log.Log;

public final class Logger
{
    public static final Companion Companion;
    private static final Logger DEFAULT;
    private final String tag;
    
    static {
        Companion = new Companion(null);
        DEFAULT = new Logger(null, 1, null);
    }
    
    public Logger() {
        this(null, 1, null);
    }
    
    public Logger(final String tag) {
        this.tag = tag;
    }
    
    public final void debug(final String s, final Throwable t) {
        Log.INSTANCE.log(Log.Priority.DEBUG, this.tag, t, s);
    }
    
    public final void error(final String s, final Throwable t) {
        Log.INSTANCE.log(Log.Priority.ERROR, this.tag, t, s);
    }
    
    public final void info(final String s, final Throwable t) {
        Log.INSTANCE.log(Log.Priority.INFO, this.tag, t, s);
    }
    
    public final void warn(final String s, final Throwable t) {
        Log.INSTANCE.log(Log.Priority.WARN, this.tag, t, s);
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
}
