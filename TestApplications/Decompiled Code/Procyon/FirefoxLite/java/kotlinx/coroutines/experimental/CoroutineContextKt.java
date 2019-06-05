// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.CoroutineContext;
import java.util.concurrent.atomic.AtomicLong;

public final class CoroutineContextKt
{
    private static final AtomicLong COROUTINE_ID;
    private static final boolean DEBUG;
    private static final CoroutineDispatcher DefaultDispatcher;
    
    static {
        String property;
        try {
            property = System.getProperty("kotlinx.coroutines.debug");
        }
        catch (SecurityException ex) {
            property = null;
        }
        boolean desiredAssertionStatus = false;
        Label_0102: {
            Label_0056: {
                if (property != null) {
                    final int hashCode = property.hashCode();
                    Label_0126: {
                        if (hashCode != 0) {
                            if (hashCode != 3551) {
                                if (hashCode != 109935) {
                                    if (hashCode == 3005871 && property.equals("auto")) {
                                        break Label_0056;
                                    }
                                    break Label_0126;
                                }
                                else {
                                    if (property.equals("off")) {
                                        desiredAssertionStatus = false;
                                        break Label_0102;
                                    }
                                    break Label_0126;
                                }
                            }
                            else if (!property.equals("on")) {
                                break Label_0126;
                            }
                        }
                        else if (!property.equals("")) {
                            break Label_0126;
                        }
                        desiredAssertionStatus = true;
                        break Label_0102;
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("System property 'kotlinx.coroutines.debug' has unrecognized value '");
                    sb.append(property);
                    sb.append('\'');
                    throw new IllegalStateException(sb.toString().toString());
                }
            }
            desiredAssertionStatus = CoroutineId.class.desiredAssertionStatus();
        }
        DEBUG = desiredAssertionStatus;
        COROUTINE_ID = new AtomicLong();
        DefaultDispatcher = CommonPool.INSTANCE;
    }
    
    public static final String getCoroutineName(final CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "$receiver");
        if (!CoroutineContextKt.DEBUG) {
            return null;
        }
        final CoroutineId coroutineId = coroutineContext.get((CoroutineContext.Key<CoroutineId>)CoroutineId.Key);
        if (coroutineId != null) {
            final CoroutineName coroutineName = coroutineContext.get((CoroutineContext.Key<CoroutineName>)CoroutineName.Key);
            String name = null;
            Label_0069: {
                if (coroutineName != null) {
                    name = coroutineName.getName();
                    if (name != null) {
                        break Label_0069;
                    }
                }
                name = "coroutine";
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append('#');
            sb.append(coroutineId.getId());
            return sb.toString();
        }
        return null;
    }
    
    public static final boolean getDEBUG() {
        return CoroutineContextKt.DEBUG;
    }
    
    public static final CoroutineDispatcher getDefaultDispatcher() {
        return CoroutineContextKt.DefaultDispatcher;
    }
    
    public static final CoroutineContext newCoroutineContext(final CoroutineContext coroutineContext, final Job job) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        CoroutineContext coroutineContext2;
        if (CoroutineContextKt.DEBUG) {
            coroutineContext2 = coroutineContext.plus(new CoroutineId(CoroutineContextKt.COROUTINE_ID.incrementAndGet()));
        }
        else {
            coroutineContext2 = coroutineContext;
        }
        if (job != null) {
            coroutineContext2 = coroutineContext2.plus(job);
        }
        CoroutineContext plus = coroutineContext2;
        if (coroutineContext != CoroutineContextKt.DefaultDispatcher) {
            plus = coroutineContext2;
            if (coroutineContext.get((CoroutineContext.Key<CoroutineContext.Element>)ContinuationInterceptor.Key) == null) {
                plus = coroutineContext2.plus(CoroutineContextKt.DefaultDispatcher);
            }
        }
        return plus;
    }
    
    public static final void restoreThreadContext(final String name) {
        if (name != null) {
            final Thread currentThread = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
            currentThread.setName(name);
        }
    }
    
    public static final String updateThreadContext(final CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "$receiver");
        if (!CoroutineContextKt.DEBUG) {
            return null;
        }
        final CoroutineId coroutineId = coroutineContext.get((CoroutineContext.Key<CoroutineId>)CoroutineId.Key);
        if (coroutineId != null) {
            final CoroutineName coroutineName = coroutineContext.get((CoroutineContext.Key<CoroutineName>)CoroutineName.Key);
            String name = null;
            Label_0069: {
                if (coroutineName != null) {
                    name = coroutineName.getName();
                    if (name != null) {
                        break Label_0069;
                    }
                }
                name = "coroutine";
            }
            final Thread currentThread = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
            final String name2 = currentThread.getName();
            final StringBuilder sb = new StringBuilder(name2.length() + name.length() + 10);
            sb.append(name2);
            sb.append(" @");
            sb.append(name);
            sb.append('#');
            sb.append(coroutineId.getId());
            final String string = sb.toString();
            Intrinsics.checkExpressionValueIsNotNull(string, "StringBuilder(capacity).\u2026builderAction).toString()");
            currentThread.setName(string);
            return name2;
        }
        return null;
    }
}
