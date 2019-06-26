package com.google.android.exoplayer2.text.webvtt;

import android.text.SpannableStringBuilder;
import android.text.Layout.Alignment;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.util.Log;

public final class WebvttCue extends Cue {
   public final long endTime;
   public final long startTime;

   public WebvttCue(long var1, long var3, CharSequence var5) {
      this(var1, var3, var5, (Alignment)null, Float.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE);
   }

   public WebvttCue(long var1, long var3, CharSequence var5, Alignment var6, float var7, int var8, int var9, float var10, int var11, float var12) {
      super(var5, var6, var7, var8, var9, var10, var11, var12);
      this.startTime = var1;
      this.endTime = var3;
   }

   public WebvttCue(CharSequence var1) {
      this(0L, 0L, var1);
   }

   public boolean isNormalCue() {
      boolean var1;
      if (super.line == Float.MIN_VALUE && super.position == Float.MIN_VALUE) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static class Builder {
      private long endTime;
      private float line;
      private int lineAnchor;
      private int lineType;
      private float position;
      private int positionAnchor;
      private long startTime;
      private SpannableStringBuilder text;
      private Alignment textAlignment;
      private float width;

      public Builder() {
         this.reset();
      }

      private WebvttCue.Builder derivePositionAnchorFromAlignment() {
         Alignment var1 = this.textAlignment;
         if (var1 == null) {
            this.positionAnchor = Integer.MIN_VALUE;
         } else {
            int var2 = null.$SwitchMap$android$text$Layout$Alignment[var1.ordinal()];
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     StringBuilder var3 = new StringBuilder();
                     var3.append("Unrecognized alignment: ");
                     var3.append(this.textAlignment);
                     Log.w("WebvttCueBuilder", var3.toString());
                     this.positionAnchor = 0;
                  } else {
                     this.positionAnchor = 2;
                  }
               } else {
                  this.positionAnchor = 1;
               }
            } else {
               this.positionAnchor = 0;
            }
         }

         return this;
      }

      public WebvttCue build() {
         if (this.position != Float.MIN_VALUE && this.positionAnchor == Integer.MIN_VALUE) {
            this.derivePositionAnchorFromAlignment();
         }

         return new WebvttCue(this.startTime, this.endTime, this.text, this.textAlignment, this.line, this.lineType, this.lineAnchor, this.position, this.positionAnchor, this.width);
      }

      public void reset() {
         this.startTime = 0L;
         this.endTime = 0L;
         this.text = null;
         this.textAlignment = null;
         this.line = Float.MIN_VALUE;
         this.lineType = Integer.MIN_VALUE;
         this.lineAnchor = Integer.MIN_VALUE;
         this.position = Float.MIN_VALUE;
         this.positionAnchor = Integer.MIN_VALUE;
         this.width = Float.MIN_VALUE;
      }

      public WebvttCue.Builder setEndTime(long var1) {
         this.endTime = var1;
         return this;
      }

      public WebvttCue.Builder setLine(float var1) {
         this.line = var1;
         return this;
      }

      public WebvttCue.Builder setLineAnchor(int var1) {
         this.lineAnchor = var1;
         return this;
      }

      public WebvttCue.Builder setLineType(int var1) {
         this.lineType = var1;
         return this;
      }

      public WebvttCue.Builder setPosition(float var1) {
         this.position = var1;
         return this;
      }

      public WebvttCue.Builder setPositionAnchor(int var1) {
         this.positionAnchor = var1;
         return this;
      }

      public WebvttCue.Builder setStartTime(long var1) {
         this.startTime = var1;
         return this;
      }

      public WebvttCue.Builder setText(SpannableStringBuilder var1) {
         this.text = var1;
         return this;
      }

      public WebvttCue.Builder setTextAlignment(Alignment var1) {
         this.textAlignment = var1;
         return this;
      }

      public WebvttCue.Builder setWidth(float var1) {
         this.width = var1;
         return this;
      }
   }
}
