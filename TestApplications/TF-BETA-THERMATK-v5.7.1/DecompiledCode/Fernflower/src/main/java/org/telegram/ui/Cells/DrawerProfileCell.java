package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SnowflakesEffect;

public class DrawerProfileCell extends FrameLayout {
   private boolean accountsShowed;
   private ImageView arrowView;
   private BackupImageView avatarImageView;
   private Integer currentColor;
   private Rect destRect = new Rect();
   private TextView nameTextView;
   private Paint paint = new Paint();
   private TextView phoneTextView;
   private ImageView shadowView;
   private SnowflakesEffect snowflakesEffect;
   private Rect srcRect = new Rect();

   public DrawerProfileCell(Context var1) {
      super(var1);
      this.shadowView = new ImageView(var1);
      this.shadowView.setVisibility(4);
      this.shadowView.setScaleType(ScaleType.FIT_XY);
      this.shadowView.setImageResource(2131165321);
      this.addView(this.shadowView, LayoutHelper.createFrame(-1, 70, 83));
      this.avatarImageView = new BackupImageView(var1);
      this.avatarImageView.getImageReceiver().setRoundRadius(AndroidUtilities.dp(32.0F));
      this.addView(this.avatarImageView, LayoutHelper.createFrame(64, 64.0F, 83, 16.0F, 0.0F, 0.0F, 67.0F));
      this.nameTextView = new TextView(var1);
      this.nameTextView.setTextSize(1, 15.0F);
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.nameTextView.setLines(1);
      this.nameTextView.setMaxLines(1);
      this.nameTextView.setSingleLine(true);
      this.nameTextView.setGravity(3);
      this.nameTextView.setEllipsize(TruncateAt.END);
      this.addView(this.nameTextView, LayoutHelper.createFrame(-1, -2.0F, 83, 16.0F, 0.0F, 76.0F, 28.0F));
      this.phoneTextView = new TextView(var1);
      this.phoneTextView.setTextSize(1, 13.0F);
      this.phoneTextView.setLines(1);
      this.phoneTextView.setMaxLines(1);
      this.phoneTextView.setSingleLine(true);
      this.phoneTextView.setGravity(3);
      this.addView(this.phoneTextView, LayoutHelper.createFrame(-1, -2.0F, 83, 16.0F, 0.0F, 76.0F, 9.0F));
      this.arrowView = new ImageView(var1);
      this.arrowView.setScaleType(ScaleType.CENTER);
      ImageView var2 = this.arrowView;
      int var3;
      String var4;
      if (this.accountsShowed) {
         var3 = 2131558438;
         var4 = "AccDescrHideAccounts";
      } else {
         var3 = 2131558472;
         var4 = "AccDescrShowAccounts";
      }

      var2.setContentDescription(LocaleController.getString(var4, var3));
      this.addView(this.arrowView, LayoutHelper.createFrame(59, 59, 85));
      if (Theme.getEventType() == 0) {
         this.snowflakesEffect = new SnowflakesEffect();
      }

   }

   public String applyBackground() {
      String var1 = (String)this.getTag();
      String var2 = "chats_menuTopBackground";
      if (!Theme.hasThemeKey("chats_menuTopBackground") || Theme.getColor("chats_menuTopBackground") == 0) {
         var2 = "chats_menuTopBackgroundCats";
      }

      if (!var2.equals(var1)) {
         this.setBackgroundColor(Theme.getColor(var2));
         this.setTag(var2);
      }

      return var2;
   }

   public boolean isAccountsShowed() {
      return this.accountsShowed;
   }

   // $FF: synthetic method
   public void lambda$setOnArrowClickListener$0$DrawerProfileCell(OnClickListener var1, View var2) {
      this.accountsShowed ^= true;
      ImageView var5 = this.arrowView;
      int var3;
      if (this.accountsShowed) {
         var3 = 2131165360;
      } else {
         var3 = 2131165359;
      }

      var5.setImageResource(var3);
      var1.onClick(this);
      var5 = this.arrowView;
      String var4;
      if (this.accountsShowed) {
         var3 = 2131558438;
         var4 = "AccDescrHideAccounts";
      } else {
         var3 = 2131558472;
         var4 = "AccDescrShowAccounts";
      }

      var5.setContentDescription(LocaleController.getString(var4, var3));
   }

