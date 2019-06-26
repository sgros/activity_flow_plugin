package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.LayoutHelper;

public class PhotoPickerPhotoCell extends FrameLayout {
   private AnimatorSet animator;
   private AnimatorSet animatorSet;
   public CheckBox checkBox;
   public FrameLayout checkFrame;
   public int itemWidth;
   public BackupImageView photoImage;
   public FrameLayout videoInfoContainer;
   public TextView videoTextView;
   private boolean zoomOnSelect;

   public PhotoPickerPhotoCell(Context var1, boolean var2) {
      super(var1);
      this.zoomOnSelect = var2;
      this.photoImage = new BackupImageView(var1);
      this.addView(this.photoImage, LayoutHelper.createFrame(-1, -1.0F));
      this.checkFrame = new FrameLayout(var1);
      this.addView(this.checkFrame, LayoutHelper.createFrame(42, 42, 53));
      this.videoInfoContainer = new FrameLayout(var1);
      this.videoInfoContainer.setBackgroundResource(2131165760);
      this.videoInfoContainer.setPadding(AndroidUtilities.dp(3.0F), 0, AndroidUtilities.dp(3.0F), 0);
      this.addView(this.videoInfoContainer, LayoutHelper.createFrame(-1, 16, 83));
      ImageView var3 = new ImageView(var1);
      var3.setImageResource(2131165475);
      this.videoInfoContainer.addView(var3, LayoutHelper.createFrame(-2, -2, 19));
      this.videoTextView = new TextView(var1);
      this.videoTextView.setTextColor(-1);
      this.videoTextView.setTextSize(1, 12.0F);
      this.videoTextView.setImportantForAccessibility(2);
      this.videoInfoContainer.addView(this.videoTextView, LayoutHelper.createFrame(-2, -2.0F, 19, 18.0F, -0.7F, 0.0F, 0.0F));
      this.checkBox = new CheckBox(var1, 2131165355);
      CheckBox var6 = this.checkBox;
      byte var4;
      if (var2) {
         var4 = 30;
      } else {
         var4 = 26;
      }

      var6.setSize(var4);
      this.checkBox.setCheckOffset(AndroidUtilities.dp(1.0F));
      this.checkBox.setDrawBackground(true);
      this.checkBox.setColor(-10043398, -1);
      var6 = this.checkBox;
      if (var2) {
         var4 = 30;
      } else {
         var4 = 26;
      }

      float var5;
      if (var2) {
         var5 = 30.0F;
      } else {
         var5 = 26.0F;
      }

      this.addView(var6, LayoutHelper.createFrame(var4, var5, 53, 0.0F, 4.0F, 4.0F, 0.0F));
      this.setFocusable(true);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(this.itemWidth, 1073741824), MeasureSpec.makeMeasureSpec(this.itemWidth, 1073741824));
   }

   public void setChecked(int var1, final boolean var2, boolean var3) {
      this.checkBox.setChecked(var1, var2, var3);
      AnimatorSet var4 = this.animator;
      if (var4 != null) {
         var4.cancel();
         this.animator = null;
      }

      if (this.zoomOnSelect) {
         var1 = -16119286;
         float var5 = 0.85F;
         float var7;
         if (var3) {
            if (var2) {
               this.setBackgroundColor(-16119286);
            }

            this.animator = new AnimatorSet();
            var4 = this.animator;
            BackupImageView var6 = this.photoImage;
            if (var2) {
               var7 = 0.85F;
            } else {
               var7 = 1.0F;
            }

            ObjectAnimator var10 = ObjectAnimator.ofFloat(var6, "scaleX", new float[]{var7});
            BackupImageView var8 = this.photoImage;
            if (!var2) {
               var5 = 1.0F;
            }

            var4.playTogether(new Animator[]{var10, ObjectAnimator.ofFloat(var8, "scaleY", new float[]{var5})});
            this.animator.setDuration(200L);
            this.animator.addListener(new AnimatorListenerAdapter() {
               public void onAnimationCancel(Animator var1) {
                  if (PhotoPickerPhotoCell.this.animator != null && PhotoPickerPhotoCell.this.animator.equals(var1)) {
                     PhotoPickerPhotoCell.this.animator = null;
                  }

               }

               public void onAnimationEnd(Animator var1) {
                  if (PhotoPickerPhotoCell.this.animator != null && PhotoPickerPhotoCell.this.animator.equals(var1)) {
                     PhotoPickerPhotoCell.this.animator = null;
                     if (!var2) {
                        PhotoPickerPhotoCell.this.setBackgroundColor(0);
                     }
                  }

               }
            });
            this.animator.start();
         } else {
            if (!var2) {
               var1 = 0;
            }

            this.setBackgroundColor(var1);
            BackupImageView var9 = this.photoImage;
            if (var2) {
               var7 = 0.85F;
            } else {
               var7 = 1.0F;
            }

            var9.setScaleX(var7);
            var9 = this.photoImage;
            if (!var2) {
               var5 = 1.0F;
            }

            var9.setScaleY(var5);
         }
      }

   }

   public void setImage(MediaController.SearchImage var1) {
      Drawable var2 = this.getResources().getDrawable(2131165697);
      TLRPC.PhotoSize var3 = var1.thumbPhotoSize;
      if (var3 != null) {
         this.photoImage.setImage((ImageLocation)ImageLocation.getForPhoto(var3, var1.photo), (String)null, (Drawable)var2, (Object)var1);
      } else {
         var3 = var1.photoSize;
         if (var3 != null) {
            this.photoImage.setImage((ImageLocation)ImageLocation.getForPhoto(var3, var1.photo), "80_80", (Drawable)var2, (Object)var1);
         } else {
            String var4 = var1.thumbPath;
            if (var4 != null) {
               this.photoImage.setImage(var4, (String)null, var2);
            } else {
               var4 = var1.thumbUrl;
               if (var4 != null && var4.length() > 0) {
                  this.photoImage.setImage(var1.thumbUrl, (String)null, var2);
               } else if (MessageObject.isDocumentHasThumb(var1.document)) {
                  var3 = FileLoader.getClosestPhotoSizeWithSize(var1.document.thumbs, 320);
                  this.photoImage.setImage((ImageLocation)ImageLocation.getForDocument(var3, var1.document), (String)null, (Drawable)var2, (Object)var1);
               } else {
                  this.photoImage.setImageDrawable(var2);
               }
            }
         }
      }

   }

   public void setNum(int var1) {
      this.checkBox.setNum(var1);
   }

   public void showCheck(boolean var1) {
      AnimatorSet var2 = this.animatorSet;
      if (var2 != null) {
         var2.cancel();
         this.animatorSet = null;
      }

      this.animatorSet = new AnimatorSet();
      this.animatorSet.setInterpolator(new DecelerateInterpolator());
      this.animatorSet.setDuration(180L);
      var2 = this.animatorSet;
      FrameLayout var3 = this.videoInfoContainer;
      float var4 = 1.0F;
      float var5;
      if (var1) {
         var5 = 1.0F;
      } else {
         var5 = 0.0F;
      }

      ObjectAnimator var7 = ObjectAnimator.ofFloat(var3, "alpha", new float[]{var5});
      CheckBox var6 = this.checkBox;
      if (var1) {
         var5 = var4;
      } else {
         var5 = 0.0F;
      }

      var2.playTogether(new Animator[]{var7, ObjectAnimator.ofFloat(var6, "alpha", new float[]{var5})});
      this.animatorSet.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            if (var1.equals(PhotoPickerPhotoCell.this.animatorSet)) {
               PhotoPickerPhotoCell.this.animatorSet = null;
            }

         }
      });
      this.animatorSet.start();
   }
}
