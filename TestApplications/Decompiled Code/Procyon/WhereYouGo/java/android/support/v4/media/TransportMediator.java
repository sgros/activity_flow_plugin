// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media;

import android.view.KeyEvent$DispatcherState;
import android.os.Build$VERSION;
import android.view.KeyEvent;
import android.app.Activity;
import android.view.View;
import java.util.ArrayList;
import android.view.KeyEvent$Callback;
import android.content.Context;
import android.media.AudioManager;

@Deprecated
public class TransportMediator extends TransportController
{
    @Deprecated
    public static final int FLAG_KEY_MEDIA_FAST_FORWARD = 64;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_NEXT = 128;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_PAUSE = 16;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_PLAY = 4;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_PLAY_PAUSE = 8;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_PREVIOUS = 1;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_REWIND = 2;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_STOP = 32;
    @Deprecated
    public static final int KEYCODE_MEDIA_PAUSE = 127;
    @Deprecated
    public static final int KEYCODE_MEDIA_PLAY = 126;
    @Deprecated
    public static final int KEYCODE_MEDIA_RECORD = 130;
    final AudioManager mAudioManager;
    final TransportPerformer mCallbacks;
    final Context mContext;
    final TransportMediatorJellybeanMR2 mController;
    final Object mDispatcherState;
    final KeyEvent$Callback mKeyEventCallback;
    final ArrayList<TransportStateListener> mListeners;
    final TransportMediatorCallback mTransportKeyCallback;
    final View mView;
    
    @Deprecated
    public TransportMediator(final Activity activity, final TransportPerformer transportPerformer) {
        this(activity, null, transportPerformer);
    }
    
