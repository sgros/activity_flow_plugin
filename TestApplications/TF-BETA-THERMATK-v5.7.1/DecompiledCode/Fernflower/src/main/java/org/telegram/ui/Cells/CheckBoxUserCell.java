package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
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
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.LayoutHelper;

public class CheckBoxUserCell extends FrameLayout {
   private AvatarDrawable avatarDrawable;
   private CheckBoxSquare checkBox;
   private TLRPC.User currentUser;
   private BackupImageView imageView;
   private boolean needDivider;
   private TextView textView;

   public CheckBoxUserCell(Context var1, boolean var2) {
      super(var1);
      this.textView = new TextView(var1);
      TextView var3 = this.textView;
      String var4;
      if (var2) {
         var4 = "dialogTextBlack";
      } else {
         var4 = "windowBackgroundWhiteBlackText";
      }

      var3.setTextColor(Theme.getColor(var4));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setEllipsize(TruncateAt.END);
      TextView var12 = this.textView;
      boolean var5 = LocaleController.isRTL;
      byte var6 = 5;
      byte var7;
      if (var5) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var12.setGravity(var7 | 16);
      var12 = this.textView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var5 = LocaleController.isRTL;
      byte var8 = 94;
      byte var9;
      if (var5) {
         var9 = 21;
      } else {
         var9 = 94;
      }

      float var10 = (float)var9;
      if (LocaleController.isRTL) {
         var9 = var8;
      } else {
         var9 = 21;
      }

      this.addView(var12, LayoutHelper.createFrame(-1, -1.0F, var7 | 48, var10, 0.0F, (float)var9, 0.0F));
      this.avatarDrawable = new AvatarDrawable();
      this.imageView = new BackupImageView(var1);
      this.imageView.setRoundRadius(AndroidUtilities.dp(36.0F));
      BackupImageView var13 = this.imageView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      this.addView(var13, LayoutHelper.createFrame(36, 36.0F, var7 | 48, 48.0F, 7.0F, 48.0F, 0.0F));
      this.checkBox = new CheckBoxSquare(var1, var2);
      CheckBoxSquare var11 = this.checkBox;
      if (LocaleController.isRTL) {
         var7 = var6;
      } else {
         var7 = 3;
      }

      var2 = LocaleController.isRTL;
      var6 = 0;
      if (var2) {
         var9 = 0;
      } else {
         var9 = 21;
      }

      var10 = (float)var9;
      var9 = var6;
      if (LocaleController.isRTL) {
         var9 = 21;
      }

      this.addView(var11, LayoutHelper.createFrame(18, 18.0F, var7 | 48, var10, 16.0F, (float)var9, 0.0F));
   }

   public CheckBoxSquare getCheckBox() {
      return this.checkBox;
   }

   public TLRPC.User getCurrentUser() {
      return this.currentUser;
   }

   public TextView getTextView() {
      return this.textView;
   }

   public boolean isChecked() {
      return this.checkBox.isChecked();
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
         float var2;
         if (LocaleController.isRTL) {
            var2 = 0.0F;
         } else {
            var2 = (float)AndroidUtilities.dp(20.0F);
         }

         float var3 = (float)(this.getMeasuredHeight() - 1);
         int var4 = this.getMeasuredWidth();
         int var5;
         if (LocaleController.isRTL) {
            var5 = AndroidUtilities.dp(20.0F);
         } else {
            var5 = 0;
         }

         var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0F) + this.needDivider, 1073741824));
   }

   public void setChecked(boolean var1, boolean var2) {
      this.checkBox.setChecked(var1, var2);
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }

   public void setUser(TLRPC.User var1, boolean var2, boolean var3) {
      this.currentUser = var1;
      this.textView.setText(ContactsController.formatName(var1.first_name, var1.last_name));
      this.checkBox.setChecked(var2, false);
      this.avatarDrawable.setInfo(var1);
      this.imageView.setImage((ImageLocation)ImageLocation.getForUser(var1, false), "50_50", (Drawable)this.avatarDrawable, (Object)var1);
      this.needDivider = var3;
      this.setWillNotDraw(var3 ^ true);
   }
}
