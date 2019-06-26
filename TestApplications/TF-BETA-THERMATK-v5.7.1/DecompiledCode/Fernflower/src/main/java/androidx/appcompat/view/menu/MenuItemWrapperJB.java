package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.View;
import android.view.ActionProvider.VisibilityListener;
import androidx.core.internal.view.SupportMenuItem;

class MenuItemWrapperJB extends MenuItemWrapperICS {
   MenuItemWrapperJB(Context var1, SupportMenuItem var2) {
      super(var1, var2);
   }

   MenuItemWrapperICS.ActionProviderWrapper createActionProviderWrapper(ActionProvider var1) {
      return new MenuItemWrapperJB.ActionProviderWrapperJB(super.mContext, var1);
   }

   class ActionProviderWrapperJB extends MenuItemWrapperICS.ActionProviderWrapper implements VisibilityListener {
      androidx.core.view.ActionProvider.VisibilityListener mListener;

      public ActionProviderWrapperJB(Context var2, ActionProvider var3) {
         super(var2, var3);
      }

      public boolean isVisible() {
         return super.mInner.isVisible();
      }

      public void onActionProviderVisibilityChanged(boolean var1) {
         androidx.core.view.ActionProvider.VisibilityListener var2 = this.mListener;
         if (var2 != null) {
            var2.onActionProviderVisibilityChanged(var1);
         }

      }

      public View onCreateActionView(MenuItem var1) {
         return super.mInner.onCreateActionView(var1);
      }

      public boolean overridesItemVisibility() {
         return super.mInner.overridesItemVisibility();
      }

      public void setVisibilityListener(androidx.core.view.ActionProvider.VisibilityListener var1) {
         this.mListener = var1;
         ActionProvider var2 = super.mInner;
         MenuItemWrapperJB.ActionProviderWrapperJB var3;
         if (var1 != null) {
            var3 = this;
         } else {
            var3 = null;
         }

         var2.setVisibilityListener(var3);
      }
   }
}
