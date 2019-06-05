// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

import android.app.Application$ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class ProcessLifecycleOwner implements LifecycleOwner
{
    private static final ProcessLifecycleOwner sInstance;
    private Runnable mDelayedPauseRunnable;
    private Handler mHandler;
    private ReportFragment.ActivityInitializationListener mInitializationListener;
    private boolean mPauseSent;
    private final LifecycleRegistry mRegistry;
    private int mResumedCounter;
    private int mStartedCounter;
    private boolean mStopSent;
    
    static {
        sInstance = new ProcessLifecycleOwner();
    }
    
    private ProcessLifecycleOwner() {
        this.mStartedCounter = 0;
        this.mResumedCounter = 0;
        this.mPauseSent = true;
        this.mStopSent = true;
        this.mRegistry = new LifecycleRegistry(this);
        this.mDelayedPauseRunnable = new Runnable() {
            @Override
            public void run() {
                ProcessLifecycleOwner.this.dispatchPauseIfNeeded();
                ProcessLifecycleOwner.this.dispatchStopIfNeeded();
            }
        };
        this.mInitializationListener = new ReportFragment.ActivityInitializationListener() {
            @Override
            public void onCreate() {
            }
            
            @Override
            public void onResume() {
                ProcessLifecycleOwner.this.activityResumed();
            }
            
            @Override
            public void onStart() {
                ProcessLifecycleOwner.this.activityStarted();
            }
        };
    }
    
    private void dispatchPauseIfNeeded() {
        if (this.mResumedCounter == 0) {
            this.mPauseSent = true;
            this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        }
    }
    
    private void dispatchStopIfNeeded() {
        if (this.mStartedCounter == 0 && this.mPauseSent) {
            this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
            this.mStopSent = true;
        }
    }
    
    static void init(final Context context) {
        ProcessLifecycleOwner.sInstance.attach(context);
    }
    
    void activityPaused() {
        --this.mResumedCounter;
        if (this.mResumedCounter == 0) {
            this.mHandler.postDelayed(this.mDelayedPauseRunnable, 700L);
        }
    }
    
    void activityResumed() {
        ++this.mResumedCounter;
        if (this.mResumedCounter == 1) {
            if (this.mPauseSent) {
                this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
                this.mPauseSent = false;
            }
            else {
                this.mHandler.removeCallbacks(this.mDelayedPauseRunnable);
            }
        }
    }
    
    void activityStarted() {
        ++this.mStartedCounter;
        if (this.mStartedCounter == 1 && this.mStopSent) {
            this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
            this.mStopSent = false;
        }
    }
    
    void activityStopped() {
        --this.mStartedCounter;
        this.dispatchStopIfNeeded();
    }
    
    void attach(final Context context) {
        this.mHandler = new Handler();
        this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        ((Application)context.getApplicationContext()).registerActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)new EmptyActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(final Activity activity, final Bundle bundle) {
                ReportFragment.get(activity).setProcessListener(ProcessLifecycleOwner.this.mInitializationListener);
            }
            
            @Override
            public void onActivityPaused(final Activity activity) {
                ProcessLifecycleOwner.this.activityPaused();
            }
            
            @Override
            public void onActivityStopped(final Activity activity) {
                ProcessLifecycleOwner.this.activityStopped();
            }
        });
    }
    
    @Override
    public Lifecycle getLifecycle() {
        return this.mRegistry;
    }
}
