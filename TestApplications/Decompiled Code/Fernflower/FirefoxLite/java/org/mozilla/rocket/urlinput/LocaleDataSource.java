package org.mozilla.rocket.urlinput;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class LocaleDataSource implements QuickSearchDataSource {
   public static final LocaleDataSource.Companion Companion = new LocaleDataSource.Companion((DefaultConstructorMarker)null);
   @SuppressLint({"StaticFieldLeak"})
   private static volatile LocaleDataSource INSTANCE;
   private Context context;

   public static final LocaleDataSource getInstance(Context var0) {
      return Companion.getInstance(var0);
   }

   public LiveData fetchEngines() {
      MutableLiveData var1 = new MutableLiveData();
      QuickSearchUtils var2 = QuickSearchUtils.INSTANCE;
      Context var3 = this.context;
      if (var3 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("context");
      }

      var2.loadEnginesByLocale$app_focusWebkitRelease(var3, var1);
      return (LiveData)var1;
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final LocaleDataSource getInstance(Context var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         LocaleDataSource var2 = LocaleDataSource.INSTANCE;
         LocaleDataSource var10;
         if (var2 != null) {
            var10 = var2;
            return var10;
         } else {
            synchronized(this){}

            Throwable var10000;
            label118: {
               boolean var10001;
               try {
                  var2 = LocaleDataSource.INSTANCE;
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label118;
               }

               if (var2 != null) {
                  var10 = var2;
                  return var10;
               }

               try {
                  var2 = new LocaleDataSource();
                  LocaleDataSource.INSTANCE = var2;
                  var1 = var1.getApplicationContext();
                  Intrinsics.checkExpressionValueIsNotNull(var1, "context.applicationContext");
                  var2.context = var1;
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label118;
               }

               var10 = var2;
               return var10;
            }

            Throwable var9 = var10000;
            throw var9;
         }
      }
   }
}
