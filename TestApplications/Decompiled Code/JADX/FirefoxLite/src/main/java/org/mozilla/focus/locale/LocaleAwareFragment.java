package org.mozilla.focus.locale;

import android.support.p001v4.app.Fragment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import java.util.Locale;
import org.mozilla.focus.navigation.FragmentAnimationAccessor;

public abstract class LocaleAwareFragment extends Fragment implements FragmentAnimationAccessor {
    private Locale cachedLocale = null;
    private Animation enterTransition;
    private Animation exitTransition;

    public abstract void applyLocale();

    public void onResume() {
        super.onResume();
        LocaleManager.getInstance().correctLocale(getContext(), getResources(), getResources().getConfiguration());
        if (this.cachedLocale == null) {
            this.cachedLocale = Locale.getDefault();
            return;
        }
        Locale currentLocale = LocaleManager.getInstance().getCurrentLocale(getActivity().getApplicationContext());
        if (currentLocale == null) {
            currentLocale = Locale.getDefault();
        }
        if (!currentLocale.equals(this.cachedLocale)) {
            this.cachedLocale = currentLocale;
            applyLocale();
        }
    }

    public Animation onCreateAnimation(int i, boolean z, int i2) {
        Animation loadAnimation = i2 != 0 ? AnimationUtils.loadAnimation(getContext(), i2) : null;
        if (z) {
            this.enterTransition = loadAnimation;
        } else {
            this.exitTransition = loadAnimation;
        }
        return loadAnimation;
    }

    public Animation getCustomEnterTransition() {
        return this.enterTransition;
    }
}
