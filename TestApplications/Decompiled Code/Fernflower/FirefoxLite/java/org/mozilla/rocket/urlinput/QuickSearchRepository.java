package org.mozilla.rocket.urlinput;

import android.arch.lifecycle.LiveData;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class QuickSearchRepository {
   public static final QuickSearchRepository.Companion Companion = new QuickSearchRepository.Companion((DefaultConstructorMarker)null);
   private static volatile QuickSearchRepository INSTANCE;
   private final QuickSearchDataSource globalDataSource;
   private final QuickSearchDataSource localeDataSource;

   public QuickSearchRepository(QuickSearchDataSource var1, QuickSearchDataSource var2) {
      Intrinsics.checkParameterIsNotNull(var1, "globalDataSource");
      Intrinsics.checkParameterIsNotNull(var2, "localeDataSource");
      super();
      this.globalDataSource = var1;
      this.localeDataSource = var2;
   }

   public static final QuickSearchRepository getInstance(QuickSearchDataSource var0, QuickSearchDataSource var1) {
      return Companion.getInstance(var0, var1);
   }

   public final LiveData fetchGlobal() {
      return this.globalDataSource.fetchEngines();
   }

   public final LiveData fetchLocale() {
      return this.localeDataSource.fetchEngines();
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final QuickSearchRepository getInstance(QuickSearchDataSource var1, QuickSearchDataSource var2) {
         Intrinsics.checkParameterIsNotNull(var1, "globalDataSource");
         Intrinsics.checkParameterIsNotNull(var2, "localeDataSource");
         QuickSearchRepository var3 = QuickSearchRepository.INSTANCE;
         QuickSearchRepository var11;
         if (var3 != null) {
            var11 = var3;
            return var11;
         } else {
            synchronized(this){}

            Throwable var10000;
            label118: {
               boolean var10001;
               try {
                  var3 = QuickSearchRepository.INSTANCE;
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label118;
               }

               if (var3 != null) {
                  var11 = var3;
                  return var11;
               }

               try {
                  var3 = new QuickSearchRepository(var1, var2);
                  QuickSearchRepository.INSTANCE = var3;
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label118;
               }

               var11 = var3;
               return var11;
            }

            Throwable var10 = var10000;
            throw var10;
         }
      }
   }
}
