package menion.android.whereyougo.gui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import menion.android.whereyougo.geo.location.SatellitePosition;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;

public class Satellite2DView extends View {
   private static final float SAT_TEXT_SIZE = Utils.getDpPixels(10.0F);
   private static final String TAG = "Satellite2DView";
   private Drawable bitCompassBg;
   private Bitmap bitSnr;
   private boolean drawLock;
   private int lastWidth;
   private float lineWidth;
   private Paint mPaintBitmap;
   private Paint mPaintSignalLine;
   private Paint mPaintText;
   private float r1;
   private Bitmap[] satImages;
   private final ArrayList satellites;
   private float satsPanelHeigh;
   private float snrWidth;
   private float spSize;
   private float spX;
   private float spY;
   private float space;

   public Satellite2DView(Context var1, AttributeSet var2, ArrayList var3) {
      super(var1, var2);
      this.setBasics();
      this.satellites = var3;
   }

   public Satellite2DView(Context var1, ArrayList var2) {
      super(var1);
      this.setBasics();
      this.satellites = var2;
   }

   private Bitmap getSatImage(float var1) {
      Bitmap var2;
      if (var1 < 25.0F) {
         var2 = this.satImages[0];
      } else if (var1 >= 20.0F && var1 < 40.0F) {
         var2 = this.satImages[1];
      } else {
         var2 = this.satImages[2];
      }

      return var2;
   }

   private void setBasics() {
      this.drawLock = false;
      this.space = Utils.getDpPixels(6.0F);
      this.bitCompassBg = Images.getImageD(2130837582);
      int var1 = (int)Utils.getDpPixels(20.0F);
      this.satImages = new Bitmap[3];
      this.satImages[0] = Images.getImageB(2130837548, var1);
      this.satImages[1] = Images.getImageB(2130837549, var1);
      this.satImages[2] = Images.getImageB(2130837550, var1);
      this.mPaintBitmap = new Paint();
      this.mPaintBitmap.setAntiAlias(true);
      this.mPaintBitmap.setFilterBitmap(true);
      this.mPaintText = new Paint();
      this.mPaintText.setAntiAlias(true);
      this.mPaintText.setTextAlign(Align.CENTER);
      this.mPaintText.setTextSize(SAT_TEXT_SIZE);
      this.mPaintText.setShadowLayer(SAT_TEXT_SIZE / 4.0F, 0.0F, 0.0F, -1);
      this.mPaintSignalLine = new Paint();
      this.mPaintSignalLine.setAntiAlias(true);
      this.mPaintSignalLine.setStyle(Style.STROKE);
      this.mPaintSignalLine.setStrokeWidth(2.0F);
      this.mPaintSignalLine.setColor(-7829368);
   }

   private void setConstants(Canvas var1) {
      if (this.lastWidth != var1.getWidth()) {
         this.lastWidth = var1.getWidth();
         int var2 = var1.getClipBounds().width();
         int var3 = var1.getClipBounds().height();
         this.lineWidth = ((float)var2 - this.space * 2.0F) / 20.0F;
         this.snrWidth = this.lineWidth - 2.0F;
         this.bitSnr = Images.getImageB(2130837583, (int)this.snrWidth);
         this.satsPanelHeigh = (float)this.bitSnr.getHeight() + this.space + this.mPaintText.getTextSize();
         float var4 = (float)var3;
         float var5 = this.satsPanelHeigh;
         float var6 = this.space;
         this.spSize = Math.min((float)var2, var4 - var5 - var6);
         this.r1 = this.spSize / 2.0F * 0.95F;
         this.spX = (float)(var1.getClipBounds().width() / 2);
         this.spY = this.spSize / 2.0F;
      }

   }

