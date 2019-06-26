// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.view.menu;

import android.view.SubMenu;
import androidx.core.internal.view.SupportSubMenu;
import android.os.Build$VERSION;
import android.view.MenuItem;
import androidx.core.internal.view.SupportMenuItem;
import android.content.Context;

public final class MenuWrapperFactory
{
    public static MenuItem wrapSupportMenuItem(final Context context, final SupportMenuItem supportMenuItem) {
        if (Build$VERSION.SDK_INT >= 16) {
            return (MenuItem)new MenuItemWrapperJB(context, supportMenuItem);
        }
        return (MenuItem)new MenuItemWrapperICS(context, supportMenuItem);
    }
    
    public static SubMenu wrapSupportSubMenu(final Context context, final SupportSubMenu supportSubMenu) {
        return (SubMenu)new SubMenuWrapperICS(context, supportSubMenu);
    }
}
