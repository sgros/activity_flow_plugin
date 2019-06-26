package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessageObject;

public class SeekBarWaveform {
   private static Paint paintInner;
   private static Paint paintOuter;
   private SeekBar.SeekBarDelegate delegate;
   private int height;
   private int innerColor;
   private MessageObject messageObject;
   private int outerColor;
   private View parentView;
   private boolean pressed = false;
   private boolean selected;
   private int selectedColor;
   private boolean startDraging = false;
   private float startX;
   private int thumbDX = 0;
   private int thumbX = 0;
   private byte[] waveformBytes;
   private int width;

   public SeekBarWaveform(Context var1) {
      if (paintInner == null) {
         paintInner = new Paint();
         paintOuter = new Paint();
      }

   }

   public void draw(Canvas var1) {
      if (this.waveformBytes != null) {
         int var2 = this.width;
         if (var2 != 0) {
            float var3 = (float)(var2 / AndroidUtilities.dp(3.0F));
            if (var3 <= 0.1F) {
               return;
            }

            int var4 = this.waveformBytes.length * 8 / 5;
            float var5 = (float)var4 / var3;
            Paint var6 = paintInner;
            MessageObject var7 = this.messageObject;
            if (var7 != null && !var7.isOutOwner() && this.messageObject.isContentUnread()) {
               var2 = this.outerColor;
            } else if (this.selected) {
               var2 = this.selectedColor;
            } else {
               var2 = this.innerColor;
            }

            var6.setColor(var2);
            paintOuter.setColor(this.outerColor);
            int var8 = (this.height - AndroidUtilities.dp(14.0F)) / 2;
            int var9 = 0;
            int var10 = 0;
            var3 = 0.0F;
            int var11 = 0;

            for(var2 = var4; var9 < var2; ++var9) {
               if (var9 == var10) {
                  int var12 = 0;

                  for(var4 = var10; var10 == var4; ++var12) {
                     var3 += var5;
                     var4 = (int)var3;
                  }

                  var10 = var9 * 5;
                  int var13 = var10 / 8;
                  int var14 = var10 - var13 * 8;
                  var10 = 8 - var14;
                  int var15 = 5 - var10;
                  byte var20 = (byte)(this.waveformBytes[var13] >> var14 & (2 << Math.min(5, var10) - 1) - 1);
                  byte var19 = var20;
                  if (var15 > 0) {
                     ++var13;
                     byte[] var18 = this.waveformBytes;
                     var19 = var20;
                     if (var13 < var18.length) {
                        var19 = (byte)((byte)(var20 << var15) | var18[var13] & (2 << var15 - 1) - 1);
                     }
                  }

                  for(var14 = 0; var14 < var12; ++var14) {
                     var13 = AndroidUtilities.dp(3.0F) * var11;
                     if (var13 < this.thumbX && AndroidUtilities.dp(2.0F) + var13 < this.thumbX) {
                        var1.drawRect((float)var13, (float)(AndroidUtilities.dp(14.0F - Math.max(1.0F, (float)var19 * 14.0F / 31.0F)) + var8), (float)(var13 + AndroidUtilities.dp(2.0F)), (float)(AndroidUtilities.dp(14.0F) + var8), paintOuter);
                     } else {
                        float var16 = (float)var13;
                        float var17 = (float)var19 * 14.0F / 31.0F;
                        var1.drawRect(var16, (float)(var8 + AndroidUtilities.dp(14.0F - Math.max(1.0F, var17))), (float)(var13 + AndroidUtilities.dp(2.0F)), (float)(var8 + AndroidUtilities.dp(14.0F)), paintInner);
                        if (var13 < this.thumbX) {
                           var1.drawRect(var16, (float)(AndroidUtilities.dp(14.0F - Math.max(1.0F, var17)) + var8), (float)this.thumbX, (float)(AndroidUtilities.dp(14.0F) + var8), paintOuter);
                        }
                     }

                     ++var11;
                  }

                  var10 = var4;
               }
            }
         }
      }

   }

   public boolean isDragging() {
      return this.pressed;
   }

   public boolean isStartDraging() {
      return this.startDraging;
   }

   public boolean onTouch(int var1, float var2, float var3) {
      if (var1 == 0) {
         if (0.0F <= var2 && var2 <= (float)this.width && var3 >= 0.0F && var3 <= (float)this.height) {
            this.startX = var2;
            this.pressed = true;
            this.thumbDX = (int)(var2 - (float)this.thumbX);
            this.startDraging = false;
            return true;
         }
      } else if (var1 != 1 && var1 != 3) {
         if (var1 == 2 && this.pressed) {
            if (this.startDraging) {
               this.thumbX = (int)(var2 - (float)this.thumbDX);
               var1 = this.thumbX;
               if (var1 < 0) {
                  this.thumbX = 0;
               } else {
                  int var4 = this.width;
                  if (var1 > var4) {
                     this.thumbX = var4;
                  }
               }
            }

            var3 = this.startX;
            if (var3 != -1.0F && Math.abs(var2 - var3) > AndroidUtilities.getPixelsInCM(0.2F, true)) {
               View var6 = this.parentView;
               if (var6 != null && var6.getParent() != null) {
                  this.parentView.getParent().requestDisallowInterceptTouchEvent(true);
               }

               this.startDraging = true;
               this.startX = -1.0F;
            }

            return true;
         }
      } else if (this.pressed) {
         if (var1 == 1) {
            SeekBar.SeekBarDelegate var5 = this.delegate;
            if (var5 != null) {
               var5.onSeekBarDrag((float)this.thumbX / (float)this.width);
            }
         }

         this.pressed = false;
         return true;
      }

      return false;
   }

   public void setColors(int var1, int var2, int var3) {
      this.innerColor = var1;
      this.outerColor = var2;
      this.selectedColor = var3;
   }

   public void setDelegate(SeekBar.SeekBarDelegate var1) {
      this.delegate = var1;
   }

   public void setMessageObject(MessageObject var1) {
      this.messageObject = var1;
   }

   public void setParentView(View var1) {
      this.parentView = var1;
   }

   public void setProgress(float var1) {
      this.thumbX = (int)Math.ceil((double)((float)this.width * var1));
      int var2 = this.thumbX;
      if (var2 < 0) {
         this.thumbX = 0;
      } else {
         int var3 = this.width;
         if (var2 > var3) {
            this.thumbX = var3;
         }
      }

   }

   public void setSelected(boolean var1) {
      this.selected = var1;
   }

   public void setSize(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   public void setWaveform(byte[] var1) {
      this.waveformBytes = var1;
   }
}
