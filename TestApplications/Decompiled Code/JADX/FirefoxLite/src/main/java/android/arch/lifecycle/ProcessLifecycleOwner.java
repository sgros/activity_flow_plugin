package android.arch.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle.Event;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

public class ProcessLifecycleOwner implements LifecycleOwner {
    private static final ProcessLifecycleOwner sInstance = new ProcessLifecycleOwner();
    private Runnable mDelayedPauseRunnable = new C00121();
    private Handler mHandler;
    private ActivityInitializationListener mInitializationListener = new C06042();
    private boolean mPauseSent = true;
    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    private int mResumedCounter = 0;
    private int mStartedCounter = 0;
    private boolean mStopSent = true;

    /* renamed from: android.arch.lifecycle.ProcessLifecycleOwner$1 */
    class C00121 implements Runnable {
        C00121() {
        }

        public void run() {
            ProcessLifecycleOwner.this.dispatchPauseIfNeeded();
            ProcessLifecycleOwner.this.dispatchStopIfNeeded();
        }
    }

    /* renamed from: android.arch.lifecycle.ProcessLifecycleOwner$2 */
    class C06042 implements ActivityInitializationListener {
        public void onCreate() {
        }

        C06042() {
        }

        public void onStart() {
            ProcessLifecycleOwner.this.activityStarted();
        }

        public void onResume() {
            ProcessLifecycleOwner.this.activityResumed();
        }
    }

    /* renamed from: android.arch.lifecycle.ProcessLifecycleOwner$3 */
    class C06053 extends EmptyActivityLifecycleCallbacks {
        C06053() {
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
            ReportFragment.get(activity).setProcessListener(ProcessLifecycleOwner.this.mInitializationListener);
        }

        public void onActivityPaused(Activity activity) {
            ProcessLifecycleOwner.this.activityPaused();
        }

        public void onActivityStopped(Activity activity) {
            ProcessLifecycleOwner.this.activityStopped();
        }
    }

    static void init(Context context) {
        sInstance.attach(context);
    }

    /* Access modifiers changed, original: 0000 */
    public void activityStarted() {
        this.mStartedCounter++;
        if (this.mStartedCounter == 1 && this.mStopSent) {
            this.mRegistry.handleLifecycleEvent(Event.ON_START);
            this.mStopSent = false;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void activityResumed() {
        this.mResumedCounter++;
        if (this.mResumedCounter != 1) {
            return;
        }
        if (this.mPauseSent) {
            this.mRegistry.handleLifecycleEvent(Event.ON_RESUME);
            this.mPauseSent = false;
            return;
        }
        this.mHandler.removeCallbacks(this.mDelayedPauseRunnable);
    }

    /* Access modifiers changed, original: 0000 */
    public void activityPaused() {
        this.mResumedCounter--;
        if (this.mResumedCounter == 0) {
            this.mHandler.postDelayed(this.mDelayedPauseRunnable, 700);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void activityStopped() {
        this.mStartedCounter--;
        dispatchStopIfNeeded();
    }

    private void dispatchPauseIfNeeded() {
        if (this.mResumedCounter == 0) {
            this.mPauseSent = true;
            this.mRegistry.handleLifecycleEvent(Event.ON_PAUSE);
        }
    }

    private void dispatchStopIfNeeded() {
        if (this.mStartedCounter == 0 && this.mPauseSent) {
            this.mRegistry.handleLifecycleEvent(Event.ON_STOP);
            this.mStopSent = true;
        }
    }

    private ProcessLifecycleOwner() {
    }

    /* Access modifiers changed, original: 0000 */
    public void attach(Context context) {
        this.mHandler = new Handler();
        this.mRegistry.handleLifecycleEvent(Event.ON_CREATE);
        ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new C06053());
    }

    public Lifecycle getLifecycle() {
        return this.mRegistry;
    }
}
