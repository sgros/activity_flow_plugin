package androidx.work.impl.background.systemalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import androidx.work.Logger;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.Processor;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.utils.WakeLocks;
import java.util.ArrayList;
import java.util.List;

public class SystemAlarmDispatcher implements ExecutionListener {
    static final String TAG = Logger.tagWithPrefix("SystemAlarmDispatcher");
    final CommandHandler mCommandHandler;
    private CommandsCompletedListener mCompletedListener;
    final Context mContext;
    Intent mCurrentIntent;
    final List<Intent> mIntents;
    private final Handler mMainHandler;
    private final Processor mProcessor;
    private final WorkManagerImpl mWorkManager;
    private final WorkTimer mWorkTimer;

    /* renamed from: androidx.work.impl.background.systemalarm.SystemAlarmDispatcher$1 */
    class C02751 implements Runnable {
        C02751() {
        }

        public void run() {
            synchronized (SystemAlarmDispatcher.this.mIntents) {
                SystemAlarmDispatcher.this.mCurrentIntent = (Intent) SystemAlarmDispatcher.this.mIntents.get(0);
            }
            if (SystemAlarmDispatcher.this.mCurrentIntent != null) {
                SystemAlarmDispatcher systemAlarmDispatcher;
                Runnable dequeueAndCheckForCompletion;
                String action = SystemAlarmDispatcher.this.mCurrentIntent.getAction();
                int intExtra = SystemAlarmDispatcher.this.mCurrentIntent.getIntExtra("KEY_START_ID", 0);
                Logger.get().debug(SystemAlarmDispatcher.TAG, String.format("Processing command %s, %s", new Object[]{SystemAlarmDispatcher.this.mCurrentIntent, Integer.valueOf(intExtra)}), new Throwable[0]);
                WakeLock newWakeLock = WakeLocks.newWakeLock(SystemAlarmDispatcher.this.mContext, String.format("%s (%s)", new Object[]{action, Integer.valueOf(intExtra)}));
                try {
                    Logger.get().debug(SystemAlarmDispatcher.TAG, String.format("Acquiring operation wake lock (%s) %s", new Object[]{action, newWakeLock}), new Throwable[0]);
                    newWakeLock.acquire();
                    SystemAlarmDispatcher.this.mCommandHandler.onHandleIntent(SystemAlarmDispatcher.this.mCurrentIntent, intExtra, SystemAlarmDispatcher.this);
                    Logger.get().debug(SystemAlarmDispatcher.TAG, String.format("Releasing operation wake lock (%s) %s", new Object[]{action, newWakeLock}), new Throwable[0]);
                    newWakeLock.release();
                    systemAlarmDispatcher = SystemAlarmDispatcher.this;
                    dequeueAndCheckForCompletion = new DequeueAndCheckForCompletion(SystemAlarmDispatcher.this);
                } catch (Throwable th) {
                    Logger.get().debug(SystemAlarmDispatcher.TAG, String.format("Releasing operation wake lock (%s) %s", new Object[]{action, newWakeLock}), new Throwable[0]);
                    newWakeLock.release();
                    SystemAlarmDispatcher.this.postOnMainThread(new DequeueAndCheckForCompletion(SystemAlarmDispatcher.this));
                }
                systemAlarmDispatcher.postOnMainThread(dequeueAndCheckForCompletion);
            }
        }
    }

    static class AddRunnable implements Runnable {
        private final SystemAlarmDispatcher mDispatcher;
        private final Intent mIntent;
        private final int mStartId;

        AddRunnable(SystemAlarmDispatcher systemAlarmDispatcher, Intent intent, int i) {
            this.mDispatcher = systemAlarmDispatcher;
            this.mIntent = intent;
            this.mStartId = i;
        }

        public void run() {
            this.mDispatcher.add(this.mIntent, this.mStartId);
        }
    }

    interface CommandsCompletedListener {
        void onAllCommandsCompleted();
    }

    static class DequeueAndCheckForCompletion implements Runnable {
        private final SystemAlarmDispatcher mDispatcher;

        DequeueAndCheckForCompletion(SystemAlarmDispatcher systemAlarmDispatcher) {
            this.mDispatcher = systemAlarmDispatcher;
        }

        public void run() {
            this.mDispatcher.dequeueAndCheckForCompletion();
        }
    }

    SystemAlarmDispatcher(Context context) {
        this(context, null, null);
    }

