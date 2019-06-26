package org.telegram.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import java.io.File;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;

public class PhotoCropActivity extends BaseFragment {
   private static final int done_button = 1;
   private String bitmapKey;
   private PhotoCropActivity.PhotoEditActivityDelegate delegate = null;
   private boolean doneButtonPressed = false;
   private BitmapDrawable drawable;
   private Bitmap imageToCrop;
   private boolean sameBitmap = false;
   private PhotoCropActivity.PhotoCropView view;

   public PhotoCropActivity(Bundle var1) {
      super(var1);
   }

   public View createView(Context var1) {
      super.actionBar.setBackgroundColor(-13421773);
      super.actionBar.setItemsBackgroundColor(-12763843, false);
      super.actionBar.setTitleColor(-1);
      super.actionBar.setItemsColor(-1, false);
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("CropImage", 2131559176));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               PhotoCropActivity.this.finishFragment();
            } else if (var1 == 1) {
               if (PhotoCropActivity.this.delegate != null && !PhotoCropActivity.this.doneButtonPressed) {
                  Bitmap var2 = PhotoCropActivity.this.view.getBitmap();
                  if (var2 == PhotoCropActivity.this.imageToCrop) {
                     PhotoCropActivity.this.sameBitmap = true;
                  }

                  PhotoCropActivity.this.delegate.didFinishEdit(var2);
                  PhotoCropActivity.this.doneButtonPressed = true;
               }

               PhotoCropActivity.this.finishFragment();
            }

         }
      });
      super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      PhotoCropActivity.PhotoCropView var2 = new PhotoCropActivity.PhotoCropView(var1);
      this.view = var2;
      super.fragmentView = var2;
      ((PhotoCropActivity.PhotoCropView)super.fragmentView).freeform = this.getArguments().getBoolean("freeform", false);
      super.fragmentView.setLayoutParams(new LayoutParams(-1, -1));
      return super.fragmentView;
   }

   public boolean onFragmentCreate() {
      super.swipeBackEnabled = false;
      if (this.imageToCrop == null) {
         String var1 = this.getArguments().getString("photoPath");
         Uri var2 = (Uri)this.getArguments().getParcelable("photoUri");
         if (var1 == null && var2 == null) {
            return false;
         }

         if (var1 != null && !(new File(var1)).exists()) {
            return false;
         }

         int var3;
         if (AndroidUtilities.isTablet()) {
            var3 = AndroidUtilities.dp(520.0F);
         } else {
            Point var4 = AndroidUtilities.displaySize;
            var3 = Math.max(var4.x, var4.y);
         }

         float var5 = (float)var3;
         this.imageToCrop = ImageLoader.loadBitmap(var1, var2, var5, var5, true);
         if (this.imageToCrop == null) {
            return false;
         }
      }

      this.drawable = new BitmapDrawable(this.imageToCrop);
      super.onFragmentCreate();
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      if (this.bitmapKey != null && ImageLoader.getInstance().decrementUseCount(this.bitmapKey) && !ImageLoader.getInstance().isInCache(this.bitmapKey)) {
         this.bitmapKey = null;
      }

      if (this.bitmapKey == null) {
         Bitmap var1 = this.imageToCrop;
         if (var1 != null && !this.sameBitmap) {
            var1.recycle();
            this.imageToCrop = null;
         }
      }

      this.drawable = null;
   }

   public void setDelegate(PhotoCropActivity.PhotoEditActivityDelegate var1) {
      this.delegate = var1;
   }

   private class PhotoCropView extends FrameLayout {
      int bitmapHeight;
      int bitmapWidth;
      int bitmapX;
      int bitmapY;
      Paint circlePaint = null;
      int draggingState = 0;
      boolean freeform;
      Paint halfPaint = null;
      float oldX = 0.0F;
      float oldY = 0.0F;
      Paint rectPaint = null;
      float rectSizeX = 600.0F;
      float rectSizeY = 600.0F;
      float rectX = -1.0F;
      float rectY = -1.0F;
      int viewHeight;
      int viewWidth;

      public PhotoCropView(Context var2) {
         super(var2);
         this.init();
      }

      private void init() {
         this.rectPaint = new Paint();
         this.rectPaint.setColor(1073412858);
         this.rectPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
         this.rectPaint.setStyle(Style.STROKE);
         this.circlePaint = new Paint();
         this.circlePaint.setColor(-1);
         this.halfPaint = new Paint();
         this.halfPaint.setColor(-939524096);
         this.setBackgroundColor(-13421773);
         this.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View var1, MotionEvent var2) {
               float var3 = var2.getX();
               float var4 = var2.getY();
               int var5 = AndroidUtilities.dp(14.0F);
               float var6;
               float var7;
               float var8;
               PhotoCropActivity.PhotoCropView var12;
               if (var2.getAction() == 0) {
                  label193: {
                     var12 = PhotoCropView.this;
                     var6 = var12.rectX;
                     var7 = (float)var5;
                     if (var6 - var7 < var3 && var6 + var7 > var3) {
                        var6 = var12.rectY;
                        if (var6 - var7 < var4 && var6 + var7 > var4) {
                           var12.draggingState = 1;
                           break label193;
                        }
                     }

                     var12 = PhotoCropView.this;
                     var6 = var12.rectX;
                     var8 = var12.rectSizeX;
                     if (var6 - var7 + var8 < var3 && var6 + var7 + var8 > var3) {
                        var6 = var12.rectY;
                        if (var6 - var7 < var4 && var6 + var7 > var4) {
                           var12.draggingState = 2;
                           break label193;
                        }
                     }

                     var12 = PhotoCropView.this;
                     var6 = var12.rectX;
                     if (var6 - var7 < var3 && var6 + var7 > var3) {
                        var6 = var12.rectY;
                        var8 = var12.rectSizeY;
                        if (var6 - var7 + var8 < var4 && var6 + var7 + var8 > var4) {
                           var12.draggingState = 3;
                           break label193;
                        }
                     }

                     var12 = PhotoCropView.this;
                     var6 = var12.rectX;
                     var8 = var12.rectSizeX;
                     if (var6 - var7 + var8 < var3 && var6 + var7 + var8 > var3) {
                        var6 = var12.rectY;
                        var8 = var12.rectSizeY;
                        if (var6 - var7 + var8 < var4 && var6 + var7 + var8 > var4) {
                           var12.draggingState = 4;
                           break label193;
                        }
                     }

                     var12 = PhotoCropView.this;
                     var7 = var12.rectX;
                     if (var7 < var3 && var7 + var12.rectSizeX > var3) {
                        var7 = var12.rectY;
                        if (var7 < var4 && var7 + var12.rectSizeY > var4) {
                           var12.draggingState = 5;
                           break label193;
                        }
                     }

                     PhotoCropView.this.draggingState = 0;
                  }

                  var12 = PhotoCropView.this;
                  if (var12.draggingState != 0) {
                     var12.requestDisallowInterceptTouchEvent(true);
                  }

                  var12 = PhotoCropView.this;
                  var12.oldX = var3;
                  var12.oldY = var4;
               } else if (var2.getAction() == 1) {
                  PhotoCropView.this.draggingState = 0;
               } else if (var2.getAction() == 2) {
                  var12 = PhotoCropView.this;
                  var5 = var12.draggingState;
                  if (var5 != 0) {
                     var6 = var3 - var12.oldX;
                     var7 = var4 - var12.oldY;
                     int var9;
                     if (var5 == 5) {
                        var12.rectX += var6;
                        var12.rectY += var7;
                        var6 = var12.rectX;
                        var9 = var12.bitmapX;
                        if (var6 < (float)var9) {
                           var12.rectX = (float)var9;
                        } else {
                           var7 = var12.rectSizeX;
                           var5 = var12.bitmapWidth;
                           if (var6 + var7 > (float)(var9 + var5)) {
                              var12.rectX = (float)(var9 + var5) - var7;
                           }
                        }

                        var12 = PhotoCropView.this;
                        var6 = var12.rectY;
                        var5 = var12.bitmapY;
                        if (var6 < (float)var5) {
                           var12.rectY = (float)var5;
                        } else {
                           var7 = var12.rectSizeY;
                           var9 = var12.bitmapHeight;
                           if (var6 + var7 > (float)(var5 + var9)) {
                              var12.rectY = (float)(var5 + var9) - var7;
                           }
                        }
                     } else {
                        float var10;
                        if (var5 == 1) {
                           var10 = var12.rectSizeX;
                           var8 = var6;
                           if (var10 - var6 < 160.0F) {
                              var8 = var10 - 160.0F;
                           }

                           var12 = PhotoCropView.this;
                           var10 = var12.rectX;
                           var5 = var12.bitmapX;
                           var6 = var8;
                           if (var10 + var8 < (float)var5) {
                              var6 = (float)var5 - var10;
                           }

                           var12 = PhotoCropView.this;
                           if (!var12.freeform) {
                              var8 = var12.rectY;
                              var5 = var12.bitmapY;
                              var7 = var6;
                              if (var8 + var6 < (float)var5) {
                                 var7 = (float)var5 - var8;
                              }

                              var12 = PhotoCropView.this;
                              var12.rectX += var7;
                              var12.rectY += var7;
                              var12.rectSizeX -= var7;
                              var12.rectSizeY -= var7;
                           } else {
                              var10 = var12.rectSizeY;
                              var8 = var7;
                              if (var10 - var7 < 160.0F) {
                                 var8 = var10 - 160.0F;
                              }

                              var12 = PhotoCropView.this;
                              var10 = var12.rectY;
                              var5 = var12.bitmapY;
                              var7 = var8;
                              if (var10 + var8 < (float)var5) {
                                 var7 = (float)var5 - var10;
                              }

                              var12 = PhotoCropView.this;
                              var12.rectX += var6;
                              var12.rectY += var7;
                              var12.rectSizeX -= var6;
                              var12.rectSizeY -= var7;
                           }
                        } else {
                           float var11;
                           if (var5 == 2) {
                              var10 = var12.rectSizeX;
                              var8 = var6;
                              if (var10 + var6 < 160.0F) {
                                 var8 = -(var10 - 160.0F);
                              }

                              var12 = PhotoCropView.this;
                              var11 = var12.rectX;
                              var10 = var12.rectSizeX;
                              var9 = var12.bitmapX;
                              var5 = var12.bitmapWidth;
                              var6 = var8;
                              if (var11 + var10 + var8 > (float)(var9 + var5)) {
                                 var6 = (float)(var9 + var5) - var11 - var10;
                              }

                              var12 = PhotoCropView.this;
                              if (!var12.freeform) {
                                 var8 = var12.rectY;
                                 var5 = var12.bitmapY;
                                 var7 = var6;
                                 if (var8 - var6 < (float)var5) {
                                    var7 = var8 - (float)var5;
                                 }

                                 var12 = PhotoCropView.this;
                                 var12.rectY -= var7;
                                 var12.rectSizeX += var7;
                                 var12.rectSizeY += var7;
                              } else {
                                 var10 = var12.rectSizeY;
                                 var8 = var7;
                                 if (var10 - var7 < 160.0F) {
                                    var8 = var10 - 160.0F;
                                 }

                                 var12 = PhotoCropView.this;
                                 var10 = var12.rectY;
                                 var5 = var12.bitmapY;
                                 var7 = var8;
                                 if (var10 + var8 < (float)var5) {
                                    var7 = (float)var5 - var10;
                                 }

                                 var12 = PhotoCropView.this;
                                 var12.rectY += var7;
                                 var12.rectSizeX += var6;
                                 var12.rectSizeY -= var7;
                              }
                           } else if (var5 == 3) {
                              var10 = var12.rectSizeX;
                              var8 = var6;
                              if (var10 - var6 < 160.0F) {
                                 var8 = var10 - 160.0F;
                              }

                              var12 = PhotoCropView.this;
                              var10 = var12.rectX;
                              var5 = var12.bitmapX;
                              var6 = var8;
                              if (var10 + var8 < (float)var5) {
                                 var6 = (float)var5 - var10;
                              }

                              var12 = PhotoCropView.this;
                              if (!var12.freeform) {
                                 var10 = var12.rectY;
                                 var8 = var12.rectSizeX;
                                 var9 = var12.bitmapY;
                                 var5 = var12.bitmapHeight;
                                 var7 = var6;
                                 if (var10 + var8 - var6 > (float)(var9 + var5)) {
                                    var7 = var10 + var8 - (float)var9 - (float)var5;
                                 }

                                 var12 = PhotoCropView.this;
                                 var12.rectX += var7;
                                 var12.rectSizeX -= var7;
                                 var12.rectSizeY -= var7;
                              } else {
                                 var11 = var12.rectY;
                                 var10 = var12.rectSizeY;
                                 var5 = var12.bitmapY;
                                 var9 = var12.bitmapHeight;
                                 var8 = var7;
                                 if (var11 + var10 + var7 > (float)(var5 + var9)) {
                                    var8 = (float)(var5 + var9) - var11 - var10;
                                 }

                                 var12 = PhotoCropView.this;
                                 var12.rectX += var6;
                                 var12.rectSizeX -= var6;
                                 var12.rectSizeY += var8;
                                 if (var12.rectSizeY < 160.0F) {
                                    var12.rectSizeY = 160.0F;
                                 }
                              }
                           } else if (var5 == 4) {
                              var10 = var12.rectX;
                              var11 = var12.rectSizeX;
                              var5 = var12.bitmapX;
                              var9 = var12.bitmapWidth;
                              var8 = var6;
                              if (var10 + var11 + var6 > (float)(var5 + var9)) {
                                 var8 = (float)(var5 + var9) - var10 - var11;
                              }

                              var12 = PhotoCropView.this;
                              if (!var12.freeform) {
                                 var10 = var12.rectY;
                                 var6 = var12.rectSizeX;
                                 var9 = var12.bitmapY;
                                 var5 = var12.bitmapHeight;
                                 var7 = var8;
                                 if (var10 + var6 + var8 > (float)(var9 + var5)) {
                                    var7 = (float)(var9 + var5) - var10 - var6;
                                 }

                                 var12 = PhotoCropView.this;
                                 var12.rectSizeX += var7;
                                 var12.rectSizeY += var7;
                              } else {
                                 var10 = var12.rectY;
                                 var11 = var12.rectSizeY;
                                 var9 = var12.bitmapY;
                                 var5 = var12.bitmapHeight;
                                 var6 = var7;
                                 if (var10 + var11 + var7 > (float)(var9 + var5)) {
                                    var6 = (float)(var9 + var5) - var10 - var11;
                                 }

                                 var12 = PhotoCropView.this;
                                 var12.rectSizeX += var8;
                                 var12.rectSizeY += var6;
                              }

                              var12 = PhotoCropView.this;
                              if (var12.rectSizeX < 160.0F) {
                                 var12.rectSizeX = 160.0F;
                              }

                              var12 = PhotoCropView.this;
                              if (var12.rectSizeY < 160.0F) {
                                 var12.rectSizeY = 160.0F;
                              }
                           }
                        }
                     }

                     var12 = PhotoCropView.this;
                     var12.oldX = var3;
                     var12.oldY = var4;
                     var12.invalidate();
                  }
               }

               return true;
            }
         });
      }

      private void updateBitmapSize() {
         if (this.viewWidth != 0 && this.viewHeight != 0 && PhotoCropActivity.this.imageToCrop != null) {
            float var1 = this.rectX;
            float var2 = (float)this.bitmapX;
            int var3 = this.bitmapWidth;
            var2 = (var1 - var2) / (float)var3;
            float var4 = this.rectY;
            var1 = (float)this.bitmapY;
            int var5 = this.bitmapHeight;
            float var6 = (var4 - var1) / (float)var5;
            var1 = this.rectSizeX / (float)var3;
            float var7 = this.rectSizeY / (float)var5;
            float var8 = (float)PhotoCropActivity.this.imageToCrop.getWidth();
            float var9 = (float)PhotoCropActivity.this.imageToCrop.getHeight();
            var5 = this.viewWidth;
            float var10 = (float)var5 / var8;
            var3 = this.viewHeight;
            var4 = (float)var3 / var9;
            if (var10 > var4) {
               this.bitmapHeight = var3;
               this.bitmapWidth = (int)Math.ceil((double)(var8 * var4));
            } else {
               this.bitmapWidth = var5;
               this.bitmapHeight = (int)Math.ceil((double)(var9 * var10));
            }

            this.bitmapX = (this.viewWidth - this.bitmapWidth) / 2 + AndroidUtilities.dp(14.0F);
            this.bitmapY = (this.viewHeight - this.bitmapHeight) / 2 + AndroidUtilities.dp(14.0F);
            if (this.rectX == -1.0F && this.rectY == -1.0F) {
               if (this.freeform) {
                  this.rectY = (float)this.bitmapY;
                  this.rectX = (float)this.bitmapX;
                  this.rectSizeX = (float)this.bitmapWidth;
                  this.rectSizeY = (float)this.bitmapHeight;
               } else {
                  var5 = this.bitmapWidth;
                  var3 = this.bitmapHeight;
                  if (var5 > var3) {
                     this.rectY = (float)this.bitmapY;
                     this.rectX = (float)((this.viewWidth - var3) / 2 + AndroidUtilities.dp(14.0F));
                     var3 = this.bitmapHeight;
                     this.rectSizeX = (float)var3;
                     this.rectSizeY = (float)var3;
                  } else {
                     this.rectX = (float)this.bitmapX;
                     this.rectY = (float)((this.viewHeight - var5) / 2 + AndroidUtilities.dp(14.0F));
                     var3 = this.bitmapWidth;
                     this.rectSizeX = (float)var3;
                     this.rectSizeY = (float)var3;
                  }
               }
            } else {
               var3 = this.bitmapWidth;
               this.rectX = var2 * (float)var3 + (float)this.bitmapX;
               var5 = this.bitmapHeight;
               this.rectY = var6 * (float)var5 + (float)this.bitmapY;
               this.rectSizeX = var1 * (float)var3;
               this.rectSizeY = var7 * (float)var5;
            }

            this.invalidate();
         }

      }

      public Bitmap getBitmap() {
         float var1 = this.rectX;
         float var2 = (float)this.bitmapX;
         int var3 = this.bitmapWidth;
         var1 = (var1 - var2) / (float)var3;
         float var4 = (this.rectY - (float)this.bitmapY) / (float)this.bitmapHeight;
         var2 = this.rectSizeX / (float)var3;
         float var5 = this.rectSizeY / (float)var3;
         int var6 = (int)(var1 * (float)PhotoCropActivity.this.imageToCrop.getWidth());
         int var7 = (int)(var4 * (float)PhotoCropActivity.this.imageToCrop.getHeight());
         int var8 = (int)(var2 * (float)PhotoCropActivity.this.imageToCrop.getWidth());
         int var9 = (int)(var5 * (float)PhotoCropActivity.this.imageToCrop.getWidth());
         var3 = var6;
         if (var6 < 0) {
            var3 = 0;
         }

         var6 = var7;
         if (var7 < 0) {
            var6 = 0;
         }

         var7 = var8;
         if (var3 + var8 > PhotoCropActivity.this.imageToCrop.getWidth()) {
            var7 = PhotoCropActivity.this.imageToCrop.getWidth() - var3;
         }

         var8 = var9;
         if (var6 + var9 > PhotoCropActivity.this.imageToCrop.getHeight()) {
            var8 = PhotoCropActivity.this.imageToCrop.getHeight() - var6;
         }

         Bitmap var10;
         try {
            var10 = Bitmaps.createBitmap(PhotoCropActivity.this.imageToCrop, var3, var6, var7, var8);
            return var10;
         } catch (Throwable var12) {
            FileLog.e(var12);
            System.gc();

            try {
               var10 = Bitmaps.createBitmap(PhotoCropActivity.this.imageToCrop, var3, var6, var7, var8);
               return var10;
            } catch (Throwable var11) {
               FileLog.e(var11);
               return null;
            }
         }
      }

      protected void onDraw(Canvas var1) {
         if (PhotoCropActivity.this.drawable != null) {
            label30: {
               Throwable var2;
               label29: {
                  BitmapDrawable var13;
                  try {
                     PhotoCropActivity.this.drawable.setBounds(this.bitmapX, this.bitmapY, this.bitmapX + this.bitmapWidth, this.bitmapY + this.bitmapHeight);
                     var13 = PhotoCropActivity.this.drawable;
                  } catch (Throwable var12) {
                     var2 = var12;
                     break label29;
                  }

                  try {
                     var13.draw(var1);
                     break label30;
                  } catch (Throwable var11) {
                     var2 = var11;
                  }
               }

               FileLog.e(var2);
            }
         }

         int var3 = this.bitmapX;
         var1.drawRect((float)var3, (float)this.bitmapY, (float)(var3 + this.bitmapWidth), this.rectY, this.halfPaint);
         float var4 = (float)this.bitmapX;
         float var5 = this.rectY;
         var1.drawRect(var4, var5, this.rectX, var5 + this.rectSizeY, this.halfPaint);
         float var6 = this.rectX;
         var4 = this.rectSizeX;
         var5 = this.rectY;
         var1.drawRect(var6 + var4, var5, (float)(this.bitmapX + this.bitmapWidth), var5 + this.rectSizeY, this.halfPaint);
         var3 = this.bitmapX;
         var5 = (float)var3;
         var4 = this.rectY;
         var1.drawRect(var5, this.rectSizeY + var4, (float)(var3 + this.bitmapWidth), (float)(this.bitmapY + this.bitmapHeight), this.halfPaint);
         var4 = this.rectX;
         var5 = this.rectY;
         var1.drawRect(var4, var5, var4 + this.rectSizeX, var5 + this.rectSizeY, this.rectPaint);
         var3 = AndroidUtilities.dp(1.0F);
         float var7 = this.rectX;
         var4 = (float)var3;
         float var8 = this.rectY;
         var6 = (float)AndroidUtilities.dp(20.0F);
         float var9 = this.rectY;
         var5 = (float)(var3 * 3);
         var1.drawRect(var7 + var4, var8 + var4, var6 + var7 + var4, var9 + var5, this.circlePaint);
         var7 = this.rectX;
         var6 = this.rectY;
         var1.drawRect(var7 + var4, var6 + var4, var7 + var5, var6 + var4 + (float)AndroidUtilities.dp(20.0F), this.circlePaint);
         var6 = this.rectX;
         var9 = this.rectSizeX;
         var8 = (float)AndroidUtilities.dp(20.0F);
         var7 = this.rectY;
         var1.drawRect(var6 + var9 - var4 - var8, var7 + var4, this.rectX + this.rectSizeX - var4, var7 + var5, this.circlePaint);
         var8 = this.rectX;
         var7 = this.rectSizeX;
         var6 = this.rectY;
         var1.drawRect(var8 + var7 - var5, var6 + var4, var8 + var7 - var4, var6 + var4 + (float)AndroidUtilities.dp(20.0F), this.circlePaint);
         var1.drawRect(this.rectX + var4, this.rectY + this.rectSizeY - var4 - (float)AndroidUtilities.dp(20.0F), this.rectX + var5, this.rectY + this.rectSizeY - var4, this.circlePaint);
         var6 = this.rectX;
         var1.drawRect(var6 + var4, this.rectY + this.rectSizeY - var5, (float)AndroidUtilities.dp(20.0F) + var6 + var4, this.rectY + this.rectSizeY - var4, this.circlePaint);
         var8 = this.rectX;
         var7 = this.rectSizeX;
         var9 = (float)AndroidUtilities.dp(20.0F);
         float var10 = this.rectY;
         var6 = this.rectSizeY;
         var1.drawRect(var8 + var7 - var4 - var9, var10 + var6 - var5, this.rectX + this.rectSizeX - var4, var10 + var6 - var4, this.circlePaint);
         var1.drawRect(this.rectX + this.rectSizeX - var5, this.rectY + this.rectSizeY - var4 - (float)AndroidUtilities.dp(20.0F), this.rectX + this.rectSizeX - var4, this.rectY + this.rectSizeY - var4, this.circlePaint);

         for(var3 = 1; var3 < 3; ++var3) {
            var6 = this.rectX;
            var8 = this.rectSizeX;
            var9 = var8 / 3.0F;
            var5 = (float)var3;
            var7 = this.rectY;
            var1.drawRect(var9 * var5 + var6, var7 + var4, var6 + var4 + var8 / 3.0F * var5, var7 + this.rectSizeY - var4, this.circlePaint);
            var7 = this.rectX;
            var8 = this.rectY;
            var6 = this.rectSizeY;
            var1.drawRect(var7 + var4, var6 / 3.0F * var5 + var8, this.rectSizeX + (var7 - var4), var8 + var6 / 3.0F * var5 + var4, this.circlePaint);
         }

      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
         this.viewWidth = var4 - var2 - AndroidUtilities.dp(28.0F);
         this.viewHeight = var5 - var3 - AndroidUtilities.dp(28.0F);
         this.updateBitmapSize();
      }
   }

   public interface PhotoEditActivityDelegate {
      void didFinishEdit(Bitmap var1);
   }
}
