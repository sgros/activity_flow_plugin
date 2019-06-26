package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.LayoutHelper;

public class ChangePhoneHelpActivity extends BaseFragment {
   private ImageView imageView;
   private TextView textView1;
   private TextView textView2;

   // $FF: synthetic method
   static boolean lambda$createView$0(View var0, MotionEvent var1) {
      return true;
   }

   public View createView(Context var1) {
      String var6;
      label23: {
         super.actionBar.setBackButtonImage(2131165409);
         super.actionBar.setAllowOverlayTitle(true);
         TLRPC.User var2 = UserConfig.getInstance(super.currentAccount).getCurrentUser();
         if (var2 != null) {
            String var3 = var2.phone;
            if (var3 != null && var3.length() != 0) {
               PhoneFormat var8 = PhoneFormat.getInstance();
               StringBuilder var4 = new StringBuilder();
               var4.append("+");
               var4.append(var2.phone);
               var6 = var8.format(var4.toString());
               break label23;
            }
         }

         var6 = LocaleController.getString("NumberUnknown", 2131560096);
      }

      super.actionBar.setTitle(var6);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ChangePhoneHelpActivity.this.finishFragment();
            }

         }
      });
      super.fragmentView = new RelativeLayout(var1);
      super.fragmentView.setOnTouchListener(_$$Lambda$ChangePhoneHelpActivity$At2SQoCuyPKWr2c7pksPfUQl31M.INSTANCE);
      RelativeLayout var7 = (RelativeLayout)super.fragmentView;
      ScrollView var9 = new ScrollView(var1);
      var7.addView(var9);
      LayoutParams var10 = (LayoutParams)var9.getLayoutParams();
      var10.width = -1;
      var10.height = -2;
      var10.addRule(15, -1);
      var9.setLayoutParams(var10);
      LinearLayout var11 = new LinearLayout(var1);
      var11.setOrientation(1);
      var11.setPadding(0, AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F));
      var9.addView(var11);
      android.widget.FrameLayout.LayoutParams var12 = (android.widget.FrameLayout.LayoutParams)var11.getLayoutParams();
      var12.width = -1;
      var12.height = -2;
      var11.setLayoutParams(var12);
      this.imageView = new ImageView(var1);
      this.imageView.setImageResource(2131165740);
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("changephoneinfo_image"), Mode.MULTIPLY));
      var11.addView(this.imageView, LayoutHelper.createLinear(-2, -2, 1));
      this.textView1 = new TextView(var1);
      this.textView1.setTextSize(1, 16.0F);
      this.textView1.setGravity(1);
      this.textView1.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));

      try {
         this.textView1.setText(AndroidUtilities.replaceTags(LocaleController.getString("PhoneNumberHelp", 2131560431)));
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
         this.textView1.setText(LocaleController.getString("PhoneNumberHelp", 2131560431));
      }

      var11.addView(this.textView1, LayoutHelper.createLinear(-2, -2, 1, 20, 56, 20, 0));
      this.textView2 = new TextView(var1);
      this.textView2.setTextSize(1, 18.0F);
      this.textView2.setGravity(1);
      this.textView2.setTextColor(Theme.getColor("key_changephoneinfo_changeText"));
      this.textView2.setText(LocaleController.getString("PhoneNumberChange", 2131560429));
      this.textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textView2.setPadding(0, AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(10.0F));
      var11.addView(this.textView2, LayoutHelper.createLinear(-2, -2, 1, 20, 46, 20, 0));
      this.textView2.setOnClickListener(new _$$Lambda$ChangePhoneHelpActivity$qVZMrZGI0M_ckM3yEp535Q8q_vk(this));
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      return new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.textView1, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.textView2, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "key_changephoneinfo_changeText"), new ThemeDescription(this.imageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "changephoneinfo_image")};
   }

   // $FF: synthetic method
   public void lambda$createView$2$ChangePhoneHelpActivity(View var1) {
      if (this.getParentActivity() != null) {
         AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
         var2.setTitle(LocaleController.getString("AppName", 2131558635));
         var2.setMessage(LocaleController.getString("PhoneNumberAlert", 2131560428));
         var2.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$ChangePhoneHelpActivity$qA8BPNV0fO4W8yi1slKpuIUhZp4(this));
         var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showDialog(var2.create());
      }
   }

   // $FF: synthetic method
   public void lambda$null$1$ChangePhoneHelpActivity(DialogInterface var1, int var2) {
      this.presentFragment(new ChangePhoneActivity(), true);
   }
}
