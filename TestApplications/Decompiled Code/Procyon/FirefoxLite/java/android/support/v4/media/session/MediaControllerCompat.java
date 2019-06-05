// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media.session;

import android.support.v4.app.BundleCompat;
import android.os.ResultReceiver;
import java.util.Iterator;
import android.util.Log;
import java.util.HashMap;
import android.os.RemoteException;
import java.lang.ref.WeakReference;
import android.os.Handler;
import android.os.Message;
import java.util.List;
import android.support.v4.media.MediaMetadataCompat;
import android.os.Bundle;
import android.os.Build$VERSION;
import android.os.IBinder$DeathRecipient;

public final class MediaControllerCompat
{
    public abstract static class Callback implements IBinder$DeathRecipient
    {
        final Object mCallbackObj;
        MessageHandler mHandler;
        IMediaControllerCallback mIControllerCallback;
        
        public Callback() {
            if (Build$VERSION.SDK_INT >= 21) {
                this.mCallbackObj = MediaControllerCompatApi21.createCallback((MediaControllerCompatApi21.Callback)new StubApi21(this));
            }
            else {
                final StubCompat stubCompat = new StubCompat(this);
                this.mIControllerCallback = stubCompat;
                this.mCallbackObj = stubCompat;
            }
        }
        
        public void onAudioInfoChanged(final PlaybackInfo playbackInfo) {
        }
        
        public void onCaptioningEnabledChanged(final boolean b) {
        }
        
        public void onExtrasChanged(final Bundle bundle) {
        }
        
        public void onMetadataChanged(final MediaMetadataCompat mediaMetadataCompat) {
        }
        
        public void onPlaybackStateChanged(final PlaybackStateCompat playbackStateCompat) {
        }
        
        public void onQueueChanged(final List<MediaSessionCompat.QueueItem> list) {
        }
        
        public void onQueueTitleChanged(final CharSequence charSequence) {
        }
        
        public void onRepeatModeChanged(final int n) {
        }
        
        public void onSessionDestroyed() {
        }
        
        public void onSessionEvent(final String s, final Bundle bundle) {
        }
        
        public void onSessionReady() {
        }
        
        public void onShuffleModeChanged(final int n) {
        }
        
        void postToHandler(final int n, final Object o, final Bundle data) {
            if (this.mHandler != null) {
                final Message obtainMessage = this.mHandler.obtainMessage(n, o);
                obtainMessage.setData(data);
                obtainMessage.sendToTarget();
            }
        }
        
        private class MessageHandler extends Handler
        {
            boolean mRegistered;
            final /* synthetic */ Callback this$0;
            
            public void handleMessage(final Message message) {
                if (!this.mRegistered) {
                    return;
                }
                switch (message.what) {
                    case 13: {
                        this.this$0.onSessionReady();
                        break;
                    }
                    case 12: {
                        this.this$0.onShuffleModeChanged((int)message.obj);
                        break;
                    }
                    case 11: {
                        this.this$0.onCaptioningEnabledChanged((boolean)message.obj);
                        break;
                    }
                    case 9: {
                        this.this$0.onRepeatModeChanged((int)message.obj);
                        break;
                    }
                    case 8: {
                        this.this$0.onSessionDestroyed();
                        break;
                    }
                    case 7: {
                        final Bundle bundle = (Bundle)message.obj;
                        MediaSessionCompat.ensureClassLoader(bundle);
                        this.this$0.onExtrasChanged(bundle);
                        break;
                    }
                    case 6: {
                        this.this$0.onQueueTitleChanged((CharSequence)message.obj);
                        break;
                    }
                    case 5: {
                        this.this$0.onQueueChanged((List<MediaSessionCompat.QueueItem>)message.obj);
                        break;
                    }
                    case 4: {
                        this.this$0.onAudioInfoChanged((PlaybackInfo)message.obj);
                        break;
                    }
                    case 3: {
                        this.this$0.onMetadataChanged((MediaMetadataCompat)message.obj);
                        break;
                    }
                    case 2: {
                        this.this$0.onPlaybackStateChanged((PlaybackStateCompat)message.obj);
                        break;
                    }
                    case 1: {
                        final Bundle data = message.getData();
                        MediaSessionCompat.ensureClassLoader(data);
                        this.this$0.onSessionEvent((String)message.obj, data);
                        break;
                    }
                }
            }
        }
        
