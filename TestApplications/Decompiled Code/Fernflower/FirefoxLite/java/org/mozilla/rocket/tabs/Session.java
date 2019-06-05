package org.mozilla.rocket.tabs;

import android.arch.lifecycle.LifecycleOwner;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ObservableProperty;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;
import kotlin.text.StringsKt;
import mozilla.components.browser.session.Download;
import mozilla.components.support.base.observer.Consumable;
import mozilla.components.support.base.observer.Observable;
import mozilla.components.support.base.observer.ObserverRegistry;

public final class Session implements Observable {
   // $FF: synthetic field
   static final KProperty[] $$delegatedProperties = new KProperty[]{(KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "url", "getUrl()Ljava/lang/String;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "title", "getTitle()Ljava/lang/String;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "progress", "getProgress()I")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "loading", "getLoading()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "canGoBack", "getCanGoBack()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "canGoForward", "getCanGoForward()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "securityInfo", "getSecurityInfo()Lmozilla/components/browser/session/Session$SecurityInfo;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "download", "getDownload()Lmozilla/components/support/base/observer/Consumable;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "findResults", "getFindResults()Ljava/util/List;"))};
   public static final Session.Companion Companion = new Session.Companion((DefaultConstructorMarker)null);
   private final ReadWriteProperty canGoBack$delegate;
   private final ReadWriteProperty canGoForward$delegate;
   private final Observable delegate;
   private final ReadWriteProperty download$delegate;
   private TabViewEngineSession.Observer engineObserver;
   private TabViewEngineSession engineSession;
   private Bitmap favicon;
   private final ReadWriteProperty findResults$delegate;
   private final String id;
   private String initialUrl;
   private final ReadWriteProperty loading$delegate;
   private String parentId;
   private final ReadWriteProperty progress$delegate;
   private final ReadWriteProperty securityInfo$delegate;
   private final ReadWriteProperty title$delegate;
   private final ReadWriteProperty url$delegate;

   public Session() {
      this((String)null, (String)null, (String)null, (Observable)null, 15, (DefaultConstructorMarker)null);
   }

   public Session(String var1, String var2, String var3) {
      this(var1, var2, var3, (Observable)null, 8, (DefaultConstructorMarker)null);
   }

   public Session(final String var1, String var2, String var3, Observable var4) {
      Intrinsics.checkParameterIsNotNull(var1, "id");
      Intrinsics.checkParameterIsNotNull(var4, "delegate");
      super();
      this.id = var1;
      this.parentId = var2;
      this.initialUrl = var3;
      this.delegate = var4;
      Delegates var5 = Delegates.INSTANCE;
      var1 = this.initialUrl;
      this.url$delegate = (ReadWriteProperty)(new ObservableProperty(var1) {
         protected void afterChange(KProperty var1x, Object var2, Object var3) {
            Intrinsics.checkParameterIsNotNull(var1x, "property");
            final String var4 = (String)var3;
            String var5 = (String)var2;
            if (var5 != null && var4 != null) {
               Session.this.notifyObservers(var5, var4, (Function1)(new Function1() {
                  public final void invoke(Session.Observer var1x) {
                     Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
                     var1x.onUrlChanged(Session.this, var4);
                  }
               }));
            }

         }
      });
      var5 = Delegates.INSTANCE;
      this.title$delegate = (ReadWriteProperty)(new ObservableProperty("", "") {
         // $FF: synthetic field
         final Object $initialValue;

         public {
            this.$initialValue = var1;
         }

         protected void afterChange(KProperty var1, Object var2, Object var3) {
            Intrinsics.checkParameterIsNotNull(var1, "property");
            final String var4 = (String)var3;
            String var5 = (String)var2;
            Session.this.notifyObservers(var5, var4, (Function1)(new Function1() {
               public final void invoke(Session.Observer var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  var1.onTitleChanged(Session.this, var4);
               }
            }));
         }
      });
      var5 = Delegates.INSTANCE;
      final Integer var6 = 0;
      this.progress$delegate = (ReadWriteProperty)(new ObservableProperty(var6) {
         protected void afterChange(KProperty var1, Object var2, Object var3) {
            Intrinsics.checkParameterIsNotNull(var1, "property");
            final int var4 = ((Number)var3).intValue();
            int var5 = ((Number)var2).intValue();
            Session.this.notifyObservers(var5, var4, (Function1)(new Function1() {
               public final void invoke(Session.Observer var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  var1.onProgress(Session.this, var4);
               }
            }));
         }
      });
      var5 = Delegates.INSTANCE;
      final Boolean var7 = false;
      this.loading$delegate = (ReadWriteProperty)(new ObservableProperty(var7) {
         protected void afterChange(KProperty var1, Object var2, Object var3) {
            Intrinsics.checkParameterIsNotNull(var1, "property");
            final boolean var4 = (Boolean)var3;
            boolean var5 = (Boolean)var2;
            Session.this.notifyObservers(var5, var4, (Function1)(new Function1() {
               public final void invoke(Session.Observer var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  var1.onLoadingStateChanged(Session.this, var4);
               }
            }));
         }
      });
      var5 = Delegates.INSTANCE;
      var7 = false;
      this.canGoBack$delegate = (ReadWriteProperty)(new ObservableProperty(var7) {
         protected void afterChange(KProperty var1, Object var2, Object var3) {
            Intrinsics.checkParameterIsNotNull(var1, "property");
            final boolean var4 = (Boolean)var3;
            boolean var5 = (Boolean)var2;
            Session.this.notifyObservers(var5, var4, (Function1)(new Function1() {
               public final void invoke(Session.Observer var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  var1.onNavigationStateChanged(Session.this, var4, Session.this.getCanGoForward());
               }
            }));
         }
      });
      var5 = Delegates.INSTANCE;
      var7 = false;
      this.canGoForward$delegate = (ReadWriteProperty)(new ObservableProperty(var7) {
         protected void afterChange(KProperty var1, Object var2, Object var3) {
            Intrinsics.checkParameterIsNotNull(var1, "property");
            final boolean var4 = (Boolean)var3;
            boolean var5 = (Boolean)var2;
            Session.this.notifyObservers(var5, var4, (Function1)(new Function1() {
               public final void invoke(Session.Observer var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  var1.onNavigationStateChanged(Session.this, Session.this.getCanGoBack(), var4);
               }
            }));
         }
      });
      var5 = Delegates.INSTANCE;
      final mozilla.components.browser.session.Session.SecurityInfo var8 = new mozilla.components.browser.session.Session.SecurityInfo(false, (String)null, (String)null, 7, (DefaultConstructorMarker)null);
      this.securityInfo$delegate = (ReadWriteProperty)(new ObservableProperty(var8) {
         protected void afterChange(KProperty var1, Object var2, Object var3) {
            Intrinsics.checkParameterIsNotNull(var1, "property");
            final mozilla.components.browser.session.Session.SecurityInfo var4 = (mozilla.components.browser.session.Session.SecurityInfo)var3;
            mozilla.components.browser.session.Session.SecurityInfo var5 = (mozilla.components.browser.session.Session.SecurityInfo)var2;
            Session.this.notifyObservers(var5, var4, (Function1)(new Function1() {
               public final void invoke(Session.Observer var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  var1.onSecurityChanged(Session.this, var4.getSecure());
               }
            }));
         }
      });
      var5 = Delegates.INSTANCE;
      final Consumable var9 = Consumable.Companion.empty();
      this.download$delegate = (ReadWriteProperty)(new ObservableProperty(var9) {
         protected boolean beforeChange(KProperty var1, Object var2, Object var3) {
            Intrinsics.checkParameterIsNotNull(var1, "property");
            Consumable var4 = (Consumable)var3;
            Consumable var5 = (Consumable)var2;
            return var4.consumeBy(Session.this.wrapConsumers((Function2)(new Function2() {
               public final boolean invoke(Session.Observer var1, Download var2) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  Intrinsics.checkParameterIsNotNull(var2, "it");
                  return var1.onDownload(Session.this, var2);
               }
            }))) ^ true;
         }
      });
      var5 = Delegates.INSTANCE;
      final List var10 = CollectionsKt.emptyList();
      this.findResults$delegate = (ReadWriteProperty)(new ObservableProperty(var10) {
         protected void afterChange(KProperty var1, Object var2, Object var3) {
            Intrinsics.checkParameterIsNotNull(var1, "property");
            final List var4 = (List)var3;
            List var5 = (List)var2;
            Session.this.notifyObservers(var5, var4, (Function1)(new Function1() {
               public final void invoke(Session.Observer var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  if (((Collection)var4).isEmpty() ^ true) {
                     var1.onFindResult(Session.this, (mozilla.components.browser.session.Session.FindResult)CollectionsKt.last(Session.this.getFindResults()));
                  }

               }
            }));
         }
      });
   }

