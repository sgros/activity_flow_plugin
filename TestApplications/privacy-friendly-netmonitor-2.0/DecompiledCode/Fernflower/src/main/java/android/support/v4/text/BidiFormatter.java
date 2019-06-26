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
      int var1 = TextUtilsCompat.getLayoutDirectionFromLocale(var0);
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   private String markAfter(CharSequence var1, TextDirectionHeuristicCompat var2) {
      boolean var3 = var2.isRtl((CharSequence)var1, 0, var1.length());
      if (this.mIsRtlContext || !var3 && getExitDir(var1) != 1) {
         return !this.mIsRtlContext || var3 && getExitDir(var1) != -1 ? "" : RLM_STRING;
      } else {
         return LRM_STRING;
      }
   }

   private String markBefore(CharSequence var1, TextDirectionHeuristicCompat var2) {
      boolean var3 = var2.isRtl((CharSequence)var1, 0, var1.length());
      if (this.mIsRtlContext || !var3 && getEntryDir(var1) != 1) {
         return !this.mIsRtlContext || var3 && getEntryDir(var1) != -1 ? "" : RLM_STRING;
      } else {
         return LRM_STRING;
      }
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
      if (var1 == null) {
         return null;
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

         if (var3) {
            if (var4) {
               var2 = TextDirectionHeuristicsCompat.RTL;
            } else {
               var2 = TextDirectionHeuristicsCompat.LTR;
            }

            var5.append(this.markAfter(var1, var2));
         }

         return var5;
      }
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
      return var1 == null ? null : this.unicodeWrap((CharSequence)var1, var2, var3).toString();
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
         return this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC ? getDefaultInstanceFromContext(this.mIsRtlContext) : new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat);
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

         while(this.charIndex > 0) {
            CharSequence var2 = this.text;
            int var3 = this.charIndex - 1;
            this.charIndex = var3;
            this.lastChar = var2.charAt(var3);
            if (this.lastChar == '&') {
               return 12;
            }

            if (this.lastChar == ';') {
               break;
            }
         }

         this.charIndex = var1;
         this.lastChar = (char)59;
         return 13;
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

         while(this.charIndex > 0) {
            CharSequence var2 = this.text;
            int var3 = this.charIndex - 1;
            this.charIndex = var3;
            this.lastChar = var2.charAt(var3);
            if (this.lastChar == '<') {
               return 12;
            }

            if (this.lastChar == '>') {
               break;
            }

            if (this.lastChar == '"' || this.lastChar == '\'') {
               char var5 = this.lastChar;

               while(this.charIndex > 0) {
                  var2 = this.text;
                  int var4 = this.charIndex - 1;
                  this.charIndex = var4;
                  char var6 = var2.charAt(var4);
                  this.lastChar = (char)var6;
                  if (var6 == var5) {
                     break;
                  }
               }
            }
         }

         this.charIndex = var1;
         this.lastChar = (char)62;
         return 13;
      }

      private byte skipTagForward() {
         int var1 = this.charIndex;

         while(true) {
            CharSequence var2;
            do {
               if (this.charIndex >= this.length) {
                  this.charIndex = var1;
                  this.lastChar = (char)60;
                  return 13;
               }

               var2 = this.text;
               int var3 = this.charIndex++;
               this.lastChar = var2.charAt(var3);
               if (this.lastChar == '>') {
                  return 12;
               }
            } while(this.lastChar != '"' && this.lastChar != '\'');

            char var5 = this.lastChar;

            while(this.charIndex < this.length) {
               var2 = this.text;
               int var4 = this.charIndex++;
               char var6 = var2.charAt(var4);
               this.lastChar = (char)var6;
               if (var6 == var5) {
                  break;
               }
            }
         }
      }

      byte dirTypeBackward() {
         this.lastChar = this.text.charAt(this.charIndex - 1);
         if (Character.isLowSurrogate(this.lastChar)) {
            int var3 = Character.codePointBefore(this.text, this.charIndex);
            this.charIndex -= Character.charCount(var3);
            return Character.getDirectionality(var3);
         } else {
            --this.charIndex;
            byte var1 = getCachedDirectionality(this.lastChar);
            byte var2 = var1;
            if (this.isHtml) {
               if (this.lastChar == '>') {
                  var1 = this.skipTagBackward();
                  var2 = var1;
               } else {
                  var2 = var1;
                  if (this.lastChar == ';') {
                     var1 = this.skipEntityBackward();
                     var2 = var1;
                  }
               }
            }

            return var2;
         }
      }

      byte dirTypeForward() {
         this.lastChar = this.text.charAt(this.charIndex);
         if (Character.isHighSurrogate(this.lastChar)) {
            int var3 = Character.codePointAt(this.text, this.charIndex);
            this.charIndex += Character.charCount(var3);
            return Character.getDirectionality(var3);
         } else {
            ++this.charIndex;
            byte var1 = getCachedDirectionality(this.lastChar);
            byte var2 = var1;
            if (this.isHtml) {
               if (this.lastChar == '<') {
                  var1 = this.skipTagForward();
                  var2 = var1;
               } else {
                  var2 = var1;
                  if (this.lastChar == '&') {
                     var1 = this.skipEntityForward();
                     var2 = var1;
                  }
               }
            }

            return var2;
         }
      }

      int getEntryDir() {
         this.charIndex = 0;
         int var1 = 0;
         int var2 = var1;
         int var3 = var1;

         while(this.charIndex < this.length && var1 == 0) {
            byte var4 = this.dirTypeForward();
            if (var4 != 9) {
               switch(var4) {
               case 0:
                  if (var3 == 0) {
                     return -1;
                  }
                  break;
               case 1:
               case 2:
                  if (var3 == 0) {
                     return 1;
                  }
                  break;
               default:
                  switch(var4) {
                  case 14:
                  case 15:
                     ++var3;
                     var2 = -1;
                     continue;
                  case 16:
                  case 17:
                     ++var3;
                     var2 = 1;
                     continue;
                  case 18:
                     --var3;
                     var2 = 0;
                     continue;
                  }
               }

               var1 = var3;
            }
         }

         if (var1 == 0) {
            return 0;
         } else if (var2 != 0) {
            return var2;
         } else {
            while(this.charIndex > 0) {
               switch(this.dirTypeBackward()) {
               case 14:
               case 15:
                  if (var1 == var3) {
                     return -1;
                  }

                  --var3;
                  break;
               case 16:
               case 17:
                  if (var1 == var3) {
                     return 1;
                  }

                  --var3;
                  break;
               case 18:
                  ++var3;
               }
            }

            return 0;
         }
      }

      int getExitDir() {
         this.charIndex = this.length;
         int var1 = 0;
         int var2 = var1;

         while(true) {
            while(true) {
               byte var3;
               do {
                  if (this.charIndex <= 0) {
                     return 0;
                  }

                  var3 = this.dirTypeBackward();
               } while(var3 == 9);

               switch(var3) {
               case 0:
                  if (var2 == 0) {
                     return -1;
                  }

                  if (var1 != 0) {
                     continue;
                  }
                  break;
               case 1:
               case 2:
                  if (var2 == 0) {
                     return 1;
                  }

                  if (var1 != 0) {
                     continue;
                  }
                  break;
               default:
                  switch(var3) {
                  case 14:
                  case 15:
                     if (var1 == var2) {
                        return -1;
                     }

                     --var2;
                     continue;
                  case 16:
                  case 17:
                     if (var1 == var2) {
                        return 1;
                     }

                     --var2;
                     continue;
                  case 18:
                     ++var2;
                     continue;
                  default:
                     if (var1 != 0) {
                        continue;
                     }
                  }
               }

               var1 = var2;
            }
         }
      }
   }
}
