package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.LayoutHelper;

public class PhotoAttachPhotoCell extends FrameLayout {
   private static Rect rect = new Rect();
   private AnimatorSet animatorSet;
   private CheckBox checkBox;
   private FrameLayout checkFrame;
   private PhotoAttachPhotoCell.PhotoAttachPhotoCellDelegate delegate;
   private BackupImageView imageView;
   private boolean isLast;
   private boolean isVertical;
   private boolean needCheckShow;
   private MediaController.PhotoEntry photoEntry;
   private boolean pressed;
   private FrameLayout videoInfoContainer;
   private TextView videoTextView;

   public PhotoAttachPhotoCell(Context var1) {
      super(var1);
      this.imageView = new BackupImageView(var1);
      this.addView(this.imageView, LayoutHelper.createFrame(80, 80.0F));
      this.checkFrame = new FrameLayout(var1);
      this.addView(this.checkFrame, LayoutHelper.createFrame(42, 42.0F, 51, 38.0F, 0.0F, 0.0F, 0.0F));
      this.videoInfoContainer = new FrameLayout(var1);
      this.videoInfoContainer.setBackgroundResource(2131165760);
      this.videoInfoContainer.setPadding(AndroidUtilities.dp(3.0F), 0, AndroidUtilities.dp(3.0F), 0);
      this.addView(this.videoInfoContainer, LayoutHelper.createFrame(80, 16, 83));
      ImageView var2 = new ImageView(var1);
      var2.setImageResource(2131165475);
      this.videoInfoContainer.addView(var2, LayoutHelper.createFrame(-2, -2, 19));
      this.videoTextView = new TextView(var1);
      this.videoTextView.setTextColor(-1);
      this.videoTextView.setTextSize(1, 12.0F);
      this.videoTextView.setImportantForAccessibility(2);
      this.videoInfoContainer.addView(this.videoTextView, LayoutHelper.createFrame(-2, -2.0F, 19, 18.0F, -0.7F, 0.0F, 0.0F));
      this.checkBox = new CheckBox(var1, 2131165355);
      this.checkBox.setSize(30);
      this.checkBox.setCheckOffset(AndroidUtilities.dp(1.0F));
      this.checkBox.setDrawBackground(true);
      this.checkBox.setColor(-12793105, -1);
      this.addView(this.checkBox, LayoutHelper.createFrame(30, 30.0F, 51, 46.0F, 4.0F, 0.0F, 0.0F));
      this.checkBox.setVisibility(0);
      this.setFocusable(true);
   }

   public void callDelegate() {
      this.delegate.onCheckClick(this);
   }

   public CheckBox getCheckBox() {
      return this.checkBox;
   }

   public FrameLayout getCheckFrame() {
      return this.checkFrame;
   }

   public BackupImageView getImageView() {
      return this.imageView;
   }

   public MediaController.PhotoEntry getPhotoEntry() {
      return this.photoEntry;
   }

   public View getVideoInfoContainer() {
      return this.videoInfoContainer;
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setEnabled(true);
      if (this.photoEntry.isVideo) {
         StringBuilder var2 = new StringBuilder();
         var2.append(LocaleController.getString("AttachVideo", 2131558733));
         var2.append(", ");
         var2.append(LocaleController.formatCallDuration(this.photoEntry.duration));
         var1.setText(var2.toString());
      } else {
         var1.setText(LocaleController.getString("AttachPhoto", 2131558727));
      }

      if (this.checkBox.isChecked()) {
         var1.setSelected(true);
      }

      if (VERSION.SDK_INT >= 21) {
         var1.addAction(new AccessibilityAction(2131230727, LocaleController.getString("Open", 2131560110)));
      }

   }

   protected void onMeasure(int var1, int var2) {
      boolean var3 = this.isVertical;
      byte var5 = 0;
      byte var4 = 0;
      if (var3) {
         var2 = MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0F), 1073741824);
         if (!this.isLast) {
            var4 = 6;
         }

