package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class AccountSelectCell extends FrameLayout {
   private int accountNumber;
   private AvatarDrawable avatarDrawable = new AvatarDrawable();
   private ImageView checkImageView;
   private BackupImageView imageView;
   private TextView textView;

   public AccountSelectCell(Context var1) {
      super(var1);
      this.avatarDrawable.setTextSize(AndroidUtilities.dp(12.0F));
      this.imageView = new BackupImageView(var1);
      this.imageView.setRoundRadius(AndroidUtilities.dp(18.0F));
      this.addView(this.imageView, LayoutHelper.createFrame(36, 36.0F, 51, 10.0F, 10.0F, 0.0F, 0.0F));
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
      this.textView.setTextSize(1, 15.0F);
      this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setGravity(19);
      this.textView.setEllipsize(TruncateAt.END);
      this.addView(this.textView, LayoutHelper.createFrame(-1, -1.0F, 51, 61.0F, 0.0F, 56.0F, 0.0F));
      this.checkImageView = new ImageView(var1);
      this.checkImageView.setImageResource(2131165270);
      this.checkImageView.setScaleType(ScaleType.CENTER);
      this.checkImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_menuItemCheck"), Mode.MULTIPLY));
      this.addView(this.checkImageView, LayoutHelper.createFrame(40, -1.0F, 53, 0.0F, 0.0F, 6.0F, 0.0F));
   }

   public int getAccountNumber() {
      return this.accountNumber;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.textView.setTextColor(Theme.getColor("chats_menuItemText"));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0F), 1073741824));
   }

   public void setAccount(int var1, boolean var2) {
      this.accountNumber = var1;
      TLRPC.User var3 = UserConfig.getInstance(this.accountNumber).getCurrentUser();
      this.avatarDrawable.setInfo(var3);
      this.textView.setText(ContactsController.formatName(var3.first_name, var3.last_name));
      this.imageView.getImageReceiver().setCurrentAccount(var1);
      BackupImageView var4 = this.imageView;
      byte var5 = 0;
      var4.setImage((ImageLocation)ImageLocation.getForUser(var3, false), "50_50", (Drawable)this.avatarDrawable, (Object)var3);
      ImageView var7 = this.checkImageView;
      byte var6;
      if (var2 && var1 == UserConfig.selectedAccount) {
         var6 = var5;
      } else {
         var6 = 4;
      }

      var7.setVisibility(var6);
   }
}
