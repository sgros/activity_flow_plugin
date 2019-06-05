// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import org.mozilla.rocket.home.pinsite.PinSiteManagerKt;
import android.preference.PreferenceManager;
import android.content.Context;
import android.content.SharedPreferences;
import org.mozilla.rocket.home.pinsite.PinSiteManager;

public class NewFeatureNotice
{
    private static NewFeatureNotice instance;
    private final boolean hasNewsPortal;
    private boolean hasShownEcShoppingLink;
    private final PinSiteManager pinSiteManager;
    private final SharedPreferences preferences;
    
    private NewFeatureNotice(final Context context) {
        this.hasShownEcShoppingLink = false;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.hasNewsPortal = AppConfigWrapper.hasNewsPortal(context);
        this.pinSiteManager = PinSiteManagerKt.getPinSiteManager(context);
    }
    
    public static NewFeatureNotice getInstance(final Context context) {
        synchronized (NewFeatureNotice.class) {
            if (NewFeatureNotice.instance == null) {
                NewFeatureNotice.instance = new NewFeatureNotice(context.getApplicationContext());
            }
            return NewFeatureNotice.instance;
        }
    }
    
    private boolean isNewlyInstalled() {
        return !this.hasShownFirstRun() && this.getLastShownFeatureVersion() == 0;
    }
    
    private void setEcShoppingLinkDidShow() {
        if (!this.hasShownEcShoppingLink) {
            return;
        }
        this.preferences.edit().putBoolean("ec_shoppingLink_shown", true).apply();
    }
    
    private void setLastShownFeatureVersion(final int n) {
        if (this.getLastShownFeatureVersion() >= n) {
            return;
        }
        this.preferences.edit().putInt("firstrun_upgrade_version", n).apply();
    }
    
    public boolean from21to40() {
        return 3 > this.getLastShownFeatureVersion();
    }
    
    public boolean from40to114() {
        return 4 > this.getLastShownFeatureVersion() && this.hasNewsPortal;
    }
    
    public int getLastShownFeatureVersion() {
        return this.preferences.getInt("firstrun_upgrade_version", 0);
    }
    
    public void hasShownEcShoppingLink() {
        this.hasShownEcShoppingLink = true;
    }
    
    public boolean hasShownFirstRun() {
        return this.preferences.getBoolean("firstrun_shown", false);
    }
    
    public void setFirstRunDidShow() {
        this.preferences.edit().putBoolean("firstrun_shown", true).apply();
    }
    
    public void setLiteUpdateDidShow() {
        this.setFirstRunDidShow();
        this.setEcShoppingLinkDidShow();
        this.setLastShownFeatureVersion(4);
    }
    
    public void setPrivacyPolicyUpdateNoticeDidShow() {
        this.setLastShownFeatureVersion(2);
    }
    
    public boolean shouldShowEcShoppingLinkOnboarding() {
        final boolean hasEcommerceShoppingLink = AppConfigWrapper.hasEcommerceShoppingLink();
        boolean b = false;
        if (hasEcommerceShoppingLink) {
            b = b;
            if (!this.preferences.getBoolean("ec_shoppingLink_shown", false)) {
                b = true;
            }
        }
        return b;
    }
    
    public boolean shouldShowLiteUpdate() {
        final boolean enabled = this.pinSiteManager.isEnabled();
        final boolean b = true;
        final boolean b2 = enabled && this.pinSiteManager.isFirstTimeEnable();
        boolean b3 = b;
        if (!this.from21to40()) {
            b3 = b;
            if (!this.from40to114()) {
                b3 = b;
                if (!this.shouldShowEcShoppingLinkOnboarding()) {
                    b3 = (b2 && b);
                }
            }
        }
        return b3;
    }
    
    public boolean shouldShowPrivacyPolicyUpdate() {
        final boolean newlyInstalled = this.isNewlyInstalled();
        boolean b = false;
        if (newlyInstalled) {
            this.setPrivacyPolicyUpdateNoticeDidShow();
            return false;
        }
        if (2 == this.getLastShownFeatureVersion() + 1) {
            b = true;
        }
        return b;
    }
}
