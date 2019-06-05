package org.mozilla.focus.provider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public final class SettingPreferenceWrapper {
   private final ContentResolver resolver;

   public SettingPreferenceWrapper(ContentResolver var1) {
      Intrinsics.checkParameterIsNotNull(var1, "resolver");
      super();
      this.resolver = var1;
   }

   private final Object getValue(String var1, String var2, Object var3) {
      Uri var19 = Uri.withAppendedPath(SettingContract.INSTANCE.getAUTHORITY_URI(), var1);
      String var4 = String.valueOf(var3);
      Object var5 = null;
      Cursor var6 = (Cursor)null;

      Cursor var21;
      try {
         var21 = this.resolver.query(var19, (String[])null, (String)null, new String[]{var2, var4}, (String)null);
      } finally {
         ;
      }

      Object var20 = var5;
      if (var21 != null) {
         label198: {
            Throwable var10000;
            label197: {
               boolean var10001;
               Bundle var23;
               try {
                  var23 = var21.getExtras();
               } catch (Throwable var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label197;
               }

               var20 = var5;
               if (var23 == null) {
                  break label198;
               }

               label186:
               try {
                  var20 = var23.get("key");
                  var23.clear();
                  break label198;
               } catch (Throwable var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label186;
               }
            }

            Throwable var22 = var10000;
            if (var21 != null) {
               var21.close();
            }

            throw var22;
         }
      }

      if (var21 != null) {
         var21.close();
      }

      if (var20 == null) {
         var20 = var3;
      }

      return var20;
   }

   public final boolean getBoolean(String var1, boolean var2) {
      Intrinsics.checkParameterIsNotNull(var1, "key");
      Object var3 = this.getValue("getBoolean", var1, var2);
      if (var3 != null) {
         return (Boolean)var3;
      } else {
         throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
      }
   }

   public final float getFloat(String var1, float var2) {
      Intrinsics.checkParameterIsNotNull(var1, "key");
      Object var3 = this.getValue("getFloat", var1, var2);
      if (var3 != null) {
         return (Float)var3;
      } else {
         throw new TypeCastException("null cannot be cast to non-null type kotlin.Float");
      }
   }
}
