package org.mozilla.rocket.tabs;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import kotlin.Pair;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.support.base.observer.Consumable;
import mozilla.components.support.base.observer.Observable;
import mozilla.components.support.base.observer.ObserverRegistry;
import org.mozilla.rocket.tabs.utils.TabUtil;

public final class SessionManager implements Observable {
   // $FF: synthetic field
   private final Observable $$delegate_0;
   private WeakReference focusRef;
   private final SessionManager.Notifier notifier;
   private final LinkedList sessions;
   private final TabViewProvider tabViewProvider;

   public SessionManager(TabViewProvider var1) {
      this(var1, (Observable)null, 2, (DefaultConstructorMarker)null);
   }

   public SessionManager(TabViewProvider var1, Observable var2) {
      Intrinsics.checkParameterIsNotNull(var1, "tabViewProvider");
      Intrinsics.checkParameterIsNotNull(var2, "delegate");
      super();
      this.$$delegate_0 = var2;
      this.tabViewProvider = var1;
      this.sessions = new LinkedList();
      this.focusRef = new WeakReference((Object)null);
      this.notifier = new SessionManager.Notifier(this);
   }

   // $FF: synthetic method
   public SessionManager(TabViewProvider var1, Observable var2, int var3, DefaultConstructorMarker var4) {
      if ((var3 & 2) != 0) {
         var2 = (Observable)(new ObserverRegistry());
      }

      this(var1, var2);
   }

   private final String addTabInternal(String var1, String var2, boolean var3, boolean var4, Bundle var5) {
      Session var6 = new Session((String)null, (String)null, (String)null, (Observable)null, 15, (DefaultConstructorMarker)null);
      var6.setUrl(var1);
      int var7;
      if (TextUtils.isEmpty((CharSequence)var2)) {
         var7 = -1;
      } else {
         if (var2 == null) {
            Intrinsics.throwNpe();
         }

         var7 = this.getTabIndex(var2);
      }

      if (var3) {
         var6.setParentId("_open_from_external_");
         this.sessions.add(var6);
      } else {
         this.insertTab(var7, var6);
      }

      this.notifier.notifyTabAdded(var6, var5);
      WeakReference var8;
      if (!var4 && !var3) {
         var8 = this.focusRef;
      } else {
         var8 = new WeakReference(var6);
      }

      this.focusRef = var8;
      this.getOrCreateEngineSession(var6);
      this.initializeEngineView(var6);
      if (var4 || var3) {
         this.notifier.notifyTabFocused(var6, SessionManager.Factor.FACTOR_TAB_ADDED);
      }

      this.notifyObservers((Function1)(new Function1() {
         public final void invoke(SessionManager.Observer var1) {
            Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
            var1.onSessionCountChanged(SessionManager.this.sessions.size());
         }
      }));
      return var6.getId();
   }

   private final void destroySession(Session var1) {
      this.unlink(var1);
      var1.unregisterObservers();
   }

   private final Session getTab(String var1) {
      int var2 = this.getTabIndex(var1);
      Session var3;
      if (var2 == -1) {
         var3 = null;
      } else {
         var3 = (Session)this.sessions.get(var2);
      }

      return var3;
   }

   private final int getTabIndex(String var1) {
      if (var1 == null) {
         return -1;
      } else {
         int var2 = 0;

         for(int var3 = ((Collection)this.sessions).size(); var2 < var3; ++var2) {
            Object var4 = this.sessions.get(var2);
            Intrinsics.checkExpressionValueIsNotNull(var4, "sessions[i]");
            if (Intrinsics.areEqual(((Session)var4).getId(), var1)) {
               return var2;
            }
         }

         return -1;
      }
   }

