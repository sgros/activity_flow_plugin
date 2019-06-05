package org.mozilla.rocket.content;

import android.annotation.SuppressLint;
import android.content.Context;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.lite.newspoint.RepositoryNewsPoint;
import org.mozilla.lite.partner.Repository;
import org.mozilla.rocket.bhaskar.RepositoryBhaskar;

public final class NewsRepository {
   public static final NewsRepository.Companion Companion = new NewsRepository.Companion((DefaultConstructorMarker)null);
   @SuppressLint({"StaticFieldLeak"})
   private static volatile Repository INSTANCE;

   public static final void reset() {
      Companion.reset();
   }

   public static final void resetSubscriptionUrl(String var0) {
      Companion.resetSubscriptionUrl(var0);
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      private final Repository buildRepository(Context var1) {
         NewsSourceManager var2 = NewsSourceManager.getInstance();
         Intrinsics.checkExpressionValueIsNotNull(var2, "NewsSourceManager.getInstance()");
         Repository var3;
         if (Intrinsics.areEqual(var2.getNewsSource(), "DainikBhaskar.com")) {
            var2 = NewsSourceManager.getInstance();
            Intrinsics.checkExpressionValueIsNotNull(var2, "NewsSourceManager.getInstance()");
            var3 = (Repository)(new RepositoryBhaskar(var1, var2.getNewsSourceUrl()));
         } else {
            var2 = NewsSourceManager.getInstance();
            Intrinsics.checkExpressionValueIsNotNull(var2, "NewsSourceManager.getInstance()");
            var3 = (Repository)(new RepositoryNewsPoint(var1, var2.getNewsSourceUrl()));
         }

         return var3;
      }

      public final Repository getInstance(Context var1) {
         Repository var2 = NewsRepository.INSTANCE;
         Repository var15;
         if (var2 != null) {
            var15 = var2;
            return var15;
         } else {
            synchronized(this){}
            Throwable var10000;
            boolean var10001;
            if (var1 != null) {
               label150: {
                  try {
                     var2 = NewsRepository.INSTANCE;
                  } catch (Throwable var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label150;
                  }

                  if (var2 != null) {
                     var15 = var2;
                  } else {
                     try {
                        NewsRepository.Companion var18 = NewsRepository.Companion;
                        var1 = var1.getApplicationContext();
                        Intrinsics.checkExpressionValueIsNotNull(var1, "context.applicationContext");
                        var15 = var18.buildRepository(var1);
                        NewsRepository.INSTANCE = var15;
                     } catch (Throwable var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label150;
                     }
                  }

                  return var15;
               }
            } else {
               label128:
               try {
                  IllegalStateException var17 = new IllegalStateException("can't create Content Repository with null context");
                  throw (Throwable)var17;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label128;
               }
            }

            Throwable var16 = var10000;
            throw var16;
         }
      }

      public final boolean isEmpty() {
         boolean var1;
         if (NewsRepository.INSTANCE == null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public final void reset() {
         Repository var1 = NewsRepository.INSTANCE;
         if (var1 != null) {
            var1.reset();
         }

         NewsRepository.INSTANCE = (Repository)null;
      }

      public final void resetSubscriptionUrl(String var1) {
         Intrinsics.checkParameterIsNotNull(var1, "subscriptionUrl");
         Repository var2 = NewsRepository.INSTANCE;
         if (var2 != null) {
            var2.setSubscriptionUrl(var1);
         }

      }
   }
}
