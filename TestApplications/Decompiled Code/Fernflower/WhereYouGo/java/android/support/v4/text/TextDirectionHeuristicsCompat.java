package android.support.v4.text;

import java.nio.CharBuffer;
import java.util.Locale;

public final class TextDirectionHeuristicsCompat {
   public static final TextDirectionHeuristicCompat ANYRTL_LTR;
   public static final TextDirectionHeuristicCompat FIRSTSTRONG_LTR;
   public static final TextDirectionHeuristicCompat FIRSTSTRONG_RTL;
   public static final TextDirectionHeuristicCompat LOCALE;
   public static final TextDirectionHeuristicCompat LTR = new TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal((TextDirectionHeuristicsCompat.TextDirectionAlgorithm)null, false);
   public static final TextDirectionHeuristicCompat RTL = new TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal((TextDirectionHeuristicsCompat.TextDirectionAlgorithm)null, true);
   private static final int STATE_FALSE = 1;
   private static final int STATE_TRUE = 0;
   private static final int STATE_UNKNOWN = 2;

   static {
      FIRSTSTRONG_LTR = new TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal(TextDirectionHeuristicsCompat.FirstStrong.INSTANCE, false);
      FIRSTSTRONG_RTL = new TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal(TextDirectionHeuristicsCompat.FirstStrong.INSTANCE, true);
      ANYRTL_LTR = new TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal(TextDirectionHeuristicsCompat.AnyStrong.INSTANCE_RTL, false);
      LOCALE = TextDirectionHeuristicsCompat.TextDirectionHeuristicLocale.INSTANCE;
   }

   private TextDirectionHeuristicsCompat() {
   }

   static int isRtlText(int var0) {
      byte var1;
      switch(var0) {
      case 0:
         var1 = 1;
         break;
      case 1:
      case 2:
         var1 = 0;
         break;
      default:
         var1 = 2;
      }

      return var1;
   }

   static int isRtlTextOrFormat(int var0) {
      byte var1;
      switch(var0) {
      case 0:
      case 14:
      case 15:
         var1 = 1;
         break;
      case 1:
      case 2:
      case 16:
      case 17:
         var1 = 0;
         break;
      default:
         var1 = 2;
      }

      return var1;
   }

   private static class AnyStrong implements TextDirectionHeuristicsCompat.TextDirectionAlgorithm {
      public static final TextDirectionHeuristicsCompat.AnyStrong INSTANCE_LTR = new TextDirectionHeuristicsCompat.AnyStrong(false);
      public static final TextDirectionHeuristicsCompat.AnyStrong INSTANCE_RTL = new TextDirectionHeuristicsCompat.AnyStrong(true);
      private final boolean mLookForRtl;

      private AnyStrong(boolean var1) {
         this.mLookForRtl = var1;
      }

      public int checkRtl(CharSequence var1, int var2, int var3) {
         byte var4 = 1;
         boolean var5 = false;
         int var6 = var2;

         byte var7;
         while(true) {
            if (var6 >= var2 + var3) {
               if (var5) {
                  var7 = var4;
                  if (!this.mLookForRtl) {
                     var7 = 0;
                  }
               } else {
                  var7 = 2;
               }
               break;
            }

            switch(TextDirectionHeuristicsCompat.isRtlText(Character.getDirectionality(var1.charAt(var6)))) {
            case 0:
               if (this.mLookForRtl) {
                  var7 = 0;
                  return var7;
               }

               var5 = true;
               break;
            case 1:
               var7 = var4;
               if (!this.mLookForRtl) {
                  return var7;
               }

               var5 = true;
            }

            ++var6;
         }

         return var7;
      }
   }

   private static class FirstStrong implements TextDirectionHeuristicsCompat.TextDirectionAlgorithm {
      public static final TextDirectionHeuristicsCompat.FirstStrong INSTANCE = new TextDirectionHeuristicsCompat.FirstStrong();

      public int checkRtl(CharSequence var1, int var2, int var3) {
         int var4 = 2;

         for(int var5 = var2; var5 < var2 + var3 && var4 == 2; ++var5) {
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

      public TextDirectionHeuristicImpl(TextDirectionHeuristicsCompat.TextDirectionAlgorithm var1) {
         this.mAlgorithm = var1;
      }

      private boolean doCheck(CharSequence var1, int var2, int var3) {
         boolean var4;
         switch(this.mAlgorithm.checkRtl(var1, var2, var3)) {
         case 0:
            var4 = true;
            break;
         case 1:
            var4 = false;
            break;
         default:
            var4 = this.defaultIsRtl();
         }

         return var4;
      }

      protected abstract boolean defaultIsRtl();

      public boolean isRtl(CharSequence var1, int var2, int var3) {
         if (var1 != null && var2 >= 0 && var3 >= 0 && var1.length() - var3 >= var2) {
            boolean var4;
            if (this.mAlgorithm == null) {
               var4 = this.defaultIsRtl();
            } else {
               var4 = this.doCheck(var1, var2, var3);
            }

            return var4;
         } else {
            throw new IllegalArgumentException();
         }
      }

      public boolean isRtl(char[] var1, int var2, int var3) {
         return this.isRtl((CharSequence)CharBuffer.wrap(var1), var2, var3);
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
      public static final TextDirectionHeuristicsCompat.TextDirectionHeuristicLocale INSTANCE = new TextDirectionHeuristicsCompat.TextDirectionHeuristicLocale();

      public TextDirectionHeuristicLocale() {
         super((TextDirectionHeuristicsCompat.TextDirectionAlgorithm)null);
      }

      protected boolean defaultIsRtl() {
         boolean var1 = true;
         if (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) != 1) {
            var1 = false;
         }

         return var1;
      }
   }
}
