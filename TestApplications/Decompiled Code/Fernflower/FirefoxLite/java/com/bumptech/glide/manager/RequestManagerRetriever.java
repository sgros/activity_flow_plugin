package com.bumptech.glide.manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;

public class RequestManagerRetriever implements Callback {
   private static final RequestManagerRetriever.RequestManagerFactory DEFAULT_FACTORY = new RequestManagerRetriever.RequestManagerFactory() {
      public RequestManager build(Glide var1, Lifecycle var2, RequestManagerTreeNode var3) {
         return new RequestManager(var1, var2, var3);
      }
   };
   private volatile RequestManager applicationManager;
   private final RequestManagerRetriever.RequestManagerFactory factory;
   private final Handler handler;
   final Map pendingRequestManagerFragments = new HashMap();
   final Map pendingSupportRequestManagerFragments = new HashMap();
   private final Bundle tempBundle = new Bundle();
   private final ArrayMap tempViewToFragment = new ArrayMap();
   private final ArrayMap tempViewToSupportFragment = new ArrayMap();

   public RequestManagerRetriever(RequestManagerRetriever.RequestManagerFactory var1) {
      if (var1 == null) {
         var1 = DEFAULT_FACTORY;
      }

      this.factory = var1;
      this.handler = new Handler(Looper.getMainLooper(), this);
   }

   @TargetApi(17)
   private static void assertNotDestroyed(Activity var0) {
      if (VERSION.SDK_INT >= 17 && var0.isDestroyed()) {
         throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
      }
   }

   private RequestManager fragmentGet(Context var1, FragmentManager var2, Fragment var3) {
      RequestManagerFragment var4 = this.getRequestManagerFragment(var2, var3);
      RequestManager var7 = var4.getRequestManager();
      RequestManager var6 = var7;
      if (var7 == null) {
         Glide var5 = Glide.get(var1);
         var6 = this.factory.build(var5, var4.getGlideLifecycle(), var4.getRequestManagerTreeNode());
         var4.setRequestManager(var6);
      }

      return var6;
   }

