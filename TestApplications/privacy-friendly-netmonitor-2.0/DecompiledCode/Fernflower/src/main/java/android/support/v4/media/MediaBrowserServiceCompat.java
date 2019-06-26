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
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class MediaBrowserServiceCompat extends Service {
   static final boolean DEBUG = Log.isLoggable("MBServiceCompat", 3);
   private static final float EPSILON = 1.0E-5F;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final String KEY_MEDIA_ITEM = "media_item";
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final String KEY_SEARCH_RESULTS = "search_results";
   static final int RESULT_ERROR = -1;
   static final int RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED = 2;
   static final int RESULT_FLAG_ON_SEARCH_NOT_IMPLEMENTED = 4;
   static final int RESULT_FLAG_OPTION_NOT_HANDLED = 1;
   static final int RESULT_OK = 0;
   static final int RESULT_PROGRESS_UPDATE = 1;
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

      Iterator var7 = ((List)var6).iterator();

      Pair var8;
      do {
         if (!var7.hasNext()) {
            ((List)var6).add(new Pair(var3, var4));
            var2.subscriptions.put(var1, var6);
            this.performLoadChildren(var1, var2, var4);
            return;
         }

         var8 = (Pair)var7.next();
      } while(var3 != var8.first || !MediaBrowserCompatUtils.areSameOptions(var4, (Bundle)var8.second));

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
               var3 = var6;
               if (var6 > var1.size()) {
                  var3 = var1.size();
               }

               return var1.subList(var5, var3);
            } else {
               return Collections.EMPTY_LIST;
            }
         }
      }
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
      if (var1 == null) {
         return false;
      } else {
         String[] var3 = this.getPackageManager().getPackagesForUid(var2);
         int var4 = var3.length;

         for(var2 = 0; var2 < var4; ++var2) {
            if (var3[var2].equals(var1)) {
               return true;
            }
         }

         return false;
      }
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
      if (VERSION.SDK_INT >= 26) {
         this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplApi24();
      } else if (VERSION.SDK_INT >= 23) {
         this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplApi23();
      } else if (VERSION.SDK_INT >= 21) {
         this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplApi21();
      } else {
         this.mImpl = new MediaBrowserServiceCompat.MediaBrowserServiceImplBase();
      }

      this.mImpl.onCreate();
   }

   public void onCustomAction(@NonNull String var1, Bundle var2, @NonNull MediaBrowserServiceCompat.Result var3) {
      var3.sendError((Bundle)null);
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

   void performCustomAction(String var1, Bundle var2, MediaBrowserServiceCompat.ConnectionRecord var3, final ResultReceiver var4) {
      MediaBrowserServiceCompat.Result var6 = new MediaBrowserServiceCompat.Result(var1) {
         void onErrorSent(Bundle var1) {
            var4.send(-1, var1);
         }

         void onProgressUpdateSent(Bundle var1) {
            var4.send(1, var1);
         }

         void onResultSent(Bundle var1) {
            var4.send(0, var1);
         }
      };
      this.mCurConnection = var3;
      this.onCustomAction(var1, var2, var6);
      this.mCurConnection = null;
      if (!var6.isDone()) {
         StringBuilder var5 = new StringBuilder();
         var5.append("onCustomAction must call detach() or sendResult() or sendError() before returning for action=");
         var5.append(var1);
         var5.append(" extras=");
         var5.append(var2);
         throw new IllegalStateException(var5.toString());
      }
   }

   void performLoadChildren(final String var1, final MediaBrowserServiceCompat.ConnectionRecord var2, final Bundle var3) {
      MediaBrowserServiceCompat.Result var4 = new MediaBrowserServiceCompat.Result(var1) {
         void onResultSent(List var1x) {
            StringBuilder var4;
            if (MediaBrowserServiceCompat.this.mConnections.get(var2.callbacks.asBinder()) != var2) {
               if (MediaBrowserServiceCompat.DEBUG) {
                  var4 = new StringBuilder();
                  var4.append("Not sending onLoadChildren result for connection that has been disconnected. pkg=");
                  var4.append(var2.pkg);
                  var4.append(" id=");
                  var4.append(var1);
                  Log.d("MBServiceCompat", var4.toString());
               }

            } else {
               List var2x = var1x;
               if ((this.getFlags() & 1) != 0) {
                  var2x = MediaBrowserServiceCompat.this.applyOptions(var1x, var3);
               }

               try {
                  var2.callbacks.onLoadChildren(var1, var2x, var3);
               } catch (RemoteException var3x) {
                  var4 = new StringBuilder();
                  var4.append("Calling onLoadChildren() failed for id=");
                  var4.append(var1);
                  var4.append(" package=");
                  var4.append(var2.pkg);
                  Log.w("MBServiceCompat", var4.toString());
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
         StringBuilder var5 = new StringBuilder();
         var5.append("onLoadChildren must call detach() or sendResult() before returning for package=");
         var5.append(var2.pkg);
         var5.append(" id=");
         var5.append(var1);
         throw new IllegalStateException(var5.toString());
      }
   }

   void performLoadItem(String var1, MediaBrowserServiceCompat.ConnectionRecord var2, final ResultReceiver var3) {
      MediaBrowserServiceCompat.Result var5 = new MediaBrowserServiceCompat.Result(var1) {
         void onResultSent(MediaBrowserCompat.MediaItem var1) {
            if ((this.getFlags() & 2) != 0) {
               var3.send(-1, (Bundle)null);
            } else {
               Bundle var2 = new Bundle();
               var2.putParcelable("media_item", var1);
               var3.send(0, var2);
            }
         }
      };
      this.mCurConnection = var2;
      this.onLoadItem(var1, var5);
      this.mCurConnection = null;
      if (!var5.isDone()) {
         StringBuilder var4 = new StringBuilder();
         var4.append("onLoadItem must call detach() or sendResult() before returning for id=");
         var4.append(var1);
         throw new IllegalStateException(var4.toString());
      }
   }

   void performSearch(String var1, Bundle var2, MediaBrowserServiceCompat.ConnectionRecord var3, final ResultReceiver var4) {
      MediaBrowserServiceCompat.Result var6 = new MediaBrowserServiceCompat.Result(var1) {
         void onResultSent(List var1) {
            if ((this.getFlags() & 4) == 0 && var1 != null) {
               Bundle var2 = new Bundle();
               var2.putParcelableArray("search_results", (Parcelable[])var1.toArray(new MediaBrowserCompat.MediaItem[0]));
               var4.send(0, var2);
            } else {
               var4.send(-1, (Bundle)null);
            }
         }
      };
      this.mCurConnection = var3;
      this.onSearch(var1, var2, var6);
      this.mCurConnection = null;
      if (!var6.isDone()) {
         StringBuilder var5 = new StringBuilder();
         var5.append("onSearch must call detach() or sendResult() before returning for query=");
         var5.append(var1);
         throw new IllegalStateException(var5.toString());
      }
   }

   boolean removeSubscription(String var1, MediaBrowserServiceCompat.ConnectionRecord var2, IBinder var3) {
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      if (var3 == null) {
         var4 = var6;
         if (var2.subscriptions.remove(var1) != null) {
            var4 = true;
         }

         return var4;
      } else {
         List var7 = (List)var2.subscriptions.get(var1);
         var6 = var5;
         if (var7 != null) {
            Iterator var8 = var7.iterator();

            while(var8.hasNext()) {
               if (var3 == ((Pair)var8.next()).first) {
                  var8.remove();
                  var4 = true;
               }
            }

            var6 = var4;
            if (var7.size() == 0) {
               var2.subscriptions.remove(var1);
               var6 = var4;
            }
         }

         return var6;
      }
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

   private static class ConnectionRecord {
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

   @RequiresApi(21)
   class MediaBrowserServiceImplApi21 implements MediaBrowserServiceCompat.MediaBrowserServiceImpl, MediaBrowserServiceCompatApi21.ServiceCompatProxy {
      Messenger mMessenger;
      final List mRootExtrasList = new ArrayList();
      Object mServiceObj;

      public Bundle getBrowserRootHints() {
         Messenger var1 = this.mMessenger;
         Bundle var2 = null;
         if (var1 == null) {
            return null;
         } else if (MediaBrowserServiceCompat.this.mCurConnection == null) {
            throw new IllegalStateException("This should be called inside of onLoadChildren, onLoadItem or onSearch methods");
         } else {
            if (MediaBrowserServiceCompat.this.mCurConnection.rootHints != null) {
               var2 = new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
            }

            return var2;
         }
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
         Bundle var5;
         if (var3 != null && var3.getInt("extra_client_version", 0) != 0) {
            var3.remove("extra_client_version");
            this.mMessenger = new Messenger(MediaBrowserServiceCompat.this.mHandler);
            Bundle var4 = new Bundle();
            var4.putInt("extra_service_version", 1);
            BundleCompat.putBinder(var4, "extra_messenger", this.mMessenger.getBinder());
            if (MediaBrowserServiceCompat.this.mSession != null) {
               IMediaSession var8 = MediaBrowserServiceCompat.this.mSession.getExtraBinder();
               IBinder var9;
               if (var8 == null) {
                  var9 = null;
               } else {
                  var9 = var8.asBinder();
               }

               BundleCompat.putBinder(var4, "extra_session_binder", var9);
               var5 = var4;
            } else {
               this.mRootExtrasList.add(var4);
               var5 = var4;
            }
         } else {
            var5 = null;
         }

         MediaBrowserServiceCompat.BrowserRoot var7 = MediaBrowserServiceCompat.this.onGetRoot(var1, var2, var3);
         if (var7 == null) {
            return null;
         } else {
            Bundle var6;
            if (var5 == null) {
               var6 = var7.getExtras();
            } else {
               var6 = var5;
               if (var7.getExtras() != null) {
                  var5.putAll(var7.getExtras());
                  var6 = var5;
               }
            }

            return new MediaBrowserServiceCompatApi21.BrowserRoot(var7.getRootId(), var6);
         }
      }

      public void onLoadChildren(String var1, final MediaBrowserServiceCompatApi21.ResultWrapper var2) {
         MediaBrowserServiceCompat.Result var3 = new MediaBrowserServiceCompat.Result(var1) {
            public void detach() {
               var2.detach();
            }

            void onResultSent(List var1) {
               ArrayList var5;
               if (var1 != null) {
                  ArrayList var2x = new ArrayList();
                  Iterator var3 = var1.iterator();

                  while(true) {
                     var5 = var2x;
                     if (!var3.hasNext()) {
                        break;
                     }

                     MediaBrowserCompat.MediaItem var4 = (MediaBrowserCompat.MediaItem)var3.next();
                     Parcel var6 = Parcel.obtain();
                     var4.writeToParcel(var6, 0);
                     var2x.add(var6);
                  }
               } else {
                  var5 = null;
               }

               var2.sendResult(var5);
            }
         };
         MediaBrowserServiceCompat.this.onLoadChildren(var1, var3);
      }

      public void setSessionToken(final MediaSessionCompat.Token var1) {
         MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
            public void run() {
               if (!MediaBrowserServiceImplApi21.this.mRootExtrasList.isEmpty()) {
                  IMediaSession var1x = var1.getExtraBinder();
                  if (var1x != null) {
                     Iterator var2 = MediaBrowserServiceImplApi21.this.mRootExtrasList.iterator();

                     while(var2.hasNext()) {
                        BundleCompat.putBinder((Bundle)var2.next(), "extra_session_binder", var1x.asBinder());
                     }
                  }

                  MediaBrowserServiceImplApi21.this.mRootExtrasList.clear();
               }

               MediaBrowserServiceCompatApi21.setSessionToken(MediaBrowserServiceImplApi21.this.mServiceObj, var1.getToken());
            }
         });
      }
   }

   @RequiresApi(23)
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

            void onResultSent(MediaBrowserCompat.MediaItem var1) {
               if (var1 == null) {
                  var2.sendResult((Object)null);
               } else {
                  Parcel var2x = Parcel.obtain();
                  var1.writeToParcel(var2x, 0);
                  var2.sendResult(var2x);
               }

            }
         };
         MediaBrowserServiceCompat.this.onLoadItem(var1, var3);
      }
   }

   @RequiresApi(26)
   class MediaBrowserServiceImplApi24 extends MediaBrowserServiceCompat.MediaBrowserServiceImplApi23 implements MediaBrowserServiceCompatApi24.ServiceCompatProxy {
      MediaBrowserServiceImplApi24() {
         super();
      }

      public Bundle getBrowserRootHints() {
         if (MediaBrowserServiceCompat.this.mCurConnection != null) {
            Bundle var1;
            if (MediaBrowserServiceCompat.this.mCurConnection.rootHints == null) {
               var1 = null;
            } else {
               var1 = new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
            }

            return var1;
         } else {
            return MediaBrowserServiceCompatApi24.getBrowserRootHints(this.mServiceObj);
         }
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

            void onResultSent(List var1) {
               ArrayList var5;
               if (var1 != null) {
                  ArrayList var2x = new ArrayList();
                  Iterator var3 = var1.iterator();

                  while(true) {
                     var5 = var2x;
                     if (!var3.hasNext()) {
                        break;
                     }

                     MediaBrowserCompat.MediaItem var4 = (MediaBrowserCompat.MediaItem)var3.next();
                     Parcel var6 = Parcel.obtain();
                     var4.writeToParcel(var6, 0);
                     var2x.add(var6);
                  }
               } else {
                  var5 = null;
               }

               var2.sendResult(var5, this.getFlags());
            }
         };
         MediaBrowserServiceCompat.this.onLoadChildren(var1, var4, var3);
      }
   }

   class MediaBrowserServiceImplBase implements MediaBrowserServiceCompat.MediaBrowserServiceImpl {
      private Messenger mMessenger;

      public Bundle getBrowserRootHints() {
         if (MediaBrowserServiceCompat.this.mCurConnection == null) {
            throw new IllegalStateException("This should be called inside of onLoadChildren, onLoadItem or onSearch methods");
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
         return "android.media.browse.MediaBrowserService".equals(var1.getAction()) ? this.mMessenger.getBinder() : null;
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
                     StringBuilder var3 = new StringBuilder();
                     var3.append("Connection for ");
                     var3.append(var2.pkg);
                     var3.append(" is no longer valid.");
                     Log.w("MBServiceCompat", var3.toString());
                     var1x.remove();
                  }
               }

            }
         });
      }
   }

   public static class Result {
      private final Object mDebug;
      private boolean mDetachCalled;
      private int mFlags;
      private boolean mSendErrorCalled;
      private boolean mSendProgressUpdateCalled;
      private boolean mSendResultCalled;

      Result(Object var1) {
         this.mDebug = var1;
      }

      private void checkExtraFields(Bundle var1) {
         if (var1 != null) {
            if (var1.containsKey("android.media.browse.extra.DOWNLOAD_PROGRESS")) {
               float var2 = var1.getFloat("android.media.browse.extra.DOWNLOAD_PROGRESS");
               if (var2 < -1.0E-5F || var2 > 1.00001F) {
                  throw new IllegalArgumentException("The value of the EXTRA_DOWNLOAD_PROGRESS field must be a float number within [0.0, 1.0].");
               }
            }

         }
      }

      public void detach() {
         StringBuilder var1;
         if (this.mDetachCalled) {
            var1 = new StringBuilder();
            var1.append("detach() called when detach() had already been called for: ");
            var1.append(this.mDebug);
            throw new IllegalStateException(var1.toString());
         } else if (this.mSendResultCalled) {
            var1 = new StringBuilder();
            var1.append("detach() called when sendResult() had already been called for: ");
            var1.append(this.mDebug);
            throw new IllegalStateException(var1.toString());
         } else if (this.mSendErrorCalled) {
            var1 = new StringBuilder();
            var1.append("detach() called when sendError() had already been called for: ");
            var1.append(this.mDebug);
            throw new IllegalStateException(var1.toString());
         } else {
            this.mDetachCalled = true;
         }
      }

      int getFlags() {
         return this.mFlags;
      }

      boolean isDone() {
         boolean var1;
         if (!this.mDetachCalled && !this.mSendResultCalled && !this.mSendErrorCalled) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      void onErrorSent(Bundle var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("It is not supported to send an error for ");
         var2.append(this.mDebug);
         throw new UnsupportedOperationException(var2.toString());
      }

      void onProgressUpdateSent(Bundle var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("It is not supported to send an interim update for ");
         var2.append(this.mDebug);
         throw new UnsupportedOperationException(var2.toString());
      }

      void onResultSent(Object var1) {
      }

      public void sendError(Bundle var1) {
         if (!this.mSendResultCalled && !this.mSendErrorCalled) {
            this.mSendErrorCalled = true;
            this.onErrorSent(var1);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("sendError() called when either sendResult() or sendError() had already been called for: ");
            var2.append(this.mDebug);
            throw new IllegalStateException(var2.toString());
         }
      }

      public void sendProgressUpdate(Bundle var1) {
         if (!this.mSendResultCalled && !this.mSendErrorCalled) {
            this.checkExtraFields(var1);
            this.mSendProgressUpdateCalled = true;
            this.onProgressUpdateSent(var1);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("sendProgressUpdate() called when either sendResult() or sendError() had already been called for: ");
            var2.append(this.mDebug);
            throw new IllegalStateException(var2.toString());
         }
      }

      public void sendResult(Object var1) {
         if (!this.mSendResultCalled && !this.mSendErrorCalled) {
            this.mSendResultCalled = true;
            this.onResultSent(var1);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("sendResult() called when either sendResult() or sendError() had already been called for: ");
            var2.append(this.mDebug);
            throw new IllegalStateException(var2.toString());
         }
      }

      void setFlags(int var1) {
         this.mFlags = var1;
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   private @interface ResultFlags {
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
                  StringBuilder var3x = new StringBuilder();
                  var3x.append("addSubscription for callback that isn't registered id=");
                  var3x.append(var1);
                  Log.w("MBServiceCompat", var3x.toString());
               } else {
                  MediaBrowserServiceCompat.this.addSubscription(var1, var2x, var2, var3);
               }
            }
         });
      }

      public void connect(final String var1, final int var2, final Bundle var3, final MediaBrowserServiceCompat.ServiceCallbacks var4) {
         if (!MediaBrowserServiceCompat.this.isValidPackage(var1, var2)) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Package/uid mismatch: uid=");
            var5.append(var2);
            var5.append(" package=");
            var5.append(var1);
            throw new IllegalArgumentException(var5.toString());
         } else {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
               public void run() {
                  IBinder var1x = var4.asBinder();
                  MediaBrowserServiceCompat.this.mConnections.remove(var1x);
                  MediaBrowserServiceCompat.ConnectionRecord var2x = new MediaBrowserServiceCompat.ConnectionRecord();
                  var2x.pkg = var1;
                  var2x.rootHints = var3;
                  var2x.callbacks = var4;
                  var2x.root = MediaBrowserServiceCompat.this.onGetRoot(var1, var2, var3);
                  if (var2x.root == null) {
                     StringBuilder var5 = new StringBuilder();
                     var5.append("No root for client ");
                     var5.append(var1);
                     var5.append(" from service ");
                     var5.append(this.getClass().getName());
                     Log.i("MBServiceCompat", var5.toString());

                     try {
                        var4.onConnectFailed();
                     } catch (RemoteException var4x) {
                        var5 = new StringBuilder();
                        var5.append("Calling onConnectFailed() failed. Ignoring. pkg=");
                        var5.append(var1);
                        Log.w("MBServiceCompat", var5.toString());
                     }
                  } else {
                     try {
                        MediaBrowserServiceCompat.this.mConnections.put(var1x, var2x);
                        if (MediaBrowserServiceCompat.this.mSession != null) {
                           var4.onConnect(var2x.root.getRootId(), MediaBrowserServiceCompat.this.mSession, var2x.root.getExtras());
                        }
                     } catch (RemoteException var3x) {
                        StringBuilder var6 = new StringBuilder();
                        var6.append("Calling onConnect() failed. Dropping client. pkg=");
                        var6.append(var1);
                        Log.w("MBServiceCompat", var6.toString());
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
               MediaBrowserServiceCompat.ConnectionRecord var2 = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.remove(var1x);
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
                     StringBuilder var3x = new StringBuilder();
                     var3x.append("getMediaItem for callback that isn't registered id=");
                     var3x.append(var1);
                     Log.w("MBServiceCompat", var3x.toString());
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
               MediaBrowserServiceCompat.ConnectionRecord var2x = new MediaBrowserServiceCompat.ConnectionRecord();
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
               StringBuilder var3x;
               if (var2x == null) {
                  var3x = new StringBuilder();
                  var3x.append("removeSubscription for callback that isn't registered id=");
                  var3x.append(var1);
                  Log.w("MBServiceCompat", var3x.toString());
               } else {
                  if (!MediaBrowserServiceCompat.this.removeSubscription(var1, var2x, var2)) {
                     var3x = new StringBuilder();
                     var3x.append("removeSubscription called for ");
                     var3x.append(var1);
                     var3x.append(" which is not subscribed");
                     Log.w("MBServiceCompat", var3x.toString());
                  }

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
                     StringBuilder var3x = new StringBuilder();
                     var3x.append("search for callback that isn't registered query=");
                     var3x.append(var1);
                     Log.w("MBServiceCompat", var3x.toString());
                  } else {
                     MediaBrowserServiceCompat.this.performSearch(var1, var2, var2x, var3);
                  }
               }
            });
         }
      }

      public void sendCustomAction(final String var1, final Bundle var2, final ResultReceiver var3, final MediaBrowserServiceCompat.ServiceCallbacks var4) {
         if (!TextUtils.isEmpty(var1) && var3 != null) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
               public void run() {
                  IBinder var1x = var4.asBinder();
                  MediaBrowserServiceCompat.ConnectionRecord var2x = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(var1x);
                  if (var2x == null) {
                     StringBuilder var3x = new StringBuilder();
                     var3x.append("sendCustomAction for callback that isn't registered action=");
                     var3x.append(var1);
                     var3x.append(", extras=");
                     var3x.append(var2);
                     Log.w("MBServiceCompat", var3x.toString());
                  } else {
                     MediaBrowserServiceCompat.this.performCustomAction(var1, var2, var2x, var3);
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

   private static class ServiceCallbacksCompat implements MediaBrowserServiceCompat.ServiceCallbacks {
      final Messenger mCallbacks;

      ServiceCallbacksCompat(Messenger var1) {
         this.mCallbacks = var1;
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
            this.mServiceBinderImpl.connect(var2.getString("data_package_name"), var2.getInt("data_calling_uid"), var2.getBundle("data_root_hints"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            break;
         case 2:
            this.mServiceBinderImpl.disconnect(new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            break;
         case 3:
            this.mServiceBinderImpl.addSubscription(var2.getString("data_media_item_id"), BundleCompat.getBinder(var2, "data_callback_token"), var2.getBundle("data_options"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            break;
         case 4:
            this.mServiceBinderImpl.removeSubscription(var2.getString("data_media_item_id"), BundleCompat.getBinder(var2, "data_callback_token"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            break;
         case 5:
            this.mServiceBinderImpl.getMediaItem(var2.getString("data_media_item_id"), (ResultReceiver)var2.getParcelable("data_result_receiver"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            break;
         case 6:
            this.mServiceBinderImpl.registerCallbacks(new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo), var2.getBundle("data_root_hints"));
            break;
         case 7:
            this.mServiceBinderImpl.unregisterCallbacks(new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            break;
         case 8:
            this.mServiceBinderImpl.search(var2.getString("data_search_query"), var2.getBundle("data_search_extras"), (ResultReceiver)var2.getParcelable("data_result_receiver"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            break;
         case 9:
            this.mServiceBinderImpl.sendCustomAction(var2.getString("data_custom_action"), var2.getBundle("data_custom_action_extras"), (ResultReceiver)var2.getParcelable("data_result_receiver"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(var1.replyTo));
            break;
         default:
            StringBuilder var3 = new StringBuilder();
            var3.append("Unhandled message: ");
            var3.append(var1);
            var3.append("\n  Service version: ");
            var3.append(1);
            var3.append("\n  Client version: ");
            var3.append(var1.arg1);
            Log.w("MBServiceCompat", var3.toString());
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
