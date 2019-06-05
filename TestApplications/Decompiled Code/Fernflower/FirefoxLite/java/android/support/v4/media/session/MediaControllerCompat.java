package android.support.v4.media.session;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.Build.VERSION;
import android.os.IBinder.DeathRecipient;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class MediaControllerCompat {
   public abstract static class Callback implements DeathRecipient {
      final Object mCallbackObj;
      MediaControllerCompat.Callback.MessageHandler mHandler;
      IMediaControllerCallback mIControllerCallback;

      public Callback() {
         if (VERSION.SDK_INT >= 21) {
            this.mCallbackObj = MediaControllerCompatApi21.createCallback(new MediaControllerCompat.Callback.StubApi21(this));
         } else {
            MediaControllerCompat.Callback.StubCompat var1 = new MediaControllerCompat.Callback.StubCompat(this);
            this.mIControllerCallback = var1;
            this.mCallbackObj = var1;
         }

      }

      public void onAudioInfoChanged(MediaControllerCompat.PlaybackInfo var1) {
      }

      public void onCaptioningEnabledChanged(boolean var1) {
      }

      public void onExtrasChanged(Bundle var1) {
      }

      public void onMetadataChanged(MediaMetadataCompat var1) {
      }

      public void onPlaybackStateChanged(PlaybackStateCompat var1) {
      }

      public void onQueueChanged(List var1) {
      }

      public void onQueueTitleChanged(CharSequence var1) {
      }

      public void onRepeatModeChanged(int var1) {
      }

      public void onSessionDestroyed() {
      }

      public void onSessionEvent(String var1, Bundle var2) {
      }

      public void onSessionReady() {
      }

      public void onShuffleModeChanged(int var1) {
      }

      void postToHandler(int var1, Object var2, Bundle var3) {
         if (this.mHandler != null) {
            Message var4 = this.mHandler.obtainMessage(var1, var2);
            var4.setData(var3);
            var4.sendToTarget();
         }

      }

      private class MessageHandler extends Handler {
         boolean mRegistered;
         // $FF: synthetic field
         final MediaControllerCompat.Callback this$0;

         public void handleMessage(Message var1) {
            if (this.mRegistered) {
               switch(var1.what) {
               case 1:
                  Bundle var2 = var1.getData();
                  MediaSessionCompat.ensureClassLoader(var2);
                  this.this$0.onSessionEvent((String)var1.obj, var2);
                  break;
               case 2:
                  this.this$0.onPlaybackStateChanged((PlaybackStateCompat)var1.obj);
                  break;
               case 3:
                  this.this$0.onMetadataChanged((MediaMetadataCompat)var1.obj);
                  break;
               case 4:
                  this.this$0.onAudioInfoChanged((MediaControllerCompat.PlaybackInfo)var1.obj);
                  break;
               case 5:
                  this.this$0.onQueueChanged((List)var1.obj);
                  break;
               case 6:
                  this.this$0.onQueueTitleChanged((CharSequence)var1.obj);
                  break;
               case 7:
                  Bundle var3 = (Bundle)var1.obj;
                  MediaSessionCompat.ensureClassLoader(var3);
                  this.this$0.onExtrasChanged(var3);
                  break;
               case 8:
                  this.this$0.onSessionDestroyed();
                  break;
               case 9:
                  this.this$0.onRepeatModeChanged((Integer)var1.obj);
               case 10:
               default:
                  break;
               case 11:
                  this.this$0.onCaptioningEnabledChanged((Boolean)var1.obj);
                  break;
               case 12:
                  this.this$0.onShuffleModeChanged((Integer)var1.obj);
                  break;
               case 13:
                  this.this$0.onSessionReady();
               }

            }
         }
      }

      private static class StubApi21 implements MediaControllerCompatApi21.Callback {
         private final WeakReference mCallback;

         StubApi21(MediaControllerCompat.Callback var1) {
            this.mCallback = new WeakReference(var1);
         }

         public void onAudioInfoChanged(int var1, int var2, int var3, int var4, int var5) {
            MediaControllerCompat.Callback var6 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var6 != null) {
               var6.onAudioInfoChanged(new MediaControllerCompat.PlaybackInfo(var1, var2, var3, var4, var5));
            }

         }

         public void onExtrasChanged(Bundle var1) {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.onExtrasChanged(var1);
            }

         }

         public void onMetadataChanged(Object var1) {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(var1));
            }

         }

         public void onPlaybackStateChanged(Object var1) {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null && var2.mIControllerCallback == null) {
               var2.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(var1));
            }

         }

         public void onQueueChanged(List var1) {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.onQueueChanged(MediaSessionCompat.QueueItem.fromQueueItemList(var1));
            }

         }

         public void onQueueTitleChanged(CharSequence var1) {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.onQueueTitleChanged(var1);
            }

         }

         public void onSessionDestroyed() {
            MediaControllerCompat.Callback var1 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var1 != null) {
               var1.onSessionDestroyed();
            }

         }

         public void onSessionEvent(String var1, Bundle var2) {
            MediaControllerCompat.Callback var3 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var3 != null && (var3.mIControllerCallback == null || VERSION.SDK_INT >= 23)) {
               var3.onSessionEvent(var1, var2);
            }

         }
      }

      private static class StubCompat extends IMediaControllerCallback.Stub {
         private final WeakReference mCallback;

         StubCompat(MediaControllerCompat.Callback var1) {
            this.mCallback = new WeakReference(var1);
         }

         public void onCaptioningEnabledChanged(boolean var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(11, var1, (Bundle)null);
            }

         }

         public void onEvent(String var1, Bundle var2) throws RemoteException {
            MediaControllerCompat.Callback var3 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var3 != null) {
               var3.postToHandler(1, var1, var2);
            }

         }

         public void onExtrasChanged(Bundle var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(7, var1, (Bundle)null);
            }

         }

         public void onMetadataChanged(MediaMetadataCompat var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(3, var1, (Bundle)null);
            }

         }

         public void onPlaybackStateChanged(PlaybackStateCompat var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(2, var1, (Bundle)null);
            }

         }

         public void onQueueChanged(List var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(5, var1, (Bundle)null);
            }

         }

         public void onQueueTitleChanged(CharSequence var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(6, var1, (Bundle)null);
            }

         }

         public void onRepeatModeChanged(int var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(9, var1, (Bundle)null);
            }

         }

         public void onSessionDestroyed() throws RemoteException {
            MediaControllerCompat.Callback var1 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var1 != null) {
               var1.postToHandler(8, (Object)null, (Bundle)null);
            }

         }

         public void onSessionReady() throws RemoteException {
            MediaControllerCompat.Callback var1 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var1 != null) {
               var1.postToHandler(13, (Object)null, (Bundle)null);
            }

         }

         public void onShuffleModeChanged(int var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(12, var1, (Bundle)null);
            }

         }

         public void onShuffleModeChangedRemoved(boolean var1) throws RemoteException {
         }

         public void onVolumeInfoChanged(ParcelableVolumeInfo var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               MediaControllerCompat.PlaybackInfo var3;
               if (var1 != null) {
                  var3 = new MediaControllerCompat.PlaybackInfo(var1.volumeType, var1.audioStream, var1.controlType, var1.maxVolume, var1.currentVolume);
               } else {
                  var3 = null;
               }

               var2.postToHandler(4, var3, (Bundle)null);
            }

         }
      }
   }

   static class MediaControllerImplApi21 {
      private HashMap mCallbackMap;
      final Object mLock;
      private final List mPendingCallbacks;
      final MediaSessionCompat.Token mSessionToken;

      void processPendingCallbacksLocked() {
         if (this.mSessionToken.getExtraBinder() != null) {
            MediaControllerCompat.Callback var2;
            for(Iterator var1 = this.mPendingCallbacks.iterator(); var1.hasNext(); var2.postToHandler(13, (Object)null, (Bundle)null)) {
               var2 = (MediaControllerCompat.Callback)var1.next();
               MediaControllerCompat.MediaControllerImplApi21.ExtraCallback var3 = new MediaControllerCompat.MediaControllerImplApi21.ExtraCallback(var2);
               this.mCallbackMap.put(var2, var3);
               var2.mIControllerCallback = var3;

               try {
                  this.mSessionToken.getExtraBinder().registerCallbackListener(var3);
               } catch (RemoteException var4) {
                  Log.e("MediaControllerCompat", "Dead object in registerCallback.", var4);
                  break;
               }
            }

            this.mPendingCallbacks.clear();
         }
      }

      private static class ExtraBinderRequestResultReceiver extends ResultReceiver {
         private WeakReference mMediaControllerImpl;

         protected void onReceiveResult(int param1, Bundle param2) {
            // $FF: Couldn't be decompiled
         }
      }

      private static class ExtraCallback extends MediaControllerCompat.Callback.StubCompat {
         ExtraCallback(MediaControllerCompat.Callback var1) {
            super(var1);
         }

         public void onExtrasChanged(Bundle var1) throws RemoteException {
            throw new AssertionError();
         }

         public void onMetadataChanged(MediaMetadataCompat var1) throws RemoteException {
            throw new AssertionError();
         }

         public void onQueueChanged(List var1) throws RemoteException {
            throw new AssertionError();
         }

         public void onQueueTitleChanged(CharSequence var1) throws RemoteException {
            throw new AssertionError();
         }

         public void onSessionDestroyed() throws RemoteException {
            throw new AssertionError();
         }

         public void onVolumeInfoChanged(ParcelableVolumeInfo var1) throws RemoteException {
            throw new AssertionError();
         }
      }
   }

   public static final class PlaybackInfo {
      private final int mAudioStream;
      private final int mCurrentVolume;
      private final int mMaxVolume;
      private final int mPlaybackType;
      private final int mVolumeControl;

      PlaybackInfo(int var1, int var2, int var3, int var4, int var5) {
         this.mPlaybackType = var1;
         this.mAudioStream = var2;
         this.mVolumeControl = var3;
         this.mMaxVolume = var4;
         this.mCurrentVolume = var5;
      }
   }
}
