// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.support.base.log.sink;

import mozilla.components.support.base.log.Log;

public interface LogSink
{
    void log(final Log.Priority p0, final String p1, final Throwable p2, final String p3);
}
