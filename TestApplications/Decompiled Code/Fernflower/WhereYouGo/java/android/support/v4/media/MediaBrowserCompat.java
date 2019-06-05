package android.support.v4.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.BuildCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public final class MediaBrowserCompat {
   static final boolean DEBUG = Log.isLoggable("MediaBrowserCompat", 3);
   public static final String EXTRA_PAGE = "android.media.browse.extra.PAGE";
   public static final String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";
   static final String TAG = "MediaBrowserCompat";
   private final MediaBrowserCompat.MediaBrowserImpl mImpl;

   public MediaBrowserCompat(Context var1, ComponentName var2, MediaBrowserCompat.ConnectionCallback var3, Bundle var4) {
      if (VERSION.SDK_INT < 26 && !BuildCompat.isAtLeastO()) {
         if (VERSION.SDK_INT >= 23) {
            this.mImpl = new MediaBrowserCompat.MediaBrowserImplApi23(var1, var2, var3, var4);
         } else if (VERSION.SDK_INT >= 21) {
            this.mImpl = new MediaBrowserCompat.MediaBrowserImplApi21(var1, var2, var3, var4);
         } else {
            this.mImpl = new MediaBrowserCompat.MediaBrowserImplBase(var1, var2, var3, var4);
         }
      } else {
         this.mImpl = new MediaBrowserCompat.MediaBrowserImplApi24(var1, var2, var3, var4);
      }

   }

   public void connect() {
      this.mImpl.connect();
   }

   public void disconnect() {
      this.mImpl.disconnect();
   }

   @Nullable
   public Bundle getExtras() {
      return this.mImpl.getExtras();
   }

   public void getItem(@NonNull String var1, @NonNull MediaBrowserCompat.ItemCallback var2) {
      this.mImpl.getItem(var1, var2);
   }

   @NonNull
   public String getRoot() {
      return this.mImpl.getRoot();
   }

   @NonNull
   public ComponentName getServiceComponent() {
      return this.mImpl.getServiceComponent();
   }

   @NonNull
   public MediaSessionCompat.Token getSessionToken() {
      return this.mImpl.getSessionToken();
   }

   public boolean isConnected() {
      return this.mImpl.isConnected();
   }

   public void search(@NonNull String var1, Bundle var2, @NonNull MediaBrowserCompat.SearchCallback var3) {
      if (TextUtils.isEmpty(var1)) {
         throw new IllegalArgumentException("query cannot be empty");
      } else if (var3 == null) {
         throw new IllegalArgumentException("callback cannot be null");
      } else {
         this.mImpl.search(var1, var2, var3);
      }
   }

   public void subscribe(@NonNull String var1, @NonNull Bundle var2, @NonNull MediaBrowserCompat.SubscriptionCallback var3) {
      if (TextUtils.isEmpty(var1)) {
         throw new IllegalArgumentException("parentId is empty");
      } else if (var3 == null) {
         throw new IllegalArgumentException("callback is null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("options are null");
      } else {
         this.mImpl.subscribe(var1, var2, var3);
      }
   }

   public void subscribe(@NonNull String var1, @NonNull MediaBrowserCompat.SubscriptionCallback var2) {
      if (TextUtils.isEmpty(var1)) {
         throw new IllegalArgumentException("parentId is empty");
      } else if (var2 == null) {
         throw new IllegalArgumentException("callback is null");
      } else {
         this.mImpl.subscribe(var1, (Bundle)null, var2);
      }
   }

   public void unsubscribe(@NonNull String var1) {
      if (TextUtils.isEmpty(var1)) {
         throw new IllegalArgumentException("parentId is empty");
      } else {
         this.mImpl.unsubscribe(var1, (MediaBrowserCompat.SubscriptionCallback)null);
      }
   }

   public void unsubscribe(@NonNull String var1, @NonNull MediaBrowserCompat.SubscriptionCallback var2) {
      if (TextUtils.isEmpty(var1)) {
         throw new IllegalArgumentException("parentId is empty");
      } else if (var2 == null) {
         throw new IllegalArgumentException("callback is null");
      } else {
         this.mImpl.unsubscribe(var1, var2);
      }
   }

   private static class CallbackHandler extends Handler {
      private final WeakReference mCallbackImplRef;
      private WeakReference mCallbacksMessengerRef;

      CallbackHandler(MediaBrowserCompat.MediaBrowserServiceCallbackImpl var1) {
         this.mCallbackImplRef = new WeakReference(var1);
      }

      public void handleMessage(Message var1) {
         if (this.mCallbacksMessengerRef != null && this.mCallbacksMessengerRef.get() != null && this.mCallbackImplRef.get() != null) {
            Bundle var2 = var1.getData();
            var2.setClassLoader(MediaSessionCompat.class.getClassLoader());
            switch(var1.what) {
            case 1:
               ((MediaBrowserCompat.MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onServiceConnected((Messenger)this.mCallbacksMessengerRef.get(), var2.getString("data_media_item_id"), (MediaSessionCompat.Token)var2.getParcelable("data_media_session_token"), var2.getBundle("data_root_hints"));
               break;
            case 2:
               ((MediaBrowserCompat.MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onConnectionFailed((Messenger)this.mCallbacksMessengerRef.get());
               break;
            case 3:
               ((MediaBrowserCompat.MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onLoadChildren((Messenger)this.mCallbacksMessengerRef.get(), var2.getString("data_media_item_id"), var2.getParcelableArrayList("data_media_item_list"), var2.getBundle("data_options"));
               break;
            default:
               Log.w("MediaBrowserCompat", "Unhandled message: " + var1 + "\n  Client version: " + 1 + "\n  Service version: " + var1.arg1);
            }
         }

      }

      void setCallbacksMessenger(Messenger var1) {
         this.mCallbacksMessengerRef = new WeakReference(var1);
      }
   }

   public static class ConnectionCallback {
      MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal mConnectionCallbackInternal;
      final Object mConnectionCallbackObj;

      public ConnectionCallback() {
         if (VERSION.SDK_INT >= 21) {
            this.mConnectionCallbackObj = MediaBrowserCompatApi21.createConnectionCallback(new MediaBrowserCompat.ConnectionCallback.StubApi21());
         } else {
            this.mConnectionCallbackObj = null;
         }

      }

      public void onConnected() {
      }

      public void onConnectionFailed() {
      }

      public void onConnectionSuspended() {
      }

      void setInternalConnectionCallback(MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal var1) {
         this.mConnectionCallbackInternal = var1;
      }

      interface ConnectionCallbackInternal {
         void onConnected();

         void onConnectionFailed();

         void onConnectionSuspended();
      }

      private class StubApi21 implements MediaBrowserCompatApi21.ConnectionCallback {
         StubApi21() {
         }

         public void onConnected() {
            if (ConnectionCallback.this.mConnectionCallbackInternal != null) {
               ConnectionCallback.this.mConnectionCallbackInternal.onConnected();
            }

            ConnectionCallback.this.onConnected();
         }

         public void onConnectionFailed() {
            if (ConnectionCallback.this.mConnectionCallbackInternal != null) {
               ConnectionCallback.this.mConnectionCallbackInternal.onConnectionFailed();
            }

            ConnectionCallback.this.onConnectionFailed();
         }

         public void onConnectionSuspended() {
            if (ConnectionCallback.this.mConnectionCallbackInternal != null) {
               ConnectionCallback.this.mConnectionCallbackInternal.onConnectionSuspended();
            }

            ConnectionCallback.this.onConnectionSuspended();
         }
      }
   }

   public abstract static class ItemCallback {
      final Object mItemCallbackObj;

      public ItemCallback() {
         if (VERSION.SDK_INT >= 23) {
            this.mItemCallbackObj = MediaBrowserCompatApi23.createItemCallback(new MediaBrowserCompat.ItemCallback.StubApi23());
         } else {
            this.mItemCallbackObj = null;
         }

      }

      public void onError(@NonNull String var1) {
      }

      public void onItemLoaded(MediaBrowserCompat.MediaItem var1) {
      }

      private class StubApi23 implements MediaBrowserCompatApi23.ItemCallback {
         StubApi23() {
         }

         public void onError(@NonNull String var1) {
            ItemCallback.this.onError(var1);
         }

         public void onItemLoaded(Parcel var1) {
            if (var1 == null) {
               ItemCallback.this.onItemLoaded((MediaBrowserCompat.MediaItem)null);
            } else {
               var1.setDataPosition(0);
               MediaBrowserCompat.MediaItem var2 = (MediaBrowserCompat.MediaItem)MediaBrowserCompat.MediaItem.CREATOR.createFromParcel(var1);
               var1.recycle();
               ItemCallback.this.onItemLoaded(var2);
            }

         }
      }
   }

   private static class ItemReceiver extends ResultReceiver {
      private final MediaBrowserCompat.ItemCallback mCallback;
      private final String mMediaId;

      ItemReceiver(String var1, MediaBrowserCompat.ItemCallback var2, Handler var3) {
         super(var3);
         this.mMediaId = var1;
         this.mCallback = var2;
      }

      protected void onReceiveResult(int var1, Bundle var2) {
         if (var2 != null) {
            var2.setClassLoader(MediaBrowserCompat.class.getClassLoader());
         }

         if (var1 == 0 && var2 != null && var2.containsKey("media_item")) {
            Parcelable var3 = var2.getParcelable("media_item");
            if (var3 != null && !(var3 instanceof MediaBrowserCompat.MediaItem)) {
               this.mCallback.onError(this.mMediaId);
            } else {
               this.mCallback.onItemLoaded((MediaBrowserCompat.MediaItem)var3);
            }
         } else {
            this.mCallback.onError(this.mMediaId);
         }

      }
   }

   interface MediaBrowserImpl {
      void connect();

      void disconnect();

      @Nullable
      Bundle getExtras();

      void getItem(@NonNull String var1, @NonNull MediaBrowserCompat.ItemCallback var2);

      @NonNull
      String getRoot();

      ComponentName getServiceComponent();

      @NonNull
      MediaSessionCompat.Token getSessionToken();

      boolean isConnected();

      void search(@NonNull String var1, Bundle var2, @NonNull MediaBrowserCompat.SearchCallback var3);

      void subscribe(@NonNull String var1, Bundle var2, @NonNull MediaBrowserCompat.SubscriptionCallback var3);

      void unsubscribe(@NonNull String var1, MediaBrowserCompat.SubscriptionCallback var2);
   }

   static class MediaBrowserImplApi21 implements MediaBrowserCompat.MediaBrowserImpl, MediaBrowserCompat.MediaBrowserServiceCallbackImpl, MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal {
      protected final Object mBrowserObj;
      protected Messenger mCallbacksMessenger;
      protected final MediaBrowserCompat.CallbackHandler mHandler = new MediaBrowserCompat.CallbackHandler(this);
      protected final Bundle mRootHints;
      protected MediaBrowserCompat.ServiceBinderWrapper mServiceBinderWrapper;
      private final ArrayMap mSubscriptions = new ArrayMap();

      public MediaBrowserImplApi21(Context var1, ComponentName var2, MediaBrowserCompat.ConnectionCallback var3, Bundle var4) {
         if (VERSION.SDK_INT <= 25) {
            Bundle var5 = var4;
            if (var4 == null) {
               var5 = new Bundle();
            }

            var5.putInt("extra_client_version", 1);
            this.mRootHints = new Bundle(var5);
         } else {
            if (var4 == null) {
               var4 = null;
            } else {
               var4 = new Bundle(var4);
            }

            this.mRootHints = var4;
         }

         var3.setInternalConnectionCallback(this);
         this.mBrowserObj = MediaBrowserCompatApi21.createBrowser(var1, var2, var3.mConnectionCallbackObj, this.mRootHints);
      }

      public void connect() {
         MediaBrowserCompatApi21.connect(this.mBrowserObj);
      }

      public void disconnect() {
         if (this.mServiceBinderWrapper != null && this.mCallbacksMessenger != null) {
            try {
               this.mServiceBinderWrapper.unregisterCallbackMessenger(this.mCallbacksMessenger);
            } catch (RemoteException var2) {
               Log.i("MediaBrowserCompat", "Remote error unregistering client messenger.");
            }
         }

         MediaBrowserCompatApi21.disconnect(this.mBrowserObj);
      }

      @Nullable
      public Bundle getExtras() {
         return MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
      }

      public void getItem(@NonNull final String var1, @NonNull final MediaBrowserCompat.ItemCallback var2) {
         if (TextUtils.isEmpty(var1)) {
            throw new IllegalArgumentException("mediaId is empty");
         } else if (var2 == null) {
            throw new IllegalArgumentException("cb is null");
         } else {
            if (!MediaBrowserCompatApi21.isConnected(this.mBrowserObj)) {
               Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var2.onError(var1);
                  }
               });
            } else if (this.mServiceBinderWrapper == null) {
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var2.onError(var1);
                  }
               });
            } else {
               MediaBrowserCompat.ItemReceiver var3 = new MediaBrowserCompat.ItemReceiver(var1, var2, this.mHandler);

               try {
                  this.mServiceBinderWrapper.getMediaItem(var1, var3, this.mCallbacksMessenger);
               } catch (RemoteException var4) {
                  Log.i("MediaBrowserCompat", "Remote error getting media item: " + var1);
                  this.mHandler.post(new Runnable() {
                     public void run() {
                        var2.onError(var1);
                     }
                  });
               }
            }

         }
      }

      @NonNull
      public String getRoot() {
         return MediaBrowserCompatApi21.getRoot(this.mBrowserObj);
      }

      public ComponentName getServiceComponent() {
         return MediaBrowserCompatApi21.getServiceComponent(this.mBrowserObj);
      }

      @NonNull
      public MediaSessionCompat.Token getSessionToken() {
         return MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj));
      }

      public boolean isConnected() {
         return MediaBrowserCompatApi21.isConnected(this.mBrowserObj);
      }

      public void onConnected() {
         Bundle var1 = MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
         if (var1 != null) {
            IBinder var3 = BundleCompat.getBinder(var1, "extra_messenger");
            if (var3 != null) {
               this.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(var3, this.mRootHints);
               this.mCallbacksMessenger = new Messenger(this.mHandler);
               this.mHandler.setCallbacksMessenger(this.mCallbacksMessenger);

               try {
                  this.mServiceBinderWrapper.registerCallbackMessenger(this.mCallbacksMessenger);
               } catch (RemoteException var2) {
                  Log.i("MediaBrowserCompat", "Remote error registering client messenger.");
               }
            }
         }

      }

      public void onConnectionFailed() {
      }

      public void onConnectionFailed(Messenger var1) {
      }

      public void onConnectionSuspended() {
         this.mServiceBinderWrapper = null;
         this.mCallbacksMessenger = null;
         this.mHandler.setCallbacksMessenger((Messenger)null);
      }

      public void onLoadChildren(Messenger var1, String var2, List var3, Bundle var4) {
         if (this.mCallbacksMessenger == var1) {
            MediaBrowserCompat.Subscription var5 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var2);
            if (var5 == null) {
               if (MediaBrowserCompat.DEBUG) {
                  Log.d("MediaBrowserCompat", "onLoadChildren for id that isn't subscribed id=" + var2);
               }
            } else {
               MediaBrowserCompat.SubscriptionCallback var6 = var5.getCallback(var4);
               if (var6 != null) {
                  if (var4 == null) {
                     if (var3 == null) {
                        var6.onError(var2);
                     } else {
                        var6.onChildrenLoaded(var2, var3);
                     }
                  } else if (var3 == null) {
                     var6.onError(var2, var4);
                  } else {
                     var6.onChildrenLoaded(var2, var3, var4);
                  }
               }
            }
         }

      }

      public void onServiceConnected(Messenger var1, String var2, MediaSessionCompat.Token var3, Bundle var4) {
      }

      public void search(@NonNull final String var1, final Bundle var2, @NonNull final MediaBrowserCompat.SearchCallback var3) {
         if (!this.isConnected()) {
            Log.i("MediaBrowserCompat", "Not connected, unable to search.");
            this.mHandler.post(new Runnable() {
               public void run() {
                  var3.onError(var1, var2);
               }
            });
         } else if (this.mServiceBinderWrapper == null) {
            Log.i("MediaBrowserCompat", "The connected service doesn't support search.");
            this.mHandler.post(new Runnable() {
               public void run() {
                  var3.onError(var1, var2);
               }
            });
         } else {
            MediaBrowserCompat.SearchResultReceiver var4 = new MediaBrowserCompat.SearchResultReceiver(var1, var2, var3, this.mHandler);

            try {
               this.mServiceBinderWrapper.search(var1, var2, var4, this.mCallbacksMessenger);
            } catch (RemoteException var5) {
               Log.i("MediaBrowserCompat", "Remote error searching items with query: " + var1, var5);
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var3.onError(var1, var2);
                  }
               });
            }
         }

      }

      public void subscribe(@NonNull String var1, Bundle var2, @NonNull MediaBrowserCompat.SubscriptionCallback var3) {
         MediaBrowserCompat.Subscription var4 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var1);
         MediaBrowserCompat.Subscription var5 = var4;
         if (var4 == null) {
            var5 = new MediaBrowserCompat.Subscription();
            this.mSubscriptions.put(var1, var5);
         }

         var3.setSubscription(var5);
         if (var2 == null) {
            var2 = null;
         } else {
            var2 = new Bundle(var2);
         }

         var5.putCallback(var2, var3);
         if (this.mServiceBinderWrapper == null) {
            MediaBrowserCompatApi21.subscribe(this.mBrowserObj, var1, var3.mSubscriptionCallbackObj);
         } else {
            try {
               this.mServiceBinderWrapper.addSubscription(var1, var3.mToken, var2, this.mCallbacksMessenger);
            } catch (RemoteException var6) {
               Log.i("MediaBrowserCompat", "Remote error subscribing media item: " + var1);
            }
         }

      }

      public void unsubscribe(@NonNull String var1, MediaBrowserCompat.SubscriptionCallback var2) {
         MediaBrowserCompat.Subscription var3 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var1);
         if (var3 != null) {
            List var4;
            List var5;
            int var6;
            if (this.mServiceBinderWrapper == null) {
               if (var2 == null) {
                  MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, var1);
               } else {
                  var4 = var3.getCallbacks();
                  var5 = var3.getOptionsList();

                  for(var6 = var4.size() - 1; var6 >= 0; --var6) {
                     if (var4.get(var6) == var2) {
                        var4.remove(var6);
                        var5.remove(var6);
                     }
                  }

                  if (var4.size() == 0) {
                     MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, var1);
                  }
               }
            } else {
               label78: {
                  boolean var10001;
                  if (var2 == null) {
                     try {
                        this.mServiceBinderWrapper.removeSubscription(var1, (IBinder)null, this.mCallbacksMessenger);
                        break label78;
                     } catch (RemoteException var7) {
                        var10001 = false;
                     }
                  } else {
                     label66: {
                        try {
                           var4 = var3.getCallbacks();
                           var5 = var3.getOptionsList();
                           var6 = var4.size() - 1;
                        } catch (RemoteException var9) {
                           var10001 = false;
                           break label66;
                        }

                        while(true) {
                           if (var6 < 0) {
                              break label78;
                           }

                           try {
                              if (var4.get(var6) == var2) {
                                 this.mServiceBinderWrapper.removeSubscription(var1, var2.mToken, this.mCallbacksMessenger);
                                 var4.remove(var6);
                                 var5.remove(var6);
                              }
                           } catch (RemoteException var8) {
                              var10001 = false;
                              break;
                           }

                           --var6;
                        }
                     }
                  }

                  Log.d("MediaBrowserCompat", "removeSubscription failed with RemoteException parentId=" + var1);
               }
            }

            if (var3.isEmpty() || var2 == null) {
               this.mSubscriptions.remove(var1);
            }
         }

      }
   }

   static class MediaBrowserImplApi23 extends MediaBrowserCompat.MediaBrowserImplApi21 {
      public MediaBrowserImplApi23(Context var1, ComponentName var2, MediaBrowserCompat.ConnectionCallback var3, Bundle var4) {
         super(var1, var2, var3, var4);
      }

      public void getItem(@NonNull String var1, @NonNull MediaBrowserCompat.ItemCallback var2) {
         if (this.mServiceBinderWrapper == null) {
            MediaBrowserCompatApi23.getItem(this.mBrowserObj, var1, var2.mItemCallbackObj);
         } else {
            super.getItem(var1, var2);
         }

      }
   }

   static class MediaBrowserImplApi24 extends MediaBrowserCompat.MediaBrowserImplApi23 {
      public MediaBrowserImplApi24(Context var1, ComponentName var2, MediaBrowserCompat.ConnectionCallback var3, Bundle var4) {
         super(var1, var2, var3, var4);
      }

      public void subscribe(@NonNull String var1, @NonNull Bundle var2, @NonNull MediaBrowserCompat.SubscriptionCallback var3) {
         if (var2 == null) {
            MediaBrowserCompatApi21.subscribe(this.mBrowserObj, var1, var3.mSubscriptionCallbackObj);
         } else {
            MediaBrowserCompatApi24.subscribe(this.mBrowserObj, var1, var2, var3.mSubscriptionCallbackObj);
         }

      }

      public void unsubscribe(@NonNull String var1, MediaBrowserCompat.SubscriptionCallback var2) {
         if (var2 == null) {
            MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, var1);
         } else {
            MediaBrowserCompatApi24.unsubscribe(this.mBrowserObj, var1, var2.mSubscriptionCallbackObj);
         }

      }
   }

   static class MediaBrowserImplBase implements MediaBrowserCompat.MediaBrowserImpl, MediaBrowserCompat.MediaBrowserServiceCallbackImpl {
      private static final int CONNECT_STATE_CONNECTED = 2;
      static final int CONNECT_STATE_CONNECTING = 1;
      static final int CONNECT_STATE_DISCONNECTED = 0;
      static final int CONNECT_STATE_SUSPENDED = 3;
      final MediaBrowserCompat.ConnectionCallback mCallback;
      Messenger mCallbacksMessenger;
      final Context mContext;
      private Bundle mExtras;
      final MediaBrowserCompat.CallbackHandler mHandler = new MediaBrowserCompat.CallbackHandler(this);
      private MediaSessionCompat.Token mMediaSessionToken;
      final Bundle mRootHints;
      private String mRootId;
      MediaBrowserCompat.ServiceBinderWrapper mServiceBinderWrapper;
      final ComponentName mServiceComponent;
      MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection mServiceConnection;
      int mState = 0;
      private final ArrayMap mSubscriptions = new ArrayMap();

      public MediaBrowserImplBase(Context var1, ComponentName var2, MediaBrowserCompat.ConnectionCallback var3, Bundle var4) {
         if (var1 == null) {
            throw new IllegalArgumentException("context must not be null");
         } else if (var2 == null) {
            throw new IllegalArgumentException("service component must not be null");
         } else if (var3 == null) {
            throw new IllegalArgumentException("connection callback must not be null");
         } else {
            this.mContext = var1;
            this.mServiceComponent = var2;
            this.mCallback = var3;
            Bundle var5;
            if (var4 == null) {
               var5 = null;
            } else {
               var5 = new Bundle(var4);
            }

            this.mRootHints = var5;
         }
      }

      private static String getStateLabel(int var0) {
         String var1;
         switch(var0) {
         case 0:
            var1 = "CONNECT_STATE_DISCONNECTED";
            break;
         case 1:
            var1 = "CONNECT_STATE_CONNECTING";
            break;
         case 2:
            var1 = "CONNECT_STATE_CONNECTED";
            break;
         case 3:
            var1 = "CONNECT_STATE_SUSPENDED";
            break;
         default:
            var1 = "UNKNOWN/" + var0;
         }

         return var1;
      }

      private boolean isCurrent(Messenger var1, String var2) {
         boolean var3;
         if (this.mCallbacksMessenger != var1) {
            if (this.mState != 0) {
               Log.i("MediaBrowserCompat", var2 + " for " + this.mServiceComponent + " with mCallbacksMessenger=" + this.mCallbacksMessenger + " this=" + this);
            }

            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      public void connect() {
         if (this.mState != 0) {
            throw new IllegalStateException("connect() called while not disconnected (state=" + getStateLabel(this.mState) + ")");
         } else if (MediaBrowserCompat.DEBUG && this.mServiceConnection != null) {
            throw new RuntimeException("mServiceConnection should be null. Instead it is " + this.mServiceConnection);
         } else if (this.mServiceBinderWrapper != null) {
            throw new RuntimeException("mServiceBinderWrapper should be null. Instead it is " + this.mServiceBinderWrapper);
         } else if (this.mCallbacksMessenger != null) {
            throw new RuntimeException("mCallbacksMessenger should be null. Instead it is " + this.mCallbacksMessenger);
         } else {
            this.mState = 1;
            Intent var1 = new Intent("android.media.browse.MediaBrowserService");
            var1.setComponent(this.mServiceComponent);
            final MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection var2 = new MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection();
            this.mServiceConnection = var2;
            boolean var3 = false;

            label33: {
               boolean var4;
               try {
                  var4 = this.mContext.bindService(var1, this.mServiceConnection, 1);
               } catch (Exception var5) {
                  Log.e("MediaBrowserCompat", "Failed binding to service " + this.mServiceComponent);
                  break label33;
               }

               var3 = var4;
            }

            if (!var3) {
               this.mHandler.post(new Runnable() {
                  public void run() {
                     if (var2 == MediaBrowserImplBase.this.mServiceConnection) {
                        MediaBrowserImplBase.this.forceCloseConnection();
                        MediaBrowserImplBase.this.mCallback.onConnectionFailed();
                     }

                  }
               });
            }

            if (MediaBrowserCompat.DEBUG) {
               Log.d("MediaBrowserCompat", "connect...");
               this.dump();
            }

         }
      }

      public void disconnect() {
         if (this.mCallbacksMessenger != null) {
            try {
               this.mServiceBinderWrapper.disconnect(this.mCallbacksMessenger);
            } catch (RemoteException var2) {
               Log.w("MediaBrowserCompat", "RemoteException during connect for " + this.mServiceComponent);
            }
         }

         this.forceCloseConnection();
         if (MediaBrowserCompat.DEBUG) {
            Log.d("MediaBrowserCompat", "disconnect...");
            this.dump();
         }

      }

      void dump() {
         Log.d("MediaBrowserCompat", "MediaBrowserCompat...");
         Log.d("MediaBrowserCompat", "  mServiceComponent=" + this.mServiceComponent);
         Log.d("MediaBrowserCompat", "  mCallback=" + this.mCallback);
         Log.d("MediaBrowserCompat", "  mRootHints=" + this.mRootHints);
         Log.d("MediaBrowserCompat", "  mState=" + getStateLabel(this.mState));
         Log.d("MediaBrowserCompat", "  mServiceConnection=" + this.mServiceConnection);
         Log.d("MediaBrowserCompat", "  mServiceBinderWrapper=" + this.mServiceBinderWrapper);
         Log.d("MediaBrowserCompat", "  mCallbacksMessenger=" + this.mCallbacksMessenger);
         Log.d("MediaBrowserCompat", "  mRootId=" + this.mRootId);
         Log.d("MediaBrowserCompat", "  mMediaSessionToken=" + this.mMediaSessionToken);
      }

      void forceCloseConnection() {
         if (this.mServiceConnection != null) {
            this.mContext.unbindService(this.mServiceConnection);
         }

         this.mState = 0;
         this.mServiceConnection = null;
         this.mServiceBinderWrapper = null;
         this.mCallbacksMessenger = null;
         this.mHandler.setCallbacksMessenger((Messenger)null);
         this.mRootId = null;
         this.mMediaSessionToken = null;
      }

      @Nullable
      public Bundle getExtras() {
         if (!this.isConnected()) {
            throw new IllegalStateException("getExtras() called while not connected (state=" + getStateLabel(this.mState) + ")");
         } else {
            return this.mExtras;
         }
      }

      public void getItem(@NonNull final String var1, @NonNull final MediaBrowserCompat.ItemCallback var2) {
         if (TextUtils.isEmpty(var1)) {
            throw new IllegalArgumentException("mediaId is empty");
         } else if (var2 == null) {
            throw new IllegalArgumentException("cb is null");
         } else {
            if (this.mState != 2) {
               Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var2.onError(var1);
                  }
               });
            } else {
               MediaBrowserCompat.ItemReceiver var3 = new MediaBrowserCompat.ItemReceiver(var1, var2, this.mHandler);

               try {
                  this.mServiceBinderWrapper.getMediaItem(var1, var3, this.mCallbacksMessenger);
               } catch (RemoteException var4) {
                  Log.i("MediaBrowserCompat", "Remote error getting media item.");
                  this.mHandler.post(new Runnable() {
                     public void run() {
                        var2.onError(var1);
                     }
                  });
               }
            }

         }
      }

      @NonNull
      public String getRoot() {
         if (!this.isConnected()) {
            throw new IllegalStateException("getRoot() called while not connected(state=" + getStateLabel(this.mState) + ")");
         } else {
            return this.mRootId;
         }
      }

      @NonNull
      public ComponentName getServiceComponent() {
         if (!this.isConnected()) {
            throw new IllegalStateException("getServiceComponent() called while not connected (state=" + this.mState + ")");
         } else {
            return this.mServiceComponent;
         }
      }

      @NonNull
      public MediaSessionCompat.Token getSessionToken() {
         if (!this.isConnected()) {
            throw new IllegalStateException("getSessionToken() called while not connected(state=" + this.mState + ")");
         } else {
            return this.mMediaSessionToken;
         }
      }

      public boolean isConnected() {
         boolean var1;
         if (this.mState == 2) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public void onConnectionFailed(Messenger var1) {
         Log.e("MediaBrowserCompat", "onConnectFailed for " + this.mServiceComponent);
         if (this.isCurrent(var1, "onConnectFailed")) {
            if (this.mState != 1) {
               Log.w("MediaBrowserCompat", "onConnect from service while mState=" + getStateLabel(this.mState) + "... ignoring");
            } else {
               this.forceCloseConnection();
               this.mCallback.onConnectionFailed();
            }
         }

      }

      public void onLoadChildren(Messenger var1, String var2, List var3, Bundle var4) {
         if (this.isCurrent(var1, "onLoadChildren")) {
            if (MediaBrowserCompat.DEBUG) {
               Log.d("MediaBrowserCompat", "onLoadChildren for " + this.mServiceComponent + " id=" + var2);
            }

            MediaBrowserCompat.Subscription var5 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var2);
            if (var5 == null) {
               if (MediaBrowserCompat.DEBUG) {
                  Log.d("MediaBrowserCompat", "onLoadChildren for id that isn't subscribed id=" + var2);
               }
            } else {
               MediaBrowserCompat.SubscriptionCallback var6 = var5.getCallback(var4);
               if (var6 != null) {
                  if (var4 == null) {
                     if (var3 == null) {
                        var6.onError(var2);
                     } else {
                        var6.onChildrenLoaded(var2, var3);
                     }
                  } else if (var3 == null) {
                     var6.onError(var2, var4);
                  } else {
                     var6.onChildrenLoaded(var2, var3, var4);
                  }
               }
            }
         }

      }

      public void onServiceConnected(Messenger var1, String var2, MediaSessionCompat.Token var3, Bundle var4) {
         if (this.isCurrent(var1, "onConnect")) {
            if (this.mState != 1) {
               Log.w("MediaBrowserCompat", "onConnect from service while mState=" + getStateLabel(this.mState) + "... ignoring");
            } else {
               this.mRootId = var2;
               this.mMediaSessionToken = var3;
               this.mExtras = var4;
               this.mState = 2;
               if (MediaBrowserCompat.DEBUG) {
                  Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                  this.dump();
               }

               this.mCallback.onConnected();

               label41: {
                  boolean var10001;
                  Iterator var9;
                  try {
                     var9 = this.mSubscriptions.entrySet().iterator();
                  } catch (RemoteException var8) {
                     var10001 = false;
                     break label41;
                  }

                  label38:
                  while(true) {
                     List var11;
                     List var13;
                     try {
                        if (!var9.hasNext()) {
                           return;
                        }

                        Entry var10 = (Entry)var9.next();
                        var2 = (String)var10.getKey();
                        MediaBrowserCompat.Subscription var12 = (MediaBrowserCompat.Subscription)var10.getValue();
                        var11 = var12.getCallbacks();
                        var13 = var12.getOptionsList();
                     } catch (RemoteException var7) {
                        var10001 = false;
                        break;
                     }

                     int var5 = 0;

                     while(true) {
                        try {
                           if (var5 >= var11.size()) {
                              break;
                           }

                           this.mServiceBinderWrapper.addSubscription(var2, ((MediaBrowserCompat.SubscriptionCallback)var11.get(var5)).mToken, (Bundle)var13.get(var5), this.mCallbacksMessenger);
                        } catch (RemoteException var6) {
                           var10001 = false;
                           break label38;
                        }

                        ++var5;
                     }
                  }
               }

               Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException.");
            }
         }

      }

      public void search(@NonNull final String var1, final Bundle var2, @NonNull final MediaBrowserCompat.SearchCallback var3) {
         if (!this.isConnected()) {
            Log.i("MediaBrowserCompat", "Not connected, unable to search.");
            this.mHandler.post(new Runnable() {
               public void run() {
                  var3.onError(var1, var2);
               }
            });
         } else {
            MediaBrowserCompat.SearchResultReceiver var4 = new MediaBrowserCompat.SearchResultReceiver(var1, var2, var3, this.mHandler);

            try {
               this.mServiceBinderWrapper.search(var1, var2, var4, this.mCallbacksMessenger);
            } catch (RemoteException var5) {
               Log.i("MediaBrowserCompat", "Remote error searching items with query: " + var1, var5);
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var3.onError(var1, var2);
                  }
               });
            }
         }

      }

      public void subscribe(@NonNull String var1, Bundle var2, @NonNull MediaBrowserCompat.SubscriptionCallback var3) {
         MediaBrowserCompat.Subscription var4 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var1);
         MediaBrowserCompat.Subscription var5 = var4;
         if (var4 == null) {
            var5 = new MediaBrowserCompat.Subscription();
            this.mSubscriptions.put(var1, var5);
         }

         if (var2 == null) {
            var2 = null;
         } else {
            var2 = new Bundle(var2);
         }

         var5.putCallback(var2, var3);
         if (this.mState == 2) {
            try {
               this.mServiceBinderWrapper.addSubscription(var1, var3.mToken, var2, this.mCallbacksMessenger);
            } catch (RemoteException var6) {
               Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException parentId=" + var1);
            }
         }

      }

      public void unsubscribe(@NonNull String var1, MediaBrowserCompat.SubscriptionCallback var2) {
         MediaBrowserCompat.Subscription var3 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var1);
         if (var3 != null) {
            label55: {
               boolean var10001;
               if (var2 == null) {
                  try {
                     if (this.mState == 2) {
                        this.mServiceBinderWrapper.removeSubscription(var1, (IBinder)null, this.mCallbacksMessenger);
                     }
                     break label55;
                  } catch (RemoteException var7) {
                     var10001 = false;
                  }
               } else {
                  label53: {
                     List var4;
                     List var5;
                     int var6;
                     try {
                        var4 = var3.getCallbacks();
                        var5 = var3.getOptionsList();
                        var6 = var4.size() - 1;
                     } catch (RemoteException var10) {
                        var10001 = false;
                        break label53;
                     }

                     while(true) {
                        if (var6 < 0) {
                           break label55;
                        }

                        label47: {
                           try {
                              if (var4.get(var6) != var2) {
                                 break label47;
                              }

                              if (this.mState == 2) {
                                 this.mServiceBinderWrapper.removeSubscription(var1, var2.mToken, this.mCallbacksMessenger);
                              }
                           } catch (RemoteException var9) {
                              var10001 = false;
                              break;
                           }

                           try {
                              var4.remove(var6);
                              var5.remove(var6);
                           } catch (RemoteException var8) {
                              var10001 = false;
                              break;
                           }
                        }

                        --var6;
                     }
                  }
               }

               Log.d("MediaBrowserCompat", "removeSubscription failed with RemoteException parentId=" + var1);
            }

            if (var3.isEmpty() || var2 == null) {
               this.mSubscriptions.remove(var1);
            }
         }

      }

      private class MediaServiceConnection implements ServiceConnection {
         MediaServiceConnection() {
         }

         private void postOrRun(Runnable var1) {
            if (Thread.currentThread() == MediaBrowserImplBase.this.mHandler.getLooper().getThread()) {
               var1.run();
            } else {
               MediaBrowserImplBase.this.mHandler.post(var1);
            }

         }

         boolean isCurrent(String var1) {
            boolean var2;
            if (MediaBrowserImplBase.this.mServiceConnection != this) {
               if (MediaBrowserImplBase.this.mState != 0) {
                  Log.i("MediaBrowserCompat", var1 + " for " + MediaBrowserImplBase.this.mServiceComponent + " with mServiceConnection=" + MediaBrowserImplBase.this.mServiceConnection + " this=" + this);
               }

               var2 = false;
            } else {
               var2 = true;
            }

            return var2;
         }

         public void onServiceConnected(final ComponentName var1, final IBinder var2) {
            this.postOrRun(new Runnable() {
               public void run() {
                  if (MediaBrowserCompat.DEBUG) {
                     Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceConnected name=" + var1 + " binder=" + var2);
                     MediaBrowserImplBase.this.dump();
                  }

                  if (MediaServiceConnection.this.isCurrent("onServiceConnected")) {
                     MediaBrowserImplBase.this.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(var2, MediaBrowserImplBase.this.mRootHints);
                     MediaBrowserImplBase.this.mCallbacksMessenger = new Messenger(MediaBrowserImplBase.this.mHandler);
                     MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(MediaBrowserImplBase.this.mCallbacksMessenger);
                     MediaBrowserImplBase.this.mState = 1;

                     try {
                        if (MediaBrowserCompat.DEBUG) {
                           Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                           MediaBrowserImplBase.this.dump();
                        }

                        MediaBrowserImplBase.this.mServiceBinderWrapper.connect(MediaBrowserImplBase.this.mContext, MediaBrowserImplBase.this.mCallbacksMessenger);
                     } catch (RemoteException var2x) {
                        Log.w("MediaBrowserCompat", "RemoteException during connect for " + MediaBrowserImplBase.this.mServiceComponent);
                        if (MediaBrowserCompat.DEBUG) {
                           Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                           MediaBrowserImplBase.this.dump();
                        }
                     }
                  }

               }
            });
         }

         public void onServiceDisconnected(final ComponentName var1) {
            this.postOrRun(new Runnable() {
               public void run() {
                  if (MediaBrowserCompat.DEBUG) {
                     Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceDisconnected name=" + var1 + " this=" + this + " mServiceConnection=" + MediaBrowserImplBase.this.mServiceConnection);
                     MediaBrowserImplBase.this.dump();
                  }

                  if (MediaServiceConnection.this.isCurrent("onServiceDisconnected")) {
                     MediaBrowserImplBase.this.mServiceBinderWrapper = null;
                     MediaBrowserImplBase.this.mCallbacksMessenger = null;
                     MediaBrowserImplBase.this.mHandler.setCallbacksMessenger((Messenger)null);
                     MediaBrowserImplBase.this.mState = 3;
                     MediaBrowserImplBase.this.mCallback.onConnectionSuspended();
                  }

               }
            });
         }
      }
   }

   interface MediaBrowserServiceCallbackImpl {
      void onConnectionFailed(Messenger var1);

      void onLoadChildren(Messenger var1, String var2, List var3, Bundle var4);

      void onServiceConnected(Messenger var1, String var2, MediaSessionCompat.Token var3, Bundle var4);
   }

   public static class MediaItem implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public MediaBrowserCompat.MediaItem createFromParcel(Parcel var1) {
            return new MediaBrowserCompat.MediaItem(var1);
         }

         public MediaBrowserCompat.MediaItem[] newArray(int var1) {
            return new MediaBrowserCompat.MediaItem[var1];
         }
      };
      public static final int FLAG_BROWSABLE = 1;
      public static final int FLAG_PLAYABLE = 2;
      private final MediaDescriptionCompat mDescription;
      private final int mFlags;

      MediaItem(Parcel var1) {
         this.mFlags = var1.readInt();
         this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(var1);
      }

      public MediaItem(@NonNull MediaDescriptionCompat var1, int var2) {
         if (var1 == null) {
            throw new IllegalArgumentException("description cannot be null");
         } else if (TextUtils.isEmpty(var1.getMediaId())) {
            throw new IllegalArgumentException("description must have a non-empty media id");
         } else {
            this.mFlags = var2;
            this.mDescription = var1;
         }
      }

      public static MediaBrowserCompat.MediaItem fromMediaItem(Object var0) {
         MediaBrowserCompat.MediaItem var2;
         if (var0 != null && VERSION.SDK_INT >= 21) {
            int var1 = MediaBrowserCompatApi21.MediaItem.getFlags(var0);
            var2 = new MediaBrowserCompat.MediaItem(MediaDescriptionCompat.fromMediaDescription(MediaBrowserCompatApi21.MediaItem.getDescription(var0)), var1);
         } else {
            var2 = null;
         }

         return var2;
      }

      public static List fromMediaItemList(List var0) {
         ArrayList var3;
         if (var0 != null && VERSION.SDK_INT >= 21) {
            ArrayList var1 = new ArrayList(var0.size());
            Iterator var2 = var0.iterator();

            while(true) {
               var3 = var1;
               if (!var2.hasNext()) {
                  break;
               }

               var1.add(fromMediaItem(var2.next()));
            }
         } else {
            var3 = null;
         }

         return var3;
      }

      public int describeContents() {
         return 0;
      }

      @NonNull
      public MediaDescriptionCompat getDescription() {
         return this.mDescription;
      }

      public int getFlags() {
         return this.mFlags;
      }

      @Nullable
      public String getMediaId() {
         return this.mDescription.getMediaId();
      }

      public boolean isBrowsable() {
         boolean var1;
         if ((this.mFlags & 1) != 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public boolean isPlayable() {
         boolean var1;
         if ((this.mFlags & 2) != 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("MediaItem{");
         var1.append("mFlags=").append(this.mFlags);
         var1.append(", mDescription=").append(this.mDescription);
         var1.append('}');
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.mFlags);
         this.mDescription.writeToParcel(var1, var2);
      }

      @Retention(RetentionPolicy.SOURCE)
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public @interface Flags {
      }
   }

   public abstract static class SearchCallback {
      public void onError(@NonNull String var1, Bundle var2) {
      }

      public void onSearchResult(@NonNull String var1, Bundle var2, @NonNull List var3) {
      }
   }

   private static class SearchResultReceiver extends ResultReceiver {
      private final MediaBrowserCompat.SearchCallback mCallback;
      private final Bundle mExtras;
      private final String mQuery;

      SearchResultReceiver(String var1, Bundle var2, MediaBrowserCompat.SearchCallback var3, Handler var4) {
         super(var4);
         this.mQuery = var1;
         this.mExtras = var2;
         this.mCallback = var3;
      }

      protected void onReceiveResult(int var1, Bundle var2) {
         if (var1 == 0 && var2 != null && var2.containsKey("search_results")) {
            Parcelable[] var3 = var2.getParcelableArray("search_results");
            ArrayList var6 = null;
            if (var3 != null) {
               ArrayList var4 = new ArrayList();
               int var5 = var3.length;
               var1 = 0;

               while(true) {
                  var6 = var4;
                  if (var1 >= var5) {
                     break;
                  }

                  var4.add((MediaBrowserCompat.MediaItem)var3[var1]);
                  ++var1;
               }
            }

            this.mCallback.onSearchResult(this.mQuery, this.mExtras, var6);
         } else {
            this.mCallback.onError(this.mQuery, this.mExtras);
         }

      }
   }

   private static class ServiceBinderWrapper {
      private Messenger mMessenger;
      private Bundle mRootHints;

      public ServiceBinderWrapper(IBinder var1, Bundle var2) {
         this.mMessenger = new Messenger(var1);
         this.mRootHints = var2;
      }

      private void sendRequest(int var1, Bundle var2, Messenger var3) throws RemoteException {
         Message var4 = Message.obtain();
         var4.what = var1;
         var4.arg1 = 1;
         var4.setData(var2);
         var4.replyTo = var3;
         this.mMessenger.send(var4);
      }

      void addSubscription(String var1, IBinder var2, Bundle var3, Messenger var4) throws RemoteException {
         Bundle var5 = new Bundle();
         var5.putString("data_media_item_id", var1);
         BundleCompat.putBinder(var5, "data_callback_token", var2);
         var5.putBundle("data_options", var3);
         this.sendRequest(3, var5, var4);
      }

      void connect(Context var1, Messenger var2) throws RemoteException {
         Bundle var3 = new Bundle();
         var3.putString("data_package_name", var1.getPackageName());
         var3.putBundle("data_root_hints", this.mRootHints);
         this.sendRequest(1, var3, var2);
      }

      void disconnect(Messenger var1) throws RemoteException {
         this.sendRequest(2, (Bundle)null, var1);
      }

      void getMediaItem(String var1, ResultReceiver var2, Messenger var3) throws RemoteException {
         Bundle var4 = new Bundle();
         var4.putString("data_media_item_id", var1);
         var4.putParcelable("data_result_receiver", var2);
         this.sendRequest(5, var4, var3);
      }

      void registerCallbackMessenger(Messenger var1) throws RemoteException {
         Bundle var2 = new Bundle();
         var2.putBundle("data_root_hints", this.mRootHints);
         this.sendRequest(6, var2, var1);
      }

      void removeSubscription(String var1, IBinder var2, Messenger var3) throws RemoteException {
         Bundle var4 = new Bundle();
         var4.putString("data_media_item_id", var1);
         BundleCompat.putBinder(var4, "data_callback_token", var2);
         this.sendRequest(4, var4, var3);
      }

      void search(String var1, Bundle var2, ResultReceiver var3, Messenger var4) throws RemoteException {
         Bundle var5 = new Bundle();
         var5.putString("data_search_query", var1);
         var5.putBundle("data_search_extras", var2);
         var5.putParcelable("data_result_receiver", var3);
         this.sendRequest(8, var5, var4);
      }

      void unregisterCallbackMessenger(Messenger var1) throws RemoteException {
         this.sendRequest(7, (Bundle)null, var1);
      }
   }

   private static class Subscription {
      private final List mCallbacks = new ArrayList();
      private final List mOptionsList = new ArrayList();

      public Subscription() {
      }

      public MediaBrowserCompat.SubscriptionCallback getCallback(Bundle var1) {
         int var2 = 0;

         MediaBrowserCompat.SubscriptionCallback var3;
         while(true) {
            if (var2 >= this.mOptionsList.size()) {
               var3 = null;
               break;
            }

            if (MediaBrowserCompatUtils.areSameOptions((Bundle)this.mOptionsList.get(var2), var1)) {
               var3 = (MediaBrowserCompat.SubscriptionCallback)this.mCallbacks.get(var2);
               break;
            }

            ++var2;
         }

         return var3;
      }

      public List getCallbacks() {
         return this.mCallbacks;
      }

      public List getOptionsList() {
         return this.mOptionsList;
      }

      public boolean isEmpty() {
         return this.mCallbacks.isEmpty();
      }

      public void putCallback(Bundle var1, MediaBrowserCompat.SubscriptionCallback var2) {
         int var3 = 0;

         while(true) {
            if (var3 >= this.mOptionsList.size()) {
               this.mCallbacks.add(var2);
               this.mOptionsList.add(var1);
               break;
            }

            if (MediaBrowserCompatUtils.areSameOptions((Bundle)this.mOptionsList.get(var3), var1)) {
               this.mCallbacks.set(var3, var2);
               break;
            }

            ++var3;
         }

      }
   }

   public abstract static class SubscriptionCallback {
      private final Object mSubscriptionCallbackObj;
      WeakReference mSubscriptionRef;
      private final IBinder mToken;

      public SubscriptionCallback() {
         if (VERSION.SDK_INT < 26 && !BuildCompat.isAtLeastO()) {
            if (VERSION.SDK_INT >= 21) {
               this.mSubscriptionCallbackObj = MediaBrowserCompatApi21.createSubscriptionCallback(new MediaBrowserCompat.SubscriptionCallback.StubApi21());
               this.mToken = new Binder();
            } else {
               this.mSubscriptionCallbackObj = null;
               this.mToken = new Binder();
            }
         } else {
            this.mSubscriptionCallbackObj = MediaBrowserCompatApi24.createSubscriptionCallback(new MediaBrowserCompat.SubscriptionCallback.StubApi24());
            this.mToken = null;
         }

      }

      private void setSubscription(MediaBrowserCompat.Subscription var1) {
         this.mSubscriptionRef = new WeakReference(var1);
      }

      public void onChildrenLoaded(@NonNull String var1, @NonNull List var2) {
      }

      public void onChildrenLoaded(@NonNull String var1, @NonNull List var2, @NonNull Bundle var3) {
      }

      public void onError(@NonNull String var1) {
      }

      public void onError(@NonNull String var1, @NonNull Bundle var2) {
      }

      private class StubApi21 implements MediaBrowserCompatApi21.SubscriptionCallback {
         StubApi21() {
         }

         List applyOptions(List var1, Bundle var2) {
            List var7;
            if (var1 == null) {
               var7 = null;
            } else {
               int var3 = var2.getInt("android.media.browse.extra.PAGE", -1);
               int var4 = var2.getInt("android.media.browse.extra.PAGE_SIZE", -1);
               if (var3 == -1) {
                  var7 = var1;
                  if (var4 == -1) {
                     return var7;
                  }
               }

               int var5 = var4 * var3;
               int var6 = var5 + var4;
               if (var3 >= 0 && var4 >= 1 && var5 < var1.size()) {
                  var3 = var6;
                  if (var6 > var1.size()) {
                     var3 = var1.size();
                  }

                  var7 = var1.subList(var5, var3);
               } else {
                  var7 = Collections.EMPTY_LIST;
               }
            }

            return var7;
         }

         public void onChildrenLoaded(@NonNull String var1, List var2) {
            MediaBrowserCompat.Subscription var3;
            if (SubscriptionCallback.this.mSubscriptionRef == null) {
               var3 = null;
            } else {
               var3 = (MediaBrowserCompat.Subscription)SubscriptionCallback.this.mSubscriptionRef.get();
            }

            if (var3 == null) {
               SubscriptionCallback.this.onChildrenLoaded(var1, MediaBrowserCompat.MediaItem.fromMediaItemList(var2));
            } else {
               List var4 = MediaBrowserCompat.MediaItem.fromMediaItemList(var2);
               var2 = var3.getCallbacks();
               List var7 = var3.getOptionsList();

               for(int var5 = 0; var5 < var2.size(); ++var5) {
                  Bundle var6 = (Bundle)var7.get(var5);
                  if (var6 == null) {
                     SubscriptionCallback.this.onChildrenLoaded(var1, var4);
                  } else {
                     SubscriptionCallback.this.onChildrenLoaded(var1, this.applyOptions(var4, var6), var6);
                  }
               }
            }

         }

         public void onError(@NonNull String var1) {
            SubscriptionCallback.this.onError(var1);
         }
      }

      private class StubApi24 extends MediaBrowserCompat.SubscriptionCallback.StubApi21 implements MediaBrowserCompatApi24.SubscriptionCallback {
         StubApi24() {
            super();
         }

         public void onChildrenLoaded(@NonNull String var1, List var2, @NonNull Bundle var3) {
            SubscriptionCallback.this.onChildrenLoaded(var1, MediaBrowserCompat.MediaItem.fromMediaItemList(var2), var3);
         }

         public void onError(@NonNull String var1, @NonNull Bundle var2) {
            SubscriptionCallback.this.onError(var1, var2);
         }
      }
   }
}
