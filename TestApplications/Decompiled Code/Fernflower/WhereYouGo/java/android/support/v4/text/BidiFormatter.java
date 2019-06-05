package android.support.v4.text;

import android.text.SpannableStringBuilder;
import java.util.Locale;

public final class BidiFormatter {
   private static final int DEFAULT_FLAGS = 2;
   private static final BidiFormatter DEFAULT_LTR_INSTANCE;
   private static final BidiFormatter DEFAULT_RTL_INSTANCE;
   private static TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC;
   private static final int DIR_LTR = -1;
   private static final int DIR_RTL = 1;
   private static final int DIR_UNKNOWN = 0;
   private static final String EMPTY_STRING = "";
   private static final int FLAG_STEREO_RESET = 2;
   private static final char LRE = '\u202a';
   private static final char LRM = '\u200e';
   private static final String LRM_STRING;
   private static final char PDF = '\u202c';
   private static final char RLE = '\u202b';
   private static final char RLM = '\u200f';
   private static final String RLM_STRING;
   private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
   private final int mFlags;
   private final boolean mIsRtlContext;

   static {
      DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
      LRM_STRING = Character.toString('\u200e');
      RLM_STRING = Character.toString('\u200f');
      DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
      DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
   }

   private BidiFormatter(boolean var1, int var2, TextDirectionHeuristicCompat var3) {
      this.mIsRtlContext = var1;
      this.mFlags = var2;
      this.mDefaultTextDirectionHeuristicCompat = var3;
   }

   // $FF: synthetic method
   BidiFormatter(boolean var1, int var2, TextDirectionHeuristicCompat var3, Object var4) {
      this(var1, var2, var3);
   }

   private static int getEntryDir(CharSequence var0) {
      return (new BidiFormatter.DirectionalityEstimator(var0, false)).getEntryDir();
   }

   private static int getExitDir(CharSequence var0) {
      return (new BidiFormatter.DirectionalityEstimator(var0, false)).getExitDir();
   }

   public static BidiFormatter getInstance() {
      return (new BidiFormatter.Builder()).build();
   }

   public static BidiFormatter getInstance(Locale var0) {
      return (new BidiFormatter.Builder(var0)).build();
   }

   public static BidiFormatter getInstance(boolean var0) {
      return (new BidiFormatter.Builder(var0)).build();
   }

   private static boolean isRtlLocale(Locale var0) {
      boolean var1 = true;
      if (TextUtilsCompat.getLayoutDirectionFromLocale(var0) != 1) {
         var1 = false;
      }

      return var1;
   }

   private String markAfter(CharSequence var1, TextDirectionHeuristicCompat var2) {
      boolean var3 = var2.isRtl((CharSequence)var1, 0, var1.length());
      String var4;
      if (this.mIsRtlContext || !var3 && getExitDir(var1) != 1) {
         if (!this.mIsRtlContext || var3 && getExitDir(var1) != -1) {
            var4 = "";
         } else {
            var4 = RLM_STRING;
         }
      } else {
         var4 = LRM_STRING;
      }

      return var4;
   }

   private String markBefore(CharSequence var1, TextDirectionHeuristicCompat var2) {
      boolean var3 = var2.isRtl((CharSequence)var1, 0, var1.length());
      String var4;
      if (this.mIsRtlContext || !var3 && getEntryDir(var1) != 1) {
         if (!this.mIsRtlContext || var3 && getEntryDir(var1) != -1) {
            var4 = "";
         } else {
            var4 = RLM_STRING;
         }
      } else {
         var4 = LRM_STRING;
      }

      return var4;
   }

   public boolean getStereoReset() {
      boolean var1;
      if ((this.mFlags & 2) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isRtl(CharSequence var1) {
      return this.mDefaultTextDirectionHeuristicCompat.isRtl((CharSequence)var1, 0, var1.length());
   }

   public boolean isRtl(String var1) {
      return this.isRtl((CharSequence)var1);
   }

   public boolean isRtlContext() {
      return this.mIsRtlContext;
   }

   public CharSequence unicodeWrap(CharSequence var1) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, true);
   }

