package android.support.v4.media.session;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.Build.VERSION;
import android.os.IBinder.DeathRecipient;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.SupportActivity;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class MediaControllerCompat {
   static final String COMMAND_ADD_QUEUE_ITEM = "android.support.v4.media.session.command.ADD_QUEUE_ITEM";
   static final String COMMAND_ADD_QUEUE_ITEM_AT = "android.support.v4.media.session.command.ADD_QUEUE_ITEM_AT";
   static final String COMMAND_ARGUMENT_INDEX = "android.support.v4.media.session.command.ARGUMENT_INDEX";
   static final String COMMAND_ARGUMENT_MEDIA_DESCRIPTION = "android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION";
   static final String COMMAND_GET_EXTRA_BINDER = "android.support.v4.media.session.command.GET_EXTRA_BINDER";
   static final String COMMAND_REMOVE_QUEUE_ITEM = "android.support.v4.media.session.command.REMOVE_QUEUE_ITEM";
   static final String COMMAND_REMOVE_QUEUE_ITEM_AT = "android.support.v4.media.session.command.REMOVE_QUEUE_ITEM_AT";
   static final String TAG = "MediaControllerCompat";
   private final MediaControllerCompat.MediaControllerImpl mImpl;
   private final MediaSessionCompat.Token mToken;

   public MediaControllerCompat(Context var1, MediaSessionCompat.Token var2) throws RemoteException {
      if (var2 == null) {
         throw new IllegalArgumentException("sessionToken must not be null");
      } else {
         this.mToken = var2;
         if (VERSION.SDK_INT >= 24) {
            this.mImpl = new MediaControllerCompat.MediaControllerImplApi24(var1, var2);
         } else if (VERSION.SDK_INT >= 23) {
            this.mImpl = new MediaControllerCompat.MediaControllerImplApi23(var1, var2);
         } else if (VERSION.SDK_INT >= 21) {
            this.mImpl = new MediaControllerCompat.MediaControllerImplApi21(var1, var2);
         } else {
            this.mImpl = new MediaControllerCompat.MediaControllerImplBase(this.mToken);
         }

      }
   }

   public MediaControllerCompat(Context var1, MediaSessionCompat var2) {
      if (var2 == null) {
         throw new IllegalArgumentException("session must not be null");
      } else {
         this.mToken = var2.getSessionToken();
         if (VERSION.SDK_INT >= 24) {
            this.mImpl = new MediaControllerCompat.MediaControllerImplApi24(var1, var2);
         } else if (VERSION.SDK_INT >= 23) {
            this.mImpl = new MediaControllerCompat.MediaControllerImplApi23(var1, var2);
         } else if (VERSION.SDK_INT >= 21) {
            this.mImpl = new MediaControllerCompat.MediaControllerImplApi21(var1, var2);
         } else {
            this.mImpl = new MediaControllerCompat.MediaControllerImplBase(this.mToken);
         }

      }
   }

   public static MediaControllerCompat getMediaController(Activity var0) {
      Object var1 = null;
      MediaControllerCompat var2;
      if (var0 instanceof SupportActivity) {
         MediaControllerCompat.MediaControllerExtraData var5 = (MediaControllerCompat.MediaControllerExtraData)((SupportActivity)var0).getExtraData(MediaControllerCompat.MediaControllerExtraData.class);
         var2 = (MediaControllerCompat)var1;
         if (var5 != null) {
            var2 = var5.getMediaController();
         }
      } else {
         var2 = (MediaControllerCompat)var1;
         if (VERSION.SDK_INT >= 21) {
            Object var3 = MediaControllerCompatApi21.getMediaController(var0);
            var2 = (MediaControllerCompat)var1;
            if (var3 != null) {
               var3 = MediaControllerCompatApi21.getSessionToken(var3);

               try {
                  var2 = new MediaControllerCompat(var0, MediaSessionCompat.Token.fromToken(var3));
               } catch (RemoteException var4) {
                  Log.e("MediaControllerCompat", "Dead object in getMediaController.", var4);
                  var2 = (MediaControllerCompat)var1;
               }
            }
         }
      }

      return var2;
   }

   public static void setMediaController(Activity var0, MediaControllerCompat var1) {
      if (var0 instanceof SupportActivity) {
         ((SupportActivity)var0).putExtraData(new MediaControllerCompat.MediaControllerExtraData(var1));
      }

      if (VERSION.SDK_INT >= 21) {
         Object var2 = null;
         if (var1 != null) {
            var2 = MediaControllerCompatApi21.fromToken(var0, var1.getSessionToken().getToken());
         }

         MediaControllerCompatApi21.setMediaController(var0, var2);
      }

   }

   public void addQueueItem(MediaDescriptionCompat var1) {
      this.mImpl.addQueueItem(var1);
   }

   public void addQueueItem(MediaDescriptionCompat var1, int var2) {
      this.mImpl.addQueueItem(var1, var2);
   }

   public void adjustVolume(int var1, int var2) {
      this.mImpl.adjustVolume(var1, var2);
   }

   public boolean dispatchMediaButtonEvent(KeyEvent var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("KeyEvent may not be null");
      } else {
         return this.mImpl.dispatchMediaButtonEvent(var1);
      }
   }

   public Bundle getExtras() {
      return this.mImpl.getExtras();
   }

   public long getFlags() {
      return this.mImpl.getFlags();
   }

   public Object getMediaController() {
      return this.mImpl.getMediaController();
   }

   public MediaMetadataCompat getMetadata() {
      return this.mImpl.getMetadata();
   }

   public String getPackageName() {
      return this.mImpl.getPackageName();
   }

   public MediaControllerCompat.PlaybackInfo getPlaybackInfo() {
      return this.mImpl.getPlaybackInfo();
   }

   public PlaybackStateCompat getPlaybackState() {
      return this.mImpl.getPlaybackState();
   }

   public List getQueue() {
      return this.mImpl.getQueue();
   }

   public CharSequence getQueueTitle() {
      return this.mImpl.getQueueTitle();
   }

   public int getRatingType() {
      return this.mImpl.getRatingType();
   }

   public int getRepeatMode() {
      return this.mImpl.getRepeatMode();
   }

   public PendingIntent getSessionActivity() {
      return this.mImpl.getSessionActivity();
   }

   public MediaSessionCompat.Token getSessionToken() {
      return this.mToken;
   }

   public MediaControllerCompat.TransportControls getTransportControls() {
      return this.mImpl.getTransportControls();
   }

   @VisibleForTesting
   boolean isExtraBinderReady() {
      boolean var1;
      if (this.mImpl instanceof MediaControllerCompat.MediaControllerImplApi21) {
         if (((MediaControllerCompat.MediaControllerImplApi21)this.mImpl).mExtraBinder != null) {
            var1 = true;
         } else {
            var1 = false;
         }
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isShuffleModeEnabled() {
      return this.mImpl.isShuffleModeEnabled();
   }

   public void registerCallback(MediaControllerCompat.Callback var1) {
      this.registerCallback(var1, (Handler)null);
   }

   public void registerCallback(MediaControllerCompat.Callback var1, Handler var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("callback cannot be null");
      } else {
         Handler var3 = var2;
         if (var2 == null) {
            var3 = new Handler();
         }

         this.mImpl.registerCallback(var1, var3);
      }
   }

   public void removeQueueItem(MediaDescriptionCompat var1) {
      this.mImpl.removeQueueItem(var1);
   }

   public void removeQueueItemAt(int var1) {
      this.mImpl.removeQueueItemAt(var1);
   }

   public void sendCommand(String var1, Bundle var2, ResultReceiver var3) {
      if (TextUtils.isEmpty(var1)) {
         throw new IllegalArgumentException("command cannot be null or empty");
      } else {
         this.mImpl.sendCommand(var1, var2, var3);
      }
   }

   public void setVolumeTo(int var1, int var2) {
      this.mImpl.setVolumeTo(var1, var2);
   }

   public void unregisterCallback(MediaControllerCompat.Callback var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("callback cannot be null");
      } else {
         this.mImpl.unregisterCallback(var1);
      }
   }

   public abstract static class Callback implements DeathRecipient {
      private final Object mCallbackObj;
      MediaControllerCompat.Callback.MessageHandler mHandler;
      boolean mHasExtraCallback;
      boolean mRegistered = false;

      public Callback() {
         if (VERSION.SDK_INT >= 21) {
            this.mCallbackObj = MediaControllerCompatApi21.createCallback(new MediaControllerCompat.Callback.StubApi21());
         } else {
            this.mCallbackObj = new MediaControllerCompat.Callback.StubCompat();
         }

      }

      private void setHandler(Handler var1) {
         this.mHandler = new MediaControllerCompat.Callback.MessageHandler(var1.getLooper());
      }

      public void binderDied() {
         this.onSessionDestroyed();
      }

      public void onAudioInfoChanged(MediaControllerCompat.PlaybackInfo var1) {
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

      public void onShuffleModeChanged(boolean var1) {
      }

      private class MessageHandler extends Handler {
         private static final int MSG_DESTROYED = 8;
         private static final int MSG_EVENT = 1;
         private static final int MSG_UPDATE_EXTRAS = 7;
         private static final int MSG_UPDATE_METADATA = 3;
         private static final int MSG_UPDATE_PLAYBACK_STATE = 2;
         private static final int MSG_UPDATE_QUEUE = 5;
         private static final int MSG_UPDATE_QUEUE_TITLE = 6;
         private static final int MSG_UPDATE_REPEAT_MODE = 9;
         private static final int MSG_UPDATE_SHUFFLE_MODE = 10;
         private static final int MSG_UPDATE_VOLUME = 4;

         public MessageHandler(Looper var2) {
            super(var2);
         }

         public void handleMessage(Message var1) {
            if (Callback.this.mRegistered) {
               switch(var1.what) {
               case 1:
                  Callback.this.onSessionEvent((String)var1.obj, var1.getData());
                  break;
               case 2:
                  Callback.this.onPlaybackStateChanged((PlaybackStateCompat)var1.obj);
                  break;
               case 3:
                  Callback.this.onMetadataChanged((MediaMetadataCompat)var1.obj);
                  break;
               case 4:
                  Callback.this.onAudioInfoChanged((MediaControllerCompat.PlaybackInfo)var1.obj);
                  break;
               case 5:
                  Callback.this.onQueueChanged((List)var1.obj);
                  break;
               case 6:
                  Callback.this.onQueueTitleChanged((CharSequence)var1.obj);
                  break;
               case 7:
                  Callback.this.onExtrasChanged((Bundle)var1.obj);
                  break;
               case 8:
                  Callback.this.onSessionDestroyed();
                  break;
               case 9:
                  Callback.this.onRepeatModeChanged((Integer)var1.obj);
                  break;
               case 10:
                  Callback.this.onShuffleModeChanged((Boolean)var1.obj);
               }
            }

         }

         public void post(int var1, Object var2, Bundle var3) {
            Message var4 = this.obtainMessage(var1, var2);
            var4.setData(var3);
            var4.sendToTarget();
         }
      }

      private class StubApi21 implements MediaControllerCompatApi21.Callback {
         StubApi21() {
         }

         public void onAudioInfoChanged(int var1, int var2, int var3, int var4, int var5) {
            Callback.this.onAudioInfoChanged(new MediaControllerCompat.PlaybackInfo(var1, var2, var3, var4, var5));
         }

         public void onExtrasChanged(Bundle var1) {
            Callback.this.onExtrasChanged(var1);
         }

         public void onMetadataChanged(Object var1) {
            Callback.this.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(var1));
         }

         public void onPlaybackStateChanged(Object var1) {
            if (!Callback.this.mHasExtraCallback) {
               Callback.this.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(var1));
            }

         }

         public void onQueueChanged(List var1) {
            Callback.this.onQueueChanged(MediaSessionCompat.QueueItem.fromQueueItemList(var1));
         }

         public void onQueueTitleChanged(CharSequence var1) {
            Callback.this.onQueueTitleChanged(var1);
         }

         public void onSessionDestroyed() {
            Callback.this.onSessionDestroyed();
         }

         public void onSessionEvent(String var1, Bundle var2) {
            if (!Callback.this.mHasExtraCallback || VERSION.SDK_INT >= 23) {
               Callback.this.onSessionEvent(var1, var2);
            }

         }
      }

      private class StubCompat extends IMediaControllerCallback.Stub {
         StubCompat() {
         }

         public void onEvent(String var1, Bundle var2) throws RemoteException {
            Callback.this.mHandler.post(1, var1, var2);
         }

         public void onExtrasChanged(Bundle var1) throws RemoteException {
            Callback.this.mHandler.post(7, var1, (Bundle)null);
         }

         public void onMetadataChanged(MediaMetadataCompat var1) throws RemoteException {
            Callback.this.mHandler.post(3, var1, (Bundle)null);
         }

         public void onPlaybackStateChanged(PlaybackStateCompat var1) throws RemoteException {
            Callback.this.mHandler.post(2, var1, (Bundle)null);
         }

         public void onQueueChanged(List var1) throws RemoteException {
            Callback.this.mHandler.post(5, var1, (Bundle)null);
         }

         public void onQueueTitleChanged(CharSequence var1) throws RemoteException {
            Callback.this.mHandler.post(6, var1, (Bundle)null);
         }

         public void onRepeatModeChanged(int var1) throws RemoteException {
            Callback.this.mHandler.post(9, var1, (Bundle)null);
         }

         public void onSessionDestroyed() throws RemoteException {
            Callback.this.mHandler.post(8, (Object)null, (Bundle)null);
         }

         public void onShuffleModeChanged(boolean var1) throws RemoteException {
            Callback.this.mHandler.post(10, var1, (Bundle)null);
         }

         public void onVolumeInfoChanged(ParcelableVolumeInfo var1) throws RemoteException {
            MediaControllerCompat.PlaybackInfo var2 = null;
            if (var1 != null) {
               var2 = new MediaControllerCompat.PlaybackInfo(var1.volumeType, var1.audioStream, var1.controlType, var1.maxVolume, var1.currentVolume);
            }

            Callback.this.mHandler.post(4, var2, (Bundle)null);
         }
      }
   }

   private static class MediaControllerExtraData extends SupportActivity.ExtraData {
      private final MediaControllerCompat mMediaController;

      MediaControllerExtraData(MediaControllerCompat var1) {
         this.mMediaController = var1;
      }

      MediaControllerCompat getMediaController() {
         return this.mMediaController;
      }
   }

   interface MediaControllerImpl {
      void addQueueItem(MediaDescriptionCompat var1);

      void addQueueItem(MediaDescriptionCompat var1, int var2);

      void adjustVolume(int var1, int var2);

      boolean dispatchMediaButtonEvent(KeyEvent var1);

      Bundle getExtras();

      long getFlags();

      Object getMediaController();

      MediaMetadataCompat getMetadata();

      String getPackageName();

      MediaControllerCompat.PlaybackInfo getPlaybackInfo();

      PlaybackStateCompat getPlaybackState();

      List getQueue();

      CharSequence getQueueTitle();

      int getRatingType();

      int getRepeatMode();

      PendingIntent getSessionActivity();

      MediaControllerCompat.TransportControls getTransportControls();

      boolean isShuffleModeEnabled();

      void registerCallback(MediaControllerCompat.Callback var1, Handler var2);

      void removeQueueItem(MediaDescriptionCompat var1);

      void removeQueueItemAt(int var1);

      void sendCommand(String var1, Bundle var2, ResultReceiver var3);

      void setVolumeTo(int var1, int var2);

      void unregisterCallback(MediaControllerCompat.Callback var1);
   }

   static class MediaControllerImplApi21 implements MediaControllerCompat.MediaControllerImpl {
      private HashMap mCallbackMap = new HashMap();
      protected final Object mControllerObj;
      private IMediaSession mExtraBinder;
      private List mPendingCallbacks = new ArrayList();

      public MediaControllerImplApi21(Context var1, MediaSessionCompat.Token var2) throws RemoteException {
         this.mControllerObj = MediaControllerCompatApi21.fromToken(var1, var2.getToken());
         if (this.mControllerObj == null) {
            throw new RemoteException();
         } else {
            this.requestExtraBinder();
         }
      }

      public MediaControllerImplApi21(Context var1, MediaSessionCompat var2) {
         this.mControllerObj = MediaControllerCompatApi21.fromToken(var1, var2.getSessionToken().getToken());
         this.requestExtraBinder();
      }

      private void processPendingCallbacks() {
         // $FF: Couldn't be decompiled
      }

      private void requestExtraBinder() {
         this.sendCommand("android.support.v4.media.session.command.GET_EXTRA_BINDER", (Bundle)null, new MediaControllerCompat.MediaControllerImplApi21.ExtraBinderRequestResultReceiver(this, new Handler()));
      }

      public void addQueueItem(MediaDescriptionCompat var1) {
         if ((4L & this.getFlags()) == 0L) {
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
         } else {
            Bundle var2 = new Bundle();
            var2.putParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION", var1);
            this.sendCommand("android.support.v4.media.session.command.ADD_QUEUE_ITEM", var2, (ResultReceiver)null);
         }
      }

      public void addQueueItem(MediaDescriptionCompat var1, int var2) {
         if ((4L & this.getFlags()) == 0L) {
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
         } else {
            Bundle var3 = new Bundle();
            var3.putParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION", var1);
            var3.putInt("android.support.v4.media.session.command.ARGUMENT_INDEX", var2);
            this.sendCommand("android.support.v4.media.session.command.ADD_QUEUE_ITEM_AT", var3, (ResultReceiver)null);
         }
      }

      public void adjustVolume(int var1, int var2) {
         MediaControllerCompatApi21.adjustVolume(this.mControllerObj, var1, var2);
      }

      public boolean dispatchMediaButtonEvent(KeyEvent var1) {
         return MediaControllerCompatApi21.dispatchMediaButtonEvent(this.mControllerObj, var1);
      }

      public Bundle getExtras() {
         return MediaControllerCompatApi21.getExtras(this.mControllerObj);
      }

      public long getFlags() {
         return MediaControllerCompatApi21.getFlags(this.mControllerObj);
      }

      public Object getMediaController() {
         return this.mControllerObj;
      }

      public MediaMetadataCompat getMetadata() {
         Object var1 = MediaControllerCompatApi21.getMetadata(this.mControllerObj);
         MediaMetadataCompat var2;
         if (var1 != null) {
            var2 = MediaMetadataCompat.fromMediaMetadata(var1);
         } else {
            var2 = null;
         }

         return var2;
      }

      public String getPackageName() {
         return MediaControllerCompatApi21.getPackageName(this.mControllerObj);
      }

      public MediaControllerCompat.PlaybackInfo getPlaybackInfo() {
         Object var1 = MediaControllerCompatApi21.getPlaybackInfo(this.mControllerObj);
         MediaControllerCompat.PlaybackInfo var2;
         if (var1 != null) {
            var2 = new MediaControllerCompat.PlaybackInfo(MediaControllerCompatApi21.PlaybackInfo.getPlaybackType(var1), MediaControllerCompatApi21.PlaybackInfo.getLegacyAudioStream(var1), MediaControllerCompatApi21.PlaybackInfo.getVolumeControl(var1), MediaControllerCompatApi21.PlaybackInfo.getMaxVolume(var1), MediaControllerCompatApi21.PlaybackInfo.getCurrentVolume(var1));
         } else {
            var2 = null;
         }

         return var2;
      }

      public PlaybackStateCompat getPlaybackState() {
         PlaybackStateCompat var3;
         if (this.mExtraBinder != null) {
            try {
               var3 = this.mExtraBinder.getPlaybackState();
               return var3;
            } catch (RemoteException var2) {
               Log.e("MediaControllerCompat", "Dead object in getPlaybackState.", var2);
            }
         }

         Object var1 = MediaControllerCompatApi21.getPlaybackState(this.mControllerObj);
         if (var1 != null) {
            var3 = PlaybackStateCompat.fromPlaybackState(var1);
         } else {
            var3 = null;
         }

         return var3;
      }

      public List getQueue() {
         List var1 = MediaControllerCompatApi21.getQueue(this.mControllerObj);
         if (var1 != null) {
            var1 = MediaSessionCompat.QueueItem.fromQueueItemList(var1);
         } else {
            var1 = null;
         }

         return var1;
      }

      public CharSequence getQueueTitle() {
         return MediaControllerCompatApi21.getQueueTitle(this.mControllerObj);
      }

      public int getRatingType() {
         int var1;
         if (VERSION.SDK_INT < 22 && this.mExtraBinder != null) {
            try {
               var1 = this.mExtraBinder.getRatingType();
               return var1;
            } catch (RemoteException var3) {
               Log.e("MediaControllerCompat", "Dead object in getRatingType.", var3);
            }
         }

         var1 = MediaControllerCompatApi21.getRatingType(this.mControllerObj);
         return var1;
      }

      public int getRepeatMode() {
         int var1;
         if (this.mExtraBinder != null) {
            try {
               var1 = this.mExtraBinder.getRepeatMode();
               return var1;
            } catch (RemoteException var3) {
               Log.e("MediaControllerCompat", "Dead object in getRepeatMode.", var3);
            }
         }

         var1 = 0;
         return var1;
      }

      public PendingIntent getSessionActivity() {
         return MediaControllerCompatApi21.getSessionActivity(this.mControllerObj);
      }

      public MediaControllerCompat.TransportControls getTransportControls() {
         Object var1 = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
         MediaControllerCompat.TransportControlsApi21 var2;
         if (var1 != null) {
            var2 = new MediaControllerCompat.TransportControlsApi21(var1);
         } else {
            var2 = null;
         }

         return var2;
      }

      public boolean isShuffleModeEnabled() {
         boolean var1;
         if (this.mExtraBinder != null) {
            try {
               var1 = this.mExtraBinder.isShuffleModeEnabled();
               return var1;
            } catch (RemoteException var3) {
               Log.e("MediaControllerCompat", "Dead object in isShuffleModeEnabled.", var3);
            }
         }

         var1 = false;
         return var1;
      }

      public final void registerCallback(MediaControllerCompat.Callback param1, Handler param2) {
         // $FF: Couldn't be decompiled
      }

      public void removeQueueItem(MediaDescriptionCompat var1) {
         if ((4L & this.getFlags()) == 0L) {
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
         } else {
            Bundle var2 = new Bundle();
            var2.putParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION", var1);
            this.sendCommand("android.support.v4.media.session.command.REMOVE_QUEUE_ITEM", var2, (ResultReceiver)null);
         }
      }

      public void removeQueueItemAt(int var1) {
         if ((4L & this.getFlags()) == 0L) {
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
         } else {
            Bundle var2 = new Bundle();
            var2.putInt("android.support.v4.media.session.command.ARGUMENT_INDEX", var1);
            this.sendCommand("android.support.v4.media.session.command.REMOVE_QUEUE_ITEM_AT", var2, (ResultReceiver)null);
         }
      }

      public void sendCommand(String var1, Bundle var2, ResultReceiver var3) {
         MediaControllerCompatApi21.sendCommand(this.mControllerObj, var1, var2, var3);
      }

      public void setVolumeTo(int var1, int var2) {
         MediaControllerCompatApi21.setVolumeTo(this.mControllerObj, var1, var2);
      }

      public final void unregisterCallback(MediaControllerCompat.Callback param1) {
         // $FF: Couldn't be decompiled
      }

      private static class ExtraBinderRequestResultReceiver extends ResultReceiver {
         private WeakReference mMediaControllerImpl;

         public ExtraBinderRequestResultReceiver(MediaControllerCompat.MediaControllerImplApi21 var1, Handler var2) {
            super(var2);
            this.mMediaControllerImpl = new WeakReference(var1);
         }

         protected void onReceiveResult(int var1, Bundle var2) {
            MediaControllerCompat.MediaControllerImplApi21 var3 = (MediaControllerCompat.MediaControllerImplApi21)this.mMediaControllerImpl.get();
            if (var3 != null && var2 != null) {
               var3.mExtraBinder = IMediaSession.Stub.asInterface(BundleCompat.getBinder(var2, "android.support.v4.media.session.EXTRA_BINDER"));
               var3.processPendingCallbacks();
            }

         }
      }

      private class ExtraCallback extends IMediaControllerCallback.Stub {
         private MediaControllerCompat.Callback mCallback;

         ExtraCallback(MediaControllerCompat.Callback var2) {
            this.mCallback = var2;
         }

         public void onEvent(final String var1, final Bundle var2) throws RemoteException {
            this.mCallback.mHandler.post(new Runnable() {
               public void run() {
                  ExtraCallback.this.mCallback.onSessionEvent(var1, var2);
               }
            });
         }

         public void onExtrasChanged(Bundle var1) throws RemoteException {
            throw new AssertionError();
         }

         public void onMetadataChanged(MediaMetadataCompat var1) throws RemoteException {
            throw new AssertionError();
         }

         public void onPlaybackStateChanged(final PlaybackStateCompat var1) throws RemoteException {
            this.mCallback.mHandler.post(new Runnable() {
               public void run() {
                  ExtraCallback.this.mCallback.onPlaybackStateChanged(var1);
               }
            });
         }

         public void onQueueChanged(List var1) throws RemoteException {
            throw new AssertionError();
         }

         public void onQueueTitleChanged(CharSequence var1) throws RemoteException {
            throw new AssertionError();
         }

         public void onRepeatModeChanged(final int var1) throws RemoteException {
            this.mCallback.mHandler.post(new Runnable() {
               public void run() {
                  ExtraCallback.this.mCallback.onRepeatModeChanged(var1);
               }
            });
         }

         public void onSessionDestroyed() throws RemoteException {
            throw new AssertionError();
         }

         public void onShuffleModeChanged(final boolean var1) throws RemoteException {
            this.mCallback.mHandler.post(new Runnable() {
               public void run() {
                  ExtraCallback.this.mCallback.onShuffleModeChanged(var1);
               }
            });
         }

         public void onVolumeInfoChanged(ParcelableVolumeInfo var1) throws RemoteException {
            throw new AssertionError();
         }
      }
   }

   static class MediaControllerImplApi23 extends MediaControllerCompat.MediaControllerImplApi21 {
      public MediaControllerImplApi23(Context var1, MediaSessionCompat.Token var2) throws RemoteException {
         super(var1, var2);
      }

      public MediaControllerImplApi23(Context var1, MediaSessionCompat var2) {
         super(var1, var2);
      }

      public MediaControllerCompat.TransportControls getTransportControls() {
         Object var1 = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
         MediaControllerCompat.TransportControlsApi23 var2;
         if (var1 != null) {
            var2 = new MediaControllerCompat.TransportControlsApi23(var1);
         } else {
            var2 = null;
         }

         return var2;
      }
   }

   static class MediaControllerImplApi24 extends MediaControllerCompat.MediaControllerImplApi23 {
      public MediaControllerImplApi24(Context var1, MediaSessionCompat.Token var2) throws RemoteException {
         super(var1, var2);
      }

      public MediaControllerImplApi24(Context var1, MediaSessionCompat var2) {
         super(var1, var2);
      }

      public MediaControllerCompat.TransportControls getTransportControls() {
         Object var1 = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
         MediaControllerCompat.TransportControlsApi24 var2;
         if (var1 != null) {
            var2 = new MediaControllerCompat.TransportControlsApi24(var1);
         } else {
            var2 = null;
         }

         return var2;
      }
   }

   static class MediaControllerImplBase implements MediaControllerCompat.MediaControllerImpl {
      private IMediaSession mBinder;
      private MediaSessionCompat.Token mToken;
      private MediaControllerCompat.TransportControls mTransportControls;

      public MediaControllerImplBase(MediaSessionCompat.Token var1) {
         this.mToken = var1;
         this.mBinder = IMediaSession.Stub.asInterface((IBinder)var1.getToken());
      }

      public void addQueueItem(MediaDescriptionCompat var1) {
         try {
            if ((4L & this.mBinder.getFlags()) == 0L) {
               UnsupportedOperationException var3 = new UnsupportedOperationException("This session doesn't support queue management operations");
               throw var3;
            }

            this.mBinder.addQueueItem(var1);
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in addQueueItem.", var2);
         }

      }

      public void addQueueItem(MediaDescriptionCompat var1, int var2) {
         try {
            if ((4L & this.mBinder.getFlags()) == 0L) {
               UnsupportedOperationException var4 = new UnsupportedOperationException("This session doesn't support queue management operations");
               throw var4;
            }

            this.mBinder.addQueueItemAt(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in addQueueItemAt.", var3);
         }

      }

      public void adjustVolume(int var1, int var2) {
         try {
            this.mBinder.adjustVolume(var1, var2, (String)null);
         } catch (RemoteException var4) {
            Log.e("MediaControllerCompat", "Dead object in adjustVolume.", var4);
         }

      }

      public boolean dispatchMediaButtonEvent(KeyEvent var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("event may not be null.");
         } else {
            try {
               this.mBinder.sendMediaButton(var1);
            } catch (RemoteException var2) {
               Log.e("MediaControllerCompat", "Dead object in dispatchMediaButtonEvent.", var2);
            }

            return false;
         }
      }

      public Bundle getExtras() {
         Bundle var1;
         try {
            var1 = this.mBinder.getExtras();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getExtras.", var2);
            var1 = null;
         }

         return var1;
      }

      public long getFlags() {
         long var1;
         try {
            var1 = this.mBinder.getFlags();
         } catch (RemoteException var4) {
            Log.e("MediaControllerCompat", "Dead object in getFlags.", var4);
            var1 = 0L;
         }

         return var1;
      }

      public Object getMediaController() {
         return null;
      }

      public MediaMetadataCompat getMetadata() {
         MediaMetadataCompat var1;
         try {
            var1 = this.mBinder.getMetadata();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getMetadata.", var2);
            var1 = null;
         }

         return var1;
      }

      public String getPackageName() {
         String var1;
         try {
            var1 = this.mBinder.getPackageName();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getPackageName.", var2);
            var1 = null;
         }

         return var1;
      }

      public MediaControllerCompat.PlaybackInfo getPlaybackInfo() {
         MediaControllerCompat.PlaybackInfo var2;
         try {
            ParcelableVolumeInfo var1 = this.mBinder.getVolumeAttributes();
            var2 = new MediaControllerCompat.PlaybackInfo(var1.volumeType, var1.audioStream, var1.controlType, var1.maxVolume, var1.currentVolume);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in getPlaybackInfo.", var3);
            var2 = null;
         }

         return var2;
      }

      public PlaybackStateCompat getPlaybackState() {
         PlaybackStateCompat var1;
         try {
            var1 = this.mBinder.getPlaybackState();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getPlaybackState.", var2);
            var1 = null;
         }

         return var1;
      }

      public List getQueue() {
         List var1;
         try {
            var1 = this.mBinder.getQueue();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getQueue.", var2);
            var1 = null;
         }

         return var1;
      }

      public CharSequence getQueueTitle() {
         CharSequence var1;
         try {
            var1 = this.mBinder.getQueueTitle();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getQueueTitle.", var2);
            var1 = null;
         }

         return var1;
      }

      public int getRatingType() {
         int var1;
         try {
            var1 = this.mBinder.getRatingType();
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in getRatingType.", var3);
            var1 = 0;
         }

         return var1;
      }

      public int getRepeatMode() {
         int var1;
         try {
            var1 = this.mBinder.getRepeatMode();
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in getRepeatMode.", var3);
            var1 = 0;
         }

         return var1;
      }

      public PendingIntent getSessionActivity() {
         PendingIntent var1;
         try {
            var1 = this.mBinder.getLaunchPendingIntent();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getSessionActivity.", var2);
            var1 = null;
         }

         return var1;
      }

      public MediaControllerCompat.TransportControls getTransportControls() {
         if (this.mTransportControls == null) {
            this.mTransportControls = new MediaControllerCompat.TransportControlsBase(this.mBinder);
         }

         return this.mTransportControls;
      }

      public boolean isShuffleModeEnabled() {
         boolean var1;
         try {
            var1 = this.mBinder.isShuffleModeEnabled();
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in isShuffleModeEnabled.", var3);
            var1 = false;
         }

         return var1;
      }

      public void registerCallback(MediaControllerCompat.Callback var1, Handler var2) {
         if (var1 == null) {
            throw new IllegalArgumentException("callback may not be null.");
         } else {
            try {
               this.mBinder.asBinder().linkToDeath(var1, 0);
               this.mBinder.registerCallbackListener((IMediaControllerCallback)var1.mCallbackObj);
               var1.setHandler(var2);
               var1.mRegistered = true;
            } catch (RemoteException var3) {
               Log.e("MediaControllerCompat", "Dead object in registerCallback.", var3);
               var1.onSessionDestroyed();
            }

         }
      }

      public void removeQueueItem(MediaDescriptionCompat var1) {
         try {
            if ((4L & this.mBinder.getFlags()) == 0L) {
               UnsupportedOperationException var3 = new UnsupportedOperationException("This session doesn't support queue management operations");
               throw var3;
            }

            this.mBinder.removeQueueItem(var1);
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in removeQueueItem.", var2);
         }

      }

      public void removeQueueItemAt(int var1) {
         try {
            if ((4L & this.mBinder.getFlags()) == 0L) {
               UnsupportedOperationException var2 = new UnsupportedOperationException("This session doesn't support queue management operations");
               throw var2;
            }

            this.mBinder.removeQueueItemAt(var1);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in removeQueueItemAt.", var3);
         }

      }

      public void sendCommand(String var1, Bundle var2, ResultReceiver var3) {
         try {
            IMediaSession var4 = this.mBinder;
            MediaSessionCompat.ResultReceiverWrapper var5 = new MediaSessionCompat.ResultReceiverWrapper(var3);
            var4.sendCommand(var1, var2, var5);
         } catch (RemoteException var6) {
            Log.e("MediaControllerCompat", "Dead object in sendCommand.", var6);
         }

      }

      public void setVolumeTo(int var1, int var2) {
         try {
            this.mBinder.setVolumeTo(var1, var2, (String)null);
         } catch (RemoteException var4) {
            Log.e("MediaControllerCompat", "Dead object in setVolumeTo.", var4);
         }

      }

      public void unregisterCallback(MediaControllerCompat.Callback var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("callback may not be null.");
         } else {
            try {
               this.mBinder.unregisterCallbackListener((IMediaControllerCallback)var1.mCallbackObj);
               this.mBinder.asBinder().unlinkToDeath(var1, 0);
               var1.mRegistered = false;
            } catch (RemoteException var2) {
               Log.e("MediaControllerCompat", "Dead object in unregisterCallback.", var2);
            }

         }
      }
   }

   public static final class PlaybackInfo {
      public static final int PLAYBACK_TYPE_LOCAL = 1;
      public static final int PLAYBACK_TYPE_REMOTE = 2;
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

      public int getAudioStream() {
         return this.mAudioStream;
      }

      public int getCurrentVolume() {
         return this.mCurrentVolume;
      }

      public int getMaxVolume() {
         return this.mMaxVolume;
      }

      public int getPlaybackType() {
         return this.mPlaybackType;
      }

      public int getVolumeControl() {
         return this.mVolumeControl;
      }
   }

   public abstract static class TransportControls {
      TransportControls() {
      }

      public abstract void fastForward();

      public abstract void pause();

      public abstract void play();

      public abstract void playFromMediaId(String var1, Bundle var2);

      public abstract void playFromSearch(String var1, Bundle var2);

      public abstract void playFromUri(Uri var1, Bundle var2);

      public abstract void prepare();

      public abstract void prepareFromMediaId(String var1, Bundle var2);

      public abstract void prepareFromSearch(String var1, Bundle var2);

      public abstract void prepareFromUri(Uri var1, Bundle var2);

      public abstract void rewind();

      public abstract void seekTo(long var1);

      public abstract void sendCustomAction(PlaybackStateCompat.CustomAction var1, Bundle var2);

      public abstract void sendCustomAction(String var1, Bundle var2);

      public abstract void setRating(RatingCompat var1);

      public abstract void setRepeatMode(int var1);

      public abstract void setShuffleModeEnabled(boolean var1);

      public abstract void skipToNext();

      public abstract void skipToPrevious();

      public abstract void skipToQueueItem(long var1);

      public abstract void stop();
   }

   static class TransportControlsApi21 extends MediaControllerCompat.TransportControls {
      protected final Object mControlsObj;

      public TransportControlsApi21(Object var1) {
         this.mControlsObj = var1;
      }

      public void fastForward() {
         MediaControllerCompatApi21.TransportControls.fastForward(this.mControlsObj);
      }

      public void pause() {
         MediaControllerCompatApi21.TransportControls.pause(this.mControlsObj);
      }

      public void play() {
         MediaControllerCompatApi21.TransportControls.play(this.mControlsObj);
      }

      public void playFromMediaId(String var1, Bundle var2) {
         MediaControllerCompatApi21.TransportControls.playFromMediaId(this.mControlsObj, var1, var2);
      }

      public void playFromSearch(String var1, Bundle var2) {
         MediaControllerCompatApi21.TransportControls.playFromSearch(this.mControlsObj, var1, var2);
      }

      public void playFromUri(Uri var1, Bundle var2) {
         if (var1 != null && !Uri.EMPTY.equals(var1)) {
            Bundle var3 = new Bundle();
            var3.putParcelable("android.support.v4.media.session.action.ARGUMENT_URI", var1);
            var3.putParcelable("android.support.v4.media.session.action.ARGUMENT_EXTRAS", var2);
            this.sendCustomAction("android.support.v4.media.session.action.PLAY_FROM_URI", var3);
         } else {
            throw new IllegalArgumentException("You must specify a non-empty Uri for playFromUri.");
         }
      }

      public void prepare() {
         this.sendCustomAction((String)"android.support.v4.media.session.action.PREPARE", (Bundle)null);
      }

      public void prepareFromMediaId(String var1, Bundle var2) {
         Bundle var3 = new Bundle();
         var3.putString("android.support.v4.media.session.action.ARGUMENT_MEDIA_ID", var1);
         var3.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", var2);
         this.sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID", var3);
      }

      public void prepareFromSearch(String var1, Bundle var2) {
         Bundle var3 = new Bundle();
         var3.putString("android.support.v4.media.session.action.ARGUMENT_QUERY", var1);
         var3.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", var2);
         this.sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_SEARCH", var3);
      }

      public void prepareFromUri(Uri var1, Bundle var2) {
         Bundle var3 = new Bundle();
         var3.putParcelable("android.support.v4.media.session.action.ARGUMENT_URI", var1);
         var3.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", var2);
         this.sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_URI", var3);
      }

      public void rewind() {
         MediaControllerCompatApi21.TransportControls.rewind(this.mControlsObj);
      }

      public void seekTo(long var1) {
         MediaControllerCompatApi21.TransportControls.seekTo(this.mControlsObj, var1);
      }

      public void sendCustomAction(PlaybackStateCompat.CustomAction var1, Bundle var2) {
         MediaControllerCompatApi21.TransportControls.sendCustomAction(this.mControlsObj, var1.getAction(), var2);
      }

      public void sendCustomAction(String var1, Bundle var2) {
         MediaControllerCompatApi21.TransportControls.sendCustomAction(this.mControlsObj, var1, var2);
      }

      public void setRating(RatingCompat var1) {
         Object var2 = this.mControlsObj;
         Object var3;
         if (var1 != null) {
            var3 = var1.getRating();
         } else {
            var3 = null;
         }

         MediaControllerCompatApi21.TransportControls.setRating(var2, var3);
      }

      public void setRepeatMode(int var1) {
         Bundle var2 = new Bundle();
         var2.putInt("android.support.v4.media.session.action.ARGUMENT_REPEAT_MODE", var1);
         this.sendCustomAction("android.support.v4.media.session.action.SET_REPEAT_MODE", var2);
      }

      public void setShuffleModeEnabled(boolean var1) {
         Bundle var2 = new Bundle();
         var2.putBoolean("android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE_ENABLED", var1);
         this.sendCustomAction("android.support.v4.media.session.action.SET_SHUFFLE_MODE_ENABLED", var2);
      }

      public void skipToNext() {
         MediaControllerCompatApi21.TransportControls.skipToNext(this.mControlsObj);
      }

      public void skipToPrevious() {
         MediaControllerCompatApi21.TransportControls.skipToPrevious(this.mControlsObj);
      }

      public void skipToQueueItem(long var1) {
         MediaControllerCompatApi21.TransportControls.skipToQueueItem(this.mControlsObj, var1);
      }

      public void stop() {
         MediaControllerCompatApi21.TransportControls.stop(this.mControlsObj);
      }
   }

   static class TransportControlsApi23 extends MediaControllerCompat.TransportControlsApi21 {
      public TransportControlsApi23(Object var1) {
         super(var1);
      }

      public void playFromUri(Uri var1, Bundle var2) {
         MediaControllerCompatApi23.TransportControls.playFromUri(this.mControlsObj, var1, var2);
      }
   }

   static class TransportControlsApi24 extends MediaControllerCompat.TransportControlsApi23 {
      public TransportControlsApi24(Object var1) {
         super(var1);
      }

      public void prepare() {
         MediaControllerCompatApi24.TransportControls.prepare(this.mControlsObj);
      }

      public void prepareFromMediaId(String var1, Bundle var2) {
         MediaControllerCompatApi24.TransportControls.prepareFromMediaId(this.mControlsObj, var1, var2);
      }

      public void prepareFromSearch(String var1, Bundle var2) {
         MediaControllerCompatApi24.TransportControls.prepareFromSearch(this.mControlsObj, var1, var2);
      }

      public void prepareFromUri(Uri var1, Bundle var2) {
         MediaControllerCompatApi24.TransportControls.prepareFromUri(this.mControlsObj, var1, var2);
      }
   }

   static class TransportControlsBase extends MediaControllerCompat.TransportControls {
      private IMediaSession mBinder;

      public TransportControlsBase(IMediaSession var1) {
         this.mBinder = var1;
      }

      public void fastForward() {
         try {
            this.mBinder.fastForward();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in fastForward.", var2);
         }

      }

      public void pause() {
         try {
            this.mBinder.pause();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in pause.", var2);
         }

      }

      public void play() {
         try {
            this.mBinder.play();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in play.", var2);
         }

      }

      public void playFromMediaId(String var1, Bundle var2) {
         try {
            this.mBinder.playFromMediaId(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in playFromMediaId.", var3);
         }

      }

      public void playFromSearch(String var1, Bundle var2) {
         try {
            this.mBinder.playFromSearch(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in playFromSearch.", var3);
         }

      }

      public void playFromUri(Uri var1, Bundle var2) {
         try {
            this.mBinder.playFromUri(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in playFromUri.", var3);
         }

      }

      public void prepare() {
         try {
            this.mBinder.prepare();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in prepare.", var2);
         }

      }

      public void prepareFromMediaId(String var1, Bundle var2) {
         try {
            this.mBinder.prepareFromMediaId(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in prepareFromMediaId.", var3);
         }

      }

      public void prepareFromSearch(String var1, Bundle var2) {
         try {
            this.mBinder.prepareFromSearch(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in prepareFromSearch.", var3);
         }

      }

      public void prepareFromUri(Uri var1, Bundle var2) {
         try {
            this.mBinder.prepareFromUri(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in prepareFromUri.", var3);
         }

      }

      public void rewind() {
         try {
            this.mBinder.rewind();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in rewind.", var2);
         }

      }

      public void seekTo(long var1) {
         try {
            this.mBinder.seekTo(var1);
         } catch (RemoteException var4) {
            Log.e("MediaControllerCompat", "Dead object in seekTo.", var4);
         }

      }

      public void sendCustomAction(PlaybackStateCompat.CustomAction var1, Bundle var2) {
         this.sendCustomAction(var1.getAction(), var2);
      }

      public void sendCustomAction(String var1, Bundle var2) {
         try {
            this.mBinder.sendCustomAction(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in sendCustomAction.", var3);
         }

      }

      public void setRating(RatingCompat var1) {
         try {
            this.mBinder.rate(var1);
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in setRating.", var2);
         }

      }

      public void setRepeatMode(int var1) {
         try {
            this.mBinder.setRepeatMode(var1);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in setRepeatMode.", var3);
         }

      }

      public void setShuffleModeEnabled(boolean var1) {
         try {
            this.mBinder.setShuffleModeEnabled(var1);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in setShuffleModeEnabled.", var3);
         }

      }

      public void skipToNext() {
         try {
            this.mBinder.next();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in skipToNext.", var2);
         }

      }

      public void skipToPrevious() {
         try {
            this.mBinder.previous();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in skipToPrevious.", var2);
         }

      }

      public void skipToQueueItem(long var1) {
         try {
            this.mBinder.skipToQueueItem(var1);
         } catch (RemoteException var4) {
            Log.e("MediaControllerCompat", "Dead object in skipToQueueItem.", var4);
         }

      }

      public void stop() {
         try {
            this.mBinder.stop();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in stop.", var2);
         }

      }
   }
}