    private TransportMediator(final Activity activity, View decorView, final TransportPerformer mCallbacks) {
        this.mListeners = new ArrayList<TransportStateListener>();
        this.mTransportKeyCallback = new TransportMediatorCallback() {
            @Override
            public long getPlaybackPosition() {
                return TransportMediator.this.mCallbacks.onGetCurrentPosition();
            }
            
            @Override
            public void handleAudioFocusChange(final int n) {
                TransportMediator.this.mCallbacks.onAudioFocusChange(n);
            }
            
            @Override
            public void handleKey(final KeyEvent keyEvent) {
                keyEvent.dispatch(TransportMediator.this.mKeyEventCallback);
            }
            
            @Override
            public void playbackPositionUpdate(final long n) {
                TransportMediator.this.mCallbacks.onSeekTo(n);
            }
        };
        this.mKeyEventCallback = (KeyEvent$Callback)new KeyEvent$Callback() {
            public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
                return TransportMediator.isMediaKey(n) && TransportMediator.this.mCallbacks.onMediaButtonDown(n, keyEvent);
            }
            
            public boolean onKeyLongPress(final int n, final KeyEvent keyEvent) {
                return false;
            }
            
            public boolean onKeyMultiple(final int n, final int n2, final KeyEvent keyEvent) {
                return false;
            }
            
            public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
                return TransportMediator.isMediaKey(n) && TransportMediator.this.mCallbacks.onMediaButtonUp(n, keyEvent);
            }
        };
        Object context;
        if (activity != null) {
            context = activity;
        }
        else {
            context = decorView.getContext();
        }
        this.mContext = (Context)context;
        this.mCallbacks = mCallbacks;
        this.mAudioManager = (AudioManager)this.mContext.getSystemService("audio");
        if (activity != null) {
            decorView = activity.getWindow().getDecorView();
        }
        this.mView = decorView;
        this.mDispatcherState = this.mView.getKeyDispatcherState();
        if (Build$VERSION.SDK_INT >= 18) {
            this.mController = new TransportMediatorJellybeanMR2(this.mContext, this.mAudioManager, this.mView, this.mTransportKeyCallback);
        }
        else {
            this.mController = null;
        }
    }
    
    @Deprecated
    public TransportMediator(final View view, final TransportPerformer transportPerformer) {
        this(null, view, transportPerformer);
    }
    
    private TransportStateListener[] getListeners() {
        TransportStateListener[] a;
        if (this.mListeners.size() <= 0) {
            a = null;
        }
        else {
            a = new TransportStateListener[this.mListeners.size()];
            this.mListeners.toArray(a);
        }
        return a;
    }
    
    static boolean isMediaKey(final int n) {
        boolean b = false;
        switch (n) {
            default: {
                b = false;
                break;
            }
            case 79:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 126:
            case 127:
            case 130: {
                b = true;
                break;
            }
        }
        return b;
    }
    
    private void pushControllerState() {
        if (this.mController != null) {
            this.mController.refreshState(this.mCallbacks.onIsPlaying(), this.mCallbacks.onGetCurrentPosition(), this.mCallbacks.onGetTransportControlFlags());
        }
    }
    
    private void reportPlayingChanged() {
        final TransportStateListener[] listeners = this.getListeners();
        if (listeners != null) {
            for (int length = listeners.length, i = 0; i < length; ++i) {
                listeners[i].onPlayingChanged(this);
            }
        }
    }
    
    private void reportTransportControlsChanged() {
        final TransportStateListener[] listeners = this.getListeners();
        if (listeners != null) {
            for (int length = listeners.length, i = 0; i < length; ++i) {
                listeners[i].onTransportControlsChanged(this);
            }
        }
    }
    
    @Deprecated
    public void destroy() {
        this.mController.destroy();
    }
    
    @Deprecated
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        return keyEvent.dispatch(this.mKeyEventCallback, (KeyEvent$DispatcherState)this.mDispatcherState, (Object)this);
    }
    
    @Deprecated
    @Override
    public int getBufferPercentage() {
        return this.mCallbacks.onGetBufferPercentage();
    }
    
    @Deprecated
    @Override
    public long getCurrentPosition() {
        return this.mCallbacks.onGetCurrentPosition();
    }
    
    @Deprecated
    @Override
    public long getDuration() {
        return this.mCallbacks.onGetDuration();
    }
    
    @Deprecated
    public Object getRemoteControlClient() {
        Object remoteControlClient;
        if (this.mController != null) {
            remoteControlClient = this.mController.getRemoteControlClient();
        }
        else {
            remoteControlClient = null;
        }
        return remoteControlClient;
    }
    
    @Deprecated
    @Override
    public int getTransportControlFlags() {
        return this.mCallbacks.onGetTransportControlFlags();
    }
    
    @Deprecated
    @Override
    public boolean isPlaying() {
        return this.mCallbacks.onIsPlaying();
    }
    
    @Deprecated
    @Override
    public void pausePlaying() {
        if (this.mController != null) {
            this.mController.pausePlaying();
        }
        this.mCallbacks.onPause();
        this.pushControllerState();
        this.reportPlayingChanged();
    }
    
    @Deprecated
    public void refreshState() {
        this.pushControllerState();
        this.reportPlayingChanged();
        this.reportTransportControlsChanged();
    }
    
    @Deprecated
    @Override
    public void registerStateListener(final TransportStateListener e) {
        this.mListeners.add(e);
    }
    
    @Deprecated
    @Override
    public void seekTo(final long n) {
        this.mCallbacks.onSeekTo(n);
    }
    
    @Deprecated
    @Override
    public void startPlaying() {
        if (this.mController != null) {
            this.mController.startPlaying();
        }
        this.mCallbacks.onStart();
        this.pushControllerState();
        this.reportPlayingChanged();
    }
    
    @Deprecated
    @Override
    public void stopPlaying() {
        if (this.mController != null) {
            this.mController.stopPlaying();
        }
        this.mCallbacks.onStop();
        this.pushControllerState();
        this.reportPlayingChanged();
    }
    
    @Deprecated
    @Override
    public void unregisterStateListener(final TransportStateListener o) {
        this.mListeners.remove(o);
    }
}
