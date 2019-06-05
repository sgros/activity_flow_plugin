package org.mozilla.focus.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.mozilla.rocket.home.pinsite.PinSiteManager;
import org.mozilla.rocket.home.pinsite.PinSiteManagerKt;

public class NewFeatureNotice {
    private static NewFeatureNotice instance;
    private final boolean hasNewsPortal;
    private boolean hasShownEcShoppingLink = false;
    private final PinSiteManager pinSiteManager;
    private final SharedPreferences preferences;

    public static synchronized NewFeatureNotice getInstance(Context context) {
        NewFeatureNotice newFeatureNotice;
        synchronized (NewFeatureNotice.class) {
            if (instance == null) {
                instance = new NewFeatureNotice(context.getApplicationContext());
            }
            newFeatureNotice = instance;
        }
        return newFeatureNotice;
    }

    private NewFeatureNotice(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.hasNewsPortal = AppConfigWrapper.hasNewsPortal(context);
        this.pinSiteManager = PinSiteManagerKt.getPinSiteManager(context);
    }

    public boolean shouldShowLiteUpdate() {
        Object obj = (this.pinSiteManager.isEnabled() && this.pinSiteManager.isFirstTimeEnable()) ? 1 : null;
        if (from21to40() || from40to114() || shouldShowEcShoppingLinkOnboarding() || obj != null) {
            return true;
        }
        return false;
    }

    public boolean from21to40() {
        return 3 > getLastShownFeatureVersion();
    }

    public boolean from40to114() {
        return 4 > getLastShownFeatureVersion() && this.hasNewsPortal;
    }

    public void setLiteUpdateDidShow() {
        setFirstRunDidShow();
        setEcShoppingLinkDidShow();
        setLastShownFeatureVersion(4);
    }

    public boolean shouldShowPrivacyPolicyUpdate() {
        boolean z = false;
        if (isNewlyInstalled()) {
            setPrivacyPolicyUpdateNoticeDidShow();
            return false;
        }
        if (2 == getLastShownFeatureVersion() + 1) {
            z = true;
        }
        return z;
    }

    public void setPrivacyPolicyUpdateNoticeDidShow() {
        setLastShownFeatureVersion(2);
    }

    public boolean hasShownFirstRun() {
        return this.preferences.getBoolean("firstrun_shown", false);
    }

    public void setFirstRunDidShow() {
        this.preferences.edit().putBoolean("firstrun_shown", true).apply();
    }

    private void setEcShoppingLinkDidShow() {
        if (this.hasShownEcShoppingLink) {
            this.preferences.edit().putBoolean("ec_shoppingLink_shown", true).apply();
        }
    }

    private boolean isNewlyInstalled() {
        return !hasShownFirstRun() && getLastShownFeatureVersion() == 0;
    }

    private void setLastShownFeatureVersion(int i) {
        if (getLastShownFeatureVersion() < i) {
            this.preferences.edit().putInt("firstrun_upgrade_version", i).apply();
        }
    }

    public int getLastShownFeatureVersion() {
        return this.preferences.getInt("firstrun_upgrade_version", 0);
    }

    public boolean shouldShowEcShoppingLinkOnboarding() {
        if (!AppConfigWrapper.hasEcommerceShoppingLink() || this.preferences.getBoolean("ec_shoppingLink_shown", false)) {
            return false;
        }
        return true;
    }

    public void hasShownEcShoppingLink() {
        this.hasShownEcShoppingLink = true;
    }
}
