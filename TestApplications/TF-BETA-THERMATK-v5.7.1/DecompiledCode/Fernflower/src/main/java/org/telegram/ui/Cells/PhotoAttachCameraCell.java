package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

@SuppressLint({"NewApi"})
public class PhotoAttachCameraCell extends FrameLayout {
   private ImageView imageView;

   public PhotoAttachCameraCell(Context var1) {
      super(var1);
      this.imageView = new ImageView(var1);
      this.imageView.setScaleType(ScaleType.CENTER);
      this.imageView.setImageResource(2131165495);
      this.imageView.setBackgroundColor(-16777216);
      this.addView(this.imageView, LayoutHelper.createFrame(80, 80.0F));
      this.setFocusable(true);
   }

   public ImageView getImageView() {
      return this.imageView;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogCameraIcon"), Mode.MULTIPLY));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0F), 1073741824));
   }
}
