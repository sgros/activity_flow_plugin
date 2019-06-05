package org.mozilla.focus.locale;

import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import java.util.Locale;
import org.mozilla.focus.navigation.FragmentAnimationAccessor;

public abstract class LocaleAwareFragment extends Fragment implements FragmentAnimationAccessor {
   private Locale cachedLocale = null;
   private Animation enterTransition;
   private Animation exitTransition;

   public abstract void applyLocale();

   public Animation getCustomEnterTransition() {
      return this.enterTransition;
   }

   public Animation onCreateAnimation(int var1, boolean var2, int var3) {
      Animation var4;
      if (var3 != 0) {
         var4 = AnimationUtils.loadAnimation(this.getContext(), var3);
      } else {
         var4 = null;
      }

      if (var2) {
         this.enterTransition = var4;
      } else {
         this.exitTransition = var4;
      }

      return var4;
   }

   public void onResume() {
      super.onResume();
      LocaleManager.getInstance().correctLocale(this.getContext(), this.getResources(), this.getResources().getConfiguration());
      if (this.cachedLocale == null) {
         this.cachedLocale = Locale.getDefault();
      } else {
         Locale var1 = LocaleManager.getInstance().getCurrentLocale(this.getActivity().getApplicationContext());
         Locale var2 = var1;
         if (var1 == null) {
            var2 = Locale.getDefault();
         }

         if (!var2.equals(this.cachedLocale)) {
            this.cachedLocale = var2;
            this.applyLocale();
         }
      }

   }
}
