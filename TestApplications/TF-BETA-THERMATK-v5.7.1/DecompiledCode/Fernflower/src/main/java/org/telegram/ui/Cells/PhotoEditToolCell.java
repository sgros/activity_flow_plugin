package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PhotoEditorSeekBar;

public class PhotoEditToolCell extends FrameLayout {
   private Runnable hideValueRunnable = new Runnable() {
      public void run() {
         PhotoEditToolCell.this.valueTextView.setTag((Object)null);
         PhotoEditToolCell.this.valueAnimation = new AnimatorSet();
         PhotoEditToolCell.this.valueAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(PhotoEditToolCell.this.valueTextView, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(PhotoEditToolCell.this.nameTextView, "alpha", new float[]{1.0F})});
         PhotoEditToolCell.this.valueAnimation.setDuration(180L);
         PhotoEditToolCell.this.valueAnimation.setInterpolator(new DecelerateInterpolator());
         PhotoEditToolCell.this.valueAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               if (var1.equals(PhotoEditToolCell.this.valueAnimation)) {
                  PhotoEditToolCell.this.valueAnimation = null;
               }

            }
         });
         PhotoEditToolCell.this.valueAnimation.start();
      }
   };
   private TextView nameTextView;
   private PhotoEditorSeekBar seekBar;
   private AnimatorSet valueAnimation;
   private TextView valueTextView;

   public PhotoEditToolCell(Context var1) {
      super(var1);
      this.nameTextView = new TextView(var1);
      this.nameTextView.setGravity(5);
      this.nameTextView.setTextColor(-1);
      this.nameTextView.setTextSize(1, 12.0F);
      this.nameTextView.setMaxLines(1);
      this.nameTextView.setSingleLine(true);
      this.nameTextView.setEllipsize(TruncateAt.END);
      this.addView(this.nameTextView, LayoutHelper.createFrame(80, -2.0F, 19, 0.0F, 0.0F, 0.0F, 0.0F));
      this.valueTextView = new TextView(var1);
      this.valueTextView.setTextColor(-9649153);
      this.valueTextView.setTextSize(1, 12.0F);
      this.valueTextView.setGravity(5);
      this.valueTextView.setSingleLine(true);
      this.addView(this.valueTextView, LayoutHelper.createFrame(80, -2.0F, 19, 0.0F, 0.0F, 0.0F, 0.0F));
      this.seekBar = new PhotoEditorSeekBar(var1);
      this.addView(this.seekBar, LayoutHelper.createFrame(-1, 40.0F, 19, 96.0F, 0.0F, 24.0F, 0.0F));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0F), 1073741824));
   }

   public void setIconAndTextAndValue(String var1, float var2, int var3, int var4) {
      AnimatorSet var5 = this.valueAnimation;
      if (var5 != null) {
         var5.cancel();
         this.valueAnimation = null;
      }

      AndroidUtilities.cancelRunOnUIThread(this.hideValueRunnable);
      this.valueTextView.setTag((Object)null);
      TextView var6 = this.nameTextView;
      StringBuilder var8 = new StringBuilder();
      var8.append(var1.substring(0, 1).toUpperCase());
      var8.append(var1.substring(1).toLowerCase());
      var6.setText(var8.toString());
      TextView var7;
      if (var2 > 0.0F) {
         var7 = this.valueTextView;
         var8 = new StringBuilder();
         var8.append("+");
         var8.append((int)var2);
         var7.setText(var8.toString());
      } else {
         var7 = this.valueTextView;
         var8 = new StringBuilder();
         var8.append("");
         var8.append((int)var2);
         var7.setText(var8.toString());
      }

      this.valueTextView.setAlpha(0.0F);
      this.nameTextView.setAlpha(1.0F);
      this.seekBar.setMinMax(var3, var4);
      this.seekBar.setProgress((int)var2, false);
   }

   public void setSeekBarDelegate(final PhotoEditorSeekBar.PhotoEditorSeekBarDelegate var1) {
      this.seekBar.setDelegate(new PhotoEditorSeekBar.PhotoEditorSeekBarDelegate() {
         public void onProgressChanged(int var1x, int var2) {
            var1.onProgressChanged(var1x, var2);
            if (var2 > 0) {
               TextView var3 = PhotoEditToolCell.this.valueTextView;
               StringBuilder var4 = new StringBuilder();
               var4.append("+");
               var4.append(var2);
               var3.setText(var4.toString());
            } else {
               TextView var6 = PhotoEditToolCell.this.valueTextView;
               StringBuilder var5 = new StringBuilder();
               var5.append("");
               var5.append(var2);
               var6.setText(var5.toString());
            }

            if (PhotoEditToolCell.this.valueTextView.getTag() == null) {
               if (PhotoEditToolCell.this.valueAnimation != null) {
                  PhotoEditToolCell.this.valueAnimation.cancel();
               }

               PhotoEditToolCell.this.valueTextView.setTag(1);
               PhotoEditToolCell.this.valueAnimation = new AnimatorSet();
               PhotoEditToolCell.this.valueAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(PhotoEditToolCell.this.valueTextView, "alpha", new float[]{1.0F}), ObjectAnimator.ofFloat(PhotoEditToolCell.this.nameTextView, "alpha", new float[]{0.0F})});
               PhotoEditToolCell.this.valueAnimation.setDuration(180L);
               PhotoEditToolCell.this.valueAnimation.setInterpolator(new DecelerateInterpolator());
               PhotoEditToolCell.this.valueAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1x) {
                     AndroidUtilities.runOnUIThread(PhotoEditToolCell.this.hideValueRunnable, 1000L);
                  }
               });
               PhotoEditToolCell.this.valueAnimation.start();
            } else {
               AndroidUtilities.cancelRunOnUIThread(PhotoEditToolCell.this.hideValueRunnable);
               AndroidUtilities.runOnUIThread(PhotoEditToolCell.this.hideValueRunnable, 1000L);
            }

         }
      });
   }

   public void setTag(Object var1) {
      super.setTag(var1);
      this.seekBar.setTag(var1);
   }
}
