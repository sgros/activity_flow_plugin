package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class MentionCell extends LinearLayout {
   private AvatarDrawable avatarDrawable;
   private BackupImageView imageView;
   private TextView nameTextView;
   private TextView usernameTextView;

   public MentionCell(Context var1) {
      super(var1);
      this.setOrientation(0);
      this.avatarDrawable = new AvatarDrawable();
      this.avatarDrawable.setTextSize(AndroidUtilities.dp(12.0F));
      this.imageView = new BackupImageView(var1);
      this.imageView.setRoundRadius(AndroidUtilities.dp(14.0F));
      this.addView(this.imageView, LayoutHelper.createLinear(28, 28, 12.0F, 4.0F, 0.0F, 0.0F));
      this.nameTextView = new TextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTextSize(1, 15.0F);
      this.nameTextView.setSingleLine(true);
      this.nameTextView.setGravity(3);
      this.nameTextView.setEllipsize(TruncateAt.END);
      this.addView(this.nameTextView, LayoutHelper.createLinear(-2, -2, 16, 12, 0, 0, 0));
      this.usernameTextView = new TextView(var1);
      this.usernameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
      this.usernameTextView.setTextSize(1, 15.0F);
      this.usernameTextView.setSingleLine(true);
      this.usernameTextView.setGravity(3);
      this.usernameTextView.setEllipsize(TruncateAt.END);
      this.addView(this.usernameTextView, LayoutHelper.createLinear(-2, -2, 16, 12, 0, 8, 0));
   }

   public void invalidate() {
      super.invalidate();
      this.nameTextView.invalidate();
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(36.0F), 1073741824));
   }

   public void setBotCommand(String var1, String var2, TLRPC.User var3) {
      if (var3 != null) {
         this.imageView.setVisibility(0);
         this.avatarDrawable.setInfo(var3);
         TLRPC.UserProfilePhoto var4 = var3.photo;
         if (var4 != null && var4.photo_small != null) {
            this.imageView.setImage((ImageLocation)ImageLocation.getForUser(var3, false), "50_50", (Drawable)this.avatarDrawable, (Object)var3);
         } else {
            this.imageView.setImageDrawable(this.avatarDrawable);
         }
      } else {
         this.imageView.setVisibility(4);
      }

      this.usernameTextView.setVisibility(0);
      this.nameTextView.setText(var1);
      TextView var5 = this.usernameTextView;
      var5.setText(Emoji.replaceEmoji(var2, var5.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false));
   }

   public void setEmojiSuggestion(DataQuery.KeywordResult var1) {
      this.imageView.setVisibility(4);
      this.usernameTextView.setVisibility(4);
      StringBuilder var2 = new StringBuilder(var1.emoji.length() + var1.keyword.length() + 4);
      var2.append(var1.emoji);
      var2.append("   :");
      var2.append(var1.keyword);
      TextView var3 = this.nameTextView;
      var3.setText(Emoji.replaceEmoji(var2, var3.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0F), false));
   }

   public void setIsDarkTheme(boolean var1) {
      if (var1) {
         this.nameTextView.setTextColor(-1);
         this.usernameTextView.setTextColor(-4473925);
      } else {
         this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.usernameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
      }

   }

   public void setText(String var1) {
      this.imageView.setVisibility(4);
      this.usernameTextView.setVisibility(4);
      this.nameTextView.setText(var1);
   }

   public void setUser(TLRPC.User var1) {
      if (var1 == null) {
         this.nameTextView.setText("");
         this.usernameTextView.setText("");
         this.imageView.setImageDrawable((Drawable)null);
      } else {
         this.avatarDrawable.setInfo(var1);
         TLRPC.UserProfilePhoto var2 = var1.photo;
         if (var2 != null && var2.photo_small != null) {
            this.imageView.setImage((ImageLocation)ImageLocation.getForUser(var1, false), "50_50", (Drawable)this.avatarDrawable, (Object)var1);
         } else {
            this.imageView.setImageDrawable(this.avatarDrawable);
         }

         this.nameTextView.setText(UserObject.getUserName(var1));
         if (var1.username != null) {
            TextView var4 = this.usernameTextView;
            StringBuilder var3 = new StringBuilder();
            var3.append("@");
            var3.append(var1.username);
            var4.setText(var3.toString());
         } else {
            this.usernameTextView.setText("");
         }

         this.imageView.setVisibility(0);
         this.usernameTextView.setVisibility(0);
      }
   }
}