   // $FF: synthetic method
   public Session(String var1, String var2, String var3, Observable var4, int var5, DefaultConstructorMarker var6) {
      if ((var5 & 1) != 0) {
         var1 = UUID.randomUUID().toString();
         Intrinsics.checkExpressionValueIsNotNull(var1, "UUID.randomUUID().toString()");
      }

      if ((var5 & 2) != 0) {
         var2 = "";
      }

      if ((var5 & 4) != 0) {
         var3 = "";
      }

      if ((var5 & 8) != 0) {
         var4 = (Observable)(new ObserverRegistry());
      }

      this(var1, var2, var3, var4);
   }

   private final void notifyObservers(Object var1, Object var2, Function1 var3) {
      if (Intrinsics.areEqual(var1, var2) ^ true) {
         this.notifyObservers(var3);
      }

   }

   public final boolean getCanGoBack() {
      return (Boolean)this.canGoBack$delegate.getValue(this, $$delegatedProperties[4]);
   }

   public final boolean getCanGoForward() {
      return (Boolean)this.canGoForward$delegate.getValue(this, $$delegatedProperties[5]);
   }

   public final TabViewEngineSession.Observer getEngineObserver() {
      return this.engineObserver;
   }

   public final TabViewEngineSession getEngineSession() {
      return this.engineSession;
   }

