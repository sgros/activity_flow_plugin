package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class FeaturedStickerSetCell extends FrameLayout {
   private TextView addButton;
   private int angle;
   private ImageView checkImage;
   private int currentAccount;
   private AnimatorSet currentAnimation;
   private boolean drawProgress;
   private BackupImageView imageView;
   private boolean isInstalled;
   private long lastUpdateTime;
   private boolean needDivider;
   private float progressAlpha;
   private Paint progressPaint;
   private RectF progressRect;
   private Rect rect = new Rect();
   private TLRPC.StickerSetCovered stickersSet;
   private TextView textView;
   private TextView valueTextView;
   private boolean wasLayout;

   public FeaturedStickerSetCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.progressRect = new RectF();
      this.progressPaint = new Paint(1);
      this.progressPaint.setColor(Theme.getColor("featuredStickers_buttonProgress"));
      this.progressPaint.setStrokeCap(Cap.ROUND);
      this.progressPaint.setStyle(Style.STROKE);
      this.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setEllipsize(TruncateAt.END);
      TextView var2 = this.textView;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var3) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var2.setGravity(var5);
      var2 = this.textView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      float var6;
      if (LocaleController.isRTL) {
         var6 = 22.0F;
      } else {
         var6 = 71.0F;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 71.0F;
      } else {
         var7 = 22.0F;
      }

      this.addView(var2, LayoutHelper.createFrame(-2, -2.0F, var5, var6, 10.0F, var7, 0.0F));
      this.valueTextView = new TextView(var1);
      this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.valueTextView.setTextSize(1, 13.0F);
      this.valueTextView.setLines(1);
      this.valueTextView.setMaxLines(1);
      this.valueTextView.setSingleLine(true);
      this.valueTextView.setEllipsize(TruncateAt.END);
      var2 = this.valueTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var2.setGravity(var5);
      var2 = this.valueTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 100.0F;
      } else {
         var6 = 71.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 71.0F;
      } else {
         var7 = 100.0F;
      }

      this.addView(var2, LayoutHelper.createFrame(-2, -2.0F, var5, var6, 35.0F, var7, 0.0F));
      this.imageView = new BackupImageView(var1);
      this.imageView.setAspectFit(true);
      BackupImageView var8 = this.imageView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 0.0F;
      } else {
         var6 = 12.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 12.0F;
      } else {
         var7 = 0.0F;
      }

      this.addView(var8, LayoutHelper.createFrame(48, 48.0F, var5 | 48, var6, 8.0F, var7, 0.0F));
      this.addButton = new TextView(var1) {
         protected void onDraw(Canvas var1) {
            super.onDraw(var1);
            if (FeaturedStickerSetCell.this.drawProgress || !FeaturedStickerSetCell.this.drawProgress && FeaturedStickerSetCell.this.progressAlpha != 0.0F) {
               FeaturedStickerSetCell.this.progressPaint.setAlpha(Math.min(255, (int)(FeaturedStickerSetCell.this.progressAlpha * 255.0F)));
               int var2 = this.getMeasuredWidth() - AndroidUtilities.dp(11.0F);
               FeaturedStickerSetCell.this.progressRect.set((float)var2, (float)AndroidUtilities.dp(3.0F), (float)(var2 + AndroidUtilities.dp(8.0F)), (float)AndroidUtilities.dp(11.0F));
               var1.drawArc(FeaturedStickerSetCell.this.progressRect, (float)FeaturedStickerSetCell.this.angle, 220.0F, false, FeaturedStickerSetCell.this.progressPaint);
               this.invalidate((int)FeaturedStickerSetCell.this.progressRect.left - AndroidUtilities.dp(2.0F), (int)FeaturedStickerSetCell.this.progressRect.top - AndroidUtilities.dp(2.0F), (int)FeaturedStickerSetCell.this.progressRect.right + AndroidUtilities.dp(2.0F), (int)FeaturedStickerSetCell.this.progressRect.bottom + AndroidUtilities.dp(2.0F));
               long var3 = System.currentTimeMillis();
               if (Math.abs(FeaturedStickerSetCell.this.lastUpdateTime - System.currentTimeMillis()) < 1000L) {
                  long var5 = var3 - FeaturedStickerSetCell.this.lastUpdateTime;
                  float var7 = (float)(360L * var5) / 2000.0F;
                  FeaturedStickerSetCell var8 = FeaturedStickerSetCell.this;
                  var8.angle = (int)((float)var8.angle + var7);
                  var8 = FeaturedStickerSetCell.this;
                  var8.angle = var8.angle - FeaturedStickerSetCell.this.angle / 360 * 360;
                  if (FeaturedStickerSetCell.this.drawProgress) {
                     if (FeaturedStickerSetCell.this.progressAlpha < 1.0F) {
                        var8 = FeaturedStickerSetCell.this;
                        var8.progressAlpha = var8.progressAlpha + (float)var5 / 200.0F;
                        if (FeaturedStickerSetCell.this.progressAlpha > 1.0F) {
                           FeaturedStickerSetCell.this.progressAlpha = 1.0F;
                        }
                     }
                  } else if (FeaturedStickerSetCell.this.progressAlpha > 0.0F) {
                     var8 = FeaturedStickerSetCell.this;
                     var8.progressAlpha = var8.progressAlpha - (float)var5 / 200.0F;
                     if (FeaturedStickerSetCell.this.progressAlpha < 0.0F) {
                        FeaturedStickerSetCell.this.progressAlpha = 0.0F;
                     }
                  }
               }

               FeaturedStickerSetCell.this.lastUpdateTime = var3;
               this.invalidate();
            }

         }
      };
      this.addButton.setGravity(17);
      this.addButton.setTextColor(Theme.getColor("featuredStickers_buttonText"));
      this.addButton.setTextSize(1, 14.0F);
      this.addButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.addButton.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0F), Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed")));
      this.addButton.setText(LocaleController.getString("Add", 2131558555).toUpperCase());
      this.addButton.setPadding(AndroidUtilities.dp(17.0F), 0, AndroidUtilities.dp(17.0F), 0);
      var2 = this.addButton;
      var5 = var4;
      if (LocaleController.isRTL) {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 14.0F;
      } else {
         var6 = 0.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 0.0F;
      } else {
         var7 = 14.0F;
      }

      this.addView(var2, LayoutHelper.createFrame(-2, 28.0F, var5 | 48, var6, 18.0F, var7, 0.0F));
      this.checkImage = new ImageView(var1);
      this.checkImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), Mode.MULTIPLY));
      this.checkImage.setImageResource(2131165858);
      this.addView(this.checkImage, LayoutHelper.createFrame(19, 14.0F));
   }

   public TLRPC.StickerSetCovered getStickerSet() {
      return this.stickersSet;
   }

   public boolean isInstalled() {
      return this.isInstalled;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.wasLayout = false;
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
         var1.drawLine(0.0F, (float)(this.getHeight() - 1), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - 1), Theme.dividerPaint);
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      var3 = this.addButton.getLeft() + this.addButton.getMeasuredWidth() / 2 - this.checkImage.getMeasuredWidth() / 2;
      var2 = this.addButton.getTop() + this.addButton.getMeasuredHeight() / 2 - this.checkImage.getMeasuredHeight() / 2;
      ImageView var6 = this.checkImage;
      var6.layout(var3, var2, var6.getMeasuredWidth() + var3, this.checkImage.getMeasuredHeight() + var2);
      this.wasLayout = true;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0F) + this.needDivider, 1073741824));
      this.measureChildWithMargins(this.textView, var1, this.addButton.getMeasuredWidth(), var2, 0);
   }

   public void setAddOnClickListener(OnClickListener var1) {
      this.addButton.setOnClickListener(var1);
   }

   public void setDrawProgress(boolean var1) {
      this.drawProgress = var1;
      this.lastUpdateTime = System.currentTimeMillis();
      this.addButton.invalidate();
   }

   public void setStickersSet(TLRPC.StickerSetCovered var1, boolean var2, boolean var3) {
      boolean var4;
      if (var1 == this.stickersSet && this.wasLayout) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.needDivider = var2;
      this.stickersSet = var1;
      this.lastUpdateTime = System.currentTimeMillis();
      this.setWillNotDraw(this.needDivider ^ true);
      AnimatorSet var5 = this.currentAnimation;
      Object var6 = null;
      if (var5 != null) {
         var5.cancel();
         this.currentAnimation = null;
      }

      this.textView.setText(this.stickersSet.set.title);
      if (var3) {
         Drawable var7 = new Drawable() {
            Paint paint = new Paint(1);

            public void draw(Canvas var1) {
               this.paint.setColor(-12277526);
               var1.drawCircle((float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(5.0F), (float)AndroidUtilities.dp(3.0F), this.paint);
            }

            public int getIntrinsicHeight() {
               return AndroidUtilities.dp(8.0F);
            }

            public int getIntrinsicWidth() {
               return AndroidUtilities.dp(12.0F);
            }

            public int getOpacity() {
               return -2;
            }

            public void setAlpha(int var1) {
            }

            public void setColorFilter(ColorFilter var1) {
            }
         };
         TextView var8 = this.textView;
         Drawable var9;
         if (LocaleController.isRTL) {
            var9 = null;
         } else {
            var9 = var7;
         }

         if (!LocaleController.isRTL) {
            var7 = null;
         }

         var8.setCompoundDrawablesWithIntrinsicBounds(var9, (Drawable)null, var7, (Drawable)null);
      } else {
         this.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
      }

      this.valueTextView.setText(LocaleController.formatPluralString("Stickers", var1.set.count));
      TLRPC.Document var12 = var1.cover;
      TLRPC.PhotoSize var10 = (TLRPC.PhotoSize)var6;
      if (var12 != null) {
         var10 = FileLoader.getClosestPhotoSizeWithSize(var12.thumbs, 90);
      }

      if (var10 != null && var10.location != null) {
         this.imageView.setImage(ImageLocation.getForDocument(var10, var1.cover), (String)null, "webp", (Drawable)null, var1);
      } else if (!var1.covers.isEmpty()) {
         TLRPC.Document var11 = (TLRPC.Document)var1.covers.get(0);
         TLRPC.PhotoSize var13 = FileLoader.getClosestPhotoSizeWithSize(var11.thumbs, 90);
         this.imageView.setImage(ImageLocation.getForDocument(var13, var11), (String)null, "webp", (Drawable)null, var1);
      } else {
         this.imageView.setImage((ImageLocation)null, (String)null, "webp", (Drawable)null, var1);
      }

      if (var4) {
         var3 = this.isInstalled;
         var2 = DataQuery.getInstance(this.currentAccount).isStickerPackInstalled(var1.set.id);
         this.isInstalled = var2;
         if (var2) {
            if (!var3) {
               this.checkImage.setVisibility(0);
               this.addButton.setClickable(false);
               this.currentAnimation = new AnimatorSet();
               this.currentAnimation.setDuration(200L);
               this.currentAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.addButton, "alpha", new float[]{1.0F, 0.0F}), ObjectAnimator.ofFloat(this.addButton, "scaleX", new float[]{1.0F, 0.01F}), ObjectAnimator.ofFloat(this.addButton, "scaleY", new float[]{1.0F, 0.01F}), ObjectAnimator.ofFloat(this.checkImage, "alpha", new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.checkImage, "scaleX", new float[]{0.01F, 1.0F}), ObjectAnimator.ofFloat(this.checkImage, "scaleY", new float[]{0.01F, 1.0F})});
               this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationCancel(Animator var1) {
                     if (FeaturedStickerSetCell.this.currentAnimation != null && FeaturedStickerSetCell.this.currentAnimation.equals(var1)) {
                        FeaturedStickerSetCell.this.currentAnimation = null;
                     }

                  }

                  public void onAnimationEnd(Animator var1) {
                     if (FeaturedStickerSetCell.this.currentAnimation != null && FeaturedStickerSetCell.this.currentAnimation.equals(var1)) {
                        FeaturedStickerSetCell.this.addButton.setVisibility(4);
                     }

                  }
               });
               this.currentAnimation.start();
            }
         } else if (var3) {
            this.addButton.setVisibility(0);
            this.addButton.setClickable(true);
            this.currentAnimation = new AnimatorSet();
            this.currentAnimation.setDuration(200L);
            this.currentAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.checkImage, "alpha", new float[]{1.0F, 0.0F}), ObjectAnimator.ofFloat(this.checkImage, "scaleX", new float[]{1.0F, 0.01F}), ObjectAnimator.ofFloat(this.checkImage, "scaleY", new float[]{1.0F, 0.01F}), ObjectAnimator.ofFloat(this.addButton, "alpha", new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.addButton, "scaleX", new float[]{0.01F, 1.0F}), ObjectAnimator.ofFloat(this.addButton, "scaleY", new float[]{0.01F, 1.0F})});
            this.currentAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationCancel(Animator var1) {
                  if (FeaturedStickerSetCell.this.currentAnimation != null && FeaturedStickerSetCell.this.currentAnimation.equals(var1)) {
                     FeaturedStickerSetCell.this.currentAnimation = null;
                  }

               }

               public void onAnimationEnd(Animator var1) {
                  if (FeaturedStickerSetCell.this.currentAnimation != null && FeaturedStickerSetCell.this.currentAnimation.equals(var1)) {
                     FeaturedStickerSetCell.this.checkImage.setVisibility(4);
                  }

               }
            });
            this.currentAnimation.start();
         }
      } else {
         var2 = DataQuery.getInstance(this.currentAccount).isStickerPackInstalled(var1.set.id);
         this.isInstalled = var2;
         if (var2) {
            this.addButton.setVisibility(4);
            this.addButton.setClickable(false);
            this.checkImage.setVisibility(0);
            this.checkImage.setScaleX(1.0F);
            this.checkImage.setScaleY(1.0F);
            this.checkImage.setAlpha(1.0F);
         } else {
            this.addButton.setVisibility(0);
            this.addButton.setClickable(true);
            this.checkImage.setVisibility(4);
            this.addButton.setScaleX(1.0F);
            this.addButton.setScaleY(1.0F);
            this.addButton.setAlpha(1.0F);
         }
      }

   }
}
