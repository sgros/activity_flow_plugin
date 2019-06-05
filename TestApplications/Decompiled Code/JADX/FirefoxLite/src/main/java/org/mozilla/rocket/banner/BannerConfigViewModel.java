package org.mozilla.rocket.banner;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class BannerConfigViewModel extends ViewModel {
    private MutableLiveData<String[]> bannerConfig;

    public MutableLiveData<String[]> getConfig() {
        if (this.bannerConfig == null) {
            this.bannerConfig = new MutableLiveData();
        }
        return this.bannerConfig;
    }
}