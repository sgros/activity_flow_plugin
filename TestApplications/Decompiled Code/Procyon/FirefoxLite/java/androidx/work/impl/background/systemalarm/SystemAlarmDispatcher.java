// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemalarm;

import android.text.TextUtils;
import android.os.PowerManager$WakeLock;
import androidx.work.impl.utils.WakeLocks;
import java.util.Iterator;
import android.os.Looper;
import java.util.ArrayList;
import androidx.work.Logger;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.Processor;
import android.os.Handler;
import java.util.List;
import android.content.Intent;
import android.content.Context;
import androidx.work.impl.ExecutionListener;

public class SystemAlarmDispatcher implements ExecutionListener
{
    static final String TAG;
    final CommandHandler mCommandHandler;
    private CommandsCompletedListener mCompletedListener;
    final Context mContext;
    Intent mCurrentIntent;
    final List<Intent> mIntents;
    private final Handler mMainHandler;
    private final Processor mProcessor;
    private final WorkManagerImpl mWorkManager;
    private final WorkTimer mWorkTimer;
    
    static {
        TAG = Logger.tagWithPrefix("SystemAlarmDispatcher");
    }
    
    SystemAlarmDispatcher(final Context context) {
        this(context, null, null);
    }
    
    SystemAlarmDispatcher(final Context context, Processor processor, WorkManagerImpl instance) {
        this.mContext = context.getApplicationContext();
        this.mCommandHandler = new CommandHandler(this.mContext);
        this.mWorkTimer = new WorkTimer();
        if (instance == null) {
            instance = WorkManagerImpl.getInstance();
        }
        this.mWorkManager = instance;
        if (processor == null) {
            processor = this.mWorkManager.getProcessor();
        }
        (this.mProcessor = processor).addExecutionListener(this);
        this.mIntents = new ArrayList<Intent>();
        this.mCurrentIntent = null;
        this.mMainHandler = new Handler(Looper.getMainLooper());
    }
    
    private void assertMainThread() {
        if (this.mMainHandler.getLooper().getThread() == Thread.currentThread()) {
            return;
        }
        throw new IllegalStateException("Needs to be invoked on the main thread.");
    }
    
    private boolean hasIntentWithAction(final String s) {
        this.assertMainThread();
        synchronized (this.mIntents) {
            final Iterator<Intent> iterator = this.mIntents.iterator();
            while (iterator.hasNext()) {
                if (s.equals(iterator.next().getAction())) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private void processCommand() {
        this.assertMainThread();
        final PowerManager$WakeLock wakeLock = WakeLocks.newWakeLock(this.mContext, "ProcessCommand");
        try {
            wakeLock.acquire();
            this.mWorkManager.getWorkTaskExecutor().executeOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    Object o = SystemAlarmDispatcher.this.mIntents;
                    synchronized (o) {
                        SystemAlarmDispatcher.this.mCurrentIntent = SystemAlarmDispatcher.this.mIntents.get(0);
                        // monitorexit(o)
                        if (SystemAlarmDispatcher.this.mCurrentIntent != null) {
                            o = SystemAlarmDispatcher.this.mCurrentIntent.getAction();
                            final int intExtra = SystemAlarmDispatcher.this.mCurrentIntent.getIntExtra("KEY_START_ID", 0);
                            Logger.get().debug(SystemAlarmDispatcher.TAG, String.format("Processing command %s, %s", SystemAlarmDispatcher.this.mCurrentIntent, intExtra), new Throwable[0]);
                            Object wakeLock = WakeLocks.newWakeLock(SystemAlarmDispatcher.this.mContext, String.format("%s (%s)", o, intExtra));
                            try {
                                try {
                                    Logger.get().debug(SystemAlarmDispatcher.TAG, String.format("Acquiring operation wake lock (%s) %s", o, wakeLock), new Throwable[0]);
                                    ((PowerManager$WakeLock)wakeLock).acquire();
                                    SystemAlarmDispatcher.this.mCommandHandler.onHandleIntent(SystemAlarmDispatcher.this.mCurrentIntent, intExtra, SystemAlarmDispatcher.this);
                                    Logger.get().debug(SystemAlarmDispatcher.TAG, String.format("Releasing operation wake lock (%s) %s", o, wakeLock), new Throwable[0]);
                                    ((PowerManager$WakeLock)wakeLock).release();
                                    o = SystemAlarmDispatcher.this;
                                    wakeLock = new DequeueAndCheckForCompletion(SystemAlarmDispatcher.this);
                                }
                                finally {}
                            }
                            catch (Throwable t) {
                                Logger.get().error(SystemAlarmDispatcher.TAG, "Unexpected error in onHandleIntent", t);
                                Logger.get().debug(SystemAlarmDispatcher.TAG, String.format("Releasing operation wake lock (%s) %s", o, wakeLock), new Throwable[0]);
                                ((PowerManager$WakeLock)wakeLock).release();
                                o = SystemAlarmDispatcher.this;
                                wakeLock = new DequeueAndCheckForCompletion(SystemAlarmDispatcher.this);
                            }
                            ((SystemAlarmDispatcher)o).postOnMainThread((Runnable)wakeLock);
                            return;
                            Logger.get().debug(SystemAlarmDispatcher.TAG, String.format("Releasing operation wake lock (%s) %s", o, wakeLock), new Throwable[0]);
                            ((PowerManager$WakeLock)wakeLock).release();
                            SystemAlarmDispatcher.this.postOnMainThread(new DequeueAndCheckForCompletion(SystemAlarmDispatcher.this));
                        }
                    }
                }
            });
        }
        finally {
            wakeLock.release();
        }
    }
    
