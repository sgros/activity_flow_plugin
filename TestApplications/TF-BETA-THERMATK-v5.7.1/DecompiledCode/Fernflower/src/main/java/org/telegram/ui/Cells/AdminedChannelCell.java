package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.URLSpanNoUnderline;

public class AdminedChannelCell extends FrameLayout {
   private AvatarDrawable avatarDrawable;
   private BackupImageView avatarImageView;
   private int currentAccount;
   private TLRPC.Chat currentChannel;
   private ImageView deleteButton;
   private boolean isLast;
   private SimpleTextView nameTextView;
   private SimpleTextView statusTextView;

   public AdminedChannelCell(Context var1, OnClickListener var2) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.avatarDrawable = new AvatarDrawable();
      this.avatarImageView = new BackupImageView(var1);
      this.avatarImageView.setRoundRadius(AndroidUtilities.dp(24.0F));
      BackupImageView var3 = this.avatarImageView;
      boolean var4 = LocaleController.isRTL;
      byte var5 = 5;
      byte var6;
      if (var4) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 0.0F;
      } else {
         var7 = 12.0F;
      }

      float var8;
      if (LocaleController.isRTL) {
         var8 = 12.0F;
      } else {
         var8 = 0.0F;
      }

      this.addView(var3, LayoutHelper.createFrame(48, 48.0F, var6 | 48, var7, 12.0F, var8, 0.0F));
      this.nameTextView = new SimpleTextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTextSize(17);
      SimpleTextView var10 = this.nameTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var10.setGravity(var6 | 48);
      var10 = this.nameTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      if (LocaleController.isRTL) {
         var7 = 62.0F;
      } else {
         var7 = 73.0F;
      }

      if (LocaleController.isRTL) {
         var8 = 73.0F;
      } else {
         var8 = 62.0F;
      }

      this.addView(var10, LayoutHelper.createFrame(-1, 20.0F, var6 | 48, var7, 15.5F, var8, 0.0F));
      this.statusTextView = new SimpleTextView(var1);
      this.statusTextView.setTextSize(14);
      this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
      this.statusTextView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
      var10 = this.statusTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var10.setGravity(var6 | 48);
      var10 = this.statusTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      if (LocaleController.isRTL) {
         var7 = 62.0F;
      } else {
         var7 = 73.0F;
      }

      if (LocaleController.isRTL) {
         var8 = 73.0F;
      } else {
         var8 = 62.0F;
      }

      this.addView(var10, LayoutHelper.createFrame(-1, 20.0F, var6 | 48, var7, 38.5F, var8, 0.0F));
      this.deleteButton = new ImageView(var1);
      this.deleteButton.setScaleType(ScaleType.CENTER);
      this.deleteButton.setImageResource(2131165652);
      this.deleteButton.setOnClickListener(var2);
      this.deleteButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText"), Mode.MULTIPLY));
      ImageView var9 = this.deleteButton;
      var6 = var5;
      if (LocaleController.isRTL) {
         var6 = 3;
      }

      if (LocaleController.isRTL) {
         var7 = 7.0F;
      } else {
         var7 = 0.0F;
      }

      if (LocaleController.isRTL) {
         var8 = 0.0F;
      } else {
         var8 = 7.0F;
      }

      this.addView(var9, LayoutHelper.createFrame(48, 48.0F, var6 | 48, var7, 12.0F, var8, 0.0F));
   }

   public TLRPC.Chat getCurrentChannel() {
      return this.currentChannel;
   }

   public ImageView getDeleteButton() {
      return this.deleteButton;
   }

   public SimpleTextView getNameTextView() {
      return this.nameTextView;
   }

   public SimpleTextView getStatusTextView() {
      return this.statusTextView;
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   protected void onMeasure(int var1, int var2) {
      var2 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824);
      byte var3;
      if (this.isLast) {
         var3 = 12;
      } else {
         var3 = 0;
      }

      super.onMeasure(var2, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)(var3 + 60)), 1073741824));
   }

   public void setChannel(TLRPC.Chat var1, boolean var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
      var3.append("/");
      String var5 = var3.toString();
      this.currentChannel = var1;
      this.avatarDrawable.setInfo(var1);
      this.nameTextView.setText(var1.title);
      StringBuilder var4 = new StringBuilder();
      var4.append(var5);
      var4.append(var1.username);
      SpannableStringBuilder var6 = new SpannableStringBuilder(var4.toString());
      var6.setSpan(new URLSpanNoUnderline(""), var5.length(), var6.length(), 33);
      this.statusTextView.setText(var6);
      this.avatarImageView.setImage((ImageLocation)ImageLocation.getForChat(var1, false), "50_50", (Drawable)this.avatarDrawable, (Object)this.currentChannel);
      this.isLast = var2;
   }

   public void update() {
      this.avatarDrawable.setInfo(this.currentChannel);
      this.avatarImageView.invalidate();
   }
}
