package org.mozilla.focus.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.preference.ListPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import java.nio.ByteBuffer;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.mozilla.focus.locale.LocaleManager;
import org.mozilla.focus.locale.Locales;

public class LocaleListPreference extends ListPreference {
   private static final Map languageCodeToNameMap = new HashMap();
   private LocaleListPreference.CharacterValidator characterValidator;
   private volatile Locale entriesLocale;

   static {
      languageCodeToNameMap.put("ast", "Asturianu");
      languageCodeToNameMap.put("cak", "Kaqchikel");
      languageCodeToNameMap.put("ia", "Interlingua");
      languageCodeToNameMap.put("meh", "Tu´un savi ñuu Yasi'í Yuku Iti");
      languageCodeToNameMap.put("mix", "Tu'un savi");
      languageCodeToNameMap.put("trs", "Triqui");
      languageCodeToNameMap.put("zam", "DíɁztè");
      languageCodeToNameMap.put("oc", "occitan");
      languageCodeToNameMap.put("an", "Aragonés");
      languageCodeToNameMap.put("tt", "татарча");
      languageCodeToNameMap.put("wo", "Wolof");
      languageCodeToNameMap.put("anp", "अंगिका");
      languageCodeToNameMap.put("ixl", "Ixil");
      languageCodeToNameMap.put("pai", "Paa ipai");
      languageCodeToNameMap.put("quy", "Chanka Qhichwa");
      languageCodeToNameMap.put("ay", "Aimara");
      languageCodeToNameMap.put("quc", "K'iche'");
      languageCodeToNameMap.put("tsz", "P'urhepecha");
      languageCodeToNameMap.put("mai", "मैथिली/মৈথিলী");
      languageCodeToNameMap.put("jv", "Basa Jawa");
      languageCodeToNameMap.put("su", "Basa Sunda");
      languageCodeToNameMap.put("ace", "Basa Acèh");
      languageCodeToNameMap.put("gor", "Bahasa Hulontalo");
   }

