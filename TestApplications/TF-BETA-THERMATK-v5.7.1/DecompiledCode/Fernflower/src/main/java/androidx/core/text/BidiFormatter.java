package androidx.core.text;

import android.text.SpannableStringBuilder;
import java.util.Locale;

public final class BidiFormatter {
   static final BidiFormatter DEFAULT_LTR_INSTANCE;
   static final BidiFormatter DEFAULT_RTL_INSTANCE;
   static final TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC;
   private static final String LRM_STRING;
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

   BidiFormatter(boolean var1, int var2, TextDirectionHeuristicCompat var3) {
      this.mIsRtlContext = var1;
      this.mFlags = var2;
      this.mDefaultTextDirectionHeuristicCompat = var3;
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

   static boolean isRtlLocale(Locale var0) {
      int var1 = TextUtilsCompat.getLayoutDirectionFromLocale(var0);
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   private String markAfter(CharSequence var1, TextDirectionHeuristicCompat var2) {
      boolean var3 = var2.isRtl(var1, 0, var1.length());
      if (this.mIsRtlContext || !var3 && getExitDir(var1) != 1) {
         return !this.mIsRtlContext || var3 && getExitDir(var1) != -1 ? "" : RLM_STRING;
      } else {
         return LRM_STRING;
      }
   }

   private String markBefore(CharSequence var1, TextDirectionHeuristicCompat var2) {
      boolean var3 = var2.isRtl(var1, 0, var1.length());
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

   public CharSequence unicodeWrap(CharSequence var1) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, true);
   }

   public CharSequence unicodeWrap(CharSequence var1, TextDirectionHeuristicCompat var2, boolean var3) {
      if (var1 == null) {
         return null;
      } else {
         boolean var4 = var2.isRtl(var1, 0, var1.length());
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

   public static final class Builder {
      private int mFlags;
      private boolean mIsRtlContext;
      private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

      public Builder() {
         this.initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
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
   }

   private static class DirectionalityEstimator {
      private static final byte[] DIR_TYPE_CACHE = new byte[1792];
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

         char var4;
         do {
            int var2 = this.charIndex;
            if (var2 <= 0) {
               break;
            }

            CharSequence var3 = this.text;
            --var2;
            this.charIndex = var2;
            this.lastChar = var3.charAt(var2);
            var4 = this.lastChar;
            if (var4 == '&') {
               return 12;
            }
         } while(var4 != ';');

         this.charIndex = var1;
         this.lastChar = (char)59;
         return 13;
      }

      private byte skipEntityForward() {
         while(true) {
            int var1 = this.charIndex;
            if (var1 < this.length) {
               CharSequence var2 = this.text;
               this.charIndex = var1 + 1;
               char var3 = var2.charAt(var1);
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

         while(true) {
            int var2 = this.charIndex;
            if (var2 <= 0) {
               break;
            }

            CharSequence var3 = this.text;
            --var2;
            this.charIndex = var2;
            this.lastChar = var3.charAt(var2);
            char var5 = this.lastChar;
            if (var5 == '<') {
               return 12;
            }

            if (var5 == '>') {
               break;
            }

            if (var5 == '"' || var5 == '\'') {
               var5 = this.lastChar;

               while(true) {
                  int var4 = this.charIndex;
                  if (var4 <= 0) {
                     break;
                  }

                  var3 = this.text;
                  --var4;
                  this.charIndex = var4;
                  char var6 = var3.charAt(var4);
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
            CharSequence var3;
            char var5;
            do {
               int var2 = this.charIndex;
               if (var2 >= this.length) {
                  this.charIndex = var1;
                  this.lastChar = (char)60;
                  return 13;
               }

               var3 = this.text;
               this.charIndex = var2 + 1;
               this.lastChar = var3.charAt(var2);
               var5 = this.lastChar;
               if (var5 == '>') {
                  return 12;
               }
            } while(var5 != '"' && var5 != '\'');

            var5 = this.lastChar;

            while(true) {
               int var4 = this.charIndex;
               if (var4 >= this.length) {
                  break;
               }

               var3 = this.text;
               this.charIndex = var4 + 1;
               char var6 = var3.charAt(var4);
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
            int var5 = Character.codePointBefore(this.text, this.charIndex);
            this.charIndex -= Character.charCount(var5);
            return Character.getDirectionality(var5);
         } else {
            --this.charIndex;
            byte var2 = getCachedDirectionality(this.lastChar);
            byte var3 = var2;
            if (this.isHtml) {
               char var1 = this.lastChar;
               byte var4;
               if (var1 == '>') {
                  var4 = this.skipTagBackward();
                  var3 = var4;
               } else {
                  var3 = var2;
                  if (var1 == ';') {
                     var4 = this.skipEntityBackward();
                     var3 = var4;
                  }
               }
            }

            return var3;
         }
      }

      byte dirTypeForward() {
         this.lastChar = this.text.charAt(this.charIndex);
         if (Character.isHighSurrogate(this.lastChar)) {
            int var5 = Character.codePointAt(this.text, this.charIndex);
            this.charIndex += Character.charCount(var5);
            return Character.getDirectionality(var5);
         } else {
            ++this.charIndex;
            byte var2 = getCachedDirectionality(this.lastChar);
            byte var3 = var2;
            if (this.isHtml) {
               char var1 = this.lastChar;
               byte var4;
               if (var1 == '<') {
                  var4 = this.skipTagForward();
                  var3 = var4;
               } else {
                  var3 = var2;
                  if (var1 == '&') {
                     var4 = this.skipEntityForward();
                     var3 = var4;
                  }
               }
            }

            return var3;
         }
      }

      int getEntryDir() {
         this.charIndex = 0;
         int var1 = 0;
         byte var2 = 0;
         int var3 = 0;

         while(this.charIndex < this.length && var1 == 0) {
            byte var4 = this.dirTypeForward();
            if (var4 != 0) {
               if (var4 != 1 && var4 != 2) {
                  if (var4 == 9) {
                     continue;
                  }

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
               } else if (var3 == 0) {
                  return 1;
               }
            } else if (var3 == 0) {
               return -1;
            }

            var1 = var3;
         }

         if (var1 == 0) {
            return 0;
         } else if (var2 != 0) {
            return var2;
         } else {
            while(true) {
               while(this.charIndex > 0) {
                  switch(this.dirTypeBackward()) {
                  case 14:
                  case 15:
                     if (var1 == var3) {
                        return -1;
                     }
                     break;
                  case 16:
                  case 17:
                     if (var1 == var3) {
                        return 1;
                     }
                     break;
                  case 18:
                     ++var3;
                  default:
                     continue;
                  }

                  --var3;
               }

               return 0;
            }
         }
      }

      int getExitDir() {
         this.charIndex = this.length;
         int var1 = 0;
         int var2 = 0;

         while(true) {
            while(this.charIndex > 0) {
               byte var3 = this.dirTypeBackward();
               if (var3 != 0) {
                  if (var3 != 1 && var3 != 2) {
                     label61: {
                        if (var3 == 9) {
                           continue;
                        }

                        switch(var3) {
                        case 14:
                        case 15:
                           if (var1 == var2) {
                              return -1;
                           }
                           break;
                        case 16:
                        case 17:
                           if (var1 == var2) {
                              return 1;
                           }
                           break;
                        case 18:
                           ++var2;
                           continue;
                        default:
                           if (var1 != 0) {
                              continue;
                           }
                           break label61;
                        }

                        --var2;
                        continue;
                     }
                  } else {
                     if (var2 == 0) {
                        return 1;
                     }

                     if (var1 != 0) {
                        continue;
                     }
                  }
               } else {
                  if (var2 == 0) {
                     return -1;
                  }

                  if (var1 != 0) {
                     continue;
                  }
               }

               var1 = var2;
            }

            return 0;
         }
      }
   }
}
