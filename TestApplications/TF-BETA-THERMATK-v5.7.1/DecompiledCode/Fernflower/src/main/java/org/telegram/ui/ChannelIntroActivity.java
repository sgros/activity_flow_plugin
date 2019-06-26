package org.telegram.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;

public class ChannelIntroActivity extends BaseFragment {
   private TextView createChannelText;
   private TextView descriptionText;
   private ImageView imageView;
   private TextView whatIsChannelText;

   // $FF: synthetic method
   static boolean lambda$createView$0(View var0, MotionEvent var1) {
      return true;
   }

   public View createView(Context var1) {
      super.actionBar.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setItemsColor(Theme.getColor("windowBackgroundWhiteGrayText2"), false);
      super.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarWhiteSelector"), false);
      super.actionBar.setCastShadows(false);
      if (!AndroidUtilities.isTablet()) {
         super.actionBar.showActionModeTop();
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ChannelIntroActivity.this.finishFragment();
            }

         }
      });
      super.fragmentView = new ViewGroup(var1) {
         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            var2 = var4 - var2;
            var3 = var5 - var3;
            float var6;
            if (var4 > var5) {
               var6 = (float)var3;
               var3 = (int)(0.05F * var6);
               ChannelIntroActivity.this.imageView.layout(0, var3, ChannelIntroActivity.this.imageView.getMeasuredWidth(), ChannelIntroActivity.this.imageView.getMeasuredHeight() + var3);
               float var7 = (float)var2;
               var2 = (int)(0.4F * var7);
               var3 = (int)(0.14F * var6);
               ChannelIntroActivity.this.whatIsChannelText.layout(var2, var3, ChannelIntroActivity.this.whatIsChannelText.getMeasuredWidth() + var2, ChannelIntroActivity.this.whatIsChannelText.getMeasuredHeight() + var3);
               var3 = (int)(0.61F * var6);
               ChannelIntroActivity.this.createChannelText.layout(var2, var3, ChannelIntroActivity.this.createChannelText.getMeasuredWidth() + var2, ChannelIntroActivity.this.createChannelText.getMeasuredHeight() + var3);
               var3 = (int)(var7 * 0.45F);
               var2 = (int)(var6 * 0.31F);
               ChannelIntroActivity.this.descriptionText.layout(var3, var2, ChannelIntroActivity.this.descriptionText.getMeasuredWidth() + var3, ChannelIntroActivity.this.descriptionText.getMeasuredHeight() + var2);
            } else {
               var6 = (float)var3;
               var3 = (int)(var6 * 0.05F);
               ChannelIntroActivity.this.imageView.layout(0, var3, ChannelIntroActivity.this.imageView.getMeasuredWidth(), ChannelIntroActivity.this.imageView.getMeasuredHeight() + var3);
               var3 = (int)(0.59F * var6);
               ChannelIntroActivity.this.whatIsChannelText.layout(0, var3, ChannelIntroActivity.this.whatIsChannelText.getMeasuredWidth(), ChannelIntroActivity.this.whatIsChannelText.getMeasuredHeight() + var3);
               var3 = (int)(0.68F * var6);
               var2 = (int)((float)var2 * 0.05F);
               ChannelIntroActivity.this.descriptionText.layout(var2, var3, ChannelIntroActivity.this.descriptionText.getMeasuredWidth() + var2, ChannelIntroActivity.this.descriptionText.getMeasuredHeight() + var3);
               var2 = (int)(var6 * 0.86F);
               ChannelIntroActivity.this.createChannelText.layout(0, var2, ChannelIntroActivity.this.createChannelText.getMeasuredWidth(), ChannelIntroActivity.this.createChannelText.getMeasuredHeight() + var2);
            }

         }

         protected void onMeasure(int var1, int var2) {
            var1 = MeasureSpec.getSize(var1);
            int var3 = MeasureSpec.getSize(var2);
            if (var1 > var3) {
               ImageView var4 = ChannelIntroActivity.this.imageView;
               float var5 = (float)var1;
               var4.measure(MeasureSpec.makeMeasureSpec((int)(0.45F * var5), 1073741824), MeasureSpec.makeMeasureSpec((int)((float)var3 * 0.78F), 1073741824));
               TextView var6 = ChannelIntroActivity.this.whatIsChannelText;
               var2 = (int)(0.6F * var5);
               var6.measure(MeasureSpec.makeMeasureSpec(var2, 1073741824), MeasureSpec.makeMeasureSpec(var3, 0));
               ChannelIntroActivity.this.descriptionText.measure(MeasureSpec.makeMeasureSpec((int)(var5 * 0.5F), 1073741824), MeasureSpec.makeMeasureSpec(var3, 0));
               ChannelIntroActivity.this.createChannelText.measure(MeasureSpec.makeMeasureSpec(var2, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0F), 1073741824));
            } else {
               ChannelIntroActivity.this.imageView.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec((int)((float)var3 * 0.44F), 1073741824));
               ChannelIntroActivity.this.whatIsChannelText.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(var3, 0));
               ChannelIntroActivity.this.descriptionText.measure(MeasureSpec.makeMeasureSpec((int)((float)var1 * 0.9F), 1073741824), MeasureSpec.makeMeasureSpec(var3, 0));
               ChannelIntroActivity.this.createChannelText.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0F), 1073741824));
            }

            this.setMeasuredDimension(var1, var3);
         }
      };
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      ViewGroup var2 = (ViewGroup)super.fragmentView;
      var2.setOnTouchListener(_$$Lambda$ChannelIntroActivity$0c8d8mysDNu6O6Ps8WT2KcKJBXc.INSTANCE);
      this.imageView = new ImageView(var1);
      this.imageView.setImageResource(2131165340);
      this.imageView.setScaleType(ScaleType.FIT_CENTER);
      var2.addView(this.imageView);
      this.whatIsChannelText = new TextView(var1);
      this.whatIsChannelText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.whatIsChannelText.setGravity(1);
      this.whatIsChannelText.setTextSize(1, 24.0F);
      this.whatIsChannelText.setText(LocaleController.getString("ChannelAlertTitle", 2131558931));
      var2.addView(this.whatIsChannelText);
      this.descriptionText = new TextView(var1);
      this.descriptionText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
      this.descriptionText.setGravity(1);
      this.descriptionText.setTextSize(1, 16.0F);
      this.descriptionText.setText(LocaleController.getString("ChannelAlertText", 2131558930));
      var2.addView(this.descriptionText);
      this.createChannelText = new TextView(var1);
      this.createChannelText.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText5"));
      this.createChannelText.setGravity(17);
      this.createChannelText.setTextSize(1, 16.0F);
      this.createChannelText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.createChannelText.setText(LocaleController.getString("ChannelAlertCreate", 2131558929));
      var2.addView(this.createChannelText);
      this.createChannelText.setOnClickListener(new _$$Lambda$ChannelIntroActivity$M58NjDpXQDsy4vkbKcDWC5YHj9o(this));
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      return new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarWhiteSelector"), new ThemeDescription(this.whatIsChannelText, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.descriptionText, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"), new ThemeDescription(this.createChannelText, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText5")};
   }

   // $FF: synthetic method
   public void lambda$createView$1$ChannelIntroActivity(View var1) {
      Bundle var2 = new Bundle();
      var2.putInt("step", 0);
      this.presentFragment(new ChannelCreateActivity(var2), true);
   }
}
