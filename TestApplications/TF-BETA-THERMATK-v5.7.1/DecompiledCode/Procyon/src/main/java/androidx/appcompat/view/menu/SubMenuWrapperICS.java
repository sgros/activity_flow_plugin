// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.view.menu;

import android.view.View;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import androidx.core.internal.view.SupportMenu;
import androidx.core.internal.view.SupportSubMenu;
import android.content.Context;
import android.view.SubMenu;

class SubMenuWrapperICS extends MenuWrapperICS implements SubMenu
{
    SubMenuWrapperICS(final Context context, final SupportSubMenu supportSubMenu) {
        super(context, supportSubMenu);
    }
    
    public void clearHeader() {
        ((SubMenu)this.getWrappedObject()).clearHeader();
    }
    
    public MenuItem getItem() {
        return this.getMenuItemWrapper(((SubMenu)this.getWrappedObject()).getItem());
    }
    
    public SupportSubMenu getWrappedObject() {
        return (SupportSubMenu)super.mWrappedObject;
    }
    
    public SubMenu setHeaderIcon(final int headerIcon) {
        ((SubMenu)this.getWrappedObject()).setHeaderIcon(headerIcon);
        return (SubMenu)this;
    }
    
    public SubMenu setHeaderIcon(final Drawable headerIcon) {
        ((SubMenu)this.getWrappedObject()).setHeaderIcon(headerIcon);
        return (SubMenu)this;
    }
    
    public SubMenu setHeaderTitle(final int headerTitle) {
        ((SubMenu)this.getWrappedObject()).setHeaderTitle(headerTitle);
        return (SubMenu)this;
    }
    
    public SubMenu setHeaderTitle(final CharSequence headerTitle) {
        ((SubMenu)this.getWrappedObject()).setHeaderTitle(headerTitle);
        return (SubMenu)this;
    }
    
    public SubMenu setHeaderView(final View headerView) {
        ((SubMenu)this.getWrappedObject()).setHeaderView(headerView);
        return (SubMenu)this;
    }
    
    public SubMenu setIcon(final int icon) {
        ((SubMenu)this.getWrappedObject()).setIcon(icon);
        return (SubMenu)this;
    }
    
    public SubMenu setIcon(final Drawable icon) {
        ((SubMenu)this.getWrappedObject()).setIcon(icon);
        return (SubMenu)this;
    }
}
