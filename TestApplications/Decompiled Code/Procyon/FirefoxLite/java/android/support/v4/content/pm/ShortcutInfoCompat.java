// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.content.pm;

import android.text.TextUtils;
import android.content.pm.ShortcutInfo$Builder;
import android.content.pm.ShortcutInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.content.pm.PackageManager$NameNotFoundException;
import android.os.Parcelable;
import android.content.Intent;
import android.support.v4.graphics.drawable.IconCompat;
import android.content.Context;
import android.content.ComponentName;

public class ShortcutInfoCompat
{
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
    
    Intent addToIntent(final Intent intent) {
        intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)this.mIntents[this.mIntents.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
        if (this.mIcon == null) {
            return intent;
        }
        Drawable loadIcon = null;
        final Drawable drawable = null;
        Label_0102: {
            if (!this.mIsAlwaysBadged) {
                break Label_0102;
            }
            final PackageManager packageManager = this.mContext.getPackageManager();
            Drawable activityIcon = drawable;
            while (true) {
                if (this.mActivity == null) {
                    break Label_0081;
                }
                try {
                    activityIcon = packageManager.getActivityIcon(this.mActivity);
                    if ((loadIcon = activityIcon) == null) {
                        loadIcon = this.mContext.getApplicationInfo().loadIcon(packageManager);
                    }
                    this.mIcon.addToShortcutIntent(intent, loadIcon, this.mContext);
                    return intent;
                }
                catch (PackageManager$NameNotFoundException ex) {
                    activityIcon = drawable;
                    continue;
                }
                break;
            }
        }
    }
    
    public ShortcutInfo toShortcutInfo() {
        final ShortcutInfo$Builder setIntents = new ShortcutInfo$Builder(this.mContext, this.mId).setShortLabel(this.mLabel).setIntents(this.mIntents);
        if (this.mIcon != null) {
            setIntents.setIcon(this.mIcon.toIcon());
        }
        if (!TextUtils.isEmpty(this.mLongLabel)) {
            setIntents.setLongLabel(this.mLongLabel);
        }
        if (!TextUtils.isEmpty(this.mDisabledMessage)) {
            setIntents.setDisabledMessage(this.mDisabledMessage);
        }
        if (this.mActivity != null) {
            setIntents.setActivity(this.mActivity);
        }
        return setIntents.build();
    }
    
    public static class Builder
    {
        private final ShortcutInfoCompat mInfo;
        
        public Builder(final Context mContext, final String mId) {
            this.mInfo = new ShortcutInfoCompat();
            this.mInfo.mContext = mContext;
            this.mInfo.mId = mId;
        }
        
        public ShortcutInfoCompat build() {
            if (TextUtils.isEmpty(this.mInfo.mLabel)) {
                throw new IllegalArgumentException("Shortcut must have a non-empty label");
            }
            if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
                return this.mInfo;
            }
            throw new IllegalArgumentException("Shortcut must have an intent");
        }
        
        public Builder setIcon(final IconCompat mIcon) {
            this.mInfo.mIcon = mIcon;
            return this;
        }
        
        public Builder setIntent(final Intent intent) {
            return this.setIntents(new Intent[] { intent });
        }
        
        public Builder setIntents(final Intent[] mIntents) {
            this.mInfo.mIntents = mIntents;
            return this;
        }
        
        public Builder setShortLabel(final CharSequence mLabel) {
            this.mInfo.mLabel = mLabel;
            return this;
        }
    }
}
