package android.support.v4.content.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.IconCompat;
import android.text.TextUtils;

public class ShortcutInfoCompat {
   ComponentName mActivity;
   Context mContext;
   CharSequence mDisabledMessage;
   IconCompat mIcon;
   String mId;
   Intent[] mIntents;
   boolean mIsAlwaysBadged;
   CharSequence mLabel;
   CharSequence mLongLabel;

   ShortcutInfoCompat() {
   }

   Intent addToIntent(Intent var1) {
      var1.putExtra("android.intent.extra.shortcut.INTENT", this.mIntents[this.mIntents.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
      if (this.mIcon != null) {
         Drawable var2 = null;
         Object var3 = null;
         if (this.mIsAlwaysBadged) {
            PackageManager var4 = this.mContext.getPackageManager();
            Drawable var5 = (Drawable)var3;
            if (this.mActivity != null) {
               try {
                  var5 = var4.getActivityIcon(this.mActivity);
               } catch (NameNotFoundException var6) {
                  var5 = (Drawable)var3;
               }
            }

            var2 = var5;
            if (var5 == null) {
               var2 = this.mContext.getApplicationInfo().loadIcon(var4);
            }
         }

         this.mIcon.addToShortcutIntent(var1, var2, this.mContext);
      }

      return var1;
   }

   public ShortcutInfo toShortcutInfo() {
      android.content.pm.ShortcutInfo.Builder var1 = (new android.content.pm.ShortcutInfo.Builder(this.mContext, this.mId)).setShortLabel(this.mLabel).setIntents(this.mIntents);
      if (this.mIcon != null) {
         var1.setIcon(this.mIcon.toIcon());
      }

      if (!TextUtils.isEmpty(this.mLongLabel)) {
         var1.setLongLabel(this.mLongLabel);
      }

      if (!TextUtils.isEmpty(this.mDisabledMessage)) {
         var1.setDisabledMessage(this.mDisabledMessage);
      }

      if (this.mActivity != null) {
         var1.setActivity(this.mActivity);
      }

      return var1.build();
   }

   public static class Builder {
      private final ShortcutInfoCompat mInfo = new ShortcutInfoCompat();

      public Builder(Context var1, String var2) {
         this.mInfo.mContext = var1;
         this.mInfo.mId = var2;
      }

      public ShortcutInfoCompat build() {
         if (!TextUtils.isEmpty(this.mInfo.mLabel)) {
            if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
               return this.mInfo;
            } else {
               throw new IllegalArgumentException("Shortcut must have an intent");
            }
         } else {
            throw new IllegalArgumentException("Shortcut must have a non-empty label");
         }
      }

      public ShortcutInfoCompat.Builder setIcon(IconCompat var1) {
         this.mInfo.mIcon = var1;
         return this;
      }

      public ShortcutInfoCompat.Builder setIntent(Intent var1) {
         return this.setIntents(new Intent[]{var1});
      }

      public ShortcutInfoCompat.Builder setIntents(Intent[] var1) {
         this.mInfo.mIntents = var1;
         return this;
      }

      public ShortcutInfoCompat.Builder setShortLabel(CharSequence var1) {
         this.mInfo.mLabel = var1;
         return this;
      }
   }
}
