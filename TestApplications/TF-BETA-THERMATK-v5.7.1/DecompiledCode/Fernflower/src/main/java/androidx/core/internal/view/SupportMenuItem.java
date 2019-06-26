package androidx.core.internal.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.view.MenuItem;
import android.view.View;
import androidx.core.view.ActionProvider;

public interface SupportMenuItem extends MenuItem {
   boolean collapseActionView();

   boolean expandActionView();

   View getActionView();

   int getAlphabeticModifiers();

   CharSequence getContentDescription();

   ColorStateList getIconTintList();

   Mode getIconTintMode();

   int getNumericModifiers();

   ActionProvider getSupportActionProvider();

   CharSequence getTooltipText();

   boolean isActionViewExpanded();

   MenuItem setActionView(int var1);

   MenuItem setActionView(View var1);

   MenuItem setAlphabeticShortcut(char var1, int var2);

   SupportMenuItem setContentDescription(CharSequence var1);

   MenuItem setIconTintList(ColorStateList var1);

   MenuItem setIconTintMode(Mode var1);

   MenuItem setNumericShortcut(char var1, int var2);

   MenuItem setShortcut(char var1, char var2, int var3, int var4);

   void setShowAsAction(int var1);

   MenuItem setShowAsActionFlags(int var1);

   SupportMenuItem setSupportActionProvider(ActionProvider var1);

   SupportMenuItem setTooltipText(CharSequence var1);
}
