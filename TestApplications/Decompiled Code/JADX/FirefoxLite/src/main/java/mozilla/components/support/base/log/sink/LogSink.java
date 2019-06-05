package mozilla.components.support.base.log.sink;

import mozilla.components.support.base.log.Log.Priority;

/* compiled from: LogSink.kt */
public interface LogSink {
    void log(Priority priority, String str, Throwable th, String str2);
}
