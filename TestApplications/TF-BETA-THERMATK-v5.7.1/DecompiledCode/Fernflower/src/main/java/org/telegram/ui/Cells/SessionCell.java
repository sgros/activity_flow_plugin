package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class SessionCell extends FrameLayout {
   private AvatarDrawable avatarDrawable;
   private int currentAccount;
   private TextView detailExTextView;
   private TextView detailTextView;
   private BackupImageView imageView;
   private TextView nameTextView;
   private boolean needDivider;
   private TextView onlineTextView;

   public SessionCell(Context var1, int var2) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      LinearLayout var3 = new LinearLayout(var1);
      byte var4 = 0;
      var3.setOrientation(0);
      var3.setWeightSum(1.0F);
      byte var5 = 15;
      byte var6 = 5;
      byte var8;
      float var9;
      byte var12;
      if (var2 == 1) {
         if (LocaleController.isRTL) {
            var12 = 5;
         } else {
            var12 = 3;
         }

         boolean var7 = LocaleController.isRTL;
         var5 = 49;
         if (var7) {
            var8 = 15;
         } else {
            var8 = 49;
         }

         var9 = (float)var8;
         if (LocaleController.isRTL) {
            var8 = var5;
         } else {
            var8 = 15;
         }

         this.addView(var3, LayoutHelper.createFrame(-1, 30.0F, var12 | 48, var9, 11.0F, (float)var8, 0.0F));
         this.avatarDrawable = new AvatarDrawable();
         this.avatarDrawable.setTextSize(AndroidUtilities.dp(10.0F));
         this.imageView = new BackupImageView(var1);
         this.imageView.setRoundRadius(AndroidUtilities.dp(10.0F));
         BackupImageView var10 = this.imageView;
         if (LocaleController.isRTL) {
            var12 = 5;
         } else {
            var12 = 3;
         }

         if (LocaleController.isRTL) {
            var8 = 0;
         } else {
            var8 = 21;
         }

         var9 = (float)var8;
         var8 = var4;
         if (LocaleController.isRTL) {
            var8 = 21;
         }

         this.addView(var10, LayoutHelper.createFrame(20, 20.0F, var12 | 48, var9, 13.0F, (float)var8, 0.0F));
      } else {
         if (LocaleController.isRTL) {
            var12 = 5;
         } else {
            var12 = 3;
         }

         if (LocaleController.isRTL) {
            var8 = 15;
         } else {
            var8 = 21;
         }

         var9 = (float)var8;
         var8 = var5;
         if (LocaleController.isRTL) {
            var8 = 21;
         }

         this.addView(var3, LayoutHelper.createFrame(-1, 30.0F, var12 | 48, var9, 11.0F, (float)var8, 0.0F));
      }

      this.nameTextView = new TextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTextSize(1, 16.0F);
      this.nameTextView.setLines(1);
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.nameTextView.setMaxLines(1);
      this.nameTextView.setSingleLine(true);
      this.nameTextView.setEllipsize(TruncateAt.END);
      TextView var14 = this.nameTextView;
      if (LocaleController.isRTL) {
         var12 = 5;
      } else {
         var12 = 3;
      }

      var14.setGravity(var12 | 48);
      this.onlineTextView = new TextView(var1);
      this.onlineTextView.setTextSize(1, 14.0F);
      var14 = this.onlineTextView;
      if (LocaleController.isRTL) {
         var12 = 3;
      } else {
         var12 = 5;
      }

      var14.setGravity(var12 | 48);
      if (LocaleController.isRTL) {
         var3.addView(this.onlineTextView, LayoutHelper.createLinear(-2, -1, 51, 0, 2, 0, 0));
         var3.addView(this.nameTextView, LayoutHelper.createLinear(0, -1, 1.0F, 53, 10, 0, 0, 0));
      } else {
         var3.addView(this.nameTextView, LayoutHelper.createLinear(0, -1, 1.0F, 51, 0, 0, 10, 0));
         var3.addView(this.onlineTextView, LayoutHelper.createLinear(-2, -1, 53, 0, 2, 0, 0));
      }

      this.detailTextView = new TextView(var1);
      this.detailTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.detailTextView.setTextSize(1, 14.0F);
      this.detailTextView.setLines(1);
      this.detailTextView.setMaxLines(1);
      this.detailTextView.setSingleLine(true);
      this.detailTextView.setEllipsize(TruncateAt.END);
      TextView var13 = this.detailTextView;
      if (LocaleController.isRTL) {
         var12 = 5;
      } else {
         var12 = 3;
      }

      var13.setGravity(var12 | 48);
      var13 = this.detailTextView;
      if (LocaleController.isRTL) {
         var12 = 5;
      } else {
         var12 = 3;
      }

      this.addView(var13, LayoutHelper.createFrame(-1, -2.0F, var12 | 48, 21.0F, 36.0F, 21.0F, 0.0F));
      this.detailExTextView = new TextView(var1);
      this.detailExTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
      this.detailExTextView.setTextSize(1, 14.0F);
      this.detailExTextView.setLines(1);
      this.detailExTextView.setMaxLines(1);
      this.detailExTextView.setSingleLine(true);
      this.detailExTextView.setEllipsize(TruncateAt.END);
      TextView var11 = this.detailExTextView;
      if (LocaleController.isRTL) {
         var12 = 5;
      } else {
         var12 = 3;
      }

      var11.setGravity(var12 | 48);
      var11 = this.detailExTextView;
      if (LocaleController.isRTL) {
         var12 = var6;
      } else {
         var12 = 3;
      }

      this.addView(var11, LayoutHelper.createFrame(-1, -2.0F, var12 | 48, 21.0F, 59.0F, 21.0F, 0.0F));
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
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(90.0F) + this.needDivider, 1073741824));
   }

   public void setSession(TLObject var1, boolean var2) {
      this.needDivider = var2;
      if (var1 instanceof TLRPC.TL_authorization) {
         TLRPC.TL_authorization var5 = (TLRPC.TL_authorization)var1;
         this.nameTextView.setText(String.format(Locale.US, "%s %s", var5.app_name, var5.app_version));
         if ((var5.flags & 1) != 0) {
            this.setTag("windowBackgroundWhiteValueText");
            this.onlineTextView.setText(LocaleController.getString("Online", 2131560100));
            this.onlineTextView.setTextColor(Theme.getColor("windowBackgroundWhiteValueText"));
         } else {
            this.setTag("windowBackgroundWhiteGrayText3");
            this.onlineTextView.setText(LocaleController.stringForMessageListDate((long)var5.date_active));
            this.onlineTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
         }

         StringBuilder var3 = new StringBuilder();
         if (var5.ip.length() != 0) {
            var3.append(var5.ip);
         }

         if (var5.country.length() != 0) {
            if (var3.length() != 0) {
               var3.append(" ");
            }

            var3.append("— ");
            var3.append(var5.country);
         }

         this.detailExTextView.setText(var3);
         var3 = new StringBuilder();
         if (var5.device_model.length() != 0) {
            if (var3.length() != 0) {
               var3.append(", ");
            }

            var3.append(var5.device_model);
         }

         if (var5.system_version.length() != 0 || var5.platform.length() != 0) {
            if (var3.length() != 0) {
               var3.append(", ");
            }

            if (var5.platform.length() != 0) {
               var3.append(var5.platform);
            }

            if (var5.system_version.length() != 0) {
               if (var5.platform.length() != 0) {
                  var3.append(" ");
               }

               var3.append(var5.system_version);
            }
         }

         if (!var5.official_app) {
            if (var3.length() != 0) {
               var3.append(", ");
            }

            var3.append(LocaleController.getString("UnofficialApp", 2131560940));
            var3.append(" (ID: ");
            var3.append(var5.api_id);
            var3.append(")");
         }

         this.detailTextView.setText(var3);
      } else if (var1 instanceof TLRPC.TL_webAuthorization) {
         TLRPC.TL_webAuthorization var7 = (TLRPC.TL_webAuthorization)var1;
         TLRPC.User var4 = MessagesController.getInstance(this.currentAccount).getUser(var7.bot_id);
         this.nameTextView.setText(var7.domain);
         String var6;
         if (var4 != null) {
            this.avatarDrawable.setInfo(var4);
            var6 = UserObject.getFirstName(var4);
            this.imageView.setImage((ImageLocation)ImageLocation.getForUser(var4, false), "50_50", (Drawable)this.avatarDrawable, (Object)var4);
         } else {
            var6 = "";
         }

         this.setTag("windowBackgroundWhiteGrayText3");
         this.onlineTextView.setText(LocaleController.stringForMessageListDate((long)var7.date_active));
         this.onlineTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
         StringBuilder var8 = new StringBuilder();
         if (var7.ip.length() != 0) {
            var8.append(var7.ip);
         }

         if (var7.region.length() != 0) {
            if (var8.length() != 0) {
               var8.append(" ");
            }

            var8.append("— ");
            var8.append(var7.region);
         }

         this.detailExTextView.setText(var8);
         var8 = new StringBuilder();
         if (!TextUtils.isEmpty(var6)) {
            var8.append(var6);
         }

         if (var7.browser.length() != 0) {
            if (var8.length() != 0) {
               var8.append(", ");
            }

            var8.append(var7.browser);
         }

         if (var7.platform.length() != 0) {
            if (var8.length() != 0) {
               var8.append(", ");
            }

            var8.append(var7.platform);
         }

         this.detailTextView.setText(var8);
      }

   }
}