   public CharSequence unicodeWrap(CharSequence var1, TextDirectionHeuristicCompat var2) {
      return this.unicodeWrap(var1, var2, true);
   }

   public CharSequence unicodeWrap(CharSequence var1, TextDirectionHeuristicCompat var2, boolean var3) {
      SpannableStringBuilder var8;
      if (var1 == null) {
         var8 = null;
      } else {
         boolean var4 = var2.isRtl((CharSequence)var1, 0, var1.length());
         SpannableStringBuilder var5 = new SpannableStringBuilder();
         if (this.getStereoReset() && var3) {
            if (var4) {
               var2 = TextDirectionHeuristicsCompat.RTL;
            } else {
               var2 = TextDirectionHeuristicsCompat.LTR;
            }

            var5.append(this.markBefore(var1, var2));
         }

         if (var4 != this.mIsRtlContext) {
            short var6;
            char var7;
            if (var4) {
               var6 = 8235;
               var7 = (char)var6;
            } else {
               var6 = 8234;
               var7 = (char)var6;
            }

            var5.append(var7);
            var5.append(var1);
            var5.append('\u202c');
         } else {
            var5.append(var1);
         }

         var8 = var5;
         if (var3) {
            if (var4) {
               var2 = TextDirectionHeuristicsCompat.RTL;
            } else {
               var2 = TextDirectionHeuristicsCompat.LTR;
            }

            var5.append(this.markAfter(var1, var2));
            var8 = var5;
         }
      }

      return var8;
   }

