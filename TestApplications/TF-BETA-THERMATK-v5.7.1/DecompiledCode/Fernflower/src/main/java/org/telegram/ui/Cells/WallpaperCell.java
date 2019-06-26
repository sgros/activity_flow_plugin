package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.io.File;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.MediaController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.WallpapersListActivity;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.LayoutHelper;

public class WallpaperCell extends FrameLayout {
   private Paint backgroundPaint;
   private Drawable checkDrawable;
   private Paint circlePaint;
   private int currentType;
   private Paint framePaint;
   private boolean isBottom;
   private boolean isTop;
   private int spanCount = 3;
   private WallpaperCell.WallpaperView[] wallpaperViews = new WallpaperCell.WallpaperView[5];

   public WallpaperCell(Context var1) {
      super(var1);
      int var2 = 0;

      while(true) {
         WallpaperCell.WallpaperView[] var3 = this.wallpaperViews;
         if (var2 >= var3.length) {
            this.framePaint = new Paint();
            this.framePaint.setColor(855638016);
            this.circlePaint = new Paint(1);
            this.checkDrawable = var1.getResources().getDrawable(2131165300).mutate();
            this.backgroundPaint = new Paint();
            this.backgroundPaint.setColor(Theme.getColor("sharedMedia_photoPlaceholder"));
            return;
         }

         WallpaperCell.WallpaperView var4 = new WallpaperCell.WallpaperView(var1);
         var3[var2] = var4;
         this.addView(var4);
         var4.setOnClickListener(new _$$Lambda$WallpaperCell$fnKXvqmCCrXx3zL9kUDMI1LGdcU(this, var4, var2));
         var4.setOnLongClickListener(new _$$Lambda$WallpaperCell$KQeRkjIAB8SBAWriFhep5_TPLbU(this, var4, var2));
         ++var2;
      }
   }

   public void invalidate() {
      super.invalidate();

      for(int var1 = 0; var1 < this.spanCount; ++var1) {
         this.wallpaperViews[var1].invalidate();
      }

   }

   // $FF: synthetic method
   public void lambda$new$0$WallpaperCell(WallpaperCell.WallpaperView var1, int var2, View var3) {
      this.onWallpaperClick(var1.currentWallpaper, var2);
   }

