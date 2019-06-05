// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.support.base.log;

import java.util.Iterator;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import java.util.ArrayList;
import mozilla.components.support.base.log.sink.LogSink;
import java.util.List;

public final class Log
{
    public static final Log INSTANCE;
    private static Priority logLevel;
    private static final List<LogSink> sinks;
    
    static {
        INSTANCE = new Log();
        Log.logLevel = Priority.DEBUG;
        sinks = new ArrayList<LogSink>();
    }
    
    private Log() {
    }
    
    public final void log(final Priority priority, final String s, final Throwable t, final String s2) {
        Intrinsics.checkParameterIsNotNull(priority, "priority");
        if (priority.getValue() >= Log.logLevel.getValue()) {
            synchronized (Log.sinks) {
                final Iterator<LogSink> iterator = Log.sinks.iterator();
                while (iterator.hasNext()) {
                    iterator.next().log(priority, s, t, s2);
                }
                final Unit instance = Unit.INSTANCE;
            }
        }
    }
    
    public enum Priority
    {
        DEBUG(3), 
        ERROR(6), 
        INFO(4), 
        WARN(5);
        
        private final int value;
        
        protected Priority(final int value) {
            this.value = value;
        }
        
        public final int getValue() {
            return this.value;
        }
    }
}