         super.onMeasure(var2, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)(var4 + 80)), 1073741824));
      } else {
         if (this.isLast) {
            var4 = var5;
         } else {
            var4 = 6;
         }

         super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)(var4 + 80)), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0F), 1073741824));
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var3;
      label29: {
         this.checkFrame.getHitRect(rect);
         int var2 = var1.getAction();
         var3 = true;
         if (var2 == 0) {
            if (rect.contains((int)var1.getX(), (int)var1.getY())) {
               this.pressed = true;
               this.invalidate();
               break label29;
            }
         } else if (this.pressed) {
            if (var1.getAction() == 1) {
               this.getParent().requestDisallowInterceptTouchEvent(true);
               this.pressed = false;
               this.playSoundEffect(0);
               this.sendAccessibilityEvent(1);
               this.delegate.onCheckClick(this);
               this.invalidate();
            } else if (var1.getAction() == 3) {
               this.pressed = false;
               this.invalidate();
            } else if (var1.getAction() == 2 && !rect.contains((int)var1.getX(), (int)var1.getY())) {
               this.pressed = false;
               this.invalidate();
            }
         }

         var3 = false;
      }

      boolean var4 = var3;
      if (!var3) {
         var4 = super.onTouchEvent(var1);
      }

      return var4;
   }

   public boolean performAccessibilityAction(int var1, Bundle var2) {
      if (var1 == 2131230727) {
         View var3 = (View)this.getParent();
         var3.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, (float)this.getLeft(), (float)(this.getTop() + this.getHeight() - 1), 0));
         var3.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, (float)this.getLeft(), (float)(this.getTop() + this.getHeight() - 1), 0));
      }

      return super.performAccessibilityAction(var1, var2);
   }

   public void setChecked(int var1, boolean var2, boolean var3) {
      this.checkBox.setChecked(var1, var2, var3);
   }

   public void setDelegate(PhotoAttachPhotoCell.PhotoAttachPhotoCellDelegate var1) {
      this.delegate = var1;
   }

   public void setIsVertical(boolean var1) {
      this.isVertical = var1;
   }

   public void setNum(int var1) {
      this.checkBox.setNum(var1);
   }

   public void setOnCheckClickLisnener(OnClickListener var1) {
      this.checkFrame.setOnClickListener(var1);
   }

   public void setPhotoEntry(MediaController.PhotoEntry var1, boolean var2, boolean var3) {
      boolean var4 = false;
      this.pressed = false;
      this.photoEntry = var1;
      this.isLast = var3;
      if (this.photoEntry.isVideo) {
         this.imageView.setOrientation(0, true);
         this.videoInfoContainer.setVisibility(0);
         int var5 = this.photoEntry.duration;
         int var6 = var5 / 60;
         this.videoTextView.setText(String.format("%d:%02d", var6, var5 - var6 * 60));
      } else {
         this.videoInfoContainer.setVisibility(4);
      }

      var1 = this.photoEntry;
      String var7 = var1.thumbPath;
      if (var7 != null) {
         this.imageView.setImage(var7, (String)null, this.getResources().getDrawable(2131165697));
      } else if (var1.path != null) {
         BackupImageView var10;
         StringBuilder var14;
         if (var1.isVideo) {
            var10 = this.imageView;
            var14 = new StringBuilder();
            var14.append("vthumb://");
            var14.append(this.photoEntry.imageId);
            var14.append(":");
            var14.append(this.photoEntry.path);
            var10.setImage(var14.toString(), (String)null, this.getResources().getDrawable(2131165697));
         } else {
            this.imageView.setOrientation(var1.orientation, true);
            var10 = this.imageView;
            var14 = new StringBuilder();
            var14.append("thumb://");
            var14.append(this.photoEntry.imageId);
            var14.append(":");
            var14.append(this.photoEntry.path);
            var10.setImage(var14.toString(), (String)null, this.getResources().getDrawable(2131165697));
         }
      } else {
         this.imageView.setImageResource(2131165697);
      }

      boolean var13 = var4;
      if (var2) {
         var13 = var4;
         if (PhotoViewer.isShowingImage(this.photoEntry.path)) {
            var13 = true;
         }
      }

      this.imageView.getImageReceiver().setVisible(var13 ^ true, true);
      CheckBox var11 = this.checkBox;
      float var8 = 0.0F;
      float var9;
      if (var13) {
         var9 = 0.0F;
      } else {
         var9 = 1.0F;
      }

      var11.setAlpha(var9);
      FrameLayout var12 = this.videoInfoContainer;
      if (var13) {
         var9 = var8;
      } else {
         var9 = 1.0F;
      }

      var12.setAlpha(var9);
      this.requestLayout();
   }

   public void showCheck(boolean var1) {
      float var2 = 1.0F;
      if ((!var1 || this.checkBox.getAlpha() != 1.0F) && (var1 || this.checkBox.getAlpha() != 0.0F)) {
         AnimatorSet var3 = this.animatorSet;
         if (var3 != null) {
            var3.cancel();
            this.animatorSet = null;
         }

         this.animatorSet = new AnimatorSet();
         this.animatorSet.setInterpolator(new DecelerateInterpolator());
         this.animatorSet.setDuration(180L);
         var3 = this.animatorSet;
         FrameLayout var4 = this.videoInfoContainer;
         float var5;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.0F;
         }

         ObjectAnimator var6 = ObjectAnimator.ofFloat(var4, "alpha", new float[]{var5});
         CheckBox var7 = this.checkBox;
         if (var1) {
            var5 = var2;
         } else {
            var5 = 0.0F;
         }

         var3.playTogether(new Animator[]{var6, ObjectAnimator.ofFloat(var7, "alpha", new float[]{var5})});
         this.animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               if (var1.equals(PhotoAttachPhotoCell.this.animatorSet)) {
                  PhotoAttachPhotoCell.this.animatorSet = null;
               }

            }
         });
         this.animatorSet.start();
      }
   }

   public void showImage() {
      this.imageView.getImageReceiver().setVisible(true, true);
   }

   public interface PhotoAttachPhotoCellDelegate {
      void onCheckClick(PhotoAttachPhotoCell var1);
   }
}
