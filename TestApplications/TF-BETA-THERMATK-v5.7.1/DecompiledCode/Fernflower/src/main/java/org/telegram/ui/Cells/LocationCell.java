package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class LocationCell extends FrameLayout {
   private TextView addressTextView;
   private BackupImageView imageView;
   private TextView nameTextView;
   private boolean needDivider;

   public LocationCell(Context var1) {
      super(var1);
      this.imageView = new BackupImageView(var1);
      this.imageView.setBackgroundResource(2131165803);
      this.imageView.setSize(AndroidUtilities.dp(30.0F), AndroidUtilities.dp(30.0F));
      this.imageView.getImageReceiver().setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText3"), Mode.MULTIPLY));
      BackupImageView var2 = this.imageView;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var3) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      float var6;
      if (LocaleController.isRTL) {
         var6 = 0.0F;
      } else {
         var6 = 17.0F;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 17.0F;
      } else {
         var7 = 0.0F;
      }

      this.addView(var2, LayoutHelper.createFrame(40, 40.0F, var5 | 48, var6, 8.0F, var7, 0.0F));
      this.nameTextView = new TextView(var1);
      this.nameTextView.setTextSize(1, 16.0F);
      this.nameTextView.setMaxLines(1);
      this.nameTextView.setEllipsize(TruncateAt.END);
      this.nameTextView.setSingleLine(true);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      TextView var11 = this.nameTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var11.setGravity(var5);
      var11 = this.nameTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var3 = LocaleController.isRTL;
      byte var8 = 16;
      byte var9;
      if (var3) {
         var9 = 16;
      } else {
         var9 = 72;
      }

      var6 = (float)var9;
      if (LocaleController.isRTL) {
         var9 = 72;
      } else {
         var9 = 16;
      }

      this.addView(var11, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, var6, 5.0F, (float)var9, 0.0F));
      this.addressTextView = new TextView(var1);
      this.addressTextView.setTextSize(1, 14.0F);
      this.addressTextView.setMaxLines(1);
      this.addressTextView.setEllipsize(TruncateAt.END);
      this.addressTextView.setSingleLine(true);
      this.addressTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
      TextView var10 = this.addressTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var10.setGravity(var5);
      var10 = this.addressTextView;
      if (LocaleController.isRTL) {
         var5 = var4;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var9 = 16;
      } else {
         var9 = 72;
      }

      var6 = (float)var9;
      var9 = var8;
      if (LocaleController.isRTL) {
         var9 = 72;
      }

      this.addView(var10, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, var6, 30.0F, (float)var9, 0.0F));
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
         var1.drawLine((float)AndroidUtilities.dp(72.0F), (float)(this.getHeight() - 1), (float)this.getWidth(), (float)(this.getHeight() - 1), Theme.dividerPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0F) + this.needDivider, 1073741824));
   }

   public void setLocation(TLRPC.TL_messageMediaVenue var1, String var2, boolean var3) {
      this.needDivider = var3;
      this.nameTextView.setText(var1.title);
      this.addressTextView.setText(var1.address);
      this.imageView.setImage(var2, (String)null, (Drawable)null);
      this.setWillNotDraw(var3 ^ true);
   }
}
