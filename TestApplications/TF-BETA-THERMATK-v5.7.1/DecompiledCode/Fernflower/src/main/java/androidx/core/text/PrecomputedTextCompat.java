package androidx.core.text;

import android.os.Build.VERSION;
import android.text.Spannable;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import androidx.core.util.ObjectsCompat;
import java.util.concurrent.Executor;

public class PrecomputedTextCompat implements Spannable {
   private static Executor sExecutor = null;
   private static final Object sLock = new Object();
   private final PrecomputedTextCompat.Params mParams;
   private final Spannable mText;

   public char charAt(int var1) {
      return this.mText.charAt(var1);
   }

   public PrecomputedTextCompat.Params getParams() {
      return this.mParams;
   }

   public int getSpanEnd(Object var1) {
      return this.mText.getSpanEnd(var1);
   }

   public int getSpanFlags(Object var1) {
      return this.mText.getSpanFlags(var1);
   }

   public int getSpanStart(Object var1) {
      return this.mText.getSpanStart(var1);
   }

   public Object[] getSpans(int var1, int var2, Class var3) {
      return this.mText.getSpans(var1, var2, var3);
   }

   public int length() {
      return this.mText.length();
   }

   public int nextSpanTransition(int var1, int var2, Class var3) {
      return this.mText.nextSpanTransition(var1, var2, var3);
   }

   public void removeSpan(Object var1) {
      if (!(var1 instanceof MetricAffectingSpan)) {
         this.mText.removeSpan(var1);
      } else {
         throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
      }
   }

   public void setSpan(Object var1, int var2, int var3, int var4) {
      if (!(var1 instanceof MetricAffectingSpan)) {
         this.mText.setSpan(var1, var2, var3, var4);
      } else {
         throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
      }
   }

   public CharSequence subSequence(int var1, int var2) {
      return this.mText.subSequence(var1, var2);
   }

   public String toString() {
      return this.mText.toString();
   }

   public static final class Params {
      private final int mBreakStrategy;
      private final int mHyphenationFrequency;
      private final TextPaint mPaint;
      private final TextDirectionHeuristic mTextDir;
      final android.text.PrecomputedText.Params mWrapped;

      public Params(android.text.PrecomputedText.Params var1) {
         this.mPaint = var1.getTextPaint();
         this.mTextDir = var1.getTextDirection();
         this.mBreakStrategy = var1.getBreakStrategy();
         this.mHyphenationFrequency = var1.getHyphenationFrequency();
         this.mWrapped = null;
      }

