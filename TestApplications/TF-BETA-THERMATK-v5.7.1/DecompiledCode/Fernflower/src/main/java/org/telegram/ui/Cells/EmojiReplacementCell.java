package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class EmojiReplacementCell extends FrameLayout {
   private String emoji;
   private ImageView imageView;

   public EmojiReplacementCell(Context var1) {
      super(var1);
      this.imageView = new ImageView(var1);
      this.imageView.setScaleType(ScaleType.CENTER);
      this.addView(this.imageView, LayoutHelper.createFrame(42, 42.0F, 1, 0.0F, 5.0F, 0.0F, 0.0F));
   }

   public String getEmoji() {
      return this.emoji;
   }

   public void invalidate() {
      super.invalidate();
      this.imageView.invalidate();
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(52.0F) + this.getPaddingLeft() + this.getPaddingRight(), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(54.0F), 1073741824));
   }

   public void setEmoji(String var1, int var2) {
      this.emoji = var1;
      this.imageView.setImageDrawable(Emoji.getEmojiBigDrawable(var1));
      if (var2 == -1) {
         this.setBackgroundResource(2131165862);
         this.setPadding(AndroidUtilities.dp(7.0F), 0, 0, 0);
      } else if (var2 == 0) {
         this.setBackgroundResource(2131165861);
         this.setPadding(0, 0, 0, 0);
      } else if (var2 == 1) {
         this.setBackgroundResource(2131165863);
         this.setPadding(0, 0, AndroidUtilities.dp(7.0F), 0);
      } else if (var2 == 2) {
         this.setBackgroundResource(2131165859);
         this.setPadding(AndroidUtilities.dp(3.0F), 0, AndroidUtilities.dp(3.0F), 0);
      }

      Drawable var3 = this.getBackground();
      if (var3 != null) {
         var3.setAlpha(230);
         var3.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_stickersHintPanel"), Mode.MULTIPLY));
      }

   }
}