   protected void onDraw(Canvas var1) {
      Drawable var2 = Theme.getCachedWallpaper();
      boolean var3 = this.applyBackground().equals("chats_menuTopBackground");
      boolean var4 = true;
      byte var5 = 0;
      boolean var6;
      if (!var3 && Theme.isCustomTheme() && !Theme.isPatternWallpaper() && var2 != null) {
         var6 = true;
      } else {
         var6 = false;
      }

      int var7;
      if (!var6 && Theme.hasThemeKey("chats_menuTopShadowCats")) {
         var7 = Theme.getColor("chats_menuTopShadowCats");
      } else {
         if (Theme.hasThemeKey("chats_menuTopShadow")) {
            var7 = Theme.getColor("chats_menuTopShadow");
         } else {
            var7 = -16777216 | Theme.getServiceMessageColor();
         }

         var4 = false;
      }

      Integer var8 = this.currentColor;
      if (var8 == null || var8 != var7) {
         this.currentColor = var7;
         this.shadowView.getDrawable().setColorFilter(new PorterDuffColorFilter(var7, Mode.MULTIPLY));
      }

      this.nameTextView.setTextColor(Theme.getColor("chats_menuName"));
      if (var6) {
         this.phoneTextView.setTextColor(Theme.getColor("chats_menuPhone"));
         if (this.shadowView.getVisibility() != 0) {
            this.shadowView.setVisibility(0);
         }

         if (var2 instanceof ColorDrawable) {
            var2.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            var2.draw(var1);
         } else if (var2 instanceof BitmapDrawable) {
            Bitmap var16 = ((BitmapDrawable)var2).getBitmap();
            float var9 = (float)this.getMeasuredWidth() / (float)var16.getWidth();
            float var10 = (float)this.getMeasuredHeight() / (float)var16.getHeight();
            float var11 = var9;
            if (var9 < var10) {
               var11 = var10;
            }

            int var13 = (int)((float)this.getMeasuredWidth() / var11);
            var7 = (int)((float)this.getMeasuredHeight() / var11);
            int var15 = (var16.getWidth() - var13) / 2;
            int var14 = (var16.getHeight() - var7) / 2;
            this.srcRect.set(var15, var14, var13 + var15, var7 + var14);
            this.destRect.set(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());

            try {
               var1.drawBitmap(var16, this.srcRect, this.destRect, this.paint);
            } catch (Throwable var12) {
               FileLog.e(var12);
            }
         }
      } else {
         byte var17;
         if (var4) {
            var17 = var5;
         } else {
            var17 = 4;
         }

         if (this.shadowView.getVisibility() != var17) {
            this.shadowView.setVisibility(var17);
         }

         this.phoneTextView.setTextColor(Theme.getColor("chats_menuPhoneCats"));
         super.onDraw(var1);
      }

      SnowflakesEffect var18 = this.snowflakesEffect;
      if (var18 != null) {
         var18.onDraw(this, var1);
      }

   }

   protected void onMeasure(int var1, int var2) {
      if (VERSION.SDK_INT >= 21) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(148.0F) + AndroidUtilities.statusBarHeight, 1073741824));
      } else {
         try {
            super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(148.0F), 1073741824));
         } catch (Exception var4) {
            this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(148.0F));
            FileLog.e((Throwable)var4);
         }
      }

   }

   public void setAccountsShowed(boolean var1) {
      if (this.accountsShowed != var1) {
         this.accountsShowed = var1;
         ImageView var2 = this.arrowView;
         int var3;
         if (this.accountsShowed) {
            var3 = 2131165360;
         } else {
            var3 = 2131165359;
         }

         var2.setImageResource(var3);
      }
   }

   public void setOnArrowClickListener(OnClickListener var1) {
      this.arrowView.setOnClickListener(new _$$Lambda$DrawerProfileCell$E00gMmT74biKthBWyKI7QNe_uk4(this, var1));
   }

   public void setUser(TLRPC.User var1, boolean var2) {
      if (var1 != null) {
         this.accountsShowed = var2;
         ImageView var3 = this.arrowView;
         int var4;
         if (this.accountsShowed) {
            var4 = 2131165360;
         } else {
            var4 = 2131165359;
         }

         var3.setImageResource(var4);
         this.nameTextView.setText(UserObject.getUserName(var1));
         TextView var5 = this.phoneTextView;
         PhoneFormat var7 = PhoneFormat.getInstance();
         StringBuilder var6 = new StringBuilder();
         var6.append("+");
         var6.append(var1.phone);
         var5.setText(var7.format(var6.toString()));
         AvatarDrawable var8 = new AvatarDrawable(var1);
         var8.setColor(Theme.getColor("avatar_backgroundInProfileBlue"));
         this.avatarImageView.setImage((ImageLocation)ImageLocation.getForUser(var1, false), "50_50", (Drawable)var8, (Object)var1);
         this.applyBackground();
      }
   }
}