   public final Bitmap getFavicon() {
      return this.favicon;
   }

   public final List getFindResults() {
      return (List)this.findResults$delegate.getValue(this, $$delegatedProperties[8]);
   }

   public final String getId() {
      return this.id;
   }

   public final String getInitialUrl() {
      return this.initialUrl;
   }

   public final String getParentId() {
      return this.parentId;
   }

   public final mozilla.components.browser.session.Session.SecurityInfo getSecurityInfo() {
      return (mozilla.components.browser.session.Session.SecurityInfo)this.securityInfo$delegate.getValue(this, $$delegatedProperties[6]);
   }

   public final String getTitle() {
      return (String)this.title$delegate.getValue(this, $$delegatedProperties[1]);
   }

   public final String getUrl() {
      return (String)this.url$delegate.getValue(this, $$delegatedProperties[0]);
   }

   public final boolean hasParentTab() {
      boolean var1;
      if (!this.isFromExternal() && !TextUtils.isEmpty((CharSequence)this.parentId)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final boolean isFromExternal() {
      return Intrinsics.areEqual("_open_from_external_", this.parentId);
   }

   public final boolean isValid() {
      boolean var1 = StringsKt.isBlank((CharSequence)this.id);
      boolean var2 = true;
      if (var1 ^ true) {
         String var3 = this.getUrl();
         boolean var4;
         if (var3 != null) {
            var4 = StringsKt.isBlank((CharSequence)var3) ^ true;
         } else {
            var4 = false;
         }

         if (var4) {
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public void notifyObservers(Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "block");
      this.delegate.notifyObservers(var1);
   }

   public void register(Session.Observer var1) {
      Intrinsics.checkParameterIsNotNull(var1, "observer");
      this.delegate.register(var1);
   }

   public void register(Session.Observer var1, LifecycleOwner var2, boolean var3) {
      Intrinsics.checkParameterIsNotNull(var1, "observer");
      Intrinsics.checkParameterIsNotNull(var2, "owner");
      this.delegate.register(var1, var2, var3);
   }

   public final void setCanGoBack(boolean var1) {
      this.canGoBack$delegate.setValue(this, $$delegatedProperties[4], var1);
   }

   public final void setCanGoForward(boolean var1) {
      this.canGoForward$delegate.setValue(this, $$delegatedProperties[5], var1);
   }

   public final void setDownload(Consumable var1) {
      Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
      this.download$delegate.setValue(this, $$delegatedProperties[7], var1);
   }

   public final void setEngineObserver(TabViewEngineSession.Observer var1) {
      this.engineObserver = var1;
   }

   public final void setEngineSession(TabViewEngineSession var1) {
      this.engineSession = var1;
   }

   public final void setFavicon(Bitmap var1) {
      this.favicon = var1;
   }

   public final void setFindResults(List var1) {
      Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
      this.findResults$delegate.setValue(this, $$delegatedProperties[8], var1);
   }

   public final void setLoading(boolean var1) {
      this.loading$delegate.setValue(this, $$delegatedProperties[3], var1);
   }

   public final void setParentId(String var1) {
      this.parentId = var1;
   }

   public final void setProgress(int var1) {
      this.progress$delegate.setValue(this, $$delegatedProperties[2], var1);
   }

   public final void setSecurityInfo(mozilla.components.browser.session.Session.SecurityInfo var1) {
      Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
      this.securityInfo$delegate.setValue(this, $$delegatedProperties[6], var1);
   }

   public final void setTitle(String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
      this.title$delegate.setValue(this, $$delegatedProperties[1], var1);
   }

   public final void setUrl(String var1) {
      this.url$delegate.setValue(this, $$delegatedProperties[0], var1);
   }

   public void unregister(Session.Observer var1) {
      Intrinsics.checkParameterIsNotNull(var1, "observer");
      this.delegate.unregister(var1);
   }

   public void unregisterObservers() {
      this.delegate.unregisterObservers();
   }

   public List wrapConsumers(Function2 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "block");
      return this.delegate.wrapConsumers(var1);
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }

   public interface Observer {
      boolean onDownload(Session var1, Download var2);

      void onEnterFullScreen(TabView.FullscreenCallback var1, View var2);

      void onExitFullScreen();

      void onFindResult(Session var1, mozilla.components.browser.session.Session.FindResult var2);

      void onGeolocationPermissionsShowPrompt(String var1, Callback var2);

      void onLoadingStateChanged(Session var1, boolean var2);

      void onLongPress(Session var1, TabView.HitTarget var2);

      void onNavigationStateChanged(Session var1, boolean var2, boolean var3);

      void onProgress(Session var1, int var2);

      void onReceivedIcon(Bitmap var1);

      void onSecurityChanged(Session var1, boolean var2);

      void onTitleChanged(Session var1, String var2);

      void onUrlChanged(Session var1, String var2);

      public static final class DefaultImpls {
         public static boolean onDownload(Session.Observer var0, Session var1, Download var2) {
            Intrinsics.checkParameterIsNotNull(var1, "session");
            Intrinsics.checkParameterIsNotNull(var2, "download");
            return false;
         }

         public static void onEnterFullScreen(Session.Observer var0, TabView.FullscreenCallback var1, View var2) {
            Intrinsics.checkParameterIsNotNull(var1, "callback");
         }

         public static void onExitFullScreen(Session.Observer var0) {
         }

         public static void onFindResult(Session.Observer var0, Session var1, mozilla.components.browser.session.Session.FindResult var2) {
            Intrinsics.checkParameterIsNotNull(var1, "session");
            Intrinsics.checkParameterIsNotNull(var2, "result");
         }

         public static void onGeolocationPermissionsShowPrompt(Session.Observer var0, String var1, Callback var2) {
            Intrinsics.checkParameterIsNotNull(var1, "origin");
         }

         public static void onLoadingStateChanged(Session.Observer var0, Session var1, boolean var2) {
            Intrinsics.checkParameterIsNotNull(var1, "session");
         }

         public static void onLongPress(Session.Observer var0, Session var1, TabView.HitTarget var2) {
            Intrinsics.checkParameterIsNotNull(var1, "session");
            Intrinsics.checkParameterIsNotNull(var2, "hitTarget");
         }

         public static void onNavigationStateChanged(Session.Observer var0, Session var1, boolean var2, boolean var3) {
            Intrinsics.checkParameterIsNotNull(var1, "session");
         }

         public static void onProgress(Session.Observer var0, Session var1, int var2) {
            Intrinsics.checkParameterIsNotNull(var1, "session");
         }

         public static void onReceivedIcon(Session.Observer var0, Bitmap var1) {
         }

         public static void onSecurityChanged(Session.Observer var0, Session var1, boolean var2) {
            Intrinsics.checkParameterIsNotNull(var1, "session");
         }
      }
   }
}
