package android.support.v4.media;

import android.app.Service;
import android.content.Intent;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.BuildCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class MediaBrowserServiceCompat extends Service {
   static final boolean DEBUG = Log.isLoggable("MBServiceCompat", 3);
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final String KEY_MEDIA_ITEM = "media_item";
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final String KEY_SEARCH_RESULTS = "search_results";
   static final int RESULT_ERROR = -1;
   static final int RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED = 2;
   static final int RESULT_FLAG_ON_SEARCH_NOT_IMPLEMENTED = 4;
   static final int RESULT_FLAG_OPTION_NOT_HANDLED = 1;
   static final int RESULT_OK = 0;
   public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";
   static final String TAG = "MBServiceCompat";
   final ArrayMap mConnections = new ArrayMap();
   MediaBrowserServiceCompat.ConnectionRecord mCurConnection;
   final MediaBrowserServiceCompat.ServiceHandler mHandler = new MediaBrowserServiceCompat.ServiceHandler();
   private MediaBrowserServiceCompat.MediaBrowserServiceImpl mImpl;
   MediaSessionCompat.Token mSession;

   void addSubscription(String var1, MediaBrowserServiceCompat.ConnectionRecord var2, IBinder var3, Bundle var4) {
      List var5 = (List)var2.subscriptions.get(var1);
      Object var6 = var5;
      if (var5 == null) {
         var6 = new ArrayList();
      }

      Iterator var8 = ((List)var6).iterator();

      Pair var7;
      do {
         if (!var8.hasNext()) {
            ((List)var6).add(new Pair(var3, var4));
            var2.subscriptions.put(var1, var6);
            this.performLoadChildren(var1, var2, var4);
            break;
         }

         var7 = (Pair)var8.next();
      } while(var3 != var7.first || !MediaBrowserCompatUtils.areSameOptions(var4, (Bundle)var7.second));

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

   public void dump(FileDescriptor var1, PrintWriter var2, String[] var3) {
   }

   public final Bundle getBrowserRootHints() {
      return this.mImpl.getBrowserRootHints();
   }

   @Nullable
   public MediaSessionCompat.Token getSessionToken() {
      return this.mSession;
   }

   boolean isValidPackage(String var1, int var2) {
      boolean var3 = false;
      boolean var4;
      if (var1 == null) {
         var4 = var3;
      } else {
         String[] var5 = this.getPackageManager().getPackagesForUid(var2);
         int var6 = var5.length;
         var2 = 0;

         while(true) {
            var4 = var3;
            if (var2 >= var6) {
               break;
            }

            if (var5[var2].equals(var1)) {
               var4 = true;
               break;
            }

            ++var2;
         }
      }

      return var4;
   }

   public void notifyChildrenChanged(@NonNull String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
      } else {
         this.mImpl.notifyChildrenChanged(var1, (Bundle)null);
      }
   }

   public void notifyChildrenChanged(@NonNull String var1, @NonNull Bundle var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
      } else if (var2 == null) {
         throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged");
      } else {
         this.mImpl.notifyChildrenChanged(var1, var2);
      }
   }

   public IBinder onBind(Intent var1) {
      return this.mImpl.onBind(var1);
   }

   public void onCreate() {
      super.onCreate();
      if (VERSION.SDK_INT < 26 && !BuildCompat.isAtLeastO()) {
         if (VERSION.SDK_INT >= 23) {
            this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplApi23();
         } else if (VERSION.SDK_INT >= 21) {
            this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplApi21();
         } else {
            this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplBase();
         }
      } else {
         this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplApi24();
      }

      this.mImpl.onCreate();
   }

   @Nullable
   public abstract MediaBrowserServiceCompat.BrowserRoot onGetRoot(@NonNull String var1, int var2, @Nullable Bundle var3);

   public abstract void onLoadChildren(@NonNull String var1, @NonNull MediaBrowserServiceCompat.Result var2);

   public void onLoadChildren(@NonNull String var1, @NonNull MediaBrowserServiceCompat.Result var2, @NonNull Bundle var3) {
      var2.setFlags(1);
      this.onLoadChildren(var1, var2);
   }

   public void onLoadItem(String var1, @NonNull MediaBrowserServiceCompat.Result var2) {
      var2.setFlags(2);
      var2.sendResult((Object)null);
   }

   public void onSearch(@NonNull String var1, Bundle var2, @NonNull MediaBrowserServiceCompat.Result var3) {
      var3.setFlags(4);
      var3.sendResult((Object)null);
   }

   void performLoadChildren(final String var1, final MediaBrowserServiceCompat.ConnectionRecord var2, final Bundle var3) {
      MediaBrowserServiceCompat.Result var4 = new MediaBrowserServiceCompat.Result(var1) {
         void onResultSent(List var1x, int var2x) {
            if (MediaBrowserServiceCompat.this.mConnections.get(var2.callbacks.asBinder()) != var2) {
               if (MediaBrowserServiceCompat.DEBUG) {
                  Log.d("MBServiceCompat", "Not sending onLoadChildren result for connection that has been disconnected. pkg=" + var2.pkg + " id=" + var1);
               }
            } else {
               if ((var2x & 1) != 0) {
                  var1x = MediaBrowserServiceCompat.this.applyOptions(var1x, var3);
               }

               try {
                  var2.callbacks.onLoadChildren(var1, var1x, var3);
               } catch (RemoteException var3x) {
                  Log.w("MBServiceCompat", "Calling onLoadChildren() failed for id=" + var1 + " package=" + var2.pkg);
               }
            }

         }
      };
      this.mCurConnection = var2;
      if (var3 == null) {
         this.onLoadChildren(var1, var4);
      } else {
         this.onLoadChildren(var1, var4, var3);
      }

      this.mCurConnection = null;
      if (!var4.isDone()) {
         throw new IllegalStateException("onLoadChildren must call detach() or sendResult() before returning for package=" + var2.pkg + " id=" + var1);
      }
   }

   void performLoadItem(String var1, MediaBrowserServiceCompat.ConnectionRecord var2, final ResultReceiver var3) {
      MediaBrowserServiceCompat.Result var4 = new MediaBrowserServiceCompat.Result(var1) {
         void onResultSent(MediaBrowserCompat.MediaItem var1, int var2) {
            if ((var2 & 2) != 0) {
               var3.send(-1, (Bundle)null);
            } else {
               Bundle var3x = new Bundle();
               var3x.putParcelable("media_item", var1);
               var3.send(0, var3x);
            }

         }
      };
      this.mCurConnection = var2;
      this.onLoadItem(var1, var4);
      this.mCurConnection = null;
      if (!var4.isDone()) {
         throw new IllegalStateException("onLoadItem must call detach() or sendResult() before returning for id=" + var1);
      }
   }

   void performSearch(String var1, Bundle var2, MediaBrowserServiceCompat.ConnectionRecord var3, final ResultReceiver var4) {
      MediaBrowserServiceCompat.Result var5 = new MediaBrowserServiceCompat.Result(var1) {
         void onResultSent(List var1, int var2) {
            if ((var2 & 4) == 0 && var1 != null) {
               Bundle var3 = new Bundle();
               var3.putParcelableArray("search_results", (Parcelable[])var1.toArray(new MediaBrowserCompat.MediaItem[0]));
               var4.send(0, var3);
            } else {
               var4.send(-1, (Bundle)null);
            }

         }
      };
      this.mCurConnection = var3;
      this.onSearch(var1, var2, var5);
      this.mCurConnection = null;
      if (!var5.isDone()) {
         throw new IllegalStateException("onSearch must call detach() or sendResult() before returning for query=" + var1);
      }
   }

   boolean removeSubscription(String var1, MediaBrowserServiceCompat.ConnectionRecord var2, IBinder var3) {
      boolean var4;
      if (var3 == null) {
         if (var2.subscriptions.remove(var1) != null) {
            var4 = true;
         } else {
            var4 = false;
         }
      } else {
         var4 = false;
         boolean var5 = false;
         List var6 = (List)var2.subscriptions.get(var1);
         if (var6 != null) {
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               if (var3 == ((Pair)var7.next()).first) {
                  var5 = true;
                  var7.remove();
               }
            }

            var4 = var5;
            if (var6.size() == 0) {
               var2.subscriptions.remove(var1);
               var4 = var5;
            }
         }
      }

      return var4;
   }

   public void setSessionToken(MediaSessionCompat.Token var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Session token may not be null.");
      } else if (this.mSession != null) {
         throw new IllegalStateException("The session token has already been set.");
      } else {
         this.mSession = var1;
         this.mImpl.setSessionToken(var1);
      }
   }

   public static final class BrowserRoot {
      public static final String EXTRA_OFFLINE = "android.service.media.extra.OFFLINE";
      public static final String EXTRA_RECENT = "android.service.media.extra.RECENT";
      public static final String EXTRA_SUGGESTED = "android.service.media.extra.SUGGESTED";
      @Deprecated
      public static final String EXTRA_SUGGESTION_KEYWORDS = "android.service.media.extra.SUGGESTION_KEYWORDS";
      private final Bundle mExtras;
      private final String mRootId;

      public BrowserRoot(@NonNull String var1, @Nullable Bundle var2) {
         if (var1 == null) {
            throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead.");
         } else {
            this.mRootId = var1;
            this.mExtras = var2;
         }
      }

      public Bundle getExtras() {
         return this.mExtras;
      }

      public String getRootId() {
         return this.mRootId;
      }
   }

   private class ConnectionRecord {
      MediaBrowserServiceCompat.ServiceCallbacks callbacks;
      String pkg;
      MediaBrowserServiceCompat.BrowserRoot root;
      Bundle rootHints;
      HashMap subscriptions = new HashMap();

      ConnectionRecord() {
      }
   }

   interface MediaBrowserServiceImpl {
      Bundle getBrowserRootHints();

      void notifyChildrenChanged(String var1, Bundle var2);

      IBinder onBind(Intent var1);

      void onCreate();

      void setSessionToken(MediaSessionCompat.Token var1);
   }

   class MediaBrowserServiceImplApi21 implements MediaBrowserServiceCompat.MediaBrowserServiceImpl, MediaBrowserServiceCompatApi21.ServiceCompatProxy {
      Messenger mMessenger;
      Object mServiceObj;

      public Bundle getBrowserRootHints() {
         Bundle var1 = null;
         if (this.mMessenger != null) {
            if (MediaBrowserServiceCompat.this.mCurConnection == null) {
               throw new IllegalStateException("This should be called inside of onLoadChildren or onLoadItem methods");
            }

            if (MediaBrowserServiceCompat.this.mCurConnection.rootHints != null) {
               var1 = new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
            }
         }

         return var1;
      }

      public void notifyChildrenChanged(final String var1, final Bundle var2) {
         if (this.mMessenger == null) {
            MediaBrowserServiceCompatApi21.notifyChildrenChanged(this.mServiceObj, var1);
         } else {
            MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
               public void run() {
                  Iterator var1x = MediaBrowserServiceCompat.this.mConnections.keySet().iterator();

                  while(true) {
                     List var3;
                     MediaBrowserServiceCompat.ConnectionRecord var5;
                     do {
                        if (!var1x.hasNext()) {
                           return;
                        }

                        IBinder var2x = (IBinder)var1x.next();
                        var5 = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var2x);
                        var3 = (List)var5.subscriptions.get(var1);
                     } while(var3 == null);

                     Iterator var6 = var3.iterator();

                     while(var6.hasNext()) {
                        Pair var4 = (Pair)var6.next();
                        if (MediaBrowserCompatUtils.hasDuplicatedItems(var2, (Bundle)var4.second)) {
                           MediaBrowserServiceCompat.this.performLoadChildren(var1, var5, (Bundle)var4.second);
                        }
                     }
                  }
               }
            });
         }

      }

      public IBinder onBind(Intent var1) {
         return MediaBrowserServiceCompatApi21.onBind(this.mServiceObj, var1);
      }

      public void onCreate() {
         this.mServiceObj = MediaBrowserServiceCompatApi21.createService(MediaBrowserServiceCompat.this, this);
         MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
      }

      public MediaBrowserServiceCompatApi21.BrowserRoot onGetRoot(String var1, int var2, Bundle var3) {
         Object var4 = null;
         Bundle var5 = (Bundle)var4;
         if (var3 != null) {
            var5 = (Bundle)var4;
            if (var3.getInt("extra_client_version", 0) != 0) {
               var3.remove("extra_client_version");
               this.mMessenger = new Messenger(MediaBrowserServiceCompat.this.mHandler);
               var5 = new Bundle();
               var5.putInt("extra_service_version", 1);
               BundleCompat.putBinder(var5, "extra_messenger", this.mMessenger.getBinder());
            }
         }

         MediaBrowserServiceCompat.BrowserRoot var8 = MediaBrowserServiceCompat.this.onGetRoot(var1, var2, var3);
         MediaBrowserServiceCompatApi21.BrowserRoot var6;
         if (var8 == null) {
            var6 = null;
         } else {
            Bundle var7;
            if (var5 == null) {
               var7 = var8.getExtras();
            } else {
               var7 = var5;
               if (var8.getExtras() != null) {
                  var5.putAll(var8.getExtras());
                  var7 = var5;
               }
            }

            var6 = new MediaBrowserServiceCompatApi21.BrowserRoot(var8.getRootId(), var7);
         }

         return var6;
      }

      public void onLoadChildren(String var1, final MediaBrowserServiceCompatApi21.ResultWrapper var2) {
         MediaBrowserServiceCompat.Result var3 = new MediaBrowserServiceCompat.Result(var1) {
            public void detach() {
               var2.detach();
            }

            void onResultSent(List var1, int var2x) {
               ArrayList var3 = null;
               if (var1 != null) {
                  ArrayList var4 = new ArrayList();
                  Iterator var6 = var1.iterator();

                  while(true) {
                     var3 = var4;
                     if (!var6.hasNext()) {
                        break;
                     }

                     MediaBrowserCompat.MediaItem var7 = (MediaBrowserCompat.MediaItem)var6.next();
                     Parcel var5 = Parcel.obtain();
                     var7.writeToParcel(var5, 0);
                     var4.add(var5);
                  }
               }

               var2.sendResult(var3);
            }
         };
         MediaBrowserServiceCompat.this.onLoadChildren(var1, var3);
      }

      public void setSessionToken(MediaSessionCompat.Token var1) {
         MediaBrowserServiceCompatApi21.setSessionToken(this.mServiceObj, var1.getToken());
      }
   }

   class MediaBrowserServiceImplApi23 extends MediaBrowserServiceCompat.MediaBrowserServiceImplApi21 implements MediaBrowserServiceCompatApi23.ServiceCompatProxy {
      MediaBrowserServiceImplApi23() {
         super();
      }

      public void onCreate() {
         this.mServiceObj = MediaBrowserServiceCompatApi23.createService(MediaBrowserServiceCompat.this, this);
         MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
      }

      public void onLoadItem(String var1, final MediaBrowserServiceCompatApi21.ResultWrapper var2) {
         MediaBrowserServiceCompat.Result var3 = new MediaBrowserServiceCompat.Result(var1) {
            public void detach() {
               var2.detach();
            }

            void onResultSent(MediaBrowserCompat.MediaItem var1, int var2x) {
               if (var1 == null) {
                  var2.sendResult((Object)null);
               } else {
                  Parcel var3 = Parcel.obtain();
                  var1.writeToParcel(var3, 0);
                  var2.sendResult(var3);
               }

            }
         };
         MediaBrowserServiceCompat.this.onLoadItem(var1, var3);
      }
   }

   class MediaBrowserServiceImplApi24 extends MediaBrowserServiceCompat.MediaBrowserServiceImplApi23 implements MediaBrowserServiceCompatApi24.ServiceCompatProxy {
      MediaBrowserServiceImplApi24() {
         super();
      }

      public Bundle getBrowserRootHints() {
         Bundle var1;
         if (MediaBrowserServiceCompat.this.mCurConnection != null) {
            if (MediaBrowserServiceCompat.this.mCurConnection.rootHints == null) {
               var1 = null;
            } else {
               var1 = new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
            }
         } else {
            var1 = MediaBrowserServiceCompatApi24.getBrowserRootHints(this.mServiceObj);
         }

         return var1;
      }

      public void notifyChildrenChanged(String var1, Bundle var2) {
         if (var2 == null) {
            MediaBrowserServiceCompatApi21.notifyChildrenChanged(this.mServiceObj, var1);
         } else {
            MediaBrowserServiceCompatApi24.notifyChildrenChanged(this.mServiceObj, var1, var2);
         }

      }

      public void onCreate() {
         this.mServiceObj = MediaBrowserServiceCompatApi24.createService(MediaBrowserServiceCompat.this, this);
         MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
      }

      public void onLoadChildren(String var1, final MediaBrowserServiceCompatApi24.ResultWrapper var2, Bundle var3) {
         MediaBrowserServiceCompat.Result var4 = new MediaBrowserServiceCompat.Result(var1) {
            public void detach() {
               var2.detach();
            }

            void onResultSent(List var1, int var2x) {
               ArrayList var3 = null;
               if (var1 != null) {
                  ArrayList var4 = new ArrayList();
                  Iterator var6 = var1.iterator();

                  while(true) {
                     var3 = var4;
                     if (!var6.hasNext()) {
                        break;
                     }

                     MediaBrowserCompat.MediaItem var5 = (MediaBrowserCompat.MediaItem)var6.next();
                     Parcel var7 = Parcel.obtain();
                     var5.writeToParcel(var7, 0);
                     var4.add(var7);
                  }
               }

               var2.sendResult(var3, var2x);
            }
         };
         MediaBrowserServiceCompat.this.onLoadChildren(var1, var4, var3);
      }
   }

   class MediaBrowserServiceImplBase implements MediaBrowserServiceCompat.MediaBrowserServiceImpl {
      private Messenger mMessenger;

      public Bundle getBrowserRootHints() {
         if (MediaBrowserServiceCompat.this.mCurConnection == null) {
            throw new IllegalStateException("This should be called inside of onLoadChildren or onLoadItem methods");
         } else {
            Bundle var1;
            if (MediaBrowserServiceCompat.this.mCurConnection.rootHints == null) {
               var1 = null;
            } else {
               var1 = new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
            }

            return var1;
         }
      }

      public void notifyChildrenChanged(@NonNull final String var1, final Bundle var2) {
         MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
               Iterator var1x = MediaBrowserServiceCompat.this.mConnections.keySet().iterator();

               while(true) {
                  List var3;
                  MediaBrowserServiceCompat.ConnectionRecord var5;
                  do {
                     if (!var1x.hasNext()) {
                        return;
                     }

                     IBinder var2x = (IBinder)var1x.next();
                     var5 = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var2x);
                     var3 = (List)var5.subscriptions.get(var1);
                  } while(var3 == null);

                  Iterator var4 = var3.iterator();

                  while(var4.hasNext()) {
                     Pair var6 = (Pair)var4.next();
                     if (MediaBrowserCompatUtils.hasDuplicatedItems(var2, (Bundle)var6.second)) {
                        MediaBrowserServiceCompat.this.performLoadChildren(var1, var5, (Bundle)var6.second);
                     }
                  }
               }
            }
         });
      }

      public IBinder onBind(Intent var1) {
         IBinder var2;
         if ("android.media.browse.MediaBrowserService".equals(var1.getAction())) {
            var2 = this.mMessenger.getBinder();
         } else {
            var2 = null;
         }

         return var2;
      }

      public void onCreate() {
         this.mMessenger = new Messenger(MediaBrowserServiceCompat.this.mHandler);
      }

      public void setSessionToken(final MediaSessionCompat.Token var1) {
         MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
               Iterator var1x = MediaBrowserServiceCompat.this.mConnections.values().iterator();

               while(var1x.hasNext()) {
                  MediaBrowserServiceCompat.ConnectionRecord var2 = (MediaBrowserServiceCompat.ConnectionRecord)var1x.next();

                  try {
                     var2.callbacks.onConnect(var2.root.getRootId(), var1, var2.root.getExtras());
                  } catch (RemoteException var4) {
                     Log.w("MBServiceCompat", "Connection for " + var2.pkg + " is no longer valid.");
                     var1x.remove();
                  }
               }

            }
         });
      }
   }

   public static class Result {
      private Object mDebug;
      private boolean mDetachCalled;
      private int mFlags;
      private boolean mSendResultCalled;

      Result(Object var1) {
         this.mDebug = var1;
      }

      public void detach() {
         if (this.mDetachCalled) {
            throw new IllegalStateException("detach() called when detach() had already been called for: " + this.mDebug);
         } else if (this.mSendResultCalled) {
            throw new IllegalStateException("detach() called when sendResult() had already been called for: " + this.mDebug);
         } else {
            this.mDetachCalled = true;
         }
      }

      boolean isDone() {
         boolean var1;
         if (!this.mDetachCalled && !this.mSendResultCalled) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      void onResultSent(Object var1, int var2) {
      }

      public void sendResult(Object var1) {
         if (this.mSendResultCalled) {
            throw new IllegalStateException("sendResult() called twice for: " + this.mDebug);
         } else {
            this.mSendResultCalled = true;
            this.onResultSent(var1, this.mFlags);
         }
      }

      void setFlags(int var1) {
         this.mFlags = var1;
      }
   }

   private class ServiceBinderImpl {
      ServiceBinderImpl() {
      }

      public void addSubscription(final String var1, final IBinder var2, final Bundle var3, final MediaBrowserServiceCompat.ServiceCallbacks var4) {
         MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
               IBinder var1x = var4.asBinder();
               MediaBrowserServiceCompat.ConnectionRecord var2x = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var1x);
               if (var2x == null) {
                  Log.w("MBServiceCompat", "addSubscription for callback that isn't registered id=" + var1);
               } else {
                  MediaBrowserServiceCompat.this.addSubscription(var1, var2x, var2, var3);
               }

            }
         });
      }

      public void connect(final String var1, final int var2, final Bundle var3, final MediaBrowserServiceCompat.ServiceCallbacks var4) {
         if (!MediaBrowserServiceCompat.this.isValidPackage(var1, var2)) {
            throw new IllegalArgumentException("Package/uid mismatch: uid=" + var2 + " package=" + var1);
         } else {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
               public void run() {
                  IBinder var1x = var4.asBinder();
                  MediaBrowserServiceCompat.this.mConnections.remove(var1x);
                  MediaBrowserServiceCompat.ConnectionRecord var2x = MediaBrowserServiceCompat.this.new ConnectionRecord();
                  var2x.pkg = var1;
                  var2x.rootHints = var3;
                  var2x.callbacks = var4;
                  var2x.root = MediaBrowserServiceCompat.this.onGetRoot(var1, var2, var3);
                  if (var2x.root == null) {
                     Log.i("MBServiceCompat", "No root for client " + var1 + " from service " + this.getClass().getName());

                     try {
                        var4.onConnectFailed();
                     } catch (RemoteException var4x) {
                        Log.w("MBServiceCompat", "Calling onConnectFailed() failed. Ignoring. pkg=" + var1);
                     }
                  } else {
                     try {
                        MediaBrowserServiceCompat.this.mConnections.put(var1x, var2x);
                        if (MediaBrowserServiceCompat.this.mSession != null) {
                           var4.onConnect(var2x.root.getRootId(), MediaBrowserServiceCompat.this.mSession, var2x.root.getExtras());
                        }
                     } catch (RemoteException var3x) {
                        Log.w("MBServiceCompat", "Calling onConnect() failed. Dropping client. pkg=" + var1);
                        MediaBrowserServiceCompat.this.mConnections.remove(var1x);
                     }
                  }

               }
            });
         }
      }

      public void disconnect(final MediaBrowserServiceCompat.ServiceCallbacks var1) {
         MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
               IBinder var1x = var1.asBinder();
               if ((MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.remove(var1x) != null) {
               }

            }
         });
      }

      public void getMediaItem(final String var1, final ResultReceiver var2, final MediaBrowserServiceCompat.ServiceCallbacks var3) {
         if (!TextUtils.isEmpty(var1) && var2 != null) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
               public void run() {
                  IBinder var1x = var3.asBinder();
                  MediaBrowserServiceCompat.ConnectionRecord var2x = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var1x);
                  if (var2x == null) {
                     Log.w("MBServiceCompat", "getMediaItem for callback that isn't registered id=" + var1);
                  } else {
                     MediaBrowserServiceCompat.this.performLoadItem(var1, var2x, var2);
                  }

               }
            });
         }

      }

      public void registerCallbacks(final MediaBrowserServiceCompat.ServiceCallbacks var1, final Bundle var2) {
         MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
               IBinder var1x = var1.asBinder();
               MediaBrowserServiceCompat.this.mConnections.remove(var1x);
               MediaBrowserServiceCompat.ConnectionRecord var2x = MediaBrowserServiceCompat.this.new ConnectionRecord();
               var2x.callbacks = var1;
               var2x.rootHints = var2;
               MediaBrowserServiceCompat.this.mConnections.put(var1x, var2x);
            }
         });
      }

      public void removeSubscription(final String var1, final IBinder var2, final MediaBrowserServiceCompat.ServiceCallbacks var3) {
         MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
               IBinder var1x = var3.asBinder();
               MediaBrowserServiceCompat.ConnectionRecord var2x = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var1x);
               if (var2x == null) {
                  Log.w("MBServiceCompat", "removeSubscription for callback that isn't registered id=" + var1);
               } else if (!MediaBrowserServiceCompat.this.removeSubscription(var1, var2x, var2)) {
                  Log.w("MBServiceCompat", "removeSubscription called for " + var1 + " which is not subscribed");
               }

            }
         });
      }

      public void search(final String var1, final Bundle var2, final ResultReceiver var3, final MediaBrowserServiceCompat.ServiceCallbacks var4) {
         if (!TextUtils.isEmpty(var1) && var3 != null) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
               public void run() {
                  IBinder var1x = var4.asBinder();
                  MediaBrowserServiceCompat.ConnectionRecord var2x = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var1x);
                  if (var2x == null) {
                     Log.w("MBServiceCompat", "search for callback that isn't registered query=" + var1);
                  } else {
                     MediaBrowserServiceCompat.this.performSearch(var1, var2, var2x, var3);
                  }

               }
            });
         }

      }

      public void unregisterCallbacks(final MediaBrowserServiceCompat.ServiceCallbacks var1) {
         MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
               IBinder var1x = var1.asBinder();
               MediaBrowserServiceCompat.this.mConnections.remove(var1x);
            }
         });
      }
   }

   private interface ServiceCallbacks {
      IBinder asBinder();

      void onConnect(String var1, MediaSessionCompat.Token var2, Bundle var3) throws RemoteException;

      void onConnectFailed() throws RemoteException;

      void onLoadChildren(String var1, List var2, Bundle var3) throws RemoteException;
   }

   private class ServiceCallbacksCompat implements MediaBrowserServiceCompat.ServiceCallbacks {
      final Messenger mCallbacks;

      ServiceCallbacksCompat(Messenger var2) {
         this.mCallbacks = var2;
      }

      private void sendRequest(int var1, Bundle var2) throws RemoteException {
         Message var3 = Message.obtain();
         var3.what = var1;
         var3.arg1 = 1;
         var3.setData(var2);
         this.mCallbacks.send(var3);
      }

      public IBinder asBinder() {
         return this.mCallbacks.getBinder();
      }

      public void onConnect(String var1, MediaSessionCompat.Token var2, Bundle var3) throws RemoteException {
         Bundle var4 = var3;
         if (var3 == null) {
            var4 = new Bundle();
         }

         var4.putInt("extra_service_version", 1);
         var3 = new Bundle();
         var3.putString("data_media_item_id", var1);
         var3.putParcelable("data_media_session_token", var2);
         var3.putBundle("data_root_hints", var4);
         this.sendRequest(1, var3);
      }

      public void onConnectFailed() throws RemoteException {
         this.sendRequest(2, (Bundle)null);
      }

      public void onLoadChildren(String var1, List var2, Bundle var3) throws RemoteException {
         Bundle var4 = new Bundle();
         var4.putString("data_media_item_id", var1);
         var4.putBundle("data_options", var3);
         if (var2 != null) {
            ArrayList var5;
            if (var2 instanceof ArrayList) {
               var5 = (ArrayList)var2;
            } else {
               var5 = new ArrayList(var2);
            }

            var4.putParcelableArrayList("data_media_item_list", var5);
         }

         this.sendRequest(3, var4);
      }
   }

   private final class ServiceHandler extends Handler {
      private final MediaBrowserServiceCompat.ServiceBinderImpl mServiceBinderImpl = MediaBrowserServiceCompat.this.new ServiceBinderImpl();

      ServiceHandler() {
      }

      public void handleMessage(Message var1) {
         Bundle var2 = var1.getData();
         switch(var1.what) {
         case 1:
            this.mServiceBinderImpl.connect(var2.getString("data_package_name"), var2.getInt("data_calling_uid"), var2.getBundle("data_root_hints"), MediaBrowserServiceCompat.this.new ServiceCallbacksCompat(var1.replyTo));
            break;
         case 2:
            this.mServiceBinderImpl.disconnect(MediaBrowserServiceCompat.this.new ServiceCallbacksCompat(var1.replyTo));
            break;
         case 3:
            this.mServiceBinderImpl.addSubscription(var2.getString("data_media_item_id"), BundleCompat.getBinder(var2, "data_callback_token"), var2.getBundle("data_options"), MediaBrowserServiceCompat.this.new ServiceCallbacksCompat(var1.replyTo));
            break;
         case 4:
            this.mServiceBinderImpl.removeSubscription(var2.getString("data_media_item_id"), BundleCompat.getBinder(var2, "data_callback_token"), MediaBrowserServiceCompat.this.new ServiceCallbacksCompat(var1.replyTo));
            break;
         case 5:
            this.mServiceBinderImpl.getMediaItem(var2.getString("data_media_item_id"), (ResultReceiver)var2.getParcelable("data_result_receiver"), MediaBrowserServiceCompat.this.new ServiceCallbacksCompat(var1.replyTo));
            break;
         case 6:
            this.mServiceBinderImpl.registerCallbacks(MediaBrowserServiceCompat.this.new ServiceCallbacksCompat(var1.replyTo), var2.getBundle("data_root_hints"));
            break;
         case 7:
            this.mServiceBinderImpl.unregisterCallbacks(MediaBrowserServiceCompat.this.new ServiceCallbacksCompat(var1.replyTo));
            break;
         case 8:
            this.mServiceBinderImpl.search(var2.getString("data_search_query"), var2.getBundle("data_search_extras"), (ResultReceiver)var2.getParcelable("data_result_receiver"), MediaBrowserServiceCompat.this.new ServiceCallbacksCompat(var1.replyTo));
            break;
         default:
            Log.w("MBServiceCompat", "Unhandled message: " + var1 + "\n  Service version: " + 1 + "\n  Client version: " + var1.arg1);
         }

      }

      public void postOrRun(Runnable var1) {
         if (Thread.currentThread() == this.getLooper().getThread()) {
            var1.run();
         } else {
            this.post(var1);
         }

      }

      public boolean sendMessageAtTime(Message var1, long var2) {
         Bundle var4 = var1.getData();
         var4.setClassLoader(MediaBrowserCompat.class.getClassLoader());
         var4.putInt("data_calling_uid", Binder.getCallingUid());
         return super.sendMessageAtTime(var1, var2);
      }
   }
}
