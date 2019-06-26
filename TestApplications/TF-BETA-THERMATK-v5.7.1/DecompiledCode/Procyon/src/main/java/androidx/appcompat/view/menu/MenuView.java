// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.view.menu;

public interface MenuView
{
    public interface ItemView
    {
        MenuItemImpl getItemData();
        
        void initialize(final MenuItemImpl p0, final int p1);
        
        boolean prefersCondensedTitle();
    }
}