    public boolean add(final Intent intent, final int i) {
        Logger.get().debug(SystemAlarmDispatcher.TAG, String.format("Adding command %s (%s)", intent, i), new Throwable[0]);
        this.assertMainThread();
        final String action = intent.getAction();
        if (TextUtils.isEmpty((CharSequence)action)) {
            Logger.get().warning(SystemAlarmDispatcher.TAG, "Unknown command. Ignoring", new Throwable[0]);
            return false;
        }
        if ("ACTION_CONSTRAINTS_CHANGED".equals(action) && this.hasIntentWithAction("ACTION_CONSTRAINTS_CHANGED")) {
            return false;
        }
        intent.putExtra("KEY_START_ID", i);
        synchronized (this.mIntents) {
            final boolean empty = this.mIntents.isEmpty();
            this.mIntents.add(intent);
            if (!(empty ^ true)) {
                this.processCommand();
            }
            return true;
        }
    }
    
    void dequeueAndCheckForCompletion() {
        Logger.get().debug(SystemAlarmDispatcher.TAG, "Checking if commands are complete.", new Throwable[0]);
        this.assertMainThread();
        synchronized (this.mIntents) {
            if (this.mCurrentIntent != null) {
                Logger.get().debug(SystemAlarmDispatcher.TAG, String.format("Removing command %s", this.mCurrentIntent), new Throwable[0]);
                if (!this.mIntents.remove(0).equals(this.mCurrentIntent)) {
                    throw new IllegalStateException("Dequeue-d command is not the first.");
                }
                this.mCurrentIntent = null;
            }
            if (!this.mCommandHandler.hasPendingCommands() && this.mIntents.isEmpty()) {
                Logger.get().debug(SystemAlarmDispatcher.TAG, "No more commands & intents.", new Throwable[0]);
                if (this.mCompletedListener != null) {
                    this.mCompletedListener.onAllCommandsCompleted();
                }
            }
            else if (!this.mIntents.isEmpty()) {
                this.processCommand();
            }
        }
    }
    
    Processor getProcessor() {
        return this.mProcessor;
    }
    
    WorkManagerImpl getWorkManager() {
        return this.mWorkManager;
    }
    
    WorkTimer getWorkTimer() {
        return this.mWorkTimer;
    }
    
    void onDestroy() {
        this.mProcessor.removeExecutionListener(this);
        this.mCompletedListener = null;
    }
    
    @Override
    public void onExecuted(final String s, final boolean b) {
        this.postOnMainThread(new AddRunnable(this, CommandHandler.createExecutionCompletedIntent(this.mContext, s, b), 0));
    }
    
    void postOnMainThread(final Runnable runnable) {
        this.mMainHandler.post(runnable);
    }
    
    void setCompletedListener(final CommandsCompletedListener mCompletedListener) {
        if (this.mCompletedListener != null) {
            Logger.get().error(SystemAlarmDispatcher.TAG, "A completion listener for SystemAlarmDispatcher already exists.", new Throwable[0]);
            return;
        }
        this.mCompletedListener = mCompletedListener;
    }
    
    static class AddRunnable implements Runnable
    {
        private final SystemAlarmDispatcher mDispatcher;
        private final Intent mIntent;
        private final int mStartId;
        
        AddRunnable(final SystemAlarmDispatcher mDispatcher, final Intent mIntent, final int mStartId) {
            this.mDispatcher = mDispatcher;
            this.mIntent = mIntent;
            this.mStartId = mStartId;
        }
        
        @Override
        public void run() {
            this.mDispatcher.add(this.mIntent, this.mStartId);
        }
    }
    
    interface CommandsCompletedListener
    {
        void onAllCommandsCompleted();
    }
    
    static class DequeueAndCheckForCompletion implements Runnable
    {
        private final SystemAlarmDispatcher mDispatcher;
        
        DequeueAndCheckForCompletion(final SystemAlarmDispatcher mDispatcher) {
            this.mDispatcher = mDispatcher;
        }
        
        @Override
        public void run() {
            this.mDispatcher.dequeueAndCheckForCompletion();
        }
    }
}