   private final void initializeEngineView(Session var1) {
      if (var1.getEngineSession() == null) {
         this.getOrCreateEngineSession(var1);
      }

      String var2;
      if (TextUtils.isEmpty((CharSequence)var1.getUrl())) {
         var2 = var1.getInitialUrl();
      } else {
         var2 = var1.getUrl();
      }

      TabView var3 = this.tabViewProvider.create();
      TabViewEngineSession var4 = var1.getEngineSession();
      if (var4 != null) {
         var4.setTabView(var3);
      }

      var4 = var1.getEngineSession();
      Object var5 = null;
      Bundle var8;
      if (var4 != null) {
         var8 = var4.getWebViewState();
      } else {
         var8 = null;
      }

      if (var8 != null) {
         TabViewEngineSession var7 = var1.getEngineSession();
         Bundle var6 = (Bundle)var5;
         if (var7 != null) {
            var6 = var7.getWebViewState();
         }

         var3.restoreViewState(var6);
      } else if (!TextUtils.isEmpty((CharSequence)var2)) {
         var3.loadUrl(var2);
      }

   }

   private final void insertTab(int var1, Session var2) {
      Session var3;
      if (var1 >= 0 && var1 < this.sessions.size()) {
         var3 = (Session)this.sessions.get(var1);
      } else {
         var3 = null;
      }

      if (var3 == null) {
         this.sessions.add(var2);
      } else {
         this.sessions.add(var1 + 1, var2);
         Iterator var4 = this.sessions.iterator();

         while(var4.hasNext()) {
            Session var5 = (Session)var4.next();
            if (Intrinsics.areEqual(var3.getId(), var5.getParentId())) {
               var5.setParentId(var2.getId());
            }
         }

         var2.setParentId(var3.getId());
      }
   }

   private final void link(Session var1, TabViewEngineSession var2) {
      this.unlink(var1);
      TabViewEngineSession.Observer var3 = (TabViewEngineSession.Observer)(new TabViewEngineObserver(var1));
      var2.register(var3);
      var1.setEngineObserver(var3);
      var2.setWindowClient((TabViewEngineSession.WindowClient)(new SessionManager.WindowClient(var1)));
      var2.setEngineSessionClient((TabViewEngineSession.Client)(new SessionManager.Client()));
      var1.setEngineSession(var2);
   }

   private final void removeTabInternal(String var1, boolean var2) {
      Session var3 = this.getTab(var1);
      if (var3 != null) {
         int var4 = this.getTabIndex(var1);
         this.sessions.remove(var3);
         this.notifier.notifyTabRemoved(var3);
         Iterator var6 = this.sessions.iterator();

         while(var6.hasNext()) {
            Session var5 = (Session)var6.next();
            if (TextUtils.equals((CharSequence)var5.getParentId(), (CharSequence)var3.getId())) {
               var5.setParentId(var3.getParentId());
            }
         }

         if (var3 == (Session)this.focusRef.get()) {
            if (var2) {
               var4 = Math.min(var4, this.sessions.size() - 1);
               WeakReference var7;
               if (var4 == -1) {
                  var7 = new WeakReference((Object)null);
               } else {
                  var7 = new WeakReference(this.sessions.get(var4));
               }

               this.focusRef = var7;
            } else {
               this.updateFocusOnClosing(var3);
            }
         }

         this.notifyObservers((Function1)(new Function1() {
            public final void invoke(SessionManager.Observer var1) {
               Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
               var1.onSessionCountChanged(SessionManager.this.sessions.size());
            }
         }));
      }
   }

   private final void unlink(Session var1) {
      TabViewEngineSession.Observer var2 = var1.getEngineObserver();
      TabViewEngineSession var3;
      if (var2 != null) {
         var3 = var1.getEngineSession();
         if (var3 != null) {
            var3.unregister(var2);
         }
      }

      var3 = var1.getEngineSession();
      if (var3 != null) {
         var3.destroy$feature_tabs_release();
      }

      var1.setEngineSession((TabViewEngineSession)null);
      var1.setEngineObserver((TabViewEngineSession.Observer)null);
   }