   public LocaleListPreference(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public LocaleListPreference(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void buildList() {
      Locale var1 = Locale.getDefault();
      StringBuilder var2 = new StringBuilder();
      var2.append("Building locales list. Current locale: ");
      var2.append(var1);
      Log.d("GeckoLocaleList", var2.toString());
      if (var1.equals(this.entriesLocale) && this.getEntries() != null) {
         Log.v("GeckoLocaleList", "No need to build list.");
      } else {
         LocaleListPreference.LocaleDescriptor[] var10 = this.getUsableLocales();
         int var3 = var10.length;
         this.entriesLocale = var1;
         int var4 = var3 + 1;
         String[] var9 = new String[var4];
         String[] var5 = new String[var4];
         String var6 = this.getContext().getString(2131755348);
         var4 = 0;
         var9[0] = var6;

         for(var5[0] = ""; var4 < var3; var5[var4] = var6) {
            String var7 = var10[var4].getDisplayName();
            var6 = var10[var4].getTag();
            StringBuilder var8 = new StringBuilder();
            var8.append(var7);
            var8.append(" => ");
            var8.append(var6);
            Log.v("GeckoLocaleList", var8.toString());
            ++var4;
            var9[var4] = var7;
         }

         this.setEntries(var9);
         this.setEntryValues(var5);
      }
   }

   private Locale getSelectedLocale() {
      String var1 = this.getValue();
      return var1 != null && !var1.equals("") ? Locales.parseLocaleCode(var1) : Locale.getDefault();
   }

   private LocaleListPreference.LocaleDescriptor[] getUsableLocales() {
      Collection var1 = LocaleManager.getPackagedLocaleTags(this.getContext());
      HashSet var2 = new HashSet(var1.size());
      Iterator var6 = var1.iterator();

      while(var6.hasNext()) {
         String var3 = (String)var6.next();
         LocaleListPreference.LocaleDescriptor var4 = new LocaleListPreference.LocaleDescriptor(var3);
         if (!var4.isUsable(this.characterValidator)) {
            StringBuilder var8 = new StringBuilder();
            var8.append("Skipping locale ");
            var8.append(var3);
            var8.append(" on this device.");
            Log.w("GeckoLocaleList", var8.toString());
         } else {
            var2.add(var4);
         }
      }

      int var5 = var2.size();
      LocaleListPreference.LocaleDescriptor[] var7 = (LocaleListPreference.LocaleDescriptor[])var2.toArray(new LocaleListPreference.LocaleDescriptor[var5]);
      Arrays.sort(var7, 0, var5);
      return var7;
   }

   public CharSequence getSummary() {
      String var1 = this.getValue();
      return TextUtils.isEmpty(var1) ? this.getContext().getString(2131755348) : (new LocaleListPreference.LocaleDescriptor(var1)).getDisplayName();
   }

   protected void onAttachedToActivity() {
      super.onAttachedToActivity();
      this.characterValidator = new LocaleListPreference.CharacterValidator(" ");
      this.buildList();
   }

   protected void onDialogClosed(boolean var1) {
      super.onDialogClosed(var1);
      Locale var2 = this.getSelectedLocale();
      Context var3 = this.getContext();
      LocaleManager.getInstance().updateConfiguration(var3, var2);
   }

   private static class CharacterValidator {
      private final byte[] missingCharacter;
      private final Paint paint = new Paint();

      public CharacterValidator(String var1) {
         this.missingCharacter = getPixels(this.drawBitmap(var1));
      }

      private Bitmap drawBitmap(String var1) {
         Bitmap var2 = Bitmap.createBitmap(32, 48, Config.ALPHA_8);
         (new Canvas(var2)).drawText(var1, 0.0F, 24.0F, this.paint);
         return var2;
      }

      private static byte[] getPixels(Bitmap var0) {
         ByteBuffer var1 = ByteBuffer.allocate(var0.getAllocationByteCount());

         try {
            var0.copyPixelsToBuffer(var1);
         } catch (RuntimeException var2) {
            if ("Buffer not large enough for pixels".equals(var2.getMessage())) {
               return var1.array();
            }

            throw var2;
         }

         return var1.array();
      }

      public boolean characterIsMissingInFont(String var1) {
         return Arrays.equals(getPixels(this.drawBitmap(var1)), this.missingCharacter);
      }
   }

   private static final class LocaleDescriptor implements Comparable {
      private static final Collator COLLATOR;
      private final String nativeName;
      public final String tag;

      static {
         COLLATOR = Collator.getInstance(Locale.US);
      }

      public LocaleDescriptor(String var1) {
         this(Locales.parseLocaleCode(var1), var1);
      }

      public LocaleDescriptor(Locale var1, String var2) {
         this.tag = var2;
         if (LocaleListPreference.languageCodeToNameMap.containsKey(var1.getLanguage())) {
            var2 = (String)LocaleListPreference.languageCodeToNameMap.get(var1.getLanguage());
         } else {
            var2 = var1.getDisplayName(var1);
         }

         if (TextUtils.isEmpty(var2)) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Display name is empty. Using ");
            var4.append(var1.toString());
            Log.w("GeckoLocaleList", var4.toString());
            this.nativeName = var1.toString();
         } else if (Character.getDirectionality(var2.charAt(0)) == 0) {
            StringBuilder var3 = new StringBuilder();
            var3.append(var2.substring(0, 1).toUpperCase(var1));
            var3.append(var2.substring(1));
            this.nativeName = var3.toString();
         } else {
            this.nativeName = var2;
         }
      }

      public int compareTo(LocaleListPreference.LocaleDescriptor var1) {
         return COLLATOR.compare(this.nativeName, var1.nativeName);
      }

      public boolean equals(Object var1) {
         boolean var2 = var1 instanceof LocaleListPreference.LocaleDescriptor;
         boolean var3 = false;
         if (var2) {
            if (this.compareTo((LocaleListPreference.LocaleDescriptor)var1) == 0) {
               var3 = true;
            }

            return var3;
         } else {
            return false;
         }
      }

      public String getDisplayName() {
         return this.nativeName;
      }

      public String getTag() {
         return this.tag;
      }

      public int hashCode() {
         return this.tag.hashCode();
      }

      public boolean isUsable(LocaleListPreference.CharacterValidator var1) {
         if (this.tag.equals("bn-IN") && !this.nativeName.startsWith("বাংলা")) {
            return false;
         } else {
            return !this.tag.equals("or") && !this.tag.equals("my") && !this.tag.equals("pa-IN") && !this.tag.equals("gu-IN") && !this.tag.equals("bn-IN") || !var1.characterIsMissingInFont(this.nativeName.substring(0, 1));
         }
      }

      public String toString() {
         return this.nativeName;
      }
   }
}
