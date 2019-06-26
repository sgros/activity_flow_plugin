package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SeekBarView;

public class BrightnessControlCell extends FrameLayout {
   private ImageView leftImageView;
   private ImageView rightImageView;
   private SeekBarView seekBarView;

   public BrightnessControlCell(Context var1) {
      super(var1);
      this.leftImageView = new ImageView(var1);
      this.leftImageView.setImageResource(2131165324);
      this.addView(this.leftImageView, LayoutHelper.createFrame(24, 24.0F, 51, 17.0F, 12.0F, 0.0F, 0.0F));
      this.seekBarView = new SeekBarView(var1) {
         public boolean onTouchEvent(MotionEvent var1) {
            if (var1.getAction() == 0) {
               this.getParent().requestDisallowInterceptTouchEvent(true);
            }

            return super.onTouchEvent(var1);
         }
      };
      this.seekBarView.setReportChanges(true);
      this.seekBarView.setDelegate(new _$$Lambda$GN4ZDAm3ZJLTBxjR6_2pFIyDFuo(this));
      this.addView(this.seekBarView, LayoutHelper.createFrame(-1, 30.0F, 51, 58.0F, 9.0F, 58.0F, 0.0F));
      this.rightImageView = new ImageView(var1);
      this.rightImageView.setImageResource(2131165323);
      this.addView(this.rightImageView, LayoutHelper.createFrame(24, 24.0F, 53, 0.0F, 12.0F, 17.0F, 0.0F));
   }

   protected void didChangedValue(float var1) {
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.leftImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("profile_actionIcon"), Mode.MULTIPLY));
      this.rightImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("profile_actionIcon"), Mode.MULTIPLY));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
   }

   public void setProgress(float var1) {
      this.seekBarView.setProgress(var1);
   }
}