   private final void updateFocusOnClosing(Session var1) {
      if (TextUtils.isEmpty((CharSequence)var1.getParentId())) {
         this.focusRef.clear();
         this.notifier.notifyTabFocused((Session)null, SessionManager.Factor.FACTOR_NO_FOCUS);
      } else if (TextUtils.equals((CharSequence)var1.getParentId(), (CharSequence)"_open_from_external_")) {
         this.focusRef.clear();
         this.notifier.notifyTabFocused((Session)null, SessionManager.Factor.FACTOR_BACK_EXTERNAL);
      } else {
         String var2 = var1.getParentId();
         if (var2 == null) {
            Intrinsics.throwNpe();
         }

         this.focusRef = new WeakReference(this.getTab(var2));
         this.notifier.notifyTabFocused((Session)this.focusRef.get(), SessionManager.Factor.FACTOR_TAB_REMOVED);
      }

   }

   public final String addTab(String var1, Bundle var2) {
      Intrinsics.checkParameterIsNotNull(var1, "url");
      Intrinsics.checkParameterIsNotNull(var2, "arguments");
      if (TextUtils.isEmpty((CharSequence)var1)) {
         var1 = null;
      } else {
         var1 = this.addTabInternal(var1, TabUtil.getParentId(var2), TabUtil.isFromExternal(var2), TabUtil.toFocus(var2), var2);
      }

      return var1;
   }

