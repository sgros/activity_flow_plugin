package org.telegram.ui.Components;

import android.text.SpannableString;
import java.lang.reflect.Field;
import org.telegram.messenger.FileLog;

public class SpannableStringLight extends SpannableString {
   private static boolean fieldsAvailable;
   private static Field mSpanCountField;
   private static Field mSpanDataField;
   private static Field mSpansField;
   private int mSpanCountOverride;
   private int[] mSpanDataOverride;
   private Object[] mSpansOverride;
   private int num;

   public SpannableStringLight(CharSequence var1) {
      super(var1);

      try {
         this.mSpansOverride = (Object[])mSpansField.get(this);
         this.mSpanDataOverride = (int[])mSpanDataField.get(this);
         this.mSpanCountOverride = (Integer)mSpanCountField.get(this);
      } catch (Throwable var2) {
         FileLog.e(var2);
      }

   }

   public static boolean isFieldsAvailable() {
      boolean var0 = fieldsAvailable;
      boolean var1 = true;
      if (!var0 && mSpansField == null) {
         try {
            mSpansField = SpannableString.class.getSuperclass().getDeclaredField("mSpans");
            mSpansField.setAccessible(true);
            mSpanDataField = SpannableString.class.getSuperclass().getDeclaredField("mSpanData");
            mSpanDataField.setAccessible(true);
            mSpanCountField = SpannableString.class.getSuperclass().getDeclaredField("mSpanCount");
            mSpanCountField.setAccessible(true);
         } catch (Throwable var3) {
            FileLog.e(var3);
         }

         fieldsAvailable = true;
      }

      if (mSpansField == null) {
         var1 = false;
      }

      return var1;
   }

   public void removeSpan(Object var1) {
      super.removeSpan(var1);
   }

   public void setSpanLight(Object var1, int var2, int var3, int var4) {
      Object[] var5 = this.mSpansOverride;
      int var6 = this.num;
      var5[var6] = var1;
      int[] var7 = this.mSpanDataOverride;
      var7[var6 * 3] = var2;
      var7[var6 * 3 + 1] = var3;
      var7[var6 * 3 + 2] = var4;
      this.num = var6 + 1;
   }

   public void setSpansCount(int var1) {
      int var2 = this.mSpanCountOverride;
      var1 += var2;
      this.mSpansOverride = new Object[var1];
      this.mSpanDataOverride = new int[var1 * 3];
      this.num = var2;
      this.mSpanCountOverride = var1;

      try {
         mSpansField.set(this, this.mSpansOverride);
         mSpanDataField.set(this, this.mSpanDataOverride);
         mSpanCountField.set(this, this.mSpanCountOverride);
      } catch (Throwable var4) {
         FileLog.e(var4);
      }

   }
}
