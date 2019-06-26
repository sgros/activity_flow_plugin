package androidx.appcompat.view.menu;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.core.internal.view.SupportMenuItem;
import androidx.core.internal.view.SupportSubMenu;

public final class MenuWrapperFactory {
   public static MenuItem wrapSupportMenuItem(Context var0, SupportMenuItem var1) {
      return (MenuItem)(VERSION.SDK_INT >= 16 ? new MenuItemWrapperJB(var0, var1) : new MenuItemWrapperICS(var0, var1));
   }

   public static SubMenu wrapSupportSubMenu(Context var0, SupportSubMenu var1) {
      return new SubMenuWrapperICS(var0, var1);
   }
}