        private static class StubApi21 implements MediaControllerCompatApi21.Callback
        {
            private final WeakReference<MediaControllerCompat.Callback> mCallback;
            
            StubApi21(final MediaControllerCompat.Callback referent) {
                this.mCallback = new WeakReference<MediaControllerCompat.Callback>(referent);
            }
            
            @Override
            public void onAudioInfoChanged(final int n, final int n2, final int n3, final int n4, final int n5) {
                final MediaControllerCompat.Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onAudioInfoChanged(new MediaControllerCompat.PlaybackInfo(n, n2, n3, n4, n5));
                }
            }
            
            @Override
            public void onExtrasChanged(final Bundle bundle) {
                final MediaControllerCompat.Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onExtrasChanged(bundle);
                }
            }
            
            @Override
            public void onMetadataChanged(final Object o) {
                final MediaControllerCompat.Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(o));
                }
            }
            
            @Override
            public void onPlaybackStateChanged(final Object o) {
                final MediaControllerCompat.Callback callback = this.mCallback.get();
                if (callback != null) {
                    if (callback.mIControllerCallback == null) {
                        callback.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(o));
                    }
                }
            }
            
            @Override
            public void onQueueChanged(final List<?> list) {
                final MediaControllerCompat.Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onQueueChanged(MediaSessionCompat.QueueItem.fromQueueItemList(list));
                }
            }
            
            @Override
            public void onQueueTitleChanged(final CharSequence charSequence) {
                final MediaControllerCompat.Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onQueueTitleChanged(charSequence);
                }
            }
            
            @Override
            public void onSessionDestroyed() {
                final MediaControllerCompat.Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onSessionDestroyed();
                }
            }
            
            @Override
            public void onSessionEvent(final String s, final Bundle bundle) {
                final MediaControllerCompat.Callback callback = this.mCallback.get();
                if (callback != null) {
                    if (callback.mIControllerCallback == null || Build$VERSION.SDK_INT >= 23) {
                        callback.onSessionEvent(s, bundle);
                    }
                }
            }
        }
        
        private static class StubCompat extends Stub
        {
            private final WeakReference<Callback> mCallback;
            
            StubCompat(final Callback referent) {
                this.mCallback = new WeakReference<Callback>(referent);
            }
            
            public void onCaptioningEnabledChanged(final boolean b) throws RemoteException {
                final Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(11, b, null);
                }
            }
            
            public void onEvent(final String s, final Bundle bundle) throws RemoteException {
                final Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(1, s, bundle);
                }
            }
            
            public void onExtrasChanged(final Bundle bundle) throws RemoteException {
                final Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(7, bundle, null);
                }
            }
            
            public void onMetadataChanged(final MediaMetadataCompat mediaMetadataCompat) throws RemoteException {
                final Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(3, mediaMetadataCompat, null);
                }
            }
            
            public void onPlaybackStateChanged(final PlaybackStateCompat playbackStateCompat) throws RemoteException {
                final Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(2, playbackStateCompat, null);
                }
            }
            
            public void onQueueChanged(final List<MediaSessionCompat.QueueItem> list) throws RemoteException {
                final Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(5, list, null);
                }
            }
            
            public void onQueueTitleChanged(final CharSequence charSequence) throws RemoteException {
                final Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(6, charSequence, null);
                }
            }
            
            public void onRepeatModeChanged(final int i) throws RemoteException {
                final Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(9, i, null);
                }
            }
            
            public void onSessionDestroyed() throws RemoteException {
                final Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(8, null, null);
                }
            }
            
            public void onSessionReady() throws RemoteException {
                final Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(13, null, null);
                }
            }
            
            public void onShuffleModeChanged(final int i) throws RemoteException {
                final Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(12, i, null);
                }
            }
            
            public void onShuffleModeChangedRemoved(final boolean b) throws RemoteException {
            }
            
            public void onVolumeInfoChanged(final ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException {
                final Callback callback = this.mCallback.get();
                if (callback != null) {
                    PlaybackInfo playbackInfo;
                    if (parcelableVolumeInfo != null) {
                        playbackInfo = new PlaybackInfo(parcelableVolumeInfo.volumeType, parcelableVolumeInfo.audioStream, parcelableVolumeInfo.controlType, parcelableVolumeInfo.maxVolume, parcelableVolumeInfo.currentVolume);
                    }
                    else {
                        playbackInfo = null;
                    }
                    callback.postToHandler(4, playbackInfo, null);
                }
            }
        }
    }
    
    static class MediaControllerImplApi21
    {
        private HashMap<Callback, ExtraCallback> mCallbackMap;
        final Object mLock;
        private final List<Callback> mPendingCallbacks;
        final MediaSessionCompat.Token mSessionToken;
        
        void processPendingCallbacksLocked() {
            if (this.mSessionToken.getExtraBinder() == null) {
                return;
            }
            for (final Callback key : this.mPendingCallbacks) {
                final ExtraCallback extraCallback = new ExtraCallback(key);
                this.mCallbackMap.put(key, extraCallback);
                key.mIControllerCallback = extraCallback;
                try {
                    this.mSessionToken.getExtraBinder().registerCallbackListener(extraCallback);
                    key.postToHandler(13, null, null);
                    continue;
                }
                catch (RemoteException ex) {
                    Log.e("MediaControllerCompat", "Dead object in registerCallback.", (Throwable)ex);
                }
                break;
            }
            this.mPendingCallbacks.clear();
        }
        
        private static class ExtraBinderRequestResultReceiver extends ResultReceiver
        {
            private WeakReference<MediaControllerImplApi21> mMediaControllerImpl;
            
            protected void onReceiveResult(final int n, final Bundle bundle) {
                final MediaControllerImplApi21 mediaControllerImplApi21 = this.mMediaControllerImpl.get();
                if (mediaControllerImplApi21 != null) {
                    if (bundle != null) {
                        synchronized (mediaControllerImplApi21.mLock) {
                            mediaControllerImplApi21.mSessionToken.setExtraBinder(IMediaSession.Stub.asInterface(BundleCompat.getBinder(bundle, "android.support.v4.media.session.EXTRA_BINDER")));
                            mediaControllerImplApi21.mSessionToken.setSessionToken2Bundle(bundle.getBundle("android.support.v4.media.session.SESSION_TOKEN2_BUNDLE"));
                            mediaControllerImplApi21.processPendingCallbacksLocked();
                        }
                    }
                }
            }
        }
        
        private static class ExtraCallback extends StubCompat
        {
            ExtraCallback(final Callback callback) {
                super(callback);
            }
            
            @Override
            public void onExtrasChanged(final Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }
            
            @Override
            public void onMetadataChanged(final MediaMetadataCompat mediaMetadataCompat) throws RemoteException {
                throw new AssertionError();
            }
            
            @Override
            public void onQueueChanged(final List<MediaSessionCompat.QueueItem> list) throws RemoteException {
                throw new AssertionError();
            }
            
            @Override
            public void onQueueTitleChanged(final CharSequence charSequence) throws RemoteException {
                throw new AssertionError();
            }
            
            @Override
            public void onSessionDestroyed() throws RemoteException {
                throw new AssertionError();
            }
            
            @Override
            public void onVolumeInfoChanged(final ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException {
                throw new AssertionError();
            }
        }
    }
    
    public static final class PlaybackInfo
    {
        private final int mAudioStream;
        private final int mCurrentVolume;
        private final int mMaxVolume;
        private final int mPlaybackType;
        private final int mVolumeControl;
        
        PlaybackInfo(final int mPlaybackType, final int mAudioStream, final int mVolumeControl, final int mMaxVolume, final int mCurrentVolume) {
            this.mPlaybackType = mPlaybackType;
            this.mAudioStream = mAudioStream;
            this.mVolumeControl = mVolumeControl;
            this.mMaxVolume = mMaxVolume;
            this.mCurrentVolume = mCurrentVolume;
        }
    }
}
