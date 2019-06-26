package android.support.v4.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.BadParcelableException;
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
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaSessionCompat;
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
   public static final String CUSTOM_ACTION_DOWNLOAD = "android.support.v4.media.action.DOWNLOAD";
   public static final String CUSTOM_ACTION_REMOVE_DOWNLOADED_FILE = "android.support.v4.media.action.REMOVE_DOWNLOADED_FILE";
   static final boolean DEBUG = Log.isLoggable("MediaBrowserCompat", 3);
   public static final String EXTRA_DOWNLOAD_PROGRESS = "android.media.browse.extra.DOWNLOAD_PROGRESS";
   public static final String EXTRA_MEDIA_ID = "android.media.browse.extra.MEDIA_ID";
   public static final String EXTRA_PAGE = "android.media.browse.extra.PAGE";
   public static final String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";
   static final String TAG = "MediaBrowserCompat";
   private final MediaBrowserCompat.MediaBrowserImpl mImpl;

   public MediaBrowserCompat(Context var1, ComponentName var2, MediaBrowserCompat.ConnectionCallback var3, Bundle var4) {
      if (VERSION.SDK_INT >= 26) {
         this.mImpl = new MediaBrowserCompat.MediaBrowserImplApi24(var1, var2, var3, var4);
      } else if (VERSION.SDK_INT >= 23) {
         this.mImpl = new MediaBrowserCompat.MediaBrowserImplApi23(var1, var2, var3, var4);
      } else if (VERSION.SDK_INT >= 21) {
         this.mImpl = new MediaBrowserCompat.MediaBrowserImplApi21(var1, var2, var3, var4);
      } else {
         this.mImpl = new MediaBrowserCompat.MediaBrowserImplBase(var1, var2, var3, var4);
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

   public void sendCustomAction(@NonNull String var1, Bundle var2, @Nullable MediaBrowserCompat.CustomActionCallback var3) {
      if (TextUtils.isEmpty(var1)) {
         throw new IllegalArgumentException("action cannot be empty");
      } else {
         this.mImpl.sendCustomAction(var1, var2, var3);
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
            MediaBrowserCompat.MediaBrowserServiceCallbackImpl var3 = (MediaBrowserCompat.MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get();
            Messenger var4 = (Messenger)this.mCallbacksMessengerRef.get();

            label63: {
               boolean var10001;
               label62: {
                  label61: {
                     label72: {
                        try {
                           switch(var1.what) {
                           case 1:
                              break label72;
                           case 2:
                              break label61;
                           case 3:
                              break label62;
                           }
                        } catch (BadParcelableException var9) {
                           var10001 = false;
                           break label63;
                        }

                        try {
                           StringBuilder var10 = new StringBuilder();
                           var10.append("Unhandled message: ");
                           var10.append(var1);
                           var10.append("\n  Client version: ");
                           var10.append(1);
                           var10.append("\n  Service version: ");
                           var10.append(var1.arg1);
                           Log.w("MediaBrowserCompat", var10.toString());
                           return;
                        } catch (BadParcelableException var8) {
                           var10001 = false;
                           break label63;
                        }
                     }

                     try {
                        var3.onServiceConnected(var4, var2.getString("data_media_item_id"), (MediaSessionCompat.Token)var2.getParcelable("data_media_session_token"), var2.getBundle("data_root_hints"));
                        return;
                     } catch (BadParcelableException var7) {
                        var10001 = false;
                        break label63;
                     }
                  }

                  try {
                     var3.onConnectionFailed(var4);
                     return;
                  } catch (BadParcelableException var6) {
                     var10001 = false;
                     break label63;
                  }
               }

               try {
                  var3.onLoadChildren(var4, var2.getString("data_media_item_id"), var2.getParcelableArrayList("data_media_item_list"), var2.getBundle("data_options"));
                  return;
               } catch (BadParcelableException var5) {
                  var10001 = false;
               }
            }

            Log.e("MediaBrowserCompat", "Could not unparcel the data.");
            if (var1.what == 1) {
               var3.onConnectionFailed(var4);
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

   public abstract static class CustomActionCallback {
      public void onError(String var1, Bundle var2, Bundle var3) {
      }

      public void onProgressUpdate(String var1, Bundle var2, Bundle var3) {
      }

      public void onResult(String var1, Bundle var2, Bundle var3) {
      }
   }

   private static class CustomActionResultReceiver extends ResultReceiver {
      private final String mAction;
      private final MediaBrowserCompat.CustomActionCallback mCallback;
      private final Bundle mExtras;

      CustomActionResultReceiver(String var1, Bundle var2, MediaBrowserCompat.CustomActionCallback var3, Handler var4) {
         super(var4);
         this.mAction = var1;
         this.mExtras = var2;
         this.mCallback = var3;
      }

      protected void onReceiveResult(int var1, Bundle var2) {
         if (this.mCallback != null) {
            switch(var1) {
            case -1:
               this.mCallback.onError(this.mAction, this.mExtras, var2);
               break;
            case 0:
               this.mCallback.onResult(this.mAction, this.mExtras, var2);
               break;
            case 1:
               this.mCallback.onProgressUpdate(this.mAction, this.mExtras, var2);
               break;
            default:
               StringBuilder var3 = new StringBuilder();
               var3.append("Unknown result code: ");
               var3.append(var1);
               var3.append(" (extras=");
               var3.append(this.mExtras);
               var3.append(", resultData=");
               var3.append(var2);
               var3.append(")");
               Log.w("MediaBrowserCompat", var3.toString());
            }

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

      void sendCustomAction(@NonNull String var1, Bundle var2, @Nullable MediaBrowserCompat.CustomActionCallback var3);

      void subscribe(@NonNull String var1, Bundle var2, @NonNull MediaBrowserCompat.SubscriptionCallback var3);

      void unsubscribe(@NonNull String var1, MediaBrowserCompat.SubscriptionCallback var2);
   }

   @RequiresApi(21)
   static class MediaBrowserImplApi21 implements MediaBrowserCompat.MediaBrowserImpl, MediaBrowserCompat.MediaBrowserServiceCallbackImpl, MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal {
      protected final Object mBrowserObj;
      protected Messenger mCallbacksMessenger;
      final Context mContext;
      protected final MediaBrowserCompat.CallbackHandler mHandler = new MediaBrowserCompat.CallbackHandler(this);
      private MediaSessionCompat.Token mMediaSessionToken;
      protected final Bundle mRootHints;
      protected MediaBrowserCompat.ServiceBinderWrapper mServiceBinderWrapper;
      private final ArrayMap mSubscriptions = new ArrayMap();

      public MediaBrowserImplApi21(Context var1, ComponentName var2, MediaBrowserCompat.ConnectionCallback var3, Bundle var4) {
         this.mContext = var1;
         Bundle var5 = var4;
         if (var4 == null) {
            var5 = new Bundle();
         }

         var5.putInt("extra_client_version", 1);
         this.mRootHints = new Bundle(var5);
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
         } else if (!MediaBrowserCompatApi21.isConnected(this.mBrowserObj)) {
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
               StringBuilder var5 = new StringBuilder();
               var5.append("Remote error getting media item: ");
               var5.append(var1);
               Log.i("MediaBrowserCompat", var5.toString());
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var2.onError(var1);
                  }
               });
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
         if (this.mMediaSessionToken == null) {
            this.mMediaSessionToken = MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj));
         }

         return this.mMediaSessionToken;
      }

      public boolean isConnected() {
         return MediaBrowserCompatApi21.isConnected(this.mBrowserObj);
      }

      public void onConnected() {
         Bundle var1 = MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
         if (var1 != null) {
            IBinder var2 = BundleCompat.getBinder(var1, "extra_messenger");
            if (var2 != null) {
               this.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(var2, this.mRootHints);
               this.mCallbacksMessenger = new Messenger(this.mHandler);
               this.mHandler.setCallbacksMessenger(this.mCallbacksMessenger);

               try {
                  this.mServiceBinderWrapper.registerCallbackMessenger(this.mCallbacksMessenger);
               } catch (RemoteException var3) {
                  Log.i("MediaBrowserCompat", "Remote error registering client messenger.");
               }
            }

            IMediaSession var4 = IMediaSession.Stub.asInterface(BundleCompat.getBinder(var1, "extra_session_binder"));
            if (var4 != null) {
               this.mMediaSessionToken = MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj), var4);
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
         this.mMediaSessionToken = null;
         this.mHandler.setCallbacksMessenger((Messenger)null);
      }

      public void onLoadChildren(Messenger var1, String var2, List var3, Bundle var4) {
         if (this.mCallbacksMessenger == var1) {
            MediaBrowserCompat.Subscription var5 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var2);
            if (var5 == null) {
               if (MediaBrowserCompat.DEBUG) {
                  StringBuilder var7 = new StringBuilder();
                  var7.append("onLoadChildren for id that isn't subscribed id=");
                  var7.append(var2);
                  Log.d("MediaBrowserCompat", var7.toString());
               }

            } else {
               MediaBrowserCompat.SubscriptionCallback var6 = var5.getCallback(this.mContext, var4);
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
            throw new IllegalStateException("search() called while not connected");
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
            } catch (RemoteException var6) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Remote error searching items with query: ");
               var5.append(var1);
               Log.i("MediaBrowserCompat", var5.toString(), var6);
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var3.onError(var1, var2);
                  }
               });
            }

         }
      }

      public void sendCustomAction(@NonNull final String var1, final Bundle var2, @Nullable final MediaBrowserCompat.CustomActionCallback var3) {
         if (!this.isConnected()) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Cannot send a custom action (");
            var7.append(var1);
            var7.append(") with ");
            var7.append("extras ");
            var7.append(var2);
            var7.append(" because the browser is not connected to the ");
            var7.append("service.");
            throw new IllegalStateException(var7.toString());
         } else {
            if (this.mServiceBinderWrapper == null) {
               Log.i("MediaBrowserCompat", "The connected service doesn't support sendCustomAction.");
               if (var3 != null) {
                  this.mHandler.post(new Runnable() {
                     public void run() {
                        var3.onError(var1, var2, (Bundle)null);
                     }
                  });
               }
            }

            MediaBrowserCompat.CustomActionResultReceiver var4 = new MediaBrowserCompat.CustomActionResultReceiver(var1, var2, var3, this.mHandler);

            try {
               this.mServiceBinderWrapper.sendCustomAction(var1, var2, var4, this.mCallbacksMessenger);
            } catch (RemoteException var6) {
               StringBuilder var8 = new StringBuilder();
               var8.append("Remote error sending a custom action: action=");
               var8.append(var1);
               var8.append(", extras=");
               var8.append(var2);
               Log.i("MediaBrowserCompat", var8.toString(), var6);
               if (var3 != null) {
                  this.mHandler.post(new Runnable() {
                     public void run() {
                        var3.onError(var1, var2, (Bundle)null);
                     }
                  });
               }
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

         var5.putCallback(this.mContext, var2, var3);
         if (this.mServiceBinderWrapper == null) {
            MediaBrowserCompatApi21.subscribe(this.mBrowserObj, var1, var3.mSubscriptionCallbackObj);
         } else {
            try {
               this.mServiceBinderWrapper.addSubscription(var1, var3.mToken, var2, this.mCallbacksMessenger);
            } catch (RemoteException var6) {
               StringBuilder var7 = new StringBuilder();
               var7.append("Remote error subscribing media item: ");
               var7.append(var1);
               Log.i("MediaBrowserCompat", var7.toString());
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
               label68: {
                  boolean var10001;
                  if (var2 == null) {
                     try {
                        this.mServiceBinderWrapper.removeSubscription(var1, (IBinder)null, this.mCallbacksMessenger);
                        break label68;
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
                              break label68;
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

                  StringBuilder var10 = new StringBuilder();
                  var10.append("removeSubscription failed with RemoteException parentId=");
                  var10.append(var1);
                  Log.d("MediaBrowserCompat", var10.toString());
               }
            }

            if (var3.isEmpty() || var2 == null) {
               this.mSubscriptions.remove(var1);
            }

         }
      }
   }

   @RequiresApi(23)
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

   @RequiresApi(26)
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
      static final int CONNECT_STATE_CONNECTED = 3;
      static final int CONNECT_STATE_CONNECTING = 2;
      static final int CONNECT_STATE_DISCONNECTED = 1;
      static final int CONNECT_STATE_DISCONNECTING = 0;
      static final int CONNECT_STATE_SUSPENDED = 4;
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
      int mState = 1;
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
         switch(var0) {
         case 0:
            return "CONNECT_STATE_DISCONNECTING";
         case 1:
            return "CONNECT_STATE_DISCONNECTED";
         case 2:
            return "CONNECT_STATE_CONNECTING";
         case 3:
            return "CONNECT_STATE_CONNECTED";
         case 4:
            return "CONNECT_STATE_SUSPENDED";
         default:
            StringBuilder var1 = new StringBuilder();
            var1.append("UNKNOWN/");
            var1.append(var0);
            return var1.toString();
         }
      }

      private boolean isCurrent(Messenger var1, String var2) {
         if (this.mCallbacksMessenger == var1 && this.mState != 0 && this.mState != 1) {
            return true;
         } else {
            if (this.mState != 0 && this.mState != 1) {
               StringBuilder var3 = new StringBuilder();
               var3.append(var2);
               var3.append(" for ");
               var3.append(this.mServiceComponent);
               var3.append(" with mCallbacksMessenger=");
               var3.append(this.mCallbacksMessenger);
               var3.append(" this=");
               var3.append(this);
               Log.i("MediaBrowserCompat", var3.toString());
            }

            return false;
         }
      }

      public void connect() {
         if (this.mState != 0 && this.mState != 1) {
            StringBuilder var1 = new StringBuilder();
            var1.append("connect() called while neigther disconnecting nor disconnected (state=");
            var1.append(getStateLabel(this.mState));
            var1.append(")");
            throw new IllegalStateException(var1.toString());
         } else {
            this.mState = 2;
            this.mHandler.post(new Runnable() {
               public void run() {
                  if (MediaBrowserImplBase.this.mState != 0) {
                     MediaBrowserImplBase.this.mState = 2;
                     StringBuilder var4;
                     if (MediaBrowserCompat.DEBUG && MediaBrowserImplBase.this.mServiceConnection != null) {
                        var4 = new StringBuilder();
                        var4.append("mServiceConnection should be null. Instead it is ");
                        var4.append(MediaBrowserImplBase.this.mServiceConnection);
                        throw new RuntimeException(var4.toString());
                     } else if (MediaBrowserImplBase.this.mServiceBinderWrapper != null) {
                        var4 = new StringBuilder();
                        var4.append("mServiceBinderWrapper should be null. Instead it is ");
                        var4.append(MediaBrowserImplBase.this.mServiceBinderWrapper);
                        throw new RuntimeException(var4.toString());
                     } else if (MediaBrowserImplBase.this.mCallbacksMessenger != null) {
                        var4 = new StringBuilder();
                        var4.append("mCallbacksMessenger should be null. Instead it is ");
                        var4.append(MediaBrowserImplBase.this.mCallbacksMessenger);
                        throw new RuntimeException(var4.toString());
                     } else {
                        Intent var1 = new Intent("android.media.browse.MediaBrowserService");
                        var1.setComponent(MediaBrowserImplBase.this.mServiceComponent);
                        MediaBrowserImplBase.this.mServiceConnection = MediaBrowserImplBase.this.new MediaServiceConnection();

                        boolean var2;
                        try {
                           var2 = MediaBrowserImplBase.this.mContext.bindService(var1, MediaBrowserImplBase.this.mServiceConnection, 1);
                        } catch (Exception var3) {
                           var4 = new StringBuilder();
                           var4.append("Failed binding to service ");
                           var4.append(MediaBrowserImplBase.this.mServiceComponent);
                           Log.e("MediaBrowserCompat", var4.toString());
                           var2 = false;
                        }

                        if (!var2) {
                           MediaBrowserImplBase.this.forceCloseConnection();
                           MediaBrowserImplBase.this.mCallback.onConnectionFailed();
                        }

                        if (MediaBrowserCompat.DEBUG) {
                           Log.d("MediaBrowserCompat", "connect...");
                           MediaBrowserImplBase.this.dump();
                        }

                     }
                  }
               }
            });
         }
      }

      public void disconnect() {
         this.mState = 0;
         this.mHandler.post(new Runnable() {
            public void run() {
               if (MediaBrowserImplBase.this.mCallbacksMessenger != null) {
                  try {
                     MediaBrowserImplBase.this.mServiceBinderWrapper.disconnect(MediaBrowserImplBase.this.mCallbacksMessenger);
                  } catch (RemoteException var3) {
                     StringBuilder var1 = new StringBuilder();
                     var1.append("RemoteException during connect for ");
                     var1.append(MediaBrowserImplBase.this.mServiceComponent);
                     Log.w("MediaBrowserCompat", var1.toString());
                  }
               }

               int var2 = MediaBrowserImplBase.this.mState;
               MediaBrowserImplBase.this.forceCloseConnection();
               if (var2 != 0) {
                  MediaBrowserImplBase.this.mState = var2;
               }

               if (MediaBrowserCompat.DEBUG) {
                  Log.d("MediaBrowserCompat", "disconnect...");
                  MediaBrowserImplBase.this.dump();
               }

            }
         });
      }

      void dump() {
         Log.d("MediaBrowserCompat", "MediaBrowserCompat...");
         StringBuilder var1 = new StringBuilder();
         var1.append("  mServiceComponent=");
         var1.append(this.mServiceComponent);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mCallback=");
         var1.append(this.mCallback);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mRootHints=");
         var1.append(this.mRootHints);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mState=");
         var1.append(getStateLabel(this.mState));
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mServiceConnection=");
         var1.append(this.mServiceConnection);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mServiceBinderWrapper=");
         var1.append(this.mServiceBinderWrapper);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mCallbacksMessenger=");
         var1.append(this.mCallbacksMessenger);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mRootId=");
         var1.append(this.mRootId);
         Log.d("MediaBrowserCompat", var1.toString());
         var1 = new StringBuilder();
         var1.append("  mMediaSessionToken=");
         var1.append(this.mMediaSessionToken);
         Log.d("MediaBrowserCompat", var1.toString());
      }

      void forceCloseConnection() {
         if (this.mServiceConnection != null) {
            this.mContext.unbindService(this.mServiceConnection);
         }

         this.mState = 1;
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
            StringBuilder var1 = new StringBuilder();
            var1.append("getExtras() called while not connected (state=");
            var1.append(getStateLabel(this.mState));
            var1.append(")");
            throw new IllegalStateException(var1.toString());
         } else {
            return this.mExtras;
         }
      }

      public void getItem(@NonNull final String var1, @NonNull final MediaBrowserCompat.ItemCallback var2) {
         if (TextUtils.isEmpty(var1)) {
            throw new IllegalArgumentException("mediaId is empty");
         } else if (var2 == null) {
            throw new IllegalArgumentException("cb is null");
         } else if (!this.isConnected()) {
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
               StringBuilder var5 = new StringBuilder();
               var5.append("Remote error getting media item: ");
               var5.append(var1);
               Log.i("MediaBrowserCompat", var5.toString());
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var2.onError(var1);
                  }
               });
            }

         }
      }

      @NonNull
      public String getRoot() {
         if (!this.isConnected()) {
            StringBuilder var1 = new StringBuilder();
            var1.append("getRoot() called while not connected(state=");
            var1.append(getStateLabel(this.mState));
            var1.append(")");
            throw new IllegalStateException(var1.toString());
         } else {
            return this.mRootId;
         }
      }

      @NonNull
      public ComponentName getServiceComponent() {
         if (!this.isConnected()) {
            StringBuilder var1 = new StringBuilder();
            var1.append("getServiceComponent() called while not connected (state=");
            var1.append(this.mState);
            var1.append(")");
            throw new IllegalStateException(var1.toString());
         } else {
            return this.mServiceComponent;
         }
      }

      @NonNull
      public MediaSessionCompat.Token getSessionToken() {
         if (!this.isConnected()) {
            StringBuilder var1 = new StringBuilder();
            var1.append("getSessionToken() called while not connected(state=");
            var1.append(this.mState);
            var1.append(")");
            throw new IllegalStateException(var1.toString());
         } else {
            return this.mMediaSessionToken;
         }
      }

      public boolean isConnected() {
         boolean var1;
         if (this.mState == 3) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public void onConnectionFailed(Messenger var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("onConnectFailed for ");
         var2.append(this.mServiceComponent);
         Log.e("MediaBrowserCompat", var2.toString());
         if (this.isCurrent(var1, "onConnectFailed")) {
            if (this.mState != 2) {
               StringBuilder var3 = new StringBuilder();
               var3.append("onConnect from service while mState=");
               var3.append(getStateLabel(this.mState));
               var3.append("... ignoring");
               Log.w("MediaBrowserCompat", var3.toString());
            } else {
               this.forceCloseConnection();
               this.mCallback.onConnectionFailed();
            }
         }
      }

      public void onLoadChildren(Messenger var1, String var2, List var3, Bundle var4) {
         if (this.isCurrent(var1, "onLoadChildren")) {
            StringBuilder var5;
            if (MediaBrowserCompat.DEBUG) {
               var5 = new StringBuilder();
               var5.append("onLoadChildren for ");
               var5.append(this.mServiceComponent);
               var5.append(" id=");
               var5.append(var2);
               Log.d("MediaBrowserCompat", var5.toString());
            }

            MediaBrowserCompat.Subscription var6 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var2);
            if (var6 == null) {
               if (MediaBrowserCompat.DEBUG) {
                  var5 = new StringBuilder();
                  var5.append("onLoadChildren for id that isn't subscribed id=");
                  var5.append(var2);
                  Log.d("MediaBrowserCompat", var5.toString());
               }

            } else {
               MediaBrowserCompat.SubscriptionCallback var7 = var6.getCallback(this.mContext, var4);
               if (var7 != null) {
                  if (var4 == null) {
                     if (var3 == null) {
                        var7.onError(var2);
                     } else {
                        var7.onChildrenLoaded(var2, var3);
                     }
                  } else if (var3 == null) {
                     var7.onError(var2, var4);
                  } else {
                     var7.onChildrenLoaded(var2, var3, var4);
                  }
               }

            }
         }
      }

      public void onServiceConnected(Messenger var1, String var2, MediaSessionCompat.Token var3, Bundle var4) {
         if (this.isCurrent(var1, "onConnect")) {
            if (this.mState != 2) {
               StringBuilder var10 = new StringBuilder();
               var10.append("onConnect from service while mState=");
               var10.append(getStateLabel(this.mState));
               var10.append("... ignoring");
               Log.w("MediaBrowserCompat", var10.toString());
            } else {
               this.mRootId = var2;
               this.mMediaSessionToken = var3;
               this.mExtras = var4;
               this.mState = 3;
               if (MediaBrowserCompat.DEBUG) {
                  Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                  this.dump();
               }

               this.mCallback.onConnected();

               label43: {
                  Iterator var9;
                  boolean var10001;
                  try {
                     var9 = this.mSubscriptions.entrySet().iterator();
                  } catch (RemoteException var8) {
                     var10001 = false;
                     break label43;
                  }

                  label40:
                  while(true) {
                     List var12;
                     List var14;
                     try {
                        if (!var9.hasNext()) {
                           return;
                        }

                        Entry var11 = (Entry)var9.next();
                        var2 = (String)var11.getKey();
                        MediaBrowserCompat.Subscription var13 = (MediaBrowserCompat.Subscription)var11.getValue();
                        var12 = var13.getCallbacks();
                        var14 = var13.getOptionsList();
                     } catch (RemoteException var7) {
                        var10001 = false;
                        break;
                     }

                     int var5 = 0;

                     while(true) {
                        try {
                           if (var5 >= var12.size()) {
                              break;
                           }

                           this.mServiceBinderWrapper.addSubscription(var2, ((MediaBrowserCompat.SubscriptionCallback)var12.get(var5)).mToken, (Bundle)var14.get(var5), this.mCallbacksMessenger);
                        } catch (RemoteException var6) {
                           var10001 = false;
                           break label40;
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
            StringBuilder var7 = new StringBuilder();
            var7.append("search() called while not connected (state=");
            var7.append(getStateLabel(this.mState));
            var7.append(")");
            throw new IllegalStateException(var7.toString());
         } else {
            MediaBrowserCompat.SearchResultReceiver var4 = new MediaBrowserCompat.SearchResultReceiver(var1, var2, var3, this.mHandler);

            try {
               this.mServiceBinderWrapper.search(var1, var2, var4, this.mCallbacksMessenger);
            } catch (RemoteException var6) {
               StringBuilder var8 = new StringBuilder();
               var8.append("Remote error searching items with query: ");
               var8.append(var1);
               Log.i("MediaBrowserCompat", var8.toString(), var6);
               this.mHandler.post(new Runnable() {
                  public void run() {
                     var3.onError(var1, var2);
                  }
               });
            }

         }
      }

      public void sendCustomAction(@NonNull final String var1, final Bundle var2, @Nullable final MediaBrowserCompat.CustomActionCallback var3) {
         if (!this.isConnected()) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Cannot send a custom action (");
            var7.append(var1);
            var7.append(") with ");
            var7.append("extras ");
            var7.append(var2);
            var7.append(" because the browser is not connected to the ");
            var7.append("service.");
            throw new IllegalStateException(var7.toString());
         } else {
            MediaBrowserCompat.CustomActionResultReceiver var4 = new MediaBrowserCompat.CustomActionResultReceiver(var1, var2, var3, this.mHandler);

            try {
               this.mServiceBinderWrapper.sendCustomAction(var1, var2, var4, this.mCallbacksMessenger);
            } catch (RemoteException var6) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Remote error sending a custom action: action=");
               var5.append(var1);
               var5.append(", extras=");
               var5.append(var2);
               Log.i("MediaBrowserCompat", var5.toString(), var6);
               if (var3 != null) {
                  this.mHandler.post(new Runnable() {
                     public void run() {
                        var3.onError(var1, var2, (Bundle)null);
                     }
                  });
               }
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

         var5.putCallback(this.mContext, var2, var3);
         if (this.isConnected()) {
            try {
               this.mServiceBinderWrapper.addSubscription(var1, var3.mToken, var2, this.mCallbacksMessenger);
            } catch (RemoteException var6) {
               StringBuilder var7 = new StringBuilder();
               var7.append("addSubscription failed with RemoteException parentId=");
               var7.append(var1);
               Log.d("MediaBrowserCompat", var7.toString());
            }
         }

      }

      public void unsubscribe(@NonNull String var1, MediaBrowserCompat.SubscriptionCallback var2) {
         MediaBrowserCompat.Subscription var3 = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(var1);
         if (var3 != null) {
            label59: {
               boolean var10001;
               if (var2 == null) {
                  try {
                     if (this.isConnected()) {
                        this.mServiceBinderWrapper.removeSubscription(var1, (IBinder)null, this.mCallbacksMessenger);
                     }
                     break label59;
                  } catch (RemoteException var7) {
                     var10001 = false;
                  }
               } else {
                  label57: {
                     List var4;
                     List var5;
                     int var6;
                     try {
                        var4 = var3.getCallbacks();
                        var5 = var3.getOptionsList();
                        var6 = var4.size() - 1;
                     } catch (RemoteException var10) {
                        var10001 = false;
                        break label57;
                     }

                     while(true) {
                        if (var6 < 0) {
                           break label59;
                        }

                        label51: {
                           try {
                              if (var4.get(var6) != var2) {
                                 break label51;
                              }

                              if (this.isConnected()) {
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

               StringBuilder var11 = new StringBuilder();
               var11.append("removeSubscription failed with RemoteException parentId=");
               var11.append(var1);
               Log.d("MediaBrowserCompat", var11.toString());
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
            if (MediaBrowserImplBase.this.mServiceConnection == this && MediaBrowserImplBase.this.mState != 0 && MediaBrowserImplBase.this.mState != 1) {
               return true;
            } else {
               if (MediaBrowserImplBase.this.mState != 0 && MediaBrowserImplBase.this.mState != 1) {
                  StringBuilder var2 = new StringBuilder();
                  var2.append(var1);
                  var2.append(" for ");
                  var2.append(MediaBrowserImplBase.this.mServiceComponent);
                  var2.append(" with mServiceConnection=");
                  var2.append(MediaBrowserImplBase.this.mServiceConnection);
                  var2.append(" this=");
                  var2.append(this);
                  Log.i("MediaBrowserCompat", var2.toString());
               }

               return false;
            }
         }

         public void onServiceConnected(final ComponentName var1, final IBinder var2) {
            this.postOrRun(new Runnable() {
               public void run() {
                  StringBuilder var1x;
                  if (MediaBrowserCompat.DEBUG) {
                     var1x = new StringBuilder();
                     var1x.append("MediaServiceConnection.onServiceConnected name=");
                     var1x.append(var1);
                     var1x.append(" binder=");
                     var1x.append(var2);
                     Log.d("MediaBrowserCompat", var1x.toString());
                     MediaBrowserImplBase.this.dump();
                  }

                  if (MediaServiceConnection.this.isCurrent("onServiceConnected")) {
                     MediaBrowserImplBase.this.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(var2, MediaBrowserImplBase.this.mRootHints);
                     MediaBrowserImplBase.this.mCallbacksMessenger = new Messenger(MediaBrowserImplBase.this.mHandler);
                     MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(MediaBrowserImplBase.this.mCallbacksMessenger);
                     MediaBrowserImplBase.this.mState = 2;

                     try {
                        if (MediaBrowserCompat.DEBUG) {
                           Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                           MediaBrowserImplBase.this.dump();
                        }

                        MediaBrowserImplBase.this.mServiceBinderWrapper.connect(MediaBrowserImplBase.this.mContext, MediaBrowserImplBase.this.mCallbacksMessenger);
                     } catch (RemoteException var2x) {
                        var1x = new StringBuilder();
                        var1x.append("RemoteException during connect for ");
                        var1x.append(MediaBrowserImplBase.this.mServiceComponent);
                        Log.w("MediaBrowserCompat", var1x.toString());
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
                     StringBuilder var1x = new StringBuilder();
                     var1x.append("MediaServiceConnection.onServiceDisconnected name=");
                     var1x.append(var1);
                     var1x.append(" this=");
                     var1x.append(this);
                     var1x.append(" mServiceConnection=");
                     var1x.append(MediaBrowserImplBase.this.mServiceConnection);
                     Log.d("MediaBrowserCompat", var1x.toString());
                     MediaBrowserImplBase.this.dump();
                  }

                  if (MediaServiceConnection.this.isCurrent("onServiceDisconnected")) {
                     MediaBrowserImplBase.this.mServiceBinderWrapper = null;
                     MediaBrowserImplBase.this.mCallbacksMessenger = null;
                     MediaBrowserImplBase.this.mHandler.setCallbacksMessenger((Messenger)null);
                     MediaBrowserImplBase.this.mState = 4;
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
         if (var0 != null && VERSION.SDK_INT >= 21) {
            int var1 = MediaBrowserCompatApi21.MediaItem.getFlags(var0);
            return new MediaBrowserCompat.MediaItem(MediaDescriptionCompat.fromMediaDescription(MediaBrowserCompatApi21.MediaItem.getDescription(var0)), var1);
         } else {
            return null;
         }
      }

      public static List fromMediaItemList(List var0) {
         if (var0 != null && VERSION.SDK_INT >= 21) {
            ArrayList var1 = new ArrayList(var0.size());
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               var1.add(fromMediaItem(var2.next()));
            }

            return var1;
         } else {
            return null;
         }
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
         int var1 = this.mFlags;
         boolean var2 = true;
         if ((var1 & 1) == 0) {
            var2 = false;
         }

         return var2;
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
         var1.append("mFlags=");
         var1.append(this.mFlags);
         var1.append(", mDescription=");
         var1.append(this.mDescription);
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
         if (var2 != null) {
            var2.setClassLoader(MediaBrowserCompat.class.getClassLoader());
         }

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

      void sendCustomAction(String var1, Bundle var2, ResultReceiver var3, Messenger var4) throws RemoteException {
         Bundle var5 = new Bundle();
         var5.putString("data_custom_action", var1);
         var5.putBundle("data_custom_action_extras", var2);
         var5.putParcelable("data_result_receiver", var3);
         this.sendRequest(9, var5, var4);
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

      public MediaBrowserCompat.SubscriptionCallback getCallback(Context var1, Bundle var2) {
         if (var2 != null) {
            var2.setClassLoader(var1.getClassLoader());
         }

         for(int var3 = 0; var3 < this.mOptionsList.size(); ++var3) {
            if (MediaBrowserCompatUtils.areSameOptions((Bundle)this.mOptionsList.get(var3), var2)) {
               return (MediaBrowserCompat.SubscriptionCallback)this.mCallbacks.get(var3);
            }
         }

         return null;
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

      public void putCallback(Context var1, Bundle var2, MediaBrowserCompat.SubscriptionCallback var3) {
         if (var2 != null) {
            var2.setClassLoader(var1.getClassLoader());
         }

         for(int var4 = 0; var4 < this.mOptionsList.size(); ++var4) {
            if (MediaBrowserCompatUtils.areSameOptions((Bundle)this.mOptionsList.get(var4), var2)) {
               this.mCallbacks.set(var4, var3);
               return;
            }
         }

         this.mCallbacks.add(var3);
         this.mOptionsList.add(var2);
      }
   }

   public abstract static class SubscriptionCallback {
      private final Object mSubscriptionCallbackObj;
      WeakReference mSubscriptionRef;
      private final IBinder mToken;

      public SubscriptionCallback() {
         if (VERSION.SDK_INT >= 26) {
            this.mSubscriptionCallbackObj = MediaBrowserCompatApi24.createSubscriptionCallback(new MediaBrowserCompat.SubscriptionCallback.StubApi24());
            this.mToken = null;
         } else if (VERSION.SDK_INT >= 21) {
            this.mSubscriptionCallbackObj = MediaBrowserCompatApi21.createSubscriptionCallback(new MediaBrowserCompat.SubscriptionCallback.StubApi21());
            this.mToken = new Binder();
         } else {
            this.mSubscriptionCallbackObj = null;
            this.mToken = new Binder();
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
            if (var1 == null) {
               return null;
            } else {
               int var3 = var2.getInt("android.media.browse.extra.PAGE", -1);
               int var4 = var2.getInt("android.media.browse.extra.PAGE_SIZE", -1);
               if (var3 == -1 && var4 == -1) {
                  return var1;
               } else {
                  int var5 = var4 * var3;
                  int var6 = var5 + var4;
                  if (var3 >= 0 && var4 >= 1 && var5 < var1.size()) {
                     var4 = var6;
                     if (var6 > var1.size()) {
                        var4 = var1.size();
                     }

                     return var1.subList(var5, var4);
                  } else {
                     return Collections.EMPTY_LIST;
                  }
               }
            }
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
               List var5 = var3.getOptionsList();

               for(int var6 = 0; var6 < var2.size(); ++var6) {
                  Bundle var7 = (Bundle)var5.get(var6);
                  if (var7 == null) {
                     SubscriptionCallback.this.onChildrenLoaded(var1, var4);
                  } else {
                     SubscriptionCallback.this.onChildrenLoaded(var1, this.applyOptions(var4, var7), var7);
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
