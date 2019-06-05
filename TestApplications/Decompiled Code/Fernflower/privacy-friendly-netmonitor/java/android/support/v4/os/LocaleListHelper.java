package android.support.v4.os;

import android.os.Build.VERSION;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.Size;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

@RequiresApi(14)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
final class LocaleListHelper {
   private static final Locale EN_LATN = LocaleHelper.forLanguageTag("en-Latn");
   private static final Locale LOCALE_AR_XB = new Locale("ar", "XB");
   private static final Locale LOCALE_EN_XA = new Locale("en", "XA");
   private static final int NUM_PSEUDO_LOCALES = 2;
   private static final String STRING_AR_XB = "ar-XB";
   private static final String STRING_EN_XA = "en-XA";
   @GuardedBy("sLock")
   private static LocaleListHelper sDefaultAdjustedLocaleList;
   @GuardedBy("sLock")
   private static LocaleListHelper sDefaultLocaleList;
   private static final Locale[] sEmptyList = new Locale[0];
   private static final LocaleListHelper sEmptyLocaleList = new LocaleListHelper(new Locale[0]);
   @GuardedBy("sLock")
   private static Locale sLastDefaultLocale;
   @GuardedBy("sLock")
   private static LocaleListHelper sLastExplicitlySetLocaleList;
   private static final Object sLock = new Object();
   private final Locale[] mList;
   @NonNull
   private final String mStringRepresentation;

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   LocaleListHelper(@NonNull Locale var1, LocaleListHelper var2) {
      if (var1 == null) {
         throw new NullPointerException("topLocale is null");
      } else {
         byte var3 = 0;
         int var4;
         if (var2 == null) {
            var4 = 0;
         } else {
            var4 = var2.mList.length;
         }

         int var5 = 0;

         while(true) {
            if (var5 >= var4) {
               var5 = -1;
               break;
            }

            if (var1.equals(var2.mList[var5])) {
               break;
            }

            ++var5;
         }

         byte var6;
         if (var5 == -1) {
            var6 = 1;
         } else {
            var6 = 0;
         }

         int var7 = var6 + var4;
         Locale[] var8 = new Locale[var7];
         var8[0] = (Locale)var1.clone();
         int var11;
         if (var5 == -1) {
            for(var5 = 0; var5 < var4; var5 = var11) {
               var11 = var5 + 1;
               var8[var11] = (Locale)var2.mList[var5].clone();
            }
         } else {
            int var9;
            for(var11 = 0; var11 < var5; var11 = var9) {
               var9 = var11 + 1;
               var8[var9] = (Locale)var2.mList[var11].clone();
            }

            ++var5;

            while(var5 < var4) {
               var8[var5] = (Locale)var2.mList[var5].clone();
               ++var5;
            }
         }

         StringBuilder var10 = new StringBuilder();

         for(var5 = var3; var5 < var7; ++var5) {
            var10.append(LocaleHelper.toLanguageTag(var8[var5]));
            if (var5 < var7 - 1) {
               var10.append(',');
            }
         }

         this.mList = var8;
         this.mStringRepresentation = var10.toString();
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   LocaleListHelper(@NonNull Locale... var1) {
      if (var1.length == 0) {
         this.mList = sEmptyList;
         this.mStringRepresentation = "";
      } else {
         Locale[] var2 = new Locale[var1.length];
         HashSet var3 = new HashSet();
         StringBuilder var4 = new StringBuilder();

         for(int var5 = 0; var5 < var1.length; ++var5) {
            Locale var6 = var1[var5];
            StringBuilder var7;
            if (var6 == null) {
               var7 = new StringBuilder();
               var7.append("list[");
               var7.append(var5);
               var7.append("] is null");
               throw new NullPointerException(var7.toString());
            }

            if (var3.contains(var6)) {
               var7 = new StringBuilder();
               var7.append("list[");
               var7.append(var5);
               var7.append("] is a repetition");
               throw new IllegalArgumentException(var7.toString());
            }

            var6 = (Locale)var6.clone();
            var2[var5] = var6;
            var4.append(LocaleHelper.toLanguageTag(var6));
            if (var5 < var1.length - 1) {
               var4.append(',');
            }

            var3.add(var6);
         }

         this.mList = var2;
         this.mStringRepresentation = var4.toString();
      }

   }

   private Locale computeFirstMatch(Collection var1, boolean var2) {
      int var3 = this.computeFirstMatchIndex(var1, var2);
      Locale var4;
      if (var3 == -1) {
         var4 = null;
      } else {
         var4 = this.mList[var3];
      }

      return var4;
   }

   private int computeFirstMatchIndex(Collection var1, boolean var2) {
      if (this.mList.length == 1) {
         return 0;
      } else if (this.mList.length == 0) {
         return -1;
      } else {
         int var3;
         label38: {
            if (var2) {
               var3 = this.findFirstMatchIndex(EN_LATN);
               if (var3 == 0) {
                  return 0;
               }

               if (var3 < Integer.MAX_VALUE) {
                  break label38;
               }
            }

            var3 = Integer.MAX_VALUE;
         }

         Iterator var5 = var1.iterator();

         while(var5.hasNext()) {
            int var4 = this.findFirstMatchIndex(LocaleHelper.forLanguageTag((String)var5.next()));
            if (var4 == 0) {
               return 0;
            }

            if (var4 < var3) {
               var3 = var4;
            }
         }

         if (var3 == Integer.MAX_VALUE) {
            return 0;
         } else {
            return var3;
         }
      }
   }

   private int findFirstMatchIndex(Locale var1) {
      for(int var2 = 0; var2 < this.mList.length; ++var2) {
         if (matchScore(var1, this.mList[var2]) > 0) {
            return var2;
         }
      }

      return Integer.MAX_VALUE;
   }

   @NonNull
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   static LocaleListHelper forLanguageTags(@Nullable String var0) {
      if (var0 != null && !var0.isEmpty()) {
         String[] var3 = var0.split(",");
         Locale[] var1 = new Locale[var3.length];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = LocaleHelper.forLanguageTag(var3[var2]);
         }

         return new LocaleListHelper(var1);
      } else {
         return getEmptyLocaleList();
      }
   }

   @NonNull
   @Size(
      min = 1L
   )
   static LocaleListHelper getAdjustedDefault() {
      // $FF: Couldn't be decompiled
   }

   @NonNull
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   @Size(
      min = 1L
   )
   static LocaleListHelper getDefault() {
      Locale var0 = Locale.getDefault();
      Object var1 = sLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label208: {
         LocaleListHelper var23;
         label207: {
            try {
               if (var0.equals(sLastDefaultLocale)) {
                  break label207;
               }

               sLastDefaultLocale = var0;
               if (sDefaultLocaleList != null && var0.equals(sDefaultLocaleList.get(0))) {
                  var23 = sDefaultLocaleList;
                  return var23;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label208;
            }

            try {
               LocaleListHelper var2 = new LocaleListHelper(var0, sLastExplicitlySetLocaleList);
               sDefaultLocaleList = var2;
               sDefaultAdjustedLocaleList = sDefaultLocaleList;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label208;
            }
         }

         label197:
         try {
            var23 = sDefaultLocaleList;
            return var23;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label197;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   @NonNull
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   static LocaleListHelper getEmptyLocaleList() {
      return sEmptyLocaleList;
   }

   private static String getLikelyScript(Locale var0) {
      if (VERSION.SDK_INT >= 21) {
         String var1 = var0.getScript();
         return !var1.isEmpty() ? var1 : "";
      } else {
         return "";
      }
   }

   private static boolean isPseudoLocale(String var0) {
      boolean var1;
      if (!"en-XA".equals(var0) && !"ar-XB".equals(var0)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isPseudoLocale(Locale var0) {
      boolean var1;
      if (!LOCALE_EN_XA.equals(var0) && !LOCALE_AR_XB.equals(var0)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   static boolean isPseudoLocalesOnly(@Nullable String[] var0) {
      if (var0 == null) {
         return true;
      } else if (var0.length > 3) {
         return false;
      } else {
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            String var3 = var0[var2];
            if (!var3.isEmpty() && !isPseudoLocale(var3)) {
               return false;
            }
         }

         return true;
      }
   }

   @IntRange(
      from = 0L,
      to = 1L
   )
   private static int matchScore(Locale var0, Locale var1) {
      boolean var2 = var0.equals(var1);
      byte var3 = 1;
      if (var2) {
         return 1;
      } else if (!var0.getLanguage().equals(var1.getLanguage())) {
         return 0;
      } else if (!isPseudoLocale(var0) && !isPseudoLocale(var1)) {
         String var4 = getLikelyScript(var0);
         if (var4.isEmpty()) {
            String var6 = var0.getCountry();
            byte var5 = var3;
            if (!var6.isEmpty()) {
               if (var6.equals(var1.getCountry())) {
                  var5 = var3;
               } else {
                  var5 = 0;
               }
            }

            return var5;
         } else {
            return var4.equals(getLikelyScript(var1));
         }
      } else {
         return 0;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   static void setDefault(@NonNull @Size(min = 1L) LocaleListHelper var0) {
      setDefault(var0, 0);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   static void setDefault(@NonNull @Size(min = 1L) LocaleListHelper var0, int var1) {
      if (var0 == null) {
         throw new NullPointerException("locales is null");
      } else if (var0.isEmpty()) {
         throw new IllegalArgumentException("locales is empty");
      } else {
         Object var2 = sLock;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label289: {
            try {
               sLastDefaultLocale = var0.get(var1);
               Locale.setDefault(sLastDefaultLocale);
               sLastExplicitlySetLocaleList = var0;
               sDefaultLocaleList = var0;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label289;
            }

            if (var1 == 0) {
               try {
                  sDefaultAdjustedLocaleList = sDefaultLocaleList;
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label289;
               }
            } else {
               try {
                  var0 = new LocaleListHelper(sLastDefaultLocale, sDefaultLocaleList);
                  sDefaultAdjustedLocaleList = var0;
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label289;
               }
            }

            label273:
            try {
               return;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label273;
            }
         }

         while(true) {
            Throwable var33 = var10000;

            try {
               throw var33;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof LocaleListHelper)) {
         return false;
      } else {
         Locale[] var3 = ((LocaleListHelper)var1).mList;
         if (this.mList.length != var3.length) {
            return false;
         } else {
            for(int var2 = 0; var2 < this.mList.length; ++var2) {
               if (!this.mList[var2].equals(var3[var2])) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   Locale get(int var1) {
      Locale var2;
      if (var1 >= 0 && var1 < this.mList.length) {
         var2 = this.mList[var1];
      } else {
         var2 = null;
      }

      return var2;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   Locale getFirstMatch(String[] var1) {
      return this.computeFirstMatch(Arrays.asList(var1), false);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getFirstMatchIndex(String[] var1) {
      return this.computeFirstMatchIndex(Arrays.asList(var1), false);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getFirstMatchIndexWithEnglishSupported(Collection var1) {
      return this.computeFirstMatchIndex(var1, true);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getFirstMatchIndexWithEnglishSupported(String[] var1) {
      return this.getFirstMatchIndexWithEnglishSupported((Collection)Arrays.asList(var1));
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   Locale getFirstMatchWithEnglishSupported(String[] var1) {
      return this.computeFirstMatch(Arrays.asList(var1), true);
   }

   public int hashCode() {
      int var1 = 1;

      for(int var2 = 0; var2 < this.mList.length; ++var2) {
         var1 = this.mList[var2].hashCode() + 31 * var1;
      }

      return var1;
   }

   @IntRange(
      from = -1L
   )
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int indexOf(Locale var1) {
      for(int var2 = 0; var2 < this.mList.length; ++var2) {
         if (this.mList[var2].equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   boolean isEmpty() {
      boolean var1;
      if (this.mList.length == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @IntRange(
      from = 0L
   )
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int size() {
      return this.mList.length;
   }

   @NonNull
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   String toLanguageTags() {
      return this.mStringRepresentation;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[");

      for(int var2 = 0; var2 < this.mList.length; ++var2) {
         var1.append(this.mList[var2]);
         if (var2 < this.mList.length - 1) {
            var1.append(',');
         }
      }

      var1.append("]");
      return var1.toString();
   }
}