   public CharSequence unicodeWrap(CharSequence var1, boolean var2) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, var2);
   }

   public String unicodeWrap(String var1) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, true);
   }

   public String unicodeWrap(String var1, TextDirectionHeuristicCompat var2) {
      return this.unicodeWrap(var1, var2, true);
   }

   public String unicodeWrap(String var1, TextDirectionHeuristicCompat var2, boolean var3) {
      if (var1 == null) {
         var1 = null;
      } else {
         var1 = this.unicodeWrap((CharSequence)var1, var2, var3).toString();
      }

      return var1;
   }

   public String unicodeWrap(String var1, boolean var2) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, var2);
   }

   public static final class Builder {
      private int mFlags;
      private boolean mIsRtlContext;
      private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

      public Builder() {
         this.initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
      }

      public Builder(Locale var1) {
         this.initialize(BidiFormatter.isRtlLocale(var1));
      }

      public Builder(boolean var1) {
         this.initialize(var1);
      }

      private static BidiFormatter getDefaultInstanceFromContext(boolean var0) {
         BidiFormatter var1;
         if (var0) {
            var1 = BidiFormatter.DEFAULT_RTL_INSTANCE;
         } else {
            var1 = BidiFormatter.DEFAULT_LTR_INSTANCE;
         }

         return var1;
      }

      private void initialize(boolean var1) {
         this.mIsRtlContext = var1;
         this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
         this.mFlags = 2;
      }

      public BidiFormatter build() {
         BidiFormatter var1;
         if (this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) {
            var1 = getDefaultInstanceFromContext(this.mIsRtlContext);
         } else {
            var1 = new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat);
         }

         return var1;
      }

      public BidiFormatter.Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat var1) {
         this.mTextDirectionHeuristicCompat = var1;
         return this;
      }

      public BidiFormatter.Builder stereoReset(boolean var1) {
         if (var1) {
            this.mFlags |= 2;
         } else {
            this.mFlags &= -3;
         }

         return this;
      }
   }

   private static class DirectionalityEstimator {
      private static final byte[] DIR_TYPE_CACHE = new byte[1792];
      private static final int DIR_TYPE_CACHE_SIZE = 1792;
      private int charIndex;
      private final boolean isHtml;
      private char lastChar;
      private final int length;
      private final CharSequence text;

      static {
         for(int var0 = 0; var0 < 1792; ++var0) {
            DIR_TYPE_CACHE[var0] = Character.getDirectionality(var0);
         }

      }

      DirectionalityEstimator(CharSequence var1, boolean var2) {
         this.text = var1;
         this.isHtml = var2;
         this.length = var1.length();
      }

      private static byte getCachedDirectionality(char var0) {
         byte var1;
         byte var2;
         if (var0 < 1792) {
            var1 = DIR_TYPE_CACHE[var0];
            var2 = var1;
         } else {
            var1 = Character.getDirectionality(var0);
            var2 = var1;
         }

         return var2;
      }

      private byte skipEntityBackward() {
         int var1 = this.charIndex;

         byte var4;
         while(true) {
            byte var5;
            if (this.charIndex > 0) {
               CharSequence var2 = this.text;
               int var3 = this.charIndex - 1;
               this.charIndex = var3;
               this.lastChar = var2.charAt(var3);
               if (this.lastChar == '&') {
                  var5 = 12;
                  var4 = var5;
                  break;
               }

               if (this.lastChar != ';') {
                  continue;
               }
            }

            this.charIndex = var1;
            this.lastChar = (char)59;
            var5 = 13;
            var4 = var5;
            break;
         }

         return var4;
      }

      private byte skipEntityForward() {
         while(true) {
            if (this.charIndex < this.length) {
               CharSequence var1 = this.text;
               int var2 = this.charIndex++;
               char var3 = var1.charAt(var2);
               this.lastChar = (char)var3;
               if (var3 != ';') {
                  continue;
               }
            }

            return 12;
         }
      }

      private byte skipTagBackward() {
         int var1 = this.charIndex;

         byte var4;
         label30:
         while(true) {
            byte var6;
            if (this.charIndex > 0) {
               CharSequence var2 = this.text;
               int var3 = this.charIndex - 1;
               this.charIndex = var3;
               this.lastChar = var2.charAt(var3);
               if (this.lastChar == '<') {
                  var6 = 12;
                  var4 = var6;
                  break;
               }

               if (this.lastChar != '>') {
                  if (this.lastChar != '"' && this.lastChar != '\'') {
                     continue;
                  }

                  char var7 = this.lastChar;

                  while(true) {
                     if (this.charIndex <= 0) {
                        continue label30;
                     }

                     var2 = this.text;
                     int var5 = this.charIndex - 1;
                     this.charIndex = var5;
                     char var8 = var2.charAt(var5);
                     this.lastChar = (char)var8;
                     if (var8 == var7) {
                        continue label30;
                     }
                  }
               }
            }

            this.charIndex = var1;
            this.lastChar = (char)62;
            var6 = 13;
            var4 = var6;
            break;
         }

         return var4;
      }

      private byte skipTagForward() {
         int var1 = this.charIndex;

         byte var4;
         label29:
         while(true) {
            byte var6;
            if (this.charIndex < this.length) {
               CharSequence var2 = this.text;
               int var3 = this.charIndex++;
               this.lastChar = var2.charAt(var3);
               if (this.lastChar == '>') {
                  var6 = 12;
                  var4 = var6;
                  break;
               }

               if (this.lastChar != '"' && this.lastChar != '\'') {
                  continue;
               }

               char var7 = this.lastChar;

               while(true) {
                  if (this.charIndex >= this.length) {
                     continue label29;
                  }

                  var2 = this.text;
                  int var5 = this.charIndex++;
                  char var8 = var2.charAt(var5);
                  this.lastChar = (char)var8;
                  if (var8 == var7) {
                     continue label29;
                  }
               }
            }

            this.charIndex = var1;
            this.lastChar = (char)60;
            var6 = 13;
            var4 = var6;
            break;
         }

         return var4;
      }

      byte dirTypeBackward() {
         this.lastChar = this.text.charAt(this.charIndex - 1);
         byte var2;
         byte var3;
         if (Character.isLowSurrogate(this.lastChar)) {
            int var1 = Character.codePointBefore(this.text, this.charIndex);
            this.charIndex -= Character.charCount(var1);
            var3 = Character.getDirectionality(var1);
            var2 = var3;
         } else {
            --this.charIndex;
            var3 = getCachedDirectionality(this.lastChar);
            var2 = var3;
            if (this.isHtml) {
               if (this.lastChar == '>') {
                  var3 = this.skipTagBackward();
                  var2 = var3;
               } else {
                  var2 = var3;
                  if (this.lastChar == ';') {
                     var3 = this.skipEntityBackward();
                     var2 = var3;
                  }
               }
            }
         }

         return var2;
      }

      byte dirTypeForward() {
         this.lastChar = this.text.charAt(this.charIndex);
         byte var2;
         byte var3;
         if (Character.isHighSurrogate(this.lastChar)) {
            int var1 = Character.codePointAt(this.text, this.charIndex);
            this.charIndex += Character.charCount(var1);
            var3 = Character.getDirectionality(var1);
            var2 = var3;
         } else {
            ++this.charIndex;
            var3 = getCachedDirectionality(this.lastChar);
            var2 = var3;
            if (this.isHtml) {
               if (this.lastChar == '<') {
                  var3 = this.skipTagForward();
                  var2 = var3;
               } else {
                  var2 = var3;
                  if (this.lastChar == '&') {
                     var3 = this.skipEntityForward();
                     var2 = var3;
                  }
               }
            }
         }

         return var2;
      }

      int getEntryDir() {
         this.charIndex = 0;
         int var1 = 0;
         byte var2 = 0;
         int var3 = 0;

         byte var4;
         while(true) {
            if (this.charIndex >= this.length || var3 != 0) {
               if (var3 == 0) {
                  var4 = 0;
               } else {
                  var4 = var2;
                  if (var2 == 0) {
                     while(this.charIndex > 0) {
                        switch(this.dirTypeBackward()) {
                        case 14:
                        case 15:
                           if (var3 == var1) {
                              var4 = -1;
                              return var4;
                           }

                           --var1;
                           break;
                        case 16:
                        case 17:
                           if (var3 == var1) {
                              var4 = 1;
                              return var4;
                           }

                           --var1;
                           break;
                        case 18:
                           ++var1;
                        }
                     }

                     var4 = 0;
                  }
               }
               break;
            }

            switch(this.dirTypeForward()) {
            case 0:
               if (var1 == 0) {
                  var4 = -1;
                  return var4;
               }

               var3 = var1;
               break;
            case 1:
            case 2:
               if (var1 == 0) {
                  var4 = 1;
                  return var4;
               }

               var3 = var1;
               break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            default:
               var3 = var1;
            case 9:
               break;
            case 14:
            case 15:
               ++var1;
               var2 = -1;
               break;
            case 16:
            case 17:
               ++var1;
               var2 = 1;
               break;
            case 18:
               --var1;
               var2 = 0;
            }
         }

         return var4;
      }

      int getExitDir() {
         byte var1 = -1;
         this.charIndex = this.length;
         int var2 = 0;
         int var3 = 0;

         byte var4;
         while(this.charIndex > 0) {
            switch(this.dirTypeBackward()) {
            case 0:
               if (var2 == 0) {
                  var4 = var1;
                  return var4;
               }

               if (var3 == 0) {
                  var3 = var2;
               }
               break;
            case 1:
            case 2:
               if (var2 == 0) {
                  var4 = 1;
                  return var4;
               }

               if (var3 == 0) {
                  var3 = var2;
               }
               break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            default:
               if (var3 == 0) {
                  var3 = var2;
               }
            case 9:
               break;
            case 14:
            case 15:
               var4 = var1;
               if (var3 == var2) {
                  return var4;
               }

               --var2;
               break;
            case 16:
            case 17:
               if (var3 == var2) {
                  var4 = 1;
                  return var4;
               }

               --var2;
               break;
            case 18:
               ++var2;
            }
         }

         var4 = 0;
         return var4;
      }
   }
}
