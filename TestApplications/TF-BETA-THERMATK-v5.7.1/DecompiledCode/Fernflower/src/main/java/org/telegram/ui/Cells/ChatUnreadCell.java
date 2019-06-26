package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class ChatUnreadCell extends FrameLayout {
   private FrameLayout backgroundLayout;
   private ImageView imageView;
   private TextView textView;

   public ChatUnreadCell(Context var1) {
      super(var1);
      this.backgroundLayout = new FrameLayout(var1);
      this.backgroundLayout.setBackgroundResource(2131165688);
      this.backgroundLayout.getBackground().setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_unreadMessagesStartBackground"), Mode.MULTIPLY));
      this.addView(this.backgroundLayout, LayoutHelper.createFrame(-1, 27.0F, 51, 0.0F, 7.0F, 0.0F, 0.0F));
      this.imageView = new ImageView(var1);
      this.imageView.setImageResource(2131165415);
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_unreadMessagesStartArrowIcon"), Mode.MULTIPLY));
      this.imageView.setPadding(0, AndroidUtilities.dp(2.0F), 0, 0);
      this.backgroundLayout.addView(this.imageView, LayoutHelper.createFrame(-2, -2.0F, 21, 0.0F, 0.0F, 10.0F, 0.0F));
      this.textView = new TextView(var1);
      this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(1.0F));
      this.textView.setTextSize(1, 14.0F);
      this.textView.setTextColor(Theme.getColor("chat_unreadMessagesStartText"));
      this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.addView(this.textView, LayoutHelper.createFrame(-2, -2, 17));
   }

   public FrameLayout getBackgroundLayout() {
      return this.backgroundLayout;
   }

   public ImageView getImageView() {
      return this.imageView;
   }

   public TextView getTextView() {
      return this.textView;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0F), 1073741824));
   }

   public void setText(String var1) {
      this.textView.setText(var1);
   }
}