   private RequestManager getApplicationManager(Context var1) {
      if (this.applicationManager == null) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (this.applicationManager == null) {
                  Glide var2 = Glide.get(var1);
                  RequestManagerRetriever.RequestManagerFactory var17 = this.factory;
                  ApplicationLifecycle var3 = new ApplicationLifecycle();
                  EmptyRequestManagerTreeNode var4 = new EmptyRequestManagerTreeNode();
                  this.applicationManager = var17.build(var2, var3, var4);
               }
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return this.applicationManager;
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var18 = var10000;

            try {
               throw var18;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               continue;
            }
         }
      } else {
         return this.applicationManager;
      }
   }

   private RequestManager supportFragmentGet(Context var1, android.support.v4.app.FragmentManager var2, android.support.v4.app.Fragment var3) {
      SupportRequestManagerFragment var4 = this.getSupportRequestManagerFragment(var2, var3);
      RequestManager var7 = var4.getRequestManager();
      RequestManager var6 = var7;
      if (var7 == null) {
         Glide var5 = Glide.get(var1);
         var6 = this.factory.build(var5, var4.getGlideLifecycle(), var4.getRequestManagerTreeNode());
         var4.setRequestManager(var6);
      }

      return var6;
   }

   public RequestManager get(Activity var1) {
      if (Util.isOnBackgroundThread()) {
         return this.get(var1.getApplicationContext());
      } else {
         assertNotDestroyed(var1);
         return this.fragmentGet(var1, var1.getFragmentManager(), (Fragment)null);
      }
   }

   public RequestManager get(Context var1) {
      if (var1 != null) {
         if (Util.isOnMainThread() && !(var1 instanceof Application)) {
            if (var1 instanceof FragmentActivity) {
               return this.get((FragmentActivity)var1);
            }

            if (var1 instanceof Activity) {
               return this.get((Activity)var1);
            }

            if (var1 instanceof ContextWrapper) {
               return this.get(((ContextWrapper)var1).getBaseContext());
            }
         }

         return this.getApplicationManager(var1);
      } else {
         throw new IllegalArgumentException("You cannot start a load on a null Context");
      }
   }

   public RequestManager get(android.support.v4.app.Fragment var1) {
      Preconditions.checkNotNull(var1.getActivity(), "You cannot start a load on a fragment before it is attached or after it is destroyed");
      if (Util.isOnBackgroundThread()) {
         return this.get(var1.getActivity().getApplicationContext());
      } else {
         android.support.v4.app.FragmentManager var2 = var1.getChildFragmentManager();
         return this.supportFragmentGet(var1.getActivity(), var2, var1);
      }
   }

   public RequestManager get(FragmentActivity var1) {
      if (Util.isOnBackgroundThread()) {
         return this.get(var1.getApplicationContext());
      } else {
         assertNotDestroyed(var1);
         return this.supportFragmentGet(var1, var1.getSupportFragmentManager(), (android.support.v4.app.Fragment)null);
      }
   }

   @TargetApi(17)
   RequestManagerFragment getRequestManagerFragment(FragmentManager var1, Fragment var2) {
      RequestManagerFragment var3 = (RequestManagerFragment)var1.findFragmentByTag("com.bumptech.glide.manager");
      RequestManagerFragment var4 = var3;
      if (var3 == null) {
         var3 = (RequestManagerFragment)this.pendingRequestManagerFragments.get(var1);
         var4 = var3;
         if (var3 == null) {
            var4 = new RequestManagerFragment();
            var4.setParentFragmentHint(var2);
            this.pendingRequestManagerFragments.put(var1, var4);
            var1.beginTransaction().add(var4, "com.bumptech.glide.manager").commitAllowingStateLoss();
            this.handler.obtainMessage(1, var1).sendToTarget();
         }
      }

      return var4;
   }

   SupportRequestManagerFragment getSupportRequestManagerFragment(android.support.v4.app.FragmentManager var1, android.support.v4.app.Fragment var2) {
      SupportRequestManagerFragment var3 = (SupportRequestManagerFragment)var1.findFragmentByTag("com.bumptech.glide.manager");
      SupportRequestManagerFragment var4 = var3;
      if (var3 == null) {
         var3 = (SupportRequestManagerFragment)this.pendingSupportRequestManagerFragments.get(var1);
         var4 = var3;
         if (var3 == null) {
            var4 = new SupportRequestManagerFragment();
            var4.setParentFragmentHint(var2);
            this.pendingSupportRequestManagerFragments.put(var1, var4);
            var1.beginTransaction().add(var4, "com.bumptech.glide.manager").commitAllowingStateLoss();
            this.handler.obtainMessage(2, var1).sendToTarget();
         }
      }

      return var4;
   }

   public boolean handleMessage(Message var1) {
      int var2 = var1.what;
      Object var3 = null;
      boolean var4 = true;
      Object var5;
      Object var6;
      switch(var2) {
      case 1:
         var6 = (FragmentManager)var1.obj;
         var5 = this.pendingRequestManagerFragments.remove(var6);
         break;
      case 2:
         var6 = (android.support.v4.app.FragmentManager)var1.obj;
         var5 = this.pendingSupportRequestManagerFragments.remove(var6);
         break;
      default:
         var4 = false;
         var5 = null;
         var6 = var3;
      }

      if (var4 && var5 == null && Log.isLoggable("RMRetriever", 5)) {
         StringBuilder var7 = new StringBuilder();
         var7.append("Failed to remove expected request manager fragment, manager: ");
         var7.append(var6);
         Log.w("RMRetriever", var7.toString());
      }

      return var4;
   }

   public interface RequestManagerFactory {
      RequestManager build(Glide var1, Lifecycle var2, RequestManagerTreeNode var3);
   }
}
