package mozilla.components.support.base.log;

import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.support.base.log.sink.LogSink;

/* compiled from: Log.kt */
public final class Log {
    public static final Log INSTANCE = new Log();
    private static Priority logLevel = Priority.DEBUG;
    private static final List<LogSink> sinks = new ArrayList();

    /* compiled from: Log.kt */
    public enum Priority {
        DEBUG(3),
        INFO(4),
        WARN(5),
        ERROR(6);
        
        private final int value;

        protected Priority(int i) {
            this.value = i;
        }

        public final int getValue() {
            return this.value;
        }
    }

    private Log() {
    }

    public final void log(Priority priority, String str, Throwable th, String str2) {
        Intrinsics.checkParameterIsNotNull(priority, "priority");
        if (priority.getValue() >= logLevel.getValue()) {
            synchronized (sinks) {
                for (LogSink log : sinks) {
                    log.log(priority, str, th, str2);
                }
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