   public final void closeTab(String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "id");
      this.removeTabInternal(var1, false);
   }

   public final void destroy() {
      Iterator var1 = this.sessions.iterator();

      while(var1.hasNext()) {
         Session var2 = (Session)var1.next();
         Intrinsics.checkExpressionValueIsNotNull(var2, "tab");
         this.destroySession(var2);
      }

   }

   public final void dropTab(String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "id");
      this.removeTabInternal(var1, true);
   }

   public final TabViewEngineSession getEngineSession(Session var1) {
      Intrinsics.checkParameterIsNotNull(var1, "session");
      return var1.getEngineSession();
   }

   public final Session getFocusSession() {
      return (Session)this.focusRef.get();
   }

   public final TabViewEngineSession getOrCreateEngineSession(Session var1) {
      Intrinsics.checkParameterIsNotNull(var1, "session");
      TabViewEngineSession var2 = this.getEngineSession(var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = new TabViewEngineSession((Observable)null, 1, (DefaultConstructorMarker)null);
         this.link(var1, var2);
         return var2;
      }
   }

   public final List getTabs() {
      return (List)(new ArrayList((Collection)this.sessions));
   }

   public final int getTabsCount() {
      return this.sessions.size();
   }

   public void notifyObservers(Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "block");
      this.$$delegate_0.notifyObservers(var1);
   }

   public final void pause() {
      Iterator var1 = this.sessions.iterator();

      while(var1.hasNext()) {
         TabViewEngineSession var2 = ((Session)var1.next()).getEngineSession();
         if (var2 != null) {
            TabView var3 = var2.getTabView();
            if (var3 != null) {
               var3.onPause();
            }
         }
      }

   }

   public void register(SessionManager.Observer var1) {
      Intrinsics.checkParameterIsNotNull(var1, "observer");
      this.$$delegate_0.register(var1);
   }

   public void register(SessionManager.Observer var1, LifecycleOwner var2, boolean var3) {
      Intrinsics.checkParameterIsNotNull(var1, "observer");
      Intrinsics.checkParameterIsNotNull(var2, "owner");
      this.$$delegate_0.register(var1, var2, var3);
   }

   public final void restore(List var1, String var2) {
      Intrinsics.checkParameterIsNotNull(var1, "states");
      Iterator var3 = var1.iterator();
      int var4 = 0;

      while(var3.hasNext()) {
         SessionManager.SessionWithState var5 = (SessionManager.SessionWithState)var3.next();
         if (var5.getSession().isValid()) {
            TabViewEngineSession var6 = this.getOrCreateEngineSession(var5.getSession());
            this.link(var5.getSession(), var6);
            TabViewEngineSession var7 = var5.getSession().getEngineSession();
            if (var7 != null) {
               var6 = var5.getEngineSession();
               Bundle var8;
               if (var6 != null) {
                  var8 = var6.getWebViewState();
               } else {
                  var8 = null;
               }

               var7.setWebViewState(var8);
            }

            this.sessions.add(var4, var5.getSession());
            ++var4;
         }
      }

      if (this.sessions.size() > 0 && this.sessions.size() == var1.size()) {
         this.focusRef = new WeakReference(this.getTab(var2));
      }

      this.notifyObservers((Function1)(new Function1() {
         public final void invoke(SessionManager.Observer var1) {
            Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
            var1.onSessionCountChanged(SessionManager.this.sessions.size());
         }
      }));
   }

   public final void resume() {
      Iterator var1 = this.sessions.iterator();

      while(var1.hasNext()) {
         TabViewEngineSession var2 = ((Session)var1.next()).getEngineSession();
         if (var2 != null) {
            TabView var3 = var2.getTabView();
            if (var3 != null) {
               var3.onResume();
            }
         }
      }

   }

   public final void switchToTab(String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "id");
      Session var2 = this.getTab(var1);
      if (var2 != null) {
         this.focusRef = new WeakReference(var2);
      }

      this.notifier.notifyTabFocused(var2, SessionManager.Factor.FACTOR_TAB_SWITCHED);
   }

   public void unregister(SessionManager.Observer var1) {
      Intrinsics.checkParameterIsNotNull(var1, "observer");
      this.$$delegate_0.unregister(var1);
   }

   public void unregisterObservers() {
      this.$$delegate_0.unregisterObservers();
   }

   public List wrapConsumers(Function2 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "block");
      return this.$$delegate_0.wrapConsumers(var1);
   }

   public final class Client implements TabViewEngineSession.Client {
      public boolean handleExternalUrl(String var1) {
         List var2 = SessionManager.this.wrapConsumers((Function2)null.INSTANCE);
         return Consumable.Companion.from(var1).consumeBy(var2);
      }

      public void onHttpAuthRequest(final TabViewClient.HttpAuthCallback var1, final String var2, final String var3) {
         Intrinsics.checkParameterIsNotNull(var1, "callback");
         SessionManager.this.notifyObservers((Function1)(new Function1() {
            public final void invoke(SessionManager.Observer var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
               var1x.onHttpAuthRequest(var1, var2, var3);
            }
         }));
      }

      public boolean onShowFileChooser(final TabViewEngineSession var1, final ValueCallback var2, FileChooserParams var3) {
         Intrinsics.checkParameterIsNotNull(var1, "es");
         List var4 = SessionManager.this.wrapConsumers((Function2)(new Function2() {
            public final boolean invoke(SessionManager.Observer var1x, FileChooserParams var2x) {
               Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
               return var1x.onShowFileChooser(var1, var2, var2x);
            }
         }));
         return Consumable.Companion.from(var3).consumeBy(var4);
      }

      public void updateFailingUrl(final String var1, final boolean var2) {
         SessionManager.this.notifyObservers((Function1)(new Function1() {
            public final void invoke(SessionManager.Observer var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
               var1x.updateFailingUrl(var1, var2);
            }
         }));
      }
   }

   public static enum Factor {
      FACTOR_BACK_EXTERNAL,
      FACTOR_NO_FOCUS,
      FACTOR_TAB_ADDED,
      FACTOR_TAB_REMOVED,
      FACTOR_TAB_SWITCHED,
      FACTOR_UNKNOWN;

      private final int value;

      static {
         SessionManager.Factor var0 = new SessionManager.Factor("FACTOR_UNKNOWN", 0, 1);
         FACTOR_UNKNOWN = var0;
         SessionManager.Factor var1 = new SessionManager.Factor("FACTOR_TAB_ADDED", 1, 2);
         FACTOR_TAB_ADDED = var1;
         SessionManager.Factor var2 = new SessionManager.Factor("FACTOR_TAB_REMOVED", 2, 3);
         FACTOR_TAB_REMOVED = var2;
         SessionManager.Factor var3 = new SessionManager.Factor("FACTOR_TAB_SWITCHED", 3, 4);
         FACTOR_TAB_SWITCHED = var3;
         SessionManager.Factor var4 = new SessionManager.Factor("FACTOR_NO_FOCUS", 4, 5);
         FACTOR_NO_FOCUS = var4;
         SessionManager.Factor var5 = new SessionManager.Factor("FACTOR_BACK_EXTERNAL", 5, 6);
         FACTOR_BACK_EXTERNAL = var5;
      }

      protected Factor(int var3) {
         this.value = var3;
      }
   }

   private static final class Notifier extends Handler {
      private final String ENUM_KEY;
      private final SessionManager observable;

      public Notifier(SessionManager var1) {
         Intrinsics.checkParameterIsNotNull(var1, "observable");
         super(Looper.getMainLooper());
         this.observable = var1;
         this.ENUM_KEY = "_key_enum";
      }

      private final void focusTab(final Session var1, final SessionManager.Factor var2) {
         if (var1 != null && this.observable.getOrCreateEngineSession(var1).getTabView() == null) {
            this.observable.initializeEngineView(var1);
         }

         this.observable.notifyObservers((Function1)(new Function1() {
            public final void invoke(SessionManager.Observer var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
               var1x.onFocusChanged(var1, var2);
            }
         }));
      }

      public final void addedTab(Message var1) {
         Intrinsics.checkParameterIsNotNull(var1, "msg");
         Object var2 = var1.obj;
         if (var2 != null) {
            final Pair var3 = (Pair)var2;
            this.observable.notifyObservers((Function1)(new Function1() {
               public final void invoke(SessionManager.Observer var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  var1.onSessionAdded((Session)var3.getFirst(), (Bundle)var3.getSecond());
               }
            }));
         } else {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Pair<org.mozilla.rocket.tabs.Session, android.os.Bundle?>");
         }
      }

      public void handleMessage(Message var1) {
         Intrinsics.checkParameterIsNotNull(var1, "msg");
         int var2 = var1.what;
         if (var2 == SessionManagerKt.getMSG_FOCUS_TAB()) {
            Session var3 = (Session)var1.obj;
            Serializable var4 = var1.getData().getSerializable(this.ENUM_KEY);
            if (var4 == null) {
               throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.SessionManager.Factor");
            }

            this.focusTab(var3, (SessionManager.Factor)var4);
         } else if (var2 == SessionManagerKt.getMSG_ADDED_TAB()) {
            this.addedTab(var1);
         } else if (var2 == SessionManagerKt.getMSG_REMOVEDED_TAB()) {
            this.removedTab(var1);
         }

      }

      public final void notifyTabAdded(Session var1, Bundle var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Message var3 = this.obtainMessage(SessionManagerKt.getMSG_ADDED_TAB());
         var3.obj = new Pair(var1, var2);
         this.sendMessage(var3);
      }

      public final void notifyTabFocused(Session var1, SessionManager.Factor var2) {
         Intrinsics.checkParameterIsNotNull(var2, "factor");
         Message var3 = this.obtainMessage(SessionManagerKt.getMSG_FOCUS_TAB());
         var3.obj = var1;
         Intrinsics.checkExpressionValueIsNotNull(var3, "msg");
         var3.getData().putSerializable(this.ENUM_KEY, (Serializable)var2);
         this.sendMessage(var3);
      }

      public final void notifyTabRemoved(Session var1) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Message var2 = this.obtainMessage(SessionManagerKt.getMSG_REMOVEDED_TAB());
         var2.obj = var1;
         this.sendMessage(var2);
      }

      public final void removedTab(Message var1) {
         Intrinsics.checkParameterIsNotNull(var1, "msg");
         Object var2 = var1.obj;
         if (var2 != null) {
            Session var3 = (Session)var2;
            this.observable.destroySession(var3);
         } else {
            throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.Session");
         }
      }
   }

   public interface Observer extends TabViewEngineSession.Client {
      void onFocusChanged(Session var1, SessionManager.Factor var2);

      void onSessionAdded(Session var1, Bundle var2);

      void onSessionCountChanged(int var1);

      public static final class DefaultImpls {
         public static void onFocusChanged(SessionManager.Observer var0, Session var1, SessionManager.Factor var2) {
            Intrinsics.checkParameterIsNotNull(var2, "factor");
         }

         public static void onSessionAdded(SessionManager.Observer var0, Session var1, Bundle var2) {
            Intrinsics.checkParameterIsNotNull(var1, "session");
         }

         public static void onSessionCountChanged(SessionManager.Observer var0, int var1) {
         }
      }
   }

   public static final class SessionWithState {
      private final TabViewEngineSession engineSession;
      private final Session session;

      public SessionWithState(Session var1, TabViewEngineSession var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         super();
         this.session = var1;
         this.engineSession = var2;
      }

      public boolean equals(Object var1) {
         if (this != var1) {
            if (!(var1 instanceof SessionManager.SessionWithState)) {
               return false;
            }

            SessionManager.SessionWithState var2 = (SessionManager.SessionWithState)var1;
            if (!Intrinsics.areEqual(this.session, var2.session) || !Intrinsics.areEqual(this.engineSession, var2.engineSession)) {
               return false;
            }
         }

         return true;
      }

      public final TabViewEngineSession getEngineSession() {
         return this.engineSession;
      }

      public final Session getSession() {
         return this.session;
      }

      public int hashCode() {
         Session var1 = this.session;
         int var2 = 0;
         int var3;
         if (var1 != null) {
            var3 = var1.hashCode();
         } else {
            var3 = 0;
         }

         TabViewEngineSession var4 = this.engineSession;
         if (var4 != null) {
            var2 = var4.hashCode();
         }

         return var3 * 31 + var2;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("SessionWithState(session=");
         var1.append(this.session);
         var1.append(", engineSession=");
         var1.append(this.engineSession);
         var1.append(")");
         return var1.toString();
      }
   }

   public final class WindowClient implements TabViewEngineSession.WindowClient {
      private Session source;

      public WindowClient(Session var2) {
         Intrinsics.checkParameterIsNotNull(var2, "source");
         super();
         this.source = var2;
      }

      public void onCloseWindow(TabViewEngineSession var1) {
         Intrinsics.checkParameterIsNotNull(var1, "es");
         if (this.source.getEngineSession() == var1) {
            Iterator var2 = ((Iterable)SessionManager.this.sessions).iterator();

            Object var4;
            while(true) {
               if (!var2.hasNext()) {
                  var4 = null;
                  break;
               }

               Object var3 = var2.next();
               if (Intrinsics.areEqual(((Session)var3).getEngineSession(), var1)) {
                  var4 = var3;
                  break;
               }
            }

            Session var5 = (Session)var4;
            if (var5 != null) {
               SessionManager.this.closeTab(var5.getId());
            }
         }

      }

      public boolean onCreateWindow(boolean var1, boolean var2, Message var3) {
         if (var3 == null) {
            return false;
         } else {
            String var4 = SessionManager.this.addTabInternal((String)null, this.source.getId(), false, var2, (Bundle)null);
            Session var5 = SessionManager.this.getTab(var4);
            if (var5 != null) {
               if (var5.getEngineSession() != null) {
                  TabViewEngineSession var6 = var5.getEngineSession();
                  if (var6 == null) {
                     Intrinsics.throwNpe();
                  }

                  if (var6.getTabView() != null) {
                     var6 = var5.getEngineSession();
                     if (var6 == null) {
                        Intrinsics.throwNpe();
                     }

                     TabView var7 = var6.getTabView();
                     if (var7 == null) {
                        Intrinsics.throwNpe();
                     }

                     var7.bindOnNewWindowCreation(var3);
                     return true;
                  }
               }

               throw (Throwable)(new RuntimeException("webview is null, previous creation failed"));
            } else {
               return false;
            }
         }
      }
   }
}