    SystemAlarmDispatcher(Context context, Processor processor, WorkManagerImpl workManagerImpl) {
        this.mContext = context.getApplicationContext();
        this.mCommandHandler = new CommandHandler(this.mContext);
        this.mWorkTimer = new WorkTimer();
        if (workManagerImpl == null) {
            workManagerImpl = WorkManagerImpl.getInstance();
        }
        this.mWorkManager = workManagerImpl;
        if (processor == null) {
            processor = this.mWorkManager.getProcessor();
        }
        this.mProcessor = processor;
        this.mProcessor.addExecutionListener(this);
        this.mIntents = new ArrayList();
        this.mCurrentIntent = null;
        this.mMainHandler = new Handler(Looper.getMainLooper());
    }

    /* Access modifiers changed, original: 0000 */
    public void onDestroy() {
        this.mProcessor.removeExecutionListener(this);
        this.mCompletedListener = null;
    }

    public void onExecuted(String str, boolean z) {
        postOnMainThread(new AddRunnable(this, CommandHandler.createExecutionCompletedIntent(this.mContext, str, z), 0));
    }

    public boolean add(Intent intent, int i) {
        Logger.get().debug(TAG, String.format("Adding command %s (%s)", new Object[]{intent, Integer.valueOf(i)}), new Throwable[0]);
        assertMainThread();
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            Logger.get().warning(TAG, "Unknown command. Ignoring", new Throwable[0]);
            return false;
        } else if ("ACTION_CONSTRAINTS_CHANGED".equals(action) && hasIntentWithAction("ACTION_CONSTRAINTS_CHANGED")) {
            return false;
        } else {
            intent.putExtra("KEY_START_ID", i);
            synchronized (this.mIntents) {
                int isEmpty = this.mIntents.isEmpty() ^ 1;
                this.mIntents.add(intent);
                if (isEmpty == 0) {
                    processCommand();
                }
            }
            return true;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setCompletedListener(CommandsCompletedListener commandsCompletedListener) {
        if (this.mCompletedListener != null) {
            Logger.get().error(TAG, "A completion listener for SystemAlarmDispatcher already exists.", new Throwable[0]);
        } else {
            this.mCompletedListener = commandsCompletedListener;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Processor getProcessor() {
        return this.mProcessor;
    }

    /* Access modifiers changed, original: 0000 */
    public WorkTimer getWorkTimer() {
        return this.mWorkTimer;
    }

    /* Access modifiers changed, original: 0000 */
    public WorkManagerImpl getWorkManager() {
        return this.mWorkManager;
    }

    /* Access modifiers changed, original: 0000 */
    public void postOnMainThread(Runnable runnable) {
        this.mMainHandler.post(runnable);
    }

    /* Access modifiers changed, original: 0000 */
    public void dequeueAndCheckForCompletion() {
        Logger.get().debug(TAG, "Checking if commands are complete.", new Throwable[0]);
        assertMainThread();
        synchronized (this.mIntents) {
            if (this.mCurrentIntent != null) {
                Logger.get().debug(TAG, String.format("Removing command %s", new Object[]{this.mCurrentIntent}), new Throwable[0]);
                if (((Intent) this.mIntents.remove(0)).equals(this.mCurrentIntent)) {
                    this.mCurrentIntent = null;
                } else {
                    throw new IllegalStateException("Dequeue-d command is not the first.");
                }
            }
            if (!this.mCommandHandler.hasPendingCommands() && this.mIntents.isEmpty()) {
                Logger.get().debug(TAG, "No more commands & intents.", new Throwable[0]);
                if (this.mCompletedListener != null) {
                    this.mCompletedListener.onAllCommandsCompleted();
                }
            } else if (!this.mIntents.isEmpty()) {
                processCommand();
            }
        }
    }

    private void processCommand() {
        assertMainThread();
        WakeLock newWakeLock = WakeLocks.newWakeLock(this.mContext, "ProcessCommand");
        try {
            newWakeLock.acquire();
            this.mWorkManager.getWorkTaskExecutor().executeOnBackgroundThread(new C02751());
        } finally {
            newWakeLock.release();
        }
    }

    private boolean hasIntentWithAction(String str) {
        assertMainThread();
        synchronized (this.mIntents) {
            for (Intent action : this.mIntents) {
                if (str.equals(action.getAction())) {
                    return true;
                }
            }
            return false;
        }
    }

    private void assertMainThread() {
        if (this.mMainHandler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Needs to be invoked on the main thread.");
        }
    }
}
