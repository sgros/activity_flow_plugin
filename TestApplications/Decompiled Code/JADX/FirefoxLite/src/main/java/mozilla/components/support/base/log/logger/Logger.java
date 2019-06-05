package mozilla.components.support.base.log.logger;

import mozilla.components.support.base.log.Log;
import mozilla.components.support.base.log.Log.Priority;

/* compiled from: Logger.kt */
public final class Logger {
    public static final Companion Companion = new Companion();
    private static final Logger DEFAULT = new Logger(null, 1, null);
    private final String tag;

    /* compiled from: Logger.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public Logger() {
        this(null, 1, null);
    }

    public Logger(String str) {
        this.tag = str;
    }

    public /* synthetic */ Logger(String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = (String) null;
        }
        this(str);
    }

    public final void debug(String str, Throwable th) {
        Log.INSTANCE.log(Priority.DEBUG, this.tag, th, str);
    }

    public final void info(String str, Throwable th) {
        Log.INSTANCE.log(Priority.INFO, this.tag, th, str);
    }

    public final void warn(String str, Throwable th) {
        Log.INSTANCE.log(Priority.WARN, this.tag, th, str);
    }

    public final void error(String str, Throwable th) {
        Log.INSTANCE.log(Priority.ERROR, this.tag, th, str);
    }
}
