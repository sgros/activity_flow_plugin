package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.WebFile;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class PaymentInfoCell extends FrameLayout {
   private TextView detailExTextView;
   private TextView detailTextView;
   private BackupImageView imageView;
   private TextView nameTextView;

   public PaymentInfoCell(Context var1) {
      super(var1);
      this.imageView = new BackupImageView(var1);
      BackupImageView var2 = this.imageView;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var3) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      this.addView(var2, LayoutHelper.createFrame(100, 100.0F, var5, 10.0F, 10.0F, 10.0F, 0.0F));
      this.nameTextView = new TextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTextSize(1, 16.0F);
      this.nameTextView.setLines(1);
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.nameTextView.setMaxLines(1);
      this.nameTextView.setSingleLine(true);
      this.nameTextView.setEllipsize(TruncateAt.END);
      TextView var9 = this.nameTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var9.setGravity(var5 | 48);
      var9 = this.nameTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      float var6;
      if (LocaleController.isRTL) {
         var6 = 10.0F;
      } else {
         var6 = 123.0F;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 123.0F;
      } else {
         var7 = 10.0F;
      }

      this.addView(var9, LayoutHelper.createFrame(-1, -2.0F, var5 | 48, var6, 9.0F, var7, 0.0F));
      this.detailTextView = new TextView(var1);
      this.detailTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.detailTextView.setTextSize(1, 14.0F);
      this.detailTextView.setMaxLines(3);
      this.detailTextView.setEllipsize(TruncateAt.END);
      var9 = this.detailTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var9.setGravity(var5 | 48);
      var9 = this.detailTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 10.0F;
      } else {
         var6 = 123.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 123.0F;
      } else {
         var7 = 10.0F;
      }

      this.addView(var9, LayoutHelper.createFrame(-1, -2.0F, var5 | 48, var6, 33.0F, var7, 0.0F));
      this.detailExTextView = new TextView(var1);
      this.detailExTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.detailExTextView.setTextSize(1, 13.0F);
      this.detailExTextView.setLines(1);
      this.detailExTextView.setMaxLines(1);
      this.detailExTextView.setSingleLine(true);
      this.detailExTextView.setEllipsize(TruncateAt.END);
      TextView var8 = this.detailExTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var8.setGravity(var5 | 48);
      var8 = this.detailExTextView;
      if (LocaleController.isRTL) {
         var5 = var4;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 10.0F;
      } else {
         var6 = 123.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 123.0F;
      } else {
         var7 = 10.0F;
      }

      this.addView(var8, LayoutHelper.createFrame(-1, -2.0F, var5 | 48, var6, 90.0F, var7, 0.0F));
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      var2 = this.detailTextView.getBottom() + AndroidUtilities.dp(3.0F);
      TextView var6 = this.detailExTextView;
      var6.layout(var6.getLeft(), var2, this.detailExTextView.getRight(), this.detailExTextView.getMeasuredHeight() + var2);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(120.0F), 1073741824));
   }

   public void setInvoice(TLRPC.TL_messageMediaInvoice var1, String var2) {
      this.nameTextView.setText(var1.title);
      this.detailTextView.setText(var1.description);
      this.detailExTextView.setText(var2);
      int var3;
      if (AndroidUtilities.isTablet()) {
         var3 = AndroidUtilities.getMinTabletSide();
      } else {
         Point var10 = AndroidUtilities.displaySize;
         var3 = Math.min(var10.x, var10.y);
      }

      var3 = (int)((float)var3 * 0.7F);
      float var4 = (float)640;
      float var5 = var4 / (float)(var3 - AndroidUtilities.dp(2.0F));
      int var6 = (int)(var4 / var5);
      int var7 = (int)((float)360 / var5);
      TLRPC.WebDocument var11 = var1.photo;
      byte var13 = 5;
      byte var8;
      if (var11 != null && var11.mime_type.startsWith("image/")) {
         TextView var12 = this.nameTextView;
         if (LocaleController.isRTL) {
            var8 = 5;
         } else {
            var8 = 3;
         }

         if (LocaleController.isRTL) {
            var4 = 10.0F;
         } else {
            var4 = 123.0F;
         }

         if (LocaleController.isRTL) {
            var5 = 123.0F;
         } else {
            var5 = 10.0F;
         }

         var12.setLayoutParams(LayoutHelper.createFrame(-1, -2.0F, var8 | 48, var4, 9.0F, var5, 0.0F));
         var12 = this.detailTextView;
         if (LocaleController.isRTL) {
            var8 = 5;
         } else {
            var8 = 3;
         }

         if (LocaleController.isRTL) {
            var4 = 10.0F;
         } else {
            var4 = 123.0F;
         }

         if (LocaleController.isRTL) {
            var5 = 123.0F;
         } else {
            var5 = 10.0F;
         }

         var12.setLayoutParams(LayoutHelper.createFrame(-1, -2.0F, var8 | 48, var4, 33.0F, var5, 0.0F));
         var12 = this.detailExTextView;
         if (!LocaleController.isRTL) {
            var13 = 3;
         }

         if (LocaleController.isRTL) {
            var4 = 10.0F;
         } else {
            var4 = 123.0F;
         }

         if (LocaleController.isRTL) {
            var5 = 123.0F;
         } else {
            var5 = 10.0F;
         }

         var12.setLayoutParams(LayoutHelper.createFrame(-1, -2.0F, var13 | 48, var4, 90.0F, var5, 0.0F));
         this.imageView.setVisibility(0);
         var2 = String.format(Locale.US, "%d_%d", var6, var7);
         this.imageView.getImageReceiver().setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(var1.photo)), var2, (ImageLocation)null, (String)null, -1, (String)null, var1, 1);
      } else {
         TextView var9 = this.nameTextView;
         if (LocaleController.isRTL) {
            var8 = 5;
         } else {
            var8 = 3;
         }

         var9.setLayoutParams(LayoutHelper.createFrame(-1, -2.0F, var8 | 48, 17.0F, 9.0F, 17.0F, 0.0F));
         var9 = this.detailTextView;
         if (LocaleController.isRTL) {
            var8 = 5;
         } else {
            var8 = 3;
         }

         var9.setLayoutParams(LayoutHelper.createFrame(-1, -2.0F, var8 | 48, 17.0F, 33.0F, 17.0F, 0.0F));
         var9 = this.detailExTextView;
         if (!LocaleController.isRTL) {
            var13 = 3;
         }

         var9.setLayoutParams(LayoutHelper.createFrame(-1, -2.0F, var13 | 48, 17.0F, 90.0F, 17.0F, 0.0F));
         this.imageView.setVisibility(8);
      }

   }
}
