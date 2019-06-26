// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import java.util.Iterator;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import android.os.Bundle;
import android.app.Activity;
import android.os.Build$VERSION;
import android.app.Application;
import java.util.concurrent.CopyOnWriteArrayList;
import android.annotation.SuppressLint;
import android.app.Application$ActivityLifecycleCallbacks;

@SuppressLint({ "NewApi" })
public class ForegroundDetector implements Application$ActivityLifecycleCallbacks
{
    private static ForegroundDetector Instance;
    private long enterBackgroundTime;
    private CopyOnWriteArrayList<Listener> listeners;
    private int refs;
    private boolean wasInBackground;
    
    public ForegroundDetector(final Application application) {
        this.wasInBackground = true;
        this.enterBackgroundTime = 0L;
        this.listeners = new CopyOnWriteArrayList<Listener>();
        application.registerActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)(ForegroundDetector.Instance = this));
    }
    
    public static ForegroundDetector getInstance() {
        return ForegroundDetector.Instance;
    }
    
    public void addListener(final Listener e) {
        this.listeners.add(e);
    }
    
    public boolean isBackground() {
        return this.refs == 0;
    }
    
    public boolean isForeground() {
        return this.refs > 0;
    }
    
    public boolean isWasInBackground(final boolean b) {
        if (b && Build$VERSION.SDK_INT >= 21 && System.currentTimeMillis() - this.enterBackgroundTime < 200L) {
            this.wasInBackground = false;
        }
        return this.wasInBackground;
    }
    
    public void onActivityCreated(final Activity activity, final Bundle bundle) {
    }
    
    public void onActivityDestroyed(final Activity activity) {
    }
    
    public void onActivityPaused(final Activity activity) {
    }
    
    public void onActivityResumed(final Activity activity) {
    }
    
    public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
    }
    
    public void onActivityStarted(Activity iterator) {
        final int refs = this.refs + 1;
        this.refs = refs;
        if (refs == 1) {
            if (System.currentTimeMillis() - this.enterBackgroundTime < 200L) {
                this.wasInBackground = false;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("switch to foreground");
            }
            iterator = (Activity)this.listeners.iterator();
            while (((Iterator)iterator).hasNext()) {
                final Listener listener = ((Iterator<Listener>)iterator).next();
                try {
                    listener.onBecameForeground();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
    }
    
    public void onActivityStopped(Activity iterator) {
        final int refs = this.refs - 1;
        this.refs = refs;
        if (refs == 0) {
            this.enterBackgroundTime = System.currentTimeMillis();
            this.wasInBackground = true;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("switch to background");
            }
            iterator = (Activity)this.listeners.iterator();
            while (((Iterator)iterator).hasNext()) {
                final Listener listener = ((Iterator<Listener>)iterator).next();
                try {
                    listener.onBecameBackground();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
    }
    
    public void removeListener(final Listener o) {
        this.listeners.remove(o);
    }
    
    public void resetBackgroundVar() {
        this.wasInBackground = false;
    }
    
    public interface Listener
    {
        void onBecameBackground();
        
        void onBecameForeground();
    }
}