      Params(TextPaint var1, TextDirectionHeuristic var2, int var3, int var4) {
         this.mWrapped = null;
         this.mPaint = var1;
         this.mTextDir = var2;
         this.mBreakStrategy = var3;
         this.mHyphenationFrequency = var4;
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof PrecomputedTextCompat.Params)) {
            return false;
         } else {
            PrecomputedTextCompat.Params var2 = (PrecomputedTextCompat.Params)var1;
            if (!this.equalsWithoutTextDirection(var2)) {
               return false;
            } else {
               return VERSION.SDK_INT < 18 || this.mTextDir == var2.getTextDirection();
            }
         }
      }

      public boolean equalsWithoutTextDirection(PrecomputedTextCompat.Params var1) {
         android.text.PrecomputedText.Params var2 = this.mWrapped;
         if (var2 != null) {
            return var2.equals(var1.mWrapped);
         } else {
            if (VERSION.SDK_INT >= 23) {
               if (this.mBreakStrategy != var1.getBreakStrategy()) {
                  return false;
               }

               if (this.mHyphenationFrequency != var1.getHyphenationFrequency()) {
                  return false;
               }
            }

            if (this.mPaint.getTextSize() != var1.getTextPaint().getTextSize()) {
               return false;
            } else if (this.mPaint.getTextScaleX() != var1.getTextPaint().getTextScaleX()) {
               return false;
            } else if (this.mPaint.getTextSkewX() != var1.getTextPaint().getTextSkewX()) {
               return false;
            } else {
               if (VERSION.SDK_INT >= 21) {
                  if (this.mPaint.getLetterSpacing() != var1.getTextPaint().getLetterSpacing()) {
                     return false;
                  }

                  if (!TextUtils.equals(this.mPaint.getFontFeatureSettings(), var1.getTextPaint().getFontFeatureSettings())) {
                     return false;
                  }
               }

               if (this.mPaint.getFlags() != var1.getTextPaint().getFlags()) {
                  return false;
               } else {
                  int var3 = VERSION.SDK_INT;
                  if (var3 >= 24) {
                     if (!this.mPaint.getTextLocales().equals(var1.getTextPaint().getTextLocales())) {
                        return false;
                     }
                  } else if (var3 >= 17 && !this.mPaint.getTextLocale().equals(var1.getTextPaint().getTextLocale())) {
                     return false;
                  }

                  if (this.mPaint.getTypeface() == null) {
                     if (var1.getTextPaint().getTypeface() != null) {
                        return false;
                     }
                  } else if (!this.mPaint.getTypeface().equals(var1.getTextPaint().getTypeface())) {
                     return false;
                  }

                  return true;
               }
            }
         }
      }

      public int getBreakStrategy() {
         return this.mBreakStrategy;
      }

      public int getHyphenationFrequency() {
         return this.mHyphenationFrequency;
      }

      public TextDirectionHeuristic getTextDirection() {
         return this.mTextDir;
      }

      public TextPaint getTextPaint() {
         return this.mPaint;
      }

      public int hashCode() {
         int var1 = VERSION.SDK_INT;
         if (var1 >= 24) {
            return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getLetterSpacing(), this.mPaint.getFlags(), this.mPaint.getTextLocales(), this.mPaint.getTypeface(), this.mPaint.isElegantTextHeight(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
         } else if (var1 >= 21) {
            return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getLetterSpacing(), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mPaint.isElegantTextHeight(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
         } else if (var1 >= 18) {
            return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
         } else {
            return var1 >= 17 ? ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency) : ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getFlags(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
         }
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("{");
         StringBuilder var2 = new StringBuilder();
         var2.append("textSize=");
         var2.append(this.mPaint.getTextSize());
         var1.append(var2.toString());
         var2 = new StringBuilder();
         var2.append(", textScaleX=");
         var2.append(this.mPaint.getTextScaleX());
         var1.append(var2.toString());
         var2 = new StringBuilder();
         var2.append(", textSkewX=");
         var2.append(this.mPaint.getTextSkewX());
         var1.append(var2.toString());
         if (VERSION.SDK_INT >= 21) {
            var2 = new StringBuilder();
            var2.append(", letterSpacing=");
            var2.append(this.mPaint.getLetterSpacing());
            var1.append(var2.toString());
            var2 = new StringBuilder();
            var2.append(", elegantTextHeight=");
            var2.append(this.mPaint.isElegantTextHeight());
            var1.append(var2.toString());
         }

         int var3 = VERSION.SDK_INT;
         if (var3 >= 24) {
            var2 = new StringBuilder();
            var2.append(", textLocale=");
            var2.append(this.mPaint.getTextLocales());
            var1.append(var2.toString());
         } else if (var3 >= 17) {
            var2 = new StringBuilder();
            var2.append(", textLocale=");
            var2.append(this.mPaint.getTextLocale());
            var1.append(var2.toString());
         }

         var2 = new StringBuilder();
         var2.append(", typeface=");
         var2.append(this.mPaint.getTypeface());
         var1.append(var2.toString());
         if (VERSION.SDK_INT >= 26) {
            var2 = new StringBuilder();
            var2.append(", variationSettings=");
            var2.append(this.mPaint.getFontVariationSettings());
            var1.append(var2.toString());
         }

         var2 = new StringBuilder();
         var2.append(", textDir=");
         var2.append(this.mTextDir);
         var1.append(var2.toString());
         var2 = new StringBuilder();
         var2.append(", breakStrategy=");
         var2.append(this.mBreakStrategy);
         var1.append(var2.toString());
         var2 = new StringBuilder();
         var2.append(", hyphenationFrequency=");
         var2.append(this.mHyphenationFrequency);
         var1.append(var2.toString());
         var1.append("}");
         return var1.toString();
      }

      public static class Builder {
         private int mBreakStrategy;
         private int mHyphenationFrequency;
         private final TextPaint mPaint;
         private TextDirectionHeuristic mTextDir;

         public Builder(TextPaint var1) {
            this.mPaint = var1;
            if (VERSION.SDK_INT >= 23) {
               this.mBreakStrategy = 1;
               this.mHyphenationFrequency = 1;
            } else {
               this.mHyphenationFrequency = 0;
               this.mBreakStrategy = 0;
            }

            if (VERSION.SDK_INT >= 18) {
               this.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
            } else {
               this.mTextDir = null;
            }

         }

         public PrecomputedTextCompat.Params build() {
            return new PrecomputedTextCompat.Params(this.mPaint, this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
         }

         public PrecomputedTextCompat.Params.Builder setBreakStrategy(int var1) {
            this.mBreakStrategy = var1;
            return this;
         }

         public PrecomputedTextCompat.Params.Builder setHyphenationFrequency(int var1) {
            this.mHyphenationFrequency = var1;
            return this;
         }

         public PrecomputedTextCompat.Params.Builder setTextDirection(TextDirectionHeuristic var1) {
            this.mTextDir = var1;
            return this;
         }
      }
   }
}