   // $FF: synthetic method
   public boolean lambda$new$1$WallpaperCell(WallpaperCell.WallpaperView var1, int var2, View var3) {
      return this.onWallpaperLongClick(var1.currentWallpaper, var2);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var4 = AndroidUtilities.dp(14.0F);
      var1 = this.isTop;
      var3 = 0;
      if (var1) {
         var2 = AndroidUtilities.dp(14.0F);
      } else {
         var2 = 0;
      }

      while(var3 < this.spanCount) {
         var5 = this.wallpaperViews[var3].getMeasuredWidth();
         WallpaperCell.WallpaperView[] var6 = this.wallpaperViews;
         var6[var3].layout(var4, var2, var4 + var5, var6[var3].getMeasuredHeight() + var2);
         var4 += var5 + AndroidUtilities.dp(6.0F);
         ++var3;
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getSize(var1);
      int var4 = var3 - AndroidUtilities.dp((float)((this.spanCount - 1) * 6 + 28));
      var2 = var4 / this.spanCount;
      int var5;
      if (this.currentType == 0) {
         var5 = AndroidUtilities.dp(180.0F);
      } else {
         var5 = var2;
      }

      boolean var6 = this.isTop;
      float var7 = 14.0F;
      byte var8 = 0;
      if (var6) {
         var1 = AndroidUtilities.dp(14.0F);
      } else {
         var1 = 0;
      }

      if (!this.isBottom) {
         var7 = 6.0F;
      }

      this.setMeasuredDimension(var3, var1 + var5 + AndroidUtilities.dp(var7));
      var1 = var4;
      var4 = var8;

      while(true) {
         int var10 = this.spanCount;
         if (var4 >= var10) {
            return;
         }

         WallpaperCell.WallpaperView var9 = this.wallpaperViews[var4];
         if (var4 == var10 - 1) {
            var10 = var1;
         } else {
            var10 = var2;
         }

         var9.measure(MeasureSpec.makeMeasureSpec(var10, 1073741824), MeasureSpec.makeMeasureSpec(var5, 1073741824));
         var1 -= var2;
         ++var4;
      }
   }

   protected void onWallpaperClick(Object var1, int var2) {
   }

   protected boolean onWallpaperLongClick(Object var1, int var2) {
      return false;
   }

   public void setChecked(int var1, boolean var2, boolean var3) {
      this.wallpaperViews[var1].setChecked(var2, var3);
   }

   public void setParams(int var1, boolean var2, boolean var3) {
      this.spanCount = var1;
      this.isTop = var2;
      this.isBottom = var3;
      int var4 = 0;

      while(true) {
         WallpaperCell.WallpaperView[] var5 = this.wallpaperViews;
         if (var4 >= var5.length) {
            return;
         }

         WallpaperCell.WallpaperView var7 = var5[var4];
         byte var6;
         if (var4 < var1) {
            var6 = 0;
         } else {
            var6 = 8;
         }

         var7.setVisibility(var6);
         this.wallpaperViews[var4].clearAnimation();
         ++var4;
      }
   }

   public void setWallpaper(int var1, int var2, Object var3, long var4, Drawable var6, boolean var7) {
      this.currentType = var1;
      if (var3 == null) {
         this.wallpaperViews[var2].setVisibility(8);
         this.wallpaperViews[var2].clearAnimation();
      } else {
         this.wallpaperViews[var2].setVisibility(0);
         this.wallpaperViews[var2].setWallpaper(var3, var4, var6, var7);
      }

   }

   private class WallpaperView extends FrameLayout {
      private AnimatorSet animator;
      private AnimatorSet animatorSet;
      private CheckBox checkBox;
      private Object currentWallpaper;
      private BackupImageView imageView;
      private ImageView imageView2;
      private boolean isSelected;
      private View selector;

      public WallpaperView(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
         this.imageView = new BackupImageView(var2) {
            protected void onDraw(Canvas var1) {
               super.onDraw(var1);
               if (WallpaperView.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                  var1.drawLine(1.0F, 0.0F, (float)(this.getMeasuredWidth() - 1), 0.0F, WallpaperCell.this.framePaint);
                  var1.drawLine(0.0F, 0.0F, 0.0F, (float)this.getMeasuredHeight(), WallpaperCell.this.framePaint);
                  var1.drawLine((float)(this.getMeasuredWidth() - 1), 0.0F, (float)(this.getMeasuredWidth() - 1), (float)this.getMeasuredHeight(), WallpaperCell.this.framePaint);
                  var1.drawLine(1.0F, (float)(this.getMeasuredHeight() - 1), (float)(this.getMeasuredWidth() - 1), (float)(this.getMeasuredHeight() - 1), WallpaperCell.this.framePaint);
               }

               if (WallpaperView.this.isSelected) {
                  WallpaperCell.this.circlePaint.setColor(Theme.serviceMessageColorBackup);
                  int var2 = this.getMeasuredWidth() / 2;
                  int var3 = this.getMeasuredHeight() / 2;
                  var1.drawCircle((float)var2, (float)var3, (float)AndroidUtilities.dp(20.0F), WallpaperCell.this.circlePaint);
                  WallpaperCell.this.checkDrawable.setBounds(var2 - WallpaperCell.this.checkDrawable.getIntrinsicWidth() / 2, var3 - WallpaperCell.this.checkDrawable.getIntrinsicHeight() / 2, var2 + WallpaperCell.this.checkDrawable.getIntrinsicWidth() / 2, var3 + WallpaperCell.this.checkDrawable.getIntrinsicHeight() / 2);
                  WallpaperCell.this.checkDrawable.draw(var1);
               }

            }
         };
         this.addView(this.imageView, LayoutHelper.createFrame(-1, -1, 51));
         this.imageView2 = new ImageView(var2);
         this.imageView2.setImageResource(2131165443);
         this.imageView2.setScaleType(ScaleType.CENTER);
         this.addView(this.imageView2, LayoutHelper.createFrame(-1, -1, 51));
         this.selector = new View(var2);
         this.selector.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         this.addView(this.selector, LayoutHelper.createFrame(-1, -1.0F));
         this.checkBox = new CheckBox(var2, 2131165802);
         this.checkBox.setVisibility(4);
         this.checkBox.setColor(Theme.getColor("checkbox"), Theme.getColor("checkboxCheck"));
         this.addView(this.checkBox, LayoutHelper.createFrame(22, 22.0F, 53, 0.0F, 2.0F, 2.0F, 0.0F));
      }

      public void clearAnimation() {
         super.clearAnimation();
         AnimatorSet var1 = this.animator;
         if (var1 != null) {
            var1.cancel();
            this.animator = null;
         }

      }

      public void invalidate() {
         super.invalidate();
         this.imageView.invalidate();
      }

      protected void onDraw(Canvas var1) {
         if (this.checkBox.isChecked() || !this.imageView.getImageReceiver().hasBitmapImage() || this.imageView.getImageReceiver().getCurrentAlpha() != 1.0F) {
            var1.drawRect(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), WallpaperCell.this.backgroundPaint);
         }

      }

      public boolean onTouchEvent(MotionEvent var1) {
         if (VERSION.SDK_INT >= 21) {
            this.selector.drawableHotspotChanged(var1.getX(), var1.getY());
         }

         return super.onTouchEvent(var1);
      }

      public void setChecked(final boolean var1, boolean var2) {
         if (this.checkBox.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
         }

         this.checkBox.setChecked(var1, var2);
         AnimatorSet var3 = this.animator;
         if (var3 != null) {
            var3.cancel();
            this.animator = null;
         }

         float var4 = 0.8875F;
         float var6;
         if (var2) {
            this.animator = new AnimatorSet();
            var3 = this.animator;
            BackupImageView var5 = this.imageView;
            if (var1) {
               var6 = 0.8875F;
            } else {
               var6 = 1.0F;
            }

            ObjectAnimator var7 = ObjectAnimator.ofFloat(var5, "scaleX", new float[]{var6});
            var5 = this.imageView;
            if (!var1) {
               var4 = 1.0F;
            }

            var3.playTogether(new Animator[]{var7, ObjectAnimator.ofFloat(var5, "scaleY", new float[]{var4})});
            this.animator.setDuration(200L);
            this.animator.addListener(new AnimatorListenerAdapter() {
               public void onAnimationCancel(Animator var1x) {
                  if (WallpaperView.this.animator != null && WallpaperView.this.animator.equals(var1x)) {
                     WallpaperView.this.animator = null;
                  }

               }

               public void onAnimationEnd(Animator var1x) {
                  if (WallpaperView.this.animator != null && WallpaperView.this.animator.equals(var1x)) {
                     WallpaperView.this.animator = null;
                     if (!var1) {
                        WallpaperView.this.setBackgroundColor(0);
                     }
                  }

               }
            });
            this.animator.start();
         } else {
            BackupImageView var8 = this.imageView;
            if (var1) {
               var6 = 0.8875F;
            } else {
               var6 = 1.0F;
            }

            var8.setScaleX(var6);
            var8 = this.imageView;
            if (!var1) {
               var4 = 1.0F;
            }

            var8.setScaleY(var4);
         }

         this.invalidate();
      }

      public void setWallpaper(Object var1, long var2, Drawable var4, boolean var5) {
         this.currentWallpaper = var1;
         boolean var6 = false;
         boolean var7 = false;
         boolean var8 = false;
         int var9;
         if (var1 == null) {
            this.imageView.setVisibility(4);
            this.imageView2.setVisibility(0);
            if (var5) {
               this.imageView2.setImageDrawable(var4);
               this.imageView2.setScaleType(ScaleType.CENTER_CROP);
            } else {
               ImageView var12 = this.imageView2;
               if (var2 != -1L && var2 != 1000001L) {
                  var9 = 1509949440;
               } else {
                  var9 = 1514625126;
               }

               var12.setBackgroundColor(var9);
               this.imageView2.setScaleType(ScaleType.CENTER);
               this.imageView2.setImageResource(2131165443);
            }
         } else {
            this.imageView.setVisibility(0);
            this.imageView2.setVisibility(4);
            BackupImageView var10 = this.imageView;
            var4 = null;
            var10.setBackgroundDrawable((Drawable)null);
            this.imageView.getImageReceiver().setColorFilter((ColorFilter)null);
            this.imageView.getImageReceiver().setAlpha(1.0F);
            TLRPC.PhotoSize var11;
            TLRPC.PhotoSize var13;
            if (var1 instanceof TLRPC.TL_wallPaper) {
               TLRPC.TL_wallPaper var20 = (TLRPC.TL_wallPaper)var1;
               var5 = var8;
               if (var20.id == var2) {
                  var5 = true;
               }

               this.isSelected = var5;
               var11 = FileLoader.getClosestPhotoSizeWithSize(var20.document.thumbs, 100);
               TLRPC.PhotoSize var17 = FileLoader.getClosestPhotoSizeWithSize(var20.document.thumbs, 320);
               var13 = var17;
               if (var17 == var11) {
                  var13 = null;
               }

               if (var13 != null) {
                  var9 = var13.size;
               } else {
                  var9 = var20.document.size;
               }

               if (var20.pattern) {
                  this.imageView.setBackgroundColor(var20.settings.background_color | -16777216);
                  this.imageView.setImage(ImageLocation.getForDocument(var13, var20.document), "100_100", ImageLocation.getForDocument(var11, var20.document), (String)null, "jpg", var9, 1, var20);
                  this.imageView.getImageReceiver().setColorFilter(new PorterDuffColorFilter(AndroidUtilities.getPatternColor(var20.settings.background_color), Mode.SRC_IN));
                  this.imageView.getImageReceiver().setAlpha((float)var20.settings.intensity / 100.0F);
               } else if (var13 != null) {
                  this.imageView.setImage(ImageLocation.getForDocument(var13, var20.document), "100_100", ImageLocation.getForDocument(var11, var20.document), "100_100_b", "jpg", var9, 1, var20);
               } else {
                  this.imageView.setImage(ImageLocation.getForDocument(var20.document), "100_100", ImageLocation.getForDocument(var11, var20.document), "100_100_b", "jpg", var9, 1, var20);
               }
            } else if (var1 instanceof WallpapersListActivity.ColorWallpaper) {
               WallpapersListActivity.ColorWallpaper var18 = (WallpapersListActivity.ColorWallpaper)var1;
               File var14 = var18.path;
               if (var14 != null) {
                  this.imageView.setImage(var14.getAbsolutePath(), "100_100", (Drawable)null);
               } else {
                  this.imageView.setImageBitmap((Bitmap)null);
                  this.imageView.setBackgroundColor(var18.color | -16777216);
               }

               var5 = var6;
               if (var18.id == var2) {
                  var5 = true;
               }

               this.isSelected = var5;
            } else if (var1 instanceof WallpapersListActivity.FileWallpaper) {
               WallpapersListActivity.FileWallpaper var15 = (WallpapersListActivity.FileWallpaper)var1;
               var5 = var7;
               if (var15.id == var2) {
                  var5 = true;
               }

               this.isSelected = var5;
               File var19 = var15.originalPath;
               if (var19 != null) {
                  this.imageView.setImage(var19.getAbsolutePath(), "100_100", (Drawable)null);
               } else {
                  var19 = var15.path;
                  if (var19 != null) {
                     this.imageView.setImage(var19.getAbsolutePath(), "100_100", (Drawable)null);
                  } else if ((long)var15.resId == -2L) {
                     this.imageView.setImageDrawable(Theme.getThemedWallpaper(true));
                  } else {
                     this.imageView.setImageResource(var15.thumbResId);
                  }
               }
            } else if (var1 instanceof MediaController.SearchImage) {
               MediaController.SearchImage var21 = (MediaController.SearchImage)var1;
               TLRPC.Photo var16 = var21.photo;
               if (var16 != null) {
                  var11 = FileLoader.getClosestPhotoSizeWithSize(var16.sizes, 100);
                  var13 = FileLoader.getClosestPhotoSizeWithSize(var21.photo.sizes, 320);
                  if (var13 == var11) {
                     var13 = var4;
                  }

                  if (var13 != null) {
                     var9 = var13.size;
                  } else {
                     var9 = 0;
                  }

                  this.imageView.setImage(ImageLocation.getForPhoto(var13, var21.photo), "100_100", ImageLocation.getForPhoto(var11, var21.photo), "100_100_b", "jpg", var9, 1, var21);
               } else {
                  this.imageView.setImage(var21.thumbUrl, "100_100", (Drawable)null);
               }
            } else {
               this.isSelected = false;
            }
         }

      }
   }
}
