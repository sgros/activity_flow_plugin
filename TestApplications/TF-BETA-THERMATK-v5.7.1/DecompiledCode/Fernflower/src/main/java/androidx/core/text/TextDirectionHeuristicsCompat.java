package androidx.core.text;

import java.util.Locale;

public final class TextDirectionHeuristicsCompat {
   public static final TextDirectionHeuristicCompat ANYRTL_LTR;
   public static final TextDirectionHeuristicCompat FIRSTSTRONG_LTR;
   public static final TextDirectionHeuristicCompat FIRSTSTRONG_RTL;
   public static final TextDirectionHeuristicCompat LOCALE;
   public static final TextDirectionHeuristicCompat LTR = new TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal((TextDirectionHeuristicsCompat.TextDirectionAlgorithm)null, false);
   public static final TextDirectionHeuristicCompat RTL = new TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal((TextDirectionHeuristicsCompat.TextDirectionAlgorithm)null, true);

   static {
      FIRSTSTRONG_LTR = new TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal(TextDirectionHeuristicsCompat.FirstStrong.INSTANCE, false);
      FIRSTSTRONG_RTL = new TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal(TextDirectionHeuristicsCompat.FirstStrong.INSTANCE, true);
      ANYRTL_LTR = new TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal(TextDirectionHeuristicsCompat.AnyStrong.INSTANCE_RTL, false);
      LOCALE = TextDirectionHeuristicsCompat.TextDirectionHeuristicLocale.INSTANCE;
   }

   static int isRtlText(int var0) {
      if (var0 != 0) {
         return var0 != 1 && var0 != 2 ? 2 : 0;
      } else {
         return 1;
      }
   }

   static int isRtlTextOrFormat(int var0) {
      if (var0 != 0) {
         if (var0 == 1 || var0 == 2) {
            return 0;
         }

         switch(var0) {
         case 14:
         case 15:
            break;
         case 16:
         case 17:
            return 0;
         default:
            return 2;
         }
      }

      return 1;
   }

   private static class AnyStrong implements TextDirectionHeuristicsCompat.TextDirectionAlgorithm {
      static final TextDirectionHeuristicsCompat.AnyStrong INSTANCE_RTL = new TextDirectionHeuristicsCompat.AnyStrong(true);
      private final boolean mLookForRtl;

      private AnyStrong(boolean var1) {
         this.mLookForRtl = var1;
      }

      public int checkRtl(CharSequence var1, int var2, int var3) {
         boolean var4 = false;

         for(int var5 = var2; var5 < var3 + var2; ++var5) {
            int var6 = TextDirectionHeuristicsCompat.isRtlText(Character.getDirectionality(var1.charAt(var5)));
            if (var6 != 0) {
               if (var6 != 1) {
                  continue;
               }

               if (!this.mLookForRtl) {
                  return 1;
               }
            } else if (this.mLookForRtl) {
               return 0;
            }

            var4 = true;
         }

         if (var4) {
            return this.mLookForRtl;
         } else {
            return 2;
         }
      }
   }

   private static class FirstStrong implements TextDirectionHeuristicsCompat.TextDirectionAlgorithm {
      static final TextDirectionHeuristicsCompat.FirstStrong INSTANCE = new TextDirectionHeuristicsCompat.FirstStrong();

      public int checkRtl(CharSequence var1, int var2, int var3) {
         int var4 = 2;

         for(int var5 = var2; var5 < var3 + var2 && var4 == 2; ++var5) {
            var4 = TextDirectionHeuristicsCompat.isRtlTextOrFormat(Character.getDirectionality(var1.charAt(var5)));
         }

         return var4;
      }
   }

   private interface TextDirectionAlgorithm {
      int checkRtl(CharSequence var1, int var2, int var3);
   }

   private abstract static class TextDirectionHeuristicImpl implements TextDirectionHeuristicCompat {
      private final TextDirectionHeuristicsCompat.TextDirectionAlgorithm mAlgorithm;

      TextDirectionHeuristicImpl(TextDirectionHeuristicsCompat.TextDirectionAlgorithm var1) {
         this.mAlgorithm = var1;
      }

      private boolean doCheck(CharSequence var1, int var2, int var3) {
         var2 = this.mAlgorithm.checkRtl(var1, var2, var3);
         if (var2 != 0) {
            return var2 != 1 ? this.defaultIsRtl() : false;
         } else {
            return true;
         }
      }

      protected abstract boolean defaultIsRtl();

      public boolean isRtl(CharSequence var1, int var2, int var3) {
         if (var1 != null && var2 >= 0 && var3 >= 0 && var1.length() - var3 >= var2) {
            return this.mAlgorithm == null ? this.defaultIsRtl() : this.doCheck(var1, var2, var3);
         } else {
            throw new IllegalArgumentException();
         }
      }
   }

   private static class TextDirectionHeuristicInternal extends TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl {
      private final boolean mDefaultIsRtl;

      TextDirectionHeuristicInternal(TextDirectionHeuristicsCompat.TextDirectionAlgorithm var1, boolean var2) {
         super(var1);
         this.mDefaultIsRtl = var2;
      }

      protected boolean defaultIsRtl() {
         return this.mDefaultIsRtl;
      }
   }

   private static class TextDirectionHeuristicLocale extends TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl {
      static final TextDirectionHeuristicsCompat.TextDirectionHeuristicLocale INSTANCE = new TextDirectionHeuristicsCompat.TextDirectionHeuristicLocale();

      TextDirectionHeuristicLocale() {
         super((TextDirectionHeuristicsCompat.TextDirectionAlgorithm)null);
      }

      protected boolean defaultIsRtl() {
         int var1 = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault());
         boolean var2 = true;
         if (var1 != 1) {
            var2 = false;
         }

         return var2;
      }
   }
}
