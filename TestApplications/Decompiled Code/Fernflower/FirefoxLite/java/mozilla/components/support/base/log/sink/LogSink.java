package mozilla.components.support.base.log.sink;

import mozilla.components.support.base.log.Log;

public interface LogSink {
   void log(Log.Priority var1, String var2, Throwable var3, String var4);
}
