// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import android.content.Context;

public abstract class WorkerFactory
{
    private static final String TAG;
    
    static {
        TAG = Logger.tagWithPrefix("WorkerFactory");
    }
    
    public static WorkerFactory getDefaultWorkerFactory() {
        return new WorkerFactory() {
            @Override
            public ListenableWorker createWorker(final Context context, final String s, final WorkerParameters workerParameters) {
                return null;
            }
        };
    }
    
    public abstract ListenableWorker createWorker(final Context p0, final String p1, final WorkerParameters p2);
    
    public final ListenableWorker createWorkerWithDefaultFallback(final Context context, final String str, final WorkerParameters workerParameters) {
        final ListenableWorker worker = this.createWorker(context, str, workerParameters);
        if (worker != null) {
            return worker;
        }
        try {
            final Class<? extends ListenableWorker> subclass = Class.forName(str).asSubclass(ListenableWorker.class);
            try {
                return (ListenableWorker)subclass.getDeclaredConstructor(Context.class, WorkerParameters.class).newInstance(context, workerParameters);
            }
            catch (Exception ex) {
                final Logger value = Logger.get();
                final String tag = WorkerFactory.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("Could not instantiate ");
                sb.append(str);
                value.error(tag, sb.toString(), ex);
                return null;
            }
        }
        catch (ClassNotFoundException ex2) {
            final Logger value2 = Logger.get();
            final String tag2 = WorkerFactory.TAG;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Class not found: ");
            sb2.append(str);
            value2.error(tag2, sb2.toString(), new Throwable[0]);
            return null;
        }
    }
}
