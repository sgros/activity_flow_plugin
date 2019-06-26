package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class JoinSheetUserCell extends FrameLayout {
   private AvatarDrawable avatarDrawable = new AvatarDrawable();
   private BackupImageView imageView;
   private TextView nameTextView;
   private int[] result = new int[1];

   public JoinSheetUserCell(Context var1) {
      super(var1);
      this.imageView = new BackupImageView(var1);
      this.imageView.setRoundRadius(AndroidUtilities.dp(27.0F));
      this.addView(this.imageView, LayoutHelper.createFrame(54, 54.0F, 49, 0.0F, 7.0F, 0.0F, 0.0F));
      this.nameTextView = new TextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("dialogTextBlack"));
      this.nameTextView.setTextSize(1, 12.0F);
      this.nameTextView.setMaxLines(1);
      this.nameTextView.setGravity(49);
      this.nameTextView.setLines(1);
      this.nameTextView.setSingleLine(true);
      this.nameTextView.setEllipsize(TruncateAt.END);
      this.addView(this.nameTextView, LayoutHelper.createFrame(-1, -2.0F, 51, 6.0F, 64.0F, 6.0F, 0.0F));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(90.0F), 1073741824));
   }

   public void setCount(int var1) {
      this.nameTextView.setText("");
      AvatarDrawable var2 = this.avatarDrawable;
      StringBuilder var3 = new StringBuilder();
      var3.append("+");
      var3.append(LocaleController.formatShortNumber(var1, this.result));
      var2.setInfo(0, (String)null, (String)null, false, var3.toString());
      this.imageView.setImage((ImageLocation)null, "50_50", (Drawable)this.avatarDrawable, (Object)null);
   }

   public void setUser(TLRPC.User var1) {
      this.nameTextView.setText(ContactsController.formatName(var1.first_name, var1.last_name));
      this.avatarDrawable.setInfo(var1);
      this.imageView.setImage((ImageLocation)ImageLocation.getForUser(var1, false), "50_50", (Drawable)this.avatarDrawable, (Object)var1);
   }
}