   protected void onDraw(Canvas var1) {
      if (!this.drawLock) {
         label120: {
            Exception var10000;
            label119: {
               int var2;
               boolean var10001;
               try {
                  this.drawLock = true;
                  this.setConstants(var1);
                  var2 = this.satellites.size();
                  this.bitCompassBg.setBounds((int)(this.spX - this.r1), (int)(this.spY - this.r1), (int)(this.spX + this.r1), (int)(this.spY + this.r1));
                  this.bitCompassBg.draw(var1);
                  this.mPaintText.setColor(-16777216);
               } catch (Exception var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label119;
               }

               if (var2 == 0) {
                  try {
                     this.mPaintText.setTextSize(Utils.getDpPixels(20.0F));
                     var1.drawText(Locale.getString(2131165227), this.spX, this.spY + this.mPaintText.descent(), this.mPaintText);
                     this.drawLock = false;
                     return;
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                  }
               } else {
                  label124: {
                     int var3;
                     float var4;
                     try {
                        this.mPaintText.setTextSize(SAT_TEXT_SIZE);
                        var3 = this.bitSnr.getHeight();
                        var4 = this.spSize;
                     } catch (Exception var29) {
                        var10000 = var29;
                        var10001 = false;
                        break label124;
                     }

                     float var5 = (float)var3;

                     float var6;
                     float var7;
                     double var8;
                     try {
                        var6 = (float)var1.getClipBounds().width();
                        var7 = this.spSize;
                        var1.drawLine(0.0F, var5 + var4, var6, (float)var3 + var7, this.mPaintSignalLine);
                        var8 = Math.log(100.0D);
                     } catch (Exception var28) {
                        var10000 = var28;
                        var10001 = false;
                        break label124;
                     }

                     int var10 = 0;

                     while(true) {
                        if (var10 >= var2) {
                           break label120;
                        }

                        SatellitePosition var11;
                        try {
                           var11 = (SatellitePosition)this.satellites.get(var10);
                        } catch (Exception var24) {
                           var10000 = var24;
                           var10001 = false;
                           break;
                        }

                        if (var2 % 2 == 0) {
                           try {
                              var5 = this.spX + (float)(var10 - var2 / 2) * this.lineWidth + this.lineWidth / 2.0F;
                           } catch (Exception var23) {
                              var10000 = var23;
                              var10001 = false;
                              break;
                           }
                        } else {
                           try {
                              var5 = this.spX + (float)(var10 - (var2 - 1) / 2) * this.lineWidth;
                           } catch (Exception var22) {
                              var10000 = var22;
                              var10001 = false;
                              break;
                           }
                        }

                        label126: {
                           try {
                              if (var11.isFixed()) {
                                 this.mPaintText.setColor(-16711936);
                                 break label126;
                              }
                           } catch (Exception var27) {
                              var10000 = var27;
                              var10001 = false;
                              break;
                           }

                           try {
                              this.mPaintText.setColor(-3355444);
                           } catch (Exception var21) {
                              var10000 = var21;
                              var10001 = false;
                              break;
                           }
                        }

                        StringBuilder var12;
                        String var13;
                        label97: {
                           label96: {
                              try {
                                 var12 = new StringBuilder();
                                 if (var11.getPrn() >= 10) {
                                    break label96;
                                 }
                              } catch (Exception var26) {
                                 var10000 = var26;
                                 var10001 = false;
                                 break;
                              }

                              var13 = "0";
                              break label97;
                           }

                           var13 = "";
                        }

                        double var14;
                        label90: {
                           try {
                              var1.drawText(var12.append(var13).append(var11.getPrn()).toString(), var5, this.spSize + this.satsPanelHeigh, this.mPaintText);
                              if (var11.getSnr() > 0) {
                                 var14 = (double)var11.getSnr();
                                 break label90;
                              }
                           } catch (Exception var25) {
                              var10000 = var25;
                              var10001 = false;
                              break;
                           }

                           var14 = 1.0D;
                        }

                        int var16;
                        try {
                           var16 = (int)(Math.log(var14) / var8 * (double)var3);
                        } catch (Exception var20) {
                           var10000 = var20;
                           var10001 = false;
                           break;
                        }

                        int var17 = var16;
                        if (var16 <= 0) {
                           var17 = 1;
                        }

                        try {
                           var1.drawBitmap(Bitmap.createBitmap(this.bitSnr, 0, var3 - var17, this.bitSnr.getWidth(), var17), var5 - this.snrWidth / 2.0F, this.spSize + (float)var3 - (float)var17, this.mPaintBitmap);
                           var6 = var11.getAzimuth();
                           var7 = (float)((double)this.r1 - Math.sin((double)(var11.getElevation() / 57.29578F)) * (double)this.r1) * 0.9F;
                           var5 = (float)((double)this.spX + (double)var7 * Math.sin((double)(var6 / 57.29578F)));
                           var7 = (float)((double)this.spY - (double)var7 * Math.cos((double)(var6 / 57.29578F)));
                           this.mPaintText.setColor(-16777216);
                           Bitmap var32 = this.getSatImage((float)var11.getSnr());
                           StringBuilder var33 = new StringBuilder();
                           var1.drawText(var33.append("").append(var11.getPrn()).toString(), var5, var7 - (float)(var32.getHeight() / 2) - 5.0F, this.mPaintText);
                           var1.drawBitmap(var32, var5 - (float)(var32.getWidth() / 2), var7 - (float)(var32.getHeight() / 2), this.mPaintBitmap);
                        } catch (Exception var19) {
                           var10000 = var19;
                           var10001 = false;
                           break;
                        }

                        ++var10;
                     }
                  }
               }
            }

            Exception var31 = var10000;
            Logger.e("Satellite2DView", "onDraw()", var31);
         }

         this.drawLock = false;
      }

   }
}
