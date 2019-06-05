// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemalarm;

import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.concurrent.Executors;
import androidx.work.Logger;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

class WorkTimer
{
    private static final String TAG;
    private final ThreadFactory mBackgroundThreadFactory;
    private final ScheduledExecutorService mExecutorService;
    final Map<String, TimeLimitExceededListener> mListeners;
    final Object mLock;
    final Map<String, WorkTimerRunnable> mTimerMap;
    
    static {
        TAG = Logger.tagWithPrefix("WorkTimer");
    }
    
    WorkTimer() {
        this.mBackgroundThreadFactory = new ThreadFactory() {
            private int mThreadsCreated = 0;
            
            @Override
            public Thread newThread(final Runnable runnable) {
                final Thread thread = Executors.defaultThreadFactory().newThread(runnable);
                final StringBuilder sb = new StringBuilder();
                sb.append("WorkManager-WorkTimer-thread-");
                sb.append(this.mThreadsCreated);
                thread.setName(sb.toString());
                ++this.mThreadsCreated;
                return thread;
            }
        };
        this.mTimerMap = new HashMap<String, WorkTimerRunnable>();
        this.mListeners = new HashMap<String, TimeLimitExceededListener>();
        this.mLock = new Object();
        this.mExecutorService = Executors.newSingleThreadScheduledExecutor(this.mBackgroundThreadFactory);
    }
    
    void startTimer(final String s, final long n, final TimeLimitExceededListener timeLimitExceededListener) {
        synchronized (this.mLock) {
            Logger.get().debug(WorkTimer.TAG, String.format("Starting timer for %s", s), new Throwable[0]);
            this.stopTimer(s);
            final WorkTimerRunnable workTimerRunnable = new WorkTimerRunnable(this, s);
            this.mTimerMap.put(s, workTimerRunnable);
            this.mListeners.put(s, timeLimitExceededListener);
            this.mExecutorService.schedule(workTimerRunnable, n, TimeUnit.MILLISECONDS);
        }
    }
    
    void stopTimer(final String s) {
        synchronized (this.mLock) {
            if (this.mTimerMap.remove(s) != null) {
                Logger.get().debug(WorkTimer.TAG, String.format("Stopping timer for %s", s), new Throwable[0]);
                this.mListeners.remove(s);
            }
        }
    }
    
    interface TimeLimitExceededListener
    {
        void onTimeLimitExceeded(final String p0);
    }
    
    static class WorkTimerRunnable implements Runnable
    {
        private final String mWorkSpecId;
        private final WorkTimer mWorkTimer;
        
        WorkTimerRunnable(final WorkTimer mWorkTimer, final String mWorkSpecId) {
            this.mWorkTimer = mWorkTimer;
            this.mWorkSpecId = mWorkSpecId;
        }
        
        @Override
        public void run() {
            synchronized (this.mWorkTimer.mLock) {
                if (this.mWorkTimer.mTimerMap.remove(this.mWorkSpecId) != null) {
                    final TimeLimitExceededListener timeLimitExceededListener = this.mWorkTimer.mListeners.remove(this.mWorkSpecId);
                    if (timeLimitExceededListener != null) {
                        timeLimitExceededListener.onTimeLimitExceeded(this.mWorkSpecId);
                    }
                }
                else {
                    Logger.get().debug("WrkTimerRunnable", String.format("Timer with %s is already marked as complete.", this.mWorkSpecId), new Throwable[0]);
                }
            }
        }
    }
}
