package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.Rating;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;
import android.media.RemoteControlClient.OnMetadataUpdateListener;
import android.media.RemoteControlClient.OnPlaybackPositionUpdateListener;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.VolumeProviderCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MediaSessionCompat {
   static final String ACTION_ARGUMENT_CAPTIONING_ENABLED = "android.support.v4.media.session.action.ARGUMENT_CAPTIONING_ENABLED";
   static final String ACTION_ARGUMENT_EXTRAS = "android.support.v4.media.session.action.ARGUMENT_EXTRAS";
   static final String ACTION_ARGUMENT_MEDIA_ID = "android.support.v4.media.session.action.ARGUMENT_MEDIA_ID";
   static final String ACTION_ARGUMENT_QUERY = "android.support.v4.media.session.action.ARGUMENT_QUERY";
   static final String ACTION_ARGUMENT_RATING = "android.support.v4.media.session.action.ARGUMENT_RATING";
   static final String ACTION_ARGUMENT_REPEAT_MODE = "android.support.v4.media.session.action.ARGUMENT_REPEAT_MODE";
   static final String ACTION_ARGUMENT_SHUFFLE_MODE = "android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE";
   static final String ACTION_ARGUMENT_SHUFFLE_MODE_ENABLED = "android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE_ENABLED";
   static final String ACTION_ARGUMENT_URI = "android.support.v4.media.session.action.ARGUMENT_URI";
   public static final String ACTION_FLAG_AS_INAPPROPRIATE = "android.support.v4.media.session.action.FLAG_AS_INAPPROPRIATE";
   public static final String ACTION_FOLLOW = "android.support.v4.media.session.action.FOLLOW";
   static final String ACTION_PLAY_FROM_URI = "android.support.v4.media.session.action.PLAY_FROM_URI";
   static final String ACTION_PREPARE = "android.support.v4.media.session.action.PREPARE";
   static final String ACTION_PREPARE_FROM_MEDIA_ID = "android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID";
   static final String ACTION_PREPARE_FROM_SEARCH = "android.support.v4.media.session.action.PREPARE_FROM_SEARCH";
   static final String ACTION_PREPARE_FROM_URI = "android.support.v4.media.session.action.PREPARE_FROM_URI";
   static final String ACTION_SET_CAPTIONING_ENABLED = "android.support.v4.media.session.action.SET_CAPTIONING_ENABLED";
   static final String ACTION_SET_RATING = "android.support.v4.media.session.action.SET_RATING";
   static final String ACTION_SET_REPEAT_MODE = "android.support.v4.media.session.action.SET_REPEAT_MODE";
   static final String ACTION_SET_SHUFFLE_MODE = "android.support.v4.media.session.action.SET_SHUFFLE_MODE";
   static final String ACTION_SET_SHUFFLE_MODE_ENABLED = "android.support.v4.media.session.action.SET_SHUFFLE_MODE_ENABLED";
   public static final String ACTION_SKIP_AD = "android.support.v4.media.session.action.SKIP_AD";
   public static final String ACTION_UNFOLLOW = "android.support.v4.media.session.action.UNFOLLOW";
   public static final String ARGUMENT_MEDIA_ATTRIBUTE = "android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE";
   public static final String ARGUMENT_MEDIA_ATTRIBUTE_VALUE = "android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE_VALUE";
   static final String EXTRA_BINDER = "android.support.v4.media.session.EXTRA_BINDER";
   public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
   public static final int FLAG_HANDLES_QUEUE_COMMANDS = 4;
   public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
   private static final int MAX_BITMAP_SIZE_IN_DP = 320;
   public static final int MEDIA_ATTRIBUTE_ALBUM = 1;
   public static final int MEDIA_ATTRIBUTE_ARTIST = 0;
   public static final int MEDIA_ATTRIBUTE_PLAYLIST = 2;
   static final String TAG = "MediaSessionCompat";
   static int sMaxBitmapSize;
   private final ArrayList mActiveListeners;
   private final MediaControllerCompat mController;
   private final MediaSessionCompat.MediaSessionImpl mImpl;

   private MediaSessionCompat(Context var1, MediaSessionCompat.MediaSessionImpl var2) {
      this.mActiveListeners = new ArrayList();
      this.mImpl = var2;
      if (VERSION.SDK_INT >= 21 && !MediaSessionCompatApi21.hasCallback(var2.getMediaSession())) {
         this.setCallback(new MediaSessionCompat.Callback() {
         });
      }

      this.mController = new MediaControllerCompat(var1, this);
   }

   public MediaSessionCompat(Context var1, String var2) {
      this(var1, var2, (ComponentName)null, (PendingIntent)null);
   }

   public MediaSessionCompat(Context var1, String var2, ComponentName var3, PendingIntent var4) {
      this.mActiveListeners = new ArrayList();
      if (var1 == null) {
         throw new IllegalArgumentException("context must not be null");
      } else if (TextUtils.isEmpty(var2)) {
         throw new IllegalArgumentException("tag must not be null or empty");
      } else {
         ComponentName var5 = var3;
         if (var3 == null) {
            var3 = MediaButtonReceiver.getMediaButtonReceiverComponent(var1);
            var5 = var3;
            if (var3 == null) {
               Log.w("MediaSessionCompat", "Couldn't find a unique registered media button receiver in the given context.");
               var5 = var3;
            }
         }

         PendingIntent var6 = var4;
         if (var5 != null) {
            var6 = var4;
            if (var4 == null) {
               Intent var7 = new Intent("android.intent.action.MEDIA_BUTTON");
               var7.setComponent(var5);
               var6 = PendingIntent.getBroadcast(var1, 0, var7, 0);
            }
         }

         if (VERSION.SDK_INT >= 21) {
            this.mImpl = new MediaSessionCompat.MediaSessionImplApi21(var1, var2);
            this.setCallback(new MediaSessionCompat.Callback() {
            });
            this.mImpl.setMediaButtonReceiver(var6);
         } else if (VERSION.SDK_INT >= 19) {
            this.mImpl = new MediaSessionCompat.MediaSessionImplApi19(var1, var2, var5, var6);
         } else if (VERSION.SDK_INT >= 18) {
            this.mImpl = new MediaSessionCompat.MediaSessionImplApi18(var1, var2, var5, var6);
         } else {
            this.mImpl = new MediaSessionCompat.MediaSessionImplBase(var1, var2, var5, var6);
         }

         this.mController = new MediaControllerCompat(var1, this);
         if (sMaxBitmapSize == 0) {
            sMaxBitmapSize = (int)TypedValue.applyDimension(1, 320.0F, var1.getResources().getDisplayMetrics());
         }

      }
   }

   public static MediaSessionCompat fromMediaSession(Context var0, Object var1) {
      return var0 != null && var1 != null && VERSION.SDK_INT >= 21 ? new MediaSessionCompat(var0, new MediaSessionCompat.MediaSessionImplApi21(var1)) : null;
   }

   private static PlaybackStateCompat getStateWithUpdatedPosition(PlaybackStateCompat var0, MediaMetadataCompat var1) {
      if (var0 != null) {
         long var2 = var0.getPosition();
         long var4 = -1L;
         if (var2 != -1L) {
            if (var0.getState() == 3 || var0.getState() == 4 || var0.getState() == 5) {
               var2 = var0.getLastPositionUpdateTime();
               if (var2 > 0L) {
                  long var6 = SystemClock.elapsedRealtime();
                  long var8 = (long)(var0.getPlaybackSpeed() * (float)(var6 - var2)) + var0.getPosition();
                  var2 = var4;
                  if (var1 != null) {
                     var2 = var4;
                     if (var1.containsKey("android.media.metadata.DURATION")) {
                        var2 = var1.getLong("android.media.metadata.DURATION");
                     }
                  }

                  if (var2 < 0L || var8 <= var2) {
                     if (var8 < 0L) {
                        var2 = 0L;
                     } else {
                        var2 = var8;
                     }
                  }

                  return (new PlaybackStateCompat.Builder(var0)).setState(var0.getState(), var2, var0.getPlaybackSpeed(), var6).build();
               }
            }

            return var0;
         }
      }

      return var0;
   }

   public void addOnActiveChangeListener(MediaSessionCompat.OnActiveChangeListener var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Listener may not be null");
      } else {
         this.mActiveListeners.add(var1);
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public String getCallingPackage() {
      return this.mImpl.getCallingPackage();
   }

   public MediaControllerCompat getController() {
      return this.mController;
   }

   public Object getMediaSession() {
      return this.mImpl.getMediaSession();
   }

   public Object getRemoteControlClient() {
      return this.mImpl.getRemoteControlClient();
   }

   public MediaSessionCompat.Token getSessionToken() {
      return this.mImpl.getSessionToken();
   }

   public boolean isActive() {
      return this.mImpl.isActive();
   }

   public void release() {
      this.mImpl.release();
   }

   public void removeOnActiveChangeListener(MediaSessionCompat.OnActiveChangeListener var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Listener may not be null");
      } else {
         this.mActiveListeners.remove(var1);
      }
   }

   public void sendSessionEvent(String var1, Bundle var2) {
      if (TextUtils.isEmpty(var1)) {
         throw new IllegalArgumentException("event cannot be null or empty");
      } else {
         this.mImpl.sendSessionEvent(var1, var2);
      }
   }

   public void setActive(boolean var1) {
      this.mImpl.setActive(var1);
      Iterator var2 = this.mActiveListeners.iterator();

      while(var2.hasNext()) {
         ((MediaSessionCompat.OnActiveChangeListener)var2.next()).onActiveChanged();
      }

   }

   public void setCallback(MediaSessionCompat.Callback var1) {
      this.setCallback(var1, (Handler)null);
   }

   public void setCallback(MediaSessionCompat.Callback var1, Handler var2) {
      MediaSessionCompat.MediaSessionImpl var3 = this.mImpl;
      if (var2 == null) {
         var2 = new Handler();
      }

      var3.setCallback(var1, var2);
   }

   public void setCaptioningEnabled(boolean var1) {
      this.mImpl.setCaptioningEnabled(var1);
   }

   public void setExtras(Bundle var1) {
      this.mImpl.setExtras(var1);
   }

   public void setFlags(int var1) {
      this.mImpl.setFlags(var1);
   }

   public void setMediaButtonReceiver(PendingIntent var1) {
      this.mImpl.setMediaButtonReceiver(var1);
   }

   public void setMetadata(MediaMetadataCompat var1) {
      this.mImpl.setMetadata(var1);
   }

   public void setPlaybackState(PlaybackStateCompat var1) {
      this.mImpl.setPlaybackState(var1);
   }

   public void setPlaybackToLocal(int var1) {
      this.mImpl.setPlaybackToLocal(var1);
   }

   public void setPlaybackToRemote(VolumeProviderCompat var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("volumeProvider may not be null!");
      } else {
         this.mImpl.setPlaybackToRemote(var1);
      }
   }

   public void setQueue(List var1) {
      this.mImpl.setQueue(var1);
   }

   public void setQueueTitle(CharSequence var1) {
      this.mImpl.setQueueTitle(var1);
   }

   public void setRatingType(int var1) {
      this.mImpl.setRatingType(var1);
   }

   public void setRepeatMode(int var1) {
      this.mImpl.setRepeatMode(var1);
   }

   public void setSessionActivity(PendingIntent var1) {
      this.mImpl.setSessionActivity(var1);
   }

   public void setShuffleMode(int var1) {
      this.mImpl.setShuffleMode(var1);
   }

   @Deprecated
   public void setShuffleModeEnabled(boolean var1) {
      this.mImpl.setShuffleModeEnabled(var1);
   }

   public abstract static class Callback {
      private MediaSessionCompat.Callback.CallbackHandler mCallbackHandler = null;
      final Object mCallbackObj;
      private boolean mMediaPlayPauseKeyPending;
      private WeakReference mSessionImpl;

      public Callback() {
         if (VERSION.SDK_INT >= 24) {
            this.mCallbackObj = MediaSessionCompatApi24.createCallback(new MediaSessionCompat.Callback.StubApi24());
         } else if (VERSION.SDK_INT >= 23) {
            this.mCallbackObj = MediaSessionCompatApi23.createCallback(new MediaSessionCompat.Callback.StubApi23());
         } else if (VERSION.SDK_INT >= 21) {
            this.mCallbackObj = MediaSessionCompatApi21.createCallback(new MediaSessionCompat.Callback.StubApi21());
         } else {
            this.mCallbackObj = null;
         }

      }

      private void handleMediaPlayPauseKeySingleTapIfPending() {
         if (this.mMediaPlayPauseKeyPending) {
            boolean var1 = false;
            this.mMediaPlayPauseKeyPending = false;
            this.mCallbackHandler.removeMessages(1);
            MediaSessionCompat.MediaSessionImpl var2 = (MediaSessionCompat.MediaSessionImpl)this.mSessionImpl.get();
            if (var2 != null) {
               PlaybackStateCompat var7 = var2.getPlaybackState();
               long var3;
               if (var7 == null) {
                  var3 = 0L;
               } else {
                  var3 = var7.getActions();
               }

               boolean var5;
               if (var7 != null && var7.getState() == 3) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               boolean var6;
               if ((var3 & 516L) != 0L) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               if ((var3 & 514L) != 0L) {
                  var1 = true;
               }

               if (var5 && var1) {
                  this.onPause();
               } else if (!var5 && var6) {
                  this.onPlay();
               }

            }
         }
      }

      private void setSessionImpl(MediaSessionCompat.MediaSessionImpl var1, Handler var2) {
         this.mSessionImpl = new WeakReference(var1);
         if (this.mCallbackHandler != null) {
            this.mCallbackHandler.removeCallbacksAndMessages((Object)null);
         }

         this.mCallbackHandler = new MediaSessionCompat.Callback.CallbackHandler(var2.getLooper());
      }

      public void onAddQueueItem(MediaDescriptionCompat var1) {
      }

      public void onAddQueueItem(MediaDescriptionCompat var1, int var2) {
      }

      public void onCommand(String var1, Bundle var2, ResultReceiver var3) {
      }

      public void onCustomAction(String var1, Bundle var2) {
      }

      public void onFastForward() {
      }

      public boolean onMediaButtonEvent(Intent var1) {
         MediaSessionCompat.MediaSessionImpl var2 = (MediaSessionCompat.MediaSessionImpl)this.mSessionImpl.get();
         if (var2 != null && this.mCallbackHandler != null) {
            KeyEvent var6 = (KeyEvent)var1.getParcelableExtra("android.intent.extra.KEY_EVENT");
            if (var6 != null && var6.getAction() == 0) {
               int var3 = var6.getKeyCode();
               if (var3 != 79 && var3 != 85) {
                  this.handleMediaPlayPauseKeySingleTapIfPending();
                  return false;
               } else {
                  if (var6.getRepeatCount() > 0) {
                     this.handleMediaPlayPauseKeySingleTapIfPending();
                  } else if (this.mMediaPlayPauseKeyPending) {
                     this.mCallbackHandler.removeMessages(1);
                     this.mMediaPlayPauseKeyPending = false;
                     PlaybackStateCompat var7 = var2.getPlaybackState();
                     long var4;
                     if (var7 == null) {
                        var4 = 0L;
                     } else {
                        var4 = var7.getActions();
                     }

                     if ((var4 & 32L) != 0L) {
                        this.onSkipToNext();
                     }
                  } else {
                     this.mMediaPlayPauseKeyPending = true;
                     this.mCallbackHandler.sendEmptyMessageDelayed(1, (long)ViewConfiguration.getDoubleTapTimeout());
                  }

                  return true;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      public void onPause() {
      }

      public void onPlay() {
      }

      public void onPlayFromMediaId(String var1, Bundle var2) {
      }

      public void onPlayFromSearch(String var1, Bundle var2) {
      }

      public void onPlayFromUri(Uri var1, Bundle var2) {
      }

      public void onPrepare() {
      }

      public void onPrepareFromMediaId(String var1, Bundle var2) {
      }

      public void onPrepareFromSearch(String var1, Bundle var2) {
      }

      public void onPrepareFromUri(Uri var1, Bundle var2) {
      }

      public void onRemoveQueueItem(MediaDescriptionCompat var1) {
      }

      @Deprecated
      public void onRemoveQueueItemAt(int var1) {
      }

      public void onRewind() {
      }

      public void onSeekTo(long var1) {
      }

      public void onSetCaptioningEnabled(boolean var1) {
      }

      public void onSetRating(RatingCompat var1) {
      }

      public void onSetRating(RatingCompat var1, Bundle var2) {
      }

      public void onSetRepeatMode(int var1) {
      }

      public void onSetShuffleMode(int var1) {
      }

      @Deprecated
      public void onSetShuffleModeEnabled(boolean var1) {
      }

      public void onSkipToNext() {
      }

      public void onSkipToPrevious() {
      }

      public void onSkipToQueueItem(long var1) {
      }

      public void onStop() {
      }

      private class CallbackHandler extends Handler {
         private static final int MSG_MEDIA_PLAY_PAUSE_KEY_DOUBLE_TAP_TIMEOUT = 1;

         CallbackHandler(Looper var2) {
            super(var2);
         }

         public void handleMessage(Message var1) {
            if (var1.what == 1) {
               Callback.this.handleMediaPlayPauseKeySingleTapIfPending();
            }

         }
      }

      @RequiresApi(21)
      private class StubApi21 implements MediaSessionCompatApi21.Callback {
         StubApi21() {
         }

         public void onCommand(String var1, Bundle var2, ResultReceiver var3) {
            label128: {
               boolean var4;
               boolean var10001;
               try {
                  var4 = var1.equals("android.support.v4.media.session.command.GET_EXTRA_BINDER");
               } catch (BadParcelableException var19) {
                  var10001 = false;
                  break label128;
               }

               Object var5 = null;
               Object var6 = null;
               if (var4) {
                  label129: {
                     MediaSessionCompat.MediaSessionImplApi21 var22;
                     try {
                        var22 = (MediaSessionCompat.MediaSessionImplApi21)Callback.this.mSessionImpl.get();
                     } catch (BadParcelableException var11) {
                        var10001 = false;
                        break label129;
                     }

                     if (var22 == null) {
                        return;
                     }

                     IMediaSession var23;
                     try {
                        var2 = new Bundle();
                        var23 = var22.getSessionToken().getExtraBinder();
                     } catch (BadParcelableException var10) {
                        var10001 = false;
                        break label129;
                     }

                     IBinder var24;
                     if (var23 == null) {
                        var24 = (IBinder)var6;
                     } else {
                        try {
                           var24 = var23.asBinder();
                        } catch (BadParcelableException var9) {
                           var10001 = false;
                           break label129;
                        }
                     }

                     try {
                        BundleCompat.putBinder(var2, "android.support.v4.media.session.EXTRA_BINDER", var24);
                        var3.send(0, var2);
                        return;
                     } catch (BadParcelableException var8) {
                        var10001 = false;
                     }
                  }
               } else {
                  label130: {
                     try {
                        if (var1.equals("android.support.v4.media.session.command.ADD_QUEUE_ITEM")) {
                           var2.setClassLoader(MediaDescriptionCompat.class.getClassLoader());
                           Callback.this.onAddQueueItem((MediaDescriptionCompat)var2.getParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION"));
                           return;
                        }
                     } catch (BadParcelableException var18) {
                        var10001 = false;
                        break label130;
                     }

                     try {
                        if (var1.equals("android.support.v4.media.session.command.ADD_QUEUE_ITEM_AT")) {
                           var2.setClassLoader(MediaDescriptionCompat.class.getClassLoader());
                           Callback.this.onAddQueueItem((MediaDescriptionCompat)var2.getParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION"), var2.getInt("android.support.v4.media.session.command.ARGUMENT_INDEX"));
                           return;
                        }
                     } catch (BadParcelableException var20) {
                        var10001 = false;
                        break label130;
                     }

                     try {
                        if (var1.equals("android.support.v4.media.session.command.REMOVE_QUEUE_ITEM")) {
                           var2.setClassLoader(MediaDescriptionCompat.class.getClassLoader());
                           Callback.this.onRemoveQueueItem((MediaDescriptionCompat)var2.getParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION"));
                           return;
                        }
                     } catch (BadParcelableException var17) {
                        var10001 = false;
                        break label130;
                     }

                     MediaSessionCompat.MediaSessionImplApi21 var25;
                     label108: {
                        try {
                           if (var1.equals("android.support.v4.media.session.command.REMOVE_QUEUE_ITEM_AT")) {
                              var25 = (MediaSessionCompat.MediaSessionImplApi21)Callback.this.mSessionImpl.get();
                              break label108;
                           }
                        } catch (BadParcelableException var16) {
                           var10001 = false;
                           break label130;
                        }

                        try {
                           Callback.this.onCommand(var1, var2, var3);
                           return;
                        } catch (BadParcelableException var15) {
                           var10001 = false;
                           break label130;
                        }
                     }

                     if (var25 == null) {
                        return;
                     }

                     int var7;
                     try {
                        if (var25.mQueue == null) {
                           return;
                        }

                        var7 = var2.getInt("android.support.v4.media.session.command.ARGUMENT_INDEX", -1);
                     } catch (BadParcelableException var14) {
                        var10001 = false;
                        break label130;
                     }

                     MediaSessionCompat.QueueItem var21 = (MediaSessionCompat.QueueItem)var5;
                     if (var7 >= 0) {
                        var21 = (MediaSessionCompat.QueueItem)var5;

                        try {
                           if (var7 < var25.mQueue.size()) {
                              var21 = (MediaSessionCompat.QueueItem)var25.mQueue.get(var7);
                           }
                        } catch (BadParcelableException var13) {
                           var10001 = false;
                           break label130;
                        }
                     }

                     if (var21 == null) {
                        return;
                     }

                     try {
                        Callback.this.onRemoveQueueItem(var21.getDescription());
                        return;
                     } catch (BadParcelableException var12) {
                        var10001 = false;
                     }
                  }
               }
            }

            Log.e("MediaSessionCompat", "Could not unparcel the extra data.");
         }

         public void onCustomAction(String var1, Bundle var2) {
            Uri var5;
            if (var1.equals("android.support.v4.media.session.action.PLAY_FROM_URI")) {
               var5 = (Uri)var2.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI");
               var2 = (Bundle)var2.getParcelable("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
               Callback.this.onPlayFromUri(var5, var2);
            } else if (var1.equals("android.support.v4.media.session.action.PREPARE")) {
               Callback.this.onPrepare();
            } else if (var1.equals("android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID")) {
               var1 = var2.getString("android.support.v4.media.session.action.ARGUMENT_MEDIA_ID");
               var2 = var2.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
               Callback.this.onPrepareFromMediaId(var1, var2);
            } else if (var1.equals("android.support.v4.media.session.action.PREPARE_FROM_SEARCH")) {
               var1 = var2.getString("android.support.v4.media.session.action.ARGUMENT_QUERY");
               var2 = var2.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
               Callback.this.onPrepareFromSearch(var1, var2);
            } else if (var1.equals("android.support.v4.media.session.action.PREPARE_FROM_URI")) {
               var5 = (Uri)var2.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI");
               var2 = var2.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
               Callback.this.onPrepareFromUri(var5, var2);
            } else {
               boolean var3;
               if (var1.equals("android.support.v4.media.session.action.SET_CAPTIONING_ENABLED")) {
                  var3 = var2.getBoolean("android.support.v4.media.session.action.ARGUMENT_CAPTIONING_ENABLED");
                  Callback.this.onSetCaptioningEnabled(var3);
               } else {
                  int var4;
                  if (var1.equals("android.support.v4.media.session.action.SET_REPEAT_MODE")) {
                     var4 = var2.getInt("android.support.v4.media.session.action.ARGUMENT_REPEAT_MODE");
                     Callback.this.onSetRepeatMode(var4);
                  } else if (var1.equals("android.support.v4.media.session.action.SET_SHUFFLE_MODE_ENABLED")) {
                     var3 = var2.getBoolean("android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE_ENABLED");
                     Callback.this.onSetShuffleModeEnabled(var3);
                  } else if (var1.equals("android.support.v4.media.session.action.SET_SHUFFLE_MODE")) {
                     var4 = var2.getInt("android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE");
                     Callback.this.onSetShuffleMode(var4);
                  } else if (var1.equals("android.support.v4.media.session.action.SET_RATING")) {
                     var2.setClassLoader(RatingCompat.class.getClassLoader());
                     RatingCompat var6 = (RatingCompat)var2.getParcelable("android.support.v4.media.session.action.ARGUMENT_RATING");
                     var2 = var2.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
                     Callback.this.onSetRating(var6, var2);
                  } else {
                     Callback.this.onCustomAction(var1, var2);
                  }
               }
            }

         }

         public void onFastForward() {
            Callback.this.onFastForward();
         }

         public boolean onMediaButtonEvent(Intent var1) {
            return Callback.this.onMediaButtonEvent(var1);
         }

         public void onPause() {
            Callback.this.onPause();
         }

         public void onPlay() {
            Callback.this.onPlay();
         }

         public void onPlayFromMediaId(String var1, Bundle var2) {
            Callback.this.onPlayFromMediaId(var1, var2);
         }

         public void onPlayFromSearch(String var1, Bundle var2) {
            Callback.this.onPlayFromSearch(var1, var2);
         }

         public void onRewind() {
            Callback.this.onRewind();
         }

         public void onSeekTo(long var1) {
            Callback.this.onSeekTo(var1);
         }

         public void onSetRating(Object var1) {
            Callback.this.onSetRating(RatingCompat.fromRating(var1));
         }

         public void onSetRating(Object var1, Bundle var2) {
            Callback.this.onSetRating(RatingCompat.fromRating(var1), var2);
         }

         public void onSkipToNext() {
            Callback.this.onSkipToNext();
         }

         public void onSkipToPrevious() {
            Callback.this.onSkipToPrevious();
         }

         public void onSkipToQueueItem(long var1) {
            Callback.this.onSkipToQueueItem(var1);
         }

         public void onStop() {
            Callback.this.onStop();
         }
      }

      @RequiresApi(23)
      private class StubApi23 extends MediaSessionCompat.Callback.StubApi21 implements MediaSessionCompatApi23.Callback {
         StubApi23() {
            super();
         }

         public void onPlayFromUri(Uri var1, Bundle var2) {
            Callback.this.onPlayFromUri(var1, var2);
         }
      }

      @RequiresApi(24)
      private class StubApi24 extends MediaSessionCompat.Callback.StubApi23 implements MediaSessionCompatApi24.Callback {
         StubApi24() {
            super();
         }

         public void onPrepare() {
            Callback.this.onPrepare();
         }

         public void onPrepareFromMediaId(String var1, Bundle var2) {
            Callback.this.onPrepareFromMediaId(var1, var2);
         }

         public void onPrepareFromSearch(String var1, Bundle var2) {
            Callback.this.onPrepareFromSearch(var1, var2);
         }

         public void onPrepareFromUri(Uri var1, Bundle var2) {
            Callback.this.onPrepareFromUri(var1, var2);
         }
      }
   }

   interface MediaSessionImpl {
      String getCallingPackage();

      Object getMediaSession();

      PlaybackStateCompat getPlaybackState();

      Object getRemoteControlClient();

      MediaSessionCompat.Token getSessionToken();

      boolean isActive();

      void release();

      void sendSessionEvent(String var1, Bundle var2);

      void setActive(boolean var1);

      void setCallback(MediaSessionCompat.Callback var1, Handler var2);

      void setCaptioningEnabled(boolean var1);

      void setExtras(Bundle var1);

      void setFlags(int var1);

      void setMediaButtonReceiver(PendingIntent var1);

      void setMetadata(MediaMetadataCompat var1);

      void setPlaybackState(PlaybackStateCompat var1);

      void setPlaybackToLocal(int var1);

      void setPlaybackToRemote(VolumeProviderCompat var1);

      void setQueue(List var1);

      void setQueueTitle(CharSequence var1);

      void setRatingType(int var1);

      void setRepeatMode(int var1);

      void setSessionActivity(PendingIntent var1);

      void setShuffleMode(int var1);

      void setShuffleModeEnabled(boolean var1);
   }

   @RequiresApi(18)
   static class MediaSessionImplApi18 extends MediaSessionCompat.MediaSessionImplBase {
      private static boolean sIsMbrPendingIntentSupported;

      MediaSessionImplApi18(Context var1, String var2, ComponentName var3, PendingIntent var4) {
         super(var1, var2, var3, var4);
      }

      int getRccTransportControlFlagsFromActions(long var1) {
         int var3 = super.getRccTransportControlFlagsFromActions(var1);
         int var4 = var3;
         if ((var1 & 256L) != 0L) {
            var4 = var3 | 256;
         }

         return var4;
      }

      void registerMediaButtonEventReceiver(PendingIntent var1, ComponentName var2) {
         if (sIsMbrPendingIntentSupported) {
            try {
               this.mAudioManager.registerMediaButtonEventReceiver(var1);
            } catch (NullPointerException var4) {
               Log.w("MediaSessionCompat", "Unable to register media button event receiver with PendingIntent, falling back to ComponentName.");
               sIsMbrPendingIntentSupported = false;
            }
         }

         if (!sIsMbrPendingIntentSupported) {
            super.registerMediaButtonEventReceiver(var1, var2);
         }

      }

      public void setCallback(MediaSessionCompat.Callback var1, Handler var2) {
         super.setCallback(var1, var2);
         if (var1 == null) {
            this.mRcc.setPlaybackPositionUpdateListener((OnPlaybackPositionUpdateListener)null);
         } else {
            OnPlaybackPositionUpdateListener var3 = new OnPlaybackPositionUpdateListener() {
               public void onPlaybackPositionUpdate(long var1) {
                  MediaSessionImplApi18.this.postToHandler(18, var1);
               }
            };
            this.mRcc.setPlaybackPositionUpdateListener(var3);
         }

      }

      void setRccState(PlaybackStateCompat var1) {
         long var2 = var1.getPosition();
         float var4 = var1.getPlaybackSpeed();
         long var5 = var1.getLastPositionUpdateTime();
         long var7 = SystemClock.elapsedRealtime();
         long var9 = var2;
         if (var1.getState() == 3) {
            long var11 = 0L;
            var9 = var2;
            if (var2 > 0L) {
               var9 = var11;
               if (var5 > 0L) {
                  var11 = var7 - var5;
                  var9 = var11;
                  if (var4 > 0.0F) {
                     var9 = var11;
                     if (var4 != 1.0F) {
                        var9 = (long)((float)var11 * var4);
                     }
                  }
               }

               var9 += var2;
            }
         }

         this.mRcc.setPlaybackState(this.getRccStateFromState(var1.getState()), var9, var4);
      }

      void unregisterMediaButtonEventReceiver(PendingIntent var1, ComponentName var2) {
         if (sIsMbrPendingIntentSupported) {
            this.mAudioManager.unregisterMediaButtonEventReceiver(var1);
         } else {
            super.unregisterMediaButtonEventReceiver(var1, var2);
         }

      }
   }

   @RequiresApi(19)
   static class MediaSessionImplApi19 extends MediaSessionCompat.MediaSessionImplApi18 {
      MediaSessionImplApi19(Context var1, String var2, ComponentName var3, PendingIntent var4) {
         super(var1, var2, var3, var4);
      }

      MetadataEditor buildRccMetadata(Bundle var1) {
         MetadataEditor var2 = super.buildRccMetadata(var1);
         long var3;
         if (this.mState == null) {
            var3 = 0L;
         } else {
            var3 = this.mState.getActions();
         }

         if ((var3 & 128L) != 0L) {
            var2.addEditableKey(268435457);
         }

         if (var1 == null) {
            return var2;
         } else {
            if (var1.containsKey("android.media.metadata.YEAR")) {
               var2.putLong(8, var1.getLong("android.media.metadata.YEAR"));
            }

            if (var1.containsKey("android.media.metadata.RATING")) {
               var2.putObject(101, var1.getParcelable("android.media.metadata.RATING"));
            }

            if (var1.containsKey("android.media.metadata.USER_RATING")) {
               var2.putObject(268435457, var1.getParcelable("android.media.metadata.USER_RATING"));
            }

            return var2;
         }
      }

      int getRccTransportControlFlagsFromActions(long var1) {
         int var3 = super.getRccTransportControlFlagsFromActions(var1);
         int var4 = var3;
         if ((var1 & 128L) != 0L) {
            var4 = var3 | 512;
         }

         return var4;
      }

      public void setCallback(MediaSessionCompat.Callback var1, Handler var2) {
         super.setCallback(var1, var2);
         if (var1 == null) {
            this.mRcc.setMetadataUpdateListener((OnMetadataUpdateListener)null);
         } else {
            OnMetadataUpdateListener var3 = new OnMetadataUpdateListener() {
               public void onMetadataUpdate(int var1, Object var2) {
                  if (var1 == 268435457 && var2 instanceof Rating) {
                     MediaSessionImplApi19.this.postToHandler(19, RatingCompat.fromRating(var2));
                  }

               }
            };
            this.mRcc.setMetadataUpdateListener(var3);
         }

      }
   }

   @RequiresApi(21)
   static class MediaSessionImplApi21 implements MediaSessionCompat.MediaSessionImpl {
      boolean mCaptioningEnabled;
      private boolean mDestroyed = false;
      private final RemoteCallbackList mExtraControllerCallbacks = new RemoteCallbackList();
      private MediaMetadataCompat mMetadata;
      private PlaybackStateCompat mPlaybackState;
      private List mQueue;
      int mRatingType;
      int mRepeatMode;
      private final Object mSessionObj;
      int mShuffleMode;
      boolean mShuffleModeEnabled;
      private final MediaSessionCompat.Token mToken;

      public MediaSessionImplApi21(Context var1, String var2) {
         this.mSessionObj = MediaSessionCompatApi21.createSession(var1, var2);
         this.mToken = new MediaSessionCompat.Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj), new MediaSessionCompat.MediaSessionImplApi21.ExtraSession());
      }

      public MediaSessionImplApi21(Object var1) {
         this.mSessionObj = MediaSessionCompatApi21.verifySession(var1);
         this.mToken = new MediaSessionCompat.Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj), new MediaSessionCompat.MediaSessionImplApi21.ExtraSession());
      }

      public String getCallingPackage() {
         return VERSION.SDK_INT < 24 ? null : MediaSessionCompatApi24.getCallingPackage(this.mSessionObj);
      }

      public Object getMediaSession() {
         return this.mSessionObj;
      }

      public PlaybackStateCompat getPlaybackState() {
         return this.mPlaybackState;
      }

      public Object getRemoteControlClient() {
         return null;
      }

      public MediaSessionCompat.Token getSessionToken() {
         return this.mToken;
      }

      public boolean isActive() {
         return MediaSessionCompatApi21.isActive(this.mSessionObj);
      }

      public void release() {
         this.mDestroyed = true;
         MediaSessionCompatApi21.release(this.mSessionObj);
      }

      public void sendSessionEvent(String var1, Bundle var2) {
         if (VERSION.SDK_INT < 23) {
            for(int var3 = this.mExtraControllerCallbacks.beginBroadcast() - 1; var3 >= 0; --var3) {
               IMediaControllerCallback var4 = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(var3);

               try {
                  var4.onEvent(var1, var2);
               } catch (RemoteException var5) {
               }
            }

            this.mExtraControllerCallbacks.finishBroadcast();
         }

         MediaSessionCompatApi21.sendSessionEvent(this.mSessionObj, var1, var2);
      }

      public void setActive(boolean var1) {
         MediaSessionCompatApi21.setActive(this.mSessionObj, var1);
      }

      public void setCallback(MediaSessionCompat.Callback var1, Handler var2) {
         Object var3 = this.mSessionObj;
         Object var4;
         if (var1 == null) {
            var4 = null;
         } else {
            var4 = var1.mCallbackObj;
         }

         MediaSessionCompatApi21.setCallback(var3, var4, var2);
         if (var1 != null) {
            var1.setSessionImpl(this, var2);
         }

      }

      public void setCaptioningEnabled(boolean var1) {
         if (this.mCaptioningEnabled != var1) {
            this.mCaptioningEnabled = var1;

            for(int var2 = this.mExtraControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
               IMediaControllerCallback var3 = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(var2);

               try {
                  var3.onCaptioningEnabledChanged(var1);
               } catch (RemoteException var4) {
               }
            }

            this.mExtraControllerCallbacks.finishBroadcast();
         }

      }

      public void setExtras(Bundle var1) {
         MediaSessionCompatApi21.setExtras(this.mSessionObj, var1);
      }

      public void setFlags(int var1) {
         MediaSessionCompatApi21.setFlags(this.mSessionObj, var1);
      }

      public void setMediaButtonReceiver(PendingIntent var1) {
         MediaSessionCompatApi21.setMediaButtonReceiver(this.mSessionObj, var1);
      }

      public void setMetadata(MediaMetadataCompat var1) {
         this.mMetadata = var1;
         Object var2 = this.mSessionObj;
         Object var3;
         if (var1 == null) {
            var3 = null;
         } else {
            var3 = var1.getMediaMetadata();
         }

         MediaSessionCompatApi21.setMetadata(var2, var3);
      }

      public void setPlaybackState(PlaybackStateCompat var1) {
         this.mPlaybackState = var1;

         for(int var2 = this.mExtraControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
            IMediaControllerCallback var3 = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(var2);

            try {
               var3.onPlaybackStateChanged(var1);
            } catch (RemoteException var4) {
            }
         }

         this.mExtraControllerCallbacks.finishBroadcast();
         Object var6 = this.mSessionObj;
         Object var5;
         if (var1 == null) {
            var5 = null;
         } else {
            var5 = var1.getPlaybackState();
         }

         MediaSessionCompatApi21.setPlaybackState(var6, var5);
      }

      public void setPlaybackToLocal(int var1) {
         MediaSessionCompatApi21.setPlaybackToLocal(this.mSessionObj, var1);
      }

      public void setPlaybackToRemote(VolumeProviderCompat var1) {
         MediaSessionCompatApi21.setPlaybackToRemote(this.mSessionObj, var1.getVolumeProvider());
      }

      public void setQueue(List var1) {
         this.mQueue = var1;
         ArrayList var4;
         if (var1 != null) {
            ArrayList var2 = new ArrayList();
            Iterator var3 = var1.iterator();

            while(true) {
               var4 = var2;
               if (!var3.hasNext()) {
                  break;
               }

               var2.add(((MediaSessionCompat.QueueItem)var3.next()).getQueueItem());
            }
         } else {
            var4 = null;
         }

         MediaSessionCompatApi21.setQueue(this.mSessionObj, var4);
      }

      public void setQueueTitle(CharSequence var1) {
         MediaSessionCompatApi21.setQueueTitle(this.mSessionObj, var1);
      }

      public void setRatingType(int var1) {
         if (VERSION.SDK_INT < 22) {
            this.mRatingType = var1;
         } else {
            MediaSessionCompatApi22.setRatingType(this.mSessionObj, var1);
         }

      }

      public void setRepeatMode(int var1) {
         if (this.mRepeatMode != var1) {
            this.mRepeatMode = var1;

            for(int var2 = this.mExtraControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
               IMediaControllerCallback var3 = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(var2);

               try {
                  var3.onRepeatModeChanged(var1);
               } catch (RemoteException var4) {
               }
            }

            this.mExtraControllerCallbacks.finishBroadcast();
         }

      }

      public void setSessionActivity(PendingIntent var1) {
         MediaSessionCompatApi21.setSessionActivity(this.mSessionObj, var1);
      }

      public void setShuffleMode(int var1) {
         if (this.mShuffleMode != var1) {
            this.mShuffleMode = var1;

            for(int var2 = this.mExtraControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
               IMediaControllerCallback var3 = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(var2);

               try {
                  var3.onShuffleModeChanged(var1);
               } catch (RemoteException var4) {
               }
            }

            this.mExtraControllerCallbacks.finishBroadcast();
         }

      }

      public void setShuffleModeEnabled(boolean var1) {
         if (this.mShuffleModeEnabled != var1) {
            this.mShuffleModeEnabled = var1;

            for(int var2 = this.mExtraControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
               IMediaControllerCallback var3 = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(var2);

               try {
                  var3.onShuffleModeChangedDeprecated(var1);
               } catch (RemoteException var4) {
               }
            }

            this.mExtraControllerCallbacks.finishBroadcast();
         }

      }

      class ExtraSession extends IMediaSession.Stub {
         public void addQueueItem(MediaDescriptionCompat var1) {
            throw new AssertionError();
         }

         public void addQueueItemAt(MediaDescriptionCompat var1, int var2) {
            throw new AssertionError();
         }

         public void adjustVolume(int var1, int var2, String var3) {
            throw new AssertionError();
         }

         public void fastForward() throws RemoteException {
            throw new AssertionError();
         }

         public Bundle getExtras() {
            throw new AssertionError();
         }

         public long getFlags() {
            throw new AssertionError();
         }

         public PendingIntent getLaunchPendingIntent() {
            throw new AssertionError();
         }

         public MediaMetadataCompat getMetadata() {
            throw new AssertionError();
         }

         public String getPackageName() {
            throw new AssertionError();
         }

         public PlaybackStateCompat getPlaybackState() {
            return MediaSessionCompat.getStateWithUpdatedPosition(MediaSessionImplApi21.this.mPlaybackState, MediaSessionImplApi21.this.mMetadata);
         }

         public List getQueue() {
            return null;
         }

         public CharSequence getQueueTitle() {
            throw new AssertionError();
         }

         public int getRatingType() {
            return MediaSessionImplApi21.this.mRatingType;
         }

         public int getRepeatMode() {
            return MediaSessionImplApi21.this.mRepeatMode;
         }

         public int getShuffleMode() {
            return MediaSessionImplApi21.this.mShuffleMode;
         }

         public String getTag() {
            throw new AssertionError();
         }

         public ParcelableVolumeInfo getVolumeAttributes() {
            throw new AssertionError();
         }

         public boolean isCaptioningEnabled() {
            return MediaSessionImplApi21.this.mCaptioningEnabled;
         }

         public boolean isShuffleModeEnabledDeprecated() {
            return MediaSessionImplApi21.this.mShuffleModeEnabled;
         }

         public boolean isTransportControlEnabled() {
            throw new AssertionError();
         }

         public void next() throws RemoteException {
            throw new AssertionError();
         }

         public void pause() throws RemoteException {
            throw new AssertionError();
         }

         public void play() throws RemoteException {
            throw new AssertionError();
         }

         public void playFromMediaId(String var1, Bundle var2) throws RemoteException {
            throw new AssertionError();
         }

         public void playFromSearch(String var1, Bundle var2) throws RemoteException {
            throw new AssertionError();
         }

         public void playFromUri(Uri var1, Bundle var2) throws RemoteException {
            throw new AssertionError();
         }

         public void prepare() throws RemoteException {
            throw new AssertionError();
         }

         public void prepareFromMediaId(String var1, Bundle var2) throws RemoteException {
            throw new AssertionError();
         }

         public void prepareFromSearch(String var1, Bundle var2) throws RemoteException {
            throw new AssertionError();
         }

         public void prepareFromUri(Uri var1, Bundle var2) throws RemoteException {
            throw new AssertionError();
         }

         public void previous() throws RemoteException {
            throw new AssertionError();
         }

         public void rate(RatingCompat var1) throws RemoteException {
            throw new AssertionError();
         }

         public void rateWithExtras(RatingCompat var1, Bundle var2) throws RemoteException {
            throw new AssertionError();
         }

         public void registerCallbackListener(IMediaControllerCallback var1) {
            if (!MediaSessionImplApi21.this.mDestroyed) {
               MediaSessionImplApi21.this.mExtraControllerCallbacks.register(var1);
            }

         }

         public void removeQueueItem(MediaDescriptionCompat var1) {
            throw new AssertionError();
         }

         public void removeQueueItemAt(int var1) {
            throw new AssertionError();
         }

         public void rewind() throws RemoteException {
            throw new AssertionError();
         }

         public void seekTo(long var1) throws RemoteException {
            throw new AssertionError();
         }

         public void sendCommand(String var1, Bundle var2, MediaSessionCompat.ResultReceiverWrapper var3) {
            throw new AssertionError();
         }

         public void sendCustomAction(String var1, Bundle var2) throws RemoteException {
            throw new AssertionError();
         }

         public boolean sendMediaButton(KeyEvent var1) {
            throw new AssertionError();
         }

         public void setCaptioningEnabled(boolean var1) throws RemoteException {
            throw new AssertionError();
         }

         public void setRepeatMode(int var1) throws RemoteException {
            throw new AssertionError();
         }

         public void setShuffleMode(int var1) throws RemoteException {
            throw new AssertionError();
         }

         public void setShuffleModeEnabledDeprecated(boolean var1) throws RemoteException {
            throw new AssertionError();
         }

         public void setVolumeTo(int var1, int var2, String var3) {
            throw new AssertionError();
         }

         public void skipToQueueItem(long var1) {
            throw new AssertionError();
         }

         public void stop() throws RemoteException {
            throw new AssertionError();
         }

         public void unregisterCallbackListener(IMediaControllerCallback var1) {
            MediaSessionImplApi21.this.mExtraControllerCallbacks.unregister(var1);
         }
      }
   }

   static class MediaSessionImplBase implements MediaSessionCompat.MediaSessionImpl {
      static final int RCC_PLAYSTATE_NONE = 0;
      final AudioManager mAudioManager;
      volatile MediaSessionCompat.Callback mCallback;
      boolean mCaptioningEnabled;
      private final Context mContext;
      final RemoteCallbackList mControllerCallbacks = new RemoteCallbackList();
      boolean mDestroyed = false;
      Bundle mExtras;
      int mFlags;
      private MediaSessionCompat.MediaSessionImplBase.MessageHandler mHandler;
      boolean mIsActive = false;
      private boolean mIsMbrRegistered = false;
      private boolean mIsRccRegistered = false;
      int mLocalStream;
      final Object mLock = new Object();
      private final ComponentName mMediaButtonReceiverComponentName;
      private final PendingIntent mMediaButtonReceiverIntent;
      MediaMetadataCompat mMetadata;
      final String mPackageName;
      List mQueue;
      CharSequence mQueueTitle;
      int mRatingType;
      final RemoteControlClient mRcc;
      int mRepeatMode;
      PendingIntent mSessionActivity;
      int mShuffleMode;
      boolean mShuffleModeEnabled;
      PlaybackStateCompat mState;
      private final MediaSessionCompat.MediaSessionImplBase.MediaSessionStub mStub;
      final String mTag;
      private final MediaSessionCompat.Token mToken;
      private VolumeProviderCompat.Callback mVolumeCallback = new VolumeProviderCompat.Callback() {
         public void onVolumeChanged(VolumeProviderCompat var1) {
            if (MediaSessionImplBase.this.mVolumeProvider == var1) {
               ParcelableVolumeInfo var2 = new ParcelableVolumeInfo(MediaSessionImplBase.this.mVolumeType, MediaSessionImplBase.this.mLocalStream, var1.getVolumeControl(), var1.getMaxVolume(), var1.getCurrentVolume());
               MediaSessionImplBase.this.sendVolumeInfoChanged(var2);
            }
         }
      };
      VolumeProviderCompat mVolumeProvider;
      int mVolumeType;

      public MediaSessionImplBase(Context var1, String var2, ComponentName var3, PendingIntent var4) {
         if (var3 == null) {
            throw new IllegalArgumentException("MediaButtonReceiver component may not be null.");
         } else {
            this.mContext = var1;
            this.mPackageName = var1.getPackageName();
            this.mAudioManager = (AudioManager)var1.getSystemService("audio");
            this.mTag = var2;
            this.mMediaButtonReceiverComponentName = var3;
            this.mMediaButtonReceiverIntent = var4;
            this.mStub = new MediaSessionCompat.MediaSessionImplBase.MediaSessionStub();
            this.mToken = new MediaSessionCompat.Token(this.mStub);
            this.mRatingType = 0;
            this.mVolumeType = 1;
            this.mLocalStream = 3;
            this.mRcc = new RemoteControlClient(var4);
         }
      }

      private void sendCaptioningEnabled(boolean var1) {
         for(int var2 = this.mControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
            IMediaControllerCallback var3 = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(var2);

            try {
               var3.onCaptioningEnabledChanged(var1);
            } catch (RemoteException var4) {
            }
         }

         this.mControllerCallbacks.finishBroadcast();
      }

      private void sendEvent(String var1, Bundle var2) {
         for(int var3 = this.mControllerCallbacks.beginBroadcast() - 1; var3 >= 0; --var3) {
            IMediaControllerCallback var4 = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(var3);

            try {
               var4.onEvent(var1, var2);
            } catch (RemoteException var5) {
            }
         }

         this.mControllerCallbacks.finishBroadcast();
      }

      private void sendExtras(Bundle var1) {
         for(int var2 = this.mControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
            IMediaControllerCallback var3 = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(var2);

            try {
               var3.onExtrasChanged(var1);
            } catch (RemoteException var4) {
            }
         }

         this.mControllerCallbacks.finishBroadcast();
      }

      private void sendMetadata(MediaMetadataCompat var1) {
         for(int var2 = this.mControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
            IMediaControllerCallback var3 = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(var2);

            try {
               var3.onMetadataChanged(var1);
            } catch (RemoteException var4) {
            }
         }

         this.mControllerCallbacks.finishBroadcast();
      }

      private void sendQueue(List var1) {
         for(int var2 = this.mControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
            IMediaControllerCallback var3 = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(var2);

            try {
               var3.onQueueChanged(var1);
            } catch (RemoteException var4) {
            }
         }

         this.mControllerCallbacks.finishBroadcast();
      }

      private void sendQueueTitle(CharSequence var1) {
         for(int var2 = this.mControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
            IMediaControllerCallback var3 = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(var2);

            try {
               var3.onQueueTitleChanged(var1);
            } catch (RemoteException var4) {
            }
         }

         this.mControllerCallbacks.finishBroadcast();
      }

      private void sendRepeatMode(int var1) {
         for(int var2 = this.mControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
            IMediaControllerCallback var3 = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(var2);

            try {
               var3.onRepeatModeChanged(var1);
            } catch (RemoteException var4) {
            }
         }

         this.mControllerCallbacks.finishBroadcast();
      }

      private void sendSessionDestroyed() {
         for(int var1 = this.mControllerCallbacks.beginBroadcast() - 1; var1 >= 0; --var1) {
            IMediaControllerCallback var2 = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(var1);

            try {
               var2.onSessionDestroyed();
            } catch (RemoteException var3) {
            }
         }

         this.mControllerCallbacks.finishBroadcast();
         this.mControllerCallbacks.kill();
      }

      private void sendShuffleMode(int var1) {
         for(int var2 = this.mControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
            IMediaControllerCallback var3 = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(var2);

            try {
               var3.onShuffleModeChanged(var1);
            } catch (RemoteException var4) {
            }
         }

         this.mControllerCallbacks.finishBroadcast();
      }

      private void sendShuffleModeEnabled(boolean var1) {
         for(int var2 = this.mControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
            IMediaControllerCallback var3 = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(var2);

            try {
               var3.onShuffleModeChangedDeprecated(var1);
            } catch (RemoteException var4) {
            }
         }

         this.mControllerCallbacks.finishBroadcast();
      }

      private void sendState(PlaybackStateCompat var1) {
         for(int var2 = this.mControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
            IMediaControllerCallback var3 = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(var2);

            try {
               var3.onPlaybackStateChanged(var1);
            } catch (RemoteException var4) {
            }
         }

         this.mControllerCallbacks.finishBroadcast();
      }

      void adjustVolume(int var1, int var2) {
         if (this.mVolumeType == 2) {
            if (this.mVolumeProvider != null) {
               this.mVolumeProvider.onAdjustVolume(var1);
            }
         } else {
            this.mAudioManager.adjustStreamVolume(this.mLocalStream, var1, var2);
         }

      }

      MetadataEditor buildRccMetadata(Bundle var1) {
         MetadataEditor var2 = this.mRcc.editMetadata(true);
         if (var1 == null) {
            return var2;
         } else {
            Bitmap var3;
            Bitmap var4;
            if (var1.containsKey("android.media.metadata.ART")) {
               var3 = (Bitmap)var1.getParcelable("android.media.metadata.ART");
               var4 = var3;
               if (var3 != null) {
                  var4 = var3.copy(var3.getConfig(), false);
               }

               var2.putBitmap(100, var4);
            } else if (var1.containsKey("android.media.metadata.ALBUM_ART")) {
               var3 = (Bitmap)var1.getParcelable("android.media.metadata.ALBUM_ART");
               var4 = var3;
               if (var3 != null) {
                  var4 = var3.copy(var3.getConfig(), false);
               }

               var2.putBitmap(100, var4);
            }

            if (var1.containsKey("android.media.metadata.ALBUM")) {
               var2.putString(1, var1.getString("android.media.metadata.ALBUM"));
            }

            if (var1.containsKey("android.media.metadata.ALBUM_ARTIST")) {
               var2.putString(13, var1.getString("android.media.metadata.ALBUM_ARTIST"));
            }

            if (var1.containsKey("android.media.metadata.ARTIST")) {
               var2.putString(2, var1.getString("android.media.metadata.ARTIST"));
            }

            if (var1.containsKey("android.media.metadata.AUTHOR")) {
               var2.putString(3, var1.getString("android.media.metadata.AUTHOR"));
            }

            if (var1.containsKey("android.media.metadata.COMPILATION")) {
               var2.putString(15, var1.getString("android.media.metadata.COMPILATION"));
            }

            if (var1.containsKey("android.media.metadata.COMPOSER")) {
               var2.putString(4, var1.getString("android.media.metadata.COMPOSER"));
            }

            if (var1.containsKey("android.media.metadata.DATE")) {
               var2.putString(5, var1.getString("android.media.metadata.DATE"));
            }

            if (var1.containsKey("android.media.metadata.DISC_NUMBER")) {
               var2.putLong(14, var1.getLong("android.media.metadata.DISC_NUMBER"));
            }

            if (var1.containsKey("android.media.metadata.DURATION")) {
               var2.putLong(9, var1.getLong("android.media.metadata.DURATION"));
            }

            if (var1.containsKey("android.media.metadata.GENRE")) {
               var2.putString(6, var1.getString("android.media.metadata.GENRE"));
            }

            if (var1.containsKey("android.media.metadata.TITLE")) {
               var2.putString(7, var1.getString("android.media.metadata.TITLE"));
            }

            if (var1.containsKey("android.media.metadata.TRACK_NUMBER")) {
               var2.putLong(0, var1.getLong("android.media.metadata.TRACK_NUMBER"));
            }

            if (var1.containsKey("android.media.metadata.WRITER")) {
               var2.putString(11, var1.getString("android.media.metadata.WRITER"));
            }

            return var2;
         }
      }

      public String getCallingPackage() {
         return null;
      }

      public Object getMediaSession() {
         return null;
      }

      public PlaybackStateCompat getPlaybackState() {
         // $FF: Couldn't be decompiled
      }

      int getRccStateFromState(int var1) {
         switch(var1) {
         case 0:
            return 0;
         case 1:
            return 1;
         case 2:
            return 2;
         case 3:
            return 3;
         case 4:
            return 4;
         case 5:
            return 5;
         case 6:
         case 8:
            return 8;
         case 7:
            return 9;
         case 9:
            return 7;
         case 10:
         case 11:
            return 6;
         default:
            return -1;
         }
      }

      int getRccTransportControlFlagsFromActions(long var1) {
         byte var3;
         if ((var1 & 1L) != 0L) {
            var3 = 32;
         } else {
            var3 = 0;
         }

         int var4 = var3;
         if ((var1 & 2L) != 0L) {
            var4 = var3 | 16;
         }

         int var5 = var4;
         if ((var1 & 4L) != 0L) {
            var5 = var4 | 4;
         }

         var4 = var5;
         if ((var1 & 8L) != 0L) {
            var4 = var5 | 2;
         }

         var5 = var4;
         if ((var1 & 16L) != 0L) {
            var5 = var4 | 1;
         }

         var4 = var5;
         if ((var1 & 32L) != 0L) {
            var4 = var5 | 128;
         }

         var5 = var4;
         if ((var1 & 64L) != 0L) {
            var5 = var4 | 64;
         }

         var4 = var5;
         if ((var1 & 512L) != 0L) {
            var4 = var5 | 8;
         }

         return var4;
      }

      public Object getRemoteControlClient() {
         return null;
      }

      public MediaSessionCompat.Token getSessionToken() {
         return this.mToken;
      }

      public boolean isActive() {
         return this.mIsActive;
      }

      void postToHandler(int var1) {
         this.postToHandler(var1, (Object)null);
      }

      void postToHandler(int var1, int var2) {
         this.postToHandler(var1, (Object)null, var2);
      }

      void postToHandler(int var1, Object var2) {
         this.postToHandler(var1, var2, (Bundle)null);
      }

      void postToHandler(int var1, Object var2, int var3) {
         Object var4 = this.mLock;
         synchronized(var4){}

         Throwable var10000;
         boolean var10001;
         label122: {
            try {
               if (this.mHandler != null) {
                  this.mHandler.post(var1, var2, var3);
               }
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label122;
            }

            label119:
            try {
               return;
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label119;
            }
         }

         while(true) {
            Throwable var17 = var10000;

            try {
               throw var17;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               continue;
            }
         }
      }

      void postToHandler(int var1, Object var2, Bundle var3) {
         Object var4 = this.mLock;
         synchronized(var4){}

         Throwable var10000;
         boolean var10001;
         label122: {
            try {
               if (this.mHandler != null) {
                  this.mHandler.post(var1, var2, var3);
               }
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label122;
            }

            label119:
            try {
               return;
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label119;
            }
         }

         while(true) {
            Throwable var17 = var10000;

            try {
               throw var17;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               continue;
            }
         }
      }

      void registerMediaButtonEventReceiver(PendingIntent var1, ComponentName var2) {
         this.mAudioManager.registerMediaButtonEventReceiver(var2);
      }

      public void release() {
         this.mIsActive = false;
         this.mDestroyed = true;
         this.update();
         this.sendSessionDestroyed();
      }

      public void sendSessionEvent(String var1, Bundle var2) {
         this.sendEvent(var1, var2);
      }

      void sendVolumeInfoChanged(ParcelableVolumeInfo var1) {
         for(int var2 = this.mControllerCallbacks.beginBroadcast() - 1; var2 >= 0; --var2) {
            IMediaControllerCallback var3 = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(var2);

            try {
               var3.onVolumeInfoChanged(var1);
            } catch (RemoteException var4) {
            }
         }

         this.mControllerCallbacks.finishBroadcast();
      }

      public void setActive(boolean var1) {
         if (var1 != this.mIsActive) {
            this.mIsActive = var1;
            if (this.update()) {
               this.setMetadata(this.mMetadata);
               this.setPlaybackState(this.mState);
            }

         }
      }

      public void setCallback(MediaSessionCompat.Callback var1, Handler var2) {
         this.mCallback = var1;
         if (var1 != null) {
            Handler var16 = var2;
            if (var2 == null) {
               var16 = new Handler();
            }

            Object var18 = this.mLock;
            synchronized(var18){}

            Throwable var10000;
            boolean var10001;
            label158: {
               try {
                  if (this.mHandler != null) {
                     this.mHandler.removeCallbacksAndMessages((Object)null);
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label158;
               }

               label155:
               try {
                  MediaSessionCompat.MediaSessionImplBase.MessageHandler var3 = new MediaSessionCompat.MediaSessionImplBase.MessageHandler(var16.getLooper());
                  this.mHandler = var3;
                  this.mCallback.setSessionImpl(this, var16);
                  return;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label155;
               }
            }

            while(true) {
               Throwable var17 = var10000;

               try {
                  throw var17;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      public void setCaptioningEnabled(boolean var1) {
         if (this.mCaptioningEnabled != var1) {
            this.mCaptioningEnabled = var1;
            this.sendCaptioningEnabled(var1);
         }

      }

      public void setExtras(Bundle var1) {
         this.mExtras = var1;
         this.sendExtras(var1);
      }

      public void setFlags(int param1) {
         // $FF: Couldn't be decompiled
      }

      public void setMediaButtonReceiver(PendingIntent var1) {
      }

      public void setMetadata(MediaMetadataCompat param1) {
         // $FF: Couldn't be decompiled
      }

      public void setPlaybackState(PlaybackStateCompat param1) {
         // $FF: Couldn't be decompiled
      }

      public void setPlaybackToLocal(int var1) {
         if (this.mVolumeProvider != null) {
            this.mVolumeProvider.setCallback((VolumeProviderCompat.Callback)null);
         }

         this.mVolumeType = 1;
         this.sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, 2, this.mAudioManager.getStreamMaxVolume(this.mLocalStream), this.mAudioManager.getStreamVolume(this.mLocalStream)));
      }

      public void setPlaybackToRemote(VolumeProviderCompat var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("volumeProvider may not be null");
         } else {
            if (this.mVolumeProvider != null) {
               this.mVolumeProvider.setCallback((VolumeProviderCompat.Callback)null);
            }

            this.mVolumeType = 2;
            this.mVolumeProvider = var1;
            this.sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, this.mVolumeProvider.getVolumeControl(), this.mVolumeProvider.getMaxVolume(), this.mVolumeProvider.getCurrentVolume()));
            var1.setCallback(this.mVolumeCallback);
         }
      }

      public void setQueue(List var1) {
         this.mQueue = var1;
         this.sendQueue(var1);
      }

      public void setQueueTitle(CharSequence var1) {
         this.mQueueTitle = var1;
         this.sendQueueTitle(var1);
      }

      public void setRatingType(int var1) {
         this.mRatingType = var1;
      }

      void setRccState(PlaybackStateCompat var1) {
         this.mRcc.setPlaybackState(this.getRccStateFromState(var1.getState()));
      }

      public void setRepeatMode(int var1) {
         if (this.mRepeatMode != var1) {
            this.mRepeatMode = var1;
            this.sendRepeatMode(var1);
         }

      }

      public void setSessionActivity(PendingIntent param1) {
         // $FF: Couldn't be decompiled
      }

      public void setShuffleMode(int var1) {
         if (this.mShuffleMode != var1) {
            this.mShuffleMode = var1;
            this.sendShuffleMode(var1);
         }

      }

      public void setShuffleModeEnabled(boolean var1) {
         if (this.mShuffleModeEnabled != var1) {
            this.mShuffleModeEnabled = var1;
            this.sendShuffleModeEnabled(var1);
         }

      }

      void setVolumeTo(int var1, int var2) {
         if (this.mVolumeType == 2) {
            if (this.mVolumeProvider != null) {
               this.mVolumeProvider.onSetVolumeTo(var1);
            }
         } else {
            this.mAudioManager.setStreamVolume(this.mLocalStream, var1, var2);
         }

      }

      void unregisterMediaButtonEventReceiver(PendingIntent var1, ComponentName var2) {
         this.mAudioManager.unregisterMediaButtonEventReceiver(var2);
      }

      boolean update() {
         boolean var1 = this.mIsActive;
         boolean var2 = true;
         if (var1) {
            if (!this.mIsMbrRegistered && (this.mFlags & 1) != 0) {
               this.registerMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
               this.mIsMbrRegistered = true;
            } else if (this.mIsMbrRegistered && (this.mFlags & 1) == 0) {
               this.unregisterMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
               this.mIsMbrRegistered = false;
            }

            if (!this.mIsRccRegistered && (this.mFlags & 2) != 0) {
               this.mAudioManager.registerRemoteControlClient(this.mRcc);
               this.mIsRccRegistered = true;
               return var2;
            }

            if (this.mIsRccRegistered && (this.mFlags & 2) == 0) {
               this.mRcc.setPlaybackState(0);
               this.mAudioManager.unregisterRemoteControlClient(this.mRcc);
               this.mIsRccRegistered = false;
            }
         } else {
            if (this.mIsMbrRegistered) {
               this.unregisterMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
               this.mIsMbrRegistered = false;
            }

            if (this.mIsRccRegistered) {
               this.mRcc.setPlaybackState(0);
               this.mAudioManager.unregisterRemoteControlClient(this.mRcc);
               this.mIsRccRegistered = false;
            }
         }

         var2 = false;
         return var2;
      }

      private static final class Command {
         public final String command;
         public final Bundle extras;
         public final ResultReceiver stub;

         public Command(String var1, Bundle var2, ResultReceiver var3) {
            this.command = var1;
            this.extras = var2;
            this.stub = var3;
         }
      }

      class MediaSessionStub extends IMediaSession.Stub {
         public void addQueueItem(MediaDescriptionCompat var1) {
            MediaSessionImplBase.this.postToHandler(25, var1);
         }

         public void addQueueItemAt(MediaDescriptionCompat var1, int var2) {
            MediaSessionImplBase.this.postToHandler(26, var1, var2);
         }

         public void adjustVolume(int var1, int var2, String var3) {
            MediaSessionImplBase.this.adjustVolume(var1, var2);
         }

         public void fastForward() throws RemoteException {
            MediaSessionImplBase.this.postToHandler(16);
         }

         public Bundle getExtras() {
            // $FF: Couldn't be decompiled
         }

         public long getFlags() {
            // $FF: Couldn't be decompiled
         }

         public PendingIntent getLaunchPendingIntent() {
            // $FF: Couldn't be decompiled
         }

         public MediaMetadataCompat getMetadata() {
            return MediaSessionImplBase.this.mMetadata;
         }

         public String getPackageName() {
            return MediaSessionImplBase.this.mPackageName;
         }

         public PlaybackStateCompat getPlaybackState() {
            // $FF: Couldn't be decompiled
         }

         public List getQueue() {
            // $FF: Couldn't be decompiled
         }

         public CharSequence getQueueTitle() {
            return MediaSessionImplBase.this.mQueueTitle;
         }

         public int getRatingType() {
            return MediaSessionImplBase.this.mRatingType;
         }

         public int getRepeatMode() {
            return MediaSessionImplBase.this.mRepeatMode;
         }

         public int getShuffleMode() {
            return MediaSessionImplBase.this.mShuffleMode;
         }

         public String getTag() {
            return MediaSessionImplBase.this.mTag;
         }

         public ParcelableVolumeInfo getVolumeAttributes() {
            Object var1 = MediaSessionImplBase.this.mLock;
            synchronized(var1){}

            Throwable var10000;
            boolean var10001;
            label259: {
               int var2;
               int var3;
               VolumeProviderCompat var4;
               try {
                  var2 = MediaSessionImplBase.this.mVolumeType;
                  var3 = MediaSessionImplBase.this.mLocalStream;
                  var4 = MediaSessionImplBase.this.mVolumeProvider;
               } catch (Throwable var37) {
                  var10000 = var37;
                  var10001 = false;
                  break label259;
               }

               int var5 = 2;
               int var6;
               int var7;
               if (var2 == 2) {
                  try {
                     var5 = var4.getVolumeControl();
                     var6 = var4.getMaxVolume();
                     var7 = var4.getCurrentVolume();
                  } catch (Throwable var36) {
                     var10000 = var36;
                     var10001 = false;
                     break label259;
                  }
               } else {
                  try {
                     var6 = MediaSessionImplBase.this.mAudioManager.getStreamMaxVolume(var3);
                     var7 = MediaSessionImplBase.this.mAudioManager.getStreamVolume(var3);
                  } catch (Throwable var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label259;
                  }
               }

               label244:
               try {
                  return new ParcelableVolumeInfo(var2, var3, var5, var6, var7);
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label244;
               }
            }

            while(true) {
               Throwable var38 = var10000;

               try {
                  throw var38;
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  continue;
               }
            }
         }

         public boolean isCaptioningEnabled() {
            return MediaSessionImplBase.this.mCaptioningEnabled;
         }

         public boolean isShuffleModeEnabledDeprecated() {
            return MediaSessionImplBase.this.mShuffleModeEnabled;
         }

         public boolean isTransportControlEnabled() {
            boolean var1;
            if ((MediaSessionImplBase.this.mFlags & 2) != 0) {
               var1 = true;
            } else {
               var1 = false;
            }

            return var1;
         }

         public void next() throws RemoteException {
            MediaSessionImplBase.this.postToHandler(14);
         }

         public void pause() throws RemoteException {
            MediaSessionImplBase.this.postToHandler(12);
         }

         public void play() throws RemoteException {
            MediaSessionImplBase.this.postToHandler(7);
         }

         public void playFromMediaId(String var1, Bundle var2) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(8, var1, var2);
         }

         public void playFromSearch(String var1, Bundle var2) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(9, var1, var2);
         }

         public void playFromUri(Uri var1, Bundle var2) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(10, var1, var2);
         }

         public void prepare() throws RemoteException {
            MediaSessionImplBase.this.postToHandler(3);
         }

         public void prepareFromMediaId(String var1, Bundle var2) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(4, var1, var2);
         }

         public void prepareFromSearch(String var1, Bundle var2) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(5, var1, var2);
         }

         public void prepareFromUri(Uri var1, Bundle var2) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(6, var1, var2);
         }

         public void previous() throws RemoteException {
            MediaSessionImplBase.this.postToHandler(15);
         }

         public void rate(RatingCompat var1) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(19, var1);
         }

         public void rateWithExtras(RatingCompat var1, Bundle var2) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(31, var1, var2);
         }

         public void registerCallbackListener(IMediaControllerCallback var1) {
            if (MediaSessionImplBase.this.mDestroyed) {
               try {
                  var1.onSessionDestroyed();
               } catch (Exception var2) {
               }

            } else {
               MediaSessionImplBase.this.mControllerCallbacks.register(var1);
            }
         }

         public void removeQueueItem(MediaDescriptionCompat var1) {
            MediaSessionImplBase.this.postToHandler(27, var1);
         }

         public void removeQueueItemAt(int var1) {
            MediaSessionImplBase.this.postToHandler(28, var1);
         }

         public void rewind() throws RemoteException {
            MediaSessionImplBase.this.postToHandler(17);
         }

         public void seekTo(long var1) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(18, var1);
         }

         public void sendCommand(String var1, Bundle var2, MediaSessionCompat.ResultReceiverWrapper var3) {
            MediaSessionImplBase.this.postToHandler(1, new MediaSessionCompat.MediaSessionImplBase.Command(var1, var2, var3.mResultReceiver));
         }

         public void sendCustomAction(String var1, Bundle var2) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(20, var1, var2);
         }

         public boolean sendMediaButton(KeyEvent var1) {
            int var2 = MediaSessionImplBase.this.mFlags;
            boolean var3 = true;
            if ((var2 & 1) == 0) {
               var3 = false;
            }

            if (var3) {
               MediaSessionImplBase.this.postToHandler(21, var1);
            }

            return var3;
         }

         public void setCaptioningEnabled(boolean var1) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(29, var1);
         }

         public void setRepeatMode(int var1) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(23, var1);
         }

         public void setShuffleMode(int var1) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(30, var1);
         }

         public void setShuffleModeEnabledDeprecated(boolean var1) throws RemoteException {
            MediaSessionImplBase.this.postToHandler(24, var1);
         }

         public void setVolumeTo(int var1, int var2, String var3) {
            MediaSessionImplBase.this.setVolumeTo(var1, var2);
         }

         public void skipToQueueItem(long var1) {
            MediaSessionImplBase.this.postToHandler(11, var1);
         }

         public void stop() throws RemoteException {
            MediaSessionImplBase.this.postToHandler(13);
         }

         public void unregisterCallbackListener(IMediaControllerCallback var1) {
            MediaSessionImplBase.this.mControllerCallbacks.unregister(var1);
         }
      }

      class MessageHandler extends Handler {
         private static final int KEYCODE_MEDIA_PAUSE = 127;
         private static final int KEYCODE_MEDIA_PLAY = 126;
         private static final int MSG_ADD_QUEUE_ITEM = 25;
         private static final int MSG_ADD_QUEUE_ITEM_AT = 26;
         private static final int MSG_ADJUST_VOLUME = 2;
         private static final int MSG_COMMAND = 1;
         private static final int MSG_CUSTOM_ACTION = 20;
         private static final int MSG_FAST_FORWARD = 16;
         private static final int MSG_MEDIA_BUTTON = 21;
         private static final int MSG_NEXT = 14;
         private static final int MSG_PAUSE = 12;
         private static final int MSG_PLAY = 7;
         private static final int MSG_PLAY_MEDIA_ID = 8;
         private static final int MSG_PLAY_SEARCH = 9;
         private static final int MSG_PLAY_URI = 10;
         private static final int MSG_PREPARE = 3;
         private static final int MSG_PREPARE_MEDIA_ID = 4;
         private static final int MSG_PREPARE_SEARCH = 5;
         private static final int MSG_PREPARE_URI = 6;
         private static final int MSG_PREVIOUS = 15;
         private static final int MSG_RATE = 19;
         private static final int MSG_RATE_EXTRA = 31;
         private static final int MSG_REMOVE_QUEUE_ITEM = 27;
         private static final int MSG_REMOVE_QUEUE_ITEM_AT = 28;
         private static final int MSG_REWIND = 17;
         private static final int MSG_SEEK_TO = 18;
         private static final int MSG_SET_CAPTIONING_ENABLED = 29;
         private static final int MSG_SET_REPEAT_MODE = 23;
         private static final int MSG_SET_SHUFFLE_MODE = 30;
         private static final int MSG_SET_SHUFFLE_MODE_ENABLED = 24;
         private static final int MSG_SET_VOLUME = 22;
         private static final int MSG_SKIP_TO_ITEM = 11;
         private static final int MSG_STOP = 13;

         public MessageHandler(Looper var2) {
            super(var2);
         }

         private void onMediaButtonEvent(KeyEvent var1, MediaSessionCompat.Callback var2) {
            if (var1 != null && var1.getAction() == 0) {
               long var3;
               if (MediaSessionImplBase.this.mState == null) {
                  var3 = 0L;
               } else {
                  var3 = MediaSessionImplBase.this.mState.getActions();
               }

               int var5 = var1.getKeyCode();
               if (var5 != 79) {
                  switch(var5) {
                  case 85:
                     break;
                  case 86:
                     if ((var3 & 1L) != 0L) {
                        var2.onStop();
                     }

                     return;
                  case 87:
                     if ((var3 & 32L) != 0L) {
                        var2.onSkipToNext();
                     }

                     return;
                  case 88:
                     if ((var3 & 16L) != 0L) {
                        var2.onSkipToPrevious();
                     }

                     return;
                  case 89:
                     if ((var3 & 8L) != 0L) {
                        var2.onRewind();
                     }

                     return;
                  case 90:
                     if ((var3 & 64L) != 0L) {
                        var2.onFastForward();
                     }

                     return;
                  default:
                     switch(var5) {
                     case 126:
                        if ((var3 & 4L) != 0L) {
                           var2.onPlay();
                        }

                        return;
                     case 127:
                        if ((var3 & 2L) != 0L) {
                           var2.onPause();
                        }

                        return;
                     default:
                        return;
                     }
                  }
               }

               Log.w("MediaSessionCompat", "KEYCODE_MEDIA_PLAY_PAUSE and KEYCODE_HEADSETHOOK are handled already");
            }
         }

         public void handleMessage(Message var1) {
            MediaSessionCompat.Callback var2 = MediaSessionImplBase.this.mCallback;
            if (var2 != null) {
               switch(var1.what) {
               case 1:
                  MediaSessionCompat.MediaSessionImplBase.Command var6 = (MediaSessionCompat.MediaSessionImplBase.Command)var1.obj;
                  var2.onCommand(var6.command, var6.extras, var6.stub);
                  break;
               case 2:
                  MediaSessionImplBase.this.adjustVolume(var1.arg1, 0);
                  break;
               case 3:
                  var2.onPrepare();
                  break;
               case 4:
                  var2.onPrepareFromMediaId((String)var1.obj, var1.getData());
                  break;
               case 5:
                  var2.onPrepareFromSearch((String)var1.obj, var1.getData());
                  break;
               case 6:
                  var2.onPrepareFromUri((Uri)var1.obj, var1.getData());
                  break;
               case 7:
                  var2.onPlay();
                  break;
               case 8:
                  var2.onPlayFromMediaId((String)var1.obj, var1.getData());
                  break;
               case 9:
                  var2.onPlayFromSearch((String)var1.obj, var1.getData());
                  break;
               case 10:
                  var2.onPlayFromUri((Uri)var1.obj, var1.getData());
                  break;
               case 11:
                  var2.onSkipToQueueItem((Long)var1.obj);
                  break;
               case 12:
                  var2.onPause();
                  break;
               case 13:
                  var2.onStop();
                  break;
               case 14:
                  var2.onSkipToNext();
                  break;
               case 15:
                  var2.onSkipToPrevious();
                  break;
               case 16:
                  var2.onFastForward();
                  break;
               case 17:
                  var2.onRewind();
                  break;
               case 18:
                  var2.onSeekTo((Long)var1.obj);
                  break;
               case 19:
                  var2.onSetRating((RatingCompat)var1.obj);
                  break;
               case 20:
                  var2.onCustomAction((String)var1.obj, var1.getData());
                  break;
               case 21:
                  KeyEvent var3 = (KeyEvent)var1.obj;
                  Intent var5 = new Intent("android.intent.action.MEDIA_BUTTON");
                  var5.putExtra("android.intent.extra.KEY_EVENT", var3);
                  if (!var2.onMediaButtonEvent(var5)) {
                     this.onMediaButtonEvent(var3, var2);
                  }
                  break;
               case 22:
                  MediaSessionImplBase.this.setVolumeTo(var1.arg1, 0);
                  break;
               case 23:
                  var2.onSetRepeatMode(var1.arg1);
                  break;
               case 24:
                  var2.onSetShuffleModeEnabled((Boolean)var1.obj);
                  break;
               case 25:
                  var2.onAddQueueItem((MediaDescriptionCompat)var1.obj);
                  break;
               case 26:
                  var2.onAddQueueItem((MediaDescriptionCompat)var1.obj, var1.arg1);
                  break;
               case 27:
                  var2.onRemoveQueueItem((MediaDescriptionCompat)var1.obj);
                  break;
               case 28:
                  if (MediaSessionImplBase.this.mQueue != null) {
                     MediaSessionCompat.QueueItem var4;
                     if (var1.arg1 >= 0 && var1.arg1 < MediaSessionImplBase.this.mQueue.size()) {
                        var4 = (MediaSessionCompat.QueueItem)MediaSessionImplBase.this.mQueue.get(var1.arg1);
                     } else {
                        var4 = null;
                     }

                     if (var4 != null) {
                        var2.onRemoveQueueItem(var4.getDescription());
                     }
                  }
                  break;
               case 29:
                  var2.onSetCaptioningEnabled((Boolean)var1.obj);
                  break;
               case 30:
                  var2.onSetShuffleMode(var1.arg1);
                  break;
               case 31:
                  var2.onSetRating((RatingCompat)var1.obj, var1.getData());
               }

            }
         }

         public void post(int var1) {
            this.post(var1, (Object)null);
         }

         public void post(int var1, Object var2) {
            this.obtainMessage(var1, var2).sendToTarget();
         }

         public void post(int var1, Object var2, int var3) {
            this.obtainMessage(var1, var3, 0, var2).sendToTarget();
         }

         public void post(int var1, Object var2, Bundle var3) {
            Message var4 = this.obtainMessage(var1, var2);
            var4.setData(var3);
            var4.sendToTarget();
         }
      }
   }

   public interface OnActiveChangeListener {
      void onActiveChanged();
   }

   public static final class QueueItem implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public MediaSessionCompat.QueueItem createFromParcel(Parcel var1) {
            return new MediaSessionCompat.QueueItem(var1);
         }

         public MediaSessionCompat.QueueItem[] newArray(int var1) {
            return new MediaSessionCompat.QueueItem[var1];
         }
      };
      public static final int UNKNOWN_ID = -1;
      private final MediaDescriptionCompat mDescription;
      private final long mId;
      private Object mItem;

      QueueItem(Parcel var1) {
         this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(var1);
         this.mId = var1.readLong();
      }

      public QueueItem(MediaDescriptionCompat var1, long var2) {
         this((Object)null, var1, var2);
      }

      private QueueItem(Object var1, MediaDescriptionCompat var2, long var3) {
         if (var2 == null) {
            throw new IllegalArgumentException("Description cannot be null.");
         } else if (var3 == -1L) {
            throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
         } else {
            this.mDescription = var2;
            this.mId = var3;
            this.mItem = var1;
         }
      }

      public static MediaSessionCompat.QueueItem fromQueueItem(Object var0) {
         return var0 != null && VERSION.SDK_INT >= 21 ? new MediaSessionCompat.QueueItem(var0, MediaDescriptionCompat.fromMediaDescription(MediaSessionCompatApi21.QueueItem.getDescription(var0)), MediaSessionCompatApi21.QueueItem.getQueueId(var0)) : null;
      }

      public static List fromQueueItemList(List var0) {
         if (var0 != null && VERSION.SDK_INT >= 21) {
            ArrayList var1 = new ArrayList();
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               var1.add(fromQueueItem(var2.next()));
            }

            return var1;
         } else {
            return null;
         }
      }

      public int describeContents() {
         return 0;
      }

      public MediaDescriptionCompat getDescription() {
         return this.mDescription;
      }

      public long getQueueId() {
         return this.mId;
      }

      public Object getQueueItem() {
         if (this.mItem == null && VERSION.SDK_INT >= 21) {
            this.mItem = MediaSessionCompatApi21.QueueItem.createItem(this.mDescription.getMediaDescription(), this.mId);
            return this.mItem;
         } else {
            return this.mItem;
         }
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("MediaSession.QueueItem {Description=");
         var1.append(this.mDescription);
         var1.append(", Id=");
         var1.append(this.mId);
         var1.append(" }");
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         this.mDescription.writeToParcel(var1, var2);
         var1.writeLong(this.mId);
      }
   }

   static final class ResultReceiverWrapper implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public MediaSessionCompat.ResultReceiverWrapper createFromParcel(Parcel var1) {
            return new MediaSessionCompat.ResultReceiverWrapper(var1);
         }

         public MediaSessionCompat.ResultReceiverWrapper[] newArray(int var1) {
            return new MediaSessionCompat.ResultReceiverWrapper[var1];
         }
      };
      private ResultReceiver mResultReceiver;

      ResultReceiverWrapper(Parcel var1) {
         this.mResultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(var1);
      }

      public ResultReceiverWrapper(ResultReceiver var1) {
         this.mResultReceiver = var1;
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel var1, int var2) {
         this.mResultReceiver.writeToParcel(var1, var2);
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface SessionFlags {
   }

   public static final class Token implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public MediaSessionCompat.Token createFromParcel(Parcel var1) {
            Object var2;
            if (VERSION.SDK_INT >= 21) {
               var2 = var1.readParcelable((ClassLoader)null);
            } else {
               var2 = var1.readStrongBinder();
            }

            return new MediaSessionCompat.Token(var2);
         }

         public MediaSessionCompat.Token[] newArray(int var1) {
            return new MediaSessionCompat.Token[var1];
         }
      };
      private final IMediaSession mExtraBinder;
      private final Object mInner;

      Token(Object var1) {
         this(var1, (IMediaSession)null);
      }

      Token(Object var1, IMediaSession var2) {
         this.mInner = var1;
         this.mExtraBinder = var2;
      }

      public static MediaSessionCompat.Token fromToken(Object var0) {
         return fromToken(var0, (IMediaSession)null);
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public static MediaSessionCompat.Token fromToken(Object var0, IMediaSession var1) {
         return var0 != null && VERSION.SDK_INT >= 21 ? new MediaSessionCompat.Token(MediaSessionCompatApi21.verifyToken(var0), var1) : null;
      }

      public int describeContents() {
         return 0;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof MediaSessionCompat.Token)) {
            return false;
         } else {
            MediaSessionCompat.Token var3 = (MediaSessionCompat.Token)var1;
            if (this.mInner == null) {
               if (var3.mInner != null) {
                  var2 = false;
               }

               return var2;
            } else {
               return var3.mInner == null ? false : this.mInner.equals(var3.mInner);
            }
         }
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public IMediaSession getExtraBinder() {
         return this.mExtraBinder;
      }

      public Object getToken() {
         return this.mInner;
      }

      public int hashCode() {
         return this.mInner == null ? 0 : this.mInner.hashCode();
      }

      public void writeToParcel(Parcel var1, int var2) {
         if (VERSION.SDK_INT >= 21) {
            var1.writeParcelable((Parcelable)this.mInner, var2);
         } else {
            var1.writeStrongBinder((IBinder)this.mInner);
         }

      }
   }
}
