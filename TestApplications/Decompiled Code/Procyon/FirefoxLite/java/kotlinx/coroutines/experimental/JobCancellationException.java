// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;
import java.util.concurrent.CancellationException;

public final class JobCancellationException extends CancellationException
{
    public final Job job;
    
    public JobCancellationException(final String message, final Throwable cause, final Job job) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Intrinsics.checkParameterIsNotNull(job, "job");
        super(message);
        this.job = job;
        if (cause != null) {
            this.initCause(cause);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (o instanceof JobCancellationException) {
                final JobCancellationException ex = (JobCancellationException)o;
                if (Intrinsics.areEqual(ex.getMessage(), this.getMessage()) && Intrinsics.areEqual(ex.job, this.job) && Intrinsics.areEqual(ex.getCause(), this.getCause())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    @Override
    public Throwable fillInStackTrace() {
        if (CoroutineContextKt.getDEBUG()) {
            final Throwable fillInStackTrace = super.fillInStackTrace();
            Intrinsics.checkExpressionValueIsNotNull(fillInStackTrace, "super.fillInStackTrace()");
            return fillInStackTrace;
        }
        return this;
    }
    
    @Override
    public int hashCode() {
        final String message = this.getMessage();
        if (message == null) {
            Intrinsics.throwNpe();
        }
        final int hashCode = message.hashCode();
        final int hashCode2 = this.job.hashCode();
        final Throwable cause = this.getCause();
        int hashCode3;
        if (cause != null) {
            hashCode3 = cause.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        return (hashCode * 31 + hashCode2) * 31 + hashCode3;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("; job=");
        sb.append(this.job);
        return sb.toString();
    }
}
