package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class ArchiveHintInnerCell extends FrameLayout {
   private TextView headerTextView;
   private ImageView imageView;
   private ImageView imageView2;
   private TextView messageTextView;

   public ArchiveHintInnerCell(Context var1, int var2) {
      super(var1);
      this.imageView = new ImageView(var1);
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_nameMessage_threeLines"), Mode.MULTIPLY));
      this.headerTextView = new TextView(var1);
      this.headerTextView.setTextColor(Theme.getColor("chats_nameMessage_threeLines"));
      this.headerTextView.setTextSize(1, 20.0F);
      this.headerTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.headerTextView.setGravity(17);
      this.addView(this.headerTextView, LayoutHelper.createFrame(-1, -2.0F, 51, 52.0F, 75.0F, 52.0F, 0.0F));
      this.messageTextView = new TextView(var1);
      this.messageTextView.setTextColor(Theme.getColor("chats_message"));
      this.messageTextView.setTextSize(1, 14.0F);
      this.messageTextView.setGravity(17);
      this.addView(this.messageTextView, LayoutHelper.createFrame(-1, -2.0F, 51, 52.0F, 110.0F, 52.0F, 0.0F));
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 == 2) {
               this.addView(this.imageView, LayoutHelper.createFrame(-2, -2.0F, 49, 0.0F, 18.0F, 0.0F, 0.0F));
               this.headerTextView.setText(LocaleController.getString("ArchiveHintHeader3", 2131558647));
               this.messageTextView.setText(LocaleController.getString("ArchiveHintText3", 2131558650));
               this.imageView.setImageResource(2131165346);
            }
         } else {
            this.addView(this.imageView, LayoutHelper.createFrame(-2, -2.0F, 49, 0.0F, 18.0F, 0.0F, 0.0F));
            this.headerTextView.setText(LocaleController.getString("ArchiveHintHeader2", 2131558646));
            this.messageTextView.setText(LocaleController.getString("ArchiveHintText2", 2131558649));
            this.imageView.setImageResource(2131165345);
         }
      } else {
         this.addView(this.imageView, LayoutHelper.createFrame(-2, -2.0F, 49, 0.0F, 20.0F, 8.0F, 0.0F));
         this.imageView2 = new ImageView(var1);
         this.imageView2.setImageResource(2131165342);
         this.imageView2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_unreadCounter"), Mode.MULTIPLY));
         this.addView(this.imageView2, LayoutHelper.createFrame(-2, -2.0F, 49, 0.0F, 20.0F, 8.0F, 0.0F));
         this.headerTextView.setText(LocaleController.getString("ArchiveHintHeader1", 2131558645));
         this.messageTextView.setText(LocaleController.getString("ArchiveHintText1", 2131558648));
         this.imageView.setImageResource(2131165343);
      }

   }
}
