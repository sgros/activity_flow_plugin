package androidx.work;

import android.content.Context;

public abstract class WorkerFactory {
    private static final String TAG = Logger.tagWithPrefix("WorkerFactory");

    /* renamed from: androidx.work.WorkerFactory$1 */
    static class C02691 extends WorkerFactory {
        public ListenableWorker createWorker(Context context, String str, WorkerParameters workerParameters) {
            return null;
        }

        C02691() {
        }
    }

    public abstract ListenableWorker createWorker(Context context, String str, WorkerParameters workerParameters);

    public final ListenableWorker createWorkerWithDefaultFallback(Context context, String str, WorkerParameters workerParameters) {
        ListenableWorker createWorker = createWorker(context, str, workerParameters);
        if (createWorker != null) {
            return createWorker;
        }
        try {
            try {
                return (ListenableWorker) Class.forName(str).asSubclass(ListenableWorker.class).getDeclaredConstructor(new Class[]{Context.class, WorkerParameters.class}).newInstance(new Object[]{context, workerParameters});
            } catch (Exception e) {
                Logger logger = Logger.get();
                String str2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not instantiate ");
                stringBuilder.append(str);
                logger.error(str2, stringBuilder.toString(), e);
                return null;
            }
        } catch (ClassNotFoundException unused) {
            Logger logger2 = Logger.get();
            String str3 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Class not found: ");
            stringBuilder2.append(str);
            logger2.error(str3, stringBuilder2.toString(), new Throwable[0]);
            return null;
        }
    }

    public static WorkerFactory getDefaultWorkerFactory() {
        return new C02691();
    }
}
