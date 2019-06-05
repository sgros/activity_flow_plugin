// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.locale;

import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import java.util.Locale;
import org.mozilla.focus.navigation.FragmentAnimationAccessor;
import android.support.v4.app.Fragment;

public abstract class LocaleAwareFragment extends Fragment implements FragmentAnimationAccessor
{
    private Locale cachedLocale;
    private Animation enterTransition;
    private Animation exitTransition;
    
    public LocaleAwareFragment() {
        this.cachedLocale = null;
    }
    
    public abstract void applyLocale();
    
    @Override
    public Animation getCustomEnterTransition() {
        return this.enterTransition;
    }
    
    @Override
    public Animation onCreateAnimation(final int n, final boolean b, final int n2) {
        Animation loadAnimation;
        if (n2 != 0) {
            loadAnimation = AnimationUtils.loadAnimation(this.getContext(), n2);
        }
        else {
            loadAnimation = null;
        }
        if (b) {
            this.enterTransition = loadAnimation;
        }
        else {
            this.exitTransition = loadAnimation;
        }
        return loadAnimation;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        LocaleManager.getInstance().correctLocale(this.getContext(), this.getResources(), this.getResources().getConfiguration());
        if (this.cachedLocale == null) {
            this.cachedLocale = Locale.getDefault();
        }
        else {
            Locale cachedLocale;
            if ((cachedLocale = LocaleManager.getInstance().getCurrentLocale(this.getActivity().getApplicationContext())) == null) {
                cachedLocale = Locale.getDefault();
            }
            if (!cachedLocale.equals(this.cachedLocale)) {
                this.cachedLocale = cachedLocale;
                this.applyLocale();
            }
        }
    }
}
