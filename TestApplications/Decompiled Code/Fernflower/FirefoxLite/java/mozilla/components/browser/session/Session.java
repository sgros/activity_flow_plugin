package mozilla.components.browser.session;

import android.arch.lifecycle.LifecycleOwner;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import mozilla.components.support.base.observer.Observable;

public final class Session implements Observable {
   // $FF: synthetic field
   static final KProperty[] $$delegatedProperties = new KProperty[]{(KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "url", "getUrl()Ljava/lang/String;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "title", "getTitle()Ljava/lang/String;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "progress", "getProgress()I")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "loading", "getLoading()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "canGoBack", "getCanGoBack()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "canGoForward", "getCanGoForward()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "searchTerms", "getSearchTerms()Ljava/lang/String;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "securityInfo", "getSecurityInfo()Lmozilla/components/browser/session/Session$SecurityInfo;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "customTabConfig", "getCustomTabConfig()Lmozilla/components/browser/session/tab/CustomTabConfig;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "download", "getDownload()Lmozilla/components/support/base/observer/Consumable;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "trackerBlockingEnabled", "getTrackerBlockingEnabled()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "trackersBlocked", "getTrackersBlocked()Ljava/util/List;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "findResults", "getFindResults()Ljava/util/List;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "hitResult", "getHitResult()Lmozilla/components/support/base/observer/Consumable;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "thumbnail", "getThumbnail()Landroid/graphics/Bitmap;")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "desktopMode", "getDesktopMode()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(Session.class), "fullScreenMode", "getFullScreenMode()Z"))};
   // $FF: synthetic field
   private final Observable $$delegate_0;
   private final String id;

   public boolean equals(Object var1) {
      if ((Session)this == var1) {
         return true;
      } else {
         Class var2 = this.getClass();
         Class var3;
         if (var1 != null) {
            var3 = var1.getClass();
         } else {
            var3 = null;
         }

         if (Intrinsics.areEqual(var2, var3) ^ true) {
            return false;
         } else if (var1 != null) {
            Session var4 = (Session)var1;
            return !(Intrinsics.areEqual(this.id, var4.id) ^ true);
         } else {
            throw new TypeCastException("null cannot be cast to non-null type mozilla.components.browser.session.Session");
         }
      }
   }

   public int hashCode() {
      return this.id.hashCode();
   }

   public void notifyObservers(Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "block");
      this.$$delegate_0.notifyObservers(var1);
   }

   public void register(Session.Observer var1) {
      Intrinsics.checkParameterIsNotNull(var1, "observer");
      this.$$delegate_0.register(var1);
   }

   public void register(Session.Observer var1, LifecycleOwner var2, boolean var3) {
      Intrinsics.checkParameterIsNotNull(var1, "observer");
      Intrinsics.checkParameterIsNotNull(var2, "owner");
      this.$$delegate_0.register(var1, var2, var3);
   }

   public void unregister(Session.Observer var1) {
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

   public static final class FindResult {
      private final int activeMatchOrdinal;
      private final boolean isDoneCounting;
      private final int numberOfMatches;

      public FindResult(int var1, int var2, boolean var3) {
         this.activeMatchOrdinal = var1;
         this.numberOfMatches = var2;
         this.isDoneCounting = var3;
      }

      public boolean equals(Object var1) {
         if (this != var1) {
            if (!(var1 instanceof Session.FindResult)) {
               return false;
            }

            Session.FindResult var3 = (Session.FindResult)var1;
            boolean var2;
            if (this.activeMatchOrdinal == var3.activeMatchOrdinal) {
               var2 = true;
            } else {
               var2 = false;
            }

            if (!var2) {
               return false;
            }

            if (this.numberOfMatches == var3.numberOfMatches) {
               var2 = true;
            } else {
               var2 = false;
            }

            if (!var2) {
               return false;
            }

            if (this.isDoneCounting == var3.isDoneCounting) {
               var2 = true;
            } else {
               var2 = false;
            }

            if (!var2) {
               return false;
            }
         }

         return true;
      }

      public final int getActiveMatchOrdinal() {
         return this.activeMatchOrdinal;
      }

      public final int getNumberOfMatches() {
         return this.numberOfMatches;
      }

      public int hashCode() {
         int var1 = this.activeMatchOrdinal;
         int var2 = this.numberOfMatches;
         byte var3 = this.isDoneCounting;
         byte var4 = var3;
         if (var3 != 0) {
            var4 = 1;
         }

         return (var1 * 31 + var2) * 31 + var4;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("FindResult(activeMatchOrdinal=");
         var1.append(this.activeMatchOrdinal);
         var1.append(", numberOfMatches=");
         var1.append(this.numberOfMatches);
         var1.append(", isDoneCounting=");
         var1.append(this.isDoneCounting);
         var1.append(")");
         return var1.toString();
      }
   }

   public interface Observer {
   }

   public static final class SecurityInfo {
      private final String host;
      private final String issuer;
      private final boolean secure;

      public SecurityInfo() {
         this(false, (String)null, (String)null, 7, (DefaultConstructorMarker)null);
      }

      public SecurityInfo(boolean var1, String var2, String var3) {
         Intrinsics.checkParameterIsNotNull(var2, "host");
         Intrinsics.checkParameterIsNotNull(var3, "issuer");
         super();
         this.secure = var1;
         this.host = var2;
         this.issuer = var3;
      }

      // $FF: synthetic method
      public SecurityInfo(boolean var1, String var2, String var3, int var4, DefaultConstructorMarker var5) {
         if ((var4 & 1) != 0) {
            var1 = false;
         }

         if ((var4 & 2) != 0) {
            var2 = "";
         }

         if ((var4 & 4) != 0) {
            var3 = "";
         }

         this(var1, var2, var3);
      }

      public boolean equals(Object var1) {
         if (this != var1) {
            if (!(var1 instanceof Session.SecurityInfo)) {
               return false;
            }

            Session.SecurityInfo var3 = (Session.SecurityInfo)var1;
            boolean var2;
            if (this.secure == var3.secure) {
               var2 = true;
            } else {
               var2 = false;
            }

            if (!var2 || !Intrinsics.areEqual(this.host, var3.host) || !Intrinsics.areEqual(this.issuer, var3.issuer)) {
               return false;
            }
         }

         return true;
      }

      public final boolean getSecure() {
         return this.secure;
      }

      public int hashCode() {
         byte var1 = this.secure;
         byte var2 = var1;
         if (var1 != 0) {
            var2 = 1;
         }

         String var3 = this.host;
         int var4 = 0;
         int var5;
         if (var3 != null) {
            var5 = var3.hashCode();
         } else {
            var5 = 0;
         }

         var3 = this.issuer;
         if (var3 != null) {
            var4 = var3.hashCode();
         }

         return (var2 * 31 + var5) * 31 + var4;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("SecurityInfo(secure=");
         var1.append(this.secure);
         var1.append(", host=");
         var1.append(this.host);
         var1.append(", issuer=");
         var1.append(this.issuer);
         var1.append(")");
         return var1.toString();
      }
   }
}
